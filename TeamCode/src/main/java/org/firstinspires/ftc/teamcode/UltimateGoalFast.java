package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import java.util.List;

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
                noLabel  = updatedRecognitions.size();
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    labelName = recognition.getLabel();
                }

        mapObjects();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //pause(2);
                        if (noLabel == 0){
                            wobbleLock();
                            startLaunch(0.42);
                            //forwardEncoder(80, 0.5);
                            forwardEncoder(170, 0.75);
                            wobbleRelease();
                            reverseEncoder(10, 0.75);
                            pause(0.25);
                            //launchLoop(1,1);
                            //zeroBotEncoder(1);
                            strafeLeftEncoder(85, 0.75); //Was 95
                            zeroBotEncoder(1);
                            //zeroBotEncoder(1);
                            
                            launchAdvance(); //Was 0.4
                            
                            //launchLoop(1.5, 0.705);
                            strafeLeftEncoder(20, 0.75); //Was 25
                            zeroBotEncoder(1);
                            //pause(1);
                            
                            launchAdvance(); //Was 0.45
                            
                            strafeLeftEncoder(20, 0.75); //Was 25
                            zeroBotEncoder(1);
                            //pause(1);
                            
                            launchAdvance(); //Was 0.4
                            
                            forwardEncoder(20, 0.75);
                            endLaunch();
                            pause(100);
                            
                        }
                        else if (labelName.equals("Single")){
                            wobbleLock();
                            startLaunch(0.42);
                            forwardEncoder(160, 0.75);
                            pause(0.25);
                            strafeLeftEncoder(85, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            strafeLeftEncoder(20, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            strafeLeftEncoder(20, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            endLaunch();
                            forwardEncoder(70, 0.75);
                            strafeRightEncoder(50, 0.75);
                            wobbleRelease();
                            reverseEncoder(50, 0.75);
                            pause(100);
                            
                            /*startLaunch(0.42);
                            forwardEncoder(253, 0.75);
                            pause(0.25);
                            //pause(1);
                            strafeLeftEncoder(85, 0.75); //Was 95, 90
                            wobbleRelease();
                            //launchLoop(1,1);
                            zeroBotEncoder(1);
                            reverseEncoder(97, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            //zeroBotEncoder(1);
                            //strafeLeftEncoder(25, 0.5);
                            
                            //launchLoopFirst(0.42); //Was 0.45
                            
                            //launchLoop(1.5, 0.705);
                            strafeLeftEncoder(25, 0.75); //Was 25
                            zeroBotEncoder(1);
                            launchAdvance();
                            
                            //launchLoopOther(0.42); //Was 0.45
                            
                            strafeLeftEncoder(20, 0.75); //Was 25
                            zeroBotEncoder(0.5);
                            launchAdvance();
                            
                            //launchLoopOther(0.42); //Was 0.45
                            
                            forwardEncoder(30, 0.75);
                            endLaunch();*/
                            pause(100);
                            
                        }
                        else if (labelName.equals("Quad")){
                            wobbleLock();
                            startLaunch(0.42);
                            forwardEncoder(160, 0.75);
                            pause(0.25);
                            strafeLeftEncoder(85, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            strafeLeftEncoder(20, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            strafeLeftEncoder(20, 0.75);
                            zeroBotEncoder(1);
                            launchAdvance();
                            endLaunch();
                            forwardEncoder(130, 0.75);
                            strafeRightEncoder(110, 0.75);
                            wobbleRelease();
                            reverseEncoder(100, 0.75);
                            /*startLaunch(0.42);
                            forwardEncoder(300, 0.75);
                            zeroBotEncoder(1);
                            wobbleRelease();
                            reverseEncoder(140, 0.75);
                            pause(0.25);
                            //launchLoop(1,1);
                            zeroBotEncoder(1);
                            strafeLeftEncoder(90, 0.75); //Was 80
                            zeroBotEncoder(1);
                            //zeroBotEncoder(1);
                            pause(0.25);//Was 87
                            
                            launchAdvance(); //Was 0.45
                            
                            //launchLoop(1.5, 0.705);
                            strafeLeftEncoder(20, 0.75); //Was 32
                            zeroBotEncoder(1);
                            
                            launchAdvance(); //Was 0.45
                            
                            strafeLeftEncoder(20, 0.75); //Was 30
                            zeroBotEncoder(1);
                            
                            launchAdvance(); //Was 0.45
                            
                            forwardEncoder(30, 0.75);
                            endLaunch();*/
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
        }

        if (tfod != null) {
            tfod.shutdown();
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
