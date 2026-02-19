package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.WinchMotor;


/**
 *
 */
public class WinchRun extends Command {

    private final WinchMotor s_WinchMotor;
    private double m_WinchVel;
    private double m_WinchAcc;
 

    public WinchRun(double WinchVel, double WinchAcc, WinchMotor subsystem) {
        m_WinchVel = WinchVel;
        m_WinchAcc = WinchAcc;

        s_WinchMotor = subsystem;
        addRequirements(s_WinchMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_WinchMotor.setWinchSpeed(m_WinchVel, m_WinchAcc);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestWinchMotor.IntakeMotorOneRun(m_WinchVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_WinchMotor.setWinchSpeed(0, m_WinchAcc);
        // s_TestWinchMotor.IntakeMotorOneRun(0);
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