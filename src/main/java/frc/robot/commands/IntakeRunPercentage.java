package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeMotor;


/**
 *
 */
public class IntakeRunPercentage extends Command {

    private final IntakeMotor s_IntakeMotor;
    private double m_setpoint;
 

    public IntakeRunPercentage(double setpoint, IntakeMotor subsystem) {
        s_IntakeMotor = subsystem;
        m_setpoint = setpoint;
        addRequirements(s_IntakeMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_IntakeMotor.IntakeMotorOneRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestIntakeMotor.IntakeMotorOneRun(m_IntakeVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_IntakeMotor.IntakeMotorOneRun(0);
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