package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FeederMotor;


/**
 *
 */
public class FeederRun extends Command {

    private final FeederMotor s_FeederMotor;
    private double m_FeederVel;
    private double m_FeederAcc;
 

    public FeederRun(double FeederVel, double FeederAcc, FeederMotor subsystem) {
        m_FeederVel = FeederVel;
        m_FeederAcc = FeederAcc;

        s_FeederMotor = subsystem;
        addRequirements(s_FeederMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_FeederMotor.setFeederSpeed(m_FeederVel, m_FeederAcc);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestShooterMotor.IntakeMotorOneRun(m_FeederVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_FeederMotor.setFeederSpeed(0, m_FeederAcc);
        // s_TestShooterMotor.IntakeMotorOneRun(0);
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