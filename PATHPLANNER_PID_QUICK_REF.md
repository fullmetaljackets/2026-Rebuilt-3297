# PathPlanner PID Tuning - Quick Reference

## Your Current Values
```java
Translation (XY):  P=2.0, I=0, D=0
Rotation (Angle):  P=2.5, I=0, D=0.5
```

## Quick Diagnostic

### Is your robot following paths accurately?

**YES** ✓
- Good! Consider fine-tuning: Increase translation P to 2.5-3.0, add translation D of 0.1

**NO** - Check these symptoms:

#### 1. Robot is SLOW to reach target
```
Symptom: Robot slowly drifts off path, doesn't catch up
Fix:     Increase P value
         Translation: Try 2.5 or 3.0
         Rotation:   Try 3.0 or 3.5
```

#### 2. Robot OVERSHOOTS (goes past target)
```
Symptom: Robot swings past point, then has to correct back
Fix:     Increase D value
         Translation: Add D=0.1 or 0.2
         Rotation:   Increase D to 0.7 or 0.8
```

#### 3. Robot OSCILLATES (wiggles side-to-side)
```
Symptom: Left-right wiggle on straight paths
Fix:     Increase D to smooth it out
         Translation: Add D=0.1, then 0.2 if needed
         Rotation:   Increase D from 0.5 to 0.7
```

#### 4. Robot does BOTH (slow AND oscillates)
```
Symptom: Both sluggish and wiggly
Fix:     Increase BOTH P and D
         Translation: P=3.0, D=0.15
         Rotation:   P=3.0, D=0.8
```

## Copy-Paste Tuning Presets

### Conservative (Safest for Competition)
```java
new PPHolonomicDriveController(
    new PIDConstants(2.0, 0, 0),      // Translation
    new PIDConstants(2.5, 0, 0.5)     // Rotation
)
```

### Balanced (Good Starting Point)
```java
new PPHolonomicDriveController(
    new PIDConstants(2.5, 0, 0.1),    // Translation
    new PIDConstants(2.5, 0, 0.6)     // Rotation
)
```

### Responsive (Better Path Following)
```java
new PPHolonomicDriveController(
    new PIDConstants(3.0, 0, 0.15),   // Translation
    new PIDConstants(3.0, 0, 0.8)     // Rotation
)
```

### Aggressive (Fastest, Requires Good Robot)
```java
new PPHolonomicDriveController(
    new PIDConstants(3.5, 0, 0.2),    // Translation
    new PIDConstants(3.5, 0, 1.0)     // Rotation
)
```

## Step-by-Step Tuning Process

### Session 1: Translation Tuning
1. Create a **straight-line path** (2 meters, no rotation)
2. Run it and watch the robot
3. If slow: Increase translation P by 0.5
4. If overshoots: Add translation D = 0.1
5. Repeat until: Smooth, quick arrival

### Session 2: Rotation Tuning
1. Create a **90° turn path**
2. Run it and watch the robot
3. If slow turn: Increase rotation P by 0.5
4. If overshoots angle: Increase rotation D by 0.2
5. Repeat until: Smooth turn, lands on target

### Session 3: Full Path Testing
1. Create your actual **auto path**
2. Run multiple times
3. Tweak values based on performance
4. Document final values

## Tuning Order (Important!)

Always tune in this order:
1. **P first** - Get the basic response right
2. **D second** - Smooth out overshooting
3. **I last** - Only if you have lag issues (rare)

## Expected Performance

### Translation Error
- **Excellent**: < 0.05m from path
- **Good**: < 0.1m from path
- **Acceptable**: < 0.2m from path
- **Poor**: > 0.2m from path

### Rotation Error
- **Excellent**: < 2°
- **Good**: < 5°
- **Acceptable**: < 10°
- **Poor**: > 10°

## Safety Checklist Before Deploying

- [ ] Did you increase P first, then D?
- [ ] Did you avoid changing I gain?
- [ ] Did you test on simple paths before complex ones?
- [ ] Does robot reach targets without crazy oscillation?
- [ ] Did you test on both Blue and Red alliance side?

## If Something Goes Wrong

**Robot jerks violently:**
- P is too high! Reduce translation P to 2.0, rotation P to 2.0
- This is normal if you increased too much

**Robot doesn't move:**
- Check that PathPlanner is enabled
- Verify robot has power
- Try reducing the path distance

**Robot overshoots badly:**
- Reduce P value, not D
- Add D gradually (0.05 at a time)

**Nothing changes when I adjust values:**
- Make sure you redeployed the code
- Confirm values in SmartDashboard match what you typed
- Check robot power

## Pro Tips

✅ **Document every change** - Write down what you tried and the result
✅ **Test same path multiple times** - Variability tells you if tuning is stable
✅ **Start conservative** - Build up, don't go too aggressive
✅ **Use vision if available** - Your Limelight helps odometry accuracy
✅ **Test on dirty field** - Friction varies, account for it

## When to Stop Tuning

You're done when:
- ✓ Robot follows straight paths smoothly
- ✓ Robot turns without big overshoots
- ✓ Robot reaches targets consistently
- ✓ Auto runs without surprises

Usually takes 2-3 practice sessions!
