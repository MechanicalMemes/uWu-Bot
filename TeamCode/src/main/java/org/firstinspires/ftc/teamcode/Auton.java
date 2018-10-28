/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Yett", group="Linear Opmode")

public class Auton extends LinearOpMode {

    // Declare OpMode members.

    private GoldAlignDetector detector;

    private DcMotor leftFront, leftRear, rightFront, rightRear;
    private Servo servo;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.downscale = 0.3;
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 50;
        detector.alignPosOffset = 200;

        detector.enable();

        leftFront = hardwareMap.dcMotor.get("lf");
        leftRear = hardwareMap.dcMotor.get("lr");
        rightFront = hardwareMap.dcMotor.get("rf");
        rightRear = hardwareMap.dcMotor.get("rr");
        servo = hardwareMap.servo.get("marker");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);



        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        //
        // AUTO START
        //
        moveEncoder(200, -200, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);
        moveEncoder(1000, 1000, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(-720, 720, 0.3);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(-800, -800, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        int tickRef = leftFront.getCurrentPosition();
        moveEncoder(2500, 2500, 0.2);

        while(detector.getAligned() == false && motorsBusy() && !isStopRequested()){
            telemetry.addData("Aligned", detector.getXPosition());
            telemetry.update();
        }
        int ticksLeft = 2500 -( leftFront.getCurrentPosition() - tickRef);
        setAllMotors(0);

        moveEncoder(500, -500, 0.3 );
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(400 , 40cff0, 0.6);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Scoring");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(-300 , -300, 0.6);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Scoring");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(-500, 500, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);
        moveEncoder(ticksLeft, ticksLeft, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Scoring");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(-530, 530, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(2000, 2000, 0.2);

        while(detector.getAligned() == false && motorsBusy() && !isStopRequested()){
            telemetry.addData("Aligned", detector.getXPosition());
            telemetry.update();
        }

        setAllMotors(0);

        moveEncoder(500, -500, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(1000, 1000, 0.9);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Scoring");
            telemetry.update();
        }
        setAllMotors(0);

        servo.setPosition(-0.8);

        servo.setPosition(0.5);


        moveEncoder(700, -700, 0.4);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(1000, 1000, 0.2);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(150, -150, 0.2);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);

        moveEncoder(4000, 4000, 1.0);
        while( motorsBusy() && !isStopRequested()){
            telemetry.addData("Status","Turning");
            telemetry.update();
        }
        setAllMotors(0);


        detector.disable();

    }

    private void moveEncoder(int ticksLeft, int ticksRight, double speed){
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

    private boolean motorsBusy(){
        return leftFront.isBusy() && leftRear.isBusy() && rightFront.isBusy() && rightRear.isBusy();
    }

    private void setAllMotors(double speed){
        leftFront.setPower(speed);
        leftRear.setPower(speed);
        rightFront.setPower(speed);
        rightRear.setPower(speed);
    }

    private void turnToAngle(double angle, double speed){

    }
}
