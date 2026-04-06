// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.util.Comparator;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.commands.FeederRun;
import frc.robot.commands.FeederRunPercentage;
import frc.robot.commands.ShooterRun;
import frc.robot.commands.WinchHold;
import frc.robot.commands.WinchRun;
import frc.robot.commands.WinchToSetpoint;
import frc.robot.commands.grouped.IntakeRun;
import frc.robot.commands.grouped.IntakeRunFast;
import frc.robot.commands.grouped.IntakeRunFastReverse;
import frc.robot.commands.grouped.IntakeRunReverse;
import frc.robot.commands.grouped.Shoot3;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;

public class RobotContainer {
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.5).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController DriveStick = new CommandXboxController(0);
    private final CommandXboxController CopilotStick = new CommandXboxController(1);

    private final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final ShooterMotor s_ShooterMotor = new ShooterMotor();
    private final FeederMotor s_FeederMotor = new FeederMotor();
    private final IntakeMotor s_IntakeMotor = new IntakeMotor();
    private final BackIntakeMotor s_BackIntakeMotor = new BackIntakeMotor();
    private final WinchMotor s_WinchMotor = new WinchMotor();
    private final Limelight limelight = new Limelight();
    private final SendableChooser<Command> autoChooser;


    public RobotContainer() {
        NamedCommands.registerCommand("IntakeRun", new IntakeRun(s_IntakeMotor, s_BackIntakeMotor));
        NamedCommands.registerCommand("ShooterWarmup", new ShooterRun(43, 1000, s_ShooterMotor));
        NamedCommands.registerCommand("Shoot", new Shoot3(s_ShooterMotor, s_FeederMotor, s_WinchMotor));
        NamedCommands.registerCommand("IntakeUp", new WinchRun(-0.2, s_WinchMotor));
        NamedCommands.registerCommand("IntakeHold", new WinchToSetpoint(0.1, -9.23, s_WinchMotor));
        NamedCommands.registerCommand("IntakeDown", new WinchToSetpoint(0.1, -1, s_WinchMotor));

        // NamedCommands.registerCommand("WinchRun", new WinchRun(-0.15, s_WinchMotor));

        // autoChooser = AutoBuilder.buildAutoChooser();
        autoChooser = AutoBuilder.buildAutoChooserWithOptionsModifier((stream) -> stream.sorted(Comparator.comparing(PathPlannerAuto::getName)));

        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-DriveStick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-DriveStick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-DriveStick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        DriveStick.x().whileTrue(drivetrain.applyRequest(() -> brake));
        // DriveStick.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-DriveStick.getLeftY(), -DriveStick.getLeftX()))
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // DriveStick.back().and(DriveStick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // DriveStick.back().and(DriveStick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // DriveStick.start().and(DriveStick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // DriveStick.start().and(DriveStick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        DriveStick.start().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //Shooter
        // CopilotStick.leftBumper().whileTrue(new Shoot2(s_ShooterMotor, s_FeederMotor, s_WinchMotor, s_IntakeMotor, s_BackIntakeMotor));
        // CopilotStick.a().whileTrue(new ShooterRun(53, 1000, s_ShooterMotor));
        CopilotStick.rightBumper().and(CopilotStick.leftTrigger()).whileTrue(new ShooterRun(53, 1000, s_ShooterMotor));
        CopilotStick.rightBumper().whileTrue(new ShooterRun(44, 1000, s_ShooterMotor));

        // DriveStick.b().whileTrue(new FeederRun(10, 1000, s_FeederMotor, s_ShooterMotor));
        DriveStick.rightBumper().whileTrue(new FeederRun(20, 1000, s_FeederMotor, s_ShooterMotor));

        //Intake
        DriveStick.leftBumper().whileTrue(new IntakeRun(s_IntakeMotor, s_BackIntakeMotor));

        DriveStick.y().whileTrue(new IntakeRunReverse(s_IntakeMotor, s_BackIntakeMotor));
        //Winch
        CopilotStick.povDown().whileTrue(new WinchRun(0.15, s_WinchMotor));
        CopilotStick.povUp().whileTrue(new WinchRun(-0.2, s_WinchMotor));
        CopilotStick.povDown().onFalse(new WinchHold(0.1, s_WinchMotor));
        CopilotStick.povUp().onFalse(new WinchHold(0.1, s_WinchMotor));

        //limelight
        // DriveStick.a().whileTrue(new GetDistanceToHub(limelight, s_ShooterMotor, 1000));

        //manuel controlls shooter
        DriveStick.leftTrigger().and(DriveStick.povUp()).whileTrue(new ShooterRun(90, 1000, s_ShooterMotor));
        DriveStick.leftTrigger().and(DriveStick.povDown()).whileTrue(new ShooterRun(-90, 1000, s_ShooterMotor));
        //manuel controlls feeder
        DriveStick.leftTrigger().and(DriveStick.povRight()).whileTrue(new FeederRunPercentage(1, s_FeederMotor));
        DriveStick.leftTrigger().and(DriveStick.povLeft()).whileTrue(new FeederRunPercentage(-1, s_FeederMotor));
        CopilotStick.leftBumper().whileTrue(new FeederRunPercentage(-1, s_FeederMotor));
        //manuel controlls Intake
        DriveStick.rightTrigger().and(DriveStick.povUp()).whileTrue(new IntakeRunFast(s_IntakeMotor, s_BackIntakeMotor));
        DriveStick.rightTrigger().and(DriveStick.povDown()).whileTrue(new IntakeRunFastReverse(s_IntakeMotor, s_BackIntakeMotor));
        //manuel controlls Winch
        // DriveStick.rightTrigger().and(DriveStick.povRight()).whileTrue(new WinchRun(0.5, s_WinchMotor));
        // DriveStick.rightTrigger().and(DriveStick.povLeft()).whileTrue(new WinchRun(-0.5, s_WinchMotor));

        // Shooter SysId bindings - CopilotStick back + X/Y for dynamic, start + X/Y for quasistatic
        // CopilotStick.back().and(CopilotStick.y()).whileTrue(s_IntakeMotor.sysIdDynamic(Direction.kForward));
        // CopilotStick.back().and(CopilotStick.x()).whileTrue(s_IntakeMotor.sysIdDynamic(Direction.kReverse));
        // CopilotStick.start().and(CopilotStick.y()).whileTrue(s_IntakeMotor.sysIdQuasistatic(Direction.kForward));
        // CopilotStick.start().and(CopilotStick.x()).whileTrue(s_IntakeMotor.sysIdQuasistatic(Direction.kReverse));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
        // // Simple drive forward auton
        // final var idle = new SwerveRequest.Idle();
        // return Commands.sequence(
        //     // Reset our field centric heading to match the robot
        //     // facing away from our alliance station wall (0 deg).
        //     drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
        //     // Then slowly drive forward (away from us) for 5 seconds.
        //     drivetrain.applyRequest(() ->
        //         drive.withVelocityX(0.5)
        //             .withVelocityY(0)
        //             .withRotationalRate(0)
        //     )
        //     .withTimeout(5.0),
        //     // Finally idle for the rest of auton
        //     drivetrain.applyRequest(() -> idle)
        // );
    }

    /**
     * Updates the drivetrain odometry with vision measurements from Limelight
     * Call this method periodically to integrate vision data into the pose estimate
     */
    public void updateOdometryWithLimelight() {
        // Check if Limelight has a valid pose estimate
        var limelightPose = limelight.getPoseEstimate();
        
        if (limelightPose != null && limelight.hasValidPoseEstimate()) {
            // Get the pose and timestamp
            edu.wpi.first.math.geometry.Pose2d limelightFullPose = limelightPose.pose;
            double timestamp = limelightPose.timestampSeconds;
            var stdDevs = limelight.getVisionStandardDeviations();
            
            // IMPORTANT: Only use X and Y from Limelight, keep the gyro's rotation
            // Limelight's rotation is based on AprilTag orientation, not robot orientation
            edu.wpi.first.math.geometry.Pose2d robotPose = new edu.wpi.first.math.geometry.Pose2d(
                limelightFullPose.getX(),
                limelightFullPose.getY(),
                drivetrain.getState().Pose.getRotation()  // Use current gyro heading, not Limelight's rotation
            );
            
            // Update the drivetrain odometry with the vision measurement
            drivetrain.updateOdometryWithVision(robotPose, timestamp, stdDevs);
            
            // Debug logging
            SmartDashboard.putString("Vision/Status", "Updating odometry");
        }
    }
}
