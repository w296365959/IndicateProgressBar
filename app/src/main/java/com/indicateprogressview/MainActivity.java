package com.indicateprogressview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IndicateProgressBar ProgressView = (IndicateProgressBar) findViewById(R.id.ProgressView);
        ProgressView.setProgress(0);//设置进度
        
    }
}
