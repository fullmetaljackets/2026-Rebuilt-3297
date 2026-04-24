package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.WinchMotor;


/**
 *
 */
public class WinchRun extends Command {

    private final WinchMotor s_WinchMotor;
    private double m_WinchSpeed;
 

    public WinchRun(double WinchSpeed, WinchMotor subsystem) {
        m_WinchSpeed = WinchSpeed;


        s_WinchMotor = subsystem;
        addRequirements();

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        s_WinchMotor.WinchMotorRun(m_WinchSpeed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // s_TestWinchMotor.IntakeMotorOneRun(m_WinchVel);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_WinchMotor.WinchMotorRun(0);
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