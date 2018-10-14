package com.disnodeteam.dogecommander.auto;

import java.util.Dictionary;

/**
 * Created by Victo on 8/12/2018.
 */

public class DogeSettings {
    public enum AllianceColor {RED,BLUE}
    public enum StartingState {HANGING, GROUNDED}

    public AllianceColor alliance = AllianceColor.BLUE;
    public StartingState startingState = StartingState.GROUNDED;
    public double speed = 1.0;

    private Dictionary<String, Object> parameters;


}
