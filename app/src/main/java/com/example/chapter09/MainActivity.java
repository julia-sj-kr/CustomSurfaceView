package com.example.chapter09;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

//public class MainActivity extends AppCompatActivity {
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        TouchEvent w=new TouchEvent(this);
//        setContentView(w);
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        //CustomView w=new CustomView(this);
//        CustomView w=new CustomView(this,null);
//        setContentView(w);
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        SingleTouchView w=new SingleTouchView(this,null);
//        setContentView(w);
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        ImageScale w=new ImageScale(this);
//        setContentView(w);
//    }
//}

public class MainActivity extends AppCompatActivity {

    private MyDraw drawView;
    private ImageButton currPaint;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydraw);
        drawView=(MyDraw)findViewById(R.id.drawing);
        LinearLayout paintLayout=(LinearLayout) findViewById(R.id.paint_colors);
        currPaint=(ImageButton) paintLayout.getChildAt(0);

    }

    public void clicked(View view) {
        if(view!=currPaint){
            String color=view.getTag().toString();
            drawView.setColor(color);
            currPaint=(ImageButton) view;
        }
    }
}