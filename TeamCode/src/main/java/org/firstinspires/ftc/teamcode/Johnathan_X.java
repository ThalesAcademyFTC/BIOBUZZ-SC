package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import android.graphics.Color;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import com.qualcomm.hardware.rev.RevColorSensorV3;

public class Johnathan_X {
    private HardwareMap hwMap;
    LinearOpMode auton;
    OpMode teleop;

    public enum Drivetrain {
        JOHNX,
        TEST,
        BONES
    }

    private Drivetrain drive;
    private Telemetry telem;
    public RevColorSensorV3 theColorSensor;
    public DcMotor motorFL, motorFR, motorBL, motorBR, motorLaunch, motorViper;
    public DcMotor[] allDriveMotors;
    public Servo servoClaw;
    /*public IMU imu;
    private IMU.Parameters parameters;*/
    public AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    private static final boolean USE_WEBCAM = true;
    static final double X_INCH_TICKS = 45;
    static final double Y_INCH_TICKS = 45;
    static final double X_DEGREE_TICKS = 11.1;

    //I don't use comments
    public Johnathan_X(OpMode opmode, Drivetrain drivetrain){
        this.teleop = opmode;
        this.hwMap = opmode.hardwareMap;
        this.drive = drivetrain;
        this.telem = opmode.telemetry;
        setupHardware();
    }
    public Johnathan_X(LinearOpMode opmode, Drivetrain drivetrain){
        this.auton = opmode;
        this.hwMap = opmode.hardwareMap;
        this.drive = drivetrain;
        this.telem = opmode.telemetry;
        setupHardware();
    }

    public Johnathan_X(HardwareMap hardwareMap, Drivetrain drivetrain){
        this.hwMap = hardwareMap;
        this.drive = drivetrain;
        setupHardware();
    }

    public void setupHardware(){
        switch (drive){
            case JOHNX:
                motorFL = hwMap.dcMotor.get("motorFL");
                motorFR = hwMap.dcMotor.get("motorFR");
                motorBL = hwMap.dcMotor.get("motorBL");
                motorBR = hwMap.dcMotor.get("motorBR");

                theColorSensor = hwMap.get(RevColorSensorV3.class, "theColorSensor");
                motorLaunch = hwMap.dcMotor.get("motorLaunch");
                motorViper = hwMap.dcMotor.get("motorViper");
                servoClaw = hwMap.servo.get("servoClaw");

                allDriveMotors = new DcMotor[]{motorBR,motorBL,motorFL,motorFR};
                motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
                motorFL.setDirection(DcMotorSimple.Direction.REVERSE);

                /*parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
                imu.initialize(parameters);*/
                break;
            //TEST
            case TEST:
                motorFL = hwMap.dcMotor.get("motorFL");
                motorFR = hwMap.dcMotor.get("motorFR");
                motorBL = hwMap.dcMotor.get("motorBL");
                motorBR = hwMap.dcMotor.get("motorBR");
                motorLaunch = hwMap.dcMotor.get("motorLaunch");
                motorViper = hwMap.dcMotor.get("motorViper");
                servoClaw = hwMap.servo.get("servoClaw");

                allDriveMotors = new DcMotor[]{motorBR,motorBL,motorFL,motorFR};
                motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
                motorFL.setDirection(DcMotorSimple.Direction.REVERSE);

                /*parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
                imu.initialize(parameters);*/
                break;

            case BONES:
                motorFL = hwMap.dcMotor.get("motorFL");
                motorFR = hwMap.dcMotor.get("motorFR");
                motorBL = hwMap.dcMotor.get("motorBL");
                motorBR = hwMap.dcMotor.get("motorBR");

                allDriveMotors = new DcMotor[]{motorFL,motorFR,motorBL,motorBR};
                motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
                motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
        }

    }

    public void rest(){
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }
    public void move(double x, double y, double turn) {
        double FL_Power;
        double FR_Power;
        double BL_Power;
        double BR_Power;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);

        FL_Power = (y + x + turn) / denominator;
        FR_Power = (y - x - turn) / denominator;
        BL_Power = (y - x + turn) / denominator;
        BR_Power = (y + x - turn) / denominator;

        motorFL.setPower(FL_Power);
        motorFR.setPower(FR_Power);
        motorBL.setPower(BL_Power);
        motorBR.setPower(BR_Power);
    }
    public void waitForMotors() {
        boolean finished = false;
        while (auton.opModeIsActive() && !finished && !auton.isStopRequested()) {
            if (!motorFL.isBusy() && !motorFR.isBusy() && !motorBL.isBusy() && !motorBR.isBusy()) {
                finished = true;
            }
        }
    }
    private void resetDriveEncoders() {
        for (DcMotor x : allDriveMotors) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void moveForwardInches(double inches, double speed) {
        //Converts to integer by rounding. CASTS to int after rounding.
        int tickTarget = (int) Math.round(inches * Y_INCH_TICKS);
        resetDriveEncoders();
        for (DcMotor x : allDriveMotors) {
            x.setTargetPosition(tickTarget);
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        move(0, speed, 0);
        waitForMotors();
        resetDriveEncoders();
    }

    public void moveBackwardInches(double inches, double speed){
        moveForwardInches(-inches, -speed);
    }

    public void moveRightInches(double inches, double speed){
        int tickTarget = (int) Math.round(inches * X_INCH_TICKS);
        resetDriveEncoders();
        motorFL.setTargetPosition(tickTarget);
        motorFR.setTargetPosition(-tickTarget);
        motorBL.setTargetPosition(-tickTarget);
        motorBR.setTargetPosition(tickTarget);
        for (DcMotor x : allDriveMotors){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        move(speed, 0, 0);
        waitForMotors();
        resetDriveEncoders();
    }

    public void moveLeftInches(double inches, double speed){
        moveRightInches(-inches, -speed);
    }

    public void turnRightDegrees(double degrees, double speed){
        int tickTarget = (int) Math.round(degrees * X_DEGREE_TICKS);
        resetDriveEncoders();
        motorFL.setTargetPosition(tickTarget);
        motorFR.setTargetPosition(-tickTarget);
        motorBL.setTargetPosition(tickTarget);
        motorBR.setTargetPosition(-tickTarget);
        for (DcMotor x : allDriveMotors){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        move(0, 0, speed);
        waitForMotors();
        resetDriveEncoders();
    }

    public void turnLeftDegrees(double degrees, double speed){
        turnRightDegrees(-degrees, -speed);
    }

    public void runLauncher(double speed){
        motorLaunch.setPower(speed);
    }

    public boolean clawGrip(boolean isOpen){
        if (isOpen){
            servoClaw.setPosition(.31);
        } else {
            servoClaw.setPosition(.485);
        }
        isOpen = !isOpen;
        return isOpen;
    }

    public void slideViper(double speed){
        motorViper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorViper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorViper.setPower(speed);
    }

    public void slide(int posOnMe, double robotShowSpeed){
        motorViper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorViper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorViper.setTargetPosition(posOnMe);
        motorViper.setPower(robotShowSpeed);
    }

    public void getHSV() {
        final float[] hsvValue = new float[3];
        NormalizedRGBA color = theColorSensor.getNormalizedColors();
        Color.colorToHSV(color.toColor(), hsvValue);
        telem.addData("R", color.red);
        telem.addData("G", color.green);
        telem.addData("B", color.blue);
        telem.update();
    }
}