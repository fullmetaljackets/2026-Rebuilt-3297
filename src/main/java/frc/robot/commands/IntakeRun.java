package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeMotor;


/**
 *
 */
public class IntakeRun extends Command {

    private final IntakeMotor s_IntakeMotor;
    private double m_IntakeVel;
    private double m_IntakeAcc;
 

    public IntakeRun(double IntakeVel, double IntakeAcc, IntakeMotor subsystem) {
        m_IntakeVel = IntakeVel;
        m_IntakeAcc = IntakeAcc;

        s_IntakeMotor = subsystem;
        addRequirements(s_IntakeMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_IntakeMotor.setIntakeSpeed(m_IntakeVel, m_IntakeAcc);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestIntakeMotor.IntakeMotorOneRun(m_IntakeVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_IntakeMotor.setIntakeSpeed(0, m_IntakeAcc);
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