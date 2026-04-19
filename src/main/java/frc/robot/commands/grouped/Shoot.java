package frc.robot.commands.grouped;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.FeederRunPercentage;
import frc.robot.commands.ShootOnMove;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;


public class Shoot extends ParallelCommandGroup{
    public Shoot(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor, Limelight limelight, CommandSwerveDrivetrain drivetrain){
        addCommands(
            // new ShooterRun(34.5, 1000, s_ShooterMotor),
            new ShootOnMove(limelight, s_ShooterMotor, drivetrain),
            // new WinchRun(-0.2, s_WinchMotor),
            new FeederRunPercentage(1, s_FeederMotor)
            // new IntakeRunSlow(s_IntakeMotor, s_BackIntakeMotor)
        );
    }

}
