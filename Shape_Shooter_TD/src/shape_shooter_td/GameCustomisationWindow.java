package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class GameCustomisationWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    private String GameMode;
    private boolean GameModeSelected = false;

    private double Difficulty;
    private boolean DifficultySelected = false;

    private String MapCode;
    private boolean MapCodeSelected = false;

    private boolean AnyPathLength = true;
    private int PathLength = 17;

    private final boolean TutorialActive;

    public GameCustomisationWindow(final boolean IsTutorialActive) {
        TutorialActive = IsTutorialActive;
    }

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the game customisation panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(0);
        //Opens game customisation panel
        GameCustomisationWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Game customisation panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(GameCustomisationPanel);
    }

    private final JLayeredPane GameCustomisationPanel = new JLayeredPane();

    private void GameCustomisationWindowSetup() {
        /*
        This method opens the game customisation panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //If tutorial messages should be displayed
        if (TutorialActive) {
            //Setup tutorial area components 
            TutorialAreaSetup();
        }
        //Setup game mode buttons
        GameModesButtonSetup();
        //Setup difficulty buttons
        DifficultyButtonSetup();
        //Setup map code generator buttons
        MapCodeGeneratorButtonsSetup();
        //Setup play button
        PlayButtonSetup();
        //Setup question mark image
        QuestionMarkImageSetup();
        //Setup map code entry text field and validation indicator
        MapCodeEntrySetup();
        //Creates 7 by 7 grid of standard tiles as well as start and end tiles
        GridSetup();
        //Sets tiles on edge of grid as edge
        EdgeDetection();
        //Setup grid and displays tiles
        AddGrid();
        //Setup game customisation panel 
        FinalSetup();
    }

    private void GameModesButtonSetup() {
        /*
        This method sets the properties of the game mode buttons
        It then adds them to the game customisation panel at the front most position
         */

        //Setup game mode area properties
        final JPanel GameModesArea = new JPanel();
        GameModesArea.setBorder(AssetsBorderArray[0]);
        GameModesArea.setBounds(20, 164, 270, 250);
        GameModesArea.setBackground(AssetsColorArray[0]);
        //Adds game mode area at back most position
        GameCustomisationPanel.add(GameModesArea, new Integer(1));
        //Setup game mode area title properties
        final JTextField GameModesAreaTitle = new JTextField();
        GameModesAreaTitle.setBounds(20, 164, 270, 60);
        GameModesAreaTitle.setFont(AssetsFontArray[5]);
        GameModesAreaTitle.setEditable(false);
        GameModesAreaTitle.setText("Select game mode");
        GameModesAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        GameModesAreaTitle.setOpaque(false);
        GameModesAreaTitle.setBorder(null);
        //Adds game mode area title at front most position
        GameCustomisationPanel.add(GameModesAreaTitle, new Integer(2));
        //Setup standard game mode button properties
        final JButton StandardGameModeButton = new JButton();
        StandardGameModeButton.setBounds(40, 224, 230, 50);
        StandardGameModeButton.setFont(AssetsFontArray[1]);
        StandardGameModeButton.setText("Standard");
        StandardGameModeButton.setBackground(AssetsColorArray[0]);
        StandardGameModeButton.setBorder(AssetsBorderArray[0]);
        StandardGameModeButton.setFocusPainted(false);
        //Adds standard game mode button at fronts most position
        GameCustomisationPanel.add(StandardGameModeButton, new Integer(2));
        //Setup bounty game mode button properties
        final JButton BountyGameModeButton = new JButton();
        BountyGameModeButton.setBounds(40, 284, 230, 50);
        BountyGameModeButton.setFont(AssetsFontArray[1]);
        BountyGameModeButton.setText("Bounty");
        BountyGameModeButton.setBackground(AssetsColorArray[0]);
        BountyGameModeButton.setBorder(AssetsBorderArray[0]);
        BountyGameModeButton.setFocusPainted(false);
        //Adds bounty game mode button at fronts most position
        GameCustomisationPanel.add(BountyGameModeButton, new Integer(2));
        //Setup budget game mode button properties
        final JButton BudgetGameModeButton = new JButton();
        BudgetGameModeButton.setBounds(40, 344, 230, 50);
        BudgetGameModeButton.setFont(AssetsFontArray[1]);
        BudgetGameModeButton.setText("Budget");
        BudgetGameModeButton.setBackground(AssetsColorArray[0]);
        BudgetGameModeButton.setBorder(AssetsBorderArray[0]);
        BudgetGameModeButton.setFocusPainted(false);
        //Adds budget game mode button at fronts most position
        GameCustomisationPanel.add(BudgetGameModeButton, new Integer(2));
        //Adds mouse press listener to budget game mode button
        BudgetGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If the game mode is not already set to budget
            if (!"Budget".equals(GameMode)) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Game mode set to budget
            GameMode = "Budget";
            //Game mode has been selected
            GameModeSelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights budget game mode button and unhighlights other game mode buttons
            BudgetGameModeButton.setBackground(AssetsColorArray[1]);
            BountyGameModeButton.setBackground(AssetsColorArray[0]);
            StandardGameModeButton.setBackground(AssetsColorArray[0]);
        });
        //Adds mouse press listener to bounty game mode button
        BountyGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If the game mode is not already set to bounty
            if (!"Bounty".equals(GameMode)) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Game mode set to bounty
            GameMode = "Bounty";
            //Game mode has been selected
            GameModeSelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights bounty game mode button and unhighlights other game mode buttons
            BountyGameModeButton.setBackground(AssetsColorArray[1]);
            BudgetGameModeButton.setBackground(AssetsColorArray[0]);
            StandardGameModeButton.setBackground(AssetsColorArray[0]);
        });
        //Adds mouse press listener to standard game mode button
        StandardGameModeButton.addActionListener((final ActionEvent ae) -> {
            //If the game mode is not already set to standard
            if (!"Standard".equals(GameMode)) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Game mode set to standard
            GameMode = "Standard";
            //Game mode has been selected
            GameModeSelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights standard game mode button and unhighlights other game mode buttons
            StandardGameModeButton.setBackground(AssetsColorArray[1]);
            BudgetGameModeButton.setBackground(AssetsColorArray[0]);
            BountyGameModeButton.setBackground(AssetsColorArray[0]);
        });
        //Adds hover listener to game mode buttons that highlights them
        AddButtonHoverColorHighlight(StandardGameModeButton, null);
        AddButtonHoverColorHighlight(BountyGameModeButton, null);
        AddButtonHoverColorHighlight(BudgetGameModeButton, null);
    }

    private void DifficultyButtonSetup() {
        /*
        This method sets the properties of the game mode buttons
        It then adds them to the game customisation panel at the front most position
         */

        //Setup difficulty area properties
        final JPanel DifficultyArea = new JPanel();
        DifficultyArea.setBorder(AssetsBorderArray[0]);
        DifficultyArea.setBounds(20, 424, 270, 250);
        DifficultyArea.setBackground(AssetsColorArray[0]);
        //Adds difficulty area at back most position
        GameCustomisationPanel.add(DifficultyArea, new Integer(1));
        //Setup difficulty area title properties
        final JTextField DifficultyAreaTitle = new JTextField();
        DifficultyAreaTitle.setBounds(20, 424, 270, 60);
        DifficultyAreaTitle.setFont(AssetsFontArray[5]);
        DifficultyAreaTitle.setEditable(false);
        DifficultyAreaTitle.setText("Select difficulty");
        DifficultyAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        DifficultyAreaTitle.setOpaque(false);
        DifficultyAreaTitle.setBorder(null);
        //Adds difficulty area title at front most position
        GameCustomisationPanel.add(DifficultyAreaTitle, new Integer(2));
        //Setup easy difficulty button properties
        final JButton EasyDifficultyButton = new JButton();
        EasyDifficultyButton.setBounds(40, 484, 230, 50);
        EasyDifficultyButton.setFont(AssetsFontArray[1]);
        EasyDifficultyButton.setText("Easy");
        EasyDifficultyButton.setBackground(AssetsColorArray[0]);
        EasyDifficultyButton.setBorder(AssetsBorderArray[0]);
        EasyDifficultyButton.setFocusPainted(false);
        //Adds easy difficulty button at front most position
        GameCustomisationPanel.add(EasyDifficultyButton, new Integer(2));
        //Setup medium difficulty button properties
        final JButton MediumDifficultyButton = new JButton();
        MediumDifficultyButton.setBounds(40, 544, 230, 50);
        MediumDifficultyButton.setFont(AssetsFontArray[1]);
        MediumDifficultyButton.setText("Medium");
        MediumDifficultyButton.setBackground(AssetsColorArray[0]);
        MediumDifficultyButton.setBorder(AssetsBorderArray[0]);
        MediumDifficultyButton.setFocusPainted(false);
        //Adds medium difficulty button at front most position
        GameCustomisationPanel.add(MediumDifficultyButton, new Integer(2));
        //Setup hard difficulty button properties
        final JButton HardDifficultyButton = new JButton();
        HardDifficultyButton.setBounds(40, 604, 230, 50);
        HardDifficultyButton.setFont(AssetsFontArray[1]);
        HardDifficultyButton.setText("Hard");
        HardDifficultyButton.setBackground(AssetsColorArray[0]);
        HardDifficultyButton.setBorder(AssetsBorderArray[0]);
        HardDifficultyButton.setFocusPainted(false);
        //Adds hard difficulty button at front most position
        GameCustomisationPanel.add(HardDifficultyButton, new Integer(2));
        //Adds mouse press listener to easy difficulty button
        EasyDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If the difficulty is not already set to easy
            if (Difficulty != 1) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Difficulty set to easy
            Difficulty = 1;
            //Difficulty is selected
            DifficultySelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights easy difficulty button and unhighlights other difficulty buttons
            EasyDifficultyButton.setBackground(AssetsColorArray[1]);
            MediumDifficultyButton.setBackground(AssetsColorArray[0]);
            HardDifficultyButton.setBackground(AssetsColorArray[0]);
        });
        //Adds mouse press listener to medium difficulty button
        MediumDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If the difficulty is not already set to medium
            if (Difficulty != 2) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Difficulty set to medium
            Difficulty = 2;
            //Difficulty is selected
            DifficultySelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights medium difficulty button and unhighlights other difficulty buttons
            MediumDifficultyButton.setBackground(AssetsColorArray[1]);
            HardDifficultyButton.setBackground(AssetsColorArray[0]);
            EasyDifficultyButton.setBackground(AssetsColorArray[0]);
        });
        //Adds mouse press listener to hard difficulty button
        HardDifficultyButton.addActionListener((final ActionEvent ae) -> {
            //If the difficulty is not already set to hard
            if (Difficulty != 3) {
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
            //Difficulty set to hard
            Difficulty = 3;
            //Difficulty is selected
            DifficultySelected = true;
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
            //Highlights hard difficulty button and unhighlights other difficulty buttons
            HardDifficultyButton.setBackground(AssetsColorArray[1]);
            MediumDifficultyButton.setBackground(AssetsColorArray[0]);
            EasyDifficultyButton.setBackground(AssetsColorArray[0]);
        });
        //Adds hover listener to difficulty buttons that highlights them
        AddButtonHoverColorHighlight(EasyDifficultyButton, null);
        AddButtonHoverColorHighlight(MediumDifficultyButton, null);
        AddButtonHoverColorHighlight(HardDifficultyButton, null);

    }

    private void MapCodeGeneratorButtonsSetup() {
        /*
        This method sets the properties of the map code generator buttons
        It then adds them to the game customisation panel at the front most position
         */

        //Setup map code generator area properties
        final JPanel MapCodeGeneratorArea = new JPanel();
        MapCodeGeneratorArea.setBorder(AssetsBorderArray[0]);
        MapCodeGeneratorArea.setBounds(20, 684, 270, 190);
        MapCodeGeneratorArea.setBackground(AssetsColorArray[0]);
        //Adds map code generator area at back most position
        GameCustomisationPanel.add(MapCodeGeneratorArea, new Integer(1));
        //Setup map code generator area title properties
        final JTextField MapCodeGeneratorAreaTitle = new JTextField();
        MapCodeGeneratorAreaTitle.setBounds(20, 684, 270, 60);
        MapCodeGeneratorAreaTitle.setFont(AssetsFontArray[5]);
        MapCodeGeneratorAreaTitle.setEditable(false);
        MapCodeGeneratorAreaTitle.setText("Create path");
        MapCodeGeneratorAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        MapCodeGeneratorAreaTitle.setOpaque(false);
        MapCodeGeneratorAreaTitle.setBorder(null);
        //Adds map code generator area title at front most position
        GameCustomisationPanel.add(MapCodeGeneratorAreaTitle, new Integer(2));
        //Sets path length combo box properties
        final String DropDownOptions[] = {"Path length: Any", "Path length: 11", "Path length: 13", "Path length: 15", "Path length: 17", "Path length: 19"};
        final JComboBox PathLengthDropDown = new JComboBox(DropDownOptions);
        PathLengthDropDown.setBounds(40, 744, 230, 50);
        ((JLabel) PathLengthDropDown.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        PathLengthDropDown.setFont(AssetsFontArray[1]);
        PathLengthDropDown.setBackground(AssetsColorArray[0]);
        PathLengthDropDown.setBorder(AssetsBorderArray[0]);
        PathLengthDropDown.setFocusable(false);
        //Adds selection change listener to path length combo box
        PathLengthDropDown.addItemListener((ItemEvent ie) -> {
            //Gets the selected path length
            final String SelectedDropDown = String.valueOf(PathLengthDropDown.getSelectedItem());
            int SelectedDropDownLength = SelectedDropDown.length();
            //If any path length selected
            if (SelectedDropDown.substring(SelectedDropDownLength - 2, SelectedDropDownLength).equals("ny")) {
                //Sets path length to any
                AnyPathLength = true;
            }//If specific path length selected
            else {
                //Sets path length to specific length
                AnyPathLength = false;
                PathLength = (Integer.valueOf(SelectedDropDown.substring(SelectedDropDownLength - 2, SelectedDropDownLength).replaceAll(" ", ""))) - 2;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds path length combo box at front most position
        GameCustomisationPanel.add(PathLengthDropDown, new Integer(3));
        //Sets path generator button properties
        final JButton GeneratePathButton = new JButton();
        GeneratePathButton.setBounds(40, 804, 130, 50);
        GeneratePathButton.setFont(AssetsFontArray[1]);
        GeneratePathButton.setText("Generate");
        GeneratePathButton.setBackground(AssetsColorArray[0]);
        GeneratePathButton.setBorder(AssetsBorderArray[0]);
        GeneratePathButton.setFocusPainted(false);
        //Adds mouse press listener to path length combo box
        GeneratePathButton.addActionListener((final ActionEvent ae) -> {
            //Stops the path from being drawn
            PathBeingDrawn = false;
            try {
                //Generates a valid map code
                MapCodeGenerator();
            } catch (Exception ex) {
                System.out.println("Map code generator error: " + ex);
                //Generates a valid map code
                MapCodeGenerator();
            }
            //Displays the generated map code
            MapCodeEntryPanel.setForeground(AssetsColorArray[3]);
            MapCodeEntryPanel.setText(MapCode);
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds hover listener to path generator button that highlights it
        AddButtonHoverColorHighlight(GeneratePathButton, null);
        //Adds path generator button at front most position
        GameCustomisationPanel.add(GeneratePathButton, new Integer(2));
        //Sets draw path button properties
        final JButton DrawPathButton = new JButton();
        DrawPathButton.setBounds(180, 804, 90, 50);
        DrawPathButton.setFont(AssetsFontArray[1]);
        DrawPathButton.setText("Draw");
        DrawPathButton.setBackground(AssetsColorArray[0]);
        DrawPathButton.setBorder(AssetsBorderArray[0]);
        DrawPathButton.setFocusPainted(false);
        //Adds mouse press listener to draw path button
        DrawPathButton.addActionListener((final ActionEvent ae) -> {
            //Starts drawing path
            DrawPath();
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        });
        //Adds hover listener to draw path button that highlights it
        AddButtonHoverColorHighlight(DrawPathButton, null);
        //Adds draw path button at front most position
        GameCustomisationPanel.add(DrawPathButton, new Integer(2));
    }

    private final JButton PlayButton = new JButton();

    private void PlayButtonSetup() {
        /*
        This method sets the properties of the play button
                It then adds them to the account login panel
         */

        //Sets properties of play button
        PlayButton.setBorder(AssetsBorderArray[0]);
        PlayButton.setBounds(20, 884, 990, 60);
        PlayButton.setBackground(AssetsColorArray[0]);
        PlayButton.setFont(AssetsFontArray[1]);
        PlayButton.setText("Play");
        PlayButton.setFocusPainted(false);
        //Adds mouse press listener to play button
        PlayButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If parameters are selected
                if (AllGameParametersSelected) {
                    PlayButton.removeActionListener(PlayButton.getActionListeners()[0]);
                    PlayButton.setBackground(AssetsColorArray[1]);
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Partial transition to game panel
                    Shape_Shooter_TD.MainClass.StartGame(Difficulty, MapCode, GameMode, TutorialActive, 1);
                }
            }
        });
        //Adds hover listener to play button that highlights it
        AddButtonHoverColorHighlight(PlayButton, null);
        PlayButton.setBackground(AssetsColorArray[2]);
        //Adds play button at front most position
        GameCustomisationPanel.add(PlayButton, new Integer(2));
    }

    private boolean AllGameParametersSelected = false;

    private void PlayButtonUpdate() {
        /*
        This method checks if game mode, difficulty, and map code have been selected
        It also prevents the user from starting a game until all parameters are selected
         */

        //If game mode, difficulty, and map code have been selected
        if (GameModeSelected && DifficultySelected && MapCodeSelected) {
            //Unlocks play button
            AllGameParametersSelected = true;
            PlayButton.setBackground(AssetsColorArray[0]);
        } //If game mode, difficulty, and map code have not all been selected
        else {
            //Locks play button
            AllGameParametersSelected = false;
            PlayButton.setBackground(AssetsColorArray[2]);
        }
    }

    private final JTextField MapCodeEntryPanel = new JTextField();
    private final JTextField MapCodeValidationDisplay = new JTextField();

    private void MapCodeEntrySetup() {
        /*
        This method sets the properties of the map code entry text field and the map code validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the game customisation panel
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
            public void insertUpdate(DocumentEvent de) {
                //Checks validation of entered map code
                MapCodeEntryUpdate(MapCodeEntryPanel.getText());
            }

            //When a character is removed from the map code entry text field
            @Override
            public void removeUpdate(DocumentEvent de) {
                //Checks validation of entered map code
                MapCodeEntryUpdate(MapCodeEntryPanel.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            }
        });
        //Adds focus listener to the map code entry text field
        MapCodeEntryPanel.addFocusListener(new FocusListener() {
            //When the map code entry text field gains focus
            @Override
            public void focusGained(FocusEvent focusEvent) {
                //Stops the path from being drawn
                PathBeingDrawn = false;
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
        GameCustomisationPanel.add(MapCodeEntryPanel, new Integer(1));
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
        GameCustomisationPanel.add(MapCodeValidationDisplay, new Integer(2));
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
            //Clears grid of previous path
            for (int i = 0; i < TilesArray.size(); i++) {
                if (TilesArray.get(i).getIsPath()) {
                    TilesArray.get(i).getButton().setIcon(AssetsTileIconArray[2]);
                    TilesArray.get(i).setIsPath(false);
                }
            }
            //Adds path to grid
            GridAddPath(EnteredMapCode);
            //Hides question mark image
            QuestionMarkImage.setVisible(false);
            //Map code is valid
            MapCode = EnteredMapCode;
            //Map code is selected
            MapCodeSelected = true;
            MapCodeValidationDisplay.setBackground(AssetsColorArray[1]);
            //Map code validation indicator displays "Valid"
            MapCodeValidationDisplay.setText("Valid");
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
        } //If map code is invalid
        else {
            //If the path is not being drawn
            if (!PathBeingDrawn) {
                //Shows question mark image
                QuestionMarkImage.setVisible(true);
            }
            //Map code is not selected
            MapCodeSelected = false;
            MapCodeValidationDisplay.setBackground(AssetsColorArray[2]);
            //Map code validation indicator displays "Invalid"
            MapCodeValidationDisplay.setText("Invalid");
            //Checks if game mode, difficulty, and map code have been selected
            PlayButtonUpdate();
        }
        //Loops through all highlighted tiles
        while (HighlightedTiles.size() > 0) {
            //Removes previous action listener from tile if it had one
            if (HighlightedTiles.get(0).getButton().getMouseListeners().length > 0) {
                HighlightedTiles.get(0).getButton().removeActionListener(HighlightedTiles.get(0).getButton().getActionListeners()[0]);
            }
            //Unhighlight tile
            HighlightedTiles.get(0).getButton().setBorder(null);
            HighlightedTiles.remove(HighlightedTiles.get(0));
        }
        //Plays key press sound effect
        if (!"Enter map code...".equals(EnteredMapCode)) {
            Thread T1 = new Thread(() -> {
                setVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        }
    }

    private String MapCodeDraw = "";
    private final ArrayList<Tiles> HighlightedTiles = new ArrayList<>();
    private Tiles PreviousDrawTile = null;
    private boolean PathBeingDrawn = false;

    private void DrawPath() {
        /*
        This method allows the user to draw a path
        It highlights selectable tiles in a way that only allows for a valid path to be created
         */

        MapCodeDraw = "";
        PathBeingDrawn = true;
        //Loops through all tiles in grid and resets them to default icons
        for (int TilesArrayIndex = 0; TilesArrayIndex < TilesArray.size(); TilesArrayIndex++) {
            if (TilesArray.get(TilesArrayIndex).getPosition().equals("12")) {
                TilesArray.get(TilesArrayIndex).getButton().setIcon(AssetsTileIconArray[1]);
            } else {
                if (TilesArray.get(TilesArrayIndex).getPosition().equals("76")) {
                    TilesArray.get(TilesArrayIndex).getButton().setIcon(AssetsTileIconArray[0]);
                } else {
                    TilesArray.get(TilesArrayIndex).getButton().setIcon(AssetsTileIconArray[2]);
                }
            }
        }
        //Removes question mark image and clears the entered map code
        QuestionMarkImage.setVisible(false);
        MapCodeDraw = "22";
        MapCodeEntryPanel.setText("Enter map code...");
        MapCodeEntryPanel.setForeground(AssetsColorArray[8]);
        //Loops through all tiles in grid
        for (int TilesArrayIndex = 0; TilesArrayIndex < TilesArray.size(); TilesArrayIndex++) {
            //If start tile
            if (TilesArray.get(TilesArrayIndex).getPosition().equals("22")) {
                //Sets it as the previous tile
                PreviousDrawTile = TilesArray.get(TilesArrayIndex);
                //Highlights tile and adds action listener to it
                AddHighlightAndListener(TilesArray.get(TilesArrayIndex));
                //Ends loop
                TilesArrayIndex = TilesArray.size() + 1;
            }
        }
    }

    private void AddHighlightAndListener(Tiles CurrentTile) {
        //Highlights tile and adds to array of all highlighted tiles
        CurrentTile.getButton().setBorder(AssetsBorderArray[2]);
        HighlightedTiles.add(CurrentTile);
        //Adds mouse press listener to tile
        CurrentTile.getButton().addActionListener((final ActionEvent ae) -> {
            //Gets column and row of previous tile position
            int Column;
            int Row;
            try {
                Column = Integer.valueOf(PreviousDrawTile.getPosition().substring(0, 1));
                Row = Integer.valueOf(PreviousDrawTile.getPosition().substring(1, 2));
            } catch (NumberFormatException ex) {
                Column = 1;
                Row = 1;
            }
            //Sets the tile as part of the path
            CurrentTile.setIsPath(true);

            //Position of tile to the right of current position
            final String Right = (Column + 1) + "" + Row;
            //Position of tile to the Left  of current position
            final String Left = (Column - 1) + "" + Row;
            //Position of tile above of current position
            final String Up = Column + "" + (Row - 1);
            //Position of tile below of current position
            final String Down = Column + "" + (Row + 1);

            String Move = "R";
            //If current tile is on the right of previous tile
            if (Right.equals(CurrentTile.getPosition())) {
                //Next move is right
                MapCodeDraw = MapCodeDraw + "R" + Right;
                Move = "R";
            }
            //If current tile is on the left of previous tile
            if (Left.equals(CurrentTile.getPosition())) {
                MapCodeDraw = MapCodeDraw + "L" + Left;
                //Next move is left
                Move = "L";
            }
            //If current tile is above previous tile
            if (Up.equals(CurrentTile.getPosition())) {
                MapCodeDraw = MapCodeDraw + "U" + Up;
                //Next move is up
                Move = "U";
            }
            //If current tile is below previous tile
            if (Down.equals(CurrentTile.getPosition())) {
                MapCodeDraw = MapCodeDraw + "D" + Down;
                //Next move is down
                Move = "D";
            }
            //Changes current tile icon to match the direction moved
            switch (Move) {
                case ("R"):
                    CurrentTile.getButton().setIcon(AssetsTileIconArray[9]);
                    break;
                case ("L"):
                    CurrentTile.getButton().setIcon(AssetsTileIconArray[10]);
                    break;
                case ("U"):
                    CurrentTile.getButton().setIcon(AssetsTileIconArray[11]);
                    break;
                case ("D"):
                    CurrentTile.getButton().setIcon(AssetsTileIconArray[12]);
                    break;
                default:
                    CurrentTile.getButton().setIcon(AssetsTileIconArray[2]);
                    break;
            }
            //Finds complete change in direction from previous tile
            String PreviousFullMove;
            if (MapCodeDraw.length() > 2) {
                PreviousFullMove = "R" + Move;
                if (MapCodeDraw.length() > 6) {
                    PreviousFullMove = (MapCodeDraw.substring(MapCodeDraw.length() - 6, MapCodeDraw.length() - 5)) + Move;
                }
                //Changes previous tile icon to match the direction moved
                switch (PreviousFullMove) {
                    case ("RR"):
                    case ("LL"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[3]);
                        break;
                    case ("DD"):
                    case ("UU"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[4]);
                        break;
                    case ("RD"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[5]);
                        break;
                    case ("RU"):
                    case ("DL"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[6]);
                        break;
                    case ("UR"):
                    case ("LD"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[7]);
                        break;
                    case ("DR"):
                        PreviousDrawTile.getButton().setIcon(AssetsTileIconArray[8]);
                        break;
                    default:
                        if (CurrentTile.getPosition().equals("22")) {
                            switch (Move) {
                                case ("R"):
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[3]);
                                    break;
                                case ("L"):
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[5]);
                                    break;
                                default:
                                    CurrentTile.getButton().setIcon(AssetsTileIconArray[2]);
                                    break;
                            }
                        } else {
                            CurrentTile.getButton().setIcon(AssetsTileIconArray[2]);
                            break;
                        }
                        break;
                }
            }
            //Sets previous tile as current tile
            PreviousDrawTile = CurrentTile;
            //Displays current map code described by drawn path
            MapCodeEntryPanel.setText(MapCodeDraw);
            MapCodeEntryPanel.setForeground(AssetsColorArray[3]);
            //If current position is end position
            if (CurrentTile.getPosition().equals("66")) {
                //Completes path and map code
                MapCodeDraw = MapCodeDraw + "R";
                MapCodeEntryPanel.setText(MapCodeDraw);
                MapCodeEntryPanel.setForeground(AssetsColorArray[3]);
            }//If current position is not end position
            else {
                //Finds possible moves from current tile that allows for a valid path
                final ArrayList<String> PossibleDrawPathPositions = FindPossibleDrawPathPositions(MapCodeDraw);
                int TilesFound = 0;
                //Loops though all tiles
                for (int TilesArrayIndex = 0; TilesArrayIndex < TilesArray.size(); TilesArrayIndex++) {
                    //If the tile is one of the possible next moves
                    if (PossibleDrawPathPositions.contains(TilesArray.get(TilesArrayIndex).getPosition())) {
                        //Highlights tile and recurses this method with said tile 
                        TilesArray.get(TilesArrayIndex).getButton().setBorder(AssetsBorderArray[2]);
                        AddHighlightAndListener(TilesArray.get(TilesArrayIndex));
                        TilesFound++;
                    }
                    //If all possible next moves tiles found
                    if (TilesFound >= PossibleDrawPathPositions.size()) {
                        //Ends loop
                        TilesArrayIndex = TilesArray.size() + 1;
                    }
                }
            }
        });
    }

    private ArrayList<String> FindPossibleDrawPathPositions(final String PartialMapCode) {
        /*
        This method Finds the possible moves from current tile that allows for a valid path
        It returns an array of positions of  tiles that can be moved to
         */

        //List of all potential map codes
        final String AllMapCodes[] = new String[]{
            "22D23R33R43D44D45D46R56R66R",
            "22D23D24R34R44U43U42R52R62D63D64D65D66R",
            "22R32R42R52D53D54R64D65D66R",
            "22R32R42D43D44R54R64D65D66R",
            "22D23D24R34D35R45R55R65D66R",
            "22R32D33R43R53D54R64D65D66R",
            "22R32R42R52D53D54D55R65D66R",
            "22R32R42R52R62D63D64D65D66R",
            "22D23D24R34R44R54R64D65D66R",
            "22D23R33R43R53D54R64D65D66R",
            "22R32R42D43D44D45R55R65D66R",
            "22R32R42R52R62D63D64L54L44L34L24D25D26R36R46R56R66R",
            "22R32D33R43R53R63D64D65D66R",
            "22R32D33D34D35R45R55D56R66R",
            "22D23D24R34D35D36R46R56R66R",
            "22R32D33D34R44D45R55D56R66R",
            "22D23D24D25R35R45D46R56R66R",
            "22R32R42R52D53R63D64D65D66R",
            "22D23D24D25R35D36R46R56R66R",
            "22D23D24D25D26R36R46U45U44U43U42R52R62D63D64D65D66R",
            "22R32D33D34D35D36R46R56R66R",
            "22D23D24D25D26R36R46R56R66R",
            "22D23R33D34D35R45D46R56R66R",
            "22D23R33R43D44D45R55D56R66R",
            "22R32R42D43R53D54R64D65D66R",
            "22R32D33R43D44D45D46R56R66R",
            "22R32D33R43D44D45R55D56R66R",
            "22D23R33R43R53D54D55D56R66R",
            "22D23R33R43D44D45R55R65D66R",
            "22R32R42D43D44D45D46R56R66R",
            "22D23R33R43U42R52R62D63D64D65D66R",
            "22D23D24R34R44U43R53R63D64D65D66R",
            "22D23D24D25R35R45U44U43U42R52R62D63D64D65D66R",
            "22R32D33D34R44D45R55R65D66R",
            "22D23D24R34D35R45D46R56R66R",
            "22R32D33R43D44R54D55D56R66R",
            "22D23R33D34R44R54D55D56R66R",
            "22D23R33D34R44R54D55R65D66R",
            "22R32R42R52D53D54L44L34L24D25D26R36R46R56R66R",
            "22R32R42D43R53D54D55R65D66R",
            "22D23R33D34R44R54R64D65D66R",
            "22D23D24R34R44D45D46R56R66R",
            "22R32D33D34L24D25D26R36R46R56R66R",
            "22D23R33D34R44D45D46R56R66R",
            "22R32D33R43D44R54R64D65D66R",
            "22R32D33D34L24D25D26R36R46U45R55R65D66R",
            "22R32D33R43R53D54D55R65D66R",
            "22R32D33D34R44R54R64D65D66R",
            "22D23D24D25D26R36R46U45U44R54R64D65D66R",
            "22D23D24R34R44R54D55R65D66R",
            "22R32R42D43D44R54D55D56R66R",
            "22R32D33D34R44R54D55R65D66R",
            "22D23R33R43D44R54R64D65D66R",
            "22D23D24R34D35R45R55D56R66R",
            "22R32R42R52R62D63D64L54D55D56R66R",
            "22R32R42D43R53D54D55D56R66R",
            "22D23R33D34D35R45R55D56R66R",
            "22R32R42D43R53R63D64D65D66R",
            "22R32R42D43D44L34L24D25D26R36R46R56R66R",
            "22R32R42R52D53D54L44L34D35D36R46R56R66R",
            "22R32R42R52R62D63D64L54L44D45D46R56R66R",
            "22D23D24D25R35R45R55R65D66R",
            "22R32R42R52D53D54L44D45D46R56R66R",
            "22D23R33R43R53R63D64D65D66R",
            "22D23R33D34R44D45R55D56R66R",
            "22R32D33D34R44D45D46R56R66R",
            "22D23R33R43D44R54D55R65D66R",
            "22D23R33R43U42R52R62D63D64L54D55D56R66R",
            "22D23D24R34R44D45R55R65D66R",
            "22D23D24R34R44R54D55D56R66R",
            "22D23D24D25R35R45U44U43R53R63D64D65D66R",
            "22R32D33R43D44R54D55R65D66R",
            "22D23R33D34D35D36R46R56R66R",
            "22D23R33R43R53D54D55R65D66R",
            "22D23D24R34R44D45R55D56R66R",
            "22R32D33D34D35R45D46R56R66R",
            "22R32R42R52D53D54D55D56R66R",
            "22R32R42D43D44D45R55D56R66R",
            "22D23R33D34R44D45R55R65D66R",
            "22R32R42D43D44L34D35D36R46R56R66R",
            "22D23D24D25R35R45R55D56R66R",
            "22R32D33D34R44R54D55D56R66R",
            "22R32R42D43D44R54D55R65D66R",
            "22D23D24D25R35R45U44R54R64D65D66R",
            "22R32D33R43R53D54D55D56R66R",
            "22D23D24D25D26R36R46U45U44U43R53R63D64D65D66R",
            "22R32D33D34D35R45R55R65D66R",
            "22D23R33R43D44R54D55D56R66R",
            "22D23D24D25D26R36R46U45R55R65D66R",
            "22D23R33D34D35R45R55R65D66R",
            "22R32R42R52R62D63D64L54L44L34D35D36R46R56R66R",
            "22R32D33R43D44D45R55R65D66R"};

        final ArrayList<String> PossibleMapCodes = new ArrayList<>();
        //Finds all map codes that contain partial map code
        for (String AllMapCode : AllMapCodes) {
            if (AllMapCode.length() >= PartialMapCode.length()) {
                if (AllMapCode.substring(0, PartialMapCode.length()).equals(PartialMapCode)) {
                    PossibleMapCodes.add(AllMapCode);
                }
            }
        }

        final ArrayList<String> PossiblePositions = new ArrayList<>();
        //Finds all map codes that contains current position and makes list of all possible next moves
        final String CurrentPosition = PartialMapCode.substring(PartialMapCode.length() - 2, PartialMapCode.length());
        for (int PossibleMapCodesIndex = 0; PossibleMapCodesIndex < PossibleMapCodes.size(); PossibleMapCodesIndex++) {
            for (int PossibleMapCodeCharacterIndex = 0; PossibleMapCodeCharacterIndex < PossibleMapCodes.get(PossibleMapCodesIndex).length() - 3; PossibleMapCodeCharacterIndex = PossibleMapCodeCharacterIndex + 3) {
                if (CurrentPosition.equals(PossibleMapCodes.get(PossibleMapCodesIndex).substring(PossibleMapCodeCharacterIndex, PossibleMapCodeCharacterIndex + 2))) {
                    if (!PossiblePositions.contains(PossibleMapCodes.get(PossibleMapCodesIndex).substring(PossibleMapCodeCharacterIndex + 3, PossibleMapCodeCharacterIndex + 5))) {
                        PossiblePositions.add(PossibleMapCodes.get(PossibleMapCodesIndex).substring(PossibleMapCodeCharacterIndex + 3, PossibleMapCodeCharacterIndex + 5));
                    }
                    //Ends embedded loop
                    PossibleMapCodeCharacterIndex = PossibleMapCodes.get(PossibleMapCodesIndex).length() + 1;
                }

            }
        }
        //Returns an array of positions of tiles that can be moved to
        return PossiblePositions;
    }

    private void MapCodeGenerator() {
        /*
        This method generates a valid map code
        It generates a random path through the 7 by 7 tile grid
        The path must always go from the start tile to the end tile
        The path must also never be directly next to itself  
        The path must also be the same length as specified
        If the path generated is valid then the map code is saved
        Otherwise the method recurses
         */

        final Random Rand = new Random();
        //Array of all possible moves
        final ArrayList<String> PossibleMoves = new ArrayList<>();
        //Map code to be generated
        String MapCodeGen = "";
        //String containing all invalid positions for path
        String InvalidPositions = "";
        //Current position of path set to start position
        String CurrentPosition = "22";

        //Path is not valid
        boolean PathNotValid = true;
        //Path is not complete
        boolean PathComplete = false;
        int CurrentPathLength = 0;
        //Loop through every tile
        for (int TileArrayPosition = 0; TileArrayPosition < TilesArray.size(); TileArrayPosition++) {
            //If tiles is on edge of grid
            if (TilesArray.get(TileArrayPosition).IsEdge()) {
                //Adds position of tile to list of invalid positions
                InvalidPositions = InvalidPositions + (TilesArray.get(TileArrayPosition).getPosition()) + " ";
            }
        }
        //Loop while path is not complete
        while (!PathComplete) {
            //Path is  valid
            PathNotValid = true;
            //Gets column and row of current position
            int Column;
            int Row;
            try {
                Column = Integer.valueOf(CurrentPosition.substring(0, 1));
                Row = Integer.valueOf(CurrentPosition.substring(1, 2));
            } catch (NumberFormatException e) {
                Column = 1;
                Row = 1;
            }
            //Adds current position to map code
            MapCodeGen = MapCodeGen + CurrentPosition;
            //Increases current path length by 1
            CurrentPathLength++;
            //Position of tile to the right of current position
            final String Right = (Column + 1) + "" + Row;
            //Position of tile to the Left  of current position
            final String Left = (Column - 1) + "" + Row;
            //Position of tile above of current position
            final String Up = Column + "" + (Row - 1);
            //Position of tile below of current position
            final String Down = Column + "" + (Row + 1);

            //Number of possible moves found
            int PossibleMovesAvailable = 0;
            //Empty possible moves array
            PossibleMoves.clear();
            //If position on right is valid
            if (!InvalidPositions.contains(Right)) {
                //Increases number of possible moves found by 1
                PossibleMovesAvailable++;
                //Adds direction of position on right to possible moves array
                PossibleMoves.add("R");
            }
            //If position on left is valid
            if (!InvalidPositions.contains(Left)) {
                //Increases number of possible moves found by 1
                PossibleMovesAvailable++;
                //Adds direction of position on left to possible moves array
                PossibleMoves.add("L");
            }
            //If position above is valid
            if (!InvalidPositions.contains(Up)) {
                //Increases number of possible moves found by 1
                PossibleMovesAvailable++;
                //Adds direction of position above to possible moves array
                PossibleMoves.add("U");
            }
            //If position below is valid
            if (!InvalidPositions.contains(Down)) {
                //Increases number of possible moves found by 1
                PossibleMovesAvailable++;
                //Adds direction of position below to possible moves array
                PossibleMoves.add("D");
            }
            //If there are no possible moves available
            if (PossibleMovesAvailable == 0) {
                //Path is complete
                PathComplete = true;
                //Path is not valid
                PathNotValid = true;

            } else {
                //Selects random direction form array of possible positions
                final int RandomMove = Rand.nextInt(PossibleMovesAvailable);
                final String MoveNext = PossibleMoves.get(RandomMove);

                //Adds Current position to list of invalid positions
                InvalidPositions = InvalidPositions + CurrentPosition + " ";

                //If position on the right selected
                if (MoveNext.equals("R")) {
                    //Sets current position as the position on the right
                    CurrentPosition = Right;
                    //Adds all other positions to list of invalid positions
                    InvalidPositions = InvalidPositions + (Left) + " ";
                    InvalidPositions = InvalidPositions + (Up) + " ";
                    InvalidPositions = InvalidPositions + (Down) + " ";

                }
                //If position on the left selected
                if (MoveNext.equals("L")) {
                    //Sets current position as the position on the left
                    CurrentPosition = Left;
                    //Adds all other positions to list of invalid positions
                    InvalidPositions = InvalidPositions + (Right) + " ";
                    InvalidPositions = InvalidPositions + (Up) + " ";
                    InvalidPositions = InvalidPositions + (Down) + " ";

                }
                //If position above selected
                if (MoveNext.equals("U")) {
                    //Sets current position as the position above
                    CurrentPosition = Up;
                    //Adds all other positions to list of invalid positions
                    InvalidPositions = InvalidPositions + (Right) + " ";
                    InvalidPositions = InvalidPositions + (Left) + " ";
                    InvalidPositions = InvalidPositions + (Down) + " ";
                }
                //If position below selected
                if (MoveNext.equals("D")) {
                    //Sets current position as the position below
                    CurrentPosition = Down;
                    //Adds all other positions to list of invalid positions
                    InvalidPositions = InvalidPositions + (Right) + " ";
                    InvalidPositions = InvalidPositions + (Left) + " ";
                    InvalidPositions = InvalidPositions + (Up) + " ";

                }
                //Adds direction of next position to map code
                MapCodeGen = MapCodeGen + MoveNext;
                //If current position is the end position
                if (CurrentPosition.equals("66")) {
                    //Adds final position and direction to map code
                    MapCodeGen = MapCodeGen + "66R";
                    //Increases current path length by 1
                    CurrentPathLength++;
                    //Path is valid
                    PathNotValid = false;
                    //Path is complete
                    PathComplete = true;
                    //If path can be of any length
                    if (AnyPathLength && !MapCodeGen.equals(MapCode)) {
                        //Sets map code as generated map code
                        setMapCode(MapCodeGen);
                    } else {
                        //If current path length is equal to chosen path length
                        if (CurrentPathLength == PathLength && !MapCodeGen.equals(MapCode)) {
                            //Sets map code as generated map code
                            setMapCode(MapCodeGen);
                        } else {
                            //Path is not valid
                            PathNotValid = true;
                        }
                    }
                }
            }
        }
        //Path generated is not valid
        if (PathNotValid) {
            //Restart method
            MapCodeGenerator();
        }
    }

    private void setMapCode(final String NewMapCode) {
        //Sets the map code
        MapCode = NewMapCode;
    }

    private final JLabel QuestionMarkImage = new JLabel();

    private void QuestionMarkImageSetup() {
        /*
        This method sets the properties of the question mark image
        It then adds it to the game customisation panel
         */

        //Sets properties of question mark image
        QuestionMarkImage.setIcon(AssetsImageIconArray[2]);
        QuestionMarkImage.setBounds(305, 169, 700, 700);
        //Adds question mark image at front most position
        GameCustomisationPanel.add(QuestionMarkImage, new Integer(2));
    }

    final private ArrayList<Tiles> TilesArray = new ArrayList<>(49);

    private void GridSetup() {
        /*
        This method creates a 7 by 7 grid of tiles
        Including the start and end tiles and all standard tiles
         */

        //Starting y coordinate (at top left of grid)
        int YCoordinate = 169;
        //Loop through each row
        for (int Row = 1; Row < 8; Row++) {
            //Starting x coordinate (at top left of grid)
            int XCoordinate = 305;
            //Loop through each column
            for (int Column = 1; Column < 8; Column++) {
                //Creates new button for tile
                final JButton BufferButton = new JButton();
                //Sets position and size of button and thus tile
                BufferButton.setBounds(XCoordinate, YCoordinate, 100, 100);
                BufferButton.setBorder(null);
                //Position = the position of the tile in the 7 by 7 grid
                final String Position = (Column + "" + Row);
                //Sets tile at column 1 and row 2 to start tile
                if (Position.equals("12")) {
                    BufferButton.setIcon(AssetsTileIconArray[1]);
                } else {
                    //Sets tile at column 7 and row 6 to end tile
                    if (Position.equals("76")) {
                        BufferButton.setIcon(AssetsTileIconArray[0]);
                    } else {
                        //Sets non end or start tiles to standard tile
                        BufferButton.setIcon(AssetsTileIconArray[2]);

                        //Adds mouse rollover listener to tile button
                        BufferButton.getModel().addChangeListener((ChangeEvent e) -> {
                            ButtonModel model = (ButtonModel) e.getSource();
                            //If tile is hovered over
                            if (model.isRollover()) {
                                if (BufferButton.getIcon().equals(AssetsTileIconArray[2]) && BufferButton.getBorder() != AssetsBorderArray[2]) {
                                    //Highlights the tile
                                    BufferButton.setBorder(AssetsBorderArray[5]);
                                }
                            } //If tile is not hovered over
                            else {
                                if (BufferButton.getBorder() != AssetsBorderArray[2]) {
                                    //Unhighlight the tile
                                    BufferButton.setBorder(null);
                                }
                            }
                        });
                    }
                }
                //Next column starts 100px right form previous
                XCoordinate = XCoordinate + 100;
                //Creates new tile
                Tiles BufferTile = new Tiles(BufferButton, Position);
                //Adds tile to array of all tiles
                TilesArray.add(BufferTile);
            }
            //Next row starts 100px down from previous
            YCoordinate = YCoordinate + 100;
        }
    }

    private void AddGrid() {
        /*
        This method sets the properties of the grid area
        It then adds it to the game panel at the back most position
        The tiles are added to the grid
         */

        //Sets properties of grid area
        final JPanel GridArea = new JPanel();
        GridArea.setBounds(300, 164, 710, 710);
        GridArea.setBorder(AssetsBorderArray[0]);
        GridArea.setBackground(null);
        //Adds grid area at back most position
        GameCustomisationPanel.add(GridArea, new Integer(0));
        //Loops through all tiles
        for (int TilesArrayIndex = 0; TilesArrayIndex < TilesArray.size(); TilesArrayIndex++) {
            //Adds tile informant of the grid but behind the question mark image
            GameCustomisationPanel.add(TilesArray.get(TilesArrayIndex).getButton(), new Integer(1));
        }
    }

    private void EdgeDetection() {
        /*
        This method sets all tiles on the edge of the 7 by 7 grid of tiles as edge tiles
        These tiles cannon be part of the path
         */

        //Loop through every tile
        for (int TileArrayPosition = 0; TileArrayPosition < TilesArray.size(); TileArrayPosition++) {
            //Gets position of each tile
            final String Position = TilesArray.get(TileArrayPosition).getPosition();
            //If tile is on edge of grid (top or bottom row, left or right most column)
            if (Position.charAt(0) == '1' || Position.charAt(0) == '7' || Position.charAt(1) == '1' || Position.charAt(1) == '7') {
                //Sets tile as edge
                TilesArray.get(TileArrayPosition).setIsEdge(true);
            }
        }
    }

    private boolean MapCodeValid(String MapCodeCheck) {
        /*
        This method checks if the map code is valid
        It creates a path based off the given map code
        Returns whether or not the path is valid
         */

        //Number of characters within map code
        final int Length = MapCodeCheck.length();
        //Character position within map code = 0
        int CharacterIndexWithinMapCode = 0;
        //String containing all valid positions
        String ValidPositions = "22 ";
        //String containing all invalid positions
        StringBuilder InvalidPositions = new StringBuilder();
        //Loops through every tile
        for (int TileArrayPosition = 0; TileArrayPosition < TilesArray.size(); TileArrayPosition++) {
            //If tiles is on edge of grid
            if (TilesArray.get(TileArrayPosition).IsEdge()) {
                //Adds position of tile to list of invalid positions
                InvalidPositions.append(TilesArray.get(TileArrayPosition).getPosition()).append(" ");
            }
        }
        //If map code practically empty
        if (MapCodeCheck.length() < 3) {
            //Returns invalid
            return false;
        } else {
            //If start of map code does not equal start position or end of map code does not equal end position and direction
            if (!MapCodeCheck.startsWith("22") || !MapCodeCheck.substring(Length - 3, Length).equals("66R")) {
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
                    if (ValidPositions.contains(CurrentPosition) && !InvalidPositions.toString().contains(CurrentPosition)) {
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
                            InvalidPositions.append(Left).append(" ");
                            InvalidPositions.append(Up).append(" ");
                            InvalidPositions.append(Down).append(" ");
                        } else {
                            //If direction of next position is to the left
                            if (move.equals("L")) {
                                //Adds position of tile to the left of current position to valid positions
                                ValidPositions = ValidPositions + (Left) + " ";
                                //Adds all other positions to list of invalid positions
                                InvalidPositions.append(Right).append(" ");
                                InvalidPositions.append(Up).append(" ");
                                InvalidPositions.append(Down).append(" ");
                            } else {
                                //If direction of next position is above
                                if (move.equals("U")) {
                                    //Adds position of tile above of current position to valid positions
                                    ValidPositions = ValidPositions + (Up);
                                    //Adds all other positions to list of invalid positions
                                    InvalidPositions.append(Right).append(" ");
                                    InvalidPositions.append(Left).append(" ");
                                    InvalidPositions.append(Down).append(" ");
                                } else {
                                    //If direction of next position is below
                                    if (move.equals("D")) {
                                        //Adds position of tile below of current position to valid positions
                                        ValidPositions = ValidPositions + (Down) + " ";
                                        //Adds all other positions to list of invalid positions
                                        InvalidPositions.append(Right).append(" ");
                                        InvalidPositions.append(Left).append(" ");
                                        InvalidPositions.append(Up).append(" ");
                                    } //If direction invalid
                                    else {
                                        //Returns invalid
                                        return false;
                                    }
                                }
                            }
                        }
                    }//If current position is invalid 
                    else {
                        //Returns invalid
                        return false;
                    }
                    //If current position is the end position and at the end of the map code
                    if (CurrentPosition.equals("66") && CharacterIndexWithinMapCode + 3 == Length) {
                        //Returns valid
                        return true;
                    } else {
                        //Adds Current position to list of invalid positions
                        InvalidPositions.append(CurrentPosition).append(" ");
                        //Increases character position within map code by 3
                        CharacterIndexWithinMapCode = CharacterIndexWithinMapCode + 3;
                    }
                }
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
            final String Position = TilesArray.get(TileArrayPosition).getPosition();
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

    private final JTextArea TutorialField = new JTextArea();
    private int TutorialMessageIndex = 0;

    private void TutorialAreaSetup() {
        /*
        This method sets the properties of the tutorial area components
        It then adds them to the game customisation panel
         */

        //List of all game customisation tutorial messages
        final String GameCustomisationTutorialMessages[] = {
            "First you must customise the game\n"
            + "Pick a game mode and difficulty",
            "Standard - income after every round, Bounty - income on \n"
            + "enemy death, Budget - fixed starting money (no income)",
            "At harder difficulties enemy waves will be stronger and\n"
            + "your weapon will charge slower",
            "Next you must create the path\n"
            + "You can enter, generate, or draw the path",
            "To enter the path, type a valid map code below\n"
            + "To generate it, select the length and press generate",
            "To draw the path, press draw and pick between the\n"
            + "highlighted tiles until the path is complete",
            "Once game mode, difficulty, and the path has been\n"
            + "selected press play to start playing the game",};
        //Sets the properties of the tutorial area
        final JPanel TutorialArea = new JPanel();
        TutorialArea.setBorder(AssetsBorderArray[0]);
        TutorialArea.setBounds(230, 20, 570, 64);
        TutorialArea.setBackground(AssetsColorArray[0]);
        //Adds the tutorial area informant of the header title
        GameCustomisationPanel.add(TutorialArea, new Integer(3));
        //Sets properties of the tutorial messages text area
        TutorialField.setBorder(AssetsBorderArray[4]);
        TutorialField.setBounds(264, 26, 502, 52);
        TutorialField.setBackground(AssetsColorArray[0]);
        TutorialField.setForeground(AssetsColorArray[3]);
        TutorialField.setFont(AssetsFontArray[2]);
        TutorialField.setText(GameCustomisationTutorialMessages[TutorialMessageIndex]);
        TutorialField.setEditable(false);
        //Adds the tutorial messages text area at the front most position
        GameCustomisationPanel.add(TutorialField, new Integer(4));

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
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds hover listener to previous tutorial message button that highlights it
        AddButtonHoverColorHighlight(PreviousTutorialMessageButton, AssetsButtonIconArray[21]);
        PreviousTutorialMessageButton.setIcon(AssetsButtonIconArray[24]);
        //Adds previous tutorial message button at front most position
        GameCustomisationPanel.add(PreviousTutorialMessageButton, new Integer(5));
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
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
            }
        });
        //Adds hover listener to next tutorial message button that highlights it
        AddButtonHoverColorHighlight(NextTutorialMessageButton, AssetsButtonIconArray[23]);
        //Adds next tutorial message button at front most position
        GameCustomisationPanel.add(NextTutorialMessageButton, new Integer(5));
    }

    private void FinalSetup() {
        /*
        This method sets the properties the game customisation panel
        It then adds it to the main panel
         */

        //Sets properties of game customisation panel
        GameCustomisationPanel.setBounds(0, 0, 1030, 964);
        //Creates header and adds it to game customisation panel
        GameCustomisationPanel.add(WindowHeaderSetup(), new Integer(2));
        //Adds the game customisation panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(GameCustomisationPanel, new Integer(1));
    }
}
