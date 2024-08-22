원격 URL 사이트
https://github.com/fury999io/public-ip-cams?tab=readme-ov-file

통신 읽는 코드 참고 링크
https://answers.opencv.org/question/15812/ip-camera-frames-manipulation/

----------------------------------------------------------------------------------------------
### 커스텀 뷰

커스텀 뷰는 기본적으로 View 클래스를 확장하여 사용자 정의 레이아웃 및 그래픽을 구현할 수 있는 방법이다.

커스텀 뷰에서 그림을 그리려면 View 클래스의 onDraw() 메소드를 오버라이드하면 된다.

커스텀 뷰 안에서 터치 이벤트를 처리하려면 View 클래스의 onTouchEvent() 메소드를 오버라이드한다.
`````````````````
public class CustomView extends View {

protected void onDraw(Canvas canvas){
      Paint paint=new Paint();
      //그리기 코드 작성
  }

public boolean onTouchEvent(MotionEvent event){
      //터치 이벤트 처리 코드 작성
  }
}
`````````````````
onDraw()의 매개 변수는 Canvas로 이 Canvas 클래스는 
drawRect(float left, float top, float right, float bottom, Paint paint)나 
drawText(String text, float x, float y, Paint paint)와 같은 모든 그리기 메소드를 가지고 있다.

Canvas의 메소드는 항상 Paint 객체를 마지막 매개 변수로 하여 호출 되어야 한다.

애플리케이션을 다시 그려야 한다면 invalidate()를 호출한다. invalidate()가 호출되면 안드로이드는 적절한 시간에 onDraw()를 호출하게 된다.

----------------------------------------------------------------------------------------------
### 이미지 표시하기


----------------------------------------------------------------------------------------------
### 서피스 뷰
애플리케이션에서 많이 복잡하지 않은 그래픽은 캔버스에 그리면 된다.

하지만 게임과 같이 복잡하고 빠른 그래픽이 요구되는 애플리케이션에서 캔버스에 그린다는 것은 무리가 있다. 왜냐하면 사용자 인터페이스를 처리하는 메인 스레드의 시간을 너무 많이 빼앗기 때문이다.
메인 스레드가 그래픽에 너무 시간을 많이 소모하면 사용자의 인터페이스를 처리하지 못하게 되어서 심하면 애플리케이션이 멈춘 것처럼 느낄 수도 있다.

따라서 이런 경우에는 그리기를 전담하는 별도의 스레드를 만드는 것이 좋다.

또한, 네트워크에서 영상을 가져오는 경우에도 별도의 스레드가 필요하다.

안드로이드에서는 UI 스레드에서 소켓(네트워크)을 읽거나 쓰지 못하게 만들어져있다.
원격으로 영상을 받기 위해서는 별도의 작업 스레드가 필요하며 작업 스레드에서 화면을 그릴때는 서피스 뷰를 사용하면 된다.

#### 서피스 뷰 사용 방법

먼저 SurfaceView를 상속받아서 새로운 클래스를 정의한다.

그리고 액티비티의 화면으로 서피스 뷰를 설정한 후에 새로운 스레드에서 이 서피스 뷰에 그림을 그리면 된다.

하지만 새로운 스레드가 실제로 서피스를 생성하기 전에 그림을 그리면 안된다. 따라서 서피스가 생성되고 소멸되는 시점을 그림을 담당하는 스레드에 알려주어야 한다.

이러한 목적으로 우리의 서피스 뷰 클래스는 SurfaceHolder.Callback을 구현한다. 이 인터페이스는 서피스 뷰에 대한 정보를 통지해준다. 예를 들어서 서피스 뷰가 생성되거나 변경되거나 파괴될 때 우리에게 통지해준다.

서피스 뷰 객체는 우리가 직접 처리할 수는 없고 반드시 SurfacdHolder를 통하여 처리하여야 한다.
왜냐하면 안드로이드 시스템이 서피스 뷰를 실제 장치의 화면으로 부지런히 복사하여야 하기 때문이다. 서피스 뷰 객체가 초기화될 때 getHolder()를 호출하여서 SurfaceHolder를 얻으면 된다.

1. **SurfaceView 클래스 상속**: 먼저 `SurfaceView`를 상속받아서 새로운 클래스를 정의합니다.
2. **SurfaceHolder.Callback 구현**: Surface의 생명주기를 관리하기 위해 `SurfaceHolder.Callback` 인터페이스를 구현합니다. 이 인터페이스는 Surface가 생성, 변경, 파괴될 때 알림을 받습니다.
3. **SurfaceHolder 사용**: `getHolder()` 메서드를 호출하여 `SurfaceHolder` 객체를 얻고, 이를 통해 SurfaceView의 Surface에 접근하여 그리기 작업을 수행합니다.
   
a. 서피스 뷰 파일
````````````````
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private MyThread thread;
    SurfaceHolder holder=getHolder();

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry=true;

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
      //생성자에서 SurfaceHolder를 매개변수로 받아 사용자 입력을 저장한다.
    }

````````````````

b. 메인 파일
````````````````
public class MainActivity extends AppCompatActivity {
    MySurfaceView view;

    //MainActivity에서 MySurfaceView를 인스턴스화하고, setContentView를 통해 UI로 설정하여 사용자 정의 View로 사용하고 있다
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new MySurfaceView(this);
        setContentView(view);
    }
````````````````




