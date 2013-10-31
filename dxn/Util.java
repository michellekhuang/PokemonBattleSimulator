package dxn;

import javax.imageio.*;
import java.io.*;
import java.awt.*;

/**
 *Utilities class
 * 
 * @author Michelle Huang
 * @version 001 10/22/13
 */
public class Util
{

    /**
     * Constructs a new array
     * @return newArray the created array
     */
    public static Object[] arrayPlus(Object[] originalArray, Object extra)
    {
        Object[] newArray = new Object[1+originalArray.length];
        for( int i = 0; i<originalArray.length; i++)
        newArray[i] = originalArray[i];
        newArray[originalArray.length]=extra;
        return newArray;
    }
    
    /**
     * 
     * @return newArray 
     */
    public static Object[] deleteFirstElement (Object[] originalArray)
    {
        Object[] newArray = new Object[originalArray.length-1];
        for( int i = 0; i<originalArray.length-1; i++)
        newArray[i] = originalArray[i+1];
        return newArray;
    }
    
    /**
     * Generates a pseudorandom number.
     * @return random number
     */
    public static double rand()
    {
        return Math.random();
    }
    
    /**
     * 
     */
    public static int randInt(int x, int y) //returns random integer between x and y
    {
        double randomNumber = x + (1+y-x)*Math.random();
        return (int)randomNumber;
    }
    
    public static double randDouble(int x, int y) //returns random duble between x and y
    {
        double randomNumber = x + (1+y-x)*Math.random();
        return randomNumber;
    }
    
    public static void delay(int ms)
    {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {System.err.println("dxn.delay() was interrupted");}
    }
    
    public static Image readImage(String path)
    {
        Image img = null;
        try
        {
            img = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            System.err.println("Image could not be read");
        }
        return img;
    }

}
