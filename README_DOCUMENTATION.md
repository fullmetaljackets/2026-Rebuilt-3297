# 2026 Robot Documentation - Complete Index

## PathPlanner PID Tuning (New!)

Comprehensive guides for tuning your autonomous path following:

1. **PATHPLANNER_SUMMARY.md** ⭐ START HERE
   - Overview of all guides
   - Quick reference
   - Getting started in 5 minutes

2. **PATHPLANNER_PID_QUICK_REF.md** 
   - 2-minute quick reference
   - Copy-paste presets
   - Symptom diagnosis

3. **PATHPLANNER_VISUAL_GUIDE.md**
   - Charts and diagrams
   - Visual problem identification
   - Quick lookup tables

4. **PATHPLANNER_PID_COMPLETE.md**
   - Full PID explanation
   - Real-world examples
   - Common issues & solutions

5. **PATHPLANNER_PID_TUNING.md**
   - Deep dive into PID control
   - Step-by-step procedures
   - Advanced tips

6. **PATHPLANNER_TESTING_GUIDE.md**
   - How to create test paths
   - Dashboard setup
   - Full testing procedures
   - Logging templates

## Vision & Odometry

1. **LIMELIGHT_ODOMETRY_INTEGRATION.md**
   - How Limelight integrates with odometry
   - Configuration options
   - Standard deviation tuning

2. **VISION_INTEGRATION_SUMMARY.md**
   - Quick overview of vision system
   - Files modified
   - Integration points

3. **HEADING_RESET_FIX.md**
   - Fix for heading resets when Limelight detects tags
   - Explanation of the problem
   - Solution implemented

## SysId Tuning

1. **SYSID_TUNING_GUIDE.md**
   - How to run SysId characterization
   - What the KS, KV, KA constants mean
   - Troubleshooting guide

## Getting Started

**If you have 5 minutes:**
- Read: PATHPLANNER_SUMMARY.md
- Then: Pick a quick guide

**If you have 30 minutes:**
- Read: PATHPLANNER_PID_COMPLETE.md
- Then: Try the "Balanced" preset

**If you have 1 hour:**
- Read: PATHPLANNER_PID_TUNING.md
- Study: PATHPLANNER_VISUAL_GUIDE.md
- Setup: PATHPLANNER_TESTING_GUIDE.md

**If you have 2+ hours:**
- Complete journey through all PathPlanner guides
- Run full test suite per PATHPLANNER_TESTING_GUIDE.md
- Document your results

## Current Configuration

### PathPlanner PID
```java
Translation: P=2.0, I=0, D=0
Rotation:    P=2.5, I=0, D=0.5
```
Location: `CommandSwerveDrivetrain.java` line ~225

### Limelight Vision
```
Status: Active
Integration: Robot.java calls updateOdometryWithLimelight()
Features: X/Y position correction (rotation ignored)
Standard Deviations: Auto-adjust based on tag count
```

### SysId Status
```
Shooter Motor: Fully instrumented
Data Logging: Enabled for voltage, position, velocity
Ready for: KS, KV, KA characterization
```

## File Organization

### Documentation Root
```
/PATHPLANNER_*.md           (7 files - Autonomous tuning)
/LIMELIGHT_*.md             (3 files - Vision odometry)
/SYSID_*.md                 (1 file - Motor characterization)
/HEADING_*.md               (1 file - Vision heading fix)
/README.md                  (This file)
```

### Code Files (No changes needed for reading, but reference)
```
/src/main/java/frc/robot/
├── Robot.java                          (Vision odometry update call)
├── RobotContainer.java                 (Vision integration method)
└── subsystems/
    ├── CommandSwerveDrivetrain.java     (PathPlanner PID config)
    ├── Limelight.java                  (Vision subsystem)
    └── ShooterMotor.java               (SysId instrumentation)
```

## What to Read for Common Tasks

### "How do I tune PathPlanner?"
→ PATHPLANNER_SUMMARY.md (5 min)
→ PATHPLANNER_PID_QUICK_REF.md (3 min)
→ Deploy and test!

### "My path following is bad"
→ PATHPLANNER_VISUAL_GUIDE.md (find symptom)
→ PATHPLANNER_PID_QUICK_REF.md (apply fix)

### "How does vision work?"
→ VISION_INTEGRATION_SUMMARY.md
→ LIMELIGHT_ODOMETRY_INTEGRATION.md

### "My heading resets with Limelight"
→ HEADING_RESET_FIX.md (already fixed!)

### "How do I use SysId?"
→ SYSID_TUNING_GUIDE.md

### "I'm completely lost, help!"
→ Start with PATHPLANNER_SUMMARY.md
→ Pick the guide matching your time
→ Read it carefully
→ You'll know what to do

## Quick Facts

- **PathPlanner**: Uses 2 independent PID controllers (translation + rotation)
- **Vision System**: Limelight provides position correction, gyro provides rotation
- **Odometry**: Kalman filter blends wheel odometry with vision data
- **SysId**: Ready to characterize shooter motor for feed-forward control
- **Current Perf**: Conservative but functional, room for improvement

## Documentation Updates

All guides were created March 20, 2026. Based on:
- Your robot configuration
- Standard FRC best practices
- PathPlanner 2024+ documentation
- WPILib pose estimation

## How to Use These Guides

1. **Read once** - Understand the concept
2. **Reference often** - Use while tuning
3. **Document your results** - Write down what works
4. **Share with team** - Help others understand
5. **Keep for next season** - These apply to future robots too

## Document Purposes

| Document | Purpose | Best For |
|----------|---------|----------|
| PATHPLANNER_SUMMARY.md | Overview & navigation | Starting point |
| PATHPLANNER_PID_QUICK_REF.md | Quick answers | During tuning |
| PATHPLANNER_VISUAL_GUIDE.md | Visual reference | Problem diagnosis |
| PATHPLANNER_PID_COMPLETE.md | Full explanation | Understanding |
| PATHPLANNER_PID_TUNING.md | Detailed procedures | Step-by-step |
| PATHPLANNER_TESTING_GUIDE.md | Test procedures | Validation |
| LIMELIGHT_ODOMETRY_INTEGRATION.md | Vision system | Setup & tuning |
| VISION_INTEGRATION_SUMMARY.md | Implementation | Integration points |
| HEADING_RESET_FIX.md | Gyro fix | Already applied |
| SYSID_TUNING_GUIDE.md | Motor tuning | SysId procedure |

## Quick Navigation

**Autonomous/PathPlanner Issues:**
→ PATHPLANNER_SUMMARY.md → pick other guide

**Vision/Odometry Issues:**
→ LIMELIGHT_ODOMETRY_INTEGRATION.md

**Heading/Gyro Issues:**
→ HEADING_RESET_FIX.md (already fixed)

**Motor Characterization:**
→ SYSID_TUNING_GUIDE.md

**General Help:**
→ PATHPLANNER_SUMMARY.md (most comprehensive)

## Success Metrics

After following the guides, your robot should:
- ✓ Follow paths within ±0.1m
- ✓ Turn smoothly without bouncing
- ✓ Complete autonomous consistently
- ✓ Have accurate odometry (vision-corrected)
- ✓ Have smooth motor control (SysId-tuned)

## Questions?

If unclear about something:
1. Check the specific guide for that topic
2. Read the "Troubleshooting" section
3. Look in PATHPLANNER_VISUAL_GUIDE.md for diagrams
4. Check PATHPLANNER_TESTING_GUIDE.md for procedures

Everything you need is in these documents!

---

**Created:** March 20, 2026
**Robot:** 2026-Rebuilt-3297
**Version:** 1.0
**Status:** Complete & Ready to Use

Start with PATHPLANNER_SUMMARY.md for the best guide! 🚀
