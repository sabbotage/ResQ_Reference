package com.sabotage.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.sabotage.autonomous.Robot;

public class Step_FindColorLine implements StepInterface {

    private static final int COLOR_SIGNAL_RED_VALUE = 40;
    private static final int COLOR_SIGNAL_BLUE_VALUE = 35;
    private static final int COLOR_SIGNAL_WHITE_VALUE = 80;
    private static final double MOTOR_POWER = .25;
    private Robot robot;


    private boolean encodersReset = false;
    private boolean initialized = false;
    private int delayUntilLoopCount = 0;

    private boolean achievedColorRight = false;
    private boolean achievedColorLeft = false;

    private Robot.ColorEnum colorEnum;

    //constructor
    public Step_FindColorLine(Robot.ColorEnum colorEnum) {

        this.colorEnum = colorEnum;

    }


    @Override
    public String getLogKey() {
        return "Step_FindColorLine";
    }

    @Override
    public void runStep() {

        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


        initializeStep();


        if (isStillWaiting()) {
            return;
        }

        robot.motorRight.setPower(MOTOR_POWER);
        robot.motorLeft.setPower(MOTOR_POWER);


        logIt("runStep: ");
    }


    private boolean hasFoundRedLine() {


        if (robot.colorSensorRight.red() > COLOR_SIGNAL_RED_VALUE
                ||
                robot.colorSensorLeft.red() > COLOR_SIGNAL_RED_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean hasFoundWhiteLine() {


        if (robot.colorSensorRight.green() > COLOR_SIGNAL_WHITE_VALUE
                ||
                robot.colorSensorLeft.green() > COLOR_SIGNAL_WHITE_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean hasFoundBlueLine() {


        if (robot.colorSensorRight.blue() > COLOR_SIGNAL_BLUE_VALUE
                ||
                robot.colorSensorLeft.blue() > COLOR_SIGNAL_BLUE_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }

    private void initializeStep() {

        if (initialized == false) {
            Log.w(getLogKey(), "initializeStep..." + robot.loopCounter);

            robot.colorSensorRight.enableLed(true);
            robot.colorSensorLeft.enableLed(true);
            setLoopDelay();
            initialized = true;
        }

    }

    private void resetEncodersAndSetMotorDirectionOnlyOnce() {

        if (encodersReset == false) {
            Log.w(getLogKey(), "resetEncodersAndSetMotorDirectionOnlyOnce..." + robot.loopCounter);
            robot.motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            robot.motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


            robot.motorLeft.setDirection(DcMotor.Direction.FORWARD);
            robot.motorRight.setDirection(DcMotor.Direction.REVERSE);


            setLoopDelay();
            encodersReset = true;
        }

    }

    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    @Override
    public boolean isStepDone() {


        if (colorEnum.equals(Robot.ColorEnum.RED)
                && hasFoundRedLine()
                ||

                colorEnum.equals(Robot.ColorEnum.BLUE)
                        && hasFoundBlueLine()
                ||

                colorEnum.equals(Robot.ColorEnum.WHITE)
                        && hasFoundWhiteLine())

        {

            robot.motorRight.setPower(0);
            robot.motorLeft.setPower(0);

            logIt("isStepDone: ");

            return true;
        }

        return false;
    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
        sb.append("  , Left Color Red/Blue:" + robot.colorSensorLeft.red() + "/" + robot.colorSensorLeft.blue());
        sb.append("  , Right Color Red/Blue:" + robot.colorSensorRight.red() + "/" + robot.colorSensorLeft.blue());
        sb.append("  , Results:" + "Left:" + this.achievedColorLeft + " Right:" + this.achievedColorRight);
        sb.append("  , ML" + robot.motorLeft.getPower() + " MR:" + +robot.motorLeft.getPower());


        Log.i(getLogKey(), sb.toString());


    }


    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

}

