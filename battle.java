import java.util.Random;
public class battle
{
    //user Pokemon party
    private Pokemon[] userPokemon = new Pokemon[3];
    //opposing Pokemon
    private Pokemon Lugia;
    //stores notifications( ex. critical hits, switch ins, damage, etc.)
    public String[] battleMessage;
    public enum state {WIN, LOSE, CONTINUE};
    public state gameState;
    //Random Number Generator
    private Random rand = new Random();
    //variables to help animate in PokemonGUI
    public String lugiaMove;
    public String userMove;
    
    public battle()
    {
        double[] defaultEVs = {250, 0, 0, 0, 0, 252}; //252 HP, 252 Speed
        double[] defaultIVs = {31, 31, 31, 31, 31, 31}; //perfect IVs
        
        //create the Pokemon
        userPokemon[0] = new Pokemon("Pikachu", 100, defaultEVs, defaultIVs);
        userPokemon[1] = new Pokemon("Charizard", 100, defaultEVs, defaultIVs);
        userPokemon[2] = new Pokemon("Chikorita", 100, defaultEVs, defaultIVs);
        Lugia = new Pokemon("Lugia", 100, defaultEVs, defaultIVs);
        
        gameState = state.CONTINUE;
        resetBattleMessage();
        battleMessage = addMessage(battleMessage, "");
        printMessage();
    }
    
    public void command (String type, String action)
    {
        //commands involve 2 parts: {type, action}
        //type: move, switch, throw ball, run
        //action:
        //  for move - the action corresponds to move choice
        //  for switch - the action corresponds to the switchIn number
        //  for throw - the action corresponds to ball type
        
        String[] command = {type, action};
        if(command[0].equals("move"))//attack
        {
            if (userPokemon[0].getSpe() >= Lugia.getSpe())
            {
                userAttack(Integer.parseInt(command[1]));
                if( !(Lugia.isFainted()))
                LugiaAttack();
                else
                gameState = state.LOSE;
            }
        else
            {
                LugiaAttack();
                if( !(userPokemon[0].getStatus().equals("Fainted")))
                userAttack(Integer.parseInt(command[1]));
                if (Lugia.isFainted())
                gameState = state.LOSE;
            }
            checkLose();
            printMessage();
        }
        else if( command[0].equals("switch"))//switch
        {
            switchIn(Integer.parseInt(command[1]));
            LugiaAttack();
            checkLose();
            printMessage();
        }
        else if( command[0].equals("deathSwitch"))//switch after Pokemon faints
        {
            switchIn(Integer.parseInt(command[1]));
            printMessage();
        }
        else if( command[0].equals("throw"))//throw
        {
            throwPokeball(command[1]);
            if(Lugia.getStatus().equals("Caught"))
            gameState = state.WIN;
            else
            LugiaAttack();
            checkLose();
            printMessage();
        }
        else if( command[0].equals("run"))
        {
            gameState = state.LOSE;
            battleMessage = addMessage(battleMessage, "Trainer ran away in fear.");
            printMessage();
        }
        else
        {
            battleMessage = addMessage(battleMessage, "Invalid Command.");
            printMessage();
        }
    }
    
    public void LugiaAttack()
    {
        int moveNumber = 1;
        do{
            moveNumber = 1+rand.nextInt(4);//choses random number between 1 and 4)
            if( (Lugia.pp[0]+Lugia.pp[1]+Lugia.pp[2]+Lugia.pp[3]) == 0)//if Lugia has no pp left, use his first move
            {
                moveNumber = 5;
                break;
            }
        }
        while (Lugia.pp[moveNumber-1] == 0);//reroll if the pp of that move was zero.
        
        lugiaMove = Lugia.getMoveName(moveNumber);
        pokemonAttack(moveNumber, Lugia, userPokemon[0]);
    }
    
    private void userAttack(int moveNumber)
    {
        pokemonAttack(moveNumber, userPokemon[0], Lugia);
        userMove = userPokemon[0].getMoveName(moveNumber);
    }
    
    private void pokemonAttack(int moveNumber, Pokemon user, Pokemon opponent)
    {
        if( !(database.nonDamage(user.getMoveName(moveNumber)))) //if the move deals damage
        {
            double damage[] = user.move(moveNumber, opponent);
            opponent.lowerHP(damage[0]);
            String damagePercent = String.format("%.2f", 100*(damage[0]/opponent.getHP()));
            battleMessage = addMessage(battleMessage, user.getName() + " used " + user.getMoveName(moveNumber) + "!");
            
            if (!(damage[3]==0)) //if it does not miss
            battleMessage = addMessage(battleMessage, "It deals " + (int)damage[0] + " damage(" + damagePercent + "%) to " + opponent.getName());
            //changing the battle message due to type effectiveness.
            if (damage[1]==2||damage[1]==4) battleMessage = addMessage(battleMessage, "Super Effective!");
            if (damage[1]==.5||damage[1]==.25) battleMessage = addMessage(battleMessage, "Not Very Effective...");
            //critical
            if (damage[2]==2) battleMessage = addMessage(battleMessage, "Critical Hit!");
            //miss
            if (damage[3]==0) battleMessage = addMessage(battleMessage, user.getName() + " missed!");
            
            if (opponent.isFainted())
            battleMessage = addMessage(battleMessage, opponent.getName() + " fainted!");
        }
            else //uses recover
            {
                battleMessage = addMessage(battleMessage,user.getName() + " used " + user.getMoveName(moveNumber) + "!");
                user.recover();
                battleMessage = addMessage(battleMessage, user.getName() + " restored " + (int)(user.getHP()/2) + " hitpoints!");
            }
            
            //lower PP
            user.pp[moveNumber-1]--;
    }
        
    //swaps active Pokemon with another in the party.
    private void switchIn(int partyNumber)
    {
        //if Pokemon trying to switch in is not fainted and not the active Pokemon, switch in
        if( (!(userPokemon[partyNumber-1].isFainted()))&&partyNumber!=1)
        {
            Pokemon temp;
            temp = userPokemon[0];
            userPokemon[0] = userPokemon[partyNumber-1];
            userPokemon[partyNumber-1] = temp;
            battleMessage = addMessage(battleMessage, "Trainer has switched in " + userPokemon[0].getName());
        }
    }
    
    private void throwPokeball(String ballType)
    {
        boolean caught = Lugia.catchMe(ballType);
        String perc = String.format("%.2f",100*Lugia.getCatchRate(ballType));
        battleMessage = addMessage(battleMessage, "You throw a " + ballType);
        if (caught)
        battleMessage = addMessage(battleMessage, Lugia.getName() + " was caught!");
        else
        battleMessage = addMessage(battleMessage, "The ball failed to catch Lugia!");
    }
    
    private void printMessage()
    {
        /*
         * System.out.print('\f');
         * System.out.println(userPokemon[0].getName()+" (You) has "+userPokemon[0].getCurrentHP()+"/"+userPokemon[0].getHP()
         * " HP left.");
         * System.out.println(Lugia.getName9)+" has "+Lugia.getCurrentHP()+"/"+Lugia.getHP()+" HP left.");
         * System.out.println("Game state: " + gameState);
         * System.out.println();
         * 
         * for (int i=0; i< battleMessage.length; i++)
         * System.out.println(battleMessage[i]);
         */
    }
    
    private void checkLose()
    {
        if( (userPokemon[0].getStatus().equals("Fainted"))){
            if ((userPokemon[1].getStatus().equals("Fainted")) && (userPokemon[2].getStatus().equals("Fainted")))
            {
                battleMessage = addMessage(battleMessage, "All of your Pokemon fainted!");
                gameState = state.LOSE;
            }
        }
    }//end checkLose()
    
    public Pokemon[] getParty()
    {
        return userPokemon;
    }
    
    public Pokemon getLugia()
    {
        return Lugia;
    }
    
    public String[] getBattleMessage()
    {
        return battleMessage;
    }
    
    public static String[] addMessage(String[] message, String extra)//attach another string to the end of an array
    {
        message = arrayLengthPlus(message);
        message[message.length-1] = extra;
        return message;
    }
    
    public static String[] arrayLengthPlus(String[] array)//add another element to an array
    {
        String[] array2 = new String[1+array.length];
        for( int i = 0; i<array.length; i++)
        array2[i] = array[i];
        return array2;
    }
    
    public void resetBattleMessage()
    {
        String[] array2 = new String[0];
        battleMessage = array2;
    }
}
    
    
    
                
            
 
        
    
         