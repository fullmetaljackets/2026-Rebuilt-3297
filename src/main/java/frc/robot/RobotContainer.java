// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.commands.FeederRun;
import frc.robot.commands.IntakeRunPercentage;
import frc.robot.commands.ShooterRun;
import frc.robot.commands.WinchRun;
import frc.robot.commands.WinchToSetpoint;
import frc.robot.commands.grouped.IntakeRun;
import frc.robot.commands.grouped.Shoot;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
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
    private final SendableChooser<Command> autoChooser;


    public RobotContainer() {
        NamedCommands.registerCommand("IntakeRun", new IntakeRunPercentage(0.5, s_IntakeMotor));
        NamedCommands.registerCommand("ShooterWarmup", new ShooterRun(43, 1000, s_ShooterMotor));
        NamedCommands.registerCommand("Shoot", new Shoot(s_ShooterMotor, s_FeederMotor, s_WinchMotor));
        // NamedCommands.registerCommand("WinchRun", new WinchRun(-0.15, s_WinchMotor));

        autoChooser = AutoBuilder.buildAutoChooser();
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

        DriveStick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        DriveStick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-DriveStick.getLeftY(), -DriveStick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // DriveStick.back().and(DriveStick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // DriveStick.back().and(DriveStick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // DriveStick.start().and(DriveStick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // DriveStick.start().and(DriveStick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        DriveStick.a().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //Shooter
        CopilotStick.rightBumper().whileTrue(new ShooterRun(43, 1000, s_ShooterMotor));
        DriveStick.rightBumper().whileTrue(new FeederRun(20, 1000, s_FeederMotor));
        //Intake
        DriveStick.leftBumper().whileTrue(new IntakeRun(s_IntakeMotor, s_BackIntakeMotor));
        //Winch
        CopilotStick.povDown().whileTrue(new WinchRun(0.15, s_WinchMotor));
        CopilotStick.povUp().whileTrue(new WinchRun(-0.15, s_WinchMotor));
        CopilotStick.povDown().onFalse(new WinchToSetpoint(0.1, s_WinchMotor));
        CopilotStick.povUp().onFalse(new WinchToSetpoint(0.1, s_WinchMotor));

        //manuel controlls
        
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
}
