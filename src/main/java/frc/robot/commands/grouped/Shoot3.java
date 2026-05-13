package frc.robot.commands.grouped;

import java.nio.file.LinkOption;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;


public class Shoot3 extends SequentialCommandGroup{
    public Shoot3(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor, Limelight limelight, CommandSwerveDrivetrain drivetrain, BackIntakeMotor s_BackIntakeMotor, IntakeMotor s_IntakeMotor){
        addCommands(
            new Shoot(s_ShooterMotor, s_FeederMotor, s_WinchMotor, limelight, drivetrain).withTimeout(2),
            new Shoot2(s_ShooterMotor, s_FeederMotor, s_WinchMotor, limelight, drivetrain, s_BackIntakeMotor, s_IntakeMotor)
        );
    }

}
