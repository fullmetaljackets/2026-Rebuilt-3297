# PathPlanner PID Tuning - Complete Reference

## Overview

You have **two separate PID controllers** in PathPlanner:

1. **Translation PID** - Controls XY position (how accurately robot reaches waypoints)
2. **Rotation PID** - Controls angle (how accurately robot faces correct direction)

Located in: `CommandSwerveDrivetrain.java` line ~225

## Current Configuration

```java
new PPHolonomicDriveController(
    new PIDConstants(2, 0, 0),        // Translation: P=2.0
    new PIDConstants(2.5, 0, 0.5)     // Rotation: P=2.5, D=0.5
)
```

## The Three Numbers Explained

### P (Proportional) - "How hard to push"
- **Range**: 1.0 to 10.0
- **Higher = Faster response, but can overshoot**
- **Lower = Slower response, less overshoot**
- **Translation P**: Usually 2-4
- **Rotation P**: Usually 2-5

### I (Integral) - "Fix steady errors"
- **Range**: 0 to 1.0
- **Usually leave at 0 for path following**
- **Only use if robot consistently lags (rare)**

### D (Derivative) - "Damping/smoothing"
- **Range**: 0 to 2.0
- **Higher = Smoother, but slower response**
- **Lower = Faster, but can overshoot**
- **Translation D**: Usually 0-0.3
- **Rotation D**: Usually 0-1.0

## How to Tune

### The Golden Rule
1. **Set P first** (find responsiveness)
2. **Add D second** (smooth out overshoot)
3. **Never touch I** (leave at 0)

### Tuning Chart

```
STEP 1: Translation P
┌─────────────────────────────────────┐
│ P=2.0 → Too slow? → Try P=2.5
│ P=2.5 → Still slow? → Try P=3.0
│ P=3.0 → Jerky? → Reduce to 2.5
│ GOAL: Smooth, quick arrival
└─────────────────────────────────────┘

STEP 2: Translation D
┌─────────────────────────────────────┐
│ Wiggling side-to-side?
│ Yes → Add D=0.1
│ Still wiggly? → Try D=0.2
│ GOAL: Smooth without wiggle
└─────────────────────────────────────┘

STEP 3: Rotation P
┌─────────────────────────────────────┐
│ P=2.5 → Too slow turning? → Try P=3.0
│ P=3.0 → Still slow? → Try P=3.5
│ P=3.5 → Overshoots? → Reduce to 3.0
│ GOAL: Smooth, quick turns
└─────────────────────────────────────┘

STEP 4: Rotation D
┌─────────────────────────────────────┐
│ Angle overshooting?
│ Yes → Increase D to 0.7 or 0.8
│ Still bouncy? → Try D=1.0
│ GOAL: Lands on angle smoothly
└─────────────────────────────────────┘
```

## Quick Presets

### Conservative (Safe, reliable)
```java
Translation: P=2.0, I=0, D=0.05
Rotation:    P=2.5, I=0, D=0.5
```
Use this if you're unsure. It works well.

### Balanced (Recommended)
```java
Translation: P=2.5, I=0, D=0.1
Rotation:    P=2.5, I=0, D=0.6
```
Good balance of speed and smoothness.

### Responsive (Aggressive, requires tuning)
```java
Translation: P=3.0, I=0, D=0.15
Rotation:    P=3.5, I=0, D=0.8
```
Fast path following, but needs testing.

## Real-World Symptoms & Solutions

### Problem: "Robot follows path slowly"
```
Likely cause: P too low
Solution: Increase P
Try next: P = P + 0.5
```

### Problem: "Robot wiggles side-to-side"
```
Likely cause: P is ok, but no D
Solution: Add D
Try next: D = 0.1, then 0.2 if needed
```

### Problem: "Robot overshoots target"
```
Likely cause: P too high, D too low
Solution: Increase D
Try next: D = D + 0.2 or reduce P
```

### Problem: "Robot bounces back and forth at target"
```
Likely cause: Very high P without enough D
Solution: Either reduce P or increase D significantly
Try: P - 0.5 OR D + 0.3
```

### Problem: "Works in practice but fails in match"
```
Likely cause: P/D too tight for match field
Solution: Decrease P slightly, increase D slightly
Try: P - 0.2, D + 0.1
```

## Testing Your Changes

### Quick Test (3 minutes)
1. Create straight-line path (2 meters)
2. Run it once
3. Did robot reach target smoothly? 
   - No → Adjust P
   - Yes but jerky → Adjust D

### Full Test (15 minutes)
1. Test straight line → Tune translation P
2. Test 90° turn → Tune rotation P
3. Test S-curve → Tune D values
4. Test full auto → Verify works together

### Validation Test (Competition)
1. Run path 5 times
2. Record results each time
3. All similar? → Good, ready to go
4. All different? → Adjust P/D for robustness

## How Each Value Works

### Increasing P (Translation)
```
Effect: Robot more aggressively corrects XY errors
Result: Faster path following, snappier movement
Risk: Can cause overshoot or oscillation
When: Use if robot is sluggish
Caution: Don't increase more than 5.0
```

### Increasing D (Translation)
```
Effect: Smooths out XY movement, reduces oscillation
Result: Jerky movement becomes smooth
Risk: Can slow down response
When: Use if robot wiggles or oscillates
Caution: Usually don't need more than 0.3
```

### Increasing P (Rotation)
```
Effect: Robot more aggressively corrects angle errors
Result: Faster rotation, snappier turns
Risk: Can overshoot target angle
When: Use if robot turns slowly
Caution: Don't increase more than 5.0
```

### Increasing D (Rotation)
```
Effect: Smooths out rotation, prevents overshoot
Result: Smooth turns, lands on angle gently
Risk: Can make turns feel sluggish
When: Use if robot overshoots angles
Caution: Usually don't need more than 1.0
```

## Common Mistakes

❌ **Changing I gain** - Don't, leave at 0
❌ **Adjusting both P and D at once** - Change one at a time
❌ **Making huge changes** - Increment by 0.5 for P, 0.1 for D
❌ **Not testing multiple times** - Test same path 3-5 times
❌ **Only tuning translation, ignoring rotation** - Both matter
❌ **Trying to be perfect** - "Good enough" is usually fine

## Success Indicators

✓ Robot reaches straight targets within ±0.1m
✓ Robot turns smoothly without bouncing
✓ Path following is smooth, not jerky
✓ Results are consistent (runs 1, 2, 3 are similar)
✓ Works on both field colors (Blue and Red)
✓ Auto completes without stopping or reversing unexpectedly

## Code Location & Modification

File: `src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java`

Find this section (around line 225):
```java
new PPHolonomicDriveController(
    new PIDConstants(2, 0, 0),        // ← Change translation P here
    new PIDConstants(2.5, 0, 0.5)     // ← Change rotation P and D here
)
```

Example: To try P=3.0 for translation:
```java
new PPHolonomicDriveController(
    new PIDConstants(3.0, 0, 0),      // Changed from 2 to 3.0
    new PIDConstants(2.5, 0, 0.5)     // Left unchanged
)
```

Then:
1. Save the file
2. Right-click file → Run As → WPILib Deploy
3. Wait for "BUILD SUCCESSFUL"
4. Disable and re-enable robot
5. Test your path

## When to Stop Tuning

You're done when:
- ✓ Path following is smooth and accurate
- ✓ Results are consistent across multiple runs
- ✓ Robot reaches targets at reasonable speed
- ✓ No excessive overshooting or undershooting
- ✓ You're happy with the performance

Typically takes 1-3 practice sessions.

## Documentation

Write down your final values:

```
Date: _______________
Translation P: ____  D: ____
Rotation P: ____     D: ____

Performance notes:
__________________________________
__________________________________

Next session improvements:
__________________________________
```

## TL;DR (Too Long; Didn't Read)

1. Your current values (P=2 trans, P=2.5 rot) are fine
2. If robot is slow: Increase P
3. If robot oscillates: Increase D
4. Test with simple paths first
5. Start conservative, increase from there
6. Don't change I gain
7. Change one value at a time
8. Test multiple times
9. Document what works

**Suggested first improvement:**
```java
new PIDConstants(2.5, 0, 0.1),    // Translation: up to 2.5, add D
new PIDConstants(2.5, 0, 0.6)     // Rotation: add D to 0.6
```

Good luck! 🤖
