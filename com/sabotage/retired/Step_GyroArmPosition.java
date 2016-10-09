package com.sabotage.retired;

import android.util.Log;

import com.sabotage.autonomous.Robot;
import com.sabotage.autonomous.steps.StepInterface;


// TODO: need to protect against going passed out target angle, current it will not recover.
public class Step_GyroArmPosition implements StepInterface {

    private static final double TOLERANCE = 3;
    private static final double LOW_POWER = .1;
    private static final double HIGH_POWER = .4;
    private static final int NEAR_TARGET_ANGLE_VALUE = 20;
    private Robot robot;

    private boolean initialized = false;
    private boolean encodersReset = false;
    private double targetAngle = 0;


    private int delayUntilLoopCount = 0;

    //constructor
    public Step_GyroArmPosition(double angleDegrees) {

        this.targetAngle = angleDegrees;

    }
    @Override
    public String getLogKey() {
        return "Step_GyroArmPosition";
    }

    @Override
    public void runStep() {


        if (isStillWaiting()) {
            return;
        }


        initializeStep();


        if (isLoweringArmRequired()) {

            lowerArmPosition(LOW_POWER);

        } else {
            raiseArmPosition(LOW_POWER);
        }

        logIt("runStep");

    }


    private boolean isLoweringArmRequired() {

        return false;
//        return (robot.gyroSensorArm.getRotation() < targetAngle);
    }

    private boolean isAtTargetAngle() {

        return true;
//        return (Math.abs(robot.gyroSensorArm.getRotation() - targetAngle)) <= TOLERANCE;
    }

    private void lowerArmPosition(double power) {

        robot.motorArm.setPower(-power);
    }

    private void raiseArmPosition(double power) {

        robot.motorArm.setPower(+power);
    }

    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    private void initializeStep() {


        if (initialized == false) {

            initialized = true;
        }

    }


    @Override
    public boolean isStepDone() {


        if (isStillWaiting() || initialized == false) {
            return false;

        }


        if (isAtTargetAngle()) {

            robot.motorArm.setPower(0);
            logIt("isStepDone");
            return true;
        }


        return false;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append("methodName:" + methodName);
        sb.append(" , LoopCounter:" + robot.loopCounter);
        sb.append(" , CurrentAngle:" + Math.abs(robot.gyroSensor.getIntegratedZValue()));
        sb.append(" , TargetAngle:" + targetAngle);
        sb.append(" , RemainingAngle:" + remainingAngle());
        sb.append(" , L Power:" + robot.motorLeft.getPower() + " , R Power:" + robot.motorRight.getPower());

        Log.i(getLogKey(), sb.toString());

    }

    private double remainingAngle() {
        return targetAngle - Math.abs(robot.gyroSensor.getIntegratedZValue());
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
