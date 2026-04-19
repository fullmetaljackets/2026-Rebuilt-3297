package frc.robot.commands;

import static edu.wpi.first.units.Units.MetersPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

public class AimAtHub extends Command {
    private final Limelight m_limelight;
    private final double kp_Angle = 0.17;
    private double MaxSpeed = 2; // kSpeedAt12Volts desired top speed
    private final CommandXboxController DriveStick = new CommandXboxController(0);

    // private final double kp_Angle_Small = 0.2;

    private final SwerveRequest.FieldCentric m_alignRequest;
    private final CommandSwerveDrivetrain drivetrain;
    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    public AimAtHub(Limelight limelight, CommandSwerveDrivetrain drivetrain) {
        m_limelight = limelight;
        this.drivetrain = drivetrain;
        m_alignRequest = new SwerveRequest.FieldCentric()
            .withDriveRequestType(DriveRequestType.Velocity);
        // Don't require drivetrain - this allows manual driving while aiming
        addRequirements(drivetrain);
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
        if (robotRot > 180){
            robotRot = robotRot - 360;
        }
        SmartDashboard.putNumber("Robot Rotation (deg)", robotRot);
        double angleError = rotationToHub - robotRot;
        SmartDashboard.putNumber("Angle Error (deg)", angleError);
        // double kp_Angle = 0.12;
        // if (angleError < 6 && angleError > 0) {
        //     // kp_Angle = 1.5;
        // }
        // if (angleError > -6 && angleError < 0) {
        //     kp_Angle = 1.5;
        // }

        double rotationalRate = kp_Angle * angleError;
        if (rotationalRate > 4){
            rotationalRate = 4;
        }
        if (rotationalRate < -4){
            rotationalRate = -4;
        }
        SmartDashboard.putNumber("Rotation Rate",rotationalRate);

        // Get manual driving input (from your joystick/controller)
        // You'll need to modify this to get actual input values
        // For now, this just aims the rotation while allowing manual X/Y input
        double manualVelocityX = -DriveStick.getLeftY();  // Replace with actual joystick X input
        double manualVelocityY = -DriveStick.getLeftX();  // Replace with actual joystick Y input

        drivetrain.setControl(
        m_alignRequest.withVelocityX(manualVelocityX * MaxSpeed) // Manual forward/backward
            .withVelocityY(manualVelocityY * MaxSpeed) // Manual left/right
            .withRotationalRate(rotationalRate) // Auto-aimed rotation
        );

    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(
        m_alignRequest.withVelocityX(0) // Manual forward/backward
            .withVelocityY(0) // Manual left/right
            .withRotationalRate(0) // Auto-aimed rotation
        );

    }
}