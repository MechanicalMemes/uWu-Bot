package com.disnodeteam.dogecv.scoring;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class MaxAreaScorer extends DogeCVScorer{
    public double weight       = 1.0;

    public MaxAreaScorer( double weight){
        this.weight = weight;

    }

    @Override
    public double calculateScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        MatOfPoint contour = (MatOfPoint) input;
        double area = Imgproc.contourArea(contour);

        return -area * weight;
    }

}
