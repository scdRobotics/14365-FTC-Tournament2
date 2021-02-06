/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.Map;

// TODO: Due to code loss, add in real values instead of "?"- base code should be same

@Autonomous(name="MovementTest", group="linearOpMode")
public class MovementTest extends AutonomousPrimeTest{
    @Override
    public void runOpMode(){
        mapObjects();
        waitForStart();

        //launch speed start = 0.42
		//launch speed second = 0.465
		//launch function is launchAdvanceFast

        double version = 0;

		//Version 0 is 4 rings
		//Version 1 is 1 ring
		//Version 2 is 0 rings

        if(version==0){
            startLaunch(0.42);
            forwardEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast (?, 1);

            strafeRightEncoder(?, 1);
            pause(0.2);
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast();
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast();
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast ();
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast();
            launchAdvanceFast();
            pause(0.2);

            rightEncoder(?, 1);
            pause(0.2);
            forwardEncoder(?, 1);
            wobbleRelease();
            pause(0.25);
            reverseEncoder(?, 1);
        }
        else if(version==1){
            startLaunch(0.42);
            forwardEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast (?, 1);
            pause(0.2);

            strafeRightEncoder(?, 1);
            pause(0.2);
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast();
            pause(0.2);

            forwardEncoder(?, 1);
            wobbleRelease();
            pause(0.25);
            reverseEncoder(?, 1);
        }
        else if(version==2){
            startLaunch(0.42);
            forwardEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(?, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast (?, 1);
            pause(0.2);

            strafeRightEncoder(?, 1);
            pause(0.2);
            reverseEncoder(?, 1);
            pause(0.5);
            launchAdvanceFast();
            pause(0.2);

            forwardEncoder(?, 1);
            wobbleRelease();
            pause(0.25);
            reverseEncoder(?, 1);
        }
    }
}*/