package com.sabotage.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.sabotage.autonomous.Robot;


public class Step_TrackWhiteLine implements StepInterface {

    private static final double HIGH_SPEED = 1;
    private static final double MEDIUM_SPEED = .23;
    private static final double MEDIUM_CORRECTION_SPEED = .3;
    private static final double LOW_SPEED = .1;
    private static final double ZERO_SPEED = 0;

    private static final int WHITE_LINE_DETECTED_VALUE = 80;
    private static final int RED_LIGHT_DETECTED_VALUE = 2;
    private static final int BLUE_LIGHT_DETECTED_VALUE = 2;

    private static final int WAIT_TIME_FOR_BUTTON_TO_BE_PRESSED_MS = 2000;
    private static final int TARGET_ANGLE = 90;

    private static final int BLUE_ALLIANCE_TURN_GONE_TOO_FAR_ANGLE = TARGET_ANGLE + 20;
    private static final int RED_ALLIANCE_TURN_GONE_TOO_FAR_ANGLE = TARGET_ANGLE - 20;

    private static final int BLUE_ALLIANCE_TURN_CORRECTION_ANGLE = TARGET_ANGLE - 10;
    private static final int RED_ALLIANCE_TURN_CORRECTION_ANGLE = TARGET_ANGLE + 5;

    private Robot.ColorEnum allianceColorEnum;
    protected Robot robot;
    private int delayUntilLoopCount = 0;
    private boolean initialized = false;
    private boolean missedWhiteLine = false;

    private boolean bumpersColorPositioned = false;
    private Long timeRecorded_bumpersColorPositionedAtTimeMS = null;

    //constructor
    public Step_TrackWhiteLine(Robot.ColorEnum allianceColorEnum) {

        this.allianceColorEnum = allianceColorEnum;
    }

    @Override
    public String getLogKey() {
        return "Step_TrackWhiteLine";
    }
    @Override
    public void runStep() {


        initializeStep();
        positionButtonBumpers();

        if (isWaiting()) {
            return;
        }


        if (hasMissedWhiteLine()) {

            turnBackForSecondChanceToFindWhiteLine();

        } else {

            trackWhiteLineToBeacon();
        }

        logSensorData();


    }


    private void initializeStep() {

        if (initialized == false) {


            Log.w(getLogKey(), "initializeStep..." + robot.loopCounter);


            robot.motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            robot.motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

            robot.motorLeft.setDirection(DcMotor.Direction.FORWARD);
            robot.motorRight.setDirection(DcMotor.Direction.REVERSE);

            initializeBeaconBumpers();
            setLoopDelay(10);
            initialized = true;


        }
    }


    private void turnBackForSecondChanceToFindWhiteLine() {


        if (isBlueTurnStillCorrecting() || isRedTurnStillCorrecting()) {

            robot.motorLeft.setPower(-HIGH_SPEED);
            robot.motorRight.setPower(MEDIUM_CORRECTION_SPEED);

            Log.w(getLogKey(), "turnBackForSecondChanceToFindWhiteLine()" + robot.loopCounter
                    + " angle: " + getGyroAngle()
                    + " WhiteLine:" + robot.colorSensorCenter.green());

        } else {

            this.missedWhiteLine = false;
        }

    }


    private void trackWhiteLineToBeacon() {


        if (isCenterSensorOnWhiteLine()) {

            moveForwardOffTheLine();

        } else {

            moveForwardBackToLine();

        }
    }

    protected boolean hasMissedWhiteLine() {

        if (this.missedWhiteLine) {
            return true;
        }

        if (hasBlueMissedWhiteLine() || hasRedMissedWhiteLine()) {

            Log.w(getLogKey() + "_M", "MISSED WhiteLine!!!!" + robot.loopCounter
                    + " angle: " + getGyroAngle()
                    + " WhiteLine:" + robot.colorSensorCenter.green());

            this.missedWhiteLine = true;
            return true;
        }

        return false;
    }

    private boolean hasRedMissedWhiteLine() {

        return isRedAlliance() && getGyroAngle() < RED_ALLIANCE_TURN_GONE_TOO_FAR_ANGLE;
    }

    private boolean hasBlueMissedWhiteLine() {

        return isBlueAlliance() && getGyroAngle() > BLUE_ALLIANCE_TURN_GONE_TOO_FAR_ANGLE;
    }


    private boolean isCenterSensorOnWhiteLine() {

        if (robot.colorSensorCenter.green() > WHITE_LINE_DETECTED_VALUE) {

            return true;
        }

        return false;
    }


    private void moveForwardOffTheLine() {

        robot.motorLeft.setPower(ZERO_SPEED);
        robot.motorRight.setPower(MEDIUM_SPEED);

        Log.w(getLogKey(), "moveForwardOffTheLine()" + robot.loopCounter
                + " angle: " + getGyroAngle()
                + " WhiteLine:" + robot.colorSensorCenter.green());
    }

    private void moveForwardBackToLine() {

        robot.motorLeft.setPower(MEDIUM_SPEED);
        robot.motorRight.setPower(ZERO_SPEED);

        Log.w(getLogKey(), "moveForwardBackToLine()" + robot.loopCounter
                + " angle: " + getGyroAngle()
                + " WhiteLine:" + robot.colorSensorCenter.green());
    }


    private boolean isDoneWaitingForButtonToBePushed() {


        return (bumpersColorPositioned &&

                System.currentTimeMillis() > timeRecorded_bumpersColorPositionedAtTimeMS + WAIT_TIME_FOR_BUTTON_TO_BE_PRESSED_MS);

    }

    private void positionButtonBumpers() {

        if (this.bumpersColorPositioned) {
            return;
        }


        if (isRightSideCorrectColor()) {

            robot.servoBumperRight.setPosition(Robot.SERVO_BUMPER_RIGHT_PRESS_POSITION);
            robot.servoBumperLeft.setPosition(Robot.SERVO_BUMPER_LEFT_DOWN_NO_PRESS_POSITION);
            timeRecorded_bumpersColorPositionedAtTimeMS = System.currentTimeMillis();
            bumpersColorPositioned = true;

            robot.motorRight.setPower(LOW_SPEED);
            robot.motorLeft.setPower(LOW_SPEED);

            setLoopDelay(100);

        } else if (isRightSideOppositeColor()) {

            robot.servoBumperLeft.setPosition(Robot.SERVO_BUMPER_LEFT_PRESS_POSITION);
            robot.servoBumperRight.setPosition(Robot.SERVO_BUMPER_RIGHT_DOWN_NO_PRESS_POSITION);
            timeRecorded_bumpersColorPositionedAtTimeMS = System.currentTimeMillis();
            bumpersColorPositioned = true;

            robot.motorRight.setPower(LOW_SPEED);
            robot.motorLeft.setPower(LOW_SPEED);

            setLoopDelay(100);
        }

    }


    private boolean isRightSideCorrectColor() {

        if (isRedAlliance() && isOnlyRedLightDetected()) {

            return true;

        } else if (isBlueAlliance() && isOnlyBlueLightDetected()) {

            return true;
        }

        return false;
    }


    private boolean isRightSideOppositeColor() {

        if (isRedAlliance() && isOnlyBlueLightDetected()) {

            return true;

        } else if (isBlueAlliance() && isOnlyRedLightDetected()) {

            return true;
        }

        return false;
    }


    private boolean isRedAlliance() {
        return Robot.ColorEnum.RED.equals(allianceColorEnum);
    }

    private boolean isBlueAlliance() {
        return Robot.ColorEnum.BLUE.equals(allianceColorEnum);
    }

    private boolean isOnlyRedLightDetected() {

        if (robot.colorSensorBeaconRight.red() >= RED_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeaconRight.blue() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE RED:" + robot.colorSensorBeaconRight.red() + "/" + robot.colorSensorBeaconRight.blue());


            return true;
        }


        return false;
    }


    private boolean isOnlyBlueLightDetected() {


        if (robot.colorSensorBeaconRight.blue() >= BLUE_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeaconRight.red() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE BLUE:" + robot.colorSensorBeaconRight.red() + "/" + robot.colorSensorBeaconRight.blue());

            return true;
        }


        return false;
    }


    private boolean isWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }


    private void initializeBeaconBumpers() {

        robot.servoBumperRight.setPosition(Robot.SERVO_BUMPER_RIGHT_DOWN_NO_PRESS_POSITION);
        robot.servoBumperLeft.setPosition(Robot.SERVO_BUMPER_LEFT_DOWN_NO_PRESS_POSITION);

    }

    private int getGyroAngle() {
        return Math.abs(robot.gyroSensor.getIntegratedZValue());
    }

    private boolean isBlueTurnStillCorrecting() {

        return isBlueAlliance() && getGyroAngle() >= BLUE_ALLIANCE_TURN_CORRECTION_ANGLE;

    }


    private boolean isRedTurnStillCorrecting() {

        return isRedAlliance() && getGyroAngle() <= RED_ALLIANCE_TURN_CORRECTION_ANGLE;
    }

    private void setLoopDelay(int delay) {

        this.delayUntilLoopCount = robot.loopCounter + delay;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


    private void logSensorData() {

        Log.w(getLogKey(), "Beacon ColorSensor Red/Blue:" + robot.colorSensorBeaconRight.red() + "/" + robot.colorSensorBeaconRight.blue());

    }

    @Override
    public boolean isStepDone() {

        if (bumpersColorPositioned && isDoneWaitingForButtonToBePushed()) {

            robot.motorRight.setPower(0);
            robot.motorLeft.setPower(0);

            return true;
        }

        return false;
    }


    @Override
    public boolean isAborted() {
        return false;
    }
}

