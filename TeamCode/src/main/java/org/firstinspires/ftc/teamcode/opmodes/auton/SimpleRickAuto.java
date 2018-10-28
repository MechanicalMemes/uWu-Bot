package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.dogecommander.UWUBot;

/**
 * Created by Victo on 10/27/2018.
 */

public abstract class SimpleRickAuto extends LinearOpMode {
    private DcMotor leftFront, leftRear, rightFront, rightRear;
    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("lf");
        leftRear = hardwareMap.dcMotor.get("lr");
        rightFront = hardwareMap.dcMotor.get("rf");
        rightRear = hardwareMap.dcMotor.get("rr");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        runInit();
        waitForStart();
        runAuto();
    }
    public abstract void runInit();
    public abstract void runAuto();

     void moveEncoder(int ticksLeft, int ticksRight, double speed){
        int lfPose = leftFront.getCurrentPosition() + ticksLeft;
        int lrPose = leftRear.getCurrentPosition() + ticksLeft;
        int rfPos = rightFront.getCurrentPosition() + ticksRight;
        int rrPos = rightRear.getCurrentPosition() + ticksRight;

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setTargetPosition(lfPose);
        leftRear.setTargetPosition(lrPose);
        rightFront.setTargetPosition(rfPos);
        rightRear.setTargetPosition(rrPos);

        leftFront.setPower(speed);
        leftRear.setPower(speed);
        rightFront.setPower(speed);
        rightRear.setPower(speed);
    }

    public boolean motorsBusy(){
        return leftFront.isBusy() && leftRear.isBusy() && rightFront.isBusy() && rightRear.isBusy();
    }

    public void setAllMotors(double speed){
        leftFront.setPower(speed);
        leftRear.setPower(speed);
        rightFront.setPower(speed);
        rightRear.setPower(speed);
    }

    public void turnToAngle(double angle, double speed){

    }
}
