package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

@Disabled
//Odometry documentation
//https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/mecanum-drive-odometry.html
//Horrifying but could be very useful for odometry
//https://i.stack.imgur.com/GCfzv.png    and    https://i.stack.imgur.com/7EDi8.png
@Autonomous(name="AutonomousPrimeTest", group="Linear Opmode")
public class AutonomousPrimeTest extends LinearOpMode { 
  
  
    protected DcMotorEx frontLeft=null;
    protected DcMotorEx frontRight=null;
    protected DcMotorEx backLeft=null;
    protected DcMotorEx backRight=null;
    
    protected DcMotorEx launchLeft=null;
    protected DcMotorEx launchRight=null;
    
    protected DcMotorEx intake=null;
    
    protected DcMotorEx grabber=null;
    
    protected double MotorPower=1.0;
    
    protected final double countPerRotation=753.2;
    
    protected static  double NEW_P = 6.0;// was 8.0
    protected static  double NEW_I = 0.05;
    protected static  double NEW_D = 0.0;
    protected static  double NEW_F = 12.0;
    protected static int tolerance = 10;
    
    protected BNO055IMU imu;
    protected BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    protected Orientation lastAngles = new Orientation();
    protected double globalAngle;
    protected double initialAngle;
    
    protected Servo intakeAdvance;
    
    protected Servo wobbleRelease;
    
    
    public void mapObjects(){
        telemetry.addData("Status","Initialized");
        telemetry.update();
        
        frontLeft=hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight=hardwareMap.get(DcMotorEx.class,"frontRight");
        backLeft=hardwareMap.get(DcMotorEx.class,"backLeft");
        backRight=hardwareMap.get(DcMotorEx.class,"backRight");
        
        launchLeft=hardwareMap.get(DcMotorEx.class,"launchLeft");
        launchRight=hardwareMap.get(DcMotorEx.class,"launchRight");
        
        intake=hardwareMap.get(DcMotorEx.class,"intake");
        
        grabber=hardwareMap.get(DcMotorEx.class,"grabber");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        //frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);
        
        //intake.setDirection(DcMotor.Direction.REVERSE);
        
        /*changePidSettings(NEW_P,NEW_I,NEW_D,NEW_F);
        tolerance = 50;
        frontLeft.setTargetPositionTolerance(tolerance);
        frontRight.setTargetPositionTolerance(tolerance);
        backLeft.setTargetPositionTolerance(tolerance);
        backRight.setTargetPositionTolerance(tolerance);*/
        
        intakeAdvance=hardwareMap.get(Servo.class,"intakeAdvance");
        
        wobbleRelease=hardwareMap.get(Servo.class,"wobbleRelease");
        
        //**** The IMU and associated variables ************
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters(); 
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        //wait for gyro to calibrate
        //commented out while we are NOT using to improve startup time 
        while(!isStopRequested() && !imu.isGyroCalibrated()){
            sleep(50);
            idle();
        }
        
        initialAngle = getAngle();
        
    }
    
    public double getAngle()
    {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        globalAngle += deltaAngle;
        lastAngles = angles;

        return globalAngle;
    }
    
    public void zeroBotEncoder(double MotorPower){
        double newAngle = getAngle();
        telemetry.addData("zeroBot Initial ",initialAngle);

        telemetry.addData("New ",newAngle);
        telemetry.addData("Diff ",Math.abs(newAngle - initialAngle));
        telemetry.update();
        while (Math.abs(newAngle - initialAngle) > 1 && opModeIsActive()){

            telemetry.addData("Zerobot Adj Initial ",initialAngle);
            telemetry.addData("New ",newAngle);
            telemetry.addData("Diff ",Math.abs(newAngle - initialAngle));
            telemetry.update();
            newAngle = getAngle(); 
            ///*** Better to come up with a formula for how long to turn based on difference in the angle
            //** ie how many degress would rightTurn(0.1) get me and calculate value based on Math.abs(newAngle - initialAngle)
            if (newAngle > initialAngle){
                rightEncoder(Math.abs(newAngle - initialAngle)*.026,MotorPower);
            }else {
                leftEncoder(Math.abs(newAngle - initialAngle)*.026,MotorPower);
            }
        }
        
        
    }
    
    public void runOpMode() {
        mapObjects();
        //ACTUAL AUTONOMOUS PROGRAM GOES HERE
    }
    
    //END OF RUNOP. ALL ENCODERS HERE
    public void configTest(double secs, double MotorPower){
        /*frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(MotorPower);
        pause(secs);
        frontLeft.setPower(0);
        
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setPower(MotorPower);
        pause(secs);
        backLeft.setPower(0);
        
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setPower(MotorPower);
        pause(secs);
        frontRight.setPower(0);
        
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setPower(MotorPower);
        pause(secs);
        backRight.setPower(0);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setPower(MotorPower);
        pause(secs);
        launchLeft.setPower(0);
        
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setPower(MotorPower);
        pause(secs);
        launchRight.setPower(0);*/
        
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(MotorPower);
        pause(secs);
        intake.setPower(0);
        
        /*grabber.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        grabber.setPower(MotorPower);
        pause(1);
        grabber.setPower(0);*/
        
        /*advancer.setPower(1);
        pause(secs);
        advancer.setPower(0);*/
        
    }

    public void forwardTime(double secs, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        backRight.setPower(MotorPower);
        
        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0); 
    }
    public void leftTime(double secs, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(-1 * MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower(-1 * MotorPower);
        backRight.setPower(MotorPower);
        
        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);  
    }
    public void rightTime(double secs, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(MotorPower);
        frontRight.setPower(-1 * MotorPower);
        backLeft.setPower(MotorPower);
        backRight.setPower(-1 * MotorPower);
        
        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);  
    }
    public void reverseTime(double secs, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(-1 * MotorPower);
        frontRight.setPower(-1 * MotorPower);
        backLeft.setPower(-1 * MotorPower);
        backRight.setPower(-1 * MotorPower);

        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);  
    }
    public void strafeLeftTime(double secs, double MotorPower){
        frontLeft.setPower(-1 * MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower (MotorPower);
        backRight.setPower(-1 * MotorPower);
        
        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);  
    }
    public void strafeRightTime(double secs, double MotorPower){
        frontLeft.setPower(MotorPower);
        frontRight.setPower(-1 * MotorPower);
        backLeft.setPower (-1 * MotorPower);
        backRight.setPower(MotorPower);
        
        pause(secs);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);  
    }
    public void launch(double secs, double MotorPower){
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setPower(MotorPower);
        launchLeft.setPower(MotorPower);
        
        pause(secs);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void startLaunch(double MotorPower){
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
    }
    public void endLaunch(){
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void launchAdvance(){
        pause(0.55);
        intakeAdvance.setPosition(0.2);
        pause(0.55);
        intakeAdvance.setPosition(0.35);
        pause(0.55);
    }
    public void launchAdvanceFast(){ //pause were 0.25
        intakeAdvance.setPosition(0.2);
        pause(0.55);
        intakeAdvance.setPosition(0.35);
        //pause(0.55);
    }
    public void intake(double secs, double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(MotorPower);
        
        pause(secs);
        intake.setPower(0);
    }
    /*public void conveyor(double secs, double MotorPower) {
        conveyor.setPower(MotorPower);
        
        pause(secs);
        conveyor.setPower(0);
    }*/
    public void launchLoop(double secs, double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(0.75);
        intakeAdvance.setPosition(0.35);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
        
        pause(secs);
        intake.setPower(0);
        intakeAdvance.setPosition(0.2);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void launchLoopFirst(double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
        pause(3.25);
        //intake.setPower(0.75);
        intakeAdvance.setPosition(0.2);
        pause(1);
        //intake.setPower(0);
        intakeAdvance.setPosition(0.35);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void launchLoopOther(double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
        pause(1.75);
        //intake.setPower(0.75);
        intakeAdvance.setPosition(0.2);
        pause(1.5);
        //intake.setPower(0);
        intakeAdvance.setPosition(0.35);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void launchLoopInsta(double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
        pause(0.25);
        intake.setPower(0.75);
        intakeAdvance.setPosition(0.2);
        pause(1.5);
        intake.setPower(0);
        intakeAdvance.setPosition(0.35);
        launchLeft.setPower(0);
        launchRight.setPower(0);
    }
    public void launchLoopNew(double MotorPower){
        //intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        
        launchLeft.setPower(-MotorPower);
        launchRight.setPower(MotorPower);
        pause(3.5);
        //intake.setPower(0.75);
        intakeAdvance.setPosition(0.2);
        pause(0.5);
        intakeAdvance.setPosition(0.35);
        pause(0.5);
        intakeAdvance.setPosition(0.2);
        pause(0.5);
        intakeAdvance.setPosition(0.35);
        pause(0.5);
        intakeAdvance.setPosition(0.2);
        pause(0.5);
        intakeAdvance.setPosition(0.35);
        pause(0.5);
        launchLeft.setPower(0);
        launchRight.setPower(0);
        //intake.setPower(0);
        //launchLeft.setPower(0);
        //launchRight.setPower(0);
    }
    public void intakeLoop(double secs){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(0.75);
        intakeAdvance.setPosition(0.2);
        
        pause(secs);
        intake.setPower(0);
        intakeAdvance.setPosition(0.35);
    }
    public void wobbleRelease() {
        wobbleRelease.setPosition(0.6);
        pause(1.5);
    }
    public void wobbleLock(){
        wobbleRelease.setPosition(0.15);
    }
    public void wobbleGrab(double MotorPower){
        telemetry.addData("Encoder value", grabber.getCurrentPosition());
        grabber.setMode(DcMotor.RunMode.RESET_ENCODERS);
        telemetry.addData("Encoder value", grabber.getCurrentPosition());
        grabber.setTargetPosition((int)(50));
        telemetry.addData("Encoder value", grabber.getCurrentPosition());
        grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Encoder value", grabber.getCurrentPosition());
        grabber.setPower(MotorPower);
        telemetry.addData("Encoder value", grabber.getCurrentPosition());
        while (opModeIsActive() && (grabber.isBusy())){
            telemetry.addData("GB ",grabber.isBusy());
            telemetry.update();
        }
    }
    
    
    
    public void forwardEncoder(double pos, double MotorPower){ //1 pos = 25 cm
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        
        double cmOffset = pos/25;
        
        frontLeft.setTargetPosition((int)(cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(cmOffset*countPerRotation));
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontLeft.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backRight.setPower(MotorPower);
        while (opModeIsActive() && (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())){
            telemetry.addData("FL ",frontLeft.isBusy());
            telemetry.addData("FR ",frontRight.isBusy());
            telemetry.addData("BL ", backLeft.isBusy());
            telemetry.addData("BR ",backRight.isBusy());
            telemetry.update();
        }
      
    }
    public void reverseMoveIntake(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(MotorPower);
        
        double cmOffset = pos/25;

        frontLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (opModeIsActive() && (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())){
            telemetry.addData("FL ",frontLeft.isBusy());
            telemetry.addData("FR ",frontRight.isBusy());
            telemetry.addData("BL ", backLeft.isBusy());
            telemetry.addData("BR ",backRight.isBusy());
            telemetry.update();
        }
        intake.setPower(0);
        
    }
    
    public void intakeStart(double MotorPower){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(MotorPower);
        
        
    }
    public void intakeEnd(){
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(0);
    }
    
    public void reverseEncoder(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        
        double cmOffset = pos/25;
        
        frontLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontLeft.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backRight.setPower(MotorPower);
        while (opModeIsActive() && (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())){
            telemetry.addData("FL ",frontLeft.isBusy());
            telemetry.addData("FR ",frontRight.isBusy());
            telemetry.addData("BL ", backLeft.isBusy());
            telemetry.addData("BR ",backRight.isBusy());
            telemetry.update();
        }
      
    }
    public void strafeLeftEncoder(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        //*** NOTE - need to make slight adjustment to front wheel because was pulling to left
        
        double cmOffset = pos/25;
        
        frontLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        backRight.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        frontLeft.setPower(MotorPower);
        
       while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()){
            
        }
    }
    public void strafeLeftEncoderCharge(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        
        
        //*** NOTE - need to make slight adjustment to front wheel because was pulling to left
        
        double cmOffset = pos/25;
        
        frontLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launchLeft.setPower(-1);
        //launchRight.setPower(1);
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        backRight.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        frontLeft.setPower(MotorPower);
        
        
        
       while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()){
            telemetry.addData("FL ",frontLeft.isBusy());
            telemetry.addData("FR ",frontRight.isBusy());
            telemetry.addData("BL ", backLeft.isBusy());
            telemetry.addData("BR ",backRight.isBusy());
            telemetry.update();
            launchLeft.setPower(-0.53);
            launchRight.setPower(0.53);
        }
    }
    public void strafeRightEncoder(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        //*** NOTE - need to make slight adjustment to front wheel because was pulling to left
        
        double cmOffset = pos/25;
        
        frontLeft.setTargetPosition((int)(cmOffset*countPerRotation));
        frontRight.setTargetPosition((int)(-cmOffset*countPerRotation));
        backLeft.setTargetPosition((int)(-cmOffset*countPerRotation));
        backRight.setTargetPosition((int)(cmOffset*countPerRotation));
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        backRight.setPower(MotorPower);
        frontRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        frontLeft.setPower(MotorPower);
        
       while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()){
            
        }
    }
    public void leftEncoder(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        
        frontRight.setTargetPosition((int)(pos*countPerRotation));
        frontLeft.setTargetPosition((int)(-pos*countPerRotation));
        backRight.setTargetPosition((int)(pos*countPerRotation));
        backLeft.setTargetPosition((int)(-pos*countPerRotation));
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontRight.setPower(MotorPower);
        frontLeft.setPower(MotorPower);
        backRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        
        while (opModeIsActive() && frontLeft.isBusy()){
            
        }
        
    }
    public void rightEncoder(double pos, double MotorPower){
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        
        frontRight.setTargetPosition((int)(-pos*countPerRotation));
        frontLeft.setTargetPosition((int)(pos*countPerRotation));
        backRight.setTargetPosition((int)(-pos*countPerRotation));
        backLeft.setTargetPosition((int)(pos*countPerRotation));
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontRight.setPower(MotorPower);
        frontLeft.setPower(MotorPower);
        backRight.setPower(MotorPower);
        backLeft.setPower(MotorPower);
        
        while (opModeIsActive() && frontLeft.isBusy()){
            
        }
        
    }
    
    public void changePidSettings(double newP, double newI, double newD, double newF){
        //get the PID coeeficients for the run using encoder
        //*** NOTE:  original settings were 10, 3, and 0
        
        PIDFCoefficients pidNew = new PIDFCoefficients(newP, newI, newD, newF);
        /*frontLeft.setVelocityPIDFCoefficients(newP, newI, newD, newF);
        frontRight.setVelocityPIDFCoefficients(newP, newI, newD, newF);
        frontLeft.setVelocityPIDFCoefficients(newP, newI, newD, newF);
        backRight.setVelocityPIDFCoefficients(newP, newI, newD, newF);*/
        
        frontLeft.setPositionPIDFCoefficients(5.0);
        frontRight.setPositionPIDFCoefficients(5.0);
        frontLeft.setPositionPIDFCoefficients(5.0);
        backRight.setPositionPIDFCoefficients(5.0);

        //telemetry.addData("Runtime", "%.03f", getRuntime());
        //telemetry.addData("P,I,D (RUE)"," %.04f, %.04f, %.04f", pidOrig.p, pidOrig.i, pidOrig.d);
        //telemetry.addData("P,I,D (RTP)"," %.04f, %.04f, %.04f", pidOrig2.p, pidOrig2.i, pidOrig2.d);
        //telemetry.addData("P,I,D (RUE)"," %.04f, %.04f, %.04f", pidOrig3.p, pidOrig3.i, pidOrig3.d);
        //telemetry.addData("P,I,D (RTP)"," %.04f, %.04f, %.04f", pidOrig4.p, pidOrig4.i, pidOrig4.d);
        //telemetry.addData("P,I,D (modified)", "%.04f, %.04f, %.04f", pidMod.p, pidMod.i, pidMod.d);
        //telemetry.update();
    }
    public void singleMotor(double pos, double MotorPower, double secs){
        frontRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontRight.setTargetPosition((int)(pos*countPerRotation));
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setPower(MotorPower);
        pause(secs);
        
        backRight.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backRight.setTargetPosition((int)(pos*countPerRotation));
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setPower(MotorPower);
        pause(secs);
        
        frontLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frontLeft.setTargetPosition((int)(pos*countPerRotation));
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setPower(MotorPower);
        pause(secs);
        
        backLeft.setMode(DcMotor.RunMode.RESET_ENCODERS);
        backLeft.setTargetPosition((int)(pos*countPerRotation));
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setPower(MotorPower);
        pause(secs);
        
        
        
        
        
        
        /*while (opModeIsActive() && frontLeft.isBusy()){
            
        }*/
    }
    //**************************************************************************
    //** used to pause when previous operation needs time to complete
    //**************************************************************************
    public void pause(double secs){
        ElapsedTime mRuntime = new ElapsedTime();
        while(mRuntime.time()< secs  && opModeIsActive() ){
            
        }
    }
    
}
