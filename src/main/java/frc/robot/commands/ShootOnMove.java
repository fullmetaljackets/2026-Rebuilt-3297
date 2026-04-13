package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ShooterMotor;

public class ShootOnMove extends Command {
    private final Limelight m_limelight;
    private final ShooterMotor s_ShooterMotor;
    private final CommandSwerveDrivetrain drivetrain;
    // private final double kp_Strafe = 2;
    // private final double kp_Angle = 1.7;
    
    public ShootOnMove(Limelight limelight, ShooterMotor shooterMotor, CommandSwerveDrivetrain drivetrain) {
        m_limelight = limelight;
        s_ShooterMotor =shooterMotor;
        this.drivetrain = drivetrain;
        addRequirements(limelight, s_ShooterMotor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Pose2d hubpose = m_limelight.getHubPoseForAlliance();
        SmartDashboard.putNumber("Hub Pose X", hubpose.getX());
        SmartDashboard.putNumber("Hub Pose Y", hubpose.getY());

        Pose2d robotPose = drivetrain.getState().Pose;
        SmartDashboard.putNumber("Robot Pose X", robotPose.getX());
        SmartDashboard.putNumber("Robot Pose Y", robotPose.getY());

        double distanceToHub = m_limelight.getDistanceToHub(robotPose, hubpose);
        SmartDashboard.putNumber("Distance to Hub", distanceToHub);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}