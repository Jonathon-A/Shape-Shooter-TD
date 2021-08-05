package shape_shooter_td;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

final public class Shape_Shooter_TD extends Shape_Shooter_TDAssets {

    public static Shape_Shooter_TD MainClass = new Shape_Shooter_TD();

    public static void main(String[] args) {
        //Initial setup for the program, called on program run
        MainClass.InitialSetup();
    }

    private void InitialSetup() {
        /*
        This method sets the properties of the GUI's window, background, and main panel
        It also checks if the program is connected to the database at launch
        It then opens the title panel and stats the music loop
         */

        //Setup GUI's window, background, and main panel properties
        WindowSetup();
        //Setup transition panel properties
        TransitionPanelSetup();
        //Tests whether or not the program is connected to the database at launch
        ConnectedToDatabase = Shape_Shooter_TD_Database.OpenConnections();
        //Open title panel
        LaunchTitleWindow();
        //Starts music loop
        Thread T1 = new Thread(() -> {
            final Shape_Shooter_TDMusic Shape_Shooter_TDMusic = new Shape_Shooter_TDMusic();
        });
        T1.start();
    }

    public final Shape_Shooter_TD_Database Shape_Shooter_TD_Database = new Shape_Shooter_TD_Database();

    private GameWindow GameWindow;
    private AccountLoginWindow AccountLoginWindow;

    private void LaunchTitleWindow() {
        //Starts and launches the title panel without a transition
        Thread T1 = new Thread(() -> {
            final TitleScreenWindow TitleScreenWindow = new TitleScreenWindow();
            TitleScreenWindow.LaunchSetup();
        });
        T1.start();

    }

    final public void StartTitleWindow(final int TransitionType) {
        //Starts and opens the title panel
        Thread T1 = new Thread(() -> {
            final TitleScreenWindow TitleScreenWindow = new TitleScreenWindow();
            TitleScreenWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartAccountRegistrationPrimaryWindow(final int TransitionType) {
        //Starts and opens the account registration primary panel
        Thread T1 = new Thread(() -> {
            final AccountRegistrationPrimaryWindow AccountRegistrationPrimaryWindow = new AccountRegistrationPrimaryWindow();
            AccountRegistrationPrimaryWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartAccountRegistrationSecondaryWindow(final String ValidEmailAddress, final int TransitionType) {
        //Starts and opens the account registration secondary panel
        Thread T1 = new Thread(() -> {
            final AccountRegistrationSecondaryWindow AccountRegistrationSecondaryWindow = new AccountRegistrationSecondaryWindow(ValidEmailAddress);
            AccountRegistrationSecondaryWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartAccountRecoveryPrimaryWindow(final int TransitionType) {
        //Starts and opens the account recovery primary panel
        Thread T1 = new Thread(() -> {
            final AccountRecoveryPrimaryWindow AccountRecoveryPrimaryWindow = new AccountRecoveryPrimaryWindow();
            AccountRecoveryPrimaryWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartAccountRecoverySecondaryWindow(final int TransitionType) {
        //Starts and opens the account recovery secondary panel
        Thread T1 = new Thread(() -> {
            final AccountRecoverySecondaryWindow AccountRecoverySecondaryWindow = new AccountRecoverySecondaryWindow();
            AccountRecoverySecondaryWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    private int RemainingLockedOutTime = 0;

    final public void StartAccountLoginWindow(final int TransitionType) {
        RemainingLockedOutTime = 0;
        //If the account login panel has already been opened
        if (AccountLoginWindow != null) {
            //Finds remaining locked time
            RemainingLockedOutTime = AccountLoginWindow.getRemainingLockedOutTime();
        }
        //Starts and opens the account login panel with remaining locked time
        Thread T1 = new Thread(() -> {
            AccountLoginWindow = new AccountLoginWindow(RemainingLockedOutTime);
            AccountLoginWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartGuestLoginWindow(final int TransitionType) {
        //Starts and opens the guest login panel
        Thread T1 = new Thread(() -> {
            final GuestLoginWindow GuestLoginWindow = new GuestLoginWindow();
            GuestLoginWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartMenuWindow(final int TransitionType) {
        //Starts and opens the menu panel
        Thread T1 = new Thread(() -> {
            final MenuWindow MenuWindow = new MenuWindow();
            MenuWindow.StartSetup(TransitionType);
        });
        //If the game is being played
        if (GameWindow != null) {
            //Stops the game
            GameWindow.ForceStopLoop();
            GameWindow = null;
        }
        T1.start();
    }

    final public void StartGameCustomisationWindow(final boolean IsTutorialActive, final int TransitionType) {
        //Starts and opens the game customisation panel with given tutorial details
        Thread T1 = new Thread(() -> {
            final GameCustomisationWindow GameCustomisationWindow = new GameCustomisationWindow(IsTutorialActive);
            GameCustomisationWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartGame(final double CustomisedDifficulty, final String CustomisedMapCode, final String CustomisedGameMode, final boolean IsTutorialActive, final int TransitionType) {
        //Starts and opens the game panel with given game and tutorial details
        Thread T1 = new Thread(() -> {
            GameWindow = new GameWindow(CustomisedDifficulty, CustomisedMapCode, CustomisedGameMode, IsTutorialActive);
            GameWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    final public void StartLeaderboardWindow(final int TransitionType) {
        //Starts and opens the leaderboard panel
        Thread T1 = new Thread(() -> {
            final LeaderboardWindow LeaderboardWindow = new LeaderboardWindow();
            LeaderboardWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    final public void StartSettingsWindow(final int TransitionType) {
        //Starts and opens the settings panel
        Thread T1 = new Thread(() -> {
            final SettingsWindow SettingsWindow = new SettingsWindow();
            SettingsWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    final public void StartDeleteAccountWindow(final int TransitionType) {
        //Starts and opens the delete account panel
        Thread T1 = new Thread(() -> {
            final DeleteAccountWindow DeleteAccountWindow = new DeleteAccountWindow();
            DeleteAccountWindow.StartSetup(TransitionType);
        });
        T1.start();

    }

    final public void StartChangeEmailAddressWindow(final int TransitionType) {
        //Starts and opens the change email address panel
        Thread T1 = new Thread(() -> {
            final ChangeEmailAddressWindow ChangeEmailAddressWindow = new ChangeEmailAddressWindow();
            ChangeEmailAddressWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    final public void StartChangeUsernameWindow(final int TransitionType) {
        //Starts and opens the change username panel
        Thread T1 = new Thread(() -> {
            final ChangeUsernameWindow ChangeUsernameWindow = new ChangeUsernameWindow();
            ChangeUsernameWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    final public void StartChangePasswordWindow(final int TransitionType) {
        //Starts and opens the change password panel
        Thread T1 = new Thread(() -> {
            final ChangePasswordWindow ChangePasswordWindow = new ChangePasswordWindow();
            ChangePasswordWindow.StartSetup(TransitionType);
        });
        T1.start();
    }

    private boolean ConnectedToDatabase = false;

    final public boolean isConnectedToDatabase() {
        //Return whether or not the program is connected to the database
        return ConnectedToDatabase;
    }

    final public JFrame getWindow() {
        //Returns the GUI's window
        return GUIWindow;
    }

    final private JFrame GUIWindow = new JFrame("Shape Shooter TD");
    final private JLabel WindowBackground = new JLabel();
    final public JLayeredPane MainWindowPanel = new JLayeredPane();

    private void WindowSetup() {
        /*
        This method sets the properties of the GUI's window
        It also sets the properties of the main panel and background
        It then adds the main panel to the GUI's window
         */

        //Finds dimension of the user's screen
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final int xSize = ((int) tk.getScreenSize().getWidth());
        final int ySize = ((int) tk.getScreenSize().getHeight());
        //Setup GUI's window properties
        GUIWindow.setLocation(0, 0);
        GUIWindow.setMaximumSize(new Dimension(xSize, ySize));
        GUIWindow.setSize(new Dimension(xSize, ySize));
        GUIWindow.setMinimumSize(new Dimension(1046, 1003));
        GUIWindow.getContentPane().setLayout(null);
        try {
            GUIWindow.setIconImage(CreateBufferedImage("IconImage.png"));
        } catch (IOException ex) {
            System.out.println("Getting icon image error: " + ex);
        }
        //Setup GUI's window background properties
        WindowBackground.setSize(new Dimension(xSize, ySize));
        //Adds background image
        BufferedImage GreyBackground;
        try {
            GreyBackground = CreateBufferedImage("GreyBackgroundCompressed.jpg");
            //Scales background image to the same size as the user's screen
            Image GreyBackgroundScaled = GreyBackground.getScaledInstance(xSize, ySize, Image.SCALE_DEFAULT);
            WindowBackground.setIcon(new ImageIcon(GreyBackgroundScaled));
        } catch (IOException ex) {
            System.out.println("Image background error " + ex);
        }
        WindowBackground.setVisible(false);
        //Sets GUI's window colour background
        GUIWindow.getContentPane().setBackground(new Color(103, 103, 103));
        GUIWindow.setResizable(true);
        GUIWindow.setUndecorated(false);
        GUIWindow.setVisible(true);
        GUIWindow.setLayout(null);
        //Program will close when GUI's window is closed
        GUIWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Setup main panel properties
        MainWindowPanel.setBackground(AssetsColorArray[5]);
        MainWindowPanel.setBorder(AssetsBorderArray[0]);
        MainWindowPanel.setOpaque(true);
        MainWindowPanel.setLayout(null);
        //Adds resize listener to GUI's Window
        GUIWindow.addComponentListener(new ComponentAdapter() {
            //If the GUI's window is resized
            @Override
            public void componentResized(ComponentEvent e) {
                //If the GUI's window should be fullscreen
                if (FullScreen) {
                    //Recenter the panel
                    MainWindowPanel.setBounds((GUIWindow.getWidth() / 2 - 515), (GUIWindow.getHeight() / 2 - 482), 1030, 964);
                } else {
                    //Recenter the panel with small offset to account for taskbar
                    MainWindowPanel.setBounds((GUIWindow.getWidth() / 2 - 515) - 8, (GUIWindow.getHeight() / 2 - 482) - 18, 1030, 964);

                }
            }
        });

        //Initially centres the panel with small offset to account for taskbar
        MainWindowPanel.setBounds((GUIWindow.getWidth() / 2 - 515) - 8, (GUIWindow.getHeight() / 2 - 482) - 18, 1030, 964);
        //Adds focus listener to GUI's Window
        GUIWindow.addWindowFocusListener(new WindowAdapter() {
            //If the GUI's Window gains focused
            @Override
            public void windowGainedFocus(WindowEvent e) {
                //GUI's window is focused
                WindowFocused = true;
            }

            //If the GUI's Window loses focus
            @Override
            public void windowLostFocus(WindowEvent we) {
                //GUI's window is not focused
                WindowFocused = false;
            }
        });
        //Adds the main panel infront of the background
        GUIWindow.add(MainWindowPanel, 0);
        GUIWindow.add(WindowBackground, 1);
    }

    private boolean WindowFocused = true;

    final public boolean isWindowFocused() {
        //Returns whether or not the GUI's window is currently focused
        return WindowFocused;
    }

    private JPanel TransitionPanel;
    private int TransitionOpacity = 0;

    final public boolean IsTransitionComplete() {
        //Return whether or not the transition is complete (no visible grey foreground
        return !TransitionPanel.isVisible();
    }

    private void TransitionPanelSetup() {
        /*
        This method sets the properties of the transition panel
        It then adds it to the main panel at the front most position
        The transition is played using the transition start and end methods
         */

        TransitionPanel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;
                //If antialiasing is set to true
                if (isANTIALIASING()) {
                    //Adds antialiasing to graphics 
                    gx.addRenderingHints(ANTIALIASING);
                }
                switch (getRenderQuality()) {
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
                //Paints transition overlay with current transition opacity
                gx.setStroke(new BasicStroke(0));
                gx.setColor(new Color(224, 224, 224, TransitionOpacity));
                gx.fillRect(6, 6, 1018, 952);
            }
        };
        //Setup transition panel properties
        TransitionPanel.setBounds(0, 0, 1030, 964);
        TransitionPanel.setLayout(null);
        TransitionPanel.setOpaque(false);
        TransitionPanel.setVisible(false);
        //Adds the transition panel to the main panel at the front most position
        MainWindowPanel.add(TransitionPanel, new Integer(3));
    }

    private JLayeredPane CurrentDisplayedJLayeredPane = null;

    final public void setCurrentDisplayedPanel(final JLayeredPane CurrentJLayeredPane) {
        //Sets the new panel, to be displayed, as the current panel
        this.CurrentDisplayedJLayeredPane = CurrentJLayeredPane;
    }

    final public void TransitionStart(final int TransitionType) {
        /*
        This method plays the start of the transition
        The transition panel fades to a grey foreground 
        It is called whenever a new panel is being displayed
         */

        //Sets bounds of transition based on transition type
        switch (TransitionType) {
            case (0):
            default:
                //Full transition
                TransitionPanel.setBounds(0, 0, 1030, 964);
                break;
            case (1):
                //Full transition excluding header
                TransitionPanel.setBounds(0, 84, 1030, 880);
                break;
            case (2):
                //Partial transition
                TransitionPanel.setBounds(0, 624, 1030, 340);
                break;
        }
        //Transition panel fades to a grey foreground
        TransitionPanel.setVisible(true);
        for (int Opacity = 0; Opacity <= 255; Opacity = Opacity + 5) {
            try {
                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException ex) {
                System.out.println("Transition start error : " + ex);
            }
            TransitionOpacity = Opacity;
            TransitionPanel.repaint();
        }
        //Completes all operations in the operations queue while to transition panel is opaque
        ExecuteOperationsQueue();
    }

    final public void TransitionEnd() {
        /*
        This method plays the end of the transition
        The previous panel is removed before the transition panel fades from a grey foreground 
        It is called whenever a new panel is being displayed
         */

        //Brief delay before ending transition
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException ex) {
            System.out.println("Transition end error : " + ex);
        }
        //Removes previous panel while transition is opaque
        if (CurrentDisplayedJLayeredPane != null) {

            CurrentDisplayedJLayeredPane.setVisible(false);
            try {
                MainWindowPanel.remove(CurrentDisplayedJLayeredPane);
            } catch (Exception e) {
                System.out.println("Transition removing panel error : " + e);
            }
            CurrentDisplayedJLayeredPane = null;
        }
        //Transition panel fades from a grey foreground
        for (int Opacity = 255; Opacity >= 0; Opacity = Opacity - 5) {
            try {
                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException ex) {
                System.out.println("Transition end error : " + ex);
            }
            TransitionOpacity = Opacity;
            TransitionPanel.repaint();
        }
        TransitionPanel.setVisible(false);
    }

    private String Username = "";

    public String getUsername() {
        //Returns the current username, username is nothing if logged in as guest
        return Username;
    }

    private final ArrayList<QueueOperation> OperationsQueue = new ArrayList<>();

    final public void AddOperationToQueue(final String Operation, final UserAccountsTableEntry UserAccountsTableEntry, final UserSettingsTableEntry UserSettingsTableEntry, final LeaderboardTableEntry LeaderboardTableEntry, final String StringData) {
        //Adds new operation to operations queue
        OperationsQueue.add(new QueueOperation(Operation, UserAccountsTableEntry, UserSettingsTableEntry, LeaderboardTableEntry, StringData));
    }

    private void ExecuteOperationsQueue() {
        /*
        This method completes all operations in the operations queue
        It is called every time the transition between panels becomes opaque
         */

        //Loops while operations remain in operations queue
        while (!OperationsQueue.isEmpty()) {

            switch (OperationsQueue.get(0).getOperation()) {
                //If operation is "AddOrUpdateAccountsEntry"
                case ("AddOrUpdateAccountsEntry"):
                    //Adds user account entry (contained in the queue operation) to user accounts table
                    Shape_Shooter_TD_Database.AddOrUpdateAccountsEntry(OperationsQueue.get(0).getUserAccountsTableEntry());
                    break;
                //If operation is "FindCurrentUserAccountEntryFromUsername"
                case ("FindCurrentUserAccountEntryFromUsername"):
                    //Finds user account entry using username inside the user accounts table
                    Shape_Shooter_TD_Database.FindCurrentUserAccountEntryFromUsername(OperationsQueue.get(0).getStringData());
                    Username = OperationsQueue.get(0).getStringData();
                    break;
                //If operation is "FindCurrentUserAccountEntryFromEmailAddress"
                case ("FindCurrentUserAccountEntryFromEmailAddress"):
                    //Finds user account entry using email address inside the user accounts table
                    Shape_Shooter_TD_Database.FindCurrentUserAccountEntryFromEmailAddress(OperationsQueue.get(0).getStringData());
                    break;
                //If operation is "FindCurrentUserSettingsEntry"
                case ("FindCurrentUserSettingsEntry"):
                    //Finds user account settings using username inside the user settings table
                    Shape_Shooter_TD_Database.FindCurrentUserSettingsEntry(OperationsQueue.get(0).getStringData());
                    break;
                //If operation is "UpdateSettings"
                case ("UpdateSettings"):
                    //Updates settings using the user settings entry contained in the queue operation
                    UpdateAllSettings(OperationsQueue.get(0).getUserSettingsTableEntry());
                    break;
                //If operation is "AddOrUpdateSettingsEntry"
                case ("AddOrUpdateSettingsEntry"):
                    //Adds user settings entry (contained in the queue operation) to user settings table
                    Shape_Shooter_TD_Database.AddOrUpdateSettingsEntry(OperationsQueue.get(0).getUserSettingsTableEntry());
                    break;
                //If operation is "FindAllLeaderboardEntries"
                case ("FindAllLeaderboardEntries"):
                    //Finds all leaderboard entries in the leaderboard details table
                    Shape_Shooter_TD_Database.FindAllLeaderboardEntries();
                    break;
                //If operation is "AddOrUpdateLeaderboardEntry"
                case ("AddOrUpdateLeaderboardEntry"):
                    //Finds all leaderboard entries in the leaderboard details table
                    Shape_Shooter_TD_Database.AddOrUpdateLeaderboardEntry(OperationsQueue.get(0).getLeaderboardTableEntry());
                    break;
                //If operation is "FindAllLeaderboardEntries"
                case ("DeleteAccount"):
                    //Adds / updates leaderboard entry inside leaderboard details table
                    Shape_Shooter_TD_Database.DeleteAccount(OperationsQueue.get(0).getStringData());
                    break;

            }
            //Removes operation from queue once completed
            OperationsQueue.remove(0);
        }
    }

    private boolean ANTIALIASINGIsOn = true;

    final public boolean isANTIALIASING() {
        //Returns whether or not GUI components drawn using graphics should be drawn using ANTIALIASING RenderHints
        return ANTIALIASINGIsOn;
    }

    private int RenderQuality = 3;

    final public int getRenderQuality() {
        //Returns whether GUI components drawn using graphics should be drawn using either SPEED_RENDER, DEFAULT_RENDER, or QUALITY_RENDER RenderHints
        return RenderQuality;
    }

    private boolean FullScreen = false;

    final public boolean isFullScreen() {
        //Returns whether or not the GUI's window should be fullscreen
        return FullScreen;
    }

    private boolean ImageBackground = false;

    final public boolean isImageBackground() {
        //Returns whether an image or colour background should be used
        return ImageBackground;
    }

    private int MusicVolume = 0;

    final public int getMusicVolume() {
        //Returns the current music volume
        return MusicVolume;
    }

    private int SoundVolume = 50;

    final public int getSoundVolume() {
        //Returns the current sound effect volume
        return SoundVolume;
    }

    private boolean AllSoundMuted = false;

    final public boolean isAllSoundMuted() {
        //Returns whether or not all sound should be muted
        return AllSoundMuted;
    }

    private boolean MuteSoundInBackground = false;

    final public boolean isMuteSoundInBackground() {
        //Returns whether or not sound should be muted while the program is unfocused
        return MuteSoundInBackground;
    }

    final public void UpdateAllSettings(final UserSettingsTableEntry UserSettings) {
        /*
        This method changes relevant variables and program properties based on the users settings
        It is called whenever settings are changed
         */

        //Sets the system full screen setting
        UpdateFullScreenSetting(UserSettings.isFullscreenSetting());
        //Sets the system image background setting
        UpdateImageBackgroundSetting(UserSettings.isImageBackgroundSetting());
        //Sets the system antialiasing setting
        ANTIALIASINGIsOn = UserSettings.isAntialiasingSetting();
        //Sets the system render quality setting
        RenderQuality = UserSettings.getRenderQualitySetting();
        //Sets the music volume setting
        MusicVolume = UserSettings.getMusicVolumeSetting();
        //Sets the sound volume setting
        SoundVolume = UserSettings.getSoundVolumeSetting();
        //Sets the sound muted setting
        AllSoundMuted = UserSettings.isAllSoundMutedSetting();
        //Sets the background sound muted setting
        MuteSoundInBackground = UserSettings.isMuteSoundInBackgroundSetting();
    }

    private void UpdateFullScreenSetting(final boolean FullScreenSetting) {
        /*
        This method updates the system full screen setting
        If changed to ON, the GUI’s window's dimensions will match the dimensions of the user’s screen and the title bar will not be visible
        If changed to OFF, the GUI’s window’s dimensions will be less than the dimensions of the user’s screen and the title bar will be visible
         */

        //Gets the size of the users screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        //If the fullscreen setting is set to on
        if (FullScreenSetting) {
            //If the system is not currently fullscreen
            if (!FullScreen) {
                //Sets the display to full screen
                GUIWindow.setResizable(false);
                GUIWindow.setLocation(0, 0);
                GUIWindow.setSize(new Dimension(xSize, ySize));
                MainWindowPanel.setBounds((GUIWindow.getWidth() / 2 - 515), (GUIWindow.getHeight() / 2 - 482), 1030, 964);
                GUIWindow.dispose();
                GUIWindow.setUndecorated(true);
                GUIWindow.setVisible(true);
                //Sets the system fullscreen setting to true
                FullScreen = true;
            }

        }//If the fullscreen setting is set to off
        else {
            //If the system is  currently fullscreen
            if (FullScreen) {
                //Sets the display to not fullscreen
                GUIWindow.setResizable(true);
                MainWindowPanel.setBounds((GUIWindow.getWidth() / 2 - 515) - 8, (GUIWindow.getHeight() / 2 - 482) - 18, 1030, 964);
                GUIWindow.dispose();
                GUIWindow.setUndecorated(false);
                GUIWindow.setVisible(true);
                //Sets the system fullscreen setting to false
                FullScreen = false;
            }
        }
    }

    private void UpdateImageBackgroundSetting(final boolean ImageBackgroundSetting) {
        /*
        This method updates the image background setting
        If changed to ON, the background of the GUI will be changed from a grey colour to a grey image
        If changed to OFF, the background of the GUI will be changed from a grey image to a grey colour
         */

        //If the image background setting is set to on
        if (ImageBackgroundSetting) {
            //If the system does not currently have an image background
            if (!ImageBackground) {
                //Sets the background to a image
                WindowBackground.setVisible(true);
                //Sets the system image background setting to true
                ImageBackground = true;
            }
        }//If the image background setting is set to off
        else {
            //If the system does  currently have an image background
            if (ImageBackground) {
                //Sets the background to a colour
                WindowBackground.setVisible(false);
                //Sets the system image background setting to false
                ImageBackground = false;
            }
        }

    }

    final public void SendConfirmationEmail(final String EmailAddress, final String Message) {
        /*
        This method sends a message via email to the specified email address
        It is used during account registration and recovery
         */

        //Prepares the email
        final Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ShapeShooterTDEmail@gmail.com", "Shape_Shooter_TD_EmailPassword");
            }
        });

        final Message message = new MimeMessage(session);
        try {
            //Sets message and subject for the email
            message.setFrom(new InternetAddress("ShapeShooterTDEmail@gmail.com"));
            message.setRecipient(RecipientType.TO, new InternetAddress(EmailAddress));
            message.setSubject("Email Confirmation");
            message.setText(Message);
            //Sends the email to the specified email address
            Transport.send(message);
        } catch (MessagingException ex) {
            System.out.println("Email error: " + ex);
        }
    }
}
