package com.sabotage.autonomous.steps;

import android.util.Log;

import com.sabotage.autonomous.Robot;

public class Step_ControlDebris implements StepInterface {


    private Robot robot;


    private boolean encodersReset = false;
    private boolean initialized = false;
    private int delayUntilLoopCount = 0;

    private Robot.ControlDebrisEnum controlDebrisEnum;


    public Step_ControlDebris(Robot.ControlDebrisEnum controlDebrisEnum) {

        this.controlDebrisEnum = controlDebrisEnum;

    }

    @Override
    public String getLogKey() {
        return "Step_ControlDebris";
    }

    @Override
    public void runStep() {


        if (isStillWaiting()) {
            return;
        }

        if (initialized == false) {
            switch (controlDebrisEnum) {

                case EJECT:
                    controlDebrisToEject();
                    break;

                case PICK_UP:
                    controlDebrisToPickup();
                    break;

                case OFF:
                    stopDebrisPickup();
                    break;

            }

            initialized = true;
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

    private void controlDebrisToEject() {

        robot.motorDebris.setPower(Robot.MOTOR_DEBRIS_EJECT);
        robot.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_FORWARD);
        robot.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_REVERSE);
        robot.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_CLOSED);
    }


    private void controlDebrisToPickup() {

        robot.motorDebris.setPower(Robot.MOTOR_DEBRIS_PICKUP);
        robot.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_REVERSE);
        robot.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_FORWARD);
        robot.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_CLOSED);
    }


    private void stopDebrisPickup() {

        robot.motorDebris.setPower(0);
        robot.servoDebrisRight.setPosition(Robot.SERVO_DEBRIS_STOP);
        robot.servoDebrisLeft.setPosition(Robot.SERVO_DEBRIS_STOP);
        robot.servoUnloadDebris.setPosition(Robot.SERVO_UNLOAD_DEBRIS_CLOSED);
    }

    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    @Override
    public boolean isStepDone() {

        if (isStillWaiting()) {

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

