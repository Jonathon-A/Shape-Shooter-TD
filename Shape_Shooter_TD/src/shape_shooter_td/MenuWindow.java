package shape_shooter_td;

import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

final public class MenuWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the menu panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens menu panel
        MenuWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Menu panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(MenuPanel);
    }

    private final JLayeredPane MenuPanel = new JLayeredPane();

    private void MenuWindowSetup() {
        /*
        This method opens the menu panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Setup menu buttons
        MenuButtonsSetup();
        //Setup menu buttons descriptions
        DescriptionDisplaySetup();
        //Setup menu panel 
        FinalSetup();
    }

    private void MenuButtonsSetup() {
        /*
        This method sets the properties of the menu buttons
        This includes what happens when the mouse cursor hovers over them
        It then adds them to the menu panel
         */

        //Setup play game menu button properties
        final JButton PlayGameButton = new JButton("Play game");
        PlayGameButton.setFont(AssetsFontArray[0]);
        PlayGameButton.setBounds(20, 94, 990, 100);
        PlayGameButton.setBackground(AssetsColorArray[0]);
        PlayGameButton.setBorder(AssetsBorderArray[0]);
        PlayGameButton.setFocusPainted(false);
        //Adds mouse press listener to play game menu button
        PlayGameButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                PlayGameButton.removeActionListener(PlayGameButton.getActionListeners()[0]);
                PlayGameButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to game customisation panel, without tutorial
                Shape_Shooter_TD.MainClass.StartGameCustomisationWindow(false, 0);
            }
        });
        //Adds mouse hover listener to play game menu button
        PlayGameButton.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //Shows game description
                DescriptionDisplayUpdate("Play");
            } //If the mouse cursor is not hovering over the button
            else {
                //Shows no description
                DescriptionDisplayUpdate("");
            }
        });
        //Adds hover listener to play game menu button that highlights it
        AddButtonHoverColorHighlight(PlayGameButton, null);
        //Adds play game menu button at front most positions
        MenuPanel.add(PlayGameButton, new Integer(1));

        //Setup tutorial menu button properties
        final JButton TutorialButton = new JButton("Play game with tutorial");
        TutorialButton.setFont(AssetsFontArray[0]);
        TutorialButton.setBounds(20, 204, 990, 100);
        TutorialButton.setBackground(AssetsColorArray[0]);
        TutorialButton.setBorder(AssetsBorderArray[0]);
        TutorialButton.setFocusPainted(false);
        //Adds mouse press listener to tutorial menu button
        TutorialButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                TutorialButton.removeActionListener(TutorialButton.getActionListeners()[0]);
                TutorialButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to game customisation panel, with tutorial
                Shape_Shooter_TD.MainClass.StartGameCustomisationWindow(true, 0);
            }
        });
        //Adds mouse hover listener to tutorial menu button
        TutorialButton.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //Shows tutorial description
                DescriptionDisplayUpdate("Tutorial");
            } //If the mouse cursor is not hovering over the button
            else {
                //Shows no description
                DescriptionDisplayUpdate("");
            }
        });
        //Adds mouse hover listener to tutorial menu button that highlights it
        AddButtonHoverColorHighlight(TutorialButton, null);
        //Adds tutorial menu button at front most positions
        MenuPanel.add(TutorialButton, new Integer(1));

        //Setup leaderboard menu button properties
        final JButton LeaderboardButton = new JButton("Leaderboard");
        LeaderboardButton.setFont(AssetsFontArray[0]);
        LeaderboardButton.setBounds(20, 314, 990, 100);
        LeaderboardButton.setBackground(AssetsColorArray[0]);
        LeaderboardButton.setBorder(AssetsBorderArray[0]);
        LeaderboardButton.setFocusPainted(false);
        //Adds mouse press listener to leaderboard menu button
        LeaderboardButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and program is connected to the database
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
                LeaderboardButton.removeActionListener(LeaderboardButton.getActionListeners()[0]);
                LeaderboardButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //If the all entries from the leaderboard details table have not been retrieved and copied to an array
                if (Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getLeaderboardEntriesArray() == null) {
                    //Adds "FindAllLeaderboardEntries" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindAllLeaderboardEntries", null, null, null, "");
                }
                //Full transition to leaderboard panel
                Shape_Shooter_TD.MainClass.StartLeaderboardWindow(0);
            }
        });
        //Adds mouse hover listener to leaderboard menu button
        LeaderboardButton.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //Shows leaderboard description
                DescriptionDisplayUpdate("Leaderboard");
            }//If the mouse cursor is not hovering over the button
            else {
                //Shows no description
                DescriptionDisplayUpdate("");
            }
        });
        //Adds mouse hover listener to leaderboard menu button that highlights it
        AddButtonHoverColorHighlight(LeaderboardButton, null);
        //If the program is not connected to the database
        if (!Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
            //Highlights leaderboard menu button red
            LeaderboardButton.setBackground(AssetsColorArray[2]);
        }
        //Adds leaderboard menu button at front most positions
        MenuPanel.add(LeaderboardButton, new Integer(1));

        //Setup settings menu button properties
        final JButton SettingsButton = new JButton("Settings");
        SettingsButton.setFont(AssetsFontArray[0]);
        SettingsButton.setBounds(20, 424, 990, 100);
        SettingsButton.setBackground(AssetsColorArray[0]);
        SettingsButton.setBorder(AssetsBorderArray[0]);
        SettingsButton.setFocusPainted(false);
        //Adds mouse press listener to settings menu button
        SettingsButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                SettingsButton.removeActionListener(SettingsButton.getActionListeners()[0]);
                SettingsButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to settings panel
                Shape_Shooter_TD.MainClass.StartSettingsWindow(0);
            }

        });
        //Adds mouse hover listener to settings menu button
        SettingsButton.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //Shows settings description
                DescriptionDisplayUpdate("Settings");
            } //If the mouse cursor is not hovering over the button
            else {
                //Shows no description
                DescriptionDisplayUpdate("");
            }

        });
        //Adds mouse hover listener to settings menu button that highlights it
        AddButtonHoverColorHighlight(SettingsButton, null);
        //Adds settings menu button at front most positions
        MenuPanel.add(SettingsButton, new Integer(1));

        //Setup sign out menu button properties
        final JButton SignOutButton = new JButton("Sign out");
        SignOutButton.setFont(AssetsFontArray[0]);
        SignOutButton.setBounds(20, 534, 990, 100);
        SignOutButton.setBackground(AssetsColorArray[0]);
        SignOutButton.setBorder(AssetsBorderArray[0]);
        SignOutButton.setFocusPainted(false);
        //Adds mouse press listener to sign out menu button
        SignOutButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                SignOutButton.removeActionListener(SignOutButton.getActionListeners()[0]);
                SignOutButton.setBackground(AssetsColorArray[1]);
                //Adds "FindCurrentUserAccountEntryFromUsername" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserAccountEntryFromUsername", null, null, null, "");
                //Adds "FindCurrentUserSettingsEntry" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserSettingsEntry", null, null, null, "");
                //Initial settings
                UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), false, false, true, 3, 0, 50, false, false);
                //Adds "UpdateSettings" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to title panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(0);
            }
        });
        //Adds mouse hover listener to sign out menu button
        SignOutButton.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //Shows sign out description
                DescriptionDisplayUpdate("Leave");
            } //If the mouse cursor is not hovering over the button
            else {
                //Shows no description
                DescriptionDisplayUpdate("");
            }
        });
        //Adds mouse hover listener to sign out menu button that highlights it
        AddButtonHoverColorHighlight(SignOutButton, null);
        //Adds sign out menu button at front most positions
        MenuPanel.add(SignOutButton, new Integer(1));

    }

    private final JTextArea DescriptionDisplayArea = new JTextArea();
    private final JTextArea DescriptionDisplayTitle = new JTextArea();
    private final JLabel DescriptionDisplayIcon = new JLabel();

    private void DescriptionDisplaySetup() {
        /*
        This method sets the properties of the description display
        It then adds it to the menu panel
         */

        //Sets the properties of the description display area
        DescriptionDisplayArea.setFont(AssetsFontArray[2]);
        DescriptionDisplayArea.setBounds(340, 644, 670, 300);
        DescriptionDisplayArea.setBackground(AssetsColorArray[0]);
        DescriptionDisplayArea.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        DescriptionDisplayArea.setEditable(false);
        DescriptionDisplayArea.setText("\n\nPlease hover over a menu option to view its description");
        //Adds the description display area at the back most position
        MenuPanel.add(DescriptionDisplayArea, new Integer(1));
        //Sets the properties of the description display title
        DescriptionDisplayTitle.setFont(AssetsFontArray[1]);
        DescriptionDisplayTitle.setBounds(346, 650, 658, 48);
        DescriptionDisplayTitle.setBackground(AssetsColorArray[0]);
        DescriptionDisplayTitle.setBorder(AssetsBorderArray[1]);
        DescriptionDisplayTitle.setText("Description");
        DescriptionDisplayTitle.setEditable(false);
        //Adds the description display title at the front most position
        MenuPanel.add(DescriptionDisplayTitle, new Integer(2));
        //Sets the properties of the description display image
        DescriptionDisplayIcon.setBounds(20, 644, 326, 300);
        DescriptionDisplayIcon.setIcon(AssetsImageIconArray[3]);
        //Adds the description display image at the front most position
        MenuPanel.add(DescriptionDisplayIcon, new Integer(1));
    }

    private void DescriptionDisplayUpdate(final String CurrentlyDescribed) {
        /*
        This method updates the description display based on what menu option the mouse cursor is hovering over
        It is called whenever the mouse cursor hovers on or off a menu button
         */

        switch (CurrentlyDescribed) {
            //If the mouse cursor is hovering over the play game menu button
            case ("Play"):
                //Describes game customisation and play
                DescriptionDisplayTitle.setText("Game description");
                DescriptionDisplayArea.setText("\n\nCustomise the game, before playing, by picking between multiple\n"
                        + "game modes and difficulties before entering/generating/drawing the path\n"
                        + "on the map.\n\n"
                        + "Survive for as long as you can against oncoming waves of enemy shapes\n"
                        + "using the turrets and weapons within your arsenal. Compete against\n"
                        + "other players with your position on the leaderboard.\n\n"
                        + "Received income varies with game mode and game speed can be altered.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[4]);
                break;
            //If the mouse cursor is hovering over the tutorial menu button
            case ("Tutorial"):
                //Describes tutorial
                DescriptionDisplayTitle.setText("Tutorial description");
                DescriptionDisplayArea.setText("\n\nLearn how to customise and play a game via text prompts at the top of\n"
                        + "the screen.\n\n"
                        + "Recommended if you haven't played before.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[8]);
                break;
            //If the mouse cursor is hovering over the leaderboard menu button
            case ("Leaderboard"):
                //Describes leaderboard
                DescriptionDisplayTitle.setText("Leaderboard description");
                DescriptionDisplayArea.setText("\n\nView the player leaderboard sorted in order by the highest number of\n"
                        + "waves survived enemy.\n\n"
                        + "Filter by game mode, difficulty, and path used.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[7]);
                break;
            //If the mouse cursor is hovering over the settings menu button
            case ("Settings"):
                //Describes settings
                DescriptionDisplayTitle.setText("Settings description");
                DescriptionDisplayArea.setText("\n\nCustomise a variety of setting options.\n\n"
                        + "Settings changes are saved to your account.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[5]);
                break;
            //If the mouse cursor is hovering over the sign out menu button
            case ("Leave"):
                //Describes signing out
                DescriptionDisplayTitle.setText("Sign out description");
                DescriptionDisplayArea.setText("\n\nSign out of your account and return to the title screen.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[6]);
                break;
            default:
                DescriptionDisplayTitle.setText("Description");
                DescriptionDisplayArea.setText("\n\nPlease hover over a menu option to view its description.");
                DescriptionDisplayIcon.setIcon(AssetsImageIconArray[3]);
                break;
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the menu panel
        It then adds it to the main panel
         */

        //Sets menu panel properties
        MenuPanel.setBounds(0, 0, 1030, 964);
        //Creates header and adds it to menu panel
        MenuPanel.add(WindowHeaderSetup(), new Integer(2));
        //Adds the menu panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(MenuPanel, new Integer(1));
    }

    @Override
    final protected JPanel WindowHeaderSetup() {
        /*
        This method overrides the standard WindowHeaderSetup method
        It creates the panel header for the menu panel
        It then returns this panel header
         */

        //Setup header area properties
        final JPanel HeaderArea = new JPanel();
        HeaderArea.setBackground(AssetsColorArray[5]);
        HeaderArea.setBounds(20, 20, 990, 64);
        HeaderArea.setLayout(null);
        //Setup exit button properties
        final JButton ExitButton = new JButton(AssetsButtonIconArray[26]);
        ExitButton.setBounds(0, 0, 200, 64);
        ExitButton.setFont(AssetsFontArray[1]);
        ExitButton.setBackground(AssetsColorArray[0]);
        ExitButton.setBorder(AssetsBorderArray[0]);
        ExitButton.setFocusPainted(false);
        //Adds mouse press listener to exit button
        ExitButton.addActionListener((ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                ExitButton.removeActionListener(ExitButton.getActionListeners()[0]);
                ExitButton.setIcon(AssetsButtonIconArray[28]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Closes the program
                System.exit(0);
            }
        });
        AddButtonHoverColorHighlight(ExitButton, AssetsButtonIconArray[27]);
        //Adds exit button to header
        HeaderArea.add(ExitButton);
        //Setup profile icon properties
        JLabel ProfileIcon = new JLabel(AssetsImageIconArray[11]);
        ProfileIcon.setBounds(790, 0, 64, 64);
        //Adds profile icon to header
        HeaderArea.add(ProfileIcon);
        //Setup username area properties
        final JTextField UsernameArea = new JTextField(GetDisplayedUsername());
        UsernameArea.setFont(AssetsFontArray[1]);
        UsernameArea.setBounds(840, 0, 150, 64);
        UsernameArea.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameArea.setBackground(AssetsColorArray[0]);
        UsernameArea.setEditable(false);
        UsernameArea.setHorizontalAlignment(JTextField.LEFT);
        //Adds username area to header
        HeaderArea.add(UsernameArea);
        //Setup title properties
        final JLabel TitleArea = new JLabel();
        TitleArea.setBounds(210, 0, 570, 64);
        TitleArea.setIcon(AssetsImageIconArray[9]);
        //Adds title to header
        HeaderArea.add(TitleArea);
        //Return the menu panel header
        return HeaderArea;
    }
}
