package com.sabotage.autonomous.steps;

import android.util.Log;

import com.sabotage.autonomous.Robot;

public class Step_ReCalibrateGyroIfNeeded implements StepInterface {


    private Robot robot;

    private static final double CALIBRATION_TOLERANCE = 2.5;

    private boolean encodersReset = false;
    private boolean initialized = false;
    private int delayUntilLoopCount = 0;


    @Override
    public String getLogKey() {
        return "Step_ReCalibrateGyroIfNeeded";
    }

    @Override
    public void runStep() {


        if (isStillWaiting()) {
            return;
        }

        if (isCalibrationBad()) {

            robot.gyroSensor.calibrate();
            setLoopDelay();
        }


        logIt("runStep: ");
    }


    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }


    private boolean isNotCalibrating() {

        return robot.gyroSensor.isCalibrating() == false;

    }

    private boolean isCalibrationBad() {


        if (isNotCalibrating() && (Math.abs(robot.gyroSensor.getIntegratedZValue()) > CALIBRATION_TOLERANCE)) {

            Log.i(getLogKey(), "RECALIBRATE!!!!.." + robot.loopCounter);
            return true;
        }
        Log.i(getLogKey(), "NO CALIBRATE!!!!.." + robot.loopCounter + " " + Math.abs(robot.gyroSensor.getIntegratedZValue()));

        return false;
    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    @Override
    public boolean isStepDone() {

        if (isStillWaiting() || robot.gyroSensor.isCalibrating()) {

            return false;
        }


        return true;
    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);


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

