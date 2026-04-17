package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterMotor;


/**
 *
 */
public class ShooterRun extends Command {

    private final ShooterMotor s_ShooterMotor;
    private double m_ShooterVel;
    private double m_ShooterAcc;
 

    public ShooterRun(double ShooterVel, double ShooterAcc, ShooterMotor subsystem) {
        m_ShooterVel = ShooterVel;
        m_ShooterAcc = ShooterAcc;

        s_ShooterMotor = subsystem;
        addRequirements(s_ShooterMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_ShooterMotor.setShooterSpeed(m_ShooterVel, m_ShooterAcc);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Boolean shooterAtSetpoint = s_ShooterMotor.ShooterAtSetpoint(m_ShooterVel, 2);
        SmartDashboard.putBoolean("Shooter Ready", shooterAtSetpoint);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_ShooterMotor.ShooterMotorRun(0);
        // s_ShooterMotor.setShooterSpeed(0, m_ShooterAcc);
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