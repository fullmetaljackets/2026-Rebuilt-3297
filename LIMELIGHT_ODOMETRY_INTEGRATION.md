# Limelight Odometry Integration Guide

## Overview

Your swerve drivetrain's odometry is now integrated with Limelight's MegaTag 2 vision pose estimation. This provides real-time pose corrections to account for drift in the wheel odometry.

## How It Works

1. **Limelight Subsystem** - Captures MegaTag 2 pose estimates from detected AprilTags
2. **Standard Deviations** - Adjusts confidence levels based on number of visible tags and distance
3. **Vision Odometry Update** - Periodically feeds Limelight data into the drivetrain's Kalman filter
4. **Pose Correction** - The Kalman filter blends wheel odometry with vision data for accurate position

## Components

### 1. Limelight Subsystem (`subsystems/Limelight.java`)

**Key Methods:**
- `getPoseEstimate()` - Returns the robot pose from MegaTag 2 detection
- `hasValidPoseEstimate()` - Checks if vision data is reliable
- `getTargetCount()` - Number of AprilTags visible
- `getVisionStandardDeviations()` - Confidence levels in the measurement
- `updateStdDevsBasedOnTargets()` - Automatically adjusts confidence

**Standard Deviations:**
- With **2+ tags**: 0.5m XY accuracy, 6 radian rotation accuracy
- With **1 tag**: 0.7m XY accuracy, 9999 (very low) rotation accuracy
- **Farther than 4m**: Increases error scaling (less confident)

### 2. Swerve Drivetrain (`subsystems/CommandSwerveDrivetrain.java`)

Added `updateOdometryWithVision()` method that:
- Accepts vision pose, timestamp, and confidence values
- Feeds data into the extended Kalman filter
- Blends vision and wheel odometry for best estimate

### 3. Robot Main Loop (`Robot.java`)

The `robotPeriodic()` method now calls:
```java
m_robotContainer.updateOdometryWithLimelight();
```

This ensures vision data is integrated **every robot loop** (50Hz by default).

### 4. Robot Container (`RobotContainer.java`)

Added `updateOdometryWithLimelight()` method that:
- Checks for valid Limelight detections
- Extracts pose, timestamp, and confidence data
- Calls the drivetrain update method

## Configuration

### Camera Mount Position

If your Limelight is not mounted at the robot center, update this line in `Limelight.java`:

```java
LimelightHelpers.setCameraPose_RobotSpace(
    LL_NAME,     // Limelight name
    0,           // X offset from center (meters)
    0,           // Y offset from center (meters)
    0,           // Z offset from center (meters)
    0, 0, 0      // Roll, Pitch, Yaw rotations (degrees)
);
```

### Standard Deviation Tuning

Adjust default confidence levels in `Limelight.java`:

```java
// In updateStdDevsBasedOnTargets():
double xStdDev = 0.5;      // Position XY accuracy (meters)
double yStdDev = 0.5;
double thetaStdDev = 6;    // Rotation accuracy (radians)
```

**Guidelines:**
- **Lower values** = More trust in vision data
- **Higher values** = More trust in wheel odometry
- **9999 radians** = Essentially ignore rotation from Limelight

## Dashboard Monitoring

Check these SmartDashboard values to monitor vision performance:

```
LL/Has Valid Pose        - Boolean: Is Limelight detecting tags?
LL/Target Count          - Number of tags in view
LL/Avg Tag Distance      - Average distance to tags (meters)
LL/Robot X               - Estimated X position
LL/Robot Y               - Estimated Y position
LL/Robot Rotation        - Estimated rotation (degrees)
Vision/Status            - Update status message
```

## Troubleshooting

### Vision Updates Not Working

1. **Check Limelight Connection**
   - Verify Limelight is on the network
   - Check NetworkTables connection in Driver Station

2. **Verify AprilTag Detection**
   - Confirm tags are in Limelight view
   - Check LL/Target Count on dashboard
   - Verify tag IDs match field layout

3. **Check Confidence (Std Devs)**
   - If seeing erratic position jumps, increase std dev values
   - If position not correcting, decrease std dev values

### Vision Data Latency

- **Capture latency**: Time to capture frame (typical 11ms for Limelight 3)
- **Pipeline latency**: Processing time (varies by pipeline)
- **Network latency**: Minimal for local robot network

The timestamp is automatically corrected via `Utils.fpgaToCurrentTime()`.

## Advanced Tuning

### Distance-Based Confidence

To adjust how distance affects confidence:

```java
// In Limelight.java updateStdDevsBasedOnTargets()
if (avgDist > 5) {  // If farther than 5 meters
    xStdDev *= 2.0;  // Double the uncertainty
    yStdDev *= 2.0;
}
```

### Multiple Vision Sources

If adding another vision camera:

1. Create similar methods in that subsystem
2. Call drivetrain update from Robot.robotPeriodic()
3. The Kalman filter will blend all measurements

## Performance Impact

- **CPU**: Minimal impact (<1% overhead)
- **Network**: Uses existing NetworkTables connection
- **Loop Rate**: Runs at robot loop rate (50Hz default)

## Field Coordinate System

- **Blue Alliance**: Origin at blue driver station corner
- **Red Alliance**: Origin at red driver station corner
- **X-Axis**: Points across the field (increasing right)
- **Y-Axis**: Points along the field (increasing away from driver)

## References

- [WPILib Pose Estimator](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/pose-estimator/index.html)
- [Limelight MegaTag 2](https://docs.limelightvision.io/en/latest/docs/software/limelight-lib/python-api-new.html)
- [2024 AprilTag Layout](https://www.firstinspires.org/resource-library/frc/competition-manual-qa-system)
