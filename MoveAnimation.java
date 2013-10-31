 import dxn.Animations;
 
 public class MoveAnimation extends Animations
 {
     public MoveAnimation()
     {
         setFrameDelay(100);
     }
     
     public void setMove(String movename)
     {
         loadImages("\\sprites\\moves\\" + movename + "\\", ".png");
     }
 }
        