package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.generated.TunerConstants;

public class Limelight extends SubsystemBase{

  public double getDistanceToHub(){
    //red side: 2, 3, 4, 5, 8, 9, 10, 11
    if (LimelightHelpers.getFiducialID("limelight-one") == 2
    || LimelightHelpers.getFiducialID("limelight-one") == 3
    || LimelightHelpers.getFiducialID("limelight-one") == 4
    || LimelightHelpers.getFiducialID("limelight-one") == 5
    || LimelightHelpers.getFiducialID("limelight-one") == 8
    || LimelightHelpers.getFiducialID("limelight-one") == 9
    || LimelightHelpers.getFiducialID("limelight-one") == 10
    || LimelightHelpers.getFiducialID("limelight-one") == 11

    //blue side: 18, 19, 20, 21, 24, 25, 26, 27
    || LimelightHelpers.getFiducialID("limelight-one") == 18
    || LimelightHelpers.getFiducialID("limelight-one") == 19
    || LimelightHelpers.getFiducialID("limelight-one") == 20
    || LimelightHelpers.getFiducialID("limelight-one") == 21
    || LimelightHelpers.getFiducialID("limelight-one") == 24
    || LimelightHelpers.getFiducialID("limelight-one") == 25
    || LimelightHelpers.getFiducialID("limelight-one") == 26
    || LimelightHelpers.getFiducialID("limelight-one") == 27){
      Rotation2d angleToGoal = Rotation2d.fromDegrees(TunerConstants.LLMountAngle)
      .plus(Rotation2d.fromDegrees(LimelightHelpers.getTY("limelight-sone")));

      double distanceToHub = (TunerConstants.ApriltagHeight - TunerConstants.LLHight) / angleToGoal.getTan();

      return distanceToHub;
    }
    else{
      return 0;
    }
  }
}
