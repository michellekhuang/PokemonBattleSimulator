import java.util.Random;

public class StatDmg
{
    public StatDmg()
    {
    }
    
    public static double[] calculateStats(double[] baseStat, double[] IV, double[] EV, double level, String nat)
    {
        double[] stats = {0, 0, 0, 0, 0, 0};
        double nature[] = {1, 1, 1, 1, 1, 1};
        
        //HP
        stats[0] = ((2 * baseStat[0] + IV[0] + (EV[0]/4)) * level / 100 + level + 10);
        //Atk, Def, SpA, SpD, Spe
        stats[1] = (((2 * baseStat[1] + IV[1] + (EV[1] / 4)) * level / 100 + 5) * nature[1]);
        stats[2] = (((2 * baseStat[2] + IV[2] + (EV[2] / 4)) * level / 100 + 5) * nature[2]);
        stats[3] = (((2 * baseStat[3] + IV[3] + (EV[3] / 4)) * level / 100 + 5) * nature[3]);
        stats[4] = (((2 * baseStat[4] + IV[4] + (EV[4] / 4)) * level / 100 + 5) * nature[4]);
        stats[5] = (((2 * baseStat[5] + IV[5] + (EV[5] / 4)) * level / 100 + 5) * nature[5]);
        return stats;
    }
    
    public static double damage(double level, double BasePower, double Atk, double Def, double critical, double STAB, 
                                double type1, double type2, double accuracy)
    {
        Random rand = new Random();
        double dmg;
        double Mod1 = 1; //for burn, Sunny Day, Etc.
        double Mod2 = 1; //for items, ex life orb
        double randomness = Math.floor(( (217 + rand.nextDouble()*38) * 100) / 255);
        dmg = (((((((level * 2 / 5) + 2) * BasePower * Atk / 50) / Def) * Mod1) + 2) *
                critical * Mod2 * randomness / 100) * STAB * type1 * type2 * accuracy;
        return dmg;
    }
    
    public static double critical()
    {
        Random rand = new Random();
        if ( (rand.nextDouble() * 100) <= 6.25)
            return 2;
        else
            return 1;
    }
    
    public static double accuracy(double accuracy)
    {
        Random rand = new Random();
        if ( rand.nextDouble() <= accuracy)
            return 1;
        else
            return 0;
    }
    
    public static boolean catchPokemon(double catchRate)
    {
        Random rand = new Random();
        if (rand.nextDouble() < catchRate)
            return true;
        else
            return false;
    }
    
    public static double findCatchRate(Pokemon victim, String ballType)
    {
        double pokemonStatusBonus = 1;
        double ballRate;
        if (ballType.equals("Master Ball"))
            ballRate = 255;
        else if (ballType.equals("Great Ball"))
            ballRate = 1.5;
        else if (ballType.equals("Ultra Ball"))
            ballRate = 2;
        else
            ballRate = 1;
        double a = ((3*victim.getHP() - 2*victim.getCurrentHP()) * (database.getCatchRate(victim.getName())
        *ballRate)/(3*victim.getHP())) * pokemonStatusBonus;
        double b = 1048560 / Math.sqrt( Math.sqrt(16711680/a));
        double catchRate;
        catchRate = Math.pow(((b+1) / 65536), 4);
        return catchRate;
    }
    
    public static double typeEff(String type1, String type2)
    {
        if (type1.equals("Normal"))
        {
            if (type2.equals("Rock") || type2.equals("Steel"))
            return 0.5;
            if (type2.equals("Ghost"))
            return 0;
        }
        
        if (type1.equals("Fighting"))
        {
            if (type2.equals("Normal") || type2.equals("Rock") || type2.equals("Ice") || type2.equals("Dark")
                || type2.equals("Steel"))
            return 2;
            if (type2.equals("Poison") || type2.equals("Flying") || type2.equals("Bug") || type2.equals("Psychic"))
            return 0.5;
            if (type2.equals("Ghost"))
            return 0;
        }
        
        if (type1.equals("Ground"))
        {
            if (type2.equals("Electric") || type2.equals("Fire") || type2.equals("Poison") || type2.equals("Rock")
                || type2.equals("Steel"))
            return 2;
            if (type2.equals("Bug") || type2.equals("Grass"))
            return 0.5;
            if (type2.equals("Flying"))
            return 0;
        }
        
        if (type1.equals("Fire"))
        {
            if (type2.equals("Bug") || type2.equals("Grass") || type2.equals("Ice") || type2.equals("Steel"))
            return 2;
            if (type2.equals("Fire") || type2.equals("Rock") || type2.equals("Dragon") || type2.equals("Water"))
            return 0.5;
        }
        
        if (type1.equals("Water"))
        {
            if (type2.equals("Fire") || type2.equals("Ground") || type2.equals("Rock"))
            return 2;
            if (type2.equals("Grass") || type2.equals("Dragon") || type2.equals("Water"))
            return 0.5;
        }
        
        if (type1.equals("Grass"))
        {
            if (type2.equals("Ground") || type2.equals("Rock") || type2.equals("Water"))
            return 2;
            if (type2.equals("Bug") || type2.equals("Fire") || type2.equals("Flying") || type2.equals("Dragon")
            || type2.equals("Grass") || type2.equals("Poison") || type2.equals("Steel"))
            return 0.5;
        }
        
        if (type1.equals("Electric"))
        {
            if (type2.equals("Flying") || type2.equals("Water"))
            return 2;
            if (type2.equals("Dragon") || type2.equals("Electric") || type2.equals("Grass"))
            return 0.5;
            if (type2.equals("Ground"))
            return 0;
        }
        
        if (type1.equals("Psychic"))
        {
            if (type2.equals("Fighting") || type2.equals("Poison"))
            return 2;
            if (type2.equals("Steel") || type2.equals("Psychic"))
            return 0.5;
            if (type2.equals("Dark"))
            return 0;
        }
        
        if (type1.equals("Ice"))
        {
            if (type2.equals("Flying") || type2.equals("Grass") || type2.equals("Dragon") || type2.equals("Ground"))
            return 2;
            if (type2.equals("Fire") || type2.equals("Ice") || type2.equals("Steel") || type2.equals("Water"))
            return 0.5;
        }
        
        if (type1.equals("Flying"))
        {
            if (type2.equals("Bug") || type2.equals("Grass") || type2.equals("Fighting"))
            return 2;
            if (type2.equals("Electric") || type2.equals("Rock") || type2.equals("Steel"))
            return 0.5;
        }
        
        if (type1.equals("Poison"))
        {
            if (type2.equals("Grass"))
            return 2;
            if (type2.equals("Ground") || type2.equals("Ghost") || type2.equals("Poison") || type2.equals("Rock"))
            return 0.5;
            if (type2.equals("Steel"))
            return 0;
        }
        
        if (type1.equals("Rock"))
        {
            if (type2.equals("Bug") || type2.equals("Fire") || type2.equals("Flying") || type2.equals("Ice"))
            return 2;
            if (type2.equals("Fighting") || type2.equals("Ground") || type2.equals("Steel"))
            return 0.5;
        }
        
        if (type1.equals("Bug"))
        {
            if (type2.equals("Dark") || type2.equals("Grass") || type2.equals("Psychic"))
            return 2;
            if (type2.equals("Fire") || type2.equals("Fighting") || type2.equals("Flying") || type2.equals("Ghost")
            || type2.equals("Steel") || type2.equals("Poison"))
            return 0.5;
        }
        
        if (type1.equals("Ghost"))
        {
            if (type2.equals("Ghost") || type2.equals("Psychic"))
            return 2;
            if (type2.equals("Dark") || type2.equals("Steel"))
            return 0.5;
            if (type2.equals("Normal"))
            return 0;
        }
        
        if (type1.equals("Steel"))
        {
            if (type2.equals("Ice") || type2.equals("Rock"))
            return 2;
            if (type2.equals("Electric") || type2.equals("Fire") || type2.equals("Steel") || type2.equals("Water"))
            return 0.5;
        }
        
        if (type1.equals("Dragon"))
        {
            if (type2.equals("Dragon"))
            return 2;
            if (type2.equals("Steel"))
            return 0.5;
        }
        
        if (type1.equals("Dark"))
        {
            if (type2.equals("Ghost") || type2.equals("Psychic"))
            return 2;
            if (type2.equals("Dark") || type2.equals("Steel") || type2.equals("Fighting"))
            return 0.5;
        }
        
        return 1;
    }
}
            
    
    
        