package com.example.chapter09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

class ImageDispView extends View {
    public ImageDispView(Context context) {
        super(context);
        setBackgroundColor(Color.YELLOW);
    }

    protected void onDraw(Canvas canvas){
        Paint paint =new Paint();
        Bitmap b= BitmapFactory.decodeResource(getResources(),R.drawable.movie1);
        canvas.drawBitmap(b,0,0,null);
    }
}

public class ImageDisp extends AppCompatActivity{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ImageDispView w=new ImageDispView(this);
        setContentView(w);
    }
}
