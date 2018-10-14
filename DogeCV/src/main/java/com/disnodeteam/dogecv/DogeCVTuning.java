package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class DogeCVTuning {

    private static ViewGroup createdView = null;

    public static ViewGroup setUpView(Context context){
        final int resID = context.getResources().getIdentifier("RelativeLayout", "id", context.getPackageName());
        final Activity activity = (Activity) context;

        ViewGroup l = (ViewGroup) activity.findViewById(resID); //R.id.RelativeLayout);

        final LinearLayout tuneLayout = new LinearLayout(context);
        tuneLayout.setBackgroundColor(Color.WHITE);
        tuneLayout.setAlpha(0.5f);
        tuneLayout.setOrientation(LinearLayout.VERTICAL);
        tuneLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        l.addView(tuneLayout);
        createdView = tuneLayout;
        return tuneLayout;
    }

    public static void clear(Context context){
        final int resID = context.getResources().getIdentifier("RelativeLayout", "id", context.getPackageName());
        final Activity activity = (Activity) context;

        ViewGroup l = (ViewGroup) activity.findViewById(resID); //R.id.RelativeLayout);

        if(createdView != null){
            l.removeView(createdView);
        }

    }

    public static Button createButton(ViewGroup view, Context context, String name, View.OnClickListener onClickListener){
        Button button = new Button(context);
        button.setText(name);
        button.setOnClickListener(onClickListener);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.addView(button);

        return button;
    }

    public static SeekBar creatSeek(ViewGroup view, Context context, int max, int defualt, SeekBar.OnSeekBarChangeListener listener){
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(max);
        seekBar.setProgress(defualt);
        seekBar.setOnSeekBarChangeListener(listener);
        view.addView(seekBar);
        return seekBar;
    }

    public static TextView creatText(ViewGroup view, Context context, String initText){
        TextView textView = new TextView(context);
        textView.setText(initText);
        view.addView(textView);
        return textView;
    }
}
