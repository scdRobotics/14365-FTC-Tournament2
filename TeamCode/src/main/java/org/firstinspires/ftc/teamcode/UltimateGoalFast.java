package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import java.util.List;

// TODO: Add wobble support

@Autonomous(name="USETHIS!!!!", group="linearOpMode")
public class UltimateGoalFast extends AutonomousPrimeTest {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "Aba+gBH/////AAABma/0sYDZakYVhtjb1kH5oBVmYfYsDZXTuEZL9m7EdnFKZN/0v/LvE/Yr0NsXiJo0mJmznKAA5MK6ojvgtV1e1ODodBaMYZpgE1YeoAXYpvvPGEsdGv3xbvgKhvwOvqDToPe3x5w6gsq7a4Ullp76kLxRIoZAqaRpOuf1/tiJJQ7gTBFf8MKgbCDosmMDj7FOZsclk7kos4L46bLkVBcD9E0l7tNR0H0ShiOvxBwq5eDvzvmzsjeGc1aPgx9Br5AbUwN1T+BOvqwvZH2pM2HDbybgcWQJKH1YvXH4O62ENsYhD9ubvktayK8hSuu2CpUd1FVU3YQp91UrCvaKPYMiMFu7zeQCnoc7UOpG1P/kdFKP";
    private static String labelName;
    private static int noLabel;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    @Override
    public void runOpMode(){
        initVuforia();
        initTfod();
        tfod.activate();

        if (tfod != null) {
            pause(1);
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                noLabel = updatedRecognitions.size();
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    labelName = recognition.getLabel();
                }
            }
        }

        mapObjects();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //pause(2);
                        if (noLabel == 0){
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
                            pause(100);
                        }
                        else if (labelName.equals("Single")){
                            startLaunch(0.42);
                            wobbleLock();
                            forwardEncoder(160, 1);
                            zeroBotEncoder(1);
                            pause(0.2);
                            launchAdvanceFast();
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

                            intakeStart(1);
                            startLaunch(0.4455);
                            strafeRightEncoder(87, 1);
                            zeroBotEncoder(1);
                            pause(0.2);
                            reverseEncoder(45, 1);
                            pause(0.5);
                            launchAdvanceFast();
                            pause(0.2);
                            intakeEnd();

                            forwardEncoder(120, 1);
                            zeroBotEncoder(1);
                            pause(0.1);
                            strafeLeftEncoder(50, 1);
                            zeroBotEncoder(1);
                            wobbleRelease();
                            pause(0.25);
                            reverseEncoder(70, 1);
                            zeroBotEncoder(1);
                            pause(0.1);

                            strafeRightEncoder(95, 1);
                            zeroBotEncoder(1);
                            pause(0.1);
                            reverseEncoder(85, 1);
                            pause(2);
                            rightEncoder(1.75, 1);
                            pause(0.2);
                            strafeLeftEncoder(150, 1);
                            pause(100);
                        }
                        else if (labelName.equals("Quad")){
                            wobbleLock();
                            startLaunch(0.42);
                            forwardEncoder(160, 1);
                            zeroBotEncoder(1);
                            //pause(0.2);
                            launchAdvanceFast();
                            launchAdvanceFast();
                            strafeLeftEncoder(25, 1);
                            zeroBotEncoder(1);
                            pause(0.1);
                            launchAdvanceFast();
                            //launchAdvanceFast();
                            strafeLeftEncoder(20, 1);
                            zeroBotEncoder(1);
                            pause(0.1);
                            launchAdvanceFast();
                            //pause(0.2);

                            intakeStart(1);
                            startLaunch(0.4455);
                            //strafeRightEncoder(87, 0.75);
                            strafeRightEncoder(87, 1);

                            zeroBotEncoder(1);
                            pause(0.2);
                            //reverseEncoder(45, 0.35);
                            reverseEncoder(45, 0.4);

                            zeroBotEncoder(1);
                            pause(0.5);
                            launchAdvanceFast();
                            reverseEncoder(9, 0.5);
                            pause(0.5);
                            launchAdvanceFast();
                            reverseEncoder(9, 0.5);

                            startLaunch(0.45);

                            pause(0.5);
                            launchAdvanceFast ();
                            reverseEncoder(9, 0.5);
                            pause(0.5);
                            launchAdvanceFast();
                            pause(0.5);
                            launchAdvanceFast();
                            pause(0.25);
                            intakeEnd();

                            rightEncoder(0.225, 1);
                            pause(0.1);
                            forwardEncoder(210, 1);
                            wobbleRelease();
                            pause(0.1);
                            reverseEncoder(95, 1);
                            pause(100);
                        } 
                        //String label = recognition.getLabel();
                        telemetry.update();
                        /* if(label == "single"){
                            
                        } 
                        else if (label == "quad"){
                            
                        } else{
                            
                        }*/
                        
                    }
                }
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = (float)0.6;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }



        /*
        OPTION ONE
        CV Scan for ring num (log in var)
        Move to line
        Shoot 3x at 80% power
        Go back to wobble and grab
        Drop off goal in correct zone based off of var
        Park
         */
        /*
        OPTION TWO
        CV Scan for ring num (log in var)
        Grab wobble
        Move to line
        Shoot 3x at 80% power
        Drop off goal in correct zone based off of var
        Park
         */
        /*
        OPTION ONE PROS
        -Potentially more consistent
        -Easier points in beginning (if fails later, have more points still)
        OPTION ONE CONS
        -More time to go back for wobble and drop it off (TIME CONCERN)

        OPTION TWO PROS
        -Faster and more fluid
        -Still gets same amount of points as option one but with chance to pick up and shoot "floor rings"
        OPTION TWO CONS
        -Points weighted towards end; less points if it fails at any moment
        -Wobble may cause drag to interfere
         */

    }
