/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.sabotage.autonomous.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.sabotage.autonomous.Robot;
import com.sabotage.autonomous.steps.StepInterface;
import com.sabotage.autonomous.steps.Step_ControlDebris;
import com.sabotage.autonomous.steps.Step_ReCalibrateGyroIfNeeded;
import com.sabotage.autonomous.steps.Step_FindColorLine;
import com.sabotage.autonomous.steps.Step_TurnLeft;
import com.sabotage.autonomous.steps.Step_TurnRight;
import com.sabotage.autonomous.steps.Step_Straight;
import com.sabotage.autonomous.steps.Step_TrackWhiteLine;
import com.sabotage.autonomous.steps.Step_UnloadClimbers;

import java.util.ArrayList;


public class Red_Outside extends BaseRescueOp {


    protected void init_defineRescueAsStepList() {

        // Here are our steps (in order) that make up our rescue plan.
        stepList = new ArrayList<StepInterface>();

        stepList.add(new Step_ReCalibrateGyroIfNeeded());

        stepList.add(new Step_Straight(1000, DcMotor.Direction.REVERSE));
        stepList.add(new Step_ControlDebris(Robot.ControlDebrisEnum.EJECT));
        stepList.add(new Step_TurnLeft(50));
        stepList.add(new Step_Straight(7000, DcMotor.Direction.REVERSE));
        stepList.add(new Step_TurnLeft(80));
        stepList.add(new Step_FindColorLine(Robot.ColorEnum.RED));
        stepList.add(new Step_TurnLeft(90));
        stepList.add(new Step_Straight(500, DcMotor.Direction.REVERSE));
        stepList.add(new Step_TurnRight(0));
        stepList.add(new Step_FindColorLine(Robot.ColorEnum.WHITE));
        stepList.add(new Step_Straight(400, DcMotor.Direction.REVERSE));
        stepList.add(new Step_TurnLeft(90));
        stepList.add(new Step_Straight(200, DcMotor.Direction.FORWARD));
        stepList.add(new Step_TrackWhiteLine(Robot.ColorEnum.RED));
        stepList.add(new Step_UnloadClimbers());

        // NEW, park robot away from beacon.
        stepList.add(new Step_Straight(400, DcMotor.Direction.FORWARD));
        stepList.add(new Step_TurnLeft(160));
        stepList.add(new Step_Straight(1800, DcMotor.Direction.REVERSE));


    }


}
