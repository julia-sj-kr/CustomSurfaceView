package com.example.chapter09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Random;

public class Snow_sub_thread extends SurfaceView {

    //SurfaceView에는 SurfaceView용 스레드가 반드시 있어야 한다.
    Thread threadSV;
    private boolean threadRunning=true;
    private Snow[] snows;

//--------------------------------------------------------------------------------------------------------------------------
    //onSizeChanged() 메서드는 뷰의 크기 또는 뷰의 위치가 변경되었을 때,
    //또는 뷰가 처음 생성되면서 크기가 설정될 때 호출됩니다.
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for(int i=0;i<snows.length;i++){
            //snows[i]=new Snow(getWidth(),getHeight());
            snows[i]=new Snow(w,h);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------

    public Snow_sub_thread(Context context, AttributeSet attrs){
        super(context,attrs);
        //setBackgroundResource(R.drawable.church);
        snows=new Snow[100];
        Log.d("Snow_sub_thread",""+getWidth()+","+getHeight());
        Thread.dumpStack();

        threadSV =new Thread(new Runnable() {
            @Override
            public void run() {//t_main
                SurfaceHolder holder = getHolder();
                Canvas canvas = null;

//--------------------------------상단에 onSizeChanged 메서드에서 오버라이딩------------------------------------------------------
//                for(int i = 0; i < snows.length; i++) {
//                    snows[i] = new Snow(getWidth(), getHeight());
//                }
//--------------------------------------------------------------------------------------------------------------------------

                while (threadRunning) {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        Bitmap church = BitmapFactory.decodeResource(getResources(), R.drawable.church);
                        church = Bitmap.createScaledBitmap(church, getWidth(), getHeight(), false);
                        canvas.drawBitmap(church, 0, 0, null); // 배경 그리기

                        for (Snow snow : snows) {
                            snow.draw(canvas);
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }

                    for (Snow snow : snows) {
                        snow.update();
                    }
                }
            }
        });

        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                Log.d("surfaceCreated","surfaceCreated");

                //threadRunning = true;
                //Snow_sub_thread.this.threadSV.start();
                threadSV.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                Log.d("surfaceDestroyed","surfaceDestroyed");
                threadRunning=false;
                try {
                    threadSV.join();
                } catch (InterruptedException e) {
                }
            }
        });
    }
}

class Snow{
    int x,y;
    int radious;
    int speed;
    Paint paint= new Paint();;
    int widthSV,heightSV;
    Random random=new Random();

    public Snow(int widthSV, int heightSV){
        radious=random.nextInt(30)+1;

        x=random.nextInt(widthSV-radious)+1;
        y=random.nextInt(heightSV-radious)+1;
        speed=random.nextInt(30)+5;

        this.widthSV=widthSV;
        this.heightSV=heightSV;

        paint.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x,y,radious,paint);
    }

    public void update(){
        int xmove=random.nextInt(10)-5;
        int ymove=random.nextInt(10)+1;
        x+=xmove;
        if(x<=0) x+=radious;
        else if(x>=widthSV) x-=radious;
        y+=ymove;
        if(y>=heightSV+radious) y=-radious;
    }
}