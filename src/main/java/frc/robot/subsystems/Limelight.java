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

  private static final String LL_NAME = "limelight";
  
  // Standard deviations for vision measurements
  // Increase these values if the vision data is noisy or unreliable
  private Matrix<N3, N1> visionStandardDeviations = 
    VecBuilder.fill(0.7, 0.7, 9999);  // X, Y, and rotation std devs

  public Limelight() {
    // Configure Limelight with your camera mount position
    // Adjust these values to match your camera's physical placement on the robot
    LimelightHelpers.setCameraPose_RobotSpace(LL_NAME, 0.028575, 0.0508, 0.7493, 0, 21, 0);
  }

  /**
   * Get the pose estimate from Limelight's MegaTag 2 pose estimation
   * Uses WPILib Blue alliance coordinate system
   * @return PoseEstimate containing the robot pose and timestamp, or null if no valid estimate
   */
  public PoseEstimate getPoseEstimate() {
    // PoseEstimate estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LL_NAME);
    PoseEstimate estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(LL_NAME);

    
    // Only return if we have a valid estimate with detected targets
    if (estimate != null && estimate.tagCount > 0) {
      return estimate;
    }
    return null;
  }

  /**
   * Check if we have a valid pose estimate
   * @return true if Limelight has valid AprilTag detections
   */
  public boolean hasValidPoseEstimate() {
    PoseEstimate estimate = getPoseEstimate();
    return estimate != null && LimelightHelpers.getTV(LL_NAME);
  }

  /**
   * Get the number of AprilTags detected
   * @return Number of AprilTags in view
   */
  public int getTargetCount() {
    return (int) LimelightHelpers.getTargetCount(LL_NAME);
  }

  /**
   * Get the average distance to detected AprilTags
   * @return Distance in meters
   */
  public double getAverageTagDistance() {
    PoseEstimate estimate = getPoseEstimate();
    return estimate != null ? estimate.avgTagDist : 0;
  }

  /**
   * Set the vision measurement standard deviations
   * Adjust these based on your Limelight's accuracy
   * @param xStdDev X position standard deviation in meters
   * @param yStdDev Y position standard deviation in meters
   * @param thetaStdDev Rotation standard deviation in radians
   */
  public void setVisionStandardDeviations(double xStdDev, double yStdDev, double thetaStdDev) {
    visionStandardDeviations = VecBuilder.fill(xStdDev, yStdDev, thetaStdDev);
  }

  /**
   * Get the vision measurement standard deviations
   * @return Matrix of standard deviations [x, y, theta]
   */
  public Matrix<N3, N1> getVisionStandardDeviations() {
    return visionStandardDeviations;
  }

  /**
   * Update vision standard deviations based on number of visible targets and average distance
   * More targets = higher confidence in the measurement
   * Closer targets = higher confidence
   */
  public void updateStdDevsBasedOnTargets() {
    int targetCount = getTargetCount();
    double avgDist = getAverageTagDistance();
    
    // Base standard deviations
    double xStdDev = 0.9;
    double yStdDev = 0.9;
    double thetaStdDev = 9999;  // VERY HIGH = essentially ignore rotation from Limelight
    
    // Improve with more targets
    if (targetCount >= 2) {
      xStdDev = 0.5;
      yStdDev = 0.5;
      thetaStdDev = 9999;  // Still ignore rotation regardless of target count
    } else if (targetCount == 1) {
      xStdDev = 0.7;
      yStdDev = 0.7;
      thetaStdDev = 9999;  // Still ignore rotation
    }
    
    // Penalize if too far away (> 4 meters)
    if (avgDist > 4) {
      xStdDev *= 1.5;
      yStdDev *= 1.5;
      // Don't penalize rotation since we're already ignoring it
    }
    
    setVisionStandardDeviations(xStdDev, yStdDev, thetaStdDev);
  }

  public double getDistanceToHub(){
    //red side: 2, 3, 4, 5, 8, 9, 10, 11
    if (LimelightHelpers.getFiducialID(LL_NAME) == 2
    || LimelightHelpers.getFiducialID(LL_NAME) == 3
    || LimelightHelpers.getFiducialID(LL_NAME) == 4
    || LimelightHelpers.getFiducialID(LL_NAME) == 5
    || LimelightHelpers.getFiducialID(LL_NAME) == 8
    || LimelightHelpers.getFiducialID(LL_NAME) == 9
    || LimelightHelpers.getFiducialID(LL_NAME) == 10
    || LimelightHelpers.getFiducialID(LL_NAME) == 11

    //blue side: 18, 19, 20, 21, 24, 25, 26, 27
    || LimelightHelpers.getFiducialID(LL_NAME) == 18
    || LimelightHelpers.getFiducialID(LL_NAME) == 19
    || LimelightHelpers.getFiducialID(LL_NAME) == 20
    || LimelightHelpers.getFiducialID(LL_NAME) == 21
    || LimelightHelpers.getFiducialID(LL_NAME) == 24
    || LimelightHelpers.getFiducialID(LL_NAME) == 25
    || LimelightHelpers.getFiducialID(LL_NAME) == 26
    || LimelightHelpers.getFiducialID(LL_NAME) == 27){
      Rotation2d angleToGoal = Rotation2d.fromDegrees(TunerConstants.LLMountAngle)
      .plus(Rotation2d.fromDegrees(LimelightHelpers.getTY(LL_NAME)));

      double distanceToHub = (TunerConstants.ApriltagHeight - TunerConstants.LLHight) / angleToGoal.getTan();

      return distanceToHub;
    }
    else{
      return 0;
    }
  }

  @Override
  public void periodic() {
    // Update standard deviations based on target count and distance
    updateStdDevsBasedOnTargets();
    
    // Dashboard data
    SmartDashboard.putBoolean("LL/Has Valid Pose", hasValidPoseEstimate());
    SmartDashboard.putNumber("LL/Target Count", getTargetCount());
    SmartDashboard.putNumber("LL/Avg Tag Distance", getAverageTagDistance());
    
    if (hasValidPoseEstimate()) {
      PoseEstimate estimate = getPoseEstimate();
      if (estimate != null) {
        Pose2d pose = estimate.pose;
        SmartDashboard.putNumber("LL/Robot X", pose.getX());
        SmartDashboard.putNumber("LL/Robot Y", pose.getY());
        SmartDashboard.putNumber("LL/Robot Rotation", pose.getRotation().getDegrees());
      }
    }
  }
}
