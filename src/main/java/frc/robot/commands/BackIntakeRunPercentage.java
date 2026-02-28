package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.BackIntakeMotor;


/**
 *
 */
public class BackIntakeRunPercentage extends Command {

    private final BackIntakeMotor s_BackIntakeMotor;
    private double m_setpoint;
 

    public BackIntakeRunPercentage(double setpoint, BackIntakeMotor subsystem) {
        s_BackIntakeMotor = subsystem;
        m_setpoint = setpoint;
        addRequirements(s_BackIntakeMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_BackIntakeMotor.BackIntakeMotorOneRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestIntakeMotor.IntakeMotorOneRun(m_IntakeVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
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