package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.generated.TunerConstants;
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
        double shuttleX = m_limelight.getShuttlePoseForAlliance();
        SmartDashboard.putNumber("Hub Pose X", hubpose.getX());
        SmartDashboard.putNumber("Hub Pose Y", hubpose.getY());
        SmartDashboard.putNumber("Shuttle X", shuttleX);

        Pose2d robotPose = drivetrain.getState().Pose;
        SmartDashboard.putNumber("Robot Pose X", robotPose.getX());
        SmartDashboard.putNumber("Robot Pose Y", robotPose.getY());

        double distanceToHub = m_limelight.getDistanceToHub(robotPose, hubpose);
        double distanceToShuttle = m_limelight.getDistanceToShuttle(robotPose, shuttleX);
        SmartDashboard.putNumber("Distance to Hub", distanceToHub);
        
        // Calculate shooter speed based on distance
        double shooterSpeed = calculateShooterSpeed(distanceToHub, distanceToShuttle);
        s_ShooterMotor.setShooterSpeed(shooterSpeed, 1000);
        SmartDashboard.putNumber("Calculated Shooter Speed", shooterSpeed);

        Boolean shooterAtSetpoint = s_ShooterMotor.ShooterAtSetpoint(shooterSpeed, 3);
        SmartDashboard.putBoolean("Shooter Ready", shooterAtSetpoint);
    }

    /**
     * Calculates shooter speed based on distance to hub using linear interpolation.
     * Smoothly adjusts speed as distance changes based on your calibrated datapoints.
     * 
     * @param distanceToHub Distance to hub in meters
     * @param distanceToShuttle Distance to shuttle in meters
     * @return Shooter speed in RPM
     */
    private double calculateShooterSpeed(double distanceToHub, double distanceToShuttle) {
        var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
        Pose2d robotPose = drivetrain.getState().Pose;
        double distance = 0;

        if (alliance == DriverStation.Alliance.Blue) { 
            if (robotPose.getX() < 4.6) {
                distance = distanceToHub;
            }else if (robotPose.getX() >= 4.6) {
                distance = distanceToShuttle;
            }
        }else{
            if (robotPose.getX() > 11.9) {
                distance = distanceToHub;
            }else if (robotPose.getX() <= 11.9) {
                distance = distanceToShuttle;
            }
        }
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
        //     } else {
                
        //     }
        // } else {
        //     if (robotPose.getX() > 11.9) {
        //         if (distanceToHub < 0) {
        //             return 0;
        //         }
        //         if (distanceToHub <= 1.4) {
        //             // Linear interpolation from 0m to 1.4m: 0 RPM → 34.5 RPM
        //             return 34.5;
        //         }
        //         if (distanceToHub <= 2.1) {
        //             // Linear interpolation from 1.7m to 2.1m: 34.5 RPM → 37.8 RPM
        //             return 34.5 + (37.8 - 34.5) * ((distanceToHub - 1.4) / (2.1 - 1.4));
        //         }
        //         if (distanceToHub <= 2.6) {
        //             // Linear interpolation from 1.7m to 2.1m: 34.5 RPM → 43 RPM
        //             return 37.8 + (43 - 37.8) * ((distanceToHub - 2.1) / (2.6 - 2.1));
        //         }
        //         if (distanceToHub <= 3.1) {
        //             // Linear interpolation from 2.1m to 3.1m: 43 RPM → 52.0 RPM
        //             return 42 + (52.0 - 43) * ((distanceToHub - 2.6) / (3.1 - 2.6));
                    
        //         }
        //         if (distanceToHub <= 3.6) {
        //             // Linear interpolation from 3.1m to 3.6m: 52.0 RPM → 100.0 RPM
        //             return 52.0 + (100.0 - 52.0) * ((distanceToHub - 3.1) / (3.6 - 3.1));
        //         }
        //         // Beyond 3.6m, use max speed
        //         return 100.0;
        //     } else {
                
        //     }

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