public class Pokemon
{
    private String species;
    private double level;
    private String[] type;
    private double[] stats = {0, 0, 0, 0, 0, 0};
    private double[] baseStat;
    private double[] EV = {252, 0, 0, 0, 0, 252}; //default EVs
    private double[] IV = {31, 31, 31, 31, 31, 31}; //default IVs
    private double currentHP;
    private String pokeStatus;
    private String[] moveset;
    public int[] pp = {99, 99, 99, 99, 99};
    
    public Pokemon()
    {
        //no-arg constructor makes a lvl 100 pikachu
        this("Pikachu", 100);
    }
    
    public Pokemon(String speciess, int lvl)
    {
        //perfect IVs and 252HP/252Speed EVs
        level = lvl; if (level>100) level=100;
        species = speciess;
        type = database.getType(speciess);
        baseStat = database.getBaseStat(speciess);
        moveset = database.getMoveset(speciess);
        checkLegalEVs();
        stats = StatDmg.calculateStats(baseStat, IV, EV, level, "Timid");
        currentHP = stats[0];//sets currentHP = maxHP
        pokeStatus = "Healthy";
    }
    
    public Pokemon(String speciess, int lvl, double[] EVs, double[] IVs)
    {
        //most flexible constructor
        level = lvl; if (level>100)level = 100;
        species = speciess;
        type = database.getType(speciess);
        EV = EVs;
        IV = IVs;
        checkLegalEVs();
        baseStat = database.getBaseStat(speciess);
        moveset = database.getMoveset(speciess);
        stats = StatDmg.calculateStats(baseStat, IVs, EVs, level, "Timid");
        currentHP = stats[0];//sets currentHP = maxHP
        //sets PP for each move
        for (int i = 0; i<4; i++)
        pp[i] = database.getPP( moveset[i]);
        pp[4] = 9999; //struggle has 9999 uses
        pokeStatus = "Healthy"; //no status ailments
    }
    
    //Attack!
    public double[] move(int moveNumber, Pokemon opponent)
    {
        double basepwr = database.getBasePwr(moveset[moveNumber-1]);
        double typeOne = StatDmg.typeEff(database.moveType(moveset[moveNumber-1]), opponent.getType1());
        double typeTwo = StatDmg.typeEff(database.moveType(moveset[moveNumber-1]), opponent.getType2());
        double crit = StatDmg.critical();
        double acc = StatDmg.accuracy(database.getAcc(moveset[moveNumber-1]));
        double stab;
        //STAB if move type is the same as Pokemon's type, STAB = 1.5
        if (database.moveType(moveset[moveNumber-1]).equals(getType1()) || database.moveType(moveset[moveNumber-1]).equals(getType2()))
        stab = 1.5;
        else
        stab = 1;
        
        double attack;
        double defense;
        //if move is physical, use Atk and Def se stats
        if (database.isPhys(moveset[moveNumber-1]))
        {
            defense = opponent.getDef();
            attack = stats[1];
        }
        //if move is special, use SpA adn SpD stats
        else
        {
            defense = opponent.getSpD();
            attack = stats[3];
        }
        double damage = StatDmg.damage(level, basepwr, attack, defense, crit, stab, typeOne, typeTwo, acc);
        //returns a) damage done, b) type effectiveness, c) critical or not, d) miss or not
        double[] info = {damage, (typeOne*typeTwo), crit, acc};
        return info;
    }
    
    public void recover()
    {
        currentHP += (.5)*getHP();
        if (currentHP >= getHP())
            currentHP = getHP();
    }
    
    public void lowerHP(double damage)
    {
        currentHP = currentHP - damage;
        if (currentHP <= 0)
        {
            currentHP = 0;
            pokeStatus = "Fainted";
        }
    }
    
    public boolean catchMe(String ballType)
    {
        boolean x = StatDmg.catchPokemon(StatDmg.findCatchRate(this, ballType));
        if (x)
            pokeStatus = "Caught";
        else;
        return x;
    }
    
    public boolean isFainted()
    {
        if (getStatus().equals("Fainted"))
            return true;
        else
            return false;
    }
    
    //Get methods
    public double getCatchRate(String ball)
    {
        return StatDmg.findCatchRate(this, ball);
    }
    
    public String getName()
    {
        return species;
    }
    
    public int getLevel()
    {
        return (int) level;
    }
    
    public String toString()
    {
        String info;
        info = species + " (lvl " + level + ")\n" + currentHP + "/" + getHP() + "\nStats:\nHP: " + 
               stats[0] + ",Atk: " + stats[1] + ", Def: " + stats[2] + "\nSpA: " + stats[3] + ", SpD: " +
               stats[4] + ", Spe: " + stats[5] + "\nStatus: " + pokeStatus;
        return info;
    }
    
    public String getType1()
    {
        return type[0];
    }
    
    public String getType2()
    {
        return type[1];
    }
    
    public double getCurrentHP()
    {
        return currentHP;
    }
    
    public double getPercentHP(double decimalPlace)
    {
        double percent = Math.round(Math.pow(10, decimalPlace + 2)*(getCurrentHP()/getHP()));
        percent = percent / Math.pow(10, decimalPlace + 2);
        return percent * 100;
    }
    
    public String getStatus()
    {
        return pokeStatus;
    }
    
    public double getHP()
    {
        return stats[0];
    }
    
    public double getDef()
    {
        return stats[2];
    }
    
    public double getSpD()
    {
        return stats[4];
    }
    
    public double getSpe()
    {
        return stats[5];
    }
    
    public String[] getMoveset()
    {
        return moveset;
    }
    
    public String getMoveName(int move)
    {
        return moveset[move-1];
    }
    
    //checks if EVs are legal (no EV can be more than 255, total cannot exceed 510)
    private void checkLegalEVs()
    {
        for (int i=0; i<EV.length; i++)
        {
            if (EV[i]>255)
                EV[i] = 255;
        }
        if ((EV[0] + EV[1] + EV[2] + EV[3] + EV[4] + EV[5]) > 510)
        {
            EV[0] = 0;
            EV[1] = 0;
            EV[2] = 0;
            EV[3] = 0;
            EV[4] = 0;
            EV[5] = 0;
        }
    }
}
    