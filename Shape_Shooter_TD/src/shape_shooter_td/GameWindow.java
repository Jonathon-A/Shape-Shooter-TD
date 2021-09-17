package shape_shooter_td;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.Clip;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

final public class GameWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    private String GameMode;
    private final double Difficulty;
    private final String MapCode;
    private final boolean TutorialActive;

    private int Money = 0;
    private int PlayerHealth = 1000;
    public int WaveNumber = 0;
    private int WaveNumberCompensator = 0;

    public GameWindow(final double CustomisedDifficulty, final String CustomisedMapCode, final String CustomisedGameMode, final boolean IsTutorialActive) {
        Difficulty = CustomisedDifficulty;
        MapCode = CustomisedMapCode;
        GameMode = CustomisedGameMode;
        TutorialActive = IsTutorialActive;
    }

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the game panel and closes the previous panel while the transition is opaque
        It them starts the game play loop
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens game customisation panel panel
        GameWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Game customisation panel panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(GameWindowPanel);
        //Starts the game play loop
        GamePlayUpdateLoop();
    }

    final private JLayeredPane GameWindowPanel = new JLayeredPane();

    private void GameWindowSetup() {
        /*
        This method opens the game panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //If tutorial messages should be displayed
        if (TutorialActive) {
            //Setup tutorial area components 
            TutorialAreaSetup();
        }
        //Setup for wave display, money display, and life display
        TextDisplaysSetup();
        //Creates 7 by 7 grid of standard tiles as well as start and end tiles
        GridSetup();
        //Adds path to grid
        GridAddPath(MapCode);
        //Adds necessary listeners to tiles and game panel
        AddListeners();
        //Sets money display text
        setMoneyCountText(Money);
        //Sets wave display text
        setWaveCountText(WaveNumber);
        //Sets life display text
        setHealthCountText(PlayerHealth);
        //Setup for laser charge display bar
        PlayerLaserBarSetup();
        //Setup for laser charge increase button
        PlayerLaserIncreaseButtonSetup();
        //Setup for fast-backward, fast-forward, and pause button
        GameSpeedButtonsSetup();
        //Setup for turret and recycle buttons
        TurretSetup();
        //Setup for enemy cover panels
        AddEnemyCovers();
        //Finds and stores all waypoints along the path in an array
        WayPoints = FindWayPoints(MapCode);
        //Setup for player laser display
        PlayerLaserPanelSetup();
        //Setup for turret lasers display
        TurretLasersPanelSetup();
        //Setup for range console display
        ConsoleSetup();
        //Setup for range indicator display
        RangeIndicatorPanelSetup();
        //Setup for tile grid
        AddGrid();
        //Setup game mode properties
        GameModeProperties(GameMode);
        //Setup game panel properties
        FinalSetup();
        //Starts game sounds loop
        Thread T1 = new Thread(() -> {
            GameSoundLoop();
        });
        T1.start();
    }

    private Enemies PlayerLaserTarget = null;

    private boolean GameOver = false;

    private void GamePlayUpdateLoop() {
        /*
        This method updates everything relating to the progression of the game
        These updates occur every game loop
        The delay between game loops can be altered by changing  the game speed, changing the rate at which the game progresses
         */

        int EnemyAddDelay = 119;
        boolean WaveAdded = true;
        GamePaused = TutorialActive;
        //Loops while the game is not over
        while (!GameOver) {
            try {
                try {
                    do {
                        //Short delay inversely proportional to the game speed
                        TimeUnit.MILLISECONDS.sleep((long) (20 / GameSpeed));
                        //Updates everything active while the game is both paused or not paused
                        GamePausedLoop();
                    } //Loops while the game is paused
                    while (GamePaused);
                } catch (InterruptedException ex) {
                    System.out.println("Game paused loop error: " + ex);
                }
                //Reduces the delay before an enemy is added by one game loop
                EnemyAddDelay++;
                //If delay before an enemy is added has finished
                if (EnemyAddDelay >= (120 / WaveSpeed)) {
                    //If all enemies from the current enemy wave have arrived
                    if (EnemyWaveQueue.isEmpty()) {
                        EnemyAddDelay = 119;
                        //All enemies from the current enemy wave have arrived
                        WaveAdded = true;
                    } //If any enemies from the current enemy wave have not yrt arrived
                    else {
                        //Resets the delay before an enemy is added
                        EnemyAddDelay = 0;
                        //Adds an enemy from the current enemy wave that has not yet arrived
                        AddEnemy();
                    }
                }
                //Updates the next position of all enemies that have arrived
                UpdateEnemiesPosition();
                //Loops through all turrets
                for (int TurretsArrayIndex = 0; TurretsArrayIndex < TurretsArray.size(); TurretsArrayIndex++) {
                    //Updates each turret's target
                    UpdateTurretTarget(TurretsArray.get(TurretsArrayIndex));
                    //Updates each turret's rotation so it aims at its target
                    UpdateTurretRotation(TurretsArray.get(TurretsArrayIndex));
                }
                //Updates the turrets' damage against their enemy targets
                UpdateTurretsDamage();
                //Loops through all enemies
                for (int EnemiesArrayIndex = 0; EnemiesArrayIndex < EnemiesArray.size(); EnemiesArrayIndex++) {
                    //Moves each enemy to its next position
                    UpdateEnemyMove(EnemiesArray.get(EnemiesArrayIndex));
                    //Rotates each enemy
                    UpdateEnemyRotate(EnemiesArray.get(EnemiesArrayIndex));
                    //Checks whether or not the mouse cursor is hovering over each enemy
                    UpdateEnemyHover(EnemiesArray.get(EnemiesArrayIndex));
                }
                //Sets player laser target to nothing
                PlayerLaserTarget = null;
                //Loops through all enemies
                for (int EnemiesArrayIndex = 0; EnemiesArrayIndex < EnemiesArray.size(); EnemiesArrayIndex++) {
                    //Checks if this enemy is the target for the player laser
                    UpdatePlayerLaserTarget(EnemiesArray.get(EnemiesArrayIndex));
                }
                //Updates the player laser's damage against its enemy target
                UpdatePlayerLaserTargetDamage();
                //Loops through all enemies
                for (int EnemiesArrayIndex = 0; EnemiesArrayIndex < EnemiesArray.size(); EnemiesArrayIndex++) {
                    //Updates the shape of each enemy
                    UpdateEnemyShape(EnemiesArray.get(EnemiesArrayIndex));
                }
                //Removes any dead enemies
                RemoveDeadEnemies();
                //Increases / decreases the player laser charge
                UpdatePlayerLaserCharge();
                //Updates the stage of the death animation for all dead enemies
                UpdateDeadEnemiesAnimation();
                //If the players health is below or equal to zero
                if (PlayerHealth <= 0) {
                    //The game is over
                    GameOver = true;
                    //Ends the game
                    GameOverSetup();
                }//If the players health is above zero
                else {
                    ////All enemies from the current enemy wave have arrived and died
                    if (EnemiesArray.isEmpty() && WaveAdded) {
                        //Stops turret laser firing sound effect
                        LasersFiring = false;
                        //Sets player laser target to nothing, stopping it from firing
                        PlayerLaserTarget = null;
                        UpdatePlayerLaserGraphics();
                        //Resets enemies removed counter
                        EnemiesRemoved = 0;
                        //Loops through all turrets
                        for (int TurretsArrayIndex = 0; TurretsArrayIndex < TurretsArray.size(); TurretsArrayIndex++) {
                            //Sets each turret's target to nothing
                            TurretsArray.get(TurretsArrayIndex).setTargetEnemy(null);
                        }
                        //If the first wave completed
                        if (WaveNumber <= 0) {
                            //Console displays "Game Start"
                            SetConsoleLine1("Game Start", -1);
                        }//If a later wave completed
                        else {
                            //Console displays that the wave has been defeated
                            SetConsoleLine1("Wave " + (WaveNumber + WaveNumberCompensator) + " Defeated", -1);
                        }
                        //Increments the wave number
                        IncrementWaveNumber();
                        //Loops for 100 game loops
                        for (int GameLoops = 0; GameLoops < 100; GameLoops++) {
                            try {
                                do {
                                    //Short delay inversely proportional to the game speed
                                    TimeUnit.MILLISECONDS.sleep((long) (20 / GameSpeed));
                                    //Updates everything active while the game is both paused or not paused
                                    GamePausedLoop();
                                } //Loops while the game is paused
                                while (GamePaused);
                            } catch (InterruptedException ex) {
                                System.out.println("Game paused loop error (between waves): " + ex);
                            }
                            //If on final game loop before start of next wave
                            if (GameLoops == 99) {
                                //If next wave includes a boss enemy
                                if ((WaveNumber + WaveNumberCompensator) % 10 == 0) {
                                    //Console displays that the next wave has arrived and is a boss wave
                                    SetConsoleLine1("Boss wave " + (WaveNumber + WaveNumberCompensator) + " Arrives", 2);
                                } else {
                                    //Console displays that the next wave has arrived
                                    SetConsoleLine1("Wave " + (WaveNumber + WaveNumberCompensator) + " Arrives", 2);
                                }
                            }
                            //Increases / decreases the player laser charge
                            UpdatePlayerLaserCharge();
                            //Updates the stage of the death animation for all dead enemies
                            UpdateDeadEnemiesAnimation();
                        }
                        //All enemies from the current enemy wave have not arrived
                        WaveAdded = false;
                        //New wave is created
                        AddEnemyWaveControl();
                    }
                }

            } catch (Exception ex) {
                System.out.println("Game loop error: " + ex);
            }
        }
    }

    private void GamePausedLoop() {
        /*
        This method updates everything relating to the progression of the game that is active while the game is both paused or not paused
        It is called once every game loop and continuously if the game is paused
         */

        //Deducts the remaining amount of time of each console line
        ConsoleLineTimeReduction();
        //Updates the console lines when their display time runs out
        ConsoleLineNullUpdate();
        //Updates the lines displayed on the console
        UpdateConsole();
        //Displays turret ranges if necessary
        UpdateRangeIndicatorGraphics();
    }

    private boolean ForcedStopped = false;

    final public void ForceStopLoop() {
        /*
        This method stops the game
        It is called whenever the game is exited
         */

        //Ends the game
        ForcedStopped = true;
        GameOver = true;
        GameOverSetup();
    }

    private void GameModeProperties(final String CurrentGameMode) {
        /*
        This method sets the properties of the certain variables based on the game mode
         */

        switch (CurrentGameMode) {
            //If bounty game mode
            case ("Bounty"):
                //Sets starting money to 1000;
                Money = 1000;
                setMoneyCountText(Money);
                break;
            //If budget game mode
            case ("Budget"):
                //Sets starting money to 45000;
                Money = 45000;
                setMoneyCountText(Money);
                break;
            //If other
            default:
                //Sets game mode to standard game mode
                GameMode = "Standard";
                break;
        }
    }

    private int WaveSpeed = 1;

    private void IncrementWaveNumber() {
        /*
        This method increases the wave number by one
        It also doubles the enemies speed every 50 waves until wave 100
         */

        //Increments wave number
        WaveNumber++;
        //Displays the current wave
        setWaveCountText(WaveNumber + WaveNumberCompensator);
        //If standard game mode
        if (GameMode.equals("Standard")) {
            //Increases money by 1000
            Money = Money + 1000;
            //Displays the current money count
            setMoneyCountText(Money);
        }
        //If wave number reached 50 or 100
        if ((WaveNumber + WaveNumberCompensator) % 50 == 0 && WaveNumber <= 100) {
            //Increases wave number compensator
            WaveNumberCompensator = WaveNumberCompensator + 30;
            //Decreases wave number to reduce total health of new enemy waves
            WaveNumber = WaveNumber - 30;
            //Doubles the enemies speed
            if (WaveSpeed == 1) {
                WaveSpeed++;
            } else {
                if (WaveSpeed == 2) {
                    WaveSpeed = 4;
                }
            }
        }
    }

    private void GameOverSetup() {
        /*
        This method ends the game
        It is called when the game ends
         */

        //Loops through all enemies and removes them from any array containing them and the game
        for (int EnemiesArrayIndex = 0; EnemiesArrayIndex < EnemiesArray.size(); EnemiesArrayIndex++) {
            if (EnemiesArrayMoveRightArray.contains(EnemiesArray.get(EnemiesArrayIndex))) {
                EnemiesArrayMoveRightArray.remove(EnemiesArray.get(EnemiesArrayIndex));
            }
            if (EnemiesArrayMoveLeftArray.contains(EnemiesArray.get(EnemiesArrayIndex))) {
                EnemiesArrayMoveLeftArray.remove(EnemiesArray.get(EnemiesArrayIndex));
            }
            if (EnemiesArrayMoveUpArray.contains(EnemiesArray.get(EnemiesArrayIndex))) {
                EnemiesArrayMoveUpArray.remove(EnemiesArray.get(EnemiesArrayIndex));
            }
            if (EnemiesArrayMoveDownArray.contains(EnemiesArray.get(EnemiesArrayIndex))) {
                EnemiesArrayMoveDownArray.remove(EnemiesArray.get(EnemiesArrayIndex));
            }
            EnemiesArray.get(EnemiesArrayIndex).getPanel().setVisible(false);
            try {
                GameWindowPanel.remove(EnemiesArray.get(EnemiesArrayIndex).getPanel());
            } catch (Exception ex) {
                System.out.println("Game over, error removing enemy panel: " + ex);
            }
        }
        //Empties array containing all enemies
        EnemiesArray.clear();
        //Loops through all dead enemies and removes them from the game
        for (int DeadEnemiesArrayIndex = 0; DeadEnemiesArrayIndex < DeadEnemiesArray.size(); DeadEnemiesArrayIndex++) {
            try {
                GameWindowPanel.remove(DeadEnemiesArray.get(DeadEnemiesArrayIndex).getPanel());
            } catch (Exception ex) {
                System.out.println("Game over, error removing dead enemy panel: " + ex);
            }
        }
        //Empties array containing all dead enemies
        DeadEnemiesArray.clear();
        //Loops through all turrets and removes their targets
        for (int TurretsArrayIndex = 0; TurretsArrayIndex < TurretsArray.size(); TurretsArrayIndex++) {
            TurretsArray.get(TurretsArrayIndex).setTargetEnemy(null);
        }
        //Sets player laser target to nothing
        PlayerLaserTarget = null;
        PlayerLaserPanel.repaint();
        TurretLasersPanel.repaint();
        //Select no option
        OptionSelected(0);
        //Displays the game details on the console
        ConsoleLineTop.setFont(AssetsFontArray[3]);
        ConsoleLineTopMiddle.setFont(AssetsFontArray[3]);
        ConsoleLineMiddle.setFont(AssetsFontArray[3]);
        ConsoleLineMiddleBottom.setFont(AssetsFontArray[4]);
        ConsoleLineBottom.setFont(AssetsFontArray[4]);
        ConsoleLineTop.setForeground(AssetsColorArray[3]);
        ConsoleLineTopMiddle.setForeground(AssetsColorArray[3]);
        ConsoleLineMiddle.setForeground(AssetsColorArray[3]);
        ConsoleLineMiddleBottom.setForeground(AssetsColorArray[3]);
        ConsoleLineBottom.setForeground(AssetsColorArray[3]);
        final String DifficultyType;
        switch ((int) Difficulty) {
            default:
            case (1):
                DifficultyType = "Easy";
                break;
            case (2):
                DifficultyType = "Medium";
                break;
            case (3):
                DifficultyType = "Hard";
                break;
        }
        String MapCodePart1 = "";
        String MapCodePart2 = MapCode;
        if (MapCode.length() / 2 > 18) {
            MapCodePart1 = " " + MapCode.substring(0, 18);
            MapCodePart2 = MapCode.substring(18, MapCode.length());
        }
        ConsoleLineTop.setText("Game Over, Final score: " + (WaveNumber + WaveNumberCompensator));
        ConsoleLineTopMiddle.setText("Game mode: " + GameMode);
        ConsoleLineMiddle.setText("Difficulty: " + DifficultyType);
        ConsoleLineMiddleBottom.setText("Map Code:" + MapCodePart1);
        ConsoleLineBottom.setText(MapCodePart2);
        //If the game was not stopped preemptively
        if (!ForcedStopped) {
            //If not logged in as guest
            if (!Shape_Shooter_TD.MainClass.getUsername().equals("")) {
                //Username and game details
                final LeaderboardTableEntry NewLeaderboardTableEntry = new LeaderboardTableEntry(Shape_Shooter_TD.MainClass.getUsername(), (WaveNumber + WaveNumberCompensator), GameMode, DifficultyType, MapCode);
                //Adds "AddOrUpdateAccountsEntry" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("AddOrUpdateLeaderboardEntry", null, null, NewLeaderboardTableEntry, "");
                //If all details from the leaderboard details table have been retrieved and copied into an array
                if (Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getLeaderboardEntriesArray() != null) {
                    //Adds / updates leaderboard entry inside said array
                    Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.InsertLeaderboardEntryIntoSortedArray(NewLeaderboardTableEntry);
                }
            }
        }
    }

    private final JTextField ConsoleLineTop = new JTextField();
    private final JTextField ConsoleLineTopMiddle = new JTextField();
    private final JTextField ConsoleLineMiddle = new JTextField();
    private final JTextField ConsoleLineMiddleBottom = new JTextField();
    private final JTextField ConsoleLineBottom = new JTextField();

    private void ConsoleSetup() {
        /*
        This method sets the properties of the console and its lines
        It then adds them to the game panel at the front most position
         */

        //Setup console area properties 
        JPanel ConsoleBackground = new JPanel();
        ConsoleBackground.setBounds(810, 815, 200, 130);
        ConsoleBackground.setBackground(AssetsColorArray[0]);
        ConsoleBackground.setBorder(AssetsBorderArray[0]);
        ConsoleBackground.setLayout(null);
        //Setup top line properties 
        ConsoleLineTop.setBounds(0, 0, 200, 43);
        ConsoleLineTop.setHorizontalAlignment(JTextField.CENTER);
        ConsoleLineTop.setFont(AssetsFontArray[2]);
        ConsoleLineTop.setText("");
        ConsoleLineTop.setEditable(false);
        ConsoleLineTop.setOpaque(false);
        ConsoleLineTop.setBorder(null);
        //Setup top middle line properties 
        ConsoleLineTopMiddle.setBounds(0, 22, 200, 43);
        ConsoleLineTopMiddle.setHorizontalAlignment(JTextField.CENTER);
        ConsoleLineTopMiddle.setFont(AssetsFontArray[2]);
        ConsoleLineTopMiddle.setText("");
        ConsoleLineTopMiddle.setEditable(false);
        ConsoleLineTopMiddle.setOpaque(false);
        ConsoleLineTopMiddle.setBorder(null);
        //Setup middle line properties 
        ConsoleLineMiddle.setBounds(0, 43, 200, 43);
        ConsoleLineMiddle.setHorizontalAlignment(JTextField.CENTER);
        ConsoleLineMiddle.setFont(AssetsFontArray[2]);
        ConsoleLineMiddle.setText("Game Start");
        ConsoleLineMiddle.setEditable(false);
        ConsoleLineMiddle.setOpaque(false);
        ConsoleLineMiddle.setBorder(null);
        //Setup middle bottom line properties 
        ConsoleLineMiddleBottom.setBounds(0, 65, 200, 43);
        ConsoleLineMiddleBottom.setHorizontalAlignment(JTextField.CENTER);
        ConsoleLineMiddleBottom.setFont(AssetsFontArray[2]);
        ConsoleLineMiddleBottom.setText("");
        ConsoleLineMiddleBottom.setEditable(false);
        ConsoleLineMiddleBottom.setOpaque(false);
        ConsoleLineMiddleBottom.setBorder(null);
        //Setup bottom line properties 
        ConsoleLineBottom.setBounds(0, 86, 200, 44);
        ConsoleLineBottom.setHorizontalAlignment(JTextField.CENTER);
        ConsoleLineBottom.setFont(AssetsFontArray[2]);
        ConsoleLineBottom.setText("");
        ConsoleLineBottom.setEditable(false);
        ConsoleLineBottom.setOpaque(false);
        ConsoleLineBottom.setBorder(null);
        //Adds mouse held listener to all lines
        AddMousePressedListener(ConsoleLineTop);
        AddMousePressedListener(ConsoleLineMiddle);
        AddMousePressedListener(ConsoleLineBottom);
        AddMousePressedListener(ConsoleLineTopMiddle);
        AddMousePressedListener(ConsoleLineMiddleBottom);
        //Adds all lines to the console
        ConsoleBackground.add(ConsoleLineTop);
        ConsoleBackground.add(ConsoleLineMiddle);
        ConsoleBackground.add(ConsoleLineBottom);
        ConsoleBackground.add(ConsoleLineTopMiddle);
        ConsoleBackground.add(ConsoleLineMiddleBottom);
        //Adds the console to the game panel at the front most position
        GameWindowPanel.add(ConsoleBackground, new Integer(9));
    }

    private void SetConsoleLine1(final String Line, final int Time) {
        //Sets first console line value and remaining time
        ConsoleLine1 = Line;
        ConsoleLine1Time = Time * 50;
    }

    private void SetConsoleLine2(final String Line, final int Time) {
        //Sets second console line value and remaining time
        ConsoleLine2 = Line;
        ConsoleLine2Time = Time * 50;
    }

    private void SetConsoleLine3(final String Line, final int Time) {
        //Sets third console line value and remaining time
        ConsoleLine3 = Line;
        ConsoleLine3Time = Time * 50;
    }

    private void ConsoleLineTimeReduction() {
        /*
        This method deducts the remaining amount of time of each console line
        It deducts an amount inversely proportional to the game speed, so they are displayed for the amount of time intended
        It is called every time the game loops
         */

        //If first console has time remaining
        if (ConsoleLine1Time > 0) {
            //Reduces first console line remaining time by amount inversely proportional to the game speed
            ConsoleLine1Time = ConsoleLine1Time - 1 / GameSpeed;
        }
        //If second console has time remaining
        if (ConsoleLine2Time > 0) {
            //Reduces second console line remaining time by amount inversely proportional to the game speed
            ConsoleLine2Time = ConsoleLine2Time - 1 / GameSpeed;
        }
        //If third console has time remaining
        if (ConsoleLine3Time > 0) {
            //Reduces third console line remaining time by amount inversely proportional to the game speed
            ConsoleLine3Time = ConsoleLine3Time - 1 / GameSpeed;
        }

    }

    private void ConsoleLineNullUpdate() {
        /*
        This method updates the console lines when their display time runs out
        It is called every time the game loops
         */

        //If first console line has ran out of time or is empty
        if (ConsoleLine1Time == 0 || ConsoleLine1.equals("")) {
            //If the first line does not display "Game Start"
            if (!ConsoleLine1.equals("Game Start")) {
                //Console displays the remaining enemies counter
                SetConsoleLine1("Enemies left: " + (EnemyCount - EnemiesRemoved), -1);
            }
        }
        //If second console line has ran out of time
        if (ConsoleLine2Time == 0) {
            //Empties the second line until it is changed
            SetConsoleLine2("", -1);
        }
        //If third console line has ran out of time
        if (ConsoleLine3Time == 0) {
            //Empties the third line until it is changed
            SetConsoleLine3("", -1);
        }
    }

    private String ConsoleLine1 = "";
    private String ConsoleLine2 = "";
    private String ConsoleLine3 = "";

    private double ConsoleLine1Time = 0;
    private double ConsoleLine2Time = 0;
    private double ConsoleLine3Time = 0;

    private void UpdateConsole() {
        /*
        This method updates the lines displayed on the console
        It is called every time the game loops
         */

        //Creates array of all lines and line times
        final ArrayList<String> AllLines = new ArrayList<>();
        final ArrayList<Double> AllTimes = new ArrayList<>();
        //If the first line is not empty
        if (!ConsoleLine1.equals("")) {
            //Adds the first line to the array of all lines
            AllLines.add(ConsoleLine1);
            //Adds the remaining time of the first line to the array of all line times
            AllTimes.add(ConsoleLine1Time);
        }
        //If the second line is not empty
        if (!ConsoleLine2.equals("")) {
            //Adds the second line to the array of all lines
            AllLines.add(ConsoleLine2);
            //Adds the remaining time of the second line to the array of all line times
            AllTimes.add(ConsoleLine2Time);
        }
        //If the third line is not empty
        if (!ConsoleLine3.equals("")) {
            //Adds the third line to the array of all lines
            AllLines.add(ConsoleLine3);
            //Adds the remaining time of the third line to the array of all line times
            AllTimes.add(ConsoleLine3Time);
        }
        //Finds the number of lines to be displayed
        final int LineNumber = AllLines.size();

        final String NextWaveLine = "Wave " + (WaveNumber + WaveNumberCompensator) + " Arrives";

        switch (LineNumber) {
            //If one line need to be displayed
            case (1):
                //Sets the middle line to the the first line in the array of all lines
                ConsoleLineMiddle.setText(AllLines.get(0));
                //If the first line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(0) <= 10 && AllTimes.get(0) >= 0 && !AllLines.get(0).equals(NextWaveLine)) {
                    //Reduces the opacity of the middle line by an amount proportional to the remaining time of the first line
                    ConsoleLineMiddle.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(0) * (25.5))));
                }//If the first line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the middle line opaque
                    ConsoleLineMiddle.setForeground(new Color(0, 0, 0, 255));
                }
                //Empties the other lines
                ConsoleLineTop.setText("");
                ConsoleLineTopMiddle.setText("");
                ConsoleLineMiddleBottom.setText("");
                ConsoleLineBottom.setText("");
                break;
            //If two lines need to be displayed
            case (2):
                //Sets the top middle line to the the first line in the array of all lines
                ConsoleLineTopMiddle.setText(AllLines.get(0));
                //If the first line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(0) <= 10 && AllTimes.get(0) >= 0 && !AllLines.get(0).equals(NextWaveLine)) {
                    //Reduces the opacity of the top middle line by an amount proportional to the remaining time of the first line
                    ConsoleLineTopMiddle.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(0) * (25.5))));
                }//If the first line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the top middle line opaque
                    ConsoleLineTopMiddle.setForeground(new Color(0, 0, 0, 255));
                }
                //Sets the middle bottom line to the the second line in the array of all lines
                ConsoleLineMiddleBottom.setText(AllLines.get(1));
                //If the second line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(1) <= 10 && AllTimes.get(1) >= 0 && !AllLines.get(1).equals(NextWaveLine)) {
                    //Reduces the opacity of the middle bottom line by an amount proportional to the remaining time of the second line
                    ConsoleLineMiddleBottom.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(1) * (25.5))));
                }//If the second line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the middle bottom line opaque
                    ConsoleLineMiddleBottom.setForeground(new Color(0, 0, 0, 255));
                }
                //Empties the other lines
                ConsoleLineTop.setText("");
                ConsoleLineMiddle.setText("");
                ConsoleLineBottom.setText("");

                break;
            //If three lines need to be displayed
            case (3):
                //Sets the top  line to the the first line in the array of all lines
                ConsoleLineTop.setText(AllLines.get(0));
                //If the first line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(0) <= 10 && AllTimes.get(0) >= 0 && !AllLines.get(0).equals(NextWaveLine)) {
                    //Reduces the opacity of the top line by an amount proportional to the remaining time of the first line
                    ConsoleLineTop.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(0) * (25.5))));
                }//If the first line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the top line opaque
                    ConsoleLineTop.setForeground(new Color(0, 0, 0, 255));
                }
                //Sets the middle line to the the second line in the array of all lines
                ConsoleLineMiddle.setText(AllLines.get(1));
                //If the second line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(1) <= 10 && AllTimes.get(1) >= 0 && !AllLines.get(1).equals(NextWaveLine)) {
                    //Reduces the opacity of the middle line by an amount proportional to the remaining time of the second line
                    ConsoleLineMiddle.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(1) * (25.5))));
                }//If the second line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the middle line opaque
                    ConsoleLineMiddle.setForeground(new Color(0, 0, 0, 255));
                }
                //Sets the bottom line to the the third line in the array of all lines
                ConsoleLineBottom.setText(AllLines.get(2));
                //If the third line has less then or equal to 200 milliseconds before before being removed from the console
                if (AllTimes.get(2) <= 10 && AllTimes.get(2) >= 0 && !AllLines.get(2).equals(NextWaveLine)) {
                    //Reduces the opacity of the bottom line by an amount proportional to the remaining time of the third line
                    ConsoleLineBottom.setForeground(new Color(0, 0, 0, (int) Math.ceil(AllTimes.get(2) * (25.5))));
                }//If the third line has more then 200 milliseconds before before being removed from the console
                else {
                    //Makes the bottom line opaque
                    ConsoleLineBottom.setForeground(new Color(0, 0, 0, 255));
                }
                //Empties the other lines
                ConsoleLineTopMiddle.setText("");
                ConsoleLineMiddleBottom.setText("");

                break;
            //If no lines need to be displayed
            default:
                //Empties the displayed lines
                ConsoleLineTop.setText("");
                ConsoleLineTopMiddle.setText("");
                ConsoleLineMiddle.setText("");
                ConsoleLineMiddleBottom.setText("");
                ConsoleLineBottom.setText("");
                break;

        }

    }

    private boolean GamePaused = false;
    private double GameSpeed = 1;

    private void GameSpeedButtonsSetup() {
        /*
        This method sets the properties of the fast-backward, fast-forward, and pause buttons
        It then adds it to the game panel at the front most position
        The buttons are used to control the game speed
         */

        final JButton FastBackward = new JButton(AssetsButtonIconArray[12]);
        final JButton FastForward = new JButton(AssetsButtonIconArray[9]);
        final JButton Pause = new JButton(AssetsButtonIconArray[15]);

        AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
        AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
        AddButtonHoverColorHighlight(Pause, AssetsButtonIconArray[31]);

        //If game is initially paused
        if (TutorialActive) {
            Pause.setIcon(AssetsButtonIconArray[16]);
            AddButtonHoverColorHighlight(Pause, AssetsButtonIconArray[32]);
        }

        //Setup fast-backward properties
        FastBackward.setBounds(95, 883, 227, 60);
        FastBackward.setBorder(null);
        //Adds mouse press listener to fast forward button
        FastBackward.addActionListener((ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //If the game is not at minimum speed
                if (GameSpeed > 0.25) {
                    //Decreases game speed
                    GameSpeed = GameSpeed / 2;
                    //Console will display the game speed for 2 seconds
                    if (GameSpeed >= 1) {
                        SetConsoleLine2("Game speed: " + (int) GameSpeed + "X", 2);
                    } else {
                        SetConsoleLine2("Game speed: " + GameSpeed + "X", 2);
                    }
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }//If the game is at minimum speed
                else {
                    //Console will display the game speed for 2 seconds
                    SetConsoleLine2("Game speed: 0.25X", 2);
                    //Console will display "Min game speed" for 2 seconds
                    SetConsoleLine3("Min game speed", 2);
                }
                //Updates the fast backward buttons icon
                if (GameSpeed == 0.25) {
                    FastBackward.setIcon(AssetsButtonIconArray[14]);
                    AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                } else {
                    if (GameSpeed == 0.5) {
                        FastBackward.setIcon(AssetsButtonIconArray[13]);
                        AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                    } else {
                        FastBackward.setIcon(AssetsButtonIconArray[12]);
                        AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                        if (GameSpeed == 2) {
                            FastForward.setIcon(AssetsButtonIconArray[10]);
                            AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                        } else {
                            if (GameSpeed == 4) {
                                FastForward.setIcon(AssetsButtonIconArray[11]);
                                AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                            } else {
                                FastForward.setIcon(AssetsButtonIconArray[9]);
                                AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                            }
                        }
                    }
                }
            }
        });
        //Adds mouse held listener to the fast backward button 
        AddMousePressedListener(FastBackward);
        //Adds the fast backward button to the game panel at the front most position
        GameWindowPanel.add(FastBackward, new Integer(9));
        //Setup pause properties
        Pause.setBounds(332, 884, 226, 60);
        Pause.setBorder(null);
        //Adds mouse press listener to pause button
        Pause.addActionListener((ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //If the game is paused
                if (GamePaused) {
                    //Unpauses the game
                    GamePaused = false;
                    Pause.setIcon(AssetsButtonIconArray[15]);
                    AddButtonHoverColorHighlight(Pause, AssetsButtonIconArray[31]);
                    //Console will display "Game resumed" for 2 seconds
                    SetConsoleLine2("Game resumed", 2);
                }//If the game is not paused
                else {
                    //Pauses the game
                    GamePaused = true;
                    Pause.setIcon(AssetsButtonIconArray[16]);
                    AddButtonHoverColorHighlight(Pause, AssetsButtonIconArray[32]);
                    //Sets player laser target to nothing
                    PlayerLaserTarget = null;
                    //Console will display "Game paused" for 2 seconds
                    SetConsoleLine2("Game paused", 2);
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds mouse held listener to the pause button 
        AddMousePressedListener(Pause);
        //Adds the pause button to the game panel at the front most position
        GameWindowPanel.add(Pause, new Integer(9));
        //Setup fast-forward properties
        FastForward.setBounds(568, 884, 227, 60);
        FastForward.setBorder(null);
        //Adds mouse press listener to fast backward button
        FastForward.addActionListener((ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //If the game is not at maximum speed
                if (GameSpeed < 4) {
                    //Increases game speed
                    GameSpeed = GameSpeed * 2;
                    //Console will display the game speed for 2 seconds
                    if (GameSpeed >= 1) {
                        SetConsoleLine2("Game speed: " + (int) GameSpeed + "X", 2);
                    } else {
                        SetConsoleLine2("Game speed: " + GameSpeed + "X", 2);
                    }
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                } //If the game is at minimum speed
                else {
                    //Console will display the game speed for 2 seconds
                    SetConsoleLine2("Game speed: 4X", 2);
                    //Console will display "Max game speed" for 2 seconds
                    SetConsoleLine3("Max game speed", 2);
                }
                //Updates the fast backward buttons icon
                if (GameSpeed == 0.25) {
                    FastBackward.setIcon(AssetsButtonIconArray[14]);
                    AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                } else {
                    if (GameSpeed == 0.5) {
                        FastBackward.setIcon(AssetsButtonIconArray[13]);
                        AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                    } else {
                        FastBackward.setIcon(AssetsButtonIconArray[12]);
                        AddButtonHoverColorHighlight(FastBackward, AssetsButtonIconArray[30]);
                        if (GameSpeed == 2) {
                            FastForward.setIcon(AssetsButtonIconArray[10]);
                            AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                        } else {
                            if (GameSpeed == 4) {
                                FastForward.setIcon(AssetsButtonIconArray[11]);
                                AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                            } else {
                                FastForward.setIcon(AssetsButtonIconArray[9]);
                                AddButtonHoverColorHighlight(FastForward, AssetsButtonIconArray[29]);
                            }
                        }
                    }
                }
            }
        });
        //Adds mouse held listener to the fast forward button 
        AddMousePressedListener(FastForward);
        //Adds the fast forward button to the game panel at the front most position
        GameWindowPanel.add(FastForward, new Integer(9));
    }

    private JPanel PlayerLaserPanel;
    private double CurrentPlayerLaserBarCharge = 0;

    private void PlayerLaserPanelSetup() {
        /*
        This method sets the properties of the player laser display
        It then adds it to the game panel behind enemies but infront of the grid
        The laser is displayed whenever the player fires at an enemy
         */

        PlayerLaserPanel = new JPanel() {
            //Paints vertical laser between top of the grid and target enemy whenever the laser if firing
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;
                //If antialiasing is set to true
                if (Shape_Shooter_TD.MainClass.isANTIALIASING()) {
                    //Adds antialiasing to graphics 
                    gx.addRenderingHints(ANTIALIASING);
                }
                switch (Shape_Shooter_TD.MainClass.getRenderQuality()) {
                    //If render quality is set to speed
                    case (1):
                        //Sets graphics render quality to speed
                        gx.addRenderingHints(SPEED_RENDER);
                        break;
                    default:
                    //Otherwise if render quality is set to default
                    case (2):
                        //Sets graphics render quality to default
                        gx.addRenderingHints(DEFAULT_RENDER);
                        break;
                    //Otherwise if render quality is set to quality
                    case (3):
                        //Sets graphics render quality to quality
                        gx.addRenderingHints(QUALITY_RENDER);
                        break;
                }
                //If the player laser has enough charge and has a target
                if (CurrentPlayerLaserBarCharge >= 3.34 && PlayerLaserTarget != null) {
                    //Finds coordinate of the targeted enemy
                    int XCoordinate = PlayerLaserTarget.getXCoordinate() - 40;
                    int EndYCoordinate = PlayerLaserTarget.getYCoordinate() - 65;
                    //Sets graphics draw properties 
                    gx.setStroke(new BasicStroke(45));
                    gx.setColor(AssetsColorArray[4]);
                    //Draws the laser vertically from the top of the grid to the centre of the enemy
                    gx.drawLine(XCoordinate, 27, XCoordinate, EndYCoordinate);
                    gx.setStroke(new BasicStroke(35));
                    gx.setColor(new Color(244, 73, 79));
                    gx.drawLine(XCoordinate, 23, XCoordinate, EndYCoordinate);
                    //Resumes player laser firing sound effect
                    PlayerLaserFiring = true;
                } else {
                    //Stops player laser firing sound effect
                    PlayerLaserFiring = false;
                }

            }

        };
        //Setup player laser properties
        PlayerLaserPanel.setBounds(90, 94, 710, 710);
        PlayerLaserPanel.setBorder(null);
        PlayerLaserPanel.setBackground(null);
        PlayerLaserPanel.setOpaque(false);
        //Adds the player laser display to the game panel behind enemies but in front of the grid
        GameWindowPanel.add(PlayerLaserPanel, new Integer(4));
    }

    private JPanel TurretLasersPanel;

    private void TurretLasersPanelSetup() {
        /*
        This method sets the properties of the turret lasers display
        It then adds it to the game panel behind enemies and the player laser but in front of the grid
        The lasers are displayed whenever turrets fire at an enemy
         */

        TurretLasersPanel = new JPanel() {
            //Initial drawn laser properties
            int StrokeWidth = 6;
            double LaserStartDistanceFromTurret = 45.4;

            //Paints lasers between firing turrets and their targeted enemy
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;
                //If antialiasing is set to true
                if (Shape_Shooter_TD.MainClass.isANTIALIASING()) {
                    //Adds antialiasing to graphics 
                    gx.addRenderingHints(ANTIALIASING);
                }
                switch (Shape_Shooter_TD.MainClass.getRenderQuality()) {
                    //If render quality is set to speed
                    case (1):
                        //Sets graphics render quality to speed
                        gx.addRenderingHints(SPEED_RENDER);
                        break;
                    default:
                    //Otherwise if render quality is set to default
                    case (2):
                        //Sets graphics render quality to default
                        gx.addRenderingHints(DEFAULT_RENDER);
                        break;
                    //Otherwise if render quality is set to quality
                    case (3):
                        //Sets graphics render quality to quality
                        gx.addRenderingHints(QUALITY_RENDER);
                        break;
                }
                //Loops through all placed turrets
                for (int TurretsArrayIndex = 0; TurretsArrayIndex < TurretsArray.size(); TurretsArrayIndex++) {
                    //If the turret is firing at an enemy
                    if (TurretsArray.get(TurretsArrayIndex).getTargetEnemy() != null) {
                        //Changes drawn laser properties based on turret type
                        switch (TurretsArray.get(TurretsArrayIndex).getTurretType()) {
                            case (1):
                                StrokeWidth = 12;
                                LaserStartDistanceFromTurret = 45.4;
                                break;
                            case (2):
                                StrokeWidth = 8;
                                LaserStartDistanceFromTurret = 47.4;
                                break;
                            case (3):
                                StrokeWidth = 6;
                                LaserStartDistanceFromTurret = 51.4;
                                break;
                            default:
                                StrokeWidth = 6;
                                LaserStartDistanceFromTurret = 45.4;
                                break;
                        }
                        try {
                            //Finds start coordinates of the drawn laser
                            int StartX = (int) Math.round(-sin(TurretsArray.get(TurretsArrayIndex).getRotatedRadians()) * (-LaserStartDistanceFromTurret) + (TurretsArray.get(TurretsArrayIndex).getXCoordinate() + 50));
                            int StartY = (int) Math.round(+cos(TurretsArray.get(TurretsArrayIndex).getRotatedRadians()) * (-LaserStartDistanceFromTurret) + (TurretsArray.get(TurretsArrayIndex).getYCoordinate() + 50));
                            //Finds end coordinates of the drawn laser
                            int EndX = TurretsArray.get(TurretsArrayIndex).getTargetEnemy().getXCoordinate() + 50;
                            int EndY = TurretsArray.get(TurretsArrayIndex).getTargetEnemy().getYCoordinate() + 50;
                            //Draws a laser between start and end points
                            gx.setStroke(new BasicStroke(StrokeWidth));
                            gx.setColor(AssetsColorArray[4]);
                            gx.drawLine(StartX, StartY, EndX, EndY);
                            gx.setStroke(new BasicStroke(StrokeWidth - 4));
                            gx.setColor(new Color(244, 73, 79));
                            gx.drawLine(StartX, StartY, EndX, EndY);
                        } catch (Exception ex) {
                            System.out.println("Drawing turret lasers error:" + ex);
                        }
                    }
                }
            }

        };
        //Setup turret lasers display properties
        TurretLasersPanel.setBounds(0, 0, 1030, 965);
        TurretLasersPanel.setBorder(null);
        TurretLasersPanel.setBackground(null);
        TurretLasersPanel.setOpaque(false);
        //Adds the turret lasers display to the game panel behind enemies but infront of the player laser, the grid, and the turrets
        GameWindowPanel.add(TurretLasersPanel, new Integer(3));
    }

    private ArrayList<String[]> WayPoints;

    private ArrayList<String[]> FindWayPoints(final String MapCodeInput) {
        /*
        This method locates all the waypoints along the path 
        A waypoint is the coordinate of where the path changes direction and the direction the path took
        Waypoints are used when enemies navigate through the path (a valid map code will have at least 2 waypoints)
        The method returns an array of all waypoints found
         */

        //Creates an empty array of waypoints
        final ArrayList<String[]> WayPointsFound = new ArrayList<>();
        //Adds initial direction (right) to the map code
        final String UsedMapCode = "R" + MapCodeInput;
        //Defines a waypoint
        String[] WayPointLocation;
        //Loops through all tiles within map code
        for (int j = 1; j < UsedMapCode.length() - 2; j = j + 3) {
            //Finds coordinate of tile within grid as described by the map code
            final String XPosition = UsedMapCode.substring(j, j + 1);
            final String YPosition = UsedMapCode.substring(j + 1, j + 2);
            //Finds Start direction and end direction of path within the tile
            final String StartMove = UsedMapCode.substring(j - 1, j);
            final String EndMove = UsedMapCode.substring(j + 2, j + 3);
            //Creates a new empty waypoint
            WayPointLocation = new String[3];
            //If the path changes direction
            if (!StartMove.equals(EndMove)) {
                //Stores the coordinate of the waypoint
                WayPointLocation[0] = String.valueOf((100 * Integer.valueOf(XPosition)) - 5);
                WayPointLocation[1] = String.valueOf((100 * Integer.valueOf(YPosition)) - 1);
                //Stores the direction the path took to the waypoint
                WayPointLocation[2] = EndMove;
                //Adds the waypoint to the array of all waypoints
                WayPointsFound.add(WayPointLocation);
            }

        }
        //Creates end waypoint at the end of the path
        WayPointLocation = new String[3];
        WayPointLocation[0] = "" + 775;
        WayPointLocation[1] = "" + 599;
        WayPointLocation[2] = "end";
        //Adds the waypoint to the end of the array of all waypoints
        WayPointsFound.add(WayPointLocation);
        //Returns the array of all waypoints found
        return WayPointsFound;
    }

    final private ArrayList<Tiles> TilesArray = new ArrayList<>(49);
    private boolean MousePressed = false;
    private JButton SelectedTileButton = null;

    private void AddListeners() {
        /*
        This method adds necessary listeners to tiles
         */

        //Loops through all tiles
        for (int TileArrayPosition = 0; TileArrayPosition < TilesArray.size(); TileArrayPosition++) {
            //Gets tile button
            final JButton BufferButton = TilesArray.get(TileArrayPosition).getButton();
            //Gets tile position
            final String Position = TilesArray.get(TileArrayPosition).getPosition();
            //Gets tile at tile array position
            final Tiles CurrentTile = TilesArray.get(TileArrayPosition);

            //If tile is not part of the path or start or end tile
            if (!TilesArray.get(TileArrayPosition).getIsPath()
                    && !Position.equals("12")
                    && !Position.equals("76")) {

                //Adds mouse press listener to tile button
                BufferButton.addActionListener((ActionEvent e) -> {

                    //Checks if turret placement is valid, upgrades tile turret if valid, and removes and refunds tile turret if turret sell is valid
                    boolean TileTurretValid = TileTurretPlacementOrUpgradeORSellValid(CurrentTile);
                    //if turret is valid and a turret is selected
                    if (CurrentTurretTypeSelected != 0 && TileTurretValid) {
                        //Adds or upgrades turret
                        AddOrUpgradeTurret(CurrentTile);
                    } else {
                        //If turret is valid
                        if (TileTurretValid) {
                            //Removes turret
                            RemoveTurret(CurrentTile);
                        }
                    }
                    //Selects no option
                    OptionSelected(0);

                });
                //Adds mouse rollover listener to tile button
                BufferButton.getModel().addChangeListener((final ChangeEvent e) -> {
                    final ButtonModel model = (ButtonModel) e.getSource();
                    //If tile is hovered over
                    if (model.isRollover()) {
                        //Highlights the tile
                        BufferButton.setBorder(AssetsBorderArray[5]);
                        //Sets selected tile as this tile
                        SelectedTileButton = BufferButton;
                    } //If tile is not hovered over
                    else {
                        //Unhighlights the tile
                        BufferButton.setBorder(null);
                        //Sets selected tile as nothing 
                        SelectedTileButton = null;
                    }
                });

            }
            //Adds mouse held listener to tile button 
            AddMousePressedListener(BufferButton);
        }

    }

    private JPanel RangeIndicatorPanel;

    private void RangeIndicatorPanelSetup() {
        /*
        This method sets the properties of the turret range indicator display
        It then adds it to the game panel behind enemies but infront of the grid and the lasers
        The range indicator is displayed whenever the player is placing a turret
         */

        RangeIndicatorPanel = new JPanel() {
            //Paints a circle around the tile that the players cursor hovers over whenever they have a turret selected for placement
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;
                //If antialiasing is set to true
                if (Shape_Shooter_TD.MainClass.isANTIALIASING()) {
                    //Adds antialiasing to graphics 
                    gx.addRenderingHints(ANTIALIASING);
                }
                switch (Shape_Shooter_TD.MainClass.getRenderQuality()) {
                    //If render quality is set to speed
                    case (1):
                        //Sets graphics render quality to speed
                        gx.addRenderingHints(SPEED_RENDER);
                        break;
                    default:
                    //Otherwise if render quality is set to default
                    case (2):
                        //Sets graphics render quality to default
                        gx.addRenderingHints(DEFAULT_RENDER);
                        break;
                    //Otherwise if render quality is set to quality
                    case (3):
                        //Sets graphics render quality to quality
                        gx.addRenderingHints(QUALITY_RENDER);
                        break;
                }
                //IF the user has a turret selected for placement and their cursor is hovering over a tile
                if (CurrentTurretTypeSelected != 0 && SelectedTileButton != null) { //OrbitalLaserTarget != null && 

                    int Range;
                    //Finds range of selected turret
                    switch (CurrentTurretTypeSelected) {
                        case (1):
                            Range = 300;
                            break;
                        case (2):
                            Range = 700;
                            break;
                        case (3):
                            Range = 1100;
                            break;
                        default:
                            Range = 0;
                            break;

                    }
                    //Finds coordinate of the tile
                    int XCoordinate = SelectedTileButton.getX() - 40 - (Range / 2);
                    int YCoordinate = SelectedTileButton.getY() - 45 - (Range / 2);
                    //Draws a circle with radius equal to the selected turrets range
                    gx.setStroke(new BasicStroke(6));
                    gx.setColor(AssetsColorArray[3]);
                    gx.drawOval(XCoordinate, YCoordinate, Range, Range);
                    gx.setStroke(new BasicStroke(0));
                    AlphaComposite AC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5);
                    gx.setComposite(AC);
                    gx.fillOval(XCoordinate, YCoordinate, Range, Range);

                }
            }

        };
        //Setup turret range indicator display properties     
        RangeIndicatorPanel.setBounds(90, 94, 710, 710);
        RangeIndicatorPanel.setBorder(null);
        RangeIndicatorPanel.setBackground(null);
        RangeIndicatorPanel.setOpaque(false);
        //Adds the turret range indicator display to the game panel behind enemies and the player laser but infront of the grid
        GameWindowPanel.add(RangeIndicatorPanel, new Integer(5));

    }

    private int CurrentTurretTypeSelected = 0;
    private boolean TurretSell = false;

    private boolean TileTurretPlacementOrUpgradeORSellValid(final Tiles CurrentTile) {
        /*
        This validates turret placement / upgrade / sell
        Makes appropriate changes to tiles
         */

        //Turret placement/upgrade/sell is valid
        boolean TurretValid = true;

        //If turret sell is true and current tile has a turret
        if (TurretSell && CurrentTile.getTurretType() != 0) {
            //Set tile turret as nothing
            CurrentTile.setTurretType(0);
            //Refund money
            Money = Money + (1000 * CurrentTile.getTurretLevel());
            //Set tile turret level to 0
            CurrentTile.setTurretLevel(0);
            //Set tile icon to standard
            CurrentTile.getButton().setIcon(AssetsTileIconArray[2]);
        } else {
            boolean DoNext = false;
            boolean ValidUpgrade = false;

            //If turret type selected is same as tile turret
            if (CurrentTurretTypeSelected == CurrentTile.getTurretType()) {
                DoNext = true;
                //Upgrade equals valid
                ValidUpgrade = true;
            }
            //If tile has no turret
            if (CurrentTile.getTurretType() == 0) {
                DoNext = true;
            } else {
                //If upgrade not valid and a turret type is selected
                if (!ValidUpgrade && CurrentTurretTypeSelected != 0) {

                    //Console will display "Invalid upgrade" for 2 seconds
                    SetConsoleLine3("Invalid upgrade", 2);
                }
            }

            if (DoNext) {
                //If tile turret not max upgrade level
                if (CurrentTile.getTurretLevel() <= 2) {
                    //If turret type is selected
                    if (CurrentTurretTypeSelected != 0) {
                        //If enough money for placement or upgrade
                        if (Money >= 1000) {
                            //Set tile turret type to selected turret type
                            CurrentTile.setTurretType(CurrentTurretTypeSelected);
                            //Increases tile turret level by one
                            CurrentTile.setTurretLevel(CurrentTile.getTurretLevel() + 1);

                            //Change tile icon based on tile turret level
                            switch (CurrentTile.getTurretLevel()) {
                                case (1):
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[13]);
                                    break;
                                case (2):
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[14]);
                                    break;
                                case (3):
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[15]);
                                    break;
                                default:
                                    break;
                            }
                            //Deduct cost from money
                            Money = Money - 1000;
                        } else {
                            //Console will display "Unaffordable" for 2 seconds
                            SetConsoleLine3("Unaffordable", 2);
                            //Turret placement/upgrade/sell is invalid
                            TurretValid = false;
                        }
                    } else {
                        if (TurretSell) {
                            //Console will display "Invalid recycle" for 2 seconds
                            SetConsoleLine3("Invalid recycle", 2);
                            //Turret placement/upgrade/sell is invalid
                            TurretValid = false;
                        }
                    }
                } else {
                    //Console will display "Invalid upgrade" for 2 seconds
                    SetConsoleLine3("Invalid upgrade", 2);
                    //Turret placement/upgrade/sell is invalid
                    TurretValid = false;
                }
            } else {
                //Turret placement/upgrade/sell is invalid
                TurretValid = false;
            }
        }
        //Turret sell is false
        TurretSell = false;
        //Update money display
        setMoneyCountText(Money);
        //Returns whether or not turret was valid
        return TurretValid;
    }

    private final ArrayList<Turrets> TurretsArray = new ArrayList<>();

    private void AddOrUpgradeTurret(final Tiles CurrentTile) {
        /*
        This method makes changes to turret that are being upgraded
        Also creates new turrets when they are placed
         */

        //Gets coordinates of tile button
        final int XCoordinate = CurrentTile.getButton().getX();
        final int YCoordinate = CurrentTile.getButton().getY();

        //If adding a turret
        if (CurrentTile.getTurretLevel() == 1) {
            //Creates new turret at same position
            final Turrets BufferTurret = new Turrets(XCoordinate, YCoordinate, CurrentTile.getTurretLevel(), CurrentTile.getTurretType());
            //Adds turret to array of all turrets
            TurretsArray.add(BufferTurret);
            //Adds turret in front of tiles but behind lasers
            GameWindowPanel.add(BufferTurret.getPanel(), new Integer(2));
        } //If upgrading a turret
        else {
            //Loop through all turrets
            for (int TurretArrayPosition = 0; TurretArrayPosition < TurretsArray.size(); TurretArrayPosition++) {
                //If turret coordinates equal to tile button coordinates
                if ((TurretsArray.get(TurretArrayPosition).getXCoordinate() == CurrentTile.getButton().getX())
                        && (TurretsArray.get(TurretArrayPosition).getYCoordinate() == CurrentTile.getButton().getY())) {
                    //Sets turret level to same as tile turret level
                    TurretsArray.get(TurretArrayPosition).setTurretLevel(CurrentTile.getTurretLevel());
                    //Ends loop
                    TurretArrayPosition = TurretsArray.size() + 1;
                }
            }
        }
    }

    private void RemoveTurret(final Tiles CurrentTile) {
        /*
        This method removes a specified turret
         */

        //Loop through all turrets
        for (int TurretArrayPosition = 0; TurretArrayPosition < TurretsArray.size(); TurretArrayPosition++) {
            //If turret coordinates equal to tile button coordinates
            if ((TurretsArray.get(TurretArrayPosition).getXCoordinate() == CurrentTile.getButton().getX())
                    && (TurretsArray.get(TurretArrayPosition).getYCoordinate() == CurrentTile.getButton().getY())) {
                //Makes turret invisible
                TurretsArray.get(TurretArrayPosition).getPanel().setVisible(false);
                //Removes turret panel from game window
                try {
                    GameWindowPanel.remove(TurretsArray.get(TurretArrayPosition).getPanel());
                } catch (Exception ex) {
                    System.out.println("Removing turret error: " + ex);
                }
                //Removes turret from array of all turrets
                TurretsArray.remove(TurretsArray.get(TurretArrayPosition));
                //Ends loop
                TurretArrayPosition = TurretsArray.size() + 1;
            }
        }
    }

    private void GridAddPath(final String MapCodeInput) {
        /*
        This method adds the path specified by the map code to the grid
        It changes specified tile icons and sets them as path
         */

        //Adds starting direction to map code
        final String AddedMapCode = "R" + MapCodeInput;
        //Loop through all tiles
        for (int TileArrayPosition = 0; TileArrayPosition < TilesArray.size(); TileArrayPosition++) {
            //Get tile position
            String Position = TilesArray.get(TileArrayPosition).getPosition();

            //Loop through map code Character positions
            for (int CharacterPositionWithinMapCode = 1; CharacterPositionWithinMapCode < AddedMapCode.length() - 2; CharacterPositionWithinMapCode = CharacterPositionWithinMapCode + 3) {

                //If map code position equals tile position
                if (AddedMapCode.substring(CharacterPositionWithinMapCode, CharacterPositionWithinMapCode + 2).equals(Position)) {
                    //get direction before and after position
                    final String StartMove = AddedMapCode.substring(CharacterPositionWithinMapCode - 1, CharacterPositionWithinMapCode);
                    final String EndMove = AddedMapCode.substring(CharacterPositionWithinMapCode + 2, CharacterPositionWithinMapCode + 3);
                    final String FullMove = StartMove + EndMove;
                    //Get tile button
                    final JButton BufferButton = TilesArray.get(TileArrayPosition).getButton();
                    //Changes button icon based on direction
                    switch (FullMove) {
                        case ("RR"):
                        case ("LL"):
                            BufferButton.setIcon(AssetsTileIconArray[3]);
                            break;

                        case ("DD"):
                        case ("UU"):
                            BufferButton.setIcon(AssetsTileIconArray[4]);
                            break;

                        case ("RD"):

                            BufferButton.setIcon(AssetsTileIconArray[5]);
                            break;

                        case ("RU"):
                        case ("DL"):

                            BufferButton.setIcon(AssetsTileIconArray[6]);
                            break;

                        case ("UR"):
                        case ("LD"):

                            BufferButton.setIcon(AssetsTileIconArray[7]);
                            break;

                        case ("DR"):

                            BufferButton.setIcon(AssetsTileIconArray[8]);
                            break;

                        default:
                            BufferButton.setIcon(AssetsTileIconArray[2]);
                            break;

                    }
                    //Sets tile as path
                    TilesArray.get(TileArrayPosition).setIsPath(true);
                }
            }
        }
    }

    private void GridSetup() {
        /*
        This method creates a 7 by 7 grid of tiles
        Including the start and end tiles and all standard tiles
         */

        //Starting y coordinate (at top left of grid)
        int YCoordinate = 99;
        //Loop through each row
        for (int Row = 1; Row < 8; Row++) {
            //Starting x coordinate (at top left of grid)
            int XCoordinate = 95;

            //Loop through each column
            for (int Column = 1; Column < 8; Column++) {
                //Creates new button for tile
                final JButton BufferButton = new JButton();
                //Sets position and size of button and thus tile
                BufferButton.setBounds(XCoordinate, YCoordinate, 100, 100);
                BufferButton.setBorder(null);
                //Position = the position of the tile in the 7 by 7 grid
                String Position = (Column + "" + Row);
                //Sets tile at column 1 and row 2 to start tile
                if (Position.equals("12")) {
                    BufferButton.setIcon(AssetsTileIconArray[1]);
                    //removes mouse listener from start tile because it will always be a part of the path
                    BufferButton.removeMouseListener(BufferButton.getMouseListeners()[0]);
                } else {
                    //Sets tile at column 7 and row 6 to end tile
                    if (Position.equals("76")) {
                        BufferButton.setIcon(AssetsTileIconArray[0]);
                        //removes mouse listener from end tile because it will always be a part of the path
                        BufferButton.removeMouseListener(BufferButton.getMouseListeners()[0]);
                    } else {
                        //Sets non end or start tiles to standard tile
                        BufferButton.setIcon(AssetsTileIconArray[2]);
                    }
                }
                //Next column starts 100px right for previous
                XCoordinate = XCoordinate + 100;

                //Creates new tile object
                Tiles BufferTile = new Tiles(BufferButton, Position);
                //Adds tile to array of all tiles
                TilesArray.add(BufferTile);

            }
            //Next row starts 100px down for previous
            YCoordinate = YCoordinate + 100;
        }
    }

    //Display that shows current wave count
    final private JTextField WaveCount = new JTextField();
    //Display that shows current money count
    final private JTextField MoneyCount = new JTextField();
    //Display that shows current life count
    final private JTextField HealthCount = new JTextField();

    private void TextDisplaysSetup() {
        /*
        This method sets the properties of the wave display, money display, and life display
        It then adds them to the game panel at the front most position
         */

        //Setup wave display properties
        WaveCount.setEditable(false);
        WaveCount.setBounds(95, 814, 227, 60);
        WaveCount.setHorizontalAlignment(JTextField.CENTER);
        WaveCount.setFont(AssetsFontArray[1]);
        WaveCount.setBackground(AssetsColorArray[0]);
        WaveCount.setBorder(AssetsBorderArray[0]);
        //Adds wave display at front most position
        GameWindowPanel.add(WaveCount, new Integer(9));

        //Setup money display properties
        MoneyCount.setEditable(false);
        MoneyCount.setBounds(332, 814, 226, 60);
        MoneyCount.setHorizontalAlignment(JTextField.CENTER);
        MoneyCount.setFont(AssetsFontArray[1]);
        MoneyCount.setBackground(AssetsColorArray[0]);
        MoneyCount.setBorder(AssetsBorderArray[0]);
        //Adds money display at front most position
        GameWindowPanel.add(MoneyCount, new Integer(9));

        //Setup life display properties
        HealthCount.setEditable(false);
        HealthCount.setBounds(568, 814, 227, 60);
        HealthCount.setHorizontalAlignment(JTextField.CENTER);
        HealthCount.setFont(AssetsFontArray[1]);
        HealthCount.setBackground(AssetsColorArray[0]);
        HealthCount.setBorder(AssetsBorderArray[0]);
        //Adds life display at front most position
        GameWindowPanel.add(HealthCount, new Integer(9));

        //Adds listener for mouse press to wave display, money display, and life display
        AddMousePressedListener(WaveCount);
        AddMousePressedListener(MoneyCount);
        AddMousePressedListener(HealthCount);

    }

    private final JButton Turret1 = new JButton(AssetsButtonIconArray[0]);
    private final JButton Turret2 = new JButton(AssetsButtonIconArray[2]);
    private final JButton Turret3 = new JButton(AssetsButtonIconArray[4]);
    private final JButton SellButton = new JButton(AssetsButtonIconArray[6]);

    private void TurretSetup() {
        /*
        This method sets the properties of the turret and turret recycle buttons
        It then adds them to the game panel at the front most position
         */

        //Setup turret 1 button properties
        AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
        Turret1.setBounds(810, 99, 200, 227);
        Turret1.setBorder(null);
        //Adds mouse press listener to turret 1 button
        Turret1.addActionListener((final ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Selects / deselects turret 1 option
                OptionSelected(1);
            }

        });
        //Adds mouse held listener to turret 1 button 
        AddMousePressedListener(Turret1);
        //Adds turret 1 button at front most position
        GameWindowPanel.add(Turret1, new Integer(9));

        //Setup turret 2 button properties
        AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
        Turret2.setBounds(810, 335, 200, 227);
        Turret2.setBorder(null);
        //Adds mouse press listener to turret 2 button
        Turret2.addActionListener((final ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Selects / deselects turret 2 option
                OptionSelected(2);
            }

        });
        //Adds mouse held listener to turret 2 button 
        AddMousePressedListener(Turret2);
        //Adds turret 2 button at front most position
        GameWindowPanel.add(Turret2, new Integer(9));

        //Setup turret 3 button properties
        AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
        Turret3.setBounds(810, 572, 200, 227);
        Turret3.setBorder(null);
        //Adds mouse press listener to turret 3 button
        Turret3.addActionListener((final ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Selects / deselects turret 3 option
                OptionSelected(3);
            }

        });
        //Adds mouse held listener to turret 3 button 
        AddMousePressedListener(Turret3);
        //Adds turret 3 button at front most position
        GameWindowPanel.add(Turret3, new Integer(9));

        //Setup turret recycle button properties
        AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
        SellButton.setBounds(20, 884, 60, 60);
        SellButton.setBorder(null);
        //Adds mouse press listener to turret recycle button
        SellButton.addActionListener((final ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Selects / deselects turret recycle option
                OptionSelected(4);
            }

        });
        //Adds mouse held listener to turret recycle button 
        AddMousePressedListener(SellButton);
        //Adds turret recycle at front most position
        GameWindowPanel.add(SellButton, new Integer(9));
    }

    private void OptionSelected(final int OptionSelected) {
        /*
        This method highlights selected buttons if necessary
        Changes relevant variables based on highlighted button
         */

        switch (OptionSelected) {
            //If turret 1 option is selected
            case (1):
                //If turret 1 was not previously selected option
                if (CurrentTurretTypeSelected != 1) {
                    //Sets selected option to turret 3
                    CurrentTurretTypeSelected = 1;
                    //Highlights selected button
                    Turret1.setIcon(AssetsButtonIconArray[1]);
                    AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                }//If turret 2 was not previously selected option
                else {
                    //Sets selected option to nothing
                    CurrentTurretTypeSelected = 0;
                    //Unhighlights previously selected button
                    Turret1.setIcon(AssetsButtonIconArray[0]);
                    AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                }
                TurretSell = false;

                //Unhighlights other buttons
                Turret2.setIcon(AssetsButtonIconArray[2]);
                AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                Turret3.setIcon(AssetsButtonIconArray[4]);
                AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                SellButton.setIcon(AssetsButtonIconArray[6]);
                AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                break;
            //If turret 2 option is selected
            case (2):
                //If turret 2 was not previously selected option
                if (CurrentTurretTypeSelected != 2) {
                    //Sets selected option to turret 3
                    CurrentTurretTypeSelected = 2;
                    //Highlights selected button
                    Turret2.setIcon(AssetsButtonIconArray[3]);
                    AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                }//If turret 2 was not previously selected option
                else {
                    //Sets selected option to nothing
                    CurrentTurretTypeSelected = 0;
                    //Unhighlights previously selected button
                    Turret2.setIcon(AssetsButtonIconArray[2]);
                    AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                }
                TurretSell = false;

                //Unhighlights other buttons
                Turret1.setIcon(AssetsButtonIconArray[0]);
                AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                Turret3.setIcon(AssetsButtonIconArray[4]);
                AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                SellButton.setIcon(AssetsButtonIconArray[6]);
                AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                break;
            //If turret 3 option is selected
            case (3):
                //If turret 3 was not previously selected option
                if (CurrentTurretTypeSelected != 3) {
                    //Sets selected option to turret 3
                    CurrentTurretTypeSelected = 3;
                    //Highlights selected button
                    Turret3.setIcon(AssetsButtonIconArray[5]);
                    AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                }//If turret 3 was not previously selected option
                else {
                    //Sets selected option to nothing
                    CurrentTurretTypeSelected = 0;
                    //Unhighlights previously selected button
                    Turret3.setIcon(AssetsButtonIconArray[4]);
                    AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                }
                TurretSell = false;

                //Unhighlights other buttons
                Turret1.setIcon(AssetsButtonIconArray[0]);
                AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                Turret2.setIcon(AssetsButtonIconArray[2]);
                AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                SellButton.setIcon(AssetsButtonIconArray[6]);
                AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                break;
            //If turret recycle option is selected
            case (4):
                //If turret recycle was not previously selected option
                if (!TurretSell) {
                    //Sets selected option to turret recycle
                    TurretSell = true;
                    //Highlights selected button
                    SellButton.setIcon(AssetsButtonIconArray[7]);
                    AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                } //If turret recycle was not previously selected option
                else {
                    //Sets selected option to nothing
                    TurretSell = false;
                    //Unhighlights previously selected button
                    SellButton.setIcon(AssetsButtonIconArray[6]);
                    AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                }
                CurrentTurretTypeSelected = 0;

                //Unhighlights other buttons
                Turret1.setIcon(AssetsButtonIconArray[0]);
                AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                Turret2.setIcon(AssetsButtonIconArray[2]);
                AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                Turret3.setIcon(AssetsButtonIconArray[4]);
                AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                break;
            //If a tile is selected
            default:
                //Sets selected option to nothing
                CurrentTurretTypeSelected = 0;
                TurretSell = false;
                //Unhighlights all buttons
                Turret1.setIcon(AssetsButtonIconArray[0]);
                AddButtonHoverColorHighlight(Turret1, AssetsButtonIconArray[33]);
                Turret2.setIcon(AssetsButtonIconArray[2]);
                AddButtonHoverColorHighlight(Turret2, AssetsButtonIconArray[34]);
                Turret3.setIcon(AssetsButtonIconArray[4]);
                AddButtonHoverColorHighlight(Turret3, AssetsButtonIconArray[35]);
                SellButton.setIcon(AssetsButtonIconArray[6]);
                AddButtonHoverColorHighlight(SellButton, AssetsButtonIconArray[36]);
                break;
        }
    }

    private void AddGrid() {
        /*
        This method sets the properties of the grid area
        It then adds it to the game panel at the back most position
        The tiles are added to the grid and listeners are removed from path tiles
         */

        //Setup grid area properties     
        final JPanel GridArea = new JPanel();
        GridArea.setBounds(90, 94, 710, 710);
        GridArea.setBorder(AssetsBorderArray[0]);
        GridArea.setBackground(null);
        //Adds the  grid area to the game panel at the back most position
        GameWindowPanel.add(GridArea, new Integer(0));
        //Adds all tiles to the grid
        for (int TilesArrayIndex = 0; TilesArrayIndex < TilesArray.size(); TilesArrayIndex++) {
            //If the tile is part of the path
            if (TilesArray.get(TilesArrayIndex).getIsPath()) {
                //Removes listener from tile
                TilesArray.get(TilesArrayIndex).getButton().removeMouseListener(TilesArray.get(TilesArrayIndex).getButton().getMouseListeners()[0]);
            }
            //Adds the tile to the game panel infront of the grid area but behind everything else
            GameWindowPanel.add(TilesArray.get(TilesArrayIndex).getButton(), new Integer(1));
        }
    }

    private JPanel PlayerLaserBarPanel;

    private void PlayerLaserBarSetup() {
        /*
        This method sets the properties of the Laser charge bar
        The bar displays the current amount of laser charge when painted
        It then adds it to the game panel at the front most position
         */

        PlayerLaserBarPanel = new JPanel() {
            //Paints current amount of laser charge within bar
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;
                //If antialiasing is set to true
                if (Shape_Shooter_TD.MainClass.isANTIALIASING()) {
                    //Adds antialiasing to graphics 
                    gx.addRenderingHints(ANTIALIASING);
                }
                switch (Shape_Shooter_TD.MainClass.getRenderQuality()) {
                    //If render quality is set to speed
                    case (1):
                        //Sets graphics render quality to speed
                        gx.addRenderingHints(SPEED_RENDER);
                        break;
                    default:
                    //Otherwise if render quality is set to default
                    case (2):
                        //Sets graphics render quality to default
                        gx.addRenderingHints(DEFAULT_RENDER);
                        break;
                    //Otherwise if render quality is set to quality
                    case (3):
                        //Sets graphics render quality to quality
                        gx.addRenderingHints(QUALITY_RENDER);
                        break;
                }
                //Fills bar with red box
                gx.setStroke(new BasicStroke(0));
                gx.setColor(AssetsColorArray[4]);
                gx.fillRect(6, 6, 58, 688);
                //Partially fills bar with a white box
                gx.setColor(AssetsColorArray[0]);
                gx.fillRect(6, 6, 58, (int) (688 - Math.floor(CurrentPlayerLaserBarCharge)));

            }

        };
        //Setup Laser charge bar properties
        PlayerLaserBarPanel.setBounds(20, 99, 60, 700);
        PlayerLaserBarPanel.setBorder(AssetsBorderArray[0]);
        //Adds Laser charge bar at front most position
        GameWindowPanel.add(PlayerLaserBarPanel, new Integer(9));
    }

    private void PlayerLaserIncreaseButtonSetup() {
        /*
        This method sets the properties of the laser increase button
        It then adds it to the game panel at the front most position
         */

        //Setup player laser increase button properties
        final JButton PlayerLaserIncreaseButton = new JButton(AssetsButtonIconArray[8]);
        PlayerLaserIncreaseButton.setBounds(20, 814, 60, 60);
        PlayerLaserIncreaseButton.setBorder(AssetsBorderArray[0]);
        PlayerLaserIncreaseButton.setBackground(null);
        AddButtonHoverColorHighlight(PlayerLaserIncreaseButton, AssetsButtonIconArray[37]);

        //Adds mouse press Listener laser increase button
        PlayerLaserIncreaseButton.addActionListener((final ActionEvent e) -> {
            //If game is not over
            if (!GameOver) {
                //If laser charge is not max and increase can be afforded
                if (CurrentPlayerLaserBarCharge < 688 && Money >= 500) {
                    //Deducts price
                    Money = Money - 500;
                    //Updates money display
                    setMoneyCountText(Money);
                    //Increases laser charge
                    CurrentPlayerLaserBarCharge = CurrentPlayerLaserBarCharge + 200;
                    if (CurrentPlayerLaserBarCharge > 688) {
                        CurrentPlayerLaserBarCharge = 688;
                    }
                    //Updates laser charge display bar graphics
                    PlayerLaserBarPanel.repaint();
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                } else {
                    //If increase can not be afforded
                    if (Money < 500) {
                        //Console will display "Unaffordable" for 2 seconds
                        SetConsoleLine3("Unaffordable", 2);

                    } else {
                        //If laser charge is max
                        if (CurrentPlayerLaserBarCharge == 688) {
                            //Console will display "Max charge" for 2 seconds
                            SetConsoleLine3("Max charge", 2);

                        }
                    }
                }
            }
        });
        //Adds mouse held listener to laser increase button 
        AddMousePressedListener(PlayerLaserIncreaseButton);
        //Adds laser increase button at front most position
        GameWindowPanel.add(PlayerLaserIncreaseButton, new Integer(9));
    }

    private void AddEnemyCovers() {
        /*
        This method sets the properties of the start and end enemy covers
        It then adds them to the game panel behind buttons but infront of enemies and tiles
         */

        //Setup start enemy cover properties
        final JLabel EnemyCoverStartPanel = new JLabel();
        EnemyCoverStartPanel.setBounds(21, 206, 80, 86);
        EnemyCoverStartPanel.setIcon(AssetsImageIconArray[0]);
        EnemyCoverStartPanel.setBorder(null);
        //Adds end enemy cover behind buttons but infront of enemies and tiles
        GameWindowPanel.add(EnemyCoverStartPanel, new Integer(8));

        //Setup end enemy properties
        final JLabel EnemyCoverEndPanel = new JLabel();
        EnemyCoverEndPanel.setBounds(789, 606, 80, 86);
        EnemyCoverEndPanel.setIcon(AssetsImageIconArray[1]);
        EnemyCoverEndPanel.setBorder(null);
        //Adds end enemy cover behind buttons but infront of enemies and tiles
        GameWindowPanel.add(EnemyCoverEndPanel, new Integer(8));

    }

    private void setWaveCountText(final int Wave) {
        //Sets wave display text
        WaveCount.setText("Wave: " + Wave);

    }

    private void setMoneyCountText(final int money) {
        //Rounds money to nearest million / thousand / integer
        float DisplayMoney;
        if (money >= 1000000) {
            DisplayMoney = money / 1000000;
            //Sets money display text
            MoneyCount.setText("Money: " + DisplayMoney + "M");
        } else {
            if (money >= 10000) {
                DisplayMoney = money / 1000;
                //Sets money display text
                MoneyCount.setText("Money: " + DisplayMoney + "K");
            } else {
                DisplayMoney = money;
                //Sets money display text
                MoneyCount.setText("Money: " + (int) DisplayMoney);
            }
        }
    }

    private void setHealthCountText(final int health) {
        //Sets life display text
        HealthCount.setText("Health: " + health);

    }

    private final ArrayList<Enemies> EnemiesArray = new ArrayList<>();

    private final ArrayList<Enemies> EnemiesArrayMoveRightArray = new ArrayList<>();
    private final ArrayList<Enemies> EnemiesArrayMoveLeftArray = new ArrayList<>();
    private final ArrayList<Enemies> EnemiesArrayMoveUpArray = new ArrayList<>();
    private final ArrayList<Enemies> EnemiesArrayMoveDownArray = new ArrayList<>();

    private void UpdateEnemiesPosition() {
        /*
        This method moves the enemies along the path
        If an enemy reaches a waypoint it changes direction
        It also detects when an enemy reaches the end of the path and deducts health when this happens
        It is called every time the game loops when not paused
         */

        int EnemyXCoordinate;
        int EnemyYCoordinate;
        //Loops through enemies moving right
        for (int EnemiesArrayMoveRightArrayIndex = 0; EnemiesArrayMoveRightArrayIndex < EnemiesArrayMoveRightArray.size(); EnemiesArrayMoveRightArrayIndex++) {
            //Moves the enemy to the right
            EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).setXCoordinate(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getXCoordinate() + (WaveSpeed));
            //Finds coordinate of the enemy after it has moved
            EnemyXCoordinate = EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getXCoordinate();
            EnemyYCoordinate = EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getYCoordinate();
            //Finds enemies next waypoint
            final String[] WayPoint = WayPoints.get(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getNextWaypointIndex());
            //If the enemy has reached the waypoint
            if (EnemyXCoordinate == Integer.valueOf(WayPoint[0]) && EnemyYCoordinate == Integer.valueOf(WayPoint[1])) {
                //Sets next waypoint for the enemy
                EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).setNextWaypointIndex(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getNextWaypointIndex() + 1);
                final String MoveNext = WayPoint[2];
                //Changes direction of the enemy
                if (MoveNext.equals("U")) {
                    EnemiesArrayMoveUpArray.add(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex));
                }
                if (MoveNext.equals("D")) {
                    EnemiesArrayMoveDownArray.add(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex));
                }
                //If the enemy has reached the end of the path
                if (MoveNext.equals("end")) {
                    //Deducts player health by amount equal to enemies remaining health
                    PlayerHealth = PlayerHealth - (int) Math.ceil(EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).getHealthPoints());
                    if (PlayerHealth < 0) {
                        PlayerHealth = 0;
                    }
                    setHealthCountText(PlayerHealth);
                    //Kills enemy
                    EnemiesArrayMoveRightArray.get(EnemiesArrayMoveRightArrayIndex).setHealthPoints(0);
                }
                EnemiesArrayMoveRightArray.remove(EnemiesArrayMoveRightArrayIndex);
                EnemiesArrayMoveRightArrayIndex--;
            }
        }
        //Loops through enemies moving left
        for (int EnemiesArrayMoveLeftArrayIndex = 0; EnemiesArrayMoveLeftArrayIndex < EnemiesArrayMoveLeftArray.size(); EnemiesArrayMoveLeftArrayIndex++) {
            //Moves the enemy to the left
            EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).setXCoordinate(EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).getXCoordinate() - (WaveSpeed));
            //Finds coordinate of the enemy after it has moved
            EnemyXCoordinate = EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).getXCoordinate();
            EnemyYCoordinate = EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).getYCoordinate();
            //Finds enemies next waypoint
            final String[] WayPoint = WayPoints.get(EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).getNextWaypointIndex());
            //If the enemy has reached the waypoint
            if (EnemyXCoordinate == Integer.valueOf(WayPoint[0]) && EnemyYCoordinate == Integer.valueOf(WayPoint[1])) {
                //Sets next waypoint for the enemy
                EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).setNextWaypointIndex(EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex).getNextWaypointIndex() + 1);
                final String MoveNext = WayPoint[2];
                //Changes direction of the enemy
                if (MoveNext.equals("U")) {
                    EnemiesArrayMoveUpArray.add(EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex));
                }
                if (MoveNext.equals("D")) {
                    EnemiesArrayMoveDownArray.add(EnemiesArrayMoveLeftArray.get(EnemiesArrayMoveLeftArrayIndex));
                }
                EnemiesArrayMoveLeftArray.remove(EnemiesArrayMoveLeftArrayIndex);
                EnemiesArrayMoveLeftArrayIndex--;
            }
        }
        //Loops through enemies moving up
        for (int EnemiesArrayMoveUpArrayIndex = 0; EnemiesArrayMoveUpArrayIndex < EnemiesArrayMoveUpArray.size(); EnemiesArrayMoveUpArrayIndex++) {
            //Moves the enemy upwards
            EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).setYCoordinate(EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).getYCoordinate() - (WaveSpeed));
            //Finds coordinate of the enemy after it has moved
            EnemyXCoordinate = EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).getXCoordinate();
            EnemyYCoordinate = EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).getYCoordinate();
            //Finds enemies next waypoint
            final String[] WayPoint = WayPoints.get(EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).getNextWaypointIndex());
            //If the enemy has reached the waypoint
            if (EnemyXCoordinate == Integer.valueOf(WayPoint[0]) && EnemyYCoordinate == Integer.valueOf(WayPoint[1])) {
                //Sets next waypoint for the enemy
                EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).setNextWaypointIndex(EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex).getNextWaypointIndex() + 1);
                //Changes direction of the enemy
                final String MoveNext = WayPoint[2];
                if (MoveNext.equals("R")) {
                    EnemiesArrayMoveRightArray.add(EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex));
                }
                if (MoveNext.equals("L")) {
                    EnemiesArrayMoveLeftArray.add(EnemiesArrayMoveUpArray.get(EnemiesArrayMoveUpArrayIndex));
                }
                EnemiesArrayMoveUpArray.remove(EnemiesArrayMoveUpArrayIndex);
                EnemiesArrayMoveUpArrayIndex--;
            }
        }
        //Loops through enemies moving down
        for (int EnemiesArrayMoveDownArrayIndex = 0; EnemiesArrayMoveDownArrayIndex < EnemiesArrayMoveDownArray.size(); EnemiesArrayMoveDownArrayIndex++) {
            //Moves the enemy downwards
            EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).setYCoordinate(EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).getYCoordinate() + (WaveSpeed));
            //Finds coordinate of the enemy after it has moved
            EnemyXCoordinate = EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).getXCoordinate();
            EnemyYCoordinate = EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).getYCoordinate();
            //Finds enemies next waypoint
            final String[] WayPoint = WayPoints.get(EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).getNextWaypointIndex());
            //If the enemy has reached the waypoint
            if (EnemyXCoordinate == Integer.valueOf(WayPoint[0]) && EnemyYCoordinate == Integer.valueOf(WayPoint[1])) {
                //Sets next waypoint for the enemy
                EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).setNextWaypointIndex(EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex).getNextWaypointIndex() + 1);
                final String MoveNext = WayPoint[2];
                //Changes direction of the enemy
                if (MoveNext.equals("R")) {
                    EnemiesArrayMoveRightArray.add(EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex));
                }
                if (MoveNext.equals("L")) {
                    EnemiesArrayMoveLeftArray.add(EnemiesArrayMoveDownArray.get(EnemiesArrayMoveDownArrayIndex));
                }
                EnemiesArrayMoveDownArray.remove(EnemiesArrayMoveDownArrayIndex);
                EnemiesArrayMoveDownArrayIndex--;
            }
        }

    }

    private void UpdateEnemyMove(final Enemies CurrentEnemy) {
        /*
        This method updates the position of the enemy within the game
        It is called every time the game loops when not paused for every enemy
         */

        //Sets the location of the enemy within the game
        CurrentEnemy.getPanel().setBounds(CurrentEnemy.getXCoordinate(), CurrentEnemy.getYCoordinate(), 100, 100);
    }

    private void UpdateEnemyRotate(final Enemies CurrentEnemy) {
        /*
        This method updates the enemies rotation
        It is called every time the game loops when not paused for every enemy
         */

        //Calculates the rotation of the shape
        double Rotation = CurrentEnemy.getRotatedDegrees();
        //Increases its rotation
        Rotation = Rotation + WaveSpeed;
        if (Rotation >= 360) {
            Rotation = Rotation - 360;
        }
        //Updates the enemies rotation
        CurrentEnemy.setRotatedDegrees(Rotation);
    }

    private void UpdateEnemyShape(Enemies CurrentEnemy) {
        /*
        This method updates the enemies shape
        It is called every time the game loops when not paused for every enemy
         */

        //Calculates the number of sides of the enemy’s shape from its health points
        final int Sides = (int) Math.ceil(CurrentEnemy.getHealthPoints() / 10);
        //If the shape is a circle sector
        if (Sides == 2) {
            //Updates the enemies circle sector shape
            CurrentEnemy.UpdateCircleSector();
        } //If the shape is not a circle sector
        else {//If the shape is a polygon
            if (Sides > 2) {
                //Updates the enemies polygon shape
                CurrentEnemy.UpdatePoly();
            }
        }
    }

    private void UpdateEnemyHover(final Enemies CurrentEnemy) {
        /*
        This method detects whether the cursor is hovering over the enemies
        It is called every time the game loops when not paused  for every enemy
         */

        //Calculates the number of sides of the enemies shape from its health points
        final int Sides = (int) Math.ceil(CurrentEnemy.getHealthPoints() / 10);
        try {
            //If the cursor is not within the enemy shape
            if (CurrentEnemy.getPanel().getMousePosition() == null) {
                //Sets hover to false
                CurrentEnemy.setHover(false);
            }//If the cursor is within the enemy shape
            else {
                //Finds coordinate cursor within shape
                Point CursorPositionWithinEnemyShape = CurrentEnemy.getPanel().getMousePosition();
                CursorPositionWithinEnemyShape.translate(-18, -18);
                //If the shape is a circle
                if (Sides == 1) {
                    //If the circle contains the cursor
                    if (CurrentEnemy.getCircle().contains(CursorPositionWithinEnemyShape)) {
                        //Sets hover to true
                        CurrentEnemy.setHover(true);
                    } else {
                        //Sets hover to false
                        CurrentEnemy.setHover(false);
                    }
                } //If the shape is not a circle
                else {
                    //If the shape is a semicircle
                    if (Sides == 2) {
                        //If the semicircle contains the cursor
                        if (CurrentEnemy.getCircleSector().contains(CursorPositionWithinEnemyShape)) {
                            //Sets hover to true
                            CurrentEnemy.setHover(true);
                        } else {
                            //Sets hover to false
                            CurrentEnemy.setHover(false);
                        }

                    } //If the shape is not a semicircle
                    else {
                        //If the polygon contains the cursor
                        if (CurrentEnemy.getPoly().contains(CursorPositionWithinEnemyShape)) {
                            //Sets hover to true
                            CurrentEnemy.setHover(true);
                        } else {
                            //Sets hover to false
                            CurrentEnemy.setHover(false);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            //Sets hover to false
            CurrentEnemy.setHover(false);
        }
    }

    private void UpdatePlayerLaserTarget(final Enemies CurrentEnemy) {
        /*
        This method sets the target for the player laser
        It is called every time the game loops when not paused for every enemy
         */

        //If the mouse is held and the current enemy is hovered over
        if (MousePressed && CurrentEnemy.isHover()) {
            //Sets this enemy to the current enemy
            PlayerLaserTarget = CurrentEnemy;
        }

    }

    private void UpdatePlayerLaserCharge() {
        /*
        This method increases / decreases the player laser charge
        It also displays the charge using a bar
        It is called every time the game loops when not paused
         */

        //If the player laser is not at full charge and does not have a target
        if (CurrentPlayerLaserBarCharge < 688 && PlayerLaserTarget == null) {
            //Increases player laser charge by amount inversely proportional to the game difficulty
            double LaserBarDifficulty;
            if (Difficulty == 3) {
                LaserBarDifficulty = 4;
            } else {
                LaserBarDifficulty = Difficulty;
            }
            CurrentPlayerLaserBarCharge = CurrentPlayerLaserBarCharge + (0.5 / (LaserBarDifficulty * 2));
            if (CurrentPlayerLaserBarCharge > 688) {
                CurrentPlayerLaserBarCharge = 688;
            }
            PlayerLaserBarPanel.repaint();
        } else {
            //If the player laser has a target and enough charge to fire
            if (PlayerLaserTarget != null && CurrentPlayerLaserBarCharge >= 3.34) {
                //Deducts charge
                CurrentPlayerLaserBarCharge = CurrentPlayerLaserBarCharge - 3.34;
                if (CurrentPlayerLaserBarCharge < 0) {
                    CurrentPlayerLaserBarCharge = 0;
                }
                PlayerLaserBarPanel.repaint();
            } else {
                //If the player laser does not have enough charge to fire 
                if (CurrentPlayerLaserBarCharge < 3.34) {
                    //Console will display "Out of charge" for 2 seconds
                    SetConsoleLine3("Out of charge", 2);
                    //Increases player laser charge by amount inversely proportional to the game difficulty
                    double LaserBarDifficulty;
                    if (Difficulty == 3) {
                        LaserBarDifficulty = 4;
                    } else {
                        LaserBarDifficulty = Difficulty;
                    }
                    CurrentPlayerLaserBarCharge = CurrentPlayerLaserBarCharge + (0.5 / (LaserBarDifficulty * 2));
                    if (CurrentPlayerLaserBarCharge > 688) {
                        CurrentPlayerLaserBarCharge = 688;
                    }
                    PlayerLaserBarPanel.repaint();
                }
            }

        }

    }

    private void UpdatePlayerLaserTargetDamage() {
        /*
        This method damages enemies targeted by the player laser
        It is called every time the game loops when not paused
         */

        //If the player is charged and has a target
        if (CurrentPlayerLaserBarCharge >= 3.34 && PlayerLaserTarget != null) {
            //Damages the player laser target
            PlayerLaserTarget.setHealthPoints(PlayerLaserTarget.getHealthPoints() - 1);
            //If the target enemy now has health below 0
            if (PlayerLaserTarget.getHealthPoints() <= 0) {
                //Sets target enemies health to 0
                PlayerLaserTarget.setHealthPoints(0);
            }
        }
    }

    private int EnemyCount = 0;

    private void UpdateTurretTarget(final Turrets CurrentTurret) {
        /*
        This method finds the enemies within range of a turret
        It then sets the turrets target to the furthest enemy along the path that is also within range
        It is called every time the game loops when not paused for every turret
         */

        int Range;
        Enemies CurrentEnemyTarget = null;
        //Finds range of turret based on turret type
        switch (CurrentTurret.getTurretType()) {
            case (1):
                Range = 150;
                break;
            case (2):
                Range = 350;
                break;
            case (3):
                Range = 550;
                break;
            default:
                Range = 0;
                break;

        }
        //Loops through all alive enemies
        for (int EnemiesArrayIndex = 0; EnemiesArrayIndex < EnemiesArray.size(); EnemiesArrayIndex++) {
            //Finds centre coordinate of turret
            final int TurretXCoordinate = CurrentTurret.getXCoordinate() + 50;
            final int TurretYCoordinate = CurrentTurret.getYCoordinate() + 50;
            //Finds centre coordinate of enemy
            final int EnemyXCoordinate = EnemiesArray.get(EnemiesArrayIndex).getXCoordinate() + 50;
            final int EnemyYCoordinate = EnemiesArray.get(EnemiesArrayIndex).getYCoordinate() + 50;
            //Calculates distance between the turret and enemy using pythagoras's theorem
            final int Distance = (int) Math.ceil(Math.sqrt(Math.pow((EnemyXCoordinate - TurretXCoordinate), 2) + Math.pow((EnemyYCoordinate - TurretYCoordinate), 2)));
            //If enemy is within range and is within the map
            if (Distance <= Range && ((EnemiesArray.get(EnemiesArrayIndex).getXCoordinate() + 50) > 101) && ((EnemiesArray.get(EnemiesArrayIndex).getXCoordinate() + 50) < 789)) {
                //If the turret does not have a target
                if (CurrentEnemyTarget == null) {
                    //If the enemies priority is lower then 1 more then the size of the wave
                    if (EnemiesArray.get(EnemiesArrayIndex).getPriority() < (EnemyCount + 1)) {
                        //Sets the turrets target to this enemy
                        CurrentEnemyTarget = EnemiesArray.get(EnemiesArrayIndex);
                    }
                }//If the enemy is further along the path then the turrets current target
                else if (EnemiesArray.get(EnemiesArrayIndex).getPriority() < CurrentEnemyTarget.getPriority()) {
                    //Sets the turrets target to this enemy
                    CurrentEnemyTarget = EnemiesArray.get(EnemiesArrayIndex);
                }
            }
        }
        //Sets the target enemy for this turret
        CurrentTurret.setTargetEnemy(CurrentEnemyTarget);
    }

    private void UpdateTurretRotation(final Turrets CurrentTurret) {
        //Finds bearing between the turret and targeted enemy
        CurrentTurret.setRotatedRadians(CurrentTurret.getAngle());
    }

    private void UpdatePlayerLaserGraphics() {
        PlayerLaserPanel.repaint();
    }

    private void UpdateRangeIndicatorGraphics() {
        RangeIndicatorPanel.repaint();
    }

    private void setEnemyCount(final int EnemyCount) {
        this.EnemyCount = EnemyCount;

    }

    private void UpdateTurretsDeadTargets(final Enemies CurrentEnemy) {
        /*
        This method removes dead enemies from turret targeting
        It is called whenever an enemy is removed from the game
         */

        //Loops through all turrets
        for (int i = 0; i < TurretsArray.size(); i++) {
            //If turret is targeting the removed enemy
            if (TurretsArray.get(i).getTargetEnemy() == CurrentEnemy) {
                //Sets turret target to nothing
                TurretsArray.get(i).setTargetEnemy(null);
            }
        }
    }

    private void UpdateTurretsDamage() {
        /*
        This method damages enemies based on turret type
        It is called every time the game loops when not paused
         */

        boolean TurretsFired = false;

        double TurretDamage;
        //Loops through all turrets
        for (int TurretsArrayIndex = 0; TurretsArrayIndex < TurretsArray.size(); TurretsArrayIndex++) {
            //If the turret has a target
            if (TurretsArray.get(TurretsArrayIndex).getTargetEnemy() != null) {
                //Finds turret level
                int TurretLevel = TurretsArray.get(TurretsArrayIndex).getTurretLevel();
                //Calculates damage based on turret type and level
                switch (TurretsArray.get(TurretsArrayIndex).getTurretType()) {
                    case (1):
                        TurretDamage = 0.05 * TurretLevel;
                        break;
                    case (2):
                        TurretDamage = 0.025 * TurretLevel;
                        break;
                    case (3):
                        TurretDamage = 0.015 * TurretLevel;
                        break;
                    default:
                        TurretDamage = 0;
                        break;
                }
                //Deducts damage from the target enemies health
                TurretsArray.get(TurretsArrayIndex).getTargetEnemy().setHealthPoints(TurretsArray.get(TurretsArrayIndex).getTargetEnemy().getHealthPoints() - (TurretDamage));
                //If the target enemy now has health below 0
                if (TurretsArray.get(TurretsArrayIndex).getTargetEnemy().getHealthPoints() < 0) {
                    //Sets target enemies health to 0
                    TurretsArray.get(TurretsArrayIndex).getTargetEnemy().setHealthPoints(0);
                }
                TurretsFired = true;
            }
        }
        LasersFiring = TurretsFired;
    }

    private int EnemiesRemoved = 0;

    private void RemoveDeadEnemies() {
        /*
        This method removes enemies that have died
        It also adds a dead enemy in its place
        It is called every time the game loops when not paused
         */

        //Loops through all enemies
        for (int EnemiesArrayPosition = 0; EnemiesArrayPosition < EnemiesArray.size(); EnemiesArrayPosition++) {
            //If enemy has no health remaining
            if (EnemiesArray.get(EnemiesArrayPosition).getHealthPoints() <= 0) {
                //Removes enemy from any enemy movement array containing it
                if (EnemiesArrayMoveRightArray.contains(EnemiesArray.get(EnemiesArrayPosition))) {
                    EnemiesArrayMoveRightArray.remove(EnemiesArray.get(EnemiesArrayPosition));
                }
                if (EnemiesArrayMoveLeftArray.contains(EnemiesArray.get(EnemiesArrayPosition))) {
                    EnemiesArrayMoveLeftArray.remove(EnemiesArray.get(EnemiesArrayPosition));
                }
                if (EnemiesArrayMoveUpArray.contains(EnemiesArray.get(EnemiesArrayPosition))) {
                    EnemiesArrayMoveUpArray.remove(EnemiesArray.get(EnemiesArrayPosition));
                }
                if (EnemiesArrayMoveDownArray.contains(EnemiesArray.get(EnemiesArrayPosition))) {
                    EnemiesArrayMoveDownArray.remove(EnemiesArray.get(EnemiesArrayPosition));
                }
                //If the enemy was the player laser target
                if (PlayerLaserTarget == EnemiesArray.get(EnemiesArrayPosition)) {
                    //Sets player laser target to nothing
                    PlayerLaserTarget = null;
                }
                //Removes enemy from the game
                EnemiesArray.get(EnemiesArrayPosition).getPanel().setVisible(false);
                try {
                    GameWindowPanel.remove(EnemiesArray.get(EnemiesArrayPosition).getPanel());
                } catch (Exception ex) {
                    System.out.println("Removing dead enemy error: " + ex);
                }
                //Removes dead enemy from turret targeting
                UpdateTurretsDeadTargets(EnemiesArray.get(EnemiesArrayPosition));
                //Creates new dead enemy at the same position as where the enemy died and adds it to the game
                final DeadEnemies BufferDeadEnemies = new DeadEnemies(EnemiesArray.get(EnemiesArrayPosition).getXCoordinate(), EnemiesArray.get(EnemiesArrayPosition).getYCoordinate(), EnemiesArray.get(EnemiesArrayPosition).getFillColor());
                DeadEnemiesArray.add(BufferDeadEnemies);
                GameWindowPanel.add(BufferDeadEnemies.getPanel(), new Integer(6));
                //Removes the enemy from the array containing all enemies
                EnemiesArray.remove(EnemiesArray.get(EnemiesArrayPosition));
                EnemiesArrayPosition--;
                //Increments number of enemies removed
                EnemiesRemoved++;
                //Displays remaining enemies on console if the console is not already occupied
                if (!ConsoleLine1.equals("Wave " + (WaveNumber + WaveNumberCompensator) + " Arrives")) {
                    ConsoleLine1 = "Enemies left: " + (EnemyCount - EnemiesRemoved);
                    ConsoleLine1Time = -1;
                }
                //If bounty game mode
                if (GameMode.equals("Bounty")) {
                    //Increase money on enemy death
                    Money = Money + 250;
                    setMoneyCountText(Money);
                }

            }
        }
    }

    private final ArrayList<DeadEnemies> DeadEnemiesArray = new ArrayList<>();

    private void UpdateDeadEnemiesAnimation() {
        /*
        This method updates the death animation of dead enemies
        It is called every time the game loops when not paused
         */

        //Loops through all dead enemies
        for (int DeadEnemiesArrayIndex = 0; DeadEnemiesArrayIndex < DeadEnemiesArray.size(); DeadEnemiesArrayIndex++) {
            //Updates death animation
            DeadEnemiesArray.get(DeadEnemiesArrayIndex).getPanel().repaint();
            //Increments dead enemies animation time
            DeadEnemiesArray.get(DeadEnemiesArrayIndex).setAnimationTime(DeadEnemiesArray.get(DeadEnemiesArrayIndex).getAnimationTime() + 1);
            //If dead enemies animation is finished
            if (DeadEnemiesArray.get(DeadEnemiesArrayIndex).getAnimationTime() >= 16) {
                //Removes dead enemy from game
                DeadEnemiesArray.get(DeadEnemiesArrayIndex).getPanel().setVisible(false);
                try {
                    GameWindowPanel.remove(DeadEnemiesArray.get(DeadEnemiesArrayIndex).getPanel());
                } catch (Exception ex) {
                    System.out.println("Updating dead enemy animation error: " + ex);
                }
                //Removes dead enemy from array containing all dead enemies
                DeadEnemiesArray.remove(DeadEnemiesArray.get(DeadEnemiesArrayIndex));

            }
        }
    }

    private void AddMousePressedListener(final Component cmpnt) {
        /*
        This method adds a mouse held listener to the component
        This is to detect whenever the player laser should be fired
         */

        //Adds mouse press and release listener to component
        cmpnt.addMouseListener(new MouseAdapter() {
            //When mouse is pressed
            @Override
            public void mousePressed(final MouseEvent e) {
                //Mouse held is set to true
                MousePressed = true;
            }

            //When mouse is released
            @Override
            public void mouseReleased(final MouseEvent e) {
                //Mouse held is set to false
                MousePressed = false;
            }
        });
    }
    private ArrayList<Enemies> EnemyWaveQueue = new ArrayList<>();

    private void AddEnemyWaveControl() {
        /*
        This method defines the properties of the new wave
        It then creates the new wave    
         */

        //Sets targeting priority to 0 because a new wave is being created
        TargetingPriority = 0;
        //Calculates difficulty of new wave
        final double DifficultyInput = 3 + (Difficulty * 0.35); //3,4,5
        //Creates new wave
        EnemyWaveQueue = EnemyWaveArrayCreator(WaveNumber, DifficultyInput);
        //Sets enemy count
        setEnemyCount(EnemyWaveQueue.size());
    }

    private ArrayList<Enemies> EnemyWaveArrayCreator(final int WaveNumber, final double Difficulty) {
        /*
        This method creates a new wave based on the difficulty settings and current wave
        It calculates the combined health of all the enemies in the new wave
        It then deterministically splits the health between multiple enemies and shifts their positions in the wave to make the wave less predictable
        It also defines the colour and shape of the enemy based on its health points
        It then returns the new enemy wave
         */

        final ArrayList<Enemies> NewEnemyWaveArrayQueue = new ArrayList<>();
        final ArrayList<Integer> EnemyHealths = new ArrayList<>();
        double BossEnemyHealth = 0;
        //Calculates combined health of the new wave
        int WaveCombinedHealthPoints = (int) Math.floor(WaveNumber * Difficulty);
        while (true) {
            //If combined health above 600
            if ((Math.floor(WaveCombinedHealthPoints / 2)) > 30) {
                //Adds new enemy with 300 health 
                EnemyHealths.add(30);
                //Removes 300 health from combined wave health
                WaveCombinedHealthPoints = (WaveCombinedHealthPoints - 30);
            }//If combined health less then 600
            else {
                //Adds new enemy with same health as half of combined wave health
                EnemyHealths.add((int) (Math.floor(WaveCombinedHealthPoints / 2)));
                //Removes half of combined wave health
                WaveCombinedHealthPoints = (int) (WaveCombinedHealthPoints - Math.floor(WaveCombinedHealthPoints / 2));
            }
            //If remaining combined health reaches zero
            if (Math.floor(WaveCombinedHealthPoints / 2) == 0) {
                //If boss wave
                if (WaveNumber % 10 == 0) {
                    //Adds new boss enemy with 60 more health then highest health enemy currently in wave
                    EnemyHealths.add(EnemyHealths.get(0) + 6);
                    BossEnemyHealth = (EnemyHealths.get(0) + 6) * 10;
                }
                break;
            }
        }
        //Generates a deterministic pseudorandom number from the wave number
        final Random PseudoRand = new Random();
        PseudoRand.setSeed(WaveNumber);
        final int PseudoRandomNumber = PseudoRand.nextInt(EnemyHealths.size());

        //Shifts the order of enemies within the wave array
        ArrayList<Integer> EnemyHealthsShifted = ShiftArray(EnemyHealths, PseudoRandomNumber);

        final Random Rand = new Random();

        //Loops through all enemies within the new wave
        for (int i = 0; i < EnemyHealthsShifted.size(); i++) {
            //Defines health of the enemy
            double HealthPoints = EnemyHealthsShifted.get(i) * 10;
            //Randomises colour of the enemy
            Color FillColor = new Color(Rand.nextInt(200) + 55, Rand.nextInt(200) + 55, Rand.nextInt(200) + 55);
            //Creates new enemy at start position
            Enemies BufferEnemy = new Enemies(HealthPoints, FillColor);
            //If this enemy has the same hit points as the boss enemy, if there is one
            if (HealthPoints == BossEnemyHealth) {
                //This enemy is a boss
                BufferEnemy.setBossEnemy();
            }
            //Adds enemy to new wave queue
            NewEnemyWaveArrayQueue.add(BufferEnemy);
        }
        //Returns the new enemy wave
        return NewEnemyWaveArrayQueue;
    }

    private ArrayList<Integer> ShiftArray(final ArrayList<Integer> EnemyHealths, final int ShiftNumber) {
        /*
        This method shifts the positions of enemies within the array to make the wave less predictable
        It then returns the shifted array
         */

        //Loops ShiftNumber times
        for (int i = 0; i < ShiftNumber; i++) {
            //Removes last element and adds it to front of the array
            int LastHealth = EnemyHealths.remove(EnemyHealths.size() - 1);
            EnemyHealths.add(0, LastHealth);
        }
        //Returns shifted array
        return EnemyHealths;
    }

    private int TargetingPriority = 0;

    private void AddEnemy() {
        /*
        This method adds an enemy to the game and sets its priority for targeting
        It adds it to all necessary arrays and removes it from the wave queue        
         */

        //Sets priority of new enemy
        EnemyWaveQueue.get(0).setPriority(TargetingPriority);
        //Adds enemy to game displays
        GameWindowPanel.add(EnemyWaveQueue.get(0).getPanel(), new Integer(7));
        //Adds enemy to array of all enemies and array of all enemies that are moving right
        EnemiesArray.add(EnemyWaveQueue.get(0));
        EnemiesArrayMoveRightArray.add(EnemyWaveQueue.get(0));
        //Removes enemy from the wave queue
        EnemyWaveQueue.remove(EnemyWaveQueue.get(0));
        //Increments priority
        TargetingPriority++;
    }

    private boolean LasersFiring = false;
    private boolean PlayerLaserFiring = false;

    private void GameSoundLoop() {
        /*
        This method plays the turret laser and player laser sound effects
         */

        final Clip LaserSounds = SetVolume(GetNewClip(AssetsSoundDirectoryArray[1]), Shape_Shooter_TD.MainClass.getSoundVolume());
        final Clip PlayerLaserSounds = SetVolume(GetNewClip(AssetsSoundDirectoryArray[2]), Shape_Shooter_TD.MainClass.getSoundVolume());
        //Loops while the game is not over
        while (!GameOver) {
            //20 millisecond delay every loop
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("Game sound loop error: " + ex);
            }
            //If the turrets are firing and sound should be audible
            if (LasersFiring && !GamePaused && !(!Shape_Shooter_TD.MainClass.isWindowFocused() && Shape_Shooter_TD.MainClass.isMuteSoundInBackground())) {
                //If the turret laser sound effect is not playing
                if (!LaserSounds.isActive()) {
                    //Plays the turret laser sound effect on loop
                    LaserSounds.start();
                    LaserSounds.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } //If the turrets are not firing or sound should be inaudible
            else {
                //If the turret laser sound effect is playing
                if (LaserSounds.isActive()) {
                    //Pauses the turret laser sound effect
                    LaserSounds.stop();
                }
            }
            //If the player laser is firing and sound should be audible
            if (PlayerLaserFiring && !GamePaused && !(!Shape_Shooter_TD.MainClass.isWindowFocused() && Shape_Shooter_TD.MainClass.isMuteSoundInBackground())) {
                //If the player laser sound effect is not playing
                if (!PlayerLaserSounds.isActive()) {
                    //Plays the player laser sound effect on loop
                    PlayerLaserSounds.start();
                    PlayerLaserSounds.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } //If the player laser is not firing or sound should be inaudible
            else {
                //If the player laser sound effect is playing
                if (PlayerLaserSounds.isActive()) {
                    //Pauses the turret laser sound effect
                    PlayerLaserSounds.stop();
                }
            }
        }
        //If the turret laser sound effect is playing
        if (LaserSounds.isActive()) {
            //Stops the turret laser sound effect
            LaserSounds.stop();
        }
        //If the player laser sound effect is playing
        if (PlayerLaserSounds.isActive()) {
            //Stops the turret laser sound effect
            PlayerLaserSounds.stop();
        }
    }

    private final JTextArea TutorialField = new JTextArea();
    private int TutorialMessageIndex = 0;

    private void TutorialAreaSetup() {
        /*
        This method sets the properties of the tutorial area components
        It then adds them to the game panel
         */

        //List of all game  tutorial messages
        final String GameCustomisationTutorialMessages[] = {
            "You must try and survive for as long as possible\n"
            + "Enemies will follow the path from top-left to bottom-right",
            "The enemy waves will grow in strength as you progress\n"
            + "Their speed will double every 50 waves until wave 100",
            "There will be a boss enemy every 10 rounds\n"
            + "The enemies will deal damage at the end of the path",
            "The game is over when your health reaches zero\n"
            + "If logged in, the game details will be on the leaderboard",
            "Each turret cost 1000 money to place and upgrade one\n"
            + "level, turrets cannot be upgraded past level 3",
            "Heavier turrets deal more damage but have less range\n"
            + "To place one, select the turret type and then select a tile",
            "To upgrade a turret, place the same turret on top of itself\n"
            + "To recycle it, select the recycle button and select a turret",
            "The bar displays your weapons charge and will slowly fill\n"
            + "The red plus button increases your charge for 500 money",
            "To fire your weapon, click and hold over an enemy\n"
            + "This will deplete your weapon charge",
            "There are various buttons that change the game speed\n"
            + "The console will display useful information",};
        //Sets the properties of the tutorial area
        final JPanel TutorialArea = new JPanel();
        TutorialArea.setBorder(AssetsBorderArray[0]);
        TutorialArea.setBounds(230, 20, 570, 64);
        TutorialArea.setBackground(AssetsColorArray[0]);
        //Adds mouse held listener to tutorial area
        AddMousePressedListener(TutorialArea);
        //Adds the tutorial area infront of the header title
        GameWindowPanel.add(TutorialArea, new Integer(10));
        //Sets properties of the tutorial messages text area
        TutorialField.setBorder(AssetsBorderArray[4]);
        TutorialField.setBounds(264, 26, 502, 52);
        TutorialField.setBackground(AssetsColorArray[0]);
        TutorialField.setForeground(AssetsColorArray[3]);
        TutorialField.setFont(AssetsFontArray[2]);
        TutorialField.setText(GameCustomisationTutorialMessages[TutorialMessageIndex]);
        TutorialField.setEditable(false);
        //Adds mouse held listener to tutorial messages text area
        AddMousePressedListener(TutorialField);
        //Adds the tutorial messages text area at the front most position
        GameWindowPanel.add(TutorialField, new Integer(11));

        final JButton NextTutorialMessageButton = new JButton(AssetsButtonIconArray[22]);
        final JButton PreviousTutorialMessageButton = new JButton(AssetsButtonIconArray[20]);
        //Sets properties of the previous tutorial message button
        PreviousTutorialMessageButton.setBounds(240, 30, 20, 44);
        PreviousTutorialMessageButton.setBorder(null);
        PreviousTutorialMessageButton.setFocusPainted(false);
        //Adds mouse press listener to the previous tutorial message button
        PreviousTutorialMessageButton.addActionListener((final ActionEvent ae) -> {
            //If not on the first tutorial message
            if (TutorialMessageIndex > 0) {
                //Display previous tutorial message
                TutorialMessageIndex--;
                TutorialField.setText(GameCustomisationTutorialMessages[TutorialMessageIndex]);
                //Updates tutorial message navigation buttons icon
                if (TutorialMessageIndex == 0) {
                    PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[24]);
                } else {
                    PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[20]);
                }
                if (TutorialMessageIndex == GameCustomisationTutorialMessages.length - 1) {
                    NextTutorialMessageButton.setIcon(AssetsButtonIconArray[25]);
                } else {
                    NextTutorialMessageButton.setIcon(AssetsButtonIconArray[22]);
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds mouse held listener to previous tutorial message button 
        AddMousePressedListener(PreviousTutorialMessageButton);
        //Adds hover listener to previous tutorial message button that highlights it
        AddButtonHoverColorHighlight(PreviousTutorialMessageButton, AssetsButtonIconArray[21]);
        PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[24]);
        //Adds previous tutorial message button at front most position
        GameWindowPanel.add(PreviousTutorialMessageButton, new Integer(12));
        //Sets properties of the next tutorial message button
        NextTutorialMessageButton.setBounds(770, 30, 20, 44);
        NextTutorialMessageButton.setBorder(null);
        NextTutorialMessageButton.setFocusPainted(false);
        //Adds mouse press listener to the next tutorial message button
        NextTutorialMessageButton.addActionListener((final ActionEvent ae) -> {
            //If not on the last tutorial message
            if (TutorialMessageIndex < GameCustomisationTutorialMessages.length - 1) {
                //Display next tutorial message
                TutorialMessageIndex++;
                TutorialField.setText(GameCustomisationTutorialMessages[TutorialMessageIndex]);
                //Updates tutorial message navigation buttons icon
                if (TutorialMessageIndex == 0) {
                    PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[24]);
                } else {
                    PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[20]);
                }
                if (TutorialMessageIndex == GameCustomisationTutorialMessages.length - 1) {
                    NextTutorialMessageButton.setIcon(AssetsButtonIconArray[25]);
                } else {
                    NextTutorialMessageButton.setIcon(AssetsButtonIconArray[22]);
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds mouse held listener to next tutorial message button 
        AddMousePressedListener(NextTutorialMessageButton);
        //Adds hover listener to next tutorial message button that highlights it
        AddButtonHoverColorHighlight(NextTutorialMessageButton, AssetsButtonIconArray[23]);
        //Adds next tutorial message button at front most position
        GameWindowPanel.add(NextTutorialMessageButton, new Integer(12));
    }

    private void FinalSetup() {
        /*
        This method sets the properties the game panel
        It then adds it to the main panel
         */

        final JFrame MainWindow = Shape_Shooter_TD.MainClass.getWindow();
        //Removes listener from the main window if it already has one
        if (MainWindow.getMouseListeners().length > 0) {
            MainWindow.removeMouseListener(MainWindow.getMouseListeners()[0]);
        }
        //Adds mouse held listener to main window
        AddMousePressedListener(MainWindow);
        //Sets properties of game panel
        GameWindowPanel.setBounds(0, 0, 1030, 964);
        //Adds mouse held listener to game panel 
        AddMousePressedListener(GameWindowPanel);
        //Creates header, adds mouse held listener to the header, and adds it to game panel
        final JPanel Header = WindowHeaderSetup();
        AddMousePressedListener(Header);
        GameWindowPanel.add(Header, new Integer(9));
        //Adds the game panel to the main panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(GameWindowPanel, new Integer(1));

    }
}
