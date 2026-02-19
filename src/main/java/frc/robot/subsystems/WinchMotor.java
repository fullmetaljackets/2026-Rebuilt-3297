package frc.robot.subsystems;


import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class WinchMotor extends SubsystemBase{
    
    private TalonFX WinchMotor;
    private TalonFXConfiguration TalonFXConfig;
    private MotorOutputConfigs MotorOutputConfig;
    final MotionMagicVelocityVoltage m_vdReq = new MotionMagicVelocityVoltage(0);
    private int m_printCount = 0;

    public WinchMotor() {
        TalonFXConfig = new TalonFXConfiguration();
        MotorOutputConfig = new MotorOutputConfigs();
        MotorOutputConfig.Inverted = InvertedValue.CounterClockwise_Positive;
        MotorOutputConfig.NeutralMode = NeutralModeValue.Coast;
        TalonFXConfig.withMotorOutput(MotorOutputConfig);
        WinchMotor = new TalonFX(12, "DriveCan");
        WinchMotor.getConfigurator().apply(TalonFXConfig);
        
        Slot0Configs slot0 = TalonFXConfig.Slot0;
        slot0.kS = 0.19; // Add 0.25 V output to overcome static friction
        slot0.kV = 0.113; // A velocity target of 1 rps results in 0.12 V output
        slot0.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output
        slot0.kP = 0.3; // A position error of 0.2 rotations results in 12 V output
        slot0.kI = 0; // No output for integrated error
        slot0.kD = 0; // A velocity error of 1 rps results in 0.5 V output

        // set Motion Magic Velocity settings
        MotionMagicConfigs motionMagicConfigs = TalonFXConfig.MotionMagic;
        motionMagicConfigs.MotionMagicAcceleration = 10;
        motionMagicConfigs.MotionMagicJerk = 100;

        StatusCode status = StatusCode.StatusCodeNotInitialized;
        for (int i = 0; i < 5; ++i) {
            status = WinchMotor.getConfigurator().apply(TalonFXConfig);
            if (status.isOK()) break;
        }
        if (!status.isOK()) {
            System.out.println("Could not configure device. Error: " + status.toString());
        }
    }

 
    public void periodic() {
                if (m_printCount++ > 10) {
            m_printCount = 0;
            SmartDashboard.putNumber("Velocity", WinchMotor.getVelocity().getValueAsDouble());
          }

    }
    public void setWinchSpeed(double velSetpoint, double accSetpoint){
        WinchMotor.setControl(m_vdReq.withVelocity(velSetpoint).withSlot(0).withAcceleration(accSetpoint).withSlot(0));
      }

    public void IntakeMotorOneRun(double setpoint){
        WinchMotor.set(setpoint);
        // IntakeMotor2.set(setpoint);
    }
        


}
