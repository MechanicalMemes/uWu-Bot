package com.disnodeteam.dogecv.scoring;

import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class RatioScorer extends DogeCVScorer{
    @Override
    public double calculateDifference(List<MatOfPoint> contours) {

        return 0;
    }
}
