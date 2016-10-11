package com.example.navneetnandan.atry;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class OldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView=(TextView)findViewById(R.id.text_view);
        Button btn=(Button)findViewById(R.id.btn);
        final RelativeLayout root= (RelativeLayout) findViewById(R.id.activity_main);
        final int[] screenWidth = new int[1];
        final int[] screenHeight = new int[1];
        final int[] layoutHeight=new int[1];
        root.post(new Runnable() {
            public void run() {
                Rect rect = new Rect();
                Window win = getWindow();  // Get the Window
                win.getDecorView().getWindowVisibleDisplayFrame(rect);
                // Get the height of Status Bar
                int statusBarHeight = rect.top;
                // Get the height occupied by the decoration contents
                int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                // Calculate titleBarHeight by deducting statusBarHeight from contentViewTop
                int titleBarHeight = contentViewTop - statusBarHeight;
                Log.i("MY", "titleHeight = " + titleBarHeight + " statusHeight = " + statusBarHeight + " contentViewTop = " + contentViewTop);

                // By now we got the height of titleBar & statusBar
                // Now lets get the screen size
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                screenHeight[0] = metrics.heightPixels;
                screenWidth[0] = metrics.widthPixels;
                Log.i("MY", "Actual Screen Height = " + screenHeight[0] + " Width = " + screenWidth[0]);

                // Now calculate the height that our layout can be set
                // If you know that your application doesn't have statusBar added, then don't add here also. Same applies to application bar also
                layoutHeight[0] = screenHeight[0] - (titleBarHeight + statusBarHeight);
                Log.i("MY", "Layout Height = " + layoutHeight[0]);
            }
        });
        Log.e("w",screenWidth[0]+"");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.measure(0,0);
                final int widthCentral=(screenWidth[0] -textView.getMeasuredWidth())/2;
                final Animation animation = new TranslateAnimation(0,widthCentral,0, 0);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.measure(0,0);
                        Log.e("height",layoutHeight[0]+"");
                        Animation anim=new TranslateAnimation(widthCentral,widthCentral,0,layoutHeight[0]-textView.getMeasuredHeight()*2);
                        anim.setDuration(1000);
                        anim.setFillAfter(true);
                        textView.startAnimation(anim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                Timer t = new Timer();
//Set the schedule function and rate
                t.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {
                                              TextView te=(TextView)findViewById(R.id.text_view);
                                              runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      textView.append("a");
                                                  }
                                              });
                                              //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                          }

                                      },
//Set how long before to start calling the TimerTask (in milliseconds)
                        0,
//Set the amount of time between each execution (in milliseconds)
                        1000);
                textView.startAnimation(animation);
                textView.setVisibility(View.VISIBLE);
            }
        });

    }
}
