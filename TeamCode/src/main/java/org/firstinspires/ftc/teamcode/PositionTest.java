package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.Map;

// TODO: Add wobble grab support

@Autonomous(name="PositionTest", group="linearOpMode")
public class PositionTest extends AutonomousPrimeTest{
    @Override
    public void runOpMode(){
        mapObjects();
        waitForStart();

        latch.setPosition(0);
        pause(2);
        latch.setPosition(0.1);
        pause(2);
        latch.setPosition(0.2);
        pause(2);
        latch.setPosition(0.3);
        pause(2);
        latch.setPosition(0.4);
        pause(2);
        latch.setPosition(0.5);
        pause(2);
        latch.setPosition(0.6);
        pause(2);
        latch.setPosition(0.7);
        pause(2);
        latch.setPosition(0.8);
        pause(2);
        latch.setPosition(0.9);
        pause(2);
        latch.setPosition(1);
        pause(2);

    }
}