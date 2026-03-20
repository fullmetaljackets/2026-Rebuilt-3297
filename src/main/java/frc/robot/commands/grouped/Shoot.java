package frc.robot.commands.grouped;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.FeederRun;
import frc.robot.commands.ShooterRun;
import frc.robot.commands.WinchRun;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;


public class Shoot extends ParallelCommandGroup{
    public Shoot(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor, IntakeMotor s_IntakeMotor, BackIntakeMotor s_BackIntakeMotor){
        addCommands(
            new ShooterRun(43, 1000, s_ShooterMotor),
            new WinchRun(-0.2, s_WinchMotor),
            new FeederRun(20, 1000, s_FeederMotor, s_ShooterMotor)
            // new IntakeRunSlow(s_IntakeMotor, s_BackIntakeMotor)
        );
    }

}
