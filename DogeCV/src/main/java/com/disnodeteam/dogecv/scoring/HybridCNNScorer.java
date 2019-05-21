package com.disnodeteam.dogecv.scoring;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HybridCNNScorer extends DogeCVScorer {

    private String modelPath = "hybcnn.tflite";
    private Interpreter interpreter;
    private ByteBuffer imgData = null;
    private int[] intValues = new int[64 * 64];

    public HybridCNNScorer(Activity activity){
        try {
            interpreter = new Interpreter(loadModelFile(activity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public double calculateScore(Mat input, Mat fullImage) {
        MatOfPoint contour = (MatOfPoint) input;
        double score = Double.MAX_VALUE;
        long startTime = SystemClock.uptimeMillis();
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(contour);
        Rect expandedRect = new Rect(Math.max(0,rect.x - 5),Math.max(0,rect.y - 5),Math.min(rect.width + 5, fullImage.width()), Math.min(rect.height + 5, fullImage.height()));

        Mat cropped = fullImage.submat(expandedRect);
        Imgproc.resize(cropped,cropped,new Size(64,64));
        Bitmap cropBitMap = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(cropped,cropBitMap);
        cropped.release();

        ByteBuffer byteBuffer = convertBitmapToByteBuffer(cropBitMap);


        float[][] result = new float[1][3];
        interpreter.run(byteBuffer,result);
        long endTime = SystemClock.uptimeMillis();


        // Gold None Silver
        if(result[0][0] > 0.5){
            Log.d("HybridDNN", "Gold Verified");
        }

        return 0;
    }



    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(49152);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[64 * 64];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int pixel : pixels) {
            float rChannel = (pixel >> 16) & 0xFF;
            float gChannel = (pixel >> 8) & 0xFF;
            float bChannel = (pixel) & 0xFF;
            float pixelValue = (rChannel + gChannel + bChannel) / 3 / 255.f;
            byteBuffer.putFloat(pixelValue);
        }
        return byteBuffer;
    }

}
