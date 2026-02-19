# Shooter Motor SysId Tuning Guide

## Overview
This guide explains how to run SysId characterization on the shooter motor to obtain KS, KV, and KA tuning constants.

## SysId Test Procedure

### Step 1: Enable SysId Data Logging
- Deploy code to the robot
- Enable the robot in test mode
- Data will automatically be logged to the RoboRIO filesystem

### Step 2: Run Quasistatic Tests (Forward Direction)
Use the **CopilotStick** (Controller 2):
- Hold **Start** button
- While holding Start, press and hold **Y button** 
- Motor will ramp voltage slowly (quasistatic)
- Let run for the full 10 second timeout
- Release buttons when test completes

### Step 3: Run Quasistatic Tests (Reverse Direction)
- Hold **Start** button
- While holding Start, press and hold **X button**
- Motor will ramp voltage slowly in reverse direction
- Let run for the full 10 second timeout

### Step 4: Run Dynamic Tests (Forward Direction)
- Hold **Back** button
- While holding Back, press and hold **Y button**
- Motor will apply a step voltage and ramp (dynamic test)
- Let run for the full 10 second timeout

### Step 5: Run Dynamic Tests (Reverse Direction)
- Hold **Back** button
- While holding Back, press and hold **X button**
- Motor will apply a step voltage and ramp in reverse
- Let run for the full 10 second timeout

## Data Collection & Analysis

### Collect the Logs
After running all four tests:
1. Connect to the RoboRIO via SSH or file transfer
2. Collect the log file from: `/home/lvuser/` (look for `SysIdShooter_State_*.csv` files)
3. The log contains:
   - Applied voltage
   - Motor position (rotations)
   - Motor velocity (rotations/second)
   - Test state

### Run SysId Analysis Tool
1. Download the WPILib SysId tool from: https://github.com/wpilibsuite/sysid/releases
2. Open the SysId GUI application
3. Load the log file(s) from the RoboRIO
4. Analyze the data to determine:
   - **KS**: Static friction constant (Volts)
   - **KV**: Velocity constant (Volts·s/rotation)
   - **KA**: Acceleration constant (Volts·s²/rotation)

### Update Your Code
Once you have the KS, KV, and KA values:

Update `ShooterMotor.java` with the new values:
```java
Slot0Configs slot0 = TalonFXConfig.Slot0;
slot0.kS = 0.XXX;  // New KS value from SysId
slot0.kV = 0.XXX;  // New KV value from SysId
slot0.kA = 0.XXX;  // New KA value from SysId
```

## Key Information

### Current Configuration
- **Motor**: TalonFX on CAN ID 12
- **Motor Neutral Mode**: Coast
- **Direction**: CounterClockwise_Positive
- **Dynamic Test Step Voltage**: 7V
- **Ramp Rate**: 1V/s (default)
- **Test Timeout**: 10 seconds (default)

### What These Constants Mean
- **KS (Static Friction)**: Minimum voltage needed to overcome static friction
  - If too low: Motor won't start moving
  - If too high: Motor will accelerate abruptly
  
- **KV (Velocity Gain)**: Voltage needed per unit of velocity
  - If too low: Motor won't reach setpoint
  - If too high: Motor will overshoot setpoint

- **KA (Acceleration Gain)**: Voltage needed per unit of acceleration
  - Usually much smaller than KV
  - Helps with smooth acceleration

## Troubleshooting

### Motor doesn't move during test
- Check motor power connections
- Verify CAN bus connectivity
- Ensure motor isn't mechanically stuck
- Increase step voltage (currently 7V)

### Data looks noisy
- Make sure motor has no load initially
- Run tests in stable environment (avoid robot collisions)
- Check for CAN communication issues

### Results don't improve shooter accuracy
- Verify KP/KI/KD are properly tuned
- Check if mechanical issues affecting shooter
- Consider running tests multiple times for average
