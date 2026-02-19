package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.SignalLogger;
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
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;


public class ShooterMotor extends SubsystemBase{
    
    private TalonFX ShooterMotor;
    private TalonFXConfiguration TalonFXConfig;
    private MotorOutputConfigs MotorOutputConfig;
    final MotionMagicVelocityVoltage m_vdReq = new MotionMagicVelocityVoltage(0);
    private int m_printCount = 0;

    public ShooterMotor() {
        TalonFXConfig = new TalonFXConfiguration();
        MotorOutputConfig = new MotorOutputConfigs();
        MotorOutputConfig.Inverted = InvertedValue.CounterClockwise_Positive;
        MotorOutputConfig.NeutralMode = NeutralModeValue.Coast;
        TalonFXConfig.withMotorOutput(MotorOutputConfig);
        ShooterMotor = new TalonFX(12, "DriveCan");
        ShooterMotor.getConfigurator().apply(TalonFXConfig);


        Slot0Configs slot0 = TalonFXConfig.Slot0;
        slot0.kS = 0.037495; // Add 0.25 V output to overcome static friction
        slot0.kV = 0.11982; // A velocity target of 1 rps results in 0.12 V output
        slot0.kA = 0.034908; // An acceleration of 1 rps/s requires 0.01 V output
        slot0.kP = 0.3; // A position error of 0.2 rotations results in 12 V output
        slot0.kI = 0; // No output for integrated error
        slot0.kD = 0; // A velocity error of 1 rps results in 0.5 V output

        // Slot0Configs slot0 = TalonFXConfig.Slot0;
        // slot0.kS = 0.19; // Add 0.25 V output to overcome static friction
        // slot0.kV = 0.113; // A velocity target of 1 rps results in 0.12 V output
        // slot0.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output
        // slot0.kP = 0.3; // A position error of 0.2 rotations results in 12 V output
        // slot0.kI = 0; // No output for integrated error
        // slot0.kD = 0; // A velocity error of 1 rps results in 0.5 V output

        // set Motion Magic Velocity settings
        MotionMagicConfigs motionMagicConfigs = TalonFXConfig.MotionMagic;
        motionMagicConfigs.MotionMagicAcceleration = 10;
        motionMagicConfigs.MotionMagicJerk = 100;

        StatusCode status = StatusCode.StatusCodeNotInitialized;
        for (int i = 0; i < 5; ++i) {
            status = ShooterMotor.getConfigurator().apply(TalonFXConfig);
            if (status.isOK()) break;
        }
        if (!status.isOK()) {
            System.out.println("Could not configure device. Error: " + status.toString());
        }
    }

 
    public void periodic() {
                if (m_printCount++ > 10) {
            m_printCount = 0;
            SmartDashboard.putNumber("Velocity", ShooterMotor.getVelocity().getValueAsDouble());
          }

    }
    public void setShooterSpeed(double velSetpoint, double accSetpoint){
        ShooterMotor.setControl(m_vdReq.withVelocity(velSetpoint).withSlot(0).withAcceleration(accSetpoint).withSlot(0));
      }

    public void IntakeMotorOneRun(double setpoint){
        ShooterMotor.set(setpoint);
        // IntakeMotor2.set(setpoint);
    }

    private final SysIdRoutine m_sysIdRoutine = new SysIdRoutine(
        new SysIdRoutine.Config(
            null,        // Use default ramp rate (1 V/s)
            Volts.of(6), // Use dynamic step voltage of 6 V
            null,        // Use default timeout (10 s)
            // Log state with SignalLogger class
            state -> SignalLogger.writeString("SysIdShooter_State", state.toString())
        ),
        new SysIdRoutine.Mechanism(
            output -> this.setVoltageForSysId(output.in(Volts)),
            log -> {
                log.motor("Shooter")
                    .voltage(Volts.of(ShooterMotor.getMotorVoltage().getValueAsDouble()))
                    .angularPosition(Rotations.of(ShooterMotor.getPosition().getValueAsDouble()))
                    .angularVelocity(RotationsPerSecond.of(ShooterMotor.getVelocity().getValueAsDouble()));
            },
            this
        )
    );

    /**
     * Sets the voltage for SysId testing
     * @param volts The voltage to apply
     */
    public void setVoltageForSysId(double volts) {
        ShooterMotor.setVoltage(volts);
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
