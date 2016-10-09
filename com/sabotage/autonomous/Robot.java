package com.sabotage.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.robocol.Telemetry;


public class Robot {

    public int HARDWARE_DELAY = 10;
    public int loopCounter;
    public Telemetry telemetry;


    public static final double SERVO_DEBRIS_FORWARD = .99;
    public static final double SERVO_DEBRIS_REVERSE = .01;
    public static final double SERVO_DEBRIS_STOP = .5;
    public static final double SERVO_CLIMBER_UNLOAD_POSITION = 0;
    public static final double SERVO_CLIMBER_REST_POSITION = .8;



    public static final double SERVO_HANGING_RELEASE_STICK = .8;
    public static final double SERVO_HANGING_HOLD_STICK = .3;



    public static final double SERVO_ZIPLINE_RIGHT_REST_POSITION = .7;
    public static final double SERVO_ZIPLINE_RIGHT_ON_POSITION = 0;


    public static final double SERVO_ZIPLINE_LEFT_REST_POSITION = .4;
    public static final double SERVO_ZIPLINE_LEFT_ON_POSITION = 1;


    public static final double SERVO_UNLOAD_DEBRIS_OPEN = .9;
    public static final double SERVO_UNLOAD_DEBRIS_CLOSED = .5;

    public static final double MOTOR_DEBRIS_PICKUP = -.5;
    public static final double MOTOR_DEBRIS_EJECT = .5;


    public static final double SERVO_BUMPER_RIGHT_UP_NO_PRESS_POSITION = 1.0;
    public static final double SERVO_BUMPER_RIGHT_DOWN_NO_PRESS_POSITION = .42;
    public static final double SERVO_BUMPER_RIGHT_PRESS_POSITION = .35;

    public static final double SERVO_BUMPER_LEFT_UP_NO_PRESS_POSITION = 0;
    public static final double SERVO_BUMPER_LEFT_DOWN_NO_PRESS_POSITION = .61;
    public static final double SERVO_BUMPER_LEFT_PRESS_POSITION = .71;

    public DcMotor motorRight;
    public DcMotor motorLeft;
    public DcMotor motorArm;

    public ColorSensor colorSensorRight;
    public ColorSensor colorSensorLeft;
    public ColorSensor colorSensorCenter;
    public TouchSensor touchSensor;


    public ModernRoboticsI2cGyro gyroSensor;
    public Servo servoUnloadClimbers;

    public Servo servoDebrisRight;
    public Servo servoDebrisLeft;
    public Servo servoUnloadDebris;
    public Servo servoZiplineRight;

    public Servo servoZiplineLeft;


    public ColorSensor colorSensorBeaconRight;
    public Servo servoBumperRight;
    public Servo servoBumperLeft;


    public DcMotor motorDebris;


    public static enum TurnEnum {

        RIGHT,

        LEFT


    }


    public static enum ColorEnum {

        RED,

        BLUE,

        WHITE


    }

    public static enum ControlDebrisEnum {

        PICK_UP,

        EJECT,

        OFF


    }

    public static enum MotorPowerEnum {

        LowLow(0.1),

        Low(0.2),

        Med(0.6),

        High(0.8),

        FTL(1.0);

        private double motorPower;

        MotorPowerEnum(double motorPower) {
            this.motorPower = motorPower;

        }

        public double getValue() {
            return this.motorPower;

        }

    }


}


