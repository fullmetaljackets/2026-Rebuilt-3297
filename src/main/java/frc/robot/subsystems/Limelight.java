package frc.robot.subsystems;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class Limelight extends SubsystemBase {

  private static final String Intake_LL = "limelight-one";
  private static final String Shooter_LL = "limelight-two";
  private final CommandSwerveDrivetrain drivetrain;

  
  // Standard deviations for vision measurements
  // Increase these values if the vision data is noisy or unreliable
  private Matrix<N3, N1> visionStandardDeviations = 
    VecBuilder.fill(0.7, 0.7, 9999);  // X, Y, and rotation std devs

  public Limelight(CommandSwerveDrivetrain drivetrain) {
    // Store reference to drivetrain for accessing robot rotation
    this.drivetrain = drivetrain;
    
    // Configure Limelight with your camera mount position
    // Adjust these values to match your camera's physical placement on the robot
    LimelightHelpers.setCameraPose_RobotSpace(Intake_LL, 0.0508, 0.0508, 0.7493, 0, 21, 180);
    // LimelightHelpers.setCameraPose_RobotSpace(Shooter_LL, 0.31115, 0.225425, 0.206375, 0, 39, 0);
  }

  /**
   * Mirror hub poses between alliances. Field dimensions are taken from
   * `TunerConstants` so they can be tuned centrally.
   */

  /**
   * Get a PoseEstimate for a specific Limelight camera name using MegaTag2
   * This feeds the drivetrain's current rotation to improve pose estimation
   */
  public PoseEstimate getPoseEstimateForName(String limelightName) {
    // Get current robot rotation from drivetrain
    double robotRotationDeg = drivetrain.getState().Pose.getRotation().getDegrees();
    
    // Tell Limelight the robot's current orientation for better MegaTag2 estimation
    LimelightHelpers.SetRobotOrientation(limelightName, robotRotationDeg, 0, 0, 0, 0, 0);
    
    // Get MegaTag2 pose estimate with robot orientation info
    PoseEstimate estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(limelightName);
    
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
    return getPoseEstimateForName(Shooter_LL);
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

    // if (avgDist > 3) {
    //   xStdDev *= 9999;
    //   yStdDev *= 9999;
    // }

    return VecBuilder.fill(xStdDev, yStdDev, thetaStdDev);
  }

    // public Matrix<N3, N1> computeStdDevsForIntake_LL(String limelightName) {
  //   PoseEstimate est = getPoseEstimateForName(limelightName);
  //   int targetCount = est != null ? est.tagCount : 0;
  //   double avgDist = est != null ? est.avgTagDist : 0;

  //   double xStdDev = 0.9;
  //   double yStdDev = 0.9;
  //   double thetaStdDev = 9999; // VERY HIGH = ignore rotation

  //   if (targetCount >= 2) {
  //     xStdDev = 0.7;
  //     yStdDev = 0.7;
  //     thetaStdDev = 9999;
  //   } else if (targetCount == 1) {
  //     xStdDev = 0.9;
  //     yStdDev = 0.9;
  //     thetaStdDev = 9999;
  //   }

  //   if (avgDist > 3) {
  //     xStdDev *= 9999;
  //     yStdDev *= 9999;
  //   }

  //   return VecBuilder.fill(xStdDev, yStdDev, thetaStdDev);
  // }

  //   public Matrix<N3, N1> computeStdDevsForShooter_LL(String limelightName) {
  //   PoseEstimate est = getPoseEstimateForName(limelightName);
  //   int targetCount = est != null ? est.tagCount : 0;
  //   double avgDist = est != null ? est.avgTagDist : 0;

  //   double xStdDev = 0.9;
  //   double yStdDev = 0.9;
  //   double thetaStdDev = 9999; // VERY HIGH = ignore rotation

  //   if (targetCount >= 2) {
  //     xStdDev = 0.7;
  //     yStdDev = 0.7;
  //     thetaStdDev = 9999;
  //   } else if (targetCount == 1) {
  //     xStdDev = 9999;
  //     yStdDev = 9999;
  //     thetaStdDev = 9999;
  //   }

  //   if (avgDist > 3) {
  //     xStdDev *= 9999;
  //     yStdDev *= 9999;
  //   }

  //   return VecBuilder.fill(xStdDev, yStdDev, thetaStdDev);
  // }


  public Matrix<N3, N1> getVisionStdDevsForIntake() {
    return computeStdDevsForName(Intake_LL);
  }

  public Matrix<N3, N1> getVisionStdDevsForShooter() {
    return computeStdDevsForName(Shooter_LL);
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
    return hasValidPoseEstimateForName(Shooter_LL);
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
    return getTargetCountForName(Shooter_LL);
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

    /**
   * Return a hub pose that is mirrored for the current alliance. Provide the
   * hub pose defined for the BLUE alliance; this method will return the
   * equivalent pose for the RED alliance by reflecting across the field center.
   *
   * @param hubPoseBlue known Pose2d of the hub for the BLUE alliance coordinate frame
   * @return Pose2d adjusted for the current alliance
   */
  public Pose2d getHubPoseForAlliance() {
  var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
    if (alliance == DriverStation.Alliance.Red) { 
      return TunerConstants.kHubPoseRed;
    }
    // Blue alliance:
    return TunerConstants.kHubPoseBlue;
  }

  public double getShuttlePoseForAlliance() {
  var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
    if (alliance == DriverStation.Alliance.Red) { 
      return TunerConstants.kShuttleXRed;
    }
    // Blue alliance:
    return TunerConstants.kShuttleXBlue;
  }



  /**
   * Compute the planar distance between the robot and the hub using field poses.
   * If robotPose is null, this method will attempt to use the Limelight's latest
   * field-relative pose estimate. Returns Double.NaN if no robot pose is available.
   *
   * @param robotPose The robot's field-relative Pose2d (may be null to use Limelight estimate)
   * @param hubPose The hub's known field-relative Pose2d
   * @return distance in meters, or Double.NaN when unavailable
   */
  public double getDistanceToHub(Pose2d robotPose, Pose2d hubPose) {
    Pose2d rp = null;
    if (robotPose != null) {
      rp = robotPose;
    }
    if (rp == null || hubPose == null) {
      return Double.NaN;
    }

    return rp.getTranslation().getDistance(hubPose.getTranslation());
  }

  public double getDistanceToShuttle(Pose2d robotPose, double ShuttleX) {
    Pose2d rp = null;
    if (robotPose != null) {
      rp = robotPose;
    }
    if (rp == null || ShuttleX == Double.NaN) {
      return Double.NaN;
    }

    return Math.abs(robotPose.getX() - ShuttleX);
  }

  public double getRotationToHub(Pose2d robotPose, Pose2d hubPose) {
    Pose2d rp = null;
    if (robotPose != null) {
      rp = robotPose;
    } 
    if (rp == null || hubPose == null) {
      return Double.NaN;
    }

    Rotation2d toHub = new Rotation2d(hubPose.getX() - rp.getX(), hubPose.getY() - rp.getY());
    return toHub.minus(rp.getRotation()).getDegrees();
  }
}
