package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ShooterMotor;


/**
 *
 */
public class ShooterRunPercentage extends Command {

    private final ShooterMotor s_ShooterMotor;
    private double m_setpoint;
 

    public ShooterRunPercentage(double setpoint, ShooterMotor subsystem) {
        s_ShooterMotor = subsystem;
        m_setpoint = setpoint;
        addRequirements(s_ShooterMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_ShooterMotor.ShooterMotorRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestIntakeMotor.IntakeMotorOneRun(m_IntakeVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_ShooterMotor.ShooterMotorRun(0);
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