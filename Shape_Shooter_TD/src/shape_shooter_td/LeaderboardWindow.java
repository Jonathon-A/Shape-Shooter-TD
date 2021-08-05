package shape_shooter_td;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class LeaderboardWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the leaderboard panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens leaderboard panels
        LeaderboardWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Leaderboard panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(LeaderboardPanel);
    }

    private final JLayeredPane LeaderboardPanel = new JLayeredPane();

    private void LeaderboardWindowSetup() {
        /*
        This method opens the leaderboard panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Setup map code entry text field and validation indicator
        MapCodeEntrySetup();
        //Setup game mode filters buttons
        LeaderboardGameModesFilterButtonSetup();
        //Setup difficulty filters buttons
        LeaderboardDifficultyFilterButtonSetup();
        //Setup map code filters buttons 
        LeaderboardMapCodeFilterButtonSetup();
        //Setup find player score button
        FindPlayerScoreSetup();
        //Setup displayed leaderboard table
        LeaderboardTableSetup();
        //Setup leaderboard panel 
        FinalSetup();
    }

    private final JTextField MapCodeEntryPanel = new JTextField();
    private final JTextField MapCodeValidationDisplay = new JTextField();

    private void MapCodeEntrySetup() {
        /*
        This method sets the properties of the map code entry text field and the map code validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the leaderboard panel
         */

        //Sets the properties of the map code entry text field
        MapCodeEntryPanel.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        MapCodeEntryPanel.setBounds(20, 94, 876, 60);
        MapCodeEntryPanel.setBackground(AssetsColorArray[0]);
        MapCodeEntryPanel.setForeground(AssetsColorArray[8]);
        MapCodeEntryPanel.setFont(AssetsFontArray[1]);
        MapCodeEntryPanel.setText("Enter map code...");
        //Adds use listener to the map code entry text field
        MapCodeEntryPanel.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the map code entry text field
            @Override
            public void insertUpdate(final DocumentEvent de) {
                //Checks validation of entered map code
                MapCodeEntryUpdate(MapCodeEntryPanel.getText());
            }

            //When a character is removed from the map code entry text field
            @Override
            public void removeUpdate(final DocumentEvent de) {
                //Checks validation of entered map code
                MapCodeEntryUpdate(MapCodeEntryPanel.getText());
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the map code entry text field
        MapCodeEntryPanel.addFocusListener(new FocusListener() {
            //When the map code entry text field gains focus
            @Override
            public void focusGained(FocusEvent focusEvent) {
                //If the map code entry text field displays "Enter map code..."
                if (MapCodeEntryPanel.getText().equals("Enter map code...")) {
                    //Empties the map code entry text field
                    MapCodeEntryPanel.setText("");
                    //Changes map code entry text field text colour to black
                    MapCodeEntryPanel.setForeground(AssetsColorArray[3]);
                }

            }

            //When the username entry text field loses focus
            @Override
            public void focusLost(FocusEvent focusEvent) {
                //If the map code entry text field is empty
                if (MapCodeEntryPanel.getText().isEmpty()) {
                    //Map code entry text field displays "Enter map code..."
                    MapCodeEntryPanel.setText("Enter map code...");
                    //Changes map code entry text field text colour to grey
                    MapCodeEntryPanel.setForeground(AssetsColorArray[8]);
                }

            }
        });
        //Adds the map code entry text field at the front most position
        LeaderboardPanel.add(MapCodeEntryPanel, new Integer(1));
        //Sets the properties of the map code validation indicator
        MapCodeValidationDisplay.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        MapCodeValidationDisplay.setBounds(890, 94, 120, 60);
        MapCodeValidationDisplay.setFont(AssetsFontArray[1]);
        MapCodeValidationDisplay.setHorizontalAlignment(JTextField.CENTER);
        MapCodeValidationDisplay.setEditable(false);
        MapCodeValidationDisplay.setBackground(AssetsColorArray[2]);
        MapCodeValidationDisplay.setText("Invalid");
        MapCodeValidationDisplay.setFocusable(false);
        //Adds the map code validation indicator at the front most position
        LeaderboardPanel.add(MapCodeValidationDisplay, new Integer(2));
    }

    private void MapCodeEntryUpdate(final String EnteredMapCode) {
        /*
        This method validates the entered map code
        It is called whenever the user enters anything into the map code entry field
         */

        //Validates map code        
        final boolean MapValidated = MapCodeValid(EnteredMapCode);
        //If map code is valid
        if (MapValidated) {
            //Map code validation indicator displays "Valid"
            MapCodeValidationDisplay.setBackground(AssetsColorArray[1]);
            MapCodeValidationDisplay.setText("Valid");
            //Unlocks map code filter button
            FilterMapCodeButton.setBackground(AssetsColorArray[0]);
            AddButtonHoverColorHighlight(FilterMapCodeButton, null);
        } //If map code is invalid
        else {
            //Map code validation indicator displays "Invalid"
            MapCodeValidationDisplay.setBackground(AssetsColorArray[2]);
            MapCodeValidationDisplay.setText("Invalid");
            //Locks map code filter button
            FilterMapCodeButton.setBackground(AssetsColorArray[2]);
            //Stops filtering by map code
            FilterByMapCode = false;
        }
        //Filters leaderboard data
        LeaderboardTableFilterUpdate();
        //Plays key press sound effect
        if (!"Enter map code...".equals(EnteredMapCode)) {
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        }

    }

    private boolean MapCodeValid(final String MapCodeCheck) {
        /*
        This method checks if the map code is valid
        It creates a path based off of map code
        Returns whether or not the path is valid
         */

        //Number of characters within map code
        final int Length = MapCodeCheck.length();
        //Character position within map code = 0
        int CharacterIndexWithinMapCode = 0;
        //String containing all valid positions
        String ValidPositions = "22 ";
        //String containing all invalid positions
        String InvalidPositions = "";
        //Adds position of all edge tiles to list of invalid positions
        InvalidPositions = InvalidPositions + "11 21 31 41 51 61 71 12 72 13 73 14 74 15 75 16 76 17 27 37 47 57 67 77 11 21 31 41 51 61 71 12 72 13 73 14 74 15 75 16 76 17 27 37 47 57 67 77";
        //If map code practically empty
        if (MapCodeCheck.length() < 3) {
            //Returns invalid
            return false;
        } else {
            //If start of map code does not equal start position or end of map code does not equal end position and direction
            if (!MapCodeCheck.substring(0, 2).equals("22") || !MapCodeCheck.substring(Length - 3, Length).equals("66R")) {
                //Returns invalid
                return false;
            } else {
                //Loops until path completed
                while (true) {
                    //Gets column and row of position at character position within map code
                    int Column;
                    int Row;
                    try {
                        Column = Integer.valueOf(MapCodeCheck.substring(CharacterIndexWithinMapCode, CharacterIndexWithinMapCode + 1));
                        Row = Integer.valueOf(MapCodeCheck.substring(CharacterIndexWithinMapCode + 1, CharacterIndexWithinMapCode + 2));
                    } catch (NumberFormatException e) {
                        Column = 1;
                        Row = 1;
                    }
                    //Current position being validated at column and row found
                    final String CurrentPosition = Column + "" + Row;
                    //If current position is valid
                    if (ValidPositions.contains(CurrentPosition) && !InvalidPositions.contains(CurrentPosition)) {
                        //Gets direction of next position at character position within map code
                        final String move = MapCodeCheck.substring(CharacterIndexWithinMapCode + 2, CharacterIndexWithinMapCode + 3);
                        //Position of tile to the right of current position
                        final String Right = (Column + 1) + "" + Row;
                        //Position of tile to the left of current position
                        final String Left = (Column - 1) + "" + Row;
                        //Position of tile above of current position
                        final String Up = Column + "" + (Row - 1);
                        //Position of tile below of current position
                        final String Down = Column + "" + (Row + 1);
                        //If direction of next position is to the right
                        if (move.equals("R")) {
                            //Adds position of tile to the right of current position to valid positions
                            ValidPositions = ValidPositions + (Right) + " ";
                            //Adds all other positions to list of invalid positions
                            InvalidPositions = InvalidPositions + (Left) + " ";
                            InvalidPositions = InvalidPositions + (Up) + " ";
                            InvalidPositions = InvalidPositions + (Down) + " ";
                        } else {
                            //If direction of next position is to the left
                            if (move.equals("L")) {
                                //Adds position of tile to the left of current position to valid positions
                                ValidPositions = ValidPositions + (Left) + " ";
                                //Adds all other positions to list of invalid positions
                                InvalidPositions = InvalidPositions + (Right) + " ";
                                InvalidPositions = InvalidPositions + (Up) + " ";
                                InvalidPositions = InvalidPositions + (Down) + " ";
                            } else {
                                //If direction of next position is above
                                if (move.equals("U")) {
                                    //Adds position of tile above of current position to valid positions
                                    ValidPositions = ValidPositions + (Up);
                                    //Adds all other positions to list of invalid positions
                                    InvalidPositions = InvalidPositions + (Right) + " ";
                                    InvalidPositions = InvalidPositions + (Left) + " ";
                                    InvalidPositions = InvalidPositions + (Down) + " ";
                                } else {
                                    //If direction of next position is below
                                    if (move.equals("D")) {
                                        //Adds position of tile below of current position to valid positions
                                        ValidPositions = ValidPositions + (Down) + " ";
                                        //Adds all other positions to list of invalid positions
                                        InvalidPositions = InvalidPositions + (Right) + " ";
                                        InvalidPositions = InvalidPositions + (Left) + " ";
                                        InvalidPositions = InvalidPositions + (Up) + " ";
                                    } //If direction invalid
                                    else {
                                        //Returns invalid
                                        return false;
                                    }
                                }
                            }
                        }
                    } else {
                        //Returns invalid
                        return false;
                    }
                    //If current position is the end position and at the end of the map code
                    if (CurrentPosition.equals("66") && CharacterIndexWithinMapCode + 3 == Length) {
                        //Returns valid
                        return true;
                    } else {
                        //Adds Current position to list of invalid positions
                        InvalidPositions = InvalidPositions + CurrentPosition + " ";
                        //Increases character position within map code by 3
                        CharacterIndexWithinMapCode = CharacterIndexWithinMapCode + 3;
                    }
                }
            }
        }
    }

    private String FilterByGameMode = "Any";

    private void LeaderboardGameModesFilterButtonSetup() {
        /*
        This method sets the properties of the game mode filter buttons
        It then adds them to the leaderboard panel at the front most position
         */

        //Setup game mode area properties
        final JPanel GameModesArea = new JPanel();
        GameModesArea.setBorder(AssetsBorderArray[0]);
        GameModesArea.setBounds(20, 164, 270, 250);
        GameModesArea.setBackground(AssetsColorArray[0]);
        //Adds game mode area at back most position
        LeaderboardPanel.add(GameModesArea, new Integer(1));

        //Setup game mode area title properties
        final JTextField GameModesAreaTitle = new JTextField();
        GameModesAreaTitle.setBounds(20, 164, 270, 60);
        GameModesAreaTitle.setFont(AssetsFontArray[5]);
        GameModesAreaTitle.setEditable(false);
        GameModesAreaTitle.setText("Filter game mode");
        GameModesAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        GameModesAreaTitle.setOpaque(false);
        GameModesAreaTitle.setBorder(null);
        //Adds game mode area title at front most position
        LeaderboardPanel.add(GameModesAreaTitle, new Integer(2));

        //Setup standard game mode button properties
        final JButton StandardGameModeButton = new JButton();
        StandardGameModeButton.setBounds(40, 224, 230, 50);
        StandardGameModeButton.setFont(AssetsFontArray[1]);
        StandardGameModeButton.setText("Standard");
        StandardGameModeButton.setBackground(AssetsColorArray[0]);
        StandardGameModeButton.setBorder(AssetsBorderArray[0]);
        StandardGameModeButton.setFocusPainted(false);
        //Adds standard game mode button at fronts most position
        LeaderboardPanel.add(StandardGameModeButton, new Integer(2));

        //Setup bounty game mode button properties
        final JButton BountyGameModeButton = new JButton();
        BountyGameModeButton.setBounds(40, 284, 230, 50);
        BountyGameModeButton.setFont(AssetsFontArray[1]);
        BountyGameModeButton.setText("Bounty");
        BountyGameModeButton.setBackground(AssetsColorArray[0]);
        BountyGameModeButton.setBorder(AssetsBorderArray[0]);
        BountyGameModeButton.setFocusPainted(false);
        //Adds bounty game mode button at fronts most position
        LeaderboardPanel.add(BountyGameModeButton, new Integer(2));

        //Setup budget game mode button properties
        final JButton BudgetGameModeButton = new JButton();
        BudgetGameModeButton.setBounds(40, 344, 230, 50);
        BudgetGameModeButton.setFont(AssetsFontArray[1]);
        BudgetGameModeButton.setText("Budget");
        BudgetGameModeButton.setBackground(AssetsColorArray[0]);
        BudgetGameModeButton.setBorder(AssetsBorderArray[0]);
        BudgetGameModeButton.setFocusPainted(false);
        //Adds budget game mode button at fronts most position
        LeaderboardPanel.add(BudgetGameModeButton, new Integer(2));

        //Adds mouse press listener to budget game mode button
        BudgetGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If budget game mode not already filtered
            if (!FilterByGameMode.equals("Budget")) {
                //Filters by budget game mode
                FilterByGameMode = "Budget";
                //Highlights this game mode filter button and unhighlights others
                BudgetGameModeButton.setBackground(AssetsColorArray[1]);
                BountyGameModeButton.setBackground(AssetsColorArray[0]);
                StandardGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(BudgetGameModeButton, null);
                AddButtonHoverColorHighlight(BountyGameModeButton, null);
                AddButtonHoverColorHighlight(StandardGameModeButton, null);
            } //If budget game mode already filtered
            else {
                //Unhighlights this game mode filter button
                BudgetGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(BudgetGameModeButton, null);
                //Filters by any game mode
                FilterByGameMode = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds mouse press listener to bounty game mode button
        BountyGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If bounty game mode not already filtered
            if (!FilterByGameMode.equals("Bounty")) {
                //Filters by bounty game mode
                FilterByGameMode = "Bounty";
                //Highlights this game mode filter button and unhighlights others
                BountyGameModeButton.setBackground(AssetsColorArray[1]);
                BudgetGameModeButton.setBackground(AssetsColorArray[0]);
                StandardGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(BudgetGameModeButton, null);
                AddButtonHoverColorHighlight(BountyGameModeButton, null);
                AddButtonHoverColorHighlight(StandardGameModeButton, null);

            }//If bounty game mode already filtered
            else {
                //Unhighlights this game mode filter button
                BountyGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(BountyGameModeButton, null);
                //Filters by any game mode
                FilterByGameMode = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();

        });
        //Adds mouse press listener to standard game mode button
        StandardGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If standard game mode not already filtered
            if (!FilterByGameMode.equals("Standard")) {
                //Filters by standard game mode
                FilterByGameMode = "Standard";
                //Highlights this game mode filter button and unhighlights others
                StandardGameModeButton.setBackground(AssetsColorArray[1]);
                BudgetGameModeButton.setBackground(AssetsColorArray[0]);
                BountyGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(BudgetGameModeButton, null);
                AddButtonHoverColorHighlight(BountyGameModeButton, null);
                AddButtonHoverColorHighlight(StandardGameModeButton, null);

            } //If standard game mode already filtered
            else {
                //Unhighlights this game mode filter button
                StandardGameModeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(StandardGameModeButton, null);
                //Filters by any game mode
                FilterByGameMode = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds hover listener to game mode filter buttons that highlights them
        AddButtonHoverColorHighlight(StandardGameModeButton, null);
        AddButtonHoverColorHighlight(BountyGameModeButton, null);
        AddButtonHoverColorHighlight(BudgetGameModeButton, null);
    }

    private String FilterByDifficulty = "Any";

    private void LeaderboardDifficultyFilterButtonSetup() {
        /*
        This method sets the properties of the difficulty filter buttons
        It then adds them to the leaderboard panel at the front most position
         */

        //Setup difficulty area properties
        final JPanel DifficultyArea = new JPanel();
        DifficultyArea.setBorder(AssetsBorderArray[0]);
        DifficultyArea.setBounds(20, 424, 270, 250);
        DifficultyArea.setBackground(AssetsColorArray[0]);
        //Adds difficulty area at back most position
        LeaderboardPanel.add(DifficultyArea, new Integer(1));

        //Setup difficulty area title properties
        final JTextField DifficultyAreaTitle = new JTextField();
        DifficultyAreaTitle.setBounds(20, 424, 270, 60);
        DifficultyAreaTitle.setFont(AssetsFontArray[5]);
        DifficultyAreaTitle.setEditable(false);
        DifficultyAreaTitle.setText("Filter difficulty");
        DifficultyAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        DifficultyAreaTitle.setOpaque(false);
        DifficultyAreaTitle.setBorder(null);
        //Adds difficulty area title at front most position
        LeaderboardPanel.add(DifficultyAreaTitle, new Integer(2));

        //Setup easy difficulty button properties
        final JButton EasyDifficultyButton = new JButton();
        EasyDifficultyButton.setBounds(40, 484, 230, 50);
        EasyDifficultyButton.setFont(AssetsFontArray[1]);
        EasyDifficultyButton.setText("Easy");
        EasyDifficultyButton.setBackground(AssetsColorArray[0]);
        EasyDifficultyButton.setBorder(AssetsBorderArray[0]);
        EasyDifficultyButton.setFocusPainted(false);
        //Adds easy difficulty button at front most position
        LeaderboardPanel.add(EasyDifficultyButton, new Integer(2));

        //Setup medium difficulty button properties
        final JButton MediumDifficultyButton = new JButton();
        MediumDifficultyButton.setBounds(40, 544, 230, 50);
        MediumDifficultyButton.setFont(AssetsFontArray[1]);
        MediumDifficultyButton.setText("Medium");
        MediumDifficultyButton.setBackground(AssetsColorArray[0]);
        MediumDifficultyButton.setBorder(AssetsBorderArray[0]);
        MediumDifficultyButton.setFocusPainted(false);
        //Adds medium difficulty button at front most position
        LeaderboardPanel.add(MediumDifficultyButton, new Integer(2));

        //Setup hard difficulty button properties
        final JButton HardDifficultyButton = new JButton();
        HardDifficultyButton.setBounds(40, 604, 230, 50);
        HardDifficultyButton.setFont(AssetsFontArray[1]);
        HardDifficultyButton.setText("Hard");
        HardDifficultyButton.setBackground(AssetsColorArray[0]);
        HardDifficultyButton.setBorder(AssetsBorderArray[0]);
        HardDifficultyButton.setFocusPainted(false);
        //Adds hard difficulty button at front most position
        LeaderboardPanel.add(HardDifficultyButton, new Integer(2));

        //Adds mouse press listener to easy difficulty button
        EasyDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If easy difficulty not already filtered
            if (!FilterByDifficulty.equals("Easy")) {
                //Filters by easy difficulty
                FilterByDifficulty = "Easy";
                //Highlights this difficulty filter button and unhighlights others
                EasyDifficultyButton.setBackground(AssetsColorArray[1]);
                MediumDifficultyButton.setBackground(AssetsColorArray[0]);
                HardDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(EasyDifficultyButton, null);
                AddButtonHoverColorHighlight(MediumDifficultyButton, null);
                AddButtonHoverColorHighlight(HardDifficultyButton, null);
            } //If easy difficulty already filtered
            else {
                //Unhighlights this difficulty filter button
                EasyDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(EasyDifficultyButton, null);
                //Filters by any difficulty
                FilterByDifficulty = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds mouse press listener to medium difficulty button
        MediumDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If medium difficulty not already filtered
            if (!FilterByDifficulty.equals("Medium")) {
                //Filters by medium difficulty
                FilterByDifficulty = "Medium";
                //Highlights this difficulty filter button and unhighlights others
                MediumDifficultyButton.setBackground(AssetsColorArray[1]);
                HardDifficultyButton.setBackground(AssetsColorArray[0]);
                EasyDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(EasyDifficultyButton, null);
                AddButtonHoverColorHighlight(MediumDifficultyButton, null);
                AddButtonHoverColorHighlight(HardDifficultyButton, null);
            } //If medium difficulty already filtered
            else {
                //Unhighlights this difficulty filter button
                MediumDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(MediumDifficultyButton, null);
                //Filters by any difficulty
                FilterByDifficulty = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds mouse press listener to hard difficulty button
        HardDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If hard difficulty not already filtered
            if (!FilterByDifficulty.equals("Hard")) {
                //Filters by hard difficulty
                FilterByDifficulty = "Hard";
                //Highlights this difficulty filter button and unhighlights others
                HardDifficultyButton.setBackground(AssetsColorArray[1]);
                MediumDifficultyButton.setBackground(AssetsColorArray[0]);
                EasyDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(EasyDifficultyButton, null);
                AddButtonHoverColorHighlight(MediumDifficultyButton, null);
                AddButtonHoverColorHighlight(HardDifficultyButton, null);
            } //If hard difficulty already filtered
            else {
                //Unhighlights this difficulty filter button
                HardDifficultyButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(HardDifficultyButton, null);
                //Filters by any difficulty
                FilterByDifficulty = "Any";
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds hover listener to difficulty filter buttons that highlights them
        AddButtonHoverColorHighlight(EasyDifficultyButton, null);
        AddButtonHoverColorHighlight(MediumDifficultyButton, null);
        AddButtonHoverColorHighlight(HardDifficultyButton, null);
    }

    private boolean FilterByMapCode = false;

    private boolean AnyPathLength = true;
    private int PathLength = 17;

    private JComboBox PathLengthFilterDropDown;
    final private JButton FilterMapCodeButton = new JButton();

    private void LeaderboardMapCodeFilterButtonSetup() {
        /*
        This method sets the properties of the map code filter buttons
        It then adds them to the leaderboard panel at the front most position
         */

        //Setup map code area properties
        final JPanel LeaderboardMapCodeArea = new JPanel();
        LeaderboardMapCodeArea.setBorder(AssetsBorderArray[0]);
        LeaderboardMapCodeArea.setBounds(20, 684, 270, 190);
        LeaderboardMapCodeArea.setBackground(AssetsColorArray[0]);
        //Adds map code area at front most position
        LeaderboardPanel.add(LeaderboardMapCodeArea, new Integer(1));

        //Setup map code title properties
        final JTextField LeaderboardMapCodeAreaTitle = new JTextField();
        LeaderboardMapCodeAreaTitle.setBounds(20, 684, 270, 60);
        LeaderboardMapCodeAreaTitle.setFont(AssetsFontArray[5]);
        LeaderboardMapCodeAreaTitle.setEditable(false);
        LeaderboardMapCodeAreaTitle.setText("Filter path");
        LeaderboardMapCodeAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        LeaderboardMapCodeAreaTitle.setOpaque(false);
        LeaderboardMapCodeAreaTitle.setBorder(null);
        //Adds map code title at front most position
        LeaderboardPanel.add(LeaderboardMapCodeAreaTitle, new Integer(2));

        //Setup filter map code button properties
        FilterMapCodeButton.setBounds(40, 744, 230, 50);//40, 804
        FilterMapCodeButton.setFont(AssetsFontArray[1]);
        FilterMapCodeButton.setText("Filter map code");
        FilterMapCodeButton.setBackground(AssetsColorArray[0]);
        FilterMapCodeButton.setBorder(AssetsBorderArray[0]);
        FilterMapCodeButton.setFocusPainted(false);
        //Adds mouse press listener to filter map code button
        FilterMapCodeButton.addActionListener((final ActionEvent ae) -> {
            //If filter map code button is unlocked
            if (FilterMapCodeButton.getBackground() != AssetsColorArray[2]) {
                //If map code is not already filtered
                if (!FilterByMapCode) {
                    //Also filters by same path length as the path length described by the map code
                    PathLengthFilterDropDown.setSelectedItem("Path length: " + ((MapCodeEntryPanel.getText().length() / 3) + 2));
                    //Filters by entered map code
                    FilterByMapCode = true;
                    //Highlights filter map code button button
                    FilterMapCodeButton.setBackground(AssetsColorArray[1]);
                    AddButtonHoverColorHighlight(FilterMapCodeButton, null);
                } //If map code is already filtered
                else {
                    //Does not filter by entered map code
                    FilterByMapCode = false;
                    //Unhighlights filter map code button button
                    FilterMapCodeButton.setBackground(AssetsColorArray[0]);
                    AddButtonHoverColorHighlight(FilterMapCodeButton, null);
                }
                //Filters leaderboard data
                LeaderboardTableFilterUpdate();
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds hover listener to filter map code button that highlights it
        AddButtonHoverColorHighlight(FilterMapCodeButton, null);
        FilterMapCodeButton.setBackground(AssetsColorArray[2]);
        //Adds filter map code button at front most position
        LeaderboardPanel.add(FilterMapCodeButton, new Integer(2));

        //Setup path length filter combo box properties
        final String DropDownOptions[] = {"Path length: Any", "Path length: 11", "Path length: 13", "Path length: 15", "Path length: 17", "Path length: 19"};
        PathLengthFilterDropDown = new JComboBox(DropDownOptions);
        PathLengthFilterDropDown.setBounds(40, 804, 230, 50);
        ((JLabel) PathLengthFilterDropDown.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        PathLengthFilterDropDown.setFont(AssetsFontArray[1]);
        PathLengthFilterDropDown.setBackground(AssetsColorArray[0]);
        PathLengthFilterDropDown.setBorder(AssetsBorderArray[0]);
        PathLengthFilterDropDown.setFocusable(false);
        //Adds selection change listener to path length filter combo box
        PathLengthFilterDropDown.addItemListener((final ItemEvent ie) -> {
            //Gets the selected path length
            String SelectedDropDown = String.valueOf(PathLengthFilterDropDown.getSelectedItem());
            int SelectedDropDownLength = SelectedDropDown.length();
            //If any path length selected
            if (SelectedDropDown.substring(SelectedDropDownLength - 2, SelectedDropDownLength).equals("ny")) {
                //Filters by specific any path length
                AnyPathLength = true;
            } //If specific path length selected
            else {
                //Filters by specific specific path length
                AnyPathLength = false;
                PathLength = (Integer.valueOf(SelectedDropDown.substring(SelectedDropDownLength - 2, SelectedDropDownLength).replaceAll(" ", ""))) - 2;
            }
            //If map code is already filtered
            if (FilterByMapCode) {
                //Does not filter by entered map code
                FilterByMapCode = false;
                //Unhighlights filter map code button button
                FilterMapCodeButton.setBackground(AssetsColorArray[0]);
                AddButtonHoverColorHighlight(FilterMapCodeButton, null);
            }
            //Filters leaderboard data
            LeaderboardTableFilterUpdate();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds the path length filter combo box at front most position
        LeaderboardPanel.add(PathLengthFilterDropDown, new Integer(3));
    }

    private JTable LeaderboardTable = new JTable();
    private DefaultCellEditor DCF;
    private JScrollPane LeaderboardTableSP = new JScrollPane();

    private void LeaderboardTableSetup() {
        /*
        This method sets the properties of the displayed leaderboard
        It then adds it to the leaderboard panel at the front most position
         */

        //Sets the properties of the displayed leaderboard area
        final JPanel LeaderboardArea = new JPanel();
        LeaderboardArea.setBounds(300, 164, 710, 780);
        LeaderboardArea.setBorder(AssetsBorderArray[0]);
        LeaderboardArea.setBackground(AssetsColorArray[0]);
        //Adds the displayed leaderboard area at back most position
        LeaderboardPanel.add(LeaderboardArea, new Integer(0));
        //Sets the properties of the displayed leaderboard cells
        final JTextField Cell = new JTextField();
        Cell.setEditable(false);
        Cell.setFont(AssetsFontArray[3]);
        Cell.setBackground(AssetsColorArray[0]);
        DCF = new DefaultCellEditor(Cell);
        //Adds the displayed leaderboard scroll plane at front most position
        LeaderboardPanel.add(LeaderboardTableSP);
        //Filters leaderboard data
        LeaderboardTableFilterUpdate();
    }

    private final ArrayList<Integer> PlayerFilteredLeaderboardPositions = new ArrayList<>();
    private final JButton FindPlayerScoreButton = new JButton();
    private int PlayerFilteredLeaderboardPositionIndex = 0;

    private void FindPlayerScoreSetup() {
        /*
        This method sets the properties of the find player entries button
        This allows the user to search for entries with their username in the displayed leaderboard
        It then adds it to the leaderboard panel at the front most position
         */

        //Sets the properties of the find player entries button
        FindPlayerScoreButton.setBounds(20, 884, 270, 60);//40, 804
        FindPlayerScoreButton.setFont(AssetsFontArray[1]);
        FindPlayerScoreButton.setText("Find my entry");
        FindPlayerScoreButton.setBackground(AssetsColorArray[0]);
        FindPlayerScoreButton.setBorder(AssetsBorderArray[0]);
        FindPlayerScoreButton.setFocusPainted(false);
        //Adds mouse press listener to find player entries button
        FindPlayerScoreButton.addActionListener((final ActionEvent ae) -> {
            //If entries with the same username as the user are currently on the displayed leaderboard
            if (!PlayerFilteredLeaderboardPositions.isEmpty()) {
                //Selects and anchors to the next entry with matching username
                LeaderboardTable.removeRowSelectionInterval(0, LeaderboardTable.getRowCount() - 1);
                LeaderboardTable.addRowSelectionInterval(PlayerFilteredLeaderboardPositions.get(PlayerFilteredLeaderboardPositionIndex), PlayerFilteredLeaderboardPositions.get(PlayerFilteredLeaderboardPositionIndex));
                LeaderboardTable.scrollRectToVisible(new Rectangle(LeaderboardTable.getCellRect(PlayerFilteredLeaderboardPositions.get(PlayerFilteredLeaderboardPositionIndex), 0, true)));
                PlayerFilteredLeaderboardPositionIndex++;
                if (PlayerFilteredLeaderboardPositionIndex >= PlayerFilteredLeaderboardPositions.size()) {
                    PlayerFilteredLeaderboardPositionIndex = 0;
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds hover listener to find player entries button that highlights it
        AddButtonHoverColorHighlight(FindPlayerScoreButton, null);
        //Adds the find player entries button at front most position
        LeaderboardPanel.add(FindPlayerScoreButton, new Integer(2));
    }

    private void LeaderboardTableFilterUpdate() {
        /*
        This method filters the displayed leaderboard data based on the selected filter options
        It also sets displayed leaderboard table properties
        It is called whenever a filter option changes
         */

        //Clears array containing indexes of leaderboard entries with same username as user
        PlayerFilteredLeaderboardPositionIndex = 0;
        PlayerFilteredLeaderboardPositions.clear();
        //Leaderboard table column names
        final String[] LeaderboardTableColumnNames = {"Username", "Score", "Game mode", "Difficulty", "Map code"};
        //Gets all leaderboard entries
        final ArrayList<LeaderboardTableEntry> AllLeaderboardEntriesArray = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getLeaderboardEntriesArray();
        final ArrayList<LeaderboardTableEntry> AllLeaderboardEntriesArrayFiltered = new ArrayList<>();
        //Loops through all leaderboard entries
        for (int AllLeaderboardEntriesArrayIndex = 0; AllLeaderboardEntriesArrayIndex < AllLeaderboardEntriesArray.size(); AllLeaderboardEntriesArrayIndex++) {
            boolean ValidLeaderboardEntry = true;
            //Filters by game mode
            if (!FilterByGameMode.equals("Any")) {
                if (!AllLeaderboardEntriesArray.get(AllLeaderboardEntriesArrayIndex).getGameMode().equals(FilterByGameMode)) {
                    ValidLeaderboardEntry = false;
                }
            }
            //Filters by difficulty
            if (!FilterByDifficulty.equals("Any") && ValidLeaderboardEntry) {
                if (!AllLeaderboardEntriesArray.get(AllLeaderboardEntriesArrayIndex).getDifficulty().equals(FilterByDifficulty)) {
                    ValidLeaderboardEntry = false;
                }
            }
            //Filters by map code
            if (FilterByMapCode) {
                if (!AllLeaderboardEntriesArray.get(AllLeaderboardEntriesArrayIndex).getMapCode().equals(MapCodeEntryPanel.getText())) {
                    ValidLeaderboardEntry = false;
                }
            }
            //Filters by path length
            if (!AnyPathLength) {
                if (PathLength != (AllLeaderboardEntriesArray.get(AllLeaderboardEntriesArrayIndex).getMapCode().length() / 3)) {
                    ValidLeaderboardEntry = false;
                }
            }
            //If leaderboard entry meets filter requirements
            if (ValidLeaderboardEntry) {
                //Adds it to array of all filtered leaderboard entries
                AllLeaderboardEntriesArrayFiltered.add(AllLeaderboardEntriesArray.get(AllLeaderboardEntriesArrayIndex));
            }
        }
        //Populates displayed leaderboard table
        final String[][] LeaderboardTableData = new String[AllLeaderboardEntriesArrayFiltered.size()][5];
        for (int AllLeaderboardEntriesArrayFilteredIndex = 0; AllLeaderboardEntriesArrayFilteredIndex < AllLeaderboardEntriesArrayFiltered.size(); AllLeaderboardEntriesArrayFilteredIndex++) {
            LeaderboardTableData[AllLeaderboardEntriesArrayFilteredIndex][0] = AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getUsername();
            LeaderboardTableData[AllLeaderboardEntriesArrayFilteredIndex][1] = String.valueOf(AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getScore());
            LeaderboardTableData[AllLeaderboardEntriesArrayFilteredIndex][2] = AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getGameMode();
            LeaderboardTableData[AllLeaderboardEntriesArrayFilteredIndex][3] = AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getDifficulty();
            LeaderboardTableData[AllLeaderboardEntriesArrayFilteredIndex][4] = AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getMapCode();
            if (AllLeaderboardEntriesArrayFiltered.get(AllLeaderboardEntriesArrayFilteredIndex).getUsername().equals(Shape_Shooter_TD.MainClass.getUsername())) {
                PlayerFilteredLeaderboardPositions.add(AllLeaderboardEntriesArrayFilteredIndex);
            }
        }
        //Sets displayed leaderboard table properties
        LeaderboardTable = new JTable(LeaderboardTableData, LeaderboardTableColumnNames);
        LeaderboardTable.setDefaultEditor(Object.class, DCF);
        LeaderboardTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        LeaderboardTable.setFont(AssetsFontArray[3]);
        LeaderboardTable.setBackground(AssetsColorArray[0]);
        LeaderboardTable.getTableHeader().setFont(AssetsFontArray[1]);
        LeaderboardTable.getTableHeader().setBackground(AssetsColorArray[0]);
        LeaderboardPanel.remove(LeaderboardTableSP);
        LeaderboardTableSP = new JScrollPane(LeaderboardTable);
        LeaderboardTableSP.setBounds(305, 169, 700, 770);
        LeaderboardTableSP.getViewport().setBackground(AssetsColorArray[0]);
        LeaderboardTableSP.setBorder(AssetsBorderArray[3]);
        LeaderboardPanel.add(LeaderboardTableSP, new Integer(1));
        //Updates find player score button
        if (PlayerFilteredLeaderboardPositions.isEmpty()) {
            FindPlayerScoreButton.setBackground(AssetsColorArray[2]);
            FindPlayerScoreButton.setText("No entries");
        } else {
            FindPlayerScoreButton.setBackground(AssetsColorArray[0]);
            if (PlayerFilteredLeaderboardPositions.size() == 1) {
                FindPlayerScoreButton.setText("Find my entry");
            } else {
                FindPlayerScoreButton.setText("Find my entries");
            }
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties the leaderboard panel
        It then adds it to the main panel
         */

        //Sets properties of leaderboard panel
        LeaderboardPanel.setBounds(0, 0, 1030, 964);
        //Creates header and adds it to leaderboard panel
        LeaderboardPanel.add(WindowHeaderSetup(), new Integer(2));
        //Adds the leaderboard panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(LeaderboardPanel, new Integer(1));
    }

}
