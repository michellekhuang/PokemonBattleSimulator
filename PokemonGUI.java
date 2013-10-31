import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.io.*;
import dxn.*;

/**
 * A Pokemon battle simulator in which two Pokemons are given attacks to fight each other. 
 * The first Pokemon to fall to 0 health will faint.
 * 
 * @author Michelle Huang
 * @version 001 10/30/13
 */
public class PokemonGUI extends JPanel implements ActionListener
{
    public JButton move;
    public JButton switchPoke;
    public JButton throwBall;
    public JButton run;
    public JButton backToUserGUI;
    public JButton backButton;
    public JButton goButton;
    
    public JMenuItem restartMenuButton;
    public JRadioButtonMenuItem normalModeRB;
    public JRadioButtonMenuItem devModeRB;
    
    public JLabel theTicker;
    public JLabel battleLabel;
    
    public JFrame frame;
    public JFrame switchFrame;
    
    public JPanel ContentPane;
    public JPanel battlePane;
    public JPanel actionPane;
    public JPanel userPane;
    public JPanel buttonPane;
    public JPanel tickerPane;
    public JPanel tickerButtonPane;
    public JPanel nonBattlePane;
    
    public BattleGraphics battleGFX;
    public AnimatedHealthBar enemyHPBar;
    public AnimatedHealthBar userHPBar;
    public PokemonAnimation enemySprite;
    public PokemonAnimation userSprite;
    public MoveAnimation moveAnimation;
    
    public String action = "";
    public String type = "";
    
    private Pokemon[] userPokemon;
    private battle theBattle;
    
    public enum dMode{NORMAL, DEVELOPER};
    public dMode devMode;
    
    public int tickerCounter;
    public int screenWidth = 320;
    public int screenHeight;
    
    public PokemonGUI()
    {
        //Intro Message
        String introMessage = "You have a Charizard, Chikorita, and Pikachu\n" + 
                              "The goal of the game is to catch Lugia (this simulation makes it 5x easier to catch than in the real game)\n" +
                              "Good luck and have fun!\n";
                              
        JOptionPane.showMessageDialog(null, introMessage);
        
        tickerCounter = 0;
        
        theBattle = new battle();
        userPokemon = theBattle.getParty();
        
        frame = new JFrame ("Pokemans! Gotta catch them all!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        java.awt.Image img;
        try
        {
            img = ImageIO.read(new File("sprites//98.png"));
        }
        catch (IOException e)
        {
            img = null;
        }
        frame.setIconImage(img);
        
        //create the map
        JMenuBar greenMenuBar = new JMenuBar();
        greenMenuBar.setOpaque(true);
        greenMenuBar.setPreferredSize(new Dimension(200, 20));
            //add menu to the MenuBar
        JMenu optionsMenu = new JMenu("Options");
        greenMenuBar.add(optionsMenu);
                //add menu item to the menu
                restartMenuButton = new JMenuItem("New Game");
                restartMenuButton.setActionCommand("restart");
                restartMenuButton.addActionListener(this);
                optionsMenu.add(restartMenuButton);
                //add submenu to the menu
                JMenu settingsMenu = new JMenu("Settings");
                optionsMenu.add(settingsMenu);
                    //add submenu item
                    JMenuItem advancedSettings = new JMenuItem("Advanced");
                    settingsMenu.add(advancedSettings);
                    settingsMenu.addSeparator();
                    //add 2 radio buttons for developer mode
                    ButtonGroup devModeGroup = new ButtonGroup();
                    
                    normalModeRB = new JRadioButtonMenuItem("Normal Mode");
                    normalModeRB.setSelected(true);
                    devMode = dMode.NORMAL;
                    normalModeRB.setActionCommand("normalModeRB");
                    normalModeRB.addActionListener(this);
                    devModeGroup.add(normalModeRB);
                    settingsMenu.add(normalModeRB);
                    
                    devModeRB = new JRadioButtonMenuItem("Developer Mode");
                    devModeRB.setActionCommand("devModeRB");
                    devModeRB.addActionListener(this);
                    devModeGroup.add(devModeRB);
                    settingsMenu.add(devModeRB);
                    
               optionsMenu.addSeparator();
               JMenuItem menuCredits = new JMenuItem("About");
               menuCredits.setActionCommand("menuCredits");
               menuCredits.addActionListener(this);
               optionsMenu.add(menuCredits);
               
               JMenuItem menuQuit = new JMenuItem("Quit? No");
               menuQuit.setActionCommand("menuQuit");
               menuQuit.addActionListener(this);
               optionsMenu.add(menuQuit);
               
        frame.setJMenuBar(greenMenuBar);
        //Menus
        
        ContentPane = new JPanel(new BorderLayout());
        ContentPane.setLayout(new BoxLayout(ContentPane, BoxLayout.Y_AXIS));
        
            //create battlePane
            battlePane = new JPanel(new BorderLayout());
            battlePane.setPreferredSize(new Dimension(screenWidth, 160));
            
                JLayeredPane layeredPane = new JLayeredPane();
                layeredPane.setBorder(BorderFactory.createTitledBorder("Layered Pane"));
                    //create the battle picture (grass, text, & pokemon sprites)
                    battleGFX = new BattleGraphics(theBattle);
                    battleGFX.setBounds(0, 0, screenWidth+20, 160);
                    battleGFX.setOpaque(false);
                    layeredPane.add(battleGFX, new Integer(1));
                    
                    //animate the background
                    BGAnimation background = new BGAnimation();
                    background.setBounds(0, 0, 340, 160);
                    background.animateT();
                    layeredPane.add(background, new Integer(0));
                    
                    //create enemy sprite
                    enemySprite = new PokemonAnimation(theBattle.getLugia().getName(),"front");
                    enemySprite.setBounds(180,-10,170,110);
                    enemySprite.setOpaque(false);
                    enemySprite.setPokemonToolTip(theBattle.getLugia());
                    enemySprite.animateT();
                    layeredPane.add(enemySprite, new Integer(3));
                    
                    //create enemy HP bar
                    enemyHPBar = new AnimatedHealthBar(theBattle.getLugia());
                    enemyHPBar.setBounds(10,10,202,36);
                    enemyHPBar.setOpaque(false);
                    layeredPane.add(enemyHPBar, new Integer(2));
                    
                    //create user HP bar
                    userHPBar = new AnimatedHealthBar(theBattle.getParty()[0]);
                    userHPBar.setBounds(135,115,202,36);
                    userHPBar.setOpaque(false);
                    layeredPane.add(userHPBar, new Integer(2));
                    
                    //lay the move animation layer over everything else
                    moveAnimation = new MoveAnimation();
                    moveAnimation.setBounds(0,0,340,160);
                    moveAnimation.setOpaque(false);
                    
               battlePane.add(layeredPane);
               
               createUserGUI();
               
        ContentPane.setBorder(BorderFactory.createEtchedBorder());
        ContentPane.add(battlePane);
        ContentPane.add(nonBattlePane);
        
        frame.setContentPane(ContentPane);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void createUserGUI()
    {
        nonBattlePane = new JPanel();
        nonBattlePane.setLayout(new BoxLayout(nonBattlePane, BoxLayout.Y_AXIS));
        
        //create userPane
        userPane = new JPanel(new BorderLayout());
        userPane.setLayout(new BoxLayout(userPane, BoxLayout.X_AXIS));
        
            //create actionPane
            actionPane = new JPanel(new BorderLayout());
            JLabel actionLabel = new JLabel();
            actionLabel.setPreferredSize(new Dimension((screenWidth/2), 60));
            actionPane.add(actionLabel);
            actionPane.setBorder(BorderFactory.createEtchedBorder());
            //end create actionPane
            
            //create buttonPane
            buttonPane = new JPanel(new BorderLayout());//uses FlowLayout as default
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
                JPanel MoveSwitch = new JPanel(new BorderLayout());
                MoveSwitch.setLayout(new BoxLayout(MoveSwitch, BoxLayout.X_AXIS));
                    JPanel Move = new JPanel(new BorderLayout());
                    Move.setPreferredSize(new Dimension((screenWidth/4),30));
                    move = new JButton("Fight");
                    move.setActionCommand("move");
                    move.addActionListener(this);
                    Move.add(move);
                    MoveSwitch.add(Move);
                    
                    JPanel Switch = new JPanel(new BorderLayout());
                    Switch.setPreferredSize(new Dimension((screenWidth/4),30));
                    switchPoke = new JButton("Switch");
                    switchPoke.setActionCommand("switch");
                    switchPoke.addActionListener(this);
                    Switch.add(switchPoke);
                    MoveSwitch.add(Switch);
                    //
                MoveSwitch.setAlignmentX(Component.RIGHT_ALIGNMENT);
                buttonPane.add(MoveSwitch);
                    //
                    JPanel ThrowRun = new JPanel(new BorderLayout());
                    ThrowRun.setLayout(new BoxLayout(ThrowRun, BoxLayout.X_AXIS));
                    
                    JPanel Throw = new JPanel(new BorderLayout());
                    Throw.setPreferredSize(new Dimension((screenWidth/4),30));
                    throwBall = new JButton("Items");
                    throwBall.setActionCommand("throw");
                    throwBall.addActionListener(this);
                    Throw.add(throwBall);
                    ThrowRun.add(Throw);
                    
                    JPanel Run = new JPanel(new BorderLayout());
                    Run.setPreferredSize(new Dimension((screenWidth/4),30));
                    run = new JButton("Run");
                    run.setActionCommand("run");
                    run.addActionListener(this);
                    Run.add(run);
                    ThrowRun.add(Run);
                    //
                    
                ThrowRun.setAlignmentX(Component.RIGHT_ALIGNMENT);
                buttonPane.add(ThrowRun);
                
                buttonPane.setBorder(BorderFactory.createEtchedBorder());
                //end Create ButtonPane
        userPane.setBorder(BorderFactory.createEtchedBorder());
        userPane.add(actionPane);
        userPane.add(buttonPane);
        //end userPane
        
        //create TickerPane
        tickerPane = new JPanel(new BorderLayout());
        tickerPane.setLayout(new BoxLayout(tickerPane, BoxLayout.X_AXIS));
        tickerPane.setPreferredSize(new Dimension(screenWidth, 20));
            //create ticker
            JPanel tickerLabelPane = new JPanel(new BorderLayout());
            tickerLabelPane.setPreferredSize(new Dimension(240,20));
            theTicker = new JLabel();
            tickerLabelPane.add(theTicker);
            
            tickerButtonPane = new JPanel();
            tickerButtonPane.setLayout(new BoxLayout(tickerButtonPane, BoxLayout.X_AXIS));
            tickerButtonPane.setPreferredSize(new Dimension(80,20));
            
                //create previous button
                backButton = new JButton("<");
                backButton.setFont(new Font("Default", 1, 8));
                backButton.setActionCommand("back");
                backButton.addActionListener(this);
                backButton.setEnabled(false);
                tickerButtonPane.add(backButton);
                
                //create next button
                goButton = new JButton(">");
                goButton.setFont(new Font("Default", 1, 8));
                goButton.setActionCommand("next");
                goButton.addActionListener(this);
                goButton.setEnabled(false);
                tickerButtonPane.add(goButton);
                
        tickerPane.add(tickerLabelPane);
        tickerPane.add(tickerButtonPane);
        tickerPane.setBorder(BorderFactory.createEtchedBorder());
        
        nonBattlePane.add(userPane);
        nonBattlePane.add(tickerPane);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if ("move".equals(e.getActionCommand()))
        {
            userPane.removeAll();
            
            type = "move";
            action = "";
            
            //if pp is gone, use struggle
            if ((theBattle.getParty()[0].pp[0]+theBattle.getParty()[0].pp[1]+theBattle.getParty()[0].pp[2]+theBattle.getParty()[0].pp[3])==0)
            {
                tickerCounter = 0;
                theBattle.resetBattleMessage();
                
                action = "5"; //action 5 is a struggle
                theBattle.command(type, action);
                
                afterCommand();
            }
            else
            {
                userPane.add(createMovePane());
            }
            
            if (devMode == dMode.DEVELOPER)
            theTicker.setText(" command >> " + type + " >> " + action);
            //frame.pack();
            frame.setVisible(true);
        }
        else if ("switch".equals(e.getActionCommand()))
        {
            clearActionPane();
            //frame.setVisisble(false);
            generateSwitchFrame();
            type = "switch";
            action = "";
            
            if (devMode == dMode.DEVELOPER)
            theTicker.setText(" command >> " + type + " >> " + action);
            //frame.pack();
            
        }
        else if ("throw".equals(e.getActionCommand()))
        {
            actionPane.removeAll();
            actionPane.add(createThrowPane());
            type = "throw";
            action = "";
            
            if (devMode == dMode.DEVELOPER)
            theTicker.setText(" command >> " + type + " >> " + action);
            //frame.pack();
            frame.setVisible(true);
        }
        else if ("run".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            
            type = "run";
            action = "run";
            theBattle.command(type, action);
            
            //frame.pack();
            frame.setVisible(true);
            
            afterCommand();
        }
        else if ("move1".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "1";
            theBattle.command(type, action);
            nonBattlePane.removeAll();
            nonBattlePane = null;
            createUserGUI();
            ContentPane.add(nonBattlePane);
            afterCommand();
        }
        else if ("move2".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "2";
            theBattle.command(type, action);
            nonBattlePane.removeAll();
            nonBattlePane = null;
            createUserGUI();
            ContentPane.add(nonBattlePane);
            afterCommand();
        }
        else if ("move3".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "3";
            theBattle.command(type, action);
            nonBattlePane.removeAll();
            nonBattlePane = null;
            createUserGUI();
            ContentPane.add(nonBattlePane);
            afterCommand();
        }
        else if ("move4".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "4";
            theBattle.command(type, action);
            nonBattlePane.removeAll();
            nonBattlePane = null;
            createUserGUI();
            ContentPane.add(nonBattlePane);
            afterCommand();
        }
        else if ("switch2".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            switchFrame.setVisible(false);
            frame.setVisible(true);
            action = "2";
            if (theBattle.getParty()[0].isFainted()) //if active pokemon fainted, use deathswitch instead
                type = "deathSwitch";
            theBattle.command(type,action);
            afterCommand();
        }
        else if ("switch3".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            switchFrame.setVisible(false);
            frame.setVisible(true);
            action = "3";
            if (theBattle.getParty()[0].isFainted())
                type = "deathSwitch";
            theBattle.command(type, action);
            afterCommand();
        }
        else if ("switchClose".equals(e.getActionCommand()))
        {
            switchFrame.setVisible(false);
            frame.setVisible(true);
            if (devMode == dMode.DEVELOPER)
            theTicker.setText(" command >> " + type + " >> " + action);
        }
        else if ("Pokeball".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "Pokeball";
            theBattle.command(type, action);
            afterCommand();
        }
        else if ("Great Ball".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "Great Ball";
            theBattle.command(type, action);
            afterCommand();
        }
        else if ("Ultra Ball".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "Ultra Ball";
            theBattle.command(type, action);
            afterCommand();
        }
        else if ("Master Ball".equals(e.getActionCommand()))
        {
            tickerCounter = 0;
            theBattle.resetBattleMessage();
            action = "Master Ball";
            theBattle.command(type, action);
            afterCommand();
        }
        else if ("menuQuit".equals(e.getActionCommand()))
        {
            for (int i=0; i<40; i++)
            JOptionPane.showMessageDialog(null, "CATCH THE BIRD!!");
            System.exit(0);
        }
        else if ("menuCredits".equals(e.getActionCommand()))
        {
            JOptionPane.showMessageDialog(null, "Programmed by Michelle Huang\nVersion: 001 (October 30, 2013)\nProject started on: October 21, 2013\n"
                                        +"\nLugia animation sprites ripped by: Deneb87\nCave background ripped by: KLNOTHINCOMIN"
                                        +"\nAll other sprites taken from Pokemon Online and Shoddy Battle");
        }
        else if ("next".equals(e.getActionCommand()))
        {
            tickerCounter++;
            dispMessageInTicker(tickerCounter);
        }
        else if ("back".equals(e.getActionCommand()))
        {
            tickerCounter--;
            dispMessageInTicker(tickerCounter);
        }
        else if ("normalModeRB".equals(e.getActionCommand()))
        {
            JOptionPane.showMessageDialog(null, "Entering normal mode");
            devMode = dMode.NORMAL;
            tickerCounter =0;
        }
        else if ("devModeRB".equals(e.getActionCommand()))
        {
            JOptionPane.showMessageDialog(null, "Entering developer mode");
            devMode = dMode.DEVELOPER;
            backButton.setEnabled(false); //disable the ticker's arrow buttons
            goButton.setEnabled(false);
            theBattle.resetBattleMessage();
        }
        else if ("restart".equals(e.getActionCommand()))
        {
            frame.setVisible(false);
            PokemonGUI newGUI = new PokemonGUI();
        }
        else if ("backToUserGUI".equals(e.getActionCommand()))
        {
            nonBattlePane.removeAll();
            nonBattlePane = null;
            createUserGUI();
            ContentPane.add(nonBattlePane);
            afterCommand();
        }
        else
            ;
        }
        
    private void beforeCommand()
    {
        
    }
    
    private void afterCommand() //checking & displaying that happens after command is given
    {
        //checkLegalButtons();
        //if your Pokemon is fainted, disable all buttons except switch and run
        if (theBattle.getParty()[0].isFainted())
        {
            move.setEnabled(false);
            throwBall.setEnabled(false);
            clearActionPane();
        }
        else
        {
            move.setEnabled(true);
            throwBall.setEnabled(true);
        }
        
        clearActionPane();
        
        battleGFX.setBattle(theBattle);
        battleGFX.repaint();//only for the purpose of repainting user Pokemon's sprite
        
        enemySprite.setPokemonToolTip(theBattle.getLugia());
        
        //HP Bar Animations
        userHPBar.setPokemon(theBattle.getParty()[0]);
        enemyHPBar.setPokemon(theBattle.getLugia());
            ThreadQueue animationQueue = new ThreadQueue();
                if (theBattle.getParty()[0].getSpe() < theBattle.getLugia().getSpe()) //if Lugia is faster
                {
                    animationQueue.addThread(userHPBar);
                    animationQueue.addThread(enemyHPBar);
                }
                else //if you are faster
                {
                    animationQueue.addThread(enemyHPBar);
                    animationQueue.addThread(userHPBar);
                }
                animationQueue.runQueueT();
        ////////////////////////////////////
        
        if (devMode == dMode.DEVELOPER)
        {
            theTicker.setText(" command >> " + type + " >> " + action + " (Action Successful)");
            //display the battle message
            String message = theBattle.getLugia().toString() + "\n\n" + theBattle.getParty()[0].toString() + "\n";
            for ( int i = 0; i<theBattle.battleMessage.length; i++)
                message = message + "\n" + theBattle.battleMessage[i];
            JOptionPane.showMessageDialog(null, message);
        }
        else
        {
            dispMessageInTicker(tickerCounter);
            frame.pack();
        }
        
        //checkGameState();
        if (theBattle.gameState.equals(battle.state.LOSE))
        {
            switchPoke.setEnabled(false); //disable all buttons
            run.setEnabled(false);
            move.setEnabled(false);
            throwBall.setEnabled(false);
            JOptionPane.showMessageDialog(null, "You lost the game");
            System.exit(0);
        }
        
        if (theBattle.gameState.equals(battle.state.WIN))
        {
            switchPoke.setEnabled(false); //disable all buttons
            run.setEnabled(false);
            move.setEnabled(false);
            throwBall.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Congratulations! You caught " + theBattle.getLugia().getName() + "!");
            System.exit(0);
        }
    }
    
    private void dispMessageInTicker(int counter)
    {
        String[] battleMessage = theBattle.getBattleMessage();
        theTicker.setText(battleMessage[counter]);
        
        if (counter == 0)
            backButton.setEnabled(false);
        else
            backButton.setEnabled(true);
            
        if (counter != battleMessage.length-1) //if there are more messages to display, enable goButton
        {
            goButton.setEnabled(true);
        }
        else
        {
            goButton.setEnabled(false);
        }
    }
    
    private void generateSwitchFrame()
    {
        switchFrame = new JFrame("Switch Pokemon");
        JPanel switchPane = new JPanel(new BorderLayout());
        
        switchPane.setLayout(new BoxLayout(switchPane, BoxLayout.Y_AXIS));
            
            JLabel yourParty = new JLabel("Your Party");
            yourParty.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JPanel switchTwo = new JPanel(new BorderLayout());
            //switchTwo.setPreferredSize(new Dimension(80,30));
            JButton switch2 = new JButton(theBattle.getParty()[1].getName() + " (" + 
                theBattle.getParty()[1].getPercentHP(2) + "%)");
            switch2.setActionCommand("switch2");
            switch2.addActionListener(this);
            if (theBattle.getParty()[1].isFainted()) //if Pokemon is fainted, disable button to switch
            switch2.setEnabled(false);
            switchTwo.add(switch2);
            
            JPanel switchThree = new JPanel(new BorderLayout());
            //switchThree.setPreferredSize(new Dimension(80,30));
            JButton switch3 = new JButton(theBattle.getParty()[2].getName() + " (" + 
                theBattle.getParty()[2].getPercentHP(2) + "%)");
            switch3.setActionCommand("switch3");
            switch3.addActionListener(this);
            if (theBattle.getParty()[2].isFainted()) //if Pokemon is fainted, disable button to switch
            switch3.setEnabled(false);
            switchThree.add(switch3);
            
            JPanel switchFour = new JPanel(new BorderLayout());
            switchFour.setPreferredSize(new Dimension(40, 30));
            JButton switch4 = new JButton("Back");
            switch4.setActionCommand("switchClose");
            switch4.addActionListener(this);
            switchFour.add(switch4);
            switchFour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
        switchPane.add(yourParty);
        switchPane.add(switchTwo);
        switchPane.add(switchThree);
        switchPane.add(switchFour);
        switchPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        switchFrame.setContentPane(switchPane);
        
        switchFrame.pack();
        switchFrame.setVisible(true);
    }
    
    private JPanel createMovePane()
    {
        JPanel movePane = new JPanel(new BorderLayout()); //uses FlowLayout as default
        movePane.setLayout(new BoxLayout(movePane, BoxLayout.X_AXIS));
        
            JPanel MoveOneTwo = new JPanel(new BorderLayout());
                MoveOneTwo.setBorder(BorderFactory.createEtchedBorder());
                MoveOneTwo.setLayout(new BoxLayout(MoveOneTwo, BoxLayout.Y_AXIS));
                JPanel MoveOne = new JPanel(new BorderLayout());
                MoveOne.setPreferredSize(new Dimension((screenWidth/2),30));
                JButton move1 = new JButton(userPokemon[0].getMoveName(1));
                move1.setActionCommand("move1");
                move1.addActionListener(this);
                move1.setToolTipText("<html>"+theBattle.getParty()[0].getMoveName(1)
                                    +"<br>Type: " + database.moveType(theBattle.getParty()[0].getMoveName(1))
                                    +"<br>PP: " + theBattle.getParty()[0].pp[0]);
                if (theBattle.getParty()[0].pp[0] == 0)
                    move1.setEnabled(false);
                MoveOne.add(move1);
                MoveOneTwo.add(MoveOne);
                
                JPanel MoveTwo = new JPanel(new BorderLayout());
                MoveTwo.setPreferredSize(new Dimension((screenWidth/2),30));
                JButton move2 = new JButton(userPokemon[0].getMoveName(2));
                move2.setActionCommand("move2");
                move2.addActionListener(this);
                move2.setToolTipText("<html>" + theBattle.getParty()[0].getMoveName(2)
                                    +"<br>Type: " + database.moveType(theBattle.getParty()[0].getMoveName(2))
                                    +"<br>PP: " + theBattle.getParty()[0].pp[1]);
                if (theBattle.getParty()[0].pp[1] == 0)
                    move2.setEnabled(false);
                MoveTwo.add(move2);
                MoveOneTwo.add(MoveTwo);
                //
            MoveOneTwo.setAlignmentX(Component.RIGHT_ALIGNMENT);
            movePane.add(MoveOneTwo);
                //
                JPanel MoveThreeFour = new JPanel(new BorderLayout());
                MoveThreeFour.setBorder(BorderFactory.createEtchedBorder());
                MoveThreeFour.setLayout(new BoxLayout(MoveThreeFour, BoxLayout.Y_AXIS));
                JPanel moveThree = new JPanel(new BorderLayout());
                moveThree.setPreferredSize(new Dimension((screenWidth/2),30));
                JButton move3 = new JButton(userPokemon[0].getMoveName(3));
                move3.setActionCommand("move3");
                move3.addActionListener(this);
                move3.setToolTipText("<html>" + theBattle.getParty()[0].getMoveName(3)
                                    +"<br>Type: " + database.moveType(theBattle.getParty()[0].getMoveName(3))
                                    +"<br>PP: " + theBattle.getParty()[0].pp[2]);
                if (theBattle.getParty()[0].pp[2] == 0)
                    move3.setEnabled(false);
                moveThree.add(move3);
                MoveThreeFour.add(moveThree);
                
                JPanel moveFour = new JPanel(new BorderLayout());
                moveFour.setPreferredSize(new Dimension((screenWidth/2), 30));
                JButton move4 = new JButton(userPokemon[0].getMoveName(4));
                move4.setActionCommand("move4");
                move4.addActionListener(this);
                move4.setToolTipText("<html>" + theBattle.getParty()[0].getMoveName(4)
                                    +"<br>Type: " + database.moveType(theBattle.getParty()[0].getMoveName(4))
                                    +"<br>PP: " + theBattle.getParty()[0].pp[3]);
                if (theBattle.getParty()[0].pp[3] == 0)
                    move4.setEnabled(false);
                moveFour.add(move4);
                MoveThreeFour.add(moveFour);
                //
           MoveThreeFour.setAlignmentX(Component.RIGHT_ALIGNMENT);
           movePane.add(MoveThreeFour);
           //movepane finished
           
           //create back button
           tickerButtonPane.removeAll();
                backToUserGUI = new JButton("Back");
                backToUserGUI.setPreferredSize(new Dimension(90,20));
                backToUserGUI.setActionCommand("backToUserGUI");
                backToUserGUI.addActionListener(this);
                tickerButtonPane.add(backToUserGUI);
           return movePane;
    }
        
    private JPanel createThrowPane()
    {
        JPanel buttonPane = new JPanel(new BorderLayout());//uses FlowLayout as default
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
            
            JPanel MoveSwitch = new JPanel(new BorderLayout());
            MoveSwitch.setLayout(new BoxLayout(MoveSwitch, BoxLayout.X_AXIS));
                JPanel Move = new JPanel(new BorderLayout());
                Move.setPreferredSize(new Dimension((screenWidth/4),30));
                JButton move = new JButton("Pokeball");
                move.setToolTipText(String.format("Pokeball - %.2f%s Success Rate", 100*theBattle.getLugia().getCatchRate("Pokeball"),"%"));
                //move.setFont(new Font("Pokeball", 1, 10));
                move.setActionCommand("Pokeball");
                move.addActionListener(this);
                Move.add(move);
                MoveSwitch.add(Move);
                
                JPanel Switch = new JPanel(new BorderLayout());
                Switch.setPreferredSize(new Dimension((screenWidth/4),30));
                JButton switchPoke = new JButton("Great Ball");
                switchPoke.setToolTipText(String.format("Great Ball - %.2f%s Success Rate", 100*theBattle.getLugia().getCatchRate("Great Ball"),"%"));
                //switchPoke.setFont(new Font("Great Ball", 1, 9));
                switchPoke.setActionCommand("Great Ball");
                switchPoke.addActionListener(this);
                Switch.add(switchPoke);
                MoveSwitch.add(Switch);
                //
            MoveSwitch.setAlignmentX(Component.RIGHT_ALIGNMENT);
            buttonPane.add(MoveSwitch);
                //
                JPanel ThrowRun = new JPanel (new BorderLayout());
                ThrowRun.setLayout(new BoxLayout(ThrowRun, BoxLayout.X_AXIS));
                JPanel Throw = new JPanel(new BorderLayout());
                Throw.setPreferredSize(new Dimension((screenWidth/4),30));
                JButton throwBall = new JButton("Ultra Ball");
                throwBall.setToolTipText(String.format("Ultra Ball - %.2f%s Success Rate", 100*theBattle.getLugia().getCatchRate("Ultra Ball"), "%"));
                //throwBall.setFont(new Font("Ultra ball", 1, 10));
                throwBall.setActionCommand("Ultra Ball");
                throwBall.addActionListener(this);
                Throw.add(throwBall);
                ThrowRun.add(Throw);
                
                JPanel Run = new JPanel (new BorderLayout());
                Run.setPreferredSize(new Dimension((screenWidth/4), 30));
                JButton run = new JButton("Master Ball");
                run.setToolTipText("Master Ball - 100% Success Rate");
                //run.setFont(new Font("Master Ball", 1, 8));
                run.setActionCommand("Master Ball");
                run.addActionListener(this);
                Run.add(run);
                ThrowRun.add(Run);
                //
            ThrowRun.setAlignmentX(Component.RIGHT_ALIGNMENT);
            buttonPane.add(ThrowRun);
            return buttonPane;
    }
    
    private void clearActionPane()
    {
        actionPane.removeAll();
            JLabel runLabel = new JLabel(); // create an empty filler space in the actionPane
            runLabel.setPreferredSize(new Dimension((screenWidth/2), 60));
            actionPane.add(runLabel);
    }    
}
        

