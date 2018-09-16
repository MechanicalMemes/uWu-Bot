package com.disnodeteam.dogecv.scoring;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class MaxAreaScorer extends DogeCVScorer{
    public double weight       = 1.0;
    public double max          = 500;

    public MaxAreaScorer( double max, double weight){
        this.weight = weight;
        this.max = max;
    }

    @Override
    public double calculateDifference(MatOfPoint contours) {


        double area = Imgproc.contourArea(contours);

        double normalizedArea = (max - area) / max;

        return normalizedArea * weight;
    }

}
