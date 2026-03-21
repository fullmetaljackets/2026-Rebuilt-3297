# PathPlanner Testing & Monitoring Guide

## Setting Up Dashboard Monitoring

### Step 1: Add Error Monitoring to CommandSwerveDrivetrain

Add this code to the `periodic()` method to see path following errors:

```java
@Override
public void periodic() {
    // Existing code...
    
    // Optional: Log path following errors for tuning
    // This helps you see how well PathPlanner is tracking
    SmartDashboard.putNumber("PathPlanner/Current X", getState().Pose.getX());
    SmartDashboard.putNumber("PathPlanner/Current Y", getState().Pose.getY());
    SmartDashboard.putNumber("PathPlanner/Current Rotation", 
        getState().Pose.getRotation().getDegrees());
}
```

### Step 2: Create Test Paths

Use PathPlanner GUI to create these test paths:

#### Test Path 1: Straight Line (Baseline)
- **Name**: `Test_StraightLine_2m`
- **Waypoints**: 
  - Start: (1, 5) facing 0°
  - End: (3, 5) facing 0°
- **Length**: 2 meters
- **Purpose**: Baseline translation tuning

#### Test Path 2: 90 Degree Turn
- **Name**: `Test_90Turn`
- **Waypoints**:
  - Start: (1, 1) facing 0°
  - Middle: (2, 1) facing 0°
  - End: (2, 2) facing 90°
- **Length**: 2 meters total
- **Purpose**: Rotation tuning

#### Test Path 3: S-Curve
- **Name**: `Test_SCurve`
- **Waypoints**:
  - (1, 1) facing 0°
  - (2, 1) facing 0°
  - (3, 1.5) facing 45°
  - (4, 1) facing 0°
- **Purpose**: Combined translation & rotation

#### Test Path 4: Full Auto
- **Name**: `Auto_Competition`
- **Your actual autonomous routine**
- **Purpose**: Real-world testing

## Testing Procedure

### Before Each Test Session
1. ✓ Deploy latest code
2. ✓ Enable robot
3. ✓ Place robot at starting position
4. ✓ Open SmartDashboard
5. ✓ Disable and re-enable to reset odometry

### Test Sequence (by session)

#### Session 1: Translation P Value
```
Goal: Find the right P for smooth XY movement

1. Run: Test_StraightLine_2m with P=2.0
   - Observe: Does robot reach target? How smooth?
   - Record: Yes/No, any drifting?

2. Run: Same path with P=2.5
   - Observe: Faster? More stable?
   - Record: Improvement?

3. Run: Same path with P=3.0
   - Observe: Very fast? Overshooting?
   - Record: Best value so far?

Decision: Pick the P that reaches target smoothly
Likely result: P=2.5 or P=3.0
```

#### Session 2: Translation D Value
```
Goal: Add damping to prevent oscillation

1. Run: Test_StraightLine_2m with your best P, D=0
   - Record: Any side-to-side wiggle?

2. Run: Same path with D=0.1
   - Record: Smoother? Better?

3. Run: Same path with D=0.2 (if needed)
   - Record: Too sluggish now?

Decision: Pick D that smooths out wiggle
Likely result: D=0.1 or D=0.15
```

#### Session 3: Rotation P Value
```
Goal: Find the right P for angle control

1. Run: Test_90Turn with P=2.5
   - Observe: How accurate? How fast?
   - Record: Overshoots? Undershoots?

2. Run: Same path with P=3.0
   - Observe: Faster turn? More overshoot?
   - Record: Better or worse?

3. Run: Same path with P=3.5 (if needed)
   - Observe: Still smooth?
   - Record: Any oscillation?

Decision: Pick the P that turns smoothly
Likely result: P=2.5-3.5
```

#### Session 4: Rotation D Value
```
Goal: Smooth out angle overshoot

1. Run: Test_90Turn with your best P, D=0.5
   - Record: Any angle bounce-back?

2. Run: Same path with D=0.7
   - Record: Smoother? Softer landing?

3. Run: Same path with D=0.9 (if needed)
   - Record: Too sluggish?

Decision: Pick D that prevents overshoot
Likely result: D=0.6-0.9
```

#### Session 5: Full Auto Testing
```
Goal: Validate tuning with real path

1. Run: Auto_Competition (full routine)
   - Record: Overall performance
   - Note any issues

2. Run: Multiple times
   - Record: Consistency
   - Same performance each time?

3. Make final adjustments if needed
   - Increase P if still sluggish
   - Increase D if overshooting
   - Usually only minor tweaks needed
```

## What to Watch For

### Translation (XY) Movement

| What You See | What It Means | What To Do |
|--------------|--------------|-----------|
| Robot smoothly follows path | ✓ P is good | Proceed to D tuning |
| Robot slowly drifts from path | P is too low | Increase P |
| Robot jerks and overshoots | P is too high | Reduce P, add D |
| Left-right wiggle on straight | Need D | Add D |
| Smooth but slightly off path | Minor P tweak | Increase P by 0.2 |

### Rotation (Angle) Movement

| What You See | What It Means | What To Do |
|--------------|--------------|-----------|
| Smooth turns landing on angle | ✓ P&D are good | Keep values |
| Slow to reach target angle | P is too low | Increase P |
| Overshoots angle by 5-10° | P is high, D is low | Increase D |
| Bounces back and forth | P too aggressive | Reduce P or increase D |
| Smooth but slight angle lag | Minor P tweak | Increase P by 0.2 |

## SmartDashboard Recommendations

### Create These Widgets

1. **Translation Error** (Graph)
   - Network Tables: `/SmartDashboard/PathPlanner/Current X/Y`
   - See real-time position tracking

2. **Rotation** (Number)
   - Network Tables: `/SmartDashboard/PathPlanner/Current Rotation`
   - Watch angle movement in real time

3. **Vision Status** (Boolean)
   - Network Tables: `/SmartDashboard/LL/Has Valid Pose`
   - Confirm Limelight helping odometry

## Logging Results Template

```
Date: ___________
Tuning Focus: [ ] Translation P [ ] Translation D [ ] Rotation P [ ] Rotation D

Before:
  Translation P: ____  D: ____
  Rotation P: ____     D: ____

Test 1 - Test_StraightLine_2m:
  Result: ______________________
  Changes: ______________________

Test 2 - Test_90Turn:
  Result: ______________________
  Changes: ______________________

Test 3 - Test_SCurve:
  Result: ______________________
  Changes: ______________________

Final Values:
  Translation P: ____  D: ____
  Rotation P: ____     D: ____

Notes:
  ______________________
  ______________________
```

## Common Issues & Solutions

### Issue: "Robot just sits still"
```
Diagnosis: Either PathPlanner not running or odometry issue
Solution:
  1. Check PathPlanner auto is selected
  2. Verify robot has power
  3. Check robot odometry initialized (look at X/Y pose)
  4. Try moving robot manually to confirm motors work
```

### Issue: "Robot goes wrong direction"
```
Diagnosis: Alliance flip or odometry reset issue
Solution:
  1. Verify you're on correct alliance (Blue/Red toggle)
  2. Reset robot pose at starting position
  3. Check field coordinate system in PathPlanner
  4. Run test path on opposite alliance side
```

### Issue: "Path following very inconsistent"
```
Diagnosis: Usually motor slipping or friction variation
Solution:
  1. Check tire condition and tread
  2. Test same path 5 times, record results
  3. Slight variation (±0.1m) is normal
  4. Large variation indicates hardware issue
```

### Issue: "Works in practice, fails in competition"
```
Diagnosis: Different field friction/grip
Solution:
  1. Field carpet vs practice carpet differs
  2. Increase D slightly to handle variance
  3. Use higher P + higher D for robustness
  4. Test on actual field if possible
```

## Performance Targets

| Metric | Good | Acceptable | Needs Work |
|--------|------|-----------|-----------|
| Straight line accuracy | ±0.05m | ±0.1m | >0.1m |
| Turn accuracy | ±2° | ±5° | >5° |
| Path smoothness | No jerks | Minor jerks | Lots of jerks |
| Consistency | Same ±0.02m | Same ±0.05m | Random results |

## Final Checklist

Before competition, verify:
- [ ] Translation P and D are tuned
- [ ] Rotation P and D are tuned
- [ ] Path smoothness acceptable
- [ ] Multiple runs show consistency
- [ ] Works on both Blue and Red side
- [ ] Auto completes without errors
- [ ] All test paths run successfully
- [ ] Documented final PID values

## Quick Debug Checklist

If performance degrades:
```
[ ] Did you update your path in PathPlanner?
[ ] Did you redeploy the code?
[ ] Did you reset odometry (disable/enable)?
[ ] Is your wheel alignment correct?
[ ] Are tires worn or slipping?
[ ] Is your gyro calibrated?
[ ] Is Limelight detecting tags (if using)?
```
