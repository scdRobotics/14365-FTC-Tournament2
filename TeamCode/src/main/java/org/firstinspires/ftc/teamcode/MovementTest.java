package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.Map;

// TODO: Add wobble grab support

@Autonomous(name="MovementTestAndroidStudio", group="linearOpMode")
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
            wobbleLock();
            startLaunch(0.42);
            forwardEncoder(160, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            launchAdvanceFast();
            strafeLeftEncoder(25, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            //launchAdvanceFast();
            strafeLeftEncoder(20, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            pause(0.2);

            intakeStart(1);
            startLaunch(0.465);
            strafeRightEncoder(87, 1);
            zeroBotEncoder(1);
            pause(0.2);
            reverseEncoder(45, 0.4);
            pause(0.5);
            launchAdvanceFast();
            reverseEncoder(9, 0.5);
            pause(0.5);
            launchAdvanceFast();
            reverseEncoder(9, 0.5);
            pause(0.5);
            launchAdvanceFast ();
            reverseEncoder(9, 0.5);
            pause(0.5);
            launchAdvanceFast();
            pause(0.5);
            launchAdvanceFast();
            pause(0.5);
            intakeEnd();

            rightEncoder(0.225, 1);
            pause(0.2);
            forwardEncoder(210, 1);
            wobbleRelease();
            pause(0.25);
            reverseEncoder(95, 1);
        }
        else if(version==1){
            startLaunch(0.42);
            forwardEncoder(160, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(25, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(20, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            pause(0.2);

            startLaunch(0.465);
            strafeRightEncoder(87, 1);
            pause(0.2);
            reverseEncoder(45, 1);
            pause(0.5);
            launchAdvanceFast();
            pause(0.2);

            forwardEncoder(120, 1);
            wobbleRelease();
            pause(0.25);
            reverseEncoder(70, 1);
        }
        else if(version==2){
            startLaunch(0.42);
            forwardEncoder(160, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(25, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            strafeLeftEncoder(20, 1);
            zeroBotEncoder(1);
            pause(0.2);
            launchAdvanceFast();
            pause(0.2);

            forwardEncoder(20, 1);
            pause(0.2);
            strafeRightEncoder(87, 1);
            pause(0.2);
            reverseEncoder(70, 1);
            pause(3); //Grab wobble
            leftEncoder(1.5, 1);
            pause(0.2);
            strafeLeftEncoder(120, 1);
            wobbleRelease();
            pause(1.5);
            strafeRightEncoder(60, 1);
        }
    }
}