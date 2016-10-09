/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.sabotage.manualcontrol;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.sabotage.autonomous.Robot;


public class ManualControl extends OpMode {


    private static final String KEY = "Manual";

    private DcMotor motorRight;
    private DcMotor motorLeft;
    private DcMotor motorArm;

    private DcMotor motorHanging;
    private Servo servoHanging;

    private Servo servoDebrisRight;


    private Servo servoUnloadClimbers;
    private Servo servoDebrisLeft;
    private DcMotor motorDebris;


    private Servo servoZiplineRight;
    private Servo servoZiplineLeft;


    private Servo servoBumperRight;
    private Servo servoBumperLeft;


    private Servo servoUnloadDebris;


    double[] scaleDriveArray = {0.0, 0.05, 0.09, 0.10, 0.22, 0.25, 0.31, 0.34,
            0.40, 0.45, 0.50, 0.50, 0.55, 0.55, 0.60, 0.60, 1.00};
    private int loopCounter = 0;


    public ManualControl() {
    }


    @Override
    public void init() {


        this.motorLeft = hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = hardwareMap.dcMotor.get("motorRight");
        this.motorArm = hardwareMap.dcMotor.get("motorArm");
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorRight.setDirection(DcMotor.Direction.FORWARD);
        this.motorDebris = hardwareMap.dcMotor.get("motorDebris");
        this.servoDebrisLeft = hardwareMap.servo.get("servoDebrisLeft");
        this.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_STOP);
        this.servoDebrisRight = hardwareMap.servo.get("servoDebrisRight");
        this.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_STOP);
        this.servoUnloadClimbers = hardwareMap.servo.get("servoUnloadClimbers");
        this.servoUnloadClimbers.setPosition(Robot.SERVO_CLIMBER_REST_POSITION);

        this.motorHanging = hardwareMap.dcMotor.get("motorHanging");
        this.motorHanging.setDirection(DcMotor.Direction.FORWARD);

        this.servoHanging = hardwareMap.servo.get("servoHanging");
        this.servoHanging.setPosition(Robot.SERVO_HANGING_HOLD_STICK);

        this.servoUnloadDebris = hardwareMap.servo.get("servoUnloadDebris");
        this.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_CLOSED);


        this.servoZiplineRight = hardwareMap.servo.get("servoZiplineRight");
        this.servoZiplineRight.setPosition(Robot.SERVO_ZIPLINE_RIGHT_REST_POSITION);

        this.servoZiplineLeft = hardwareMap.servo.get("servoZiplineLeft");
        this.servoZiplineLeft.setPosition(Robot.SERVO_ZIPLINE_LEFT_REST_POSITION);


        this.servoBumperRight = hardwareMap.servo.get("servoBumperRight");
        this.servoBumperLeft = hardwareMap.servo.get("servoBumperLeft");

        this.servoBumperRight.setPosition(Robot.SERVO_BUMPER_RIGHT_PRESS_POSITION);
        this.servoBumperLeft.setPosition(Robot.SERVO_BUMPER_LEFT_PRESS_POSITION);


        // test positions of servos using the init mode.
//        this.servoBumperRight.setPosition(Robot.SERVO_BUMPER_RIGHT_PRESS_POSITION);
//        this.servoBumperLeft.setPosition(Robot.SERVO_BUMPER_LEFT_PRESS_POSITION);


//        this.servoZiplineRight.setPosition(Robot.SERVO_ZIPLINE_RIGHT_REST_POSITION);
//        this.servoZiplineLeft.setPosition(Robot.SERVO_ZIPLINE_LEFT_REST_POSITION);


    }


    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        loopCounter = loopCounter + 1;

        test();


        driver_controlArmAngle();
        driver_controlDriveMotors();


        controlDebrisPickup_Bumpers();


        operator_controlUnloadClimber_X_Button();

        operator_controlUnloadDebris_Y_Button();
        operator_controlHangingMotor_Dpad();
        operator_controlServoHanging_AB_Buttons();
        operator_controlServoZiplineLeft_LeftTrigger();
        operator_controlServoZiplineRight_RightTrigger();

    }


    private void test() {


//       this.servoBumperLeft.setPosition(gamepad2.right_trigger);
//        this.servoBumperRight.setPosition(gamepad2.right_trigger);

//       this.servoBumperLeft .setPosition(gamepad2.right_trigger);
//        Log.i(KEY + "Test", "input dpad_down: " + gamepad2.dpad_down);
//        Log.i(KEY + "Test", "input dpad_up: " + gamepad2.dpad_up);

    }


    private void operator_controlServoZiplineRight_RightTrigger() {

        boolean buttonPressed = (gamepad2.right_trigger > 0.5);


        if (buttonPressed && !isRobotDescendingMountain()) {
            this.servoZiplineRight.setPosition(Robot.SERVO_ZIPLINE_RIGHT_ON_POSITION);

        } else {
            this.servoZiplineRight.setPosition(Robot.SERVO_ZIPLINE_RIGHT_REST_POSITION);
        }

    }


    private void operator_controlServoZiplineLeft_LeftTrigger() {

        boolean buttonPressed = (gamepad2.left_trigger > 0.5);

        if (buttonPressed && !isRobotDescendingMountain()) {
            this.servoZiplineLeft.setPosition(Robot.SERVO_ZIPLINE_LEFT_ON_POSITION);

        } else {
            this.servoZiplineLeft.setPosition(Robot.SERVO_ZIPLINE_LEFT_REST_POSITION);
        }

    }


    private void operator_controlHangingMotor_Dpad() {


        boolean buttonUpPressed = gamepad2.dpad_up;
        boolean buttonDownPressed = gamepad2.dpad_down;




        if (buttonUpPressed) {
        Log.i(KEY + "Test", "input dpad_up: " + gamepad2.dpad_up);
            motorHanging.setPower(+1.0);

        } else if (buttonDownPressed) {
        Log.i(KEY + "Test", "input dpad_down: " + gamepad2.dpad_down);

            motorHanging.setPower(-1.0);
        } else {

            motorHanging.setPower(0);
        }


    }

    private void operator_controlUnloadClimber_X_Button() {

        boolean unloadClimbersRequest = gamepad2.x;

        if (unloadClimbersRequest) {
            this.servoUnloadClimbers.setPosition(Robot.SERVO_CLIMBER_UNLOAD_POSITION);

        } else {
            this.servoUnloadClimbers.setPosition(Robot.SERVO_CLIMBER_REST_POSITION);
        }

    }


    private void operator_controlServoHanging_AB_Buttons() {

        boolean releaseHangingStickRequest = gamepad2.a && gamepad2.b;


        if (releaseHangingStickRequest) {
            this.servoHanging.setPosition(Robot.SERVO_HANGING_RELEASE_STICK);

        } else {
            this.servoHanging.setPosition(Robot.SERVO_HANGING_HOLD_STICK);
        }


    }


    private void operator_controlUnloadDebris_Y_Button() {

        boolean buttonPressed = gamepad2.y;


        if (buttonPressed) {
            this.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_OPEN);

        } else {
            this.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_CLOSED);
        }

    }

    private void controlDebrisPickup_Bumpers() {

        boolean debrisEjectRequest = gamepad2.right_bumper;
        boolean debrisPickupRequest = gamepad2.left_bumper;
        boolean debrisStopRequest = gamepad2.a;


        if (debrisEjectRequest) {
            this.motorDebris.setPower(Robot.MOTOR_DEBRIS_EJECT);
            this.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_FORWARD);
            this.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_REVERSE);
        }
        if (debrisPickupRequest) {
            this.motorDebris.setPower(Robot.MOTOR_DEBRIS_PICKUP);
            this.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_REVERSE);
            this.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_FORWARD);
        }

        if (debrisStopRequest) {
            this.motorDebris.setPower(0);
            this.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_STOP);
            this.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_STOP);
        }


        Log.i(KEY + "_DebrisPickup", "Output BeltDrive: " + String.format("%.2f", motorDebris.getPower())

                        + " servoDebrisRight: " + String.format("%.2f", servoDebrisRight.getPosition())

                        + " servoDebrisLeft: " + String.format("%.2f", servoDebrisLeft.getPosition())
        );

    }

    private void driver_controlDriveMotors() {

        // clip the rightDrive/leftDrive values so that the values never exceed +/- 1
        float rightDriveInput = Range.clip(gamepad1.right_stick_y, -1, 1);
        float leftDriveInput = Range.clip(gamepad1.left_stick_y, -1, 1);


        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        float rightDriveOutput = (float) scaleOutput(rightDriveInput, scaleDriveArray);
        float leftDriveOutput = (float) scaleOutput(leftDriveInput, scaleDriveArray);


        // write the values to the motors
        this.motorRight.setPower(rightDriveOutput);
        this.motorLeft.setPower(leftDriveOutput);

        Log.i(KEY + "_DRIVE", "Output leftDrive: " + String.format("%.2f", leftDriveOutput) + " rightDrive: " + String.format("%.2f", rightDriveOutput));


    }

    private boolean isRobotDescendingMountain() {

        return (this.motorRight.getPower() < 0.0 && this.motorRight.getPower() < 0.0);


    }

    private void driver_controlArmAngle() {

        if (gamepad1.right_bumper) {

            this.motorArm.setPower(.6);

        } else if (gamepad1.left_bumper) {

            this.motorArm.setPower(-.6);

        } else {

            this.motorArm.setPower(0);
        }
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

        telemetry.addData("TextStop", "***Stop happened**" + loopCounter);

    }

    /*
     * This method scales the joystick inputValue so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleOutput(double inputValue, double[] scaleArray) {


        // get the corresponding index for the scaleOutput array.
        int index = (int) (inputValue * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (inputValue < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
