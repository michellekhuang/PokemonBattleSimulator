//Contains information for base stats, move power, move accuracy, Pokemon types, and move types
import java.io.*;
import javax.imageio.*;
import java.io.File;
import java.awt.*;

public class database
{
    public static String[] getType(String ID)
    {
        String type[] = new String[2];
        if (ID.equals("Pikachu"))
        {
            type[0] = "Electric"; type[1] = "";
        }
        else if (ID.equals("Lugia"))
        {
            type[0] = "Psychic"; type[1] = "Flying";
        }
        else if (ID.equals("Charizard"))
        {
            type[0] = "Fire"; type[1] = "Flying";
        }
        else if (ID.equals("Chikorita"))
        {
            type[0] = "Grass"; type[1] = "";
        }
        else
        {
            type[0] = ""; type[1] = "";
        }
        return type;
    }
    
    public static double[] getBaseStat(String ID)
    {
        double stat[] = new double[6];
        if (ID.equals("Pikachu"))
        {
            stat[0] = 35;
            stat[1] = 55;
            stat[2] = 30;
            stat[3] = 90;
            stat[4] = 40;
            stat[5] = 120; //SpA should be 50, Spe should be 90
        }
        else if (ID.equals("Lugia"))
        {
            stat[0] = 106;
            stat[1] = 90;
            stat[2] = 130;
            stat[3] = 90;
            stat[4] = 154;
            stat[5] = 110;
        }
        else if (ID.equals("Charizard"))
        {
            stat[0] = 78;
            stat[1] = 84;
            stat[2] = 78;
            stat[3] = 109;
            stat[4] = 85;
            stat[5] = 100;
        }
        else if (ID.equals("Chikorita"))
        {
            stat[0] = 45;
            stat[1] = 49;
            stat[2] = 65;
            stat[3] = 49;
            stat[4] = 65;
            stat[5] = 45;
        }
        return stat;
    }
    
    public static String[] getMoveset(String ID)
    {
        String[] moveset = new String[5];
        if (ID.equals("Pikachu"))
        {
            moveset[0] = "Thunderbolt";
            moveset[1] = "Thunder";
            moveset[2] = "Surf";
            moveset[3] = "Body Slam";
        }
        else if (ID.equals("Lugia"))
        {
            moveset[0] = "Psychic";
            moveset[1] = "Ice Beam";
            moveset[2] = "Thunderbolt";
            moveset[3] = "Recover";
        }
        else if (ID.equals("Charizard"))
        {
            moveset[0] = "Flamethrower";
            moveset[1] = "Fire Blast";
            moveset[2] = "Brick Break";
            moveset[3] = "Earthquake";
        }
        else if (ID.equals("Chikorita"))
        {
            moveset[0] = "Recover";
            moveset[1] = "Razor Leaf";
            moveset[2] = "Body Slam";
            moveset[3] = "Hydro Pump";
        }
        else
        {
        }
        moveset[4] = "Struggle";
        return moveset;
    }
    
    public static double getCatchRate(String ID)
    {
        if (ID.equals("Lugia"))
            return 15; //normally 3
        else
            return 255;
    }
    
    public static Image getUserSprite(String ID)
    {
        Image sprite = null;
        try
        {
            if (ID.equals("Pikachu"))
                sprite = ImageIO.read(new File("sprites//pokemon//Pikachu//DPbf.png"));
            else if (ID.equals("Charizard"))
                sprite = ImageIO.read(new File("sprites//pokemon//Charizard//userSprite//1.png"));
            else if (ID.equals("Chikorita"))
                sprite = ImageIO.read(new File("sprites//pokemon//Chikorita//DPbm.png"));
        }
        catch (IOException e)
        {
            System.out.println("Image could not be read");
        }
        return sprite;
    }
    
    public static Image getEnemySprite(String ID)
    {
        Image sprite = null;
        try
        {
            if (ID.equals("Lugia"))
                sprite = ImageIO.read(new File("sprites//pokemon//Lugia//IdleAnimation//front//1.png"));
        }
        catch (IOException e)
        {
            System.out.println("Image could not be read");
        }
        return sprite;
    }
    
    public static Image getBackground(String name)
    {
        Image sprite = null;
        try
        {
            sprite = ImageIO.read(new File("sprites//background//"+name+".png"));
        }
        catch (IOException e)
        {
            System.out.println("Image could not be read");
        }
        return sprite;
    }
    
    //Moves
    public static double getBasePwr(String moveName)
    {
        if (moveName.equals("Thunder") || moveName.equals("Fire Blast") || moveName.equals("Hydro Pump"))
            return 120;
        else if (moveName.equals("Quick Attack"))
            return 40;
        else if (moveName.equals("Psychic"))
            return 90;
        else if (moveName.equals("Ice Beam") || moveName.equals("Surf") || moveName.equals("Thunderbolt")
                || moveName.equals("Flamethrower"))
            return 95;
        else if (moveName.equals("Brick Break"))
            return 75;
        else if (moveName.equals("Earthquake"))
            return 100;
        else if (moveName.equals("Body Slam"))
            return 85;
        else if (moveName.equals("Razor Leaf"))
            return 55;
        else if (moveName.equals("Struggle"))
            return 50;
        else if (moveName.equals("Teleport"))
            return 10;
        else
            return 0;
    }
    
    public static double getAcc(String moveName)
    {
        //only list non 100% accuracy moves
        if (moveName.equals("Thunder"))
            return 0.7;
        else if (moveName.equals("Fire Blast") || moveName.equals("Hydro Pump"))
            return 0.85;
        else
            return 1;
    }
    
    public static String moveType(String moveName)
    {
        if (moveName.equals("Thunderbolt") || moveName.equals("Thunder"))
            return "Electric";
        else if (moveName.equals("Surf") || moveName.equals("Hydro Pump"))
            return "Water";
        else if (moveName.equals("Quick Attack") || moveName.equals("Body Slam"))
            return "Normal";
        else if (moveName.equals("Psychic"))
            return "Psychic";
        else if (moveName.equals("Ice Beam"))
            return "Ice";
        else if (moveName.equals("Fire Blast") || moveName.equals("Flamethrower"))
            return "Fire";
        else if (moveName.equals("Brick Break"))
            return "Fighting";
        else if (moveName.equals("Earthquake"))
            return "Ground";
        else if (moveName.equals("Razor Leaf") || moveName.equals("Recover"))
            return "Grass";
        else
            return "Error - Could not retrieve moveType";
    }
    
    public static int getPP(String moveName)
    {
        if (moveName.equals("Thunderbolt") || moveName.equals("Surf") || moveName.equals("Ice Beam")
            || moveName.equals("Flamethrower") || moveName.equals("Brick Break"))
            return 15;
        else if (moveName.equals("Thunder") || moveName.equals("Fire Blast") || moveName.equals("Hydro Pump"))
            return 5;
        else if (moveName.equals("Quick Attack"))
            return 40;
        else if (moveName.equals("Psychic") || moveName.equals("Earthquake") || moveName.equals("Recover"))
            return 10;
        else if (moveName.equals("Body Slam"))
            return 20;
        else if (moveName.equals("Razor Leaf"))
            return 25;
        else if (moveName.equals("Teleport"))
            return 1;
        else
            return 0;
    }
    
    public static boolean isPhys(String move)
    {
        if (move.equals("Quick Attack") || move.equals("Brick Break") || move.equals("Body Slam")
            || move.equals("Earthquake") || move.equals("Razor Leaf") || move.equals("Struggle"))
            return true;
        else
            return false;
    }
    
    public static boolean nonDamage(String move)
    {
        if (move.equals("Recover"))
            return true;
        else
            return false;
    }
}
            
            
        
        


        
