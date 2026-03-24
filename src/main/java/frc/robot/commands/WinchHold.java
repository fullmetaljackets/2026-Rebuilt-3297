// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WinchMotor;

public class WinchHold extends Command {
  private final WinchMotor m_WinchMotor;
  private double m_tolernace;
  private double m_WinchSetpoint;


  /** Creates a new Arm. */
  public WinchHold(double tolerance, WinchMotor subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_WinchMotor = subsystem;
    m_tolernace = tolerance;
    m_WinchSetpoint = m_WinchMotor.getWinchPosition();
    addRequirements(m_WinchMotor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_WinchSetpoint = m_WinchMotor.getWinchPosition();
    m_WinchMotor.setMy_WinchMotor(m_WinchSetpoint);
    SmartDashboard.putString("test", "test");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_WinchMotor.atTargetPosition(m_WinchSetpoint, m_tolernace);
  }
}
