# PathPlanner PID Tuning - Complete Documentation Summary

## Created Documents

I've created 6 comprehensive guides for PathPlanner PID tuning:

### 📋 Index & Reference
- **PATHPLANNER_PID_INDEX.md** - Overview of all guides and quick navigation

### 🚀 Quick Start (2-5 min read)
- **PATHPLANNER_PID_QUICK_REF.md** - Quick diagnostics and copy-paste presets
- **PATHPLANNER_VISUAL_GUIDE.md** - Visual charts and diagrams

### 📚 Learning (10-20 min read)
- **PATHPLANNER_PID_COMPLETE.md** - Full PID explanation and troubleshooting
- **PATHPLANNER_PID_TUNING.md** - Detailed step-by-step tuning procedures

### 🧪 Testing (15-30 min read)
- **PATHPLANNER_TESTING_GUIDE.md** - How to create tests and validate tuning

## Your Configuration

**Location:** `src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java` (line ~225)

**Current Values:**
```java
new PPHolonomicDriveController(
    new PIDConstants(2, 0, 0),        // Translation P=2.0
    new PIDConstants(2.5, 0, 0.5)     // Rotation P=2.5, D=0.5
)
```

**Status:** ✓ Functional baseline, can be improved
**Recommended:** Try `P=2.5-3.0, D=0.1` for translation, `P=2.5, D=0.6-0.8` for rotation

## The Essential Concept

There are **3 magic numbers**:

1. **P (Proportional)** = How hard the robot pushes to reach the target
   - Too high = Jerky, overshoots
   - Too low = Slow, drifts off path
   
2. **D (Derivative)** = How smooth the movement is (damping)
   - Too high = Sluggish
   - Too low = Oscillates, bounces
   
3. **I (Integral)** = Leave at 0 (rarely needed)

## Tuning in 30 Seconds

1. **If slow:** Increase P
2. **If jerky/wiggly:** Increase D
3. **If bouncy:** Increase D
4. **If overshoots:** Increase D or decrease P
5. **If sluggish:** Decrease D
6. **Test multiple times to confirm**

## Before You Start

✓ Deploy latest code
✓ Test your robot moves correctly
✓ Create simple test paths in PathPlanner
✓ Open SmartDashboard
✓ Have a notebook to record results

## The Process

```
1. Create test path (straight line)
2. Run path and observe
3. Adjust ONE value
4. Redeploy and test again
5. Repeat until satisfied
6. Move to next test (90° turn)
7. Repeat steps 2-5
8. Test full auto
```

**Expected time:** 1-3 practice sessions

## Which Guide to Read

| Goal | Guide | Time |
|------|-------|------|
| Quick answer NOW | PATHPLANNER_PID_QUICK_REF.md | 2 min |
| Understand why | PATHPLANNER_PID_COMPLETE.md | 10 min |
| Step-by-step tuning | PATHPLANNER_PID_TUNING.md | 15 min |
| How to test | PATHPLANNER_TESTING_GUIDE.md | 20 min |
| Visual reference | PATHPLANNER_VISUAL_GUIDE.md | 5 min |
| Full reference | PATHPLANNER_PID_INDEX.md | 10 min |

## Start Here (Choose One)

### Option A: I want results FAST (5 min)
1. Read: PATHPLANNER_PID_QUICK_REF.md
2. Use: Copy-paste the "Balanced" preset
3. Deploy and test
4. Adjust based on diagnostics

### Option B: I want to understand (30 min)
1. Read: PATHPLANNER_PID_COMPLETE.md
2. Read: PATHPLANNER_VISUAL_GUIDE.md
3. Use: Follow the step-by-step process
4. Reference: Check PATHPLANNER_TESTING_GUIDE.md while testing

### Option C: I want to be thorough (1-2 hours)
1. Read: PATHPLANNER_PID_TUNING.md
2. Study: PATHPLANNER_VISUAL_GUIDE.md
3. Follow: PATHPLANNER_TESTING_GUIDE.md exactly
4. Document: Record all your results
5. Reference: PATHPLANNER_PID_COMPLETE.md for troubleshooting

## Critical Information

### The Tuning Order
1. **Translation P first**
2. **Translation D second**
3. **Rotation P third**
4. **Rotation D fourth**
5. **Never touch I**

### Values to Try

**Translation P:**
- Too slow? Try 2.5
- Still slow? Try 3.0
- Jerky? Go back to 2.5, add D

**Translation D:**
- Wiggly? Try 0.1
- Still wiggly? Try 0.2
- Sluggish? Go back to 0.1

**Rotation P:**
- Too slow? Try 3.0
- Overshoots? Increase D instead
- Bouncy? Try increasing D by a lot

**Rotation D:**
- Overshoots? Try 0.7
- Still bouncy? Try 0.9
- Too smooth? Try 0.5

### Common Results

**What works for most robots:**
```
Translation: P=2.5-3.0, D=0.1-0.15
Rotation:    P=2.5-3.0, D=0.6-0.8
```

This gives good balance of speed and smoothness.

## Testing Your Changes

### Quick Test (3 minutes)
1. Deploy code
2. Run one test path
3. Smooth and reaches target?
   - Yes → Good
   - No → Adjust and repeat

### Full Test (15 minutes)
1. Test straight line (tune translation)
2. Test 90° turn (tune rotation)
3. Test full auto (verify everything works)

### Validation Test (5-10 minutes)
1. Run test path 5 times
2. All similar results?
   - Yes → Good, tuning is stable
   - No → Adjust values and retest

## Deployment Cycle

Each iteration takes ~5 minutes:
1. **Change one value** in CommandSwerveDrivetrain.java
2. **Save file** (Ctrl+S)
3. **Deploy** (Right-click → Run As → WPILib Deploy)
4. **Wait** for "BUILD SUCCESSFUL"
5. **Test** robot on test path
6. **Record** results
7. **Repeat** with new value

## Success Criteria

You're done when:
- ✓ Straight paths are followed smoothly
- ✓ Turns are smooth without bounce
- ✓ Reaches targets reliably
- ✓ Results are consistent (runs 1, 2, 3 similar)
- ✓ Happy with performance

Typically: 30-60 minutes of testing total

## If You Get Stuck

### "Robot too slow"
1. Open PATHPLANNER_PID_QUICK_REF.md
2. Find "Robot is SLOW" section
3. Follow the fix

### "Robot oscillates"
1. Open PATHPLANNER_VISUAL_GUIDE.md
2. Find oscillation symptom
3. Follow the fix

### "Don't know how to test"
1. Open PATHPLANNER_TESTING_GUIDE.md
2. Follow "Testing Procedure" section step-by-step

### "Want complete understanding"
1. Read PATHPLANNER_PID_TUNING.md thoroughly
2. Reference PATHPLANNER_PID_COMPLETE.md
3. Use PATHPLANNER_VISUAL_GUIDE.md to visualize

## Documentation Storage

All guides are in your project root directory:
```
2026-Rebuilt-3297/
├── PATHPLANNER_PID_INDEX.md
├── PATHPLANNER_PID_QUICK_REF.md
├── PATHPLANNER_PID_COMPLETE.md
├── PATHPLANNER_PID_TUNING.md
├── PATHPLANNER_TESTING_GUIDE.md
├── PATHPLANNER_VISUAL_GUIDE.md
└── PATHPLANNER_SUMMARY.md (this file)
```

Plus code to modify:
```
src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java
```

## Final Advice

1. **Start conservative** - You can always increase P later
2. **Change one thing at a time** - So you know what worked
3. **Test multiple times** - To confirm consistency
4. **Document everything** - For next season reference
5. **Don't over-tune** - "Good enough" works
6. **Use your vision system** - Limelight helps a lot
7. **Have fun** - It's satisfying when it works!

## Quick Modification Example

To change translation P from 2.0 to 2.5:

Find this in CommandSwerveDrivetrain.java:
```java
new PPHolonomicDriveController(
    new PIDConstants(2, 0, 0),        // ← Change this
    new PIDConstants(2.5, 0, 0.5)
)
```

Change to:
```java
new PPHolonomicDriveController(
    new PIDConstants(2.5, 0, 0),      // ← Changed from 2 to 2.5
    new PIDConstants(2.5, 0, 0.5)
)
```

Save, deploy, test. Done!

## Estimated Timeline

| Task | Time |
|------|------|
| Read one quick guide | 5 min |
| First code change | 2 min |
| Deploy | 3 min |
| Test one path | 5 min |
| Evaluate & adjust | 3 min |
| **One iteration cycle** | **~18 min** |
| Multiple tuning sessions | 1-3 hours total |

## Success Stories

A well-tuned bot can:
- ✓ Follow paths within ±0.1m
- ✓ Turn smoothly without bounce
- ✓ Complete autonomous consistently
- ✓ Be competitive in matches

## Let's Get Started!

**Pick one:**
1. **Quick Start:** Read PATHPLANNER_PID_QUICK_REF.md
2. **Detailed:** Read PATHPLANNER_PID_COMPLETE.md
3. **Thorough:** Read PATHPLANNER_PID_TUNING.md

Then deploy, test, and iterate!

🤖 Good luck! 🚀
