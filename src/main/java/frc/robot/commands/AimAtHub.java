package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ShooterMotor;

public class AimAtHub extends Command {
    private final Limelight m_limelight;
    private final double kp_Angle = 0.08;
    private final SwerveRequest.RobotCentric m_alignRequest;
    private final CommandSwerveDrivetrain drivetrain;
    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    public AimAtHub(Limelight limelight, CommandSwerveDrivetrain drivetrain) {
        m_limelight = limelight;
        this.drivetrain = drivetrain;
        m_alignRequest = new SwerveRequest.RobotCentric()
            .withDriveRequestType(DriveRequestType.Velocity);
        addRequirements(limelight, drivetrain);
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

        double rotationToHub = Math.toDegrees(Math.atan2(
            hubpose.getY() - robotPose.getY(),
            hubpose.getX() - robotPose.getX()
        ));
        SmartDashboard.putNumber("Rotation To Hub (deg)", rotationToHub);
        double robotRot = drivetrain.getState().Pose.getRotation().getDegrees() + 180;
        if (robotRot > 180)
        robotRot = robotRot - 360;
        SmartDashboard.putNumber("Robot Rotation (deg)", robotRot);
        double angleError = rotationToHub - robotRot;
        SmartDashboard.putNumber("Angle Error (deg)", angleError);
        double rotationalRate = kp_Angle * angleError;
        SmartDashboard.putNumber("Rotation Rate",rotationalRate);

        drivetrain.setControl(
        m_alignRequest.withVelocityX(0) // Drive forward with negative Y (forward)
            .withVelocityY(0) // Drive left with negative X (left)
            .withRotationalRate(rotationalRate) // Drive counterclockwise with negative X (left)
        );

    }

    @Override
    public void end(boolean interrupted) {
    }
}