package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ShooterMotor;

public class ShootOnMove extends Command {
    private final Limelight m_limelight;
    private final ShooterMotor s_ShooterMotor;
    private final CommandSwerveDrivetrain drivetrain;

    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    public ShootOnMove(Limelight limelight, ShooterMotor shooterMotor, CommandSwerveDrivetrain drivetrain) {
        m_limelight = limelight;
        s_ShooterMotor =shooterMotor;
        this.drivetrain = drivetrain;
        addRequirements(s_ShooterMotor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Pose2d hubpose = m_limelight.getHubPoseForAlliance();
        SmartDashboard.putNumber("Hub Pose X", hubpose.getX());
        SmartDashboard.putNumber("Hub Pose Y", hubpose.getY());

        Pose2d robotPose = drivetrain.getState().Pose;
        SmartDashboard.putNumber("Robot Pose X", robotPose.getX());
        SmartDashboard.putNumber("Robot Pose Y", robotPose.getY());

        double distanceToHub = m_limelight.getDistanceToHub(robotPose, hubpose);
        SmartDashboard.putNumber("Distance to Hub", distanceToHub);
        
    // Calculate shooter speed based on distance (base setpoint)
    double shooterSpeed = calculateShooterSpeed(distanceToHub);

    // --- Velocity & time-of-flight compensation ---
    // Tunable constants (adjust these on the dashboard or here while tuning)
    final double NOMINAL_PROJECTILE_SPEED_MPS = 8.0; // m/s - estimate of ball speed for the base shooter unit
    final double DERIV_EPS = 0.05; // meters for numerical derivative
    final double MIN_SHOOTER_SETPOINT = 0.0;
    final double MAX_SHOOTER_SETPOINT = 150.0;

    // Get robot chassis speeds (robot-relative) and convert to field-relative
    ChassisSpeeds speeds = drivetrain.getState().Speeds;
    double vx = speeds.vxMetersPerSecond; // forward (robot frame)
    double vy = speeds.vyMetersPerSecond; // left (robot frame)
    Rotation2d rot = robotPose.getRotation();
    double cos = rot.getCos();
    double sin = rot.getSin();
    double fieldVx = vx * cos - vy * sin;
    double fieldVy = vx * sin + vy * cos;

    // Unit vector from robot to hub
    double dhx = hubpose.getX() - robotPose.getX();
    double dhy = hubpose.getY() - robotPose.getY();
    double dist = Math.max(distanceToHub, 1e-6);
    double ux = dhx / dist;
    double uy = dhy / dist;

    // Robot velocity component along the line to the hub (positive -> toward hub)
    double relSpeedAlongLine = fieldVx * ux + fieldVy * uy;

    // Estimate time of flight (simple approximation)
    double estimatedProjectileSpeed = Math.max(NOMINAL_PROJECTILE_SPEED_MPS, 0.1);
    double timeOfFlight = dist / estimatedProjectileSpeed;

    // Estimate how much the robot will change distance during flight
    double deltaDistanceDuringFlight = relSpeedAlongLine * timeOfFlight;

    // Numerical derivative d(setpoint)/d(distance)
    double dPlus = calculateShooterSpeed(distanceToHub + DERIV_EPS);
    double dMinus = calculateShooterSpeed(Math.max(0.0, distanceToHub - DERIV_EPS));
    double derivative = (dPlus - dMinus) / (2.0 * DERIV_EPS);

    // Compute RPM (or setpoint units) offset to compensate for robot motion
    double deltaSetpoint = derivative * deltaDistanceDuringFlight;

    double compensatedSetpoint = shooterSpeed + deltaSetpoint;
    // Clamp to safe bounds
    compensatedSetpoint = Math.max(MIN_SHOOTER_SETPOINT, Math.min(MAX_SHOOTER_SETPOINT, compensatedSetpoint));

    // Use compensated setpoint
    s_ShooterMotor.setShooterSpeed(compensatedSetpoint, 1000);
    SmartDashboard.putNumber("Calculated Shooter Speed", shooterSpeed);
    SmartDashboard.putNumber("Compensated Shooter Speed", compensatedSetpoint);
    SmartDashboard.putNumber("Rel Speed Along Line (m/s)", relSpeedAlongLine);
    SmartDashboard.putNumber("Estimated TOF (s)", timeOfFlight);
    SmartDashboard.putNumber("Delta Distance During Flight (m)", deltaDistanceDuringFlight);
    SmartDashboard.putNumber("dSetpoint/dDistance", derivative);

        Boolean shooterAtSetpoint = s_ShooterMotor.ShooterAtSetpoint(compensatedSetpoint, 3);
        SmartDashboard.putBoolean("Shooter Ready", shooterAtSetpoint);
    }

    /**
     * Calculates shooter speed based on distance to hub using linear interpolation.
     * Smoothly adjusts speed as distance changes based on your calibrated datapoints.
     * 
     * @param distance Distance to hub in meters
     * @return Shooter speed in RPM
     */
    private double calculateShooterSpeed(double distance) {
        // Calibration points from your tested ranges:
        // 0.0-1.7m  → 34.5 RPM
        // 1.7-2.1m  → 37.8 RPM
        // 2.1-3.1m  → 52.0 RPM
        // 3.1-3.6m  → 100.0 RPM
        
        if (distance < 0) {
            return 0;
        }
        if (distance <= 1.4) {
            // Linear interpolation from 0m to 1.4m: 0 RPM → 34.5 RPM
            return 34.5;
        }
        if (distance <= 2.1) {
            // Linear interpolation from 1.7m to 2.1m: 34.5 RPM → 37.8 RPM
            return 34.5 + (37.8 - 34.5) * ((distance - 1.4) / (2.1 - 1.4));
        }
        if (distance <= 2.6) {
            // Linear interpolation from 1.7m to 2.1m: 34.5 RPM → 43 RPM
            return 37.8 + (43 - 37.8) * ((distance - 2.1) / (2.6 - 2.1));
        }
        if (distance <= 3.1) {
            // Linear interpolation from 2.1m to 3.1m: 43 RPM → 52.0 RPM
            return 42 + (52.0 - 43) * ((distance - 2.6) / (3.1 - 2.6));
            
        }
        if (distance <= 3.6) {
            // Linear interpolation from 3.1m to 3.6m: 52.0 RPM → 100.0 RPM
            return 52.0 + (100.0 - 52.0) * ((distance - 3.1) / (3.6 - 3.1));
        }
        // Beyond 3.6m, use max speed
        return 100.0;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        s_ShooterMotor.ShooterMotorRun(0);
        SmartDashboard.putBoolean("Shooter Ready", false);

    }

}