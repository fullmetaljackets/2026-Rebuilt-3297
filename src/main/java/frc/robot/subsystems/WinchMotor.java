package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;


public class WinchMotor extends SubsystemBase{
    
    private TalonFX WinchMotor;
    private TalonFXConfiguration TalonFXConfig;
    private MotorOutputConfigs MotorOutputConfig;
    final MotionMagicExpoTorqueCurrentFOC m_vdReq = new MotionMagicExpoTorqueCurrentFOC(0);
    private int m_printCount = 0;

    public WinchMotor() {
        TalonFXConfig = new TalonFXConfiguration();
        MotorOutputConfig = new MotorOutputConfigs();
        MotorOutputConfig.Inverted = InvertedValue.CounterClockwise_Positive;
        MotorOutputConfig.NeutralMode = NeutralModeValue.Coast;
        TalonFXConfig.withMotorOutput(MotorOutputConfig);
        WinchMotor = new TalonFX(13, "DriveCan");
        WinchMotor.getConfigurator().apply(TalonFXConfig);
        
        FeedbackConfigs fdc = TalonFXConfig.Feedback;
        fdc.SensorToMechanismRatio = 1.833; // 1 rotation of the sensor results in 1.7143 rotations of the mechanism

        SoftwareLimitSwitchConfigs softLimit =TalonFXConfig.SoftwareLimitSwitch;
        softLimit.ForwardSoftLimitEnable = true;
        softLimit.ForwardSoftLimitThreshold = -2;
        softLimit.ReverseSoftLimitEnable = true;
        softLimit.ReverseSoftLimitThreshold = -10;


        Slot0Configs slot0 = TalonFXConfig.Slot0;
        slot0.kS = 1; // Add 0.25 V output to overcome static friction
        slot0.kV = 0; // A velocity target of 1 rps results in 0.12 V output
        slot0.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output
        slot0.kP = 140; // A position error of 0.2 rotations results in 12 V output
        slot0.kI = 0; // No output for integrated error
        slot0.kD = 9; // A velocity error of 1 rps results in 0.5 V output

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

    public double getWinchPosition(){
        SmartDashboard.putNumber("Winch Position", WinchMotor.getPosition().getValueAsDouble());
        return WinchMotor.getPosition().getValueAsDouble();
    }

    // public void setWinchSpeed(double velSetpoint, double accSetpoint){
    //     WinchMotor.setControl(m_vdReq.(velSetpoint).withSlot(0).withAcceleration(accSetpoint).withSlot(0));
    // }

    public void WinchMotorRun(double setpoint){
        WinchMotor.set(setpoint);
        // IntakeMotor2.set(setpoint);
    }

    public void setMy_WinchMotor(double setpoint){
        SmartDashboard.putNumber("setpoint", setpoint);
        WinchMotor.setControl(m_vdReq.withPosition(setpoint).withSlot(0));
    }

    public boolean atTargetPosition(double setpoint, double tolerance){
        SmartDashboard.putNumber("Winch Pos-setPoint", Math.abs(getWinchPosition() - setpoint));
        SmartDashboard.putNumber("Winch tolerance", tolerance);
        return Math.abs(getWinchPosition() - setpoint) <= tolerance;
    }

    
    private final SysIdRoutine m_sysIdRoutine = new SysIdRoutine(
        new SysIdRoutine.Config(
            null,        // Use default ramp rate (1 V/s)
            Volts.of(6), // Use dynamic step voltage of 6 V
            null,        // Use default timeout (10 s)
            // Log state with SignalLogger class
            state -> SignalLogger.writeString("SysIdWinch_State", state.toString())
        ),
        new SysIdRoutine.Mechanism(
            output -> this.setVoltageForSysId(output.in(Volts)),
            log -> {
                log.motor("Winch")
                    .voltage(Volts.of(WinchMotor.getMotorVoltage().getValueAsDouble()))
                    .angularPosition(Rotations.of(WinchMotor.getPosition().getValueAsDouble()))
                    .angularVelocity(RotationsPerSecond.of(WinchMotor.getVelocity().getValueAsDouble()));
            },
            this
        )
    );

    /**
     * Sets the voltage for SysId testing
     * @param volts The voltage to apply
     */
    public void setVoltageForSysId(double volts) {
        WinchMotor.setVoltage(volts);
    }

    /**
     * Returns a command for the SysId quasistatic test in the forward direction
     */
    public edu.wpi.first.wpilibj2.command.Command sysIdQuasistatic(edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    /**
     * Returns a command for the SysId dynamic test in the forward direction
     */
    public edu.wpi.first.wpilibj2.command.Command sysIdDynamic(edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }



}
