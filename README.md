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

