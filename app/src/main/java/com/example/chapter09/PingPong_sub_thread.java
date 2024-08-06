package com.example.chapter09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import kotlin.jvm.Synchronized;

//움직이는 공을 나타내는 클래스
class Ball{
    int x,y,xInc=1, yInc=1;
    int diameter;
    static int WIDTH=1080, HEIGHT=1920;

    public Ball(int d){
        this.diameter=d;

        x=(int)(Math.random()*(WIDTH-d)+3);
        y=(int)(Math.random()*(HEIGHT-d)+3);

        xInc=(int)(Math.random()*30+1);
        yInc=(int)(Math.random()*30+1);
    }


    //여기서 공을 그린다.
    public void paint(Canvas g){
        Paint paint=new Paint();

        //벽에 부딪히면 반사하게 한다.
        if (x<0||x>(WIDTH-diameter)) xInc=-xInc;
        if (y<0||y>(WIDTH-diameter)) yInc=-yInc;

        //볼의 좌표를 갱신한다.
        x+=xInc;
        y+=yInc;

        //볼을 화면에 크린다.
        paint.setColor(Color.RED);
        g.drawCircle(x,y,diameter,paint);
    }
}

//서피스 뷰 정의
public class PingPong_sub_thread extends SurfaceView implements SurfaceHolder.Callback{

    public Ball basket[]=new Ball[100];
    private MyThread thread; //내부 클래스로 정의한 사용자 정의 스레드의 참조 변수

    public PingPong_sub_thread(Context context) { //생성자
        super(context);

        SurfaceHolder holder=getHolder();
        holder.addCallback(this);

        thread=new MyThread(holder);

        for(int i=0;i<basket.length;i++)
            basket[i]=new Ball(20);
    }

    public MyThread getThread() {
        return thread;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry=true;

        //스레드를 중지시킨다.
        thread.setRunning(false);
        while (retry){
            try{
                thread.join();
                retry=false;
            } catch (InterruptedException e){
            }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder,int format, int width, int height) {

    }

    //스레드를 내부 클래스로 정의한다.
    public class MyThread extends Thread{
        private boolean mRun=false;
        private SurfaceHolder mSurfaceHolder;

        //생성자에서 SurfaceHolder를 매개변수로 받아 mSurfaceHolder 변수에 저장합니다.
        public MyThread(SurfaceHolder surfaceHolder){
            mSurfaceHolder=surfaceHolder;
        }

        public void run(){
            while (mRun){
                Canvas c=null;
                try{
                    c=mSurfaceHolder.lockCanvas(null);//캔버스를 얻는다.
                    c.drawColor(Color.BLACK);//캔버스의 배경을 지운다.
                    synchronized(mSurfaceHolder){
                        for(Ball b:basket){//basket의 모든 원소를 그린다.
                            b.paint(c);
                        }
                    }
                }finally {
                    if (c!=null){
                        //캔버스의 로킹을 푼다.
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                //try{Thread.sleep(100);} catch (InterruptedException e){}
            }
        }

        public void setRunning(boolean b){
            mRun=b;
        }
    }
}

