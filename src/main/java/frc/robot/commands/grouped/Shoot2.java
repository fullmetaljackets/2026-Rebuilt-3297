package frc.robot.commands.grouped;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.FeederRun;
import frc.robot.commands.ShootOnMove;
import frc.robot.commands.ShooterRun;
import frc.robot.commands.WinchRun;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FeederMotor;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterMotor;
import frc.robot.subsystems.WinchMotor;


public class Shoot2 extends ParallelCommandGroup{
    public Shoot2(ShooterMotor s_ShooterMotor, FeederMotor s_FeederMotor, WinchMotor s_WinchMotor, Limelight limelight, CommandSwerveDrivetrain drivetrain){
        addCommands(
            // new ShooterRun(34.5, 1000, s_ShooterMotor),
            new ShootOnMove(limelight, s_ShooterMotor, drivetrain),
            new WinchRun(-0.2, s_WinchMotor),
            new FeederRun(20, 1000, s_FeederMotor, s_ShooterMotor)
        );
    }

}
