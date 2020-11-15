package com.example.duoxianchengpanduansushu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Runnable myWorker = new Runnable() {
            @Override
            public void run() {
                int num=52;
                int flag=0;
                for(int i=2;i<num;i++){
                    if(num%i==0){
                        flag++;
                    }
                }
                if(flag>0){
                    Log.i("acb","不是素数");
                }else{
                    Log.i("acb","是素数");
                }
            }
        };
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread workThread = new Thread(null, myWorker, "WorkThread");
                workThread.start();
            }
        });

    }
}