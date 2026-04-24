// package frc.robot.commands.grouped;

// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import frc.robot.commands.BackIntakeRunPercentage;
// import frc.robot.commands.IntakeRunPercentage;
// import frc.robot.subsystems.BackIntakeMotor;
// import frc.robot.subsystems.IntakeMotor;
// import frc.robot.subsystems.WinchMotor;

// public class IntakeRun extends ParallelCommandGroup{
//     public IntakeRun(IntakeMotor s_IntakeMotor, BackIntakeMotor s_BackIntakeMotor, WinchMotor s_WinchMotor){
//         if (s_WinchMotor.getWinchPosition() < -2){
//             addCommands(
//                 new BackIntakeRunPercentage(-0.65, s_BackIntakeMotor)
//             );
//         }else{
//             addCommands(
//                 new IntakeRunPercentage(0.65, s_IntakeMotor),
//                 new BackIntakeRunPercentage(-0.65, s_BackIntakeMotor)
//             );
//         }
//     }

// }
