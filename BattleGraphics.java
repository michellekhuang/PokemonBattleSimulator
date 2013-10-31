import java.awt.*;
import javax.swing.*;

public class BattleGraphics extends JPanel
{
    //draws a cascade of shapes starting from the top left corner
    
    public battle theBattle;
    
    public BattleGraphics(battle battle1)
    {
        theBattle = battle1;
    }
    
    public void setBattle(battle battle1)
    {
        theBattle = battle1;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //draw user pokemon's sprite
        g.drawImage(database.getUserSprite(theBattle.getParty()[0].getName()), 20, 80, null);
    }
    //end method paintComponent
}//end class BattleGraphics