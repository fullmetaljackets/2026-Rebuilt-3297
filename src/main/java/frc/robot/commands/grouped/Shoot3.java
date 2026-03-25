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


public class Shoot3 extends SequentialCommandGroup{
    public Shoot3(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor){
        addCommands(
            new Shoot(s_ShooterMotor, s_FeederMotor, s_WinchMotor).withTimeout(2),
            new Shoot2(s_ShooterMotor, s_FeederMotor, s_WinchMotor)
        );
    }

}
