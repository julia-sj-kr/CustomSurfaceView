package com.example.chapter09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MyPhotoShop extends AppCompatActivity {

    float scaleX=1.0f, scaleY=1.0f, rotateAngle;
    MyView displayView;

    public class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            Paint paint=new Paint();

            int centerX=getWidth()/2;
            int centerY=getHeight()/2;

            canvas.scale(scaleX,scaleY,centerX,centerY);
            canvas.rotate(rotateAngle,centerX,centerY);

            Bitmap picture= BitmapFactory.decodeResource(getResources(),R.drawable.lena);
            canvas.drawBitmap(picture,0,0,paint);
            //canvas.drawBitmap(picture,0,0,null);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myphotoshop);
        LinearLayout layout=(LinearLayout) findViewById(R.id.imageDisplay);
        displayView=new MyView(this);
        layout.addView(displayView);
    }

    //displayView.invalidate();는 View의 onDraw() 메서드를 다시 호출하여 화면을 업데이트하는 데 사용됩니다.
    // 이 호출은 화면을 다시 그리기 위해 View의 상태를 무효화하고, 시스템에 onDraw()를 호출하라고 요청합니다.
    public void expand(View view){
        scaleX+=0.3;
        scaleY+=0.3;
        displayView.invalidate();
    }

    public void shrink(View view){
        scaleX-=0.3;
        scaleY-=0.3;
        displayView.invalidate();
    }
}
