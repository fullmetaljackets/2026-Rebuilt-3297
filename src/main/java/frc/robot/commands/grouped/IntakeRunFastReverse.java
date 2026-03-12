package frc.robot.commands.grouped;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.commands.IntakeRunPercentage;
import frc.robot.commands.BackIntakeRunPercentage;


public class IntakeRunFastReverse extends ParallelCommandGroup{
    public IntakeRunFastReverse(IntakeMotor s_IntakeMotor, BackIntakeMotor s_BackIntakeMotor){

        addCommands(
            new IntakeRunPercentage(-1, s_IntakeMotor),
            new BackIntakeRunPercentage(1, s_BackIntakeMotor)
        );
    }

}
