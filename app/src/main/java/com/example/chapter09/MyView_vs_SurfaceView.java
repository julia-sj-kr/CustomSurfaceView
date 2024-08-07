package com.example.chapter09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


public class MyView_vs_SurfaceView extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_layout);
    }
}

class Ball2{
    int x,y;
    int radius=30;
    int dx=5, dy=5;
    int w,h;

    Paint paint=new Paint();
    Random random = new Random();

    public Ball2(int x, int y, int w, int h){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        dx=random.nextInt(10)-5;
        dy=random.nextInt(10)-5;

        int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        paint.setColor(color);
    }
    public void draw(Canvas canvas){
        canvas.drawCircle(x,y,radius,paint);
    }
    public void update(){
        x+=dx;
        y+=dy;
        if(x+radius>w||x-radius<0){
            dx=-dx;
        }
        if(y+radius>h||y-radius<0){
            dy=-dy;
        }
    }
}

class MyView3 extends View {

    ArrayList<Ball2> points=new ArrayList<>();

    public MyView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.YELLOW);

//-------------주기성을 주는 방법1-----------------------Collections.synchronizedList 이용하여 충돌 해결 필요----------------------------------------------------------------
//        new Thread(() -> {
//            while (true) {
//                try {
//                    for (Ball2 ball : points) {
//                        ball.update();
//                        //안드로이드에서 invalidate() 메서드는 뷰의 화면을 갱신하도록 요청하는 데 사용됩니다.
//                        //뷰의 onDraw() 메서드가 다시 호출되어 화면이 다시 그려지게 됩니다.
//                        //이는 주로 사용자 정의 뷰에서 화면을 갱신할 필요가 있을 때 사용됩니다.
//                    }
//                    invalidate();
//                    Thread.sleep(33);
//                } catch (InterruptedException e) {
//
//                }
//            }
//        }).start();
//------------------------------------------------------------------------------------------------------------------------------------


//-------------주기성을 주는 방법2-----모든 작업을 UI스레드에서 하므로 동기화 과정에서 충돌 문제가 없다.-----------------------------------------------------
        //핸들러가 하는 역할은 러너블 객체를 UI 스레드에 주는것
        Handler handler = new Handler();//핸들러는 일회용?
        handler.postDelayed(new Runnable() {//핸들러를 이용해서 포스트한다.
            @Override
            public void run() {
                for (Ball2 ball : points) {
                    ball.update();
                }
                invalidate();
                handler.postDelayed(this, 10);//핸들러는 일회용이여서 또 붙여준다.
            }
        },10);
//------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for(Ball2 ball:points){
            ball.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x=(int)event.getX();
        int y=(int)event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            points.add(new Ball2(x, y, getWidth(), getHeight()));
            //invalidate();//=> 있고 없고 차이가 뭐지...?
            return true;
        }
        return super.onTouchEvent(event);
    }
}

class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    ArrayList<Ball2> points = new ArrayList<>();
    Thread threadSView;//1.스레드 참조자 만들고
    boolean threadRunning = true;//2.스레드 러닝이라는 변수 만들고

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (points) { //1번 신호등 :points에 대해서 동시 접근을 안할거야
                points.add(new Ball2(x, y, getWidth(), getHeight()));
            }
            //invalidate();=>서브 스레드가 주기적으로 가져가므로 그냥 더해주기만 하면 된다. 서브 스레드가 있을때는 invalidate 메서드는 불필요
            return true;
        }
        return super.onTouchEvent(event);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(Color.CYAN);
        getHolder().addCallback(this);
        threadSView = new Thread(this);//3.생성자 쪽에서 스레드 만들어주고
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        threadSView.start();//4.스레드 시작하고
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder,  int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        threadRunning = false;//5.스레드 종료하고
        try {
            threadSView.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //스레드 같은 경우 일단 홀더가 있어야함, 홀더의 역할이 뭐징
        SurfaceHolder holder = getHolder();
        while (threadRunning) {
            Canvas canvas = null;
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.CYAN);
                //2번 신호등 :points에 대해서 동시 접근을 안할거야
                synchronized (points) {
                    for (Ball2 ball : points) {
                        ball.update();
                        ball.draw(canvas);
                    }
                }
                holder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyHomeCCTV extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    Thread threadSView;
    boolean threadRunning=true;


    public MyHomeCCTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        threadSView=new Thread(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        threadSView.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        threadRunning = false;
        try {
            threadSView.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final int maxImgSize=1000000;//1MG 바이트를 메모리에 할당

        byte[] arr=new byte[maxImgSize];//이미지 크기가 클수록 바이트 수가 많이 필요

        try {
            URL url = new URL("http://220.233.144.165:8888/mjpg/video.mjpg");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();//주소 옮겨주고
            InputStream in=con.getInputStream();//통신 시작, 네트워크 카드쪽 읽는거
            while (threadRunning){
                int i=0;
                for (;i<1000;i++){
                    int b=in.read();//한 바이트를 읽어
                    if (b==0xff){//Start OF Image의 시작 바이트
                        int b2=in.read();//한 바이트를 더 읽어
                        if (b2==0xd8)//Start OF Image의 끝 바이트
                            break;
                    }
                }
                if (i>999){
                    Log.e("MyHomeCCTV","Bad head");
                    continue;//=>하면 다시 while문 첫 줄로 감, 천장씩 잘라서 읽는 결과
                }
                arr[0]=(byte) 0xff;
                arr[1]=(byte) 0xd8;
                i=2;
                for (;i<maxImgSize;i++){
                    int b=in.read();
                    arr[i]=(byte) b;
                    if(b==0xff){//End OF Image의 시작 바이트
                        i++;
                        int b2=in.read();
                        arr[i]=(byte) b2;
                        if (b2==0xd9){//End OF Image의 끝 바이트
                            break;
                        }
                    }
                }
                i++;
                int nBytes=i;
                Log.e("MyHomeCCTV","got an image, "+nBytes+"bytes!");

                Bitmap bitmap= BitmapFactory.decodeByteArray(arr,0,nBytes);
                bitmap=Bitmap.createScaledBitmap(bitmap,getWidth(),getHeight(),true);//수신되는 이미지를 뷰 크기에 맞춰주기

                SurfaceHolder holder=getHolder();
                Canvas canvas=null;
                canvas=holder.lockCanvas();
                if(canvas!=null){
                    canvas.drawColor(Color.TRANSPARENT);
                    canvas.drawBitmap(bitmap,0,0,null);
                    holder.unlockCanvasAndPost(canvas);
                }

            }

        }catch (Exception e){
            Log.e("MyHomeCCTV","Error:"+e.toString());
        }
    }
}