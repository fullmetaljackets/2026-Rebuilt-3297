package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FeederMotor;


/**
 *
 */
public class FeederRunPercentage extends Command {

    private final FeederMotor s_FeederMotor;
    private double m_setpoint;
 

    public FeederRunPercentage(double setpoint, FeederMotor subsystem) {
        s_FeederMotor = subsystem;
        m_setpoint = setpoint;
        addRequirements(s_FeederMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_FeederMotor.FeederMotorRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestIntakeMotor.IntakeMotorOneRun(m_IntakeVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_FeederMotor.FeederMotorRun(0);
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