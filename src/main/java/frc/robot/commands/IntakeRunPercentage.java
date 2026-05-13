package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeMotor;
import frc.robot.subsystems.BackIntakeMotor;
import frc.robot.subsystems.WinchMotor;



/**
 */
public class IntakeRunPercentage extends Command {

    private final IntakeMotor s_IntakeMotor;
    private final BackIntakeMotor s_BackIntakeMotor;
    private final WinchMotor s_WinchMotor;
    private final double m_IntakeVelFront;
    private final double m_IntakeVelBack;

    public IntakeRunPercentage(IntakeMotor subsystem, BackIntakeMotor backIntakeMotor, WinchMotor winchMotor, double intakeVelFront, double intakeVelBack) {
        s_IntakeMotor = subsystem;
        s_BackIntakeMotor = backIntakeMotor;
        s_WinchMotor = winchMotor;
        m_IntakeVelFront = intakeVelFront;
        m_IntakeVelBack = intakeVelBack;
        addRequirements(s_IntakeMotor, s_BackIntakeMotor);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // s_IntakeMotor.IntakeMotorOneRun(m_setpoint);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (s_WinchMotor.getWinchPosition() > -2){
            s_IntakeMotor.IntakeMotorOneRun(m_IntakeVelFront);
            s_BackIntakeMotor.BackIntakeMotorOneRun(m_IntakeVelBack);
        } else {
            s_BackIntakeMotor.BackIntakeMotorOneRun(m_IntakeVelBack);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        s_IntakeMotor.IntakeMotorOneRun(0);
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