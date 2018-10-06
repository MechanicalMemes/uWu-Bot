package com.disnodeteam.dogecv.detectors.roverrukus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.disnodeteam.dogecv.dnn.GoldClassifier;
import com.disnodeteam.dogecv.ViewDisplay;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.dnn.SilverClassifier;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

/**
 * Created by Victo on 9/10/2018.
 */

public class DNNDetector extends DogeCVDetector {


    private Mat displayMat = new Mat();
    private Mat dnnInout = new Mat();
    private Bitmap bitmap;
    private GoldClassifier dnn;
    public DNNDetector() {
        super();
        this.detectorName = "DNN Detector";

    }

    @Override
    public void init(Context context, ViewDisplay viewDisplay) {
        super.init(context, viewDisplay);
        try {
            dnn = new GoldClassifier((Activity)context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mat process(Mat input) {
        if(input.channels() < 0 || input.cols() <= 0){
            Log.e("DogeCV", "Bad INPUT MAT!");
        }
        input.copyTo(displayMat);
        Imgproc.resize(displayMat, dnnInout, new Size(300,300));
        if(bitmap == null){
            bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.RGB_565);
        }
        if(bitmap != null){
            Utils.matToBitmap(dnnInout, bitmap);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            dnn.classifyFrame(bitmap,stringBuilder);

            float[][] locations = ((float[][][]) dnn.outputMap.get(0))[0];
            float[] scores = ((float[][]) dnn.outputMap.get(2))[0];

            for(int i=0;i<locations.length;i++){
                float[] location = locations[i];
                if(scores[i] > 0.4){
                    Point topLeft =  new Point(location[1] * 300, location[0]  * 300);
                    Point bottomRight = new Point(location[3] * 300, location[2]  * 300);
                    Imgproc.rectangle(dnnInout,topLeft,bottomRight,new Scalar(255,255,255),2);
                    Imgproc.putText(dnnInout, "Gold: " + (scores[i]*100) + "% acc", new Point(topLeft.x, topLeft.y -10), 0, 0.3,new Scalar(255,255,0));
                }
            }


        }
       // Imgproc.putText(displayMat,results.toString(), new Point(20,20),0,2.0,new Scalar(255,255,255));
        return dnnInout;
    }

    @Override
    public void useDefaults() {

    }

}
