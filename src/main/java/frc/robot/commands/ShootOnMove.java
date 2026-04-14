package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.geometry.Pose2d;
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
        addRequirements(limelight, s_ShooterMotor);
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
        
        // Calculate shooter speed based on distance
        double shooterSpeed = calculateShooterSpeed(distanceToHub);
        s_ShooterMotor.setShooterSpeed(shooterSpeed, 1000);
        SmartDashboard.putNumber("Calculated Shooter Speed", shooterSpeed);
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
        if (distance <= 3.1) {
            // Linear interpolation from 2.1m to 3.1m: 37.8 RPM → 52.0 RPM
            return 37.8 + (52.0 - 37.8) * ((distance - 2.1) / (3.1 - 2.1));
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
    }

//     // OLD CODE - DISCRETE RANGE APPROACH (kept for reference)
//     /*
//     @Override
//     public void executeOld() {
//         Pose2d hubpose = m_limelight.getHubPoseForAlliance();
//         SmartDashboard.putNumber("Hub Pose X", hubpose.getX());
//         SmartDashboard.putNumber("Hub Pose Y", hubpose.getY());

//         Pose2d robotPose = drivetrain.getState().Pose;
//         SmartDashboard.putNumber("Robot Pose X", robotPose.getX());
//         SmartDashboard.putNumber("Robot Pose Y", robotPose.getY());

//         double distanceToHub = m_limelight.getDistanceToHub(robotPose, hubpose);
//         SmartDashboard.putNumber("Distance to Hub", distanceToHub);
//         if (0 < distanceToHub && distanceToHub < 1.7) { //10ft in meters
//             s_ShooterMotor.setShooterSpeed(34.5, 1000);
//         }
//         if (1.7 < distanceToHub && distanceToHub < 2.1) { //10ft in meters
//             s_ShooterMotor.setShooterSpeed(37.8, 1000);
//         }
//         if (2.1 < distanceToHub && distanceToHub < 3.1) { //10ft in meters
//             s_ShooterMotor.setShooterSpeed(52, 1000);
//         }
//         if (3.1 < distanceToHub && distanceToHub < 3.6) { //10ft in meters
//             s_ShooterMotor.setShooterSpeed(100, 1000);
//         }
//     }

//     @Override
//     public boolean isFinished() {
//         return false;
//     }

//     @Override
//     public void end(boolean interrupted) {
//         s_ShooterMotor.ShooterMotorRun(0);
//     }
}