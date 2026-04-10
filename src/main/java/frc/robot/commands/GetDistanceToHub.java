package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterMotor;

public class GetDistanceToHub extends Command {
    private final Limelight m_limelight;
    private final ShooterMotor s_ShooterMotor;
    private final double distToSpeedOffset = 1; // Proportional control constant
    private double m_ShooterAcc;
    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    public GetDistanceToHub(Limelight limelight, ShooterMotor shooterMotor, double ShooterAcc) {
        m_limelight = limelight;
        s_ShooterMotor =shooterMotor;
        m_ShooterAcc = ShooterAcc;
        addRequirements(limelight, s_ShooterMotor);
    }

    @Override
    public void initialize() {
        // Initialization code if needed
        LimelightHelpers.setPipelineIndex("limelight", 0);

    }

    @Override
    public void execute() {
        // // LimelightHelpers.getTargetPose3d_CameraSpace("limelight");
        
        // double distance = m_limelight.getDistanceToHub();
        // // double angleError = -Units.degreesToRadians(LimelightHelpers.getTX("limelight-one")); // Assume you have a method to get the angle error

        
        // // Proportional control for distance and angle
        // double ShooterSpeed = distToSpeedOffset * distance;
        // // double turnSpeed = kp_Angle * angleError;

        // SmartDashboard.putNumber("distance", distance);

        // SmartDashboard.putNumber("Shooter speed", ShooterSpeed);
        // // SmartDashboard.putNumber("turn speed", turnSpeed);

        // // Drive the robot
        // // drivetrain.arcadeDrive(forwardSpeed, turnSpeed);


        // s_ShooterMotor.setShooterSpeed(ShooterSpeed, m_ShooterAcc);
    }

    @Override
    public boolean isFinished() {
        // double distance = m_limelight.getDistanceToHub() - DistanceOffset;
        // // double angleError = -Units.degreesToRadians(LimelightHelpers.getTX("limelight-one")); // Assume you have a method to get the angle error

        // double ShooterSpeed = kP_Distance * distance;
        // // double turnSpeed = kp_Angle * angleError;


        // // Define a condition to end the command, e.g., when the robot is close enough to the tag
        // return Math.abs(ShooterSpeed) < 0.04; 
        // // && Math.abs(turnSpeed) < 0.03;
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        s_ShooterMotor.setShooterSpeed(0, m_ShooterAcc);
    }
}