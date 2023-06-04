package com.example.hideandseek_login_free;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;


public class matchActivity extends AppCompatActivity {
    private static final String TAG = "MyTag";
    public Client client;
    private Timer timer;
    public Gamer thisGamer= new Gamer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        thisGamer.GamerName = intent.getStringExtra("userName");
        thisGamer.Icon = intent.getStringExtra("userIcon");
        Log.d(TAG, thisGamer.GamerName + "  " + thisGamer.Icon);
        client = new Client(thisGamer);
        Thread thread = new Thread(client);
        thread.start();

        timer = new Timer();
        // 启动定时器，延迟 0 毫秒开始执行任务，每隔 1000 毫秒重复执行一次
        timer.schedule(timerTask, 0, 1000);
    }

    private TimerTask timerTask = new TimerTask() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            // 在这里执行您想要定时执行的任务
            TextView GamerNumText = (TextView) findViewById(R.id.GamerNum);
            GamerNumText.setText(client.room.GamerNum+"人");
        }
    };

}