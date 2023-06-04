package com.example.hideandseek_login_free;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private EditText userName;
    public Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //匹配按钮
        Button nextBtn = (Button) findViewById(R.id.nextbtn);

        //昵称
        userName =(EditText) findViewById(R.id.userNameEditView);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this,GameActivity.class);
                //startActivity(intent);

                //创建客户端线程
                String userName_str=userName.getText().toString();
                String userIcon = "1";
                Gamer tmpg = new Gamer(userName_str,userIcon);
                //这时需要切换到匹配窗体中去
                Intent intent = new Intent(MainActivity.this,matchActivity.class);
                intent.putExtra("userName",userName_str);
                intent.putExtra("userIcon",userIcon);
                startActivity(intent);

            }
        });
    }

}