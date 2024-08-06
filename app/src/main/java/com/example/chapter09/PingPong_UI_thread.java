package com.example.chapter09;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PingPong_UI_thread extends AppCompatActivity {
    PingPong_sub_thread view;

    //PingPong_sub_thread를 사용자 정의 View로 사용하고 있다는 것을 알 수 있는 코드는
    //PingPong_UI_thread 액티비티에서 PingPong_sub_thread를 인스턴스화하고, setContentView를 통해 UI로 설정하는 부분입니다.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new PingPong_sub_thread(this);
        setContentView(view);
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
}
