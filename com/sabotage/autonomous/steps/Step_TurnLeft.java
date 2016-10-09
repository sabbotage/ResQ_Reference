package com.sabotage.autonomous.steps;


public class Step_TurnLeft extends Step_TurnRight {


    public Step_TurnLeft(double angleDegrees) {
        super(angleDegrees);
    }


    @Override
    public String getLogKey() {
        return "Step_TurnLeft";
    }

    protected double remainingAngle() {

        return this.targetAngle - robot.gyroSensor.getIntegratedZValue();
    }



    protected double determinePower() {

        return -super.determinePower();

    }

}
