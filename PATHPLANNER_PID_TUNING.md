# PathPlanner PID Tuning Guide for 2026 Robot

## Current Configuration

Your PathPlanner is configured with:

```java
// Translation PID (XY movement)
new PIDConstants(2, 0, 0)

// Rotation PID (angular movement)
new PIDConstants(2.5, 0, 0.5)
```

## Understanding the PID Constants

### P (Proportional Gain)
- **Effect**: How aggressively the controller corrects error
- **High P**: Fast response, but can overshoot or oscillate
- **Low P**: Slow response, path following less accurate
- **Units**: Units of velocity output per unit of position error

### I (Integral Gain)
- **Effect**: Corrects steady-state error over time
- **Usually 0**: For path following, you rarely need I (P handles most corrections)
- **Use cases**: Only if robot consistently lags behind at steady state
- **Caution**: Can cause oscillation if too high

### D (Derivative Gain)
- **Effect**: Damping - reduces overshooting
- **High D**: Smooth, less overshoot, but can be "sluggish"
- **Low D**: Responsive, but can overshoot
- **Units**: Output per unit of error rate

## Translation PID (XY Position Control)

**Your current:** `P=2, I=0, D=0`

### Starting Points (by robot speed)
```
Slow Robot (< 2 m/s):      P = 1.0 - 1.5,  D = 0.0
Medium Robot (2-3 m/s):    P = 2.0 - 3.0,  D = 0.0 - 0.1
Fast Robot (3+ m/s):       P = 3.0 - 5.0,  D = 0.1 - 0.3
```

### Tuning Steps

**Step 1: Test Base P Value**
1. Deploy with `P=2, I=0, D=0`
2. Run a simple path (straight line, 2 meters)
3. Watch SmartDashboard for "Translation Error"
4. Expected: Smooth arrival at target within ±0.1m

**Step 2: If Path Following is Sluggish**
- **Symptom**: Robot slowly drifts off path corners
- **Fix**: Increase P by 0.5 (try 2.5, 3.0, 3.5)
- **Stop when**: Robot reaches target quickly without overshoot

**Step 3: If Robot Oscillates**
- **Symptom**: Path follower oscillates left-right around path
- **Fix**: Add D (try 0.1, 0.2, 0.3)
- **Effect**: Smooths out the oscillations
- **Example**: `P=3.0, D=0.2`

**Step 4: If Robot Lags Behind**
- **Symptom**: Robot consistently behind the path's speed
- **Rare**: Usually fixed with higher P
- **Last resort**: Add small I (try 0.1, 0.05)

### Translation Tuning Progression

```
Starting point:   P=2.0, I=0, D=0     ← Start here
Too slow:        P=2.5, I=0, D=0
Oscillating:     P=2.5, I=0, D=0.1
Good baseline:   P=3.0, I=0, D=0.1   ← Common target

Fine tuning:     P=3.0, I=0, D=0.15
If still sluggish: P=3.5, I=0, D=0.15
```

## Rotation PID (Angular Control)

**Your current:** `P=2.5, I=0, D=0.5`

This is already pretty good! But here's how to tune it further.

### Starting Points (by robot's max rotation)
```
Slow rotation (< 180°/s):      P = 2.0 - 3.0,  D = 0.3 - 0.5
Medium rotation (180-360°/s):  P = 3.0 - 5.0,  D = 0.5 - 1.0
Fast rotation (360°+/s):       P = 5.0 - 8.0,  D = 1.0 - 2.0
```

### Tuning Steps

**Step 1: Test Base Configuration**
1. Your current `P=2.5, D=0.5` is good
2. Run a path with sharp turns
3. Watch: Does robot turn smoothly? Any overshoot?

**Step 2: If Rotation is Too Slow**
- **Symptom**: Robot falls behind angle targets
- **Fix**: Increase P by 0.5 (try 3.0, 3.5, 4.0)
- **Stop when**: Smooth turns without overshooting

**Step 3: If Robot Overshoots Angles**
- **Symptom**: Bounces back and forth when aligning
- **Fix**: Increase D by 0.2 (try 0.7, 0.9, 1.1)
- **Example**: `P=2.5, D=0.7`

**Step 4: If Robot is Sluggish with Oscillations**
- **Symptom**: Overshoots AND slow response
- **Fix**: Increase P AND D together
- **Example**: `P=3.5, D=0.8`

### Rotation Tuning Progression

```
Conservative:    P=2.0, I=0, D=0.3
Your current:    P=2.5, I=0, D=0.5   ← Start here
More responsive: P=3.0, I=0, D=0.6
Aggressive:      P=4.0, I=0, D=0.8
Very aggressive: P=5.0, I=0, D=1.0
```

## Quick Tuning Checklist

### Translation Issues Diagnosis

| Symptom | Cause | Fix |
|---------|-------|-----|
| Robot drifts off path corners | Low P | Increase P |
| Robot oscillates left/right on straight path | High P, no D | Add D or reduce P |
| Robot overshoots target | High P, low D | Increase D |
| Robot reaches target slowly | Low P | Increase P |
| Jerky movements | P too high for speed | Lower P or increase D |

### Rotation Issues Diagnosis

| Symptom | Cause | Fix |
|---------|-------|-----|
| Robot turns slowly | Low P | Increase P |
| Robot overshoots angle by 5-10° | High P, low D | Increase D |
| Robot bounces back and forth | P too aggressive | Lower P or increase D |
| Smooth but slow turns | Conservative P/D | Increase P |

## Recommended Testing Sequence

### Test 1: Straight Line (Baseline)
```
- Create path: (0,0) → (2,0) with no rotation
- Expected: Robot drives straight, smooth arrival
- Adjusts: Translation P value
```

### Test 2: 90° Turn
```
- Create path: (0,0) → (1,1) with 90° turn
- Expected: Smooth turn without overshoot
- Adjusts: Rotation P and D values
```

### Test 3: S-Curve
```
- Create path: (0,0) → (1,0) → (2,1) → (3,0)
- Expected: Smooth curves without oscillation
- Adjusts: Balance of both PID values
```

### Test 4: Full Auto
```
- Run your actual autonomous routine
- Expected: Consistent, smooth path following
- Adjusts: Fine-tune based on real performance
```

## Advanced Tips

### Feed Forward (You're Already Using!)
Good news: PathPlanner is using wheel force feedforwards:
```java
.withWheelForceFeedforwardsX(feedforwards.robotRelativeForcesXNewtons())
.withWheelForceFeedforwardsY(feedforwards.robotRelativeForcesYNewtons())
```
This means your motors get predictive voltage, so PID values don't need to be as aggressive.

### Tuning for Competition
- Start with safe values (P=2-3 for translation, P=2-3 for rotation)
- Use only P first (set D=0)
- Add D only if you see oscillation
- Avoid I gain unless you have steady-state lag

### Dashboard Monitoring
Add these to SmartDashboard for live tuning:
```java
SmartDashboard.putNumber("PP/Translation Error X", errorX);
SmartDashboard.putNumber("PP/Translation Error Y", errorY);
SmartDashboard.putNumber("PP/Rotation Error", rotationError);
SmartDashboard.putNumber("PP/Target Angle", targetAngle);
```

## Implementation Example

To change your values:

```java
new PPHolonomicDriveController(
    // Translation: Increase from 2 to 3 for more responsiveness
    new PIDConstants(3.0, 0, 0.1),
    // Rotation: Increase D from 0.5 to 0.8 for smoother turns
    new PIDConstants(2.5, 0, 0.8)
)
```

## Common Mistakes to Avoid

❌ **Too-High P**: Causes rapid oscillations, jerky motion
❌ **Non-Zero I**: Usually unnecessary, can cause drift
❌ **Unbalanced P/D**: High P without D causes overshoot
❌ **Same Values for Translation and Rotation**: They have different dynamics
❌ **Tuning during match**: Always practice in practice mode first

## Summary

1. **Start with Translation**: `P=2-3, D=0`
2. **Then Rotation**: `P=2.5-3.5, D=0.5-0.8`
3. **Test with simple paths first**
4. **Add D if you see oscillation**
5. **Increase P if robot is sluggish**
6. **Never use I unless absolutely necessary**

Your current values (P=2 translation, P=2.5 rotation with D=0.5) are already reasonable. Small increases in P (to 3.0) and D (to 0.1 for translation) would likely improve performance!
