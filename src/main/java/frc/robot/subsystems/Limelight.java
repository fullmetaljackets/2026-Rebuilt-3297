package frc.robot.subsystems;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.generated.TunerConstants;

public class Limelight extends SubsystemBase {

  private static final String Intake_LL = "limelight-one";
  private static final String ShooterLL_Name = "limelight-two";

  
  // Standard deviations for vision measurements
  // Increase these values if the vision data is noisy or unreliable
  private Matrix<N3, N1> visionStandardDeviations = 
    VecBuilder.fill(0.7, 0.7, 9999);  // X, Y, and rotation std devs

  public Limelight() {
    // Configure Limelight with your camera mount position
    // Adjust these values to match your camera's physical placement on the robot
    LimelightHelpers.setCameraPose_RobotSpace(Intake_LL, 0.04445, -0.0508, 0.7493, 0, 21, 0);
    LimelightHelpers.setCameraPose_RobotSpace(ShooterLL_Name, 0, 0, 0, 0, 0, 0);
  }

  /**
   * Get a PoseEstimate for a specific Limelight camera name (returns null if invalid)
   */
  public PoseEstimate getPoseEstimateForName(String limelightName) {
    PoseEstimate estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(limelightName);
    if (estimate != null && estimate.tagCount > 0) {
      return estimate;
    }
    return null;
  }

  /** Convenience accessors for the two cameras */
  public PoseEstimate getIntakePoseEstimate() {
    return getPoseEstimateForName(Intake_LL);
  }

  public PoseEstimate getShooterPoseEstimate() {
    return getPoseEstimateForName(ShooterLL_Name);
  }

  /**
   * Compute per-camera vision standard deviations using the same heuristics
   * as updateStdDevsBasedOnTargets() but per-camera.
   */
  private Matrix<N3, N1> computeStdDevsForName(String limelightName) {
    PoseEstimate est = getPoseEstimateForName(limelightName);
    int targetCount = est != null ? est.tagCount : 0;
    double avgDist = est != null ? est.avgTagDist : 0;

    double xStdDev = 0.9;
    double yStdDev = 0.9;
    double thetaStdDev = 9999; // VERY HIGH = ignore rotation

    if (targetCount >= 2) {
      xStdDev = 0.5;
      yStdDev = 0.5;
      thetaStdDev = 9999;
    } else if (targetCount == 1) {
      xStdDev = 0.7;
      yStdDev = 0.7;
      thetaStdDev = 9999;
    }

    if (avgDist > 3) {
      xStdDev *= 9999;
      yStdDev *= 9999;
    }

    return VecBuilder.fill(xStdDev, yStdDev, thetaStdDev);
  }

  public Matrix<N3, N1> getVisionStdDevsForIntake() {
    return computeStdDevsForName(Intake_LL);
  }

  public Matrix<N3, N1> getVisionStdDevsForShooter() {
    return computeStdDevsForName(ShooterLL_Name);
  }

  /**
   * Check if we have a valid pose estimate
   * @return true if Limelight has valid AprilTag detections
   */
  public boolean hasValidPoseEstimateForName(String limelightName) {
    PoseEstimate estimate = getPoseEstimateForName(limelightName);
    return estimate != null && LimelightHelpers.getTV(limelightName);
  }

  public boolean hasValidIntakePoseEstimate() {
    return hasValidPoseEstimateForName(Intake_LL);
  }

  public boolean hasValidShooterPoseEstimate() {
    return hasValidPoseEstimateForName(ShooterLL_Name);
  }

  /**
   * Get the number of AprilTags detected
   * @return Number of AprilTags in view
   */
  public int getTargetCountForName(String limelightName) {
    return (int) LimelightHelpers.getTargetCount(limelightName);
  }
  public int getIntakeTargetCount() {
    return getTargetCountForName(Intake_LL);
  }
  public int getShooterTargetCount() {
    return getTargetCountForName(ShooterLL_Name);
  }

  /**
   * Get the average distance to detected AprilTags
   * @return Distance in meters
   */

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Intake_LL Has Valid Pose", hasValidIntakePoseEstimate());
    SmartDashboard.putBoolean("Shooter_LL Has Valid Pose", hasValidShooterPoseEstimate());
  }
}
