package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class N00BS extends LinearOpMode {
    private Johnathan_X johnX;
    // Or yeet_slinger
    // Possibly SCBB
    private ElapsedTime runtime = new ElapsedTime();
    /*
    Boom Crash
    Don't make auton go for too long...
    OR ELSE
    U get foul
     */
    @Override
    public void runOpMode(){
        johnX = new Johnathan_X(this, Johnathan_X.Drivetrain.TEST);
        int rest = 100;
        boolean clawOpen = false;
        clawOpen = johnX.clawGrip(clawOpen);

        waitForStart();
        runtime.reset();
        if(opModeIsActive()) {
            johnX.moveForwardInches(20, .5);
            sleep(rest);
            johnX.moveBackwardInches(20, .5);
            sleep(rest);
            johnX.moveLeftInches(10, .8);
            sleep(rest);
            johnX.moveRightInches(10, .8);
            sleep(rest);
            johnX.slide(147,0.8);
            sleep(rest);
            clawOpen = johnX.clawGrip(clawOpen);
            sleep(rest);
            clawOpen = johnX.clawGrip(clawOpen);
            sleep(rest);
            johnX.slide(0, .8);
            /*sleep(rest);
            johnX.runLauncher(.8);
            sleep(2000);
            johnX.runLauncher(0);*/
        }
    }
}