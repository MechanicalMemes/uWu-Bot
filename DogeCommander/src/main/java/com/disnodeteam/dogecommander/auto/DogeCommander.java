package com.disnodeteam.dogecommander.auto;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.hardware.DogeBot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

public class DogeCommander {
    private static String TAG = "DogeCommander";

    public Telemetry telemetry;
    public DogeBot bot;
    public LinearOpMode linearOpMode;
    private boolean halt;
    private String status;
    private List<DogeCommand> currentStack = new ArrayList<>();

    public DogeCommander(){
        UniLogger.Log(TAG, "Created DogeCommander");
    }

    public void setBot(DogeBot bot){
        this.bot = bot;
        bot.init();
        UniLogger.Log(TAG, "Init'd DogeCommander with bot: " + bot.getClass().getSimpleName());
    }

    public void usingLinearOpMode(LinearOpMode opMode){
        linearOpMode = opMode;
        UniLogger.Log(TAG, "Set to use Linear opMode");
    }

    public boolean getOpModeStop(){
        if(linearOpMode == null){
            return false;
        }
        return !linearOpMode.opModeIsActive() || linearOpMode.isStopRequested();
    }

    public void runCommand(DogeCommand command){

        runCommandsSequential(new DogeCommand[]{command});

    }

    public void runCommandsSequential(DogeCommand[] commands){
        this.halt = false;
        UniLogger.Log(TAG, "Running commands: " + commands.length);
        for(DogeCommand command : commands){
            currentStack.add(command);
            UniLogger.Log(TAG, "Running command: " +command.getClass().getSimpleName());
            command.Init(this);
            command.start();
        }

        while(!halt && !getOpModeStop() && !isTaskRunningInStack()){
            for(DogeCommand command : commands){
                command.loop();
            }

        }

        for(DogeCommand command : commands){
            UniLogger.Log(TAG, "Stopped command: " +command.getClass().getSimpleName());
            command.stop();
            currentStack.remove(command);

        }

        UniLogger.Log(TAG, "Run finished");
    }

    public void halt(){
        this.halt = true;
        UniLogger.Log(TAG, "Manually haulting run");
    }
    public void getStatus(){

    }


    private boolean isTaskRunningInStack(){
        boolean isTaskRunning = false;

        for(DogeCommand command : currentStack){
            if(command.isTaskRunning()){
                isTaskRunning = true;
            }
        }
        return isTaskRunning;
    }



}
