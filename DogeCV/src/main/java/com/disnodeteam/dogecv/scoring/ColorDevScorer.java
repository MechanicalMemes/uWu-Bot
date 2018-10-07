package com.disnodeteam.dogecv.scoring;

import com.disnodeteam.dogecv.math.MathFTC;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;

/**
 * Created by LeviG on 10/7/2018.
 */

public class ColorDevScorer {

    private static MatOfDouble std = new MatOfDouble();
    private static MatOfDouble mean = new MatOfDouble();

    public static double calculateDifferences(Mat region) {
        Core.meanStdDev(region, mean, std);
        return MathFTC.mean(std.get(0,0));
    }
}
