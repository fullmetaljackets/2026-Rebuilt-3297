package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.BackIntakeMotor;



/**
 */
public class IntakeRunReverse extends Command {

    private final IntakeMotor s_IntakeMotor;
    private final BackIntakeMotor s_BackIntakeMotor;
 

    public IntakeRunReverse( IntakeMotor subsystem, BackIntakeMotor backIntakeMotor) {
        s_IntakeMotor = subsystem;
        s_BackIntakeMotor = backIntakeMotor;
        addRequirements(s_IntakeMotor, s_BackIntakeMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // s_IntakeMotor.IntakeMotorOneRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        s_BackIntakeMotor.BackIntakeMotorOneRun(1);
        s_IntakeMotor.IntakeMotorOneRun(-1);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_IntakeMotor.IntakeMotorOneRun(0);
        s_BackIntakeMotor.BackIntakeMotorOneRun(0);
        // s_TestIntakeMotor.IntakeMotorOneRun(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}