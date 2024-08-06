package com.example.chapter09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
        setBackgroundColor(Color.YELLOW);
    }

    //XML에서 이 뷰를 참조하려면 반드시 이 생성자를 구현하여야 한다.
    public CustomView(Context context, AttributeSet attrs) {
        super(context);
        setBackgroundColor(Color.YELLOW);
    }

    protected void onDraw(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        canvas.drawArc(new RectF(10,10,600,600),45,320,true,paint);
    }
}

