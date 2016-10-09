package com.sabotage.autonomous.steps;

import com.sabotage.autonomous.Robot;


public class Step_TrackWhiteLineNoCorrection extends Step_TrackWhiteLine {

    //constructor
    public Step_TrackWhiteLineNoCorrection(Robot.ColorEnum allianceColorEnum) {

        super(allianceColorEnum);
    }
    @Override
    public String getLogKey() {
        return "Step_TrackWhiteLineNO";
    }

    @Override
    public void runStep() {

        robot.telemetry.addData("ColorR/B:", robot.colorSensorBeaconRight.red() + "/" + robot.colorSensorBeaconRight.blue());

        super.runStep();
    }

    protected boolean hasMissedWhiteLine() {
        return false;


    }

}

