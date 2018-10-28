package com.disnodeteam.dogecommander;

/**
 * Created by Victo on 9/16/2018.
 */


import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Victo on 1/23/2018.
 */

public class UniLogger {
    private static Telemetry telemetry;
    public static void setTele(Telemetry tel){
        telemetry = tel;
    }
    public static void Log(String tag, String message){
        Log.d(tag, message);
        if(telemetry != null){
            telemetry.addData(tag, message);
            telemetry.update();
        }
    }
}
