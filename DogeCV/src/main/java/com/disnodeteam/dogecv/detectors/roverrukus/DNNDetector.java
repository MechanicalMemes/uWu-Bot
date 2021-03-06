package com.disnodeteam.dogecv.detectors.roverrukus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.disnodeteam.dogecv.dnn.GoldClassifier;
import com.disnodeteam.dogecv.ViewDisplay;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.dnn.ImageClassifier;
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


    /*
    THIS IS WIP DETECTOR. THIS IS OFFICIALLY UNSUPPORTED AS OF 2018.2 WITH THE FULL RELEASE PLANNED
    FOR 2018.3. USE AT YOUR OWN RISK
    */

    private Mat displayMat = new Mat();
    private Mat dnnInout = new Mat();
    private Bitmap bitmap;
    private ImageClassifier classifier;

    public enum DNNObject {GOLD,SILVER};

    public DNNObject objectToFind = DNNObject.GOLD;

    public DNNDetector() {
        super();
        this.detectorName = "DNN Detector";

    }

    @Override
    public void init(Context context, ViewDisplay viewDisplay) {
        super.init(context, viewDisplay);
        try {
            switch (objectToFind) {
                case GOLD:
                    classifier = new GoldClassifier((Activity)context);

                    break;
                case SILVER:
                    classifier = new SilverClassifier((Activity)context);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mat process(Mat input) {
        input.copyTo(displayMat);
        Imgproc.resize(displayMat, dnnInout, new Size(300,300));
        if(bitmap == null){
            bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.RGB_565);
        }
        if(bitmap != null){
            Utils.matToBitmap(dnnInout, bitmap);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            classifier.classifyFrame(bitmap,stringBuilder);

            float[][] locations = ((float[][][]) classifier.outputMap.get(0))[0];
            float[] scores = ((float[][]) classifier.outputMap.get(2))[0];

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
