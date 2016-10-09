package com.sabotage.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Servo;
import com.sabotage.autonomous.Robot;

public class Step_UnloadClimbers implements StepInterface {

    private static final int SERVO_REPOSITIONING_DELAY = 80;
    private Robot robot;

    private boolean returnedToRestPosition = false;
    private boolean unloadedClimbers = false;

    private int delayUntilLoopCount = 0;
    private double targetPosition = 0;

    //constructor
    public Step_UnloadClimbers() {

    }
    @Override
    public String getLogKey() {
        return "Step_UnloadClimbers";
    }

    @Override
    public void runStep() {

        unloadClimbers();

        if (isStillWaiting()) {
            return;
        }


        returnToRestPosition();

        logIt("runStep:");

    }

    private void returnToRestPosition() {
        if (returnedToRestPosition == false) {
            Log.w(getLogKey(), "returnToRestPosition..." + robot.loopCounter);
            robot.servoUnloadClimbers.setDirection(Servo.Direction.FORWARD);
            robot.servoUnloadClimbers.setPosition(Robot.SERVO_CLIMBER_REST_POSITION);
//            setLoopDelay();
            returnedToRestPosition = true;
        }
    }

    private void unloadClimbers() {

        if (unloadedClimbers == false) {
            Log.w(getLogKey(), "unloadClimbers..." + robot.loopCounter);
            robot.servoUnloadClimbers.setDirection(Servo.Direction.FORWARD);
            robot.servoUnloadClimbers.setPosition(Robot.SERVO_CLIMBER_UNLOAD_POSITION);
            setLoopDelay();
            unloadedClimbers = true;
        }
    }

    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + SERVO_REPOSITIONING_DELAY;
    }

    @Override
    public boolean isStepDone() {


        if (isStillWaiting() || unloadedClimbers == false || returnedToRestPosition == false) {
            return false;
        }
        logIt("isStepDone:");
        return true;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
        sb.append(" , CurrentPosition:" + robot.servoUnloadClimbers.getPosition());
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
