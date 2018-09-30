package org.firstinspires.ftc.teamcode.dogecommander;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.auto.DogeCommander;

/**
 * Created by Victo on 9/16/2018.
 */

public class TestCommand extends DogeCommand {
    public TestCommand() {
        super();
    }

    @Override
    public void start() {
        if(commander.bot.getSubSystem("Test") == null){
            UniLogger.Log("DogeCommander", "Cant find subsystem Test");
        }

        if(commander.bot.getSubSystem("Test Sub") == null){
            UniLogger.Log("DogeCommander", "Cant find subsystem Test Sub");
        }else{
            ( (TestSubsystem) commander.bot.getSubSystem("Test Sub")).setPower(1.0);
        }
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isTaskRunning() {
        return false;
    }
}
