# Fix: Field Centric Heading Reset When Limelight Detects AprilTag

## Problem
Your field-centric heading was resetting when Limelight detected one or more AprilTags. This happened because Limelight's pose estimate includes a rotation component based on the AprilTag's orientation, which is **not** the same as the robot's rotation measured by the gyro.

## Root Cause
When Limelight first acquired a tag view:
1. Limelight calculated a full 6D pose (X, Y, Z, Roll, Pitch, Yaw)
2. The Yaw (rotation) was based on the AprilTag's orientation in the field
3. This rotation was being fed into the odometry Kalman filter
4. The Kalman filter would blend this AprilTag-based rotation with the gyro
5. This caused the perceived robot heading to suddenly "jump" to match the AprilTag orientation

## Solution Implemented

### 1. Modified `RobotContainer.updateOdometryWithLimelight()` (PRIMARY FIX)
Changed to **only use X and Y position from Limelight** and **preserve the gyro's rotation**:

```java
// OLD: Used Limelight's full pose including rotation
Pose2d robotPose = limelightPose.pose;

// NEW: Use Limelight XY, but keep gyro rotation
Pose2d robotPose = new Pose2d(
    limelightPose.pose.getX(),
    limelightPose.pose.getY(),
    drivetrain.getState().Pose.getRotation()  // Gyro rotation, not Limelight's
);
```

### 2. Updated `Limelight.updateStdDevsBasedOnTargets()` (REINFORCEMENT)
Set rotation standard deviation to **9999 everywhere** (extremely high = ignore rotation):

```java
// BEFORE: Used lower std dev with 2+ tags (6 radians)
if (targetCount >= 2) {
    thetaStdDev = 6;  // Would partially use Limelight's rotation
}

// AFTER: Always ignore rotation
thetaStdDev = 9999;  // Ignore Limelight's rotation regardless
```

## Why This Works

1. **Limelight only provides position info** - We extract X and Y (accurate)
2. **Gyro measures rotation** - We use the existing gyro for heading (reliable)
3. **No sudden heading changes** - Gyro provides continuous rotation
4. **Position still corrects drift** - Limelight's XY fixes odometry drift
5. **Confidence stays high** - We still trust Limelight for position accuracy

## What You'll Notice

✅ **Fixed**: Heading no longer resets when Limelight sees tags
✅ **Improved**: Position estimates are corrected by Limelight
✅ **Preserved**: Gyro-based rotation continues smoothly
✅ **Stable**: No jittery heading changes during autonomy

## Testing

Deploy and verify:
1. Drive robot manually with Limelight viewing AprilTags
2. Check SmartDashboard heading value - should NOT jump
3. Position values should gradually correct toward Limelight estimate
4. Autonomous should follow paths more accurately

## If Issues Persist

If you still see heading changes:
1. Check that your gyro calibration is correct
2. Verify Limelight is detecting AprilTags consistently
3. Reduce XY standard deviations if position corrections are too aggressive:
   ```java
   xStdDev = 1.0;  // Instead of 0.5
   yStdDev = 1.0;
   ```
