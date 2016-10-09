package com.sabotage.retired;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.sabotage.autonomous.Robot;
import com.sabotage.autonomous.steps.StepInterface;


// TODO: need to protect against going passed out target angle, current it will not recover.
public class Step_GyroTurn implements StepInterface {

    private static final double TOLERANCE = 1;
    private static final double LOW_POWER = .05;
    private static final double HIGH_POWER = .25;
    private static final int NEAR_TARGET_ANGLE_VALUE = 20;
    private  Robot.TurnEnum turnEnum;
    private Robot robot;

    private boolean initialized = false;
    private boolean encodersReset = false;
    private double targetAngle = 0;


    private int delayUntilLoopCount = 0;

    //constructor
    public Step_GyroTurn(double angleDegrees, Robot.TurnEnum turnEnum) {


        this.turnEnum = turnEnum;
        this.targetAngle = angleDegrees;


    }
    @Override
    public String getLogKey() {
        return "Step_GyroTurn";
    }

    @Override
    public void runStep() {

        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


        initializeStep();

//        ifPassedAngleReverseTurnDirection();


        if (turnEnum.equals(Robot.TurnEnum.LEFT)) {

            if (isCloseToTargetAngle()) {

                turnRight(LOW_POWER);
            } else {
                turnRight(HIGH_POWER);
            }

        } else {

            if (isCloseToTargetAngle()) {

                turnLeft(LOW_POWER);
            } else {
                turnLeft(HIGH_POWER);
            }
        }

        logIt("runStep");
    }

    private void ifPassedAngleReverseTurnDirection() {

        if(remainingAngle() < -10){
            Log.i(getLogKey()+"T", "Changing Turn Direction was direction:" +turnEnum  + "  count:"+ robot.loopCounter);
            toggleTurnDirection();

        }

    }

    private void toggleTurnDirection() {
        if(turnEnum.equals(Robot.TurnEnum.RIGHT)){
            turnEnum = Robot.TurnEnum.LEFT;
        }else{
            turnEnum = Robot.TurnEnum.RIGHT;
        }
    }


    private boolean isCloseToTargetAngle() {

        if (Robot.TurnEnum.RIGHT.equals(turnEnum)) {
            return (Math.abs(robot.gyroSensor.getIntegratedZValue() + targetAngle)) < NEAR_TARGET_ANGLE_VALUE;
        }
        return (Math.abs(robot.gyroSensor.getIntegratedZValue() - targetAngle)) < NEAR_TARGET_ANGLE_VALUE;
    }

    private boolean isAtTargetAngle() {

        if (Robot.TurnEnum.RIGHT.equals(turnEnum)) {
            return (Math.abs(robot.gyroSensor.getIntegratedZValue() + targetAngle)) <= TOLERANCE;
        }
        return (Math.abs(robot.gyroSensor.getIntegratedZValue() - targetAngle)) <= TOLERANCE;
    }

    private void turnLeft(double power) {

        robot.motorRight.setPower(-power);
        robot.motorLeft.setPower(power);
    }

    private void turnRight(double power) {

        robot.motorRight.setPower(power);
        robot.motorLeft.setPower(-power);
    }


    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
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

    private void initializeStep() {


        if (initialized == false) {


            initialized = true;
        }

    }


    @Override
    public boolean isStepDone() {


        if (isStillWaiting() || encodersReset == false || initialized == false) {
            return false;

        }


        if (isAtTargetAngle()) {

            robot.motorRight.setPower(0);
            robot.motorLeft.setPower(0);
            logIt("isStepDone");
            return true;
        }


        return false;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append("methodName:" + methodName);
        sb.append(" , LoopCounter:" + robot.loopCounter);
        sb.append(" , turnEnum :" + turnEnum);
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
