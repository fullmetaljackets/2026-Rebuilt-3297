package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FeederMotor;


/**
 *
 */
public class FeederRun extends Command {

    private final FeederMotor s_FeederMotor;
    private double m_ShooterVel;
    private double m_ShooterAcc;
 

    public FeederRun(double ShooterVel, double ShooterAcc, FeederMotor subsystem) {
        m_ShooterVel = ShooterVel;
        m_ShooterAcc = ShooterAcc;

        s_FeederMotor = subsystem;
        addRequirements(s_FeederMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_FeederMotor.setShooterSpeed(m_ShooterVel, m_ShooterAcc);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestShooterMotor.IntakeMotorOneRun(m_ShooterVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_FeederMotor.setShooterSpeed(0, m_ShooterAcc);
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