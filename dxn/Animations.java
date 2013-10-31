
/**
 * Simplifies animation when using java.awt.Graphics and javax.swing
 * Built in io and easy multithreading
 * One method runs the entire animation any amount of times
 * 
 * @author Michelle Huang
 * @ver 10/23/13
 */
package dxn;

import java.awt.Image;
import java.awt.Graphics;
import javax.imageio.*;
import java.io.*;
import javax.swing.JPanel;

public class Animations extends JPanel implements Runnable
{
    private Animation anim;//the animation object
    private int frameDelay;//delay between each frame
    private int loopCount;//number of times the animation repeats
    private boolean infiniteLooping;//if true, loops infinitely (ex. idle animations)
    private int xScale;
    private int yScale;
    private int xPos;
    private int yPos;
    
    public Animations()
    {
        this(new Animation(), 60);
    }
    
    public Animations(Animation newAnim, int delay)
    {
        anim = newAnim;
        frameDelay = delay;
        loopCount = 1;
        xPos = 0;
        yPos = 0;
    }
    
    /**
    *Loads an array of images into the animation class.
    *The animation images must be named as integers starting from 1
    *
    *@param directory   The directory in which the images are located
    *@param extension   The filename extension for the frames
    */
    public void loadImages(String directory, String extension)
    {
        Image oneFrame = null;
        Image[] allFrames = new Image[0];
        for(int i=1; i>0;i++)
        {
            try
            {
               oneFrame =  ImageIO.read(new File("" + directory + "//" + i + extension));
            }
            catch(IOException e)
            {
                break;//once an image cannot be read, stop loading
            }
            allFrames = arrayPlus(allFrames, oneFrame); //adds oneFrame to the end of allFrames
        }
        anim.setFrames(allFrames);
        xScale = anim.getActiveFrame().getWidth(null);
        yScale = anim.getActiveFrame().getHeight(null);
    }
    
    public void clearImages()
    {
        anim = new Animation();
    }
 
    /**
     * Display each still image with a delay between them
     */
     public void animate()
    {
        while(!( anim.isDone)){//while animation is not done
        repaint();
        anim.incrementFrame();
        try {
         Thread.sleep(frameDelay);//delay for however many ms
         } catch (InterruptedException e) {} 
         }//end while loop
    }//end method animate()
    
     public void showFrame(int frameNumber)
     {
        anim.setActiveFrame(frameNumber);
        repaint();
    }
    
    /**
     * Stops the animation
     */
    public void stop()
    {
        anim.isDone = true;
        infiniteLooping = false;
        loopCount = 0;
    }
        
    /**
     * Sets the delay (in ms) between displaying each still frame
     * @param   rate    The delay to be set
     */
    public void setFrameDelay(int delay)
    {
        frameDelay = delay;
    }
    
    /**
     * Returns the delay between each frame in ms
     * @return  The delay
     */
    public int getFrameDelay()
    {
        return frameDelay;
    }
    
    public void reset()
    {
        anim.reset();  
    }
     
    /**
     * Rescales by setting the animation's width and height.
     * @param   x    the new height
     * @param   y    the new width
     */
    public void rescale(int x, int y)
    {
        xScale = x;
        yScale = y;
    }
    
    /**
     * Rescales while maintaining height:width ratio
     * @param  ratio    the ratio to resize by
     */
    public void ratioScale(double ratio)
    {
        xScale = (int)(ratio*xScale);
        yScale = (int)(ratio*yScale);
    }
    
    public int[] getScale()
    {
        int[] scaling = {xScale,yScale};
        return scaling;
    }
    
    //array concat, used when loading images
    private static Image[] arrayPlus(Image[] originalArray, Image extra)
    {
        Image[] newArray = new Image[1+originalArray.length];
        for( int i = 0; i<originalArray.length; i++)
        newArray[i] = originalArray[i];
        newArray[originalArray.length]=extra;
        return newArray;
    }
     public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(anim.getActiveFrame(),xPos,yPos,xScale,yScale, null);
    }//end method paintComponent

    //threading
    public Thread newThread;
    /**
     * Animates in a separate thread indefinitely until stop() is called
     * @see animateThread(int loops)
     */
    public void animateT()
    {
        infiniteLooping = true;
        animateT(1);
    }//end method animateT()
    
    /**
     * Repeats the animation a certain number of times
     * @param loops The number of repetitions
     */
    public void animateT(int loops)
    {
        if(loops <=0)
            System.err.print("ERROR: Animations -> animateT(int loops) -> loops was set to 0 or less");
        else
        {
            loopCount = loops;
            newThread = new Thread(this);
            newThread.start();
        }
    }//end method animateT(int loops)
    
    public void run()
    {
        for(int i=0; i<loopCount; i++)
        {
            animate();
            reset();
        
            if(infiniteLooping)
            {
                loopCount = 2;
                i = 0;
            }
        }
    }//end method run()
    
}//end class AnimationManager

/**
 * Represents an animation as an array of images
 * @author Michelle Huang
 */
class Animation
{
  private Image[] frames;
  private Image activeFrame;
  public boolean isDone;
  private int frameCounter;
  
  public Animation()
  {
      this(null);
  }
  
   public Animation(Image[] images)
  {
      frameCounter = 0;
      frames = images;
      if(frames != null)
      activeFrame = frames[0];
  }  
  
  public Image getActiveFrame()
  {
      return activeFrame;
  }
  
  public void reset()
  {
      frameCounter = 0;
      activeFrame = frames[0];
      isDone = false;
  }
  
  public void incrementFrame()
  {
     // System.out.println("Setting next frame: "+frameCounter+"/"+frames.length);
      frameCounter++;
      if(frameCounter >= frames.length)
        isDone = true;
      else
      {
          // System.out.print("Passed the if/else: "+frameCounter+"/"+frames.length);
          activeFrame = frames[frameCounter];
      }
  }
  
  public void setActiveFrame(int index)
  {
      activeFrame = frames[index];
  }
    
  public void setFrames(Image[] input)
  {
     frames = input; 
     activeFrame = frames[0];
  }
}

