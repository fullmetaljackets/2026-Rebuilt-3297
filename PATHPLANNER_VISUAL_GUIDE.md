# PathPlanner PID Tuning - Visual Quick Guide

## The PID Tuning Decision Tree

```
┌─────────────────────────────────────────────────────────────────┐
│                   Run your autonomous path                      │
└────────────────────────┬────────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
    Smooth?          Slow?           Jerky?
        │                │                │
        ↓                ↓                ↓
    ✓ GOOD      Increase P        Increase D
                or                 or
            Increase           Reduce P
           Limelight
            accuracy
```

## Translation P Tuning Chart

```
P = 1.5 ┤ Very slow, won't catch up (not recommended)
        │
P = 2.0 ┤ ◄────── Your current value (baseline)
        │           OK, but can be better
P = 2.5 ┤ ◄────── Better responsiveness
        │           Still smooth, worth trying
P = 3.0 ┤ ◄────── Good responsiveness, smooth
        │           ← Recommended for most robots
P = 3.5 ┤         Getting aggressive, watch for jerks
        │
P = 4.0 ┤ Too aggressive, likely to overshoot
        │
        └────────────────────────────────────────
          Speed of path following increases →
```

## Translation D Tuning Chart

```
D = 0.0 ┤ No smoothing (may wiggle)
        │
D = 0.1 ┤ ◄────── Good starting point for D
        │           Smooths most robots
D = 0.2 ┤         More smoothing, slightly sluggish
        │
D = 0.3 ┤         Very smooth, starts feeling slow
        │
D = 0.5 ┤ Too much, robot feels heavy/slow
        │
        └────────────────────────────────────────
          Smoothness increases →
```

## Rotation P Tuning Chart

```
P = 1.5 ┤ Very slow turns
        │
P = 2.0 ┤ Slow turns
        │
P = 2.5 ┤ ◄────── Your current value
        │           OK baseline for rotation
P = 3.0 ┤ ◄────── Better turn speed
        │
P = 3.5 ┤ ◄────── Good responsiveness
        │           Worth trying
P = 4.0 ┤ Fast turns, watch for overshoot
        │
P = 5.0 ┤ Very aggressive turns
        │
        └────────────────────────────────────────
          Turn speed increases →
```

## Rotation D Tuning Chart

```
D = 0.3 ┤ Some overshoot expected
        │
D = 0.5 ┤ ◄────── Your current value
        │           OK baseline for rotation
D = 0.6 ┤ ◄────── Slightly smoother, good balance
        │
D = 0.7 ┤ ◄────── More smooth, recommended
        │
D = 0.8 ┤ Very smooth turns, good dampening
        │
D = 1.0 ┤ Smooth, but turns feel sluggish
        │
D = 1.5 ┤ Too much damping, poor responsiveness
        │
        └────────────────────────────────────────
          Smoothness increases →
```

## The Tuning Workflow

```
┌──────────────────────────────────────────────────┐
│ Step 1: Create a straight-line test path        │
│         (1.5-2 meters, no rotation)             │
└────────────────┬─────────────────────────────────┘
                 │ Run path 3 times
                 ↓
        ┌────────────────┐
        │ Results:       │
        │ How smooth?    │
        └────────────────┘
             │
    ┌────────┼────────┐
    │        │        │
  Poor     Good    Jerky
    │        │        │
    ↓        ↓        ↓
Increase  Move on  Increase
 P by 0.5  to Step   D by 0.1
   & try   2      & try again
   again

┌──────────────────────────────────────────────────┐
│ Step 2: Create a 90-degree turn test path       │
│         (1m straight, then 90° turn)            │
└────────────────┬─────────────────────────────────┘
                 │ Run path 3 times
                 ↓
        ┌────────────────┐
        │ Results:       │
        │ How smooth?    │
        └────────────────┘
             │
    ┌────────┼────────┐
    │        │        │
  Poor     Good    Bouncy
    │        │        │
    ↓        ↓        ↓
Increase  Move on  Increase
 P by 0.5  to Step   D by 0.2
   & try   3      & try again
   again

┌──────────────────────────────────────────────────┐
│ Step 3: Full auto test                          │
│         Run your entire autonomous routine      │
└────────────────┬─────────────────────────────────┘
                 │
           Does it work?
                 │
        ┌────────┴────────┐
        │                 │
       YES                NO
        │                 │
        ↓                 ↓
     DONE!          Go back and
  Document        fine-tune the
   values         problematic part
```

## Problem Symptom Visual Guide

### Translation (XY) Problems

```
SYMPTOM: Robot slowly drifts off target path
┌─────────────────────────────────────────┐
│ Target path: ━━━━━━━━━━━━━━━━━━         │
│ Actual:      ╲─ ╲─ ╲─ ╲─ ╲─            │
│              (drifts away)              │
└─────────────────────────────────────────┘
DIAGNOSIS: P is too low
FIX: Increase translation P by 0.5

SYMPTOM: Robot wiggles side-to-side
┌─────────────────────────────────────────┐
│ Target path: ━━━━━━━━━━━━━━━━━━         │
│ Actual:      ╱╲╱╲╱╲╱╲╱╲╱╲╱╲            │
│              (oscillates)               │
└─────────────────────────────────────────┘
DIAGNOSIS: No damping (D too low)
FIX: Increase translation D by 0.1

SYMPTOM: Robot overshoots then corrects
┌─────────────────────────────────────────┐
│ Target:      •                          │
│ Actual:      ━━━━━●━━━● (stops)         │
│              (too far, comes back)      │
└─────────────────────────────────────────┘
DIAGNOSIS: P too high, D too low
FIX: Increase D by 0.1 or reduce P by 0.5
```

### Rotation (Angle) Problems

```
SYMPTOM: Robot turns slowly to target angle
┌─────────────────────────────────────────┐
│ Target: 90°                             │
│ Actual: 30° → 50° → 70° → 85° (slow)   │
└─────────────────────────────────────────┘
DIAGNOSIS: P is too low
FIX: Increase rotation P by 0.5

SYMPTOM: Robot overshoots angle
┌─────────────────────────────────────────┐
│ Target: 90°                             │
│ Actual: 30° → 95° (overshot!)           │
└─────────────────────────────────────────┘
DIAGNOSIS: P too high, D too low
FIX: Increase rotation D by 0.2

SYMPTOM: Robot bounces at target angle
┌─────────────────────────────────────────┐
│ Target: 90°                             │
│ Actual: 85° ⟶ 95° ⟶ 85° ⟶ 95°         │
│         (bounces back and forth)        │
└─────────────────────────────────────────┘
DIAGNOSIS: P is too aggressive
FIX: Reduce P by 0.5 AND increase D by 0.3
```

## At-a-Glance Tuning Table

```
PROBLEM              │ ADJUST           │ VALUE CHANGE
──────────────────────┼──────────────────┼──────────────
Too slow             │ Translation P    │ +0.5
Wiggly/oscillating   │ Translation D    │ +0.1
Slow turns           │ Rotation P       │ +0.5
Angle overshoot      │ Rotation D       │ +0.2
Bouncy/hunting angle │ Rotation P & D   │ P-0.5, D+0.3
Jerky movement       │ Both D values    │ +0.1
Very sluggish        │ Decrease D       │ -0.05 or -0.1
Very jerky           │ Decrease P       │ -0.5
```

## Preset Configurations Quick View

```
┌──────────────────────────────────────────────────────────┐
│ CONSERVATIVE (Safe, slower)                             │
│ Translation: P=2.0, D=0.05  │  Rotation: P=2.5, D=0.5  │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│ BALANCED (Good starting point)                          │
│ Translation: P=2.5, D=0.1   │  Rotation: P=2.5, D=0.6  │
│                             ← Try this first!           │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│ RESPONSIVE (Faster, needs tuning)                       │
│ Translation: P=3.0, D=0.15  │  Rotation: P=3.0, D=0.8  │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│ AGGRESSIVE (Fast, can be jittery)                       │
│ Translation: P=3.5, D=0.2   │  Rotation: P=3.5, D=1.0  │
└──────────────────────────────────────────────────────────┘
```

## Expected Behavior After Each Adjustment

```
If you increase P:
  ✓ Response faster
  ✗ May overshoot more
  ✗ May oscillate if D too low

If you increase D:
  ✓ Smoother, less overshoot
  ✓ Dampens oscillation
  ✗ May feel sluggish if D too high
  ✗ Slightly slower response

If you decrease P:
  ✓ Less overshoot
  ✗ Slower response
  ✗ More lag behind target

If you decrease D:
  ✓ Faster, snappier response
  ✗ More overshoot
  ✗ More oscillation
```

## Final Checklist (Visual)

```
┌─ Translation P tuned?
│  □ Tested, smooth, responsive
├─ Translation D tuned?
│  □ Tested, no oscillation
├─ Rotation P tuned?
│  □ Tested, smooth turns
├─ Rotation D tuned?
│  □ Tested, no bounce
├─ Full auto tested?
│  □ Runs 3 times consistently
├─ Works both sides?
│  □ Blue and Red alliance
└─ Documented values?
   □ Written down for next season
   
If all checked → YOU'RE DONE! ✓
```

## One-Minute Summary

```
Current: Trans P=2, Rot P=2.5, Rot D=0.5

Slow path following?
  → Increase Translation P to 2.5

Wiggly on straight?
  → Add Translation D=0.1

Slow turns?
  → Increase Rotation P to 3.0

Bouncy turns?
  → Increase Rotation D to 0.7

Test with straight line, then 90° turn,
then full auto.

If satisfied: Done! If not: Adjust again.
```
