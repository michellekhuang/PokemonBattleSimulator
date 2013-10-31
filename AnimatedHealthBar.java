import java.awt.*;
import javax.swing.*;

public class AnimatedHealthBar extends JPanel implements Runnable
{
    private double percHP;
    private double percHPNew;
    public boolean isDone;
    public Pokemon poke;

    public AnimatedHealthBar(Pokemon pokem)
    {
        setBackground(Color.white);
        percHPNew = 100.000;
        percHP = 100.000;
        isDone = true;
        poke = pokem;
    }
    
    public void setHP( double hp)
    {
        percHPNew = hp;
        isDone = false;
    }
    
    public void setPokemon(Pokemon pokem)
    {
        poke = pokem;
        setHP(pokem.getPercentHP(8));
        isDone = false;
    }

    
    
    public void animateHP()
    {
        if( percHP > percHPNew)
        {
             while(percHP>percHPNew)
             {
                 percHP = percHP - .2;
                 repaint();
                 try 
                 {
                     Thread.sleep(5);
                 } 
                 catch (InterruptedException e) 
                 {} 
             }
        }
        else
        {
            while(percHP<percHPNew)
             {
                 percHP = percHP + .2;
                 repaint();
                 try 
                 {
                     Thread.sleep(5);
                 } catch (InterruptedException e) 
                 {} 
             }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.drawHealthBarBase(g, 20, 15, poke);
        this.drawHealthBar(g, 20, 15, poke);
    }
    
    public static void delay(int ms)
    {
        try 
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) 
        {}
    }
        private void drawHealthBarBase(Graphics g, int x, int y, Pokemon poke)
    {
         //shadow
        g.setColor(Color.black);
        g.fillRoundRect(x-16, y-12, 182, 33, 8, 8);
        //1st outline
        g.setColor(Color.gray);
        g.fillRoundRect(x-20, y-15, 182, 33, 8, 8);
        //2nd outline
        g.setColor(Color.lightGray);
        g.fillRoundRect(x-18, y-14, 178, 31, 8, 8);
        
        //HP bar outline
        g.setColor(Color.black);
        g.fillRoundRect(x-15, y-1, 170, 16, 8, 8);
        
        g.setColor(new Color(70, 90, 255));
        g.setFont( new Font("Default", Font.BOLD, 8));
        g.drawString("HP", x-12, y+9);
        g.setFont( new Font("Default", Font.BOLD, 9));
        g.setColor(new Color(30, 40, 205));
        g.drawString("Lv", x+50, y-4);
        g.setColor(Color.black);
        g.drawString(poke.getLevel() + "", x+63, y-4);
        g.drawString(poke.getName(), x-5, y-4); 
    }
    
     private void drawHealthBar(Graphics g, int x, int y, Pokemon poke)
    {
        double percentHPLeft = percHP;
        if(percentHPLeft<0) 
            percentHPLeft = 0;
        
        int userHealthBar = (int)percentHPLeft;
        Color healthBarColor;
       
        //draw HP above health bar
        g.setColor(Color.black);
        if(percHP <= 0)
            g.setColor(Color.red);
        g.setFont( new Font("Default",Font.BOLD,9));    
        g.drawString((int)((percHP/100)*poke.getHP())+"/"+ (int)poke.getHP(),x+115,y-4);
       
        
        int colorScale = (int)Math.floor(510*(percentHPLeft/100));
        if(percentHPLeft >=50)
            healthBarColor = new Color( 510-colorScale,255,0);
        else
            healthBarColor = new Color( 255,colorScale,0);
            
             //fill in the empty part of the health bar
           g.setColor(Color.gray);
           g.fillRoundRect(x+1,y+1, 150,9,4,4);
                    //shade the health bar
                    g.setColor((Color.gray).darker());
                    g.fillRoundRect(x+1,y+3, 149,6,3,3);
                    g.setColor(((Color.gray).darker()).darker());
                    g.fillRect(x+1,y+5, 147,2);
         
           //fill the health bar
           g.setColor((healthBarColor.darker()).darker());
           g.fillRoundRect(x+1,y+1,(int)(1.5*userHealthBar),9, 4,4);
                // shade health bar
                    g.setColor(healthBarColor.darker());
                    g.fillRoundRect(x+2,y+1,(int)(1.5*userHealthBar)-2,6,3,3);
                    g.setColor(healthBarColor);
                    g.fillRect(x+4,y+3,(int)(1.5*userHealthBar)-6,2);

          
    }
    
        //threading
    public Thread newThread;
    public void animateHPThread()
    {
        newThread = new Thread(this);
        newThread.start();
    }
    public void run()
    {
        animateHP();
        newThread = null;
    }
}


