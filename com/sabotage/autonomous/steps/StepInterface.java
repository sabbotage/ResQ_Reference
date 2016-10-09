package com.sabotage.autonomous.steps;

import com.sabotage.autonomous.Robot;

public interface StepInterface {


    public abstract boolean isStepDone();

    public abstract void runStep();

    public abstract boolean isAborted();

    public abstract void setRobot(Robot robot);

    public abstract String getLogKey();

}
