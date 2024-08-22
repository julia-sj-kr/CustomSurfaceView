원격 URL 사이트
https://github.com/fury999io/public-ip-cams?tab=readme-ov-file

통신 읽는 코드 참고 링크
https://answers.opencv.org/question/15812/ip-camera-frames-manipulation/

----------------------------------------------------------------------------------------------
######커스텀 뷰
커스텀 뷰는 기본적으로 View 클래스를 확장하여 사용자 정의 레이아웃 및 그래픽을 구현할 수 있는 방법이다.
커스텀 뷰에서 그림을 그리려면 View 클래스의 onDraw() 메소드를 오버라이드하면 된다.
커스텀 뷰 안에서 터치 이벤트를 처리하려면 View 클래스의 onTouchEvent() 메소드를 오버라이드한다.
`````````````````
public class CustomView extends View {

protected void onDraw(Canvas canvas){
      //그리기 코드 작성
  }

public boolean onTouchEvent(MotionEvent event){
      //터치 이벤트 처리 코드 작성
  }
}

