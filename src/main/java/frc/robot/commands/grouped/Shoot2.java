package frc.robot.commands.grouped;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.FeederRun;
import frc.robot.commands.ShooterRun;
import frc.robot.commands.WinchRun;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;


public class Shoot2 extends SequentialCommandGroup{
    public Shoot2(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor, IntakeMotor s_IntakeMotor, BackIntakeMotor s_BackIntakeMotor){
        addCommands(
            new ShooterRun(43, 1000, s_ShooterMotor).withTimeout(1),
            new Shoot(s_ShooterMotor, s_FeederMotor, s_WinchMotor, s_IntakeMotor, s_BackIntakeMotor)
        );
    }

}
