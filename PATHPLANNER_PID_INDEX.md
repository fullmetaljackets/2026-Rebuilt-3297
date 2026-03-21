# PathPlanner PID Tuning - Documentation Index

## Available Guides

I've created comprehensive guides to help you tune your PathPlanner PID values. Here's what each covers:

### 1. **PATHPLANNER_PID_QUICK_REF.md** ⭐ START HERE
**Best for:** Quick decision-making during practice
- Copy-paste preset configurations
- Quick diagnostic flowchart
- When-to-stop checklist
- 5-minute read

### 2. **PATHPLANNER_PID_COMPLETE.md** 
**Best for:** Understanding the full picture
- Complete PID explanation
- Real-world symptoms and solutions
- Code modification guide
- Success indicators
- 10-minute read

### 3. **PATHPLANNER_PID_TUNING.md**
**Best for:** Deep understanding of PID control
- Detailed explanation of P, I, D
- Starting points by robot speed
- Step-by-step tuning procedures
- Advanced tips and tricks
- 15-minute read

### 4. **PATHPLANNER_TESTING_GUIDE.md**
**Best for:** Structured testing procedures
- Setting up dashboard monitoring
- Creating test paths
- Full testing procedures
- Logging template
- Common issues & solutions
- 20-minute read

## Quick Start

### If you have 2 minutes:
1. Open **PATHPLANNER_PID_QUICK_REF.md**
2. Find your symptom in the diagnostic
3. Apply the suggested fix
4. Test and iterate

### If you have 10 minutes:
1. Read **PATHPLANNER_PID_COMPLETE.md**
2. Understand what P, I, D do
3. Try the "Balanced" preset
4. Test your auto path

### If you have 30 minutes:
1. Read **PATHPLANNER_PID_TUNING.md**
2. Follow the step-by-step tuning
3. Create test paths using **PATHPLANNER_TESTING_GUIDE.md**
4. Run the full testing sequence

## Your Current Configuration

```java
// File: CommandSwerveDrivetrain.java, line ~225
new PPHolonomicDriveController(
    new PIDConstants(2, 0, 0),        // Translation P=2.0
    new PIDConstants(2.5, 0, 0.5)     // Rotation P=2.5, D=0.5
)
```

**Status:** Reasonable baseline, can be improved

**Recommended first step:**
```java
new PPHolonomicDriveController(
    new PIDConstants(2.5, 0, 0.1),    // Increase P, add D
    new PIDConstants(2.5, 0, 0.7)     // Increase D for smoother turns
)
```

## The Tuning Process in One Picture

```
START: Current values
  ↓
Test straight-line path
  ↓
Robot slow? → Increase translation P
  ↓
Robot jerky? → Increase translation D
  ↓
Test 90° turn
  ↓
Turn slow? → Increase rotation P
  ↓
Turn overshoots? → Increase rotation D
  ↓
Test full auto
  ↓
Looks good? → DONE! Document values
  ↓
Issues? → Go back to specific step
```

## Key Concepts (TL;DR)

| Concept | What It Does | How to Adjust |
|---------|--------------|---------------|
| **P (Proportional)** | Makes robot respond to error | Increase if slow, decrease if jerky |
| **D (Derivative)** | Smooths out movement | Increase if overshoots, decrease if sluggish |
| **I (Integral)** | Fixes steady errors | Leave at 0 (rarely needed) |

## Testing Checklist

Before you start tuning:
- [ ] Code deployed successfully
- [ ] Robot has power
- [ ] PathPlanner auto paths created
- [ ] Dashboard open
- [ ] Robot placed at starting position

During testing:
- [ ] Changed ONE value at a time
- [ ] Tested multiple times (at least 3)
- [ ] Documented results
- [ ] Ran simple paths before complex ones

## Common Questions

**Q: Which should I tune first?**
A: Translation (P), then Translation (D), then Rotation (P), then Rotation (D)

**Q: How much should I change per iteration?**
A: P by 0.5, D by 0.1 (small increments)

**Q: When should I stop tuning?**
A: When robot follows paths smoothly and consistently

**Q: Will tuning on my practice field work in competition?**
A: Usually, but expect slight differences due to field friction

**Q: Should I use I gain?**
A: No, leave at 0 (PathPlanner is specifically designed for I=0)

**Q: My values work on blue side but not red side**
A: Ensure PathPlanner is flipping the path correctly

## File Locations

All guides are in the project root:
```
/PATHPLANNER_PID_QUICK_REF.md       ← Quick reference
/PATHPLANNER_PID_COMPLETE.md        ← Full guide
/PATHPLANNER_PID_TUNING.md          ← Deep dive
/PATHPLANNER_TESTING_GUIDE.md       ← Testing procedures
/PATHPLANNER_PID_INDEX.md           ← This file
```

Code to modify:
```
/src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java (line ~225)
```

## Need Help?

### For Quick Answers
→ Check **PATHPLANNER_PID_QUICK_REF.md**

### For Understanding Why
→ Read **PATHPLANNER_PID_COMPLETE.md**

### For Step-by-Step Tuning
→ Follow **PATHPLANNER_PID_TUNING.md**

### For Testing Procedures
→ Use **PATHPLANNER_TESTING_GUIDE.md**

## Success Metrics

Your tuning is successful when:

- ✓ Path deviation < 0.1m
- ✓ Angle deviation < 5°
- ✓ Movement is smooth (no jerks)
- ✓ Consistent results across multiple runs
- ✓ Works on both Blue and Red alliance
- ✓ Full auto completes without issues

## Next Steps

1. **Choose your guide** based on available time
2. **Pick a test path** from PATHPLANNER_TESTING_GUIDE.md
3. **Modify one value** in CommandSwerveDrivetrain.java
4. **Deploy and test** (5-10 minute cycle)
5. **Document results** in your log
6. **Iterate** until satisfied

## Final Notes

- You don't need perfect tuning to be competitive
- A consistent, slightly conservative setup beats an unstable aggressive one
- Test on the actual field when possible
- Document your final values for next season
- Your vision odometry (Limelight) helps path accuracy significantly

Good luck! 🚀
