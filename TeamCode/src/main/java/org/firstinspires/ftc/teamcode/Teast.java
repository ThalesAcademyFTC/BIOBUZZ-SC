package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teast")
public class Teast extends OpMode {
    private Johnathan_X johnX;
    boolean clawOpen = false;

    public void init(){
        johnX = new Johnathan_X(this, Johnathan_X.Drivetrain.TEST);
        clawOpen = johnX.clawGrip(clawOpen);
    }
    public void loop(){
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x/2;

        x *= x;
        if (gamepad1.left_stick_x < 0){
            x = -x;
        }
        y *= y;
        if (gamepad1.left_stick_y > 0){
            y = -y;
        }
        if (gamepad1.a){
            x /= 3;
            y /= 3;
            turn /= 2;
        }

        johnX.move(x, y, turn);

        if (gamepad2.dpad_up){
            if(gamepad2.a){
                johnX.slideViper(.4);
            } else {
                johnX.slideViper(1);
            }
        } else if (gamepad2.dpad_down) {
            if(gamepad2.a){
                // Speed offset is slighter because it works with gravity rather than against it
                johnX.slideViper(-.1);
            } else {
                johnX.slideViper(-1);
            }
        } else {
            johnX.slideViper(0);
        }

        if (gamepad2.xWasPressed()){
            clawOpen = johnX.clawGrip(clawOpen);
        } else if (gamepad2.xWasReleased()){
            gamepad2.resetEdgeDetection();
        }

        //johnX.runLauncher(gamepad2.right_trigger);
    }
}
