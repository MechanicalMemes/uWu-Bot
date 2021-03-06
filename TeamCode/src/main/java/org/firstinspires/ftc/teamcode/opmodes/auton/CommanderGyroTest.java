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

package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.disnodeteam.dogecommander.auto.DogeCommander;
import com.disnodeteam.dogecommander.examples.commands.TankDriveTurnPID;
import com.disnodeteam.dogecommander.examples.commands.WaitForTime;
import com.disnodeteam.dogecommander.utils.PIDSettings;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.dogecommander.UWUBot;

@Autonomous(name="Commander Gyro Test", group="DogeCommander")
public class CommanderGyroTest extends LinearOpMode {

    private UWUBot bot;
    private DogeCommander commander;
    public void runOpMode() {

        bot = new UWUBot(hardwareMap);
        commander = new DogeCommander();
        commander.usingLinearOpMode(this);
        commander.setBot(bot);

        waitForStart();
        PIDSettings settings = new PIDSettings();

        while(opModeIsActive()){
            settings.set(RobotConstants.TURNING_PID.kP,RobotConstants.TURNING_PID.kI,RobotConstants.TURNING_PID.kD);
            commander.runCommand(new TankDriveTurnPID(bot.tankDrive, bot.navigationHardware,90,1,settings));
            commander.runCommand(new WaitForTime(1.0));
            commander.runCommand(new TankDriveTurnPID(bot.tankDrive, bot.navigationHardware,-90,1,settings));
            commander.runCommand(new WaitForTime(1.0));
            commander.runCommand(new TankDriveTurnPID(bot.tankDrive, bot.navigationHardware,0,1,settings));
            commander.runCommand(new WaitForTime(1.0));
            commander.runCommand(new TankDriveTurnPID(bot.tankDrive, bot.navigationHardware,179,1,settings));


        }

        requestOpModeStop();
    }


}
