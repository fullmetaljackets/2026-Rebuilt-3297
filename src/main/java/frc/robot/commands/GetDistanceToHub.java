package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.LimelightHelpers;

public class GetDistanceToHub extends Command {
    private final CommandSwerveDrivetrain m_drivetrain;
    private final SwerveRequest.RobotCentric m_alignRequest;
    private final Limelight m_limelight;
    private final double kP_Distance = 0.05; // Proportional control constant
    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    private final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric();

    public GetDistanceToHub(CommandSwerveDrivetrain drivetrain, Limelight limelight) {
        m_drivetrain = drivetrain;
        m_limelight = limelight;
        m_alignRequest = new SwerveRequest.RobotCentric()
            .withDriveRequestType(DriveRequestType.Velocity);
        addRequirements( limelight);
    }

    @Override
    public void initialize() {
        // Initialization code if needed
        LimelightHelpers.setPipelineIndex("limelight-one", 0);

    }

    @Override
    public void execute() {
        // LimelightHelpers.getTargetPose3d_CameraSpace("limelight");
        
        double distance = m_limelight.getDistanceToHub();
        // double angleError = -Units.degreesToRadians(LimelightHelpers.getTX("limelight-one")); // Assume you have a method to get the angle error

        
        // Proportional control for distance and angle
        double ShooterSpeed = kP_Distance * distance;
        // double turnSpeed = kp_Angle * angleError;

        SmartDashboard.putNumber("distance", distance);

        SmartDashboard.putNumber("Shooter speed", ShooterSpeed);
        // SmartDashboard.putNumber("turn speed", turnSpeed);

        // Drive the robot
        // drivetrain.arcadeDrive(forwardSpeed, turnSpeed);


        m_drivetrain.setControl(
        m_alignRequest.withVelocityX(0) // Drive forward with negative Y (forward)
            .withVelocityY(0) // Drive left with negative X (left)
            .withRotationalRate(0) // Drive counterclockwise with negative X (left)
        );
    }

    @Override
    public boolean isFinished() {
        // double distance = m_limelight.getDistanceToHub() - DistanceOffset;
        // // double angleError = -Units.degreesToRadians(LimelightHelpers.getTX("limelight-one")); // Assume you have a method to get the angle error

        // double ShooterSpeed = kP_Distance * distance;
        // // double turnSpeed = kp_Angle * angleError;


        // // Define a condition to end the command, e.g., when the robot is close enough to the tag
        // return Math.abs(ShooterSpeed) < 0.04; 
        // // && Math.abs(turnSpeed) < 0.03;
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the drivetrain when the command ends
        m_drivetrain.setControl(
        drive.withVelocityX(0) // Drive forward with negative Y (forward)
            .withVelocityY(0) // Drive left with negative X (left)
            .withRotationalRate(0)); // Drive counterclockwise with negative X (left)
        // );
    }
}