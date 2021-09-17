package shape_shooter_td;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final public class TitleScreenWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the title panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens title panel
        TitleScreenWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Title panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(TitleScreenPanel);

    }

    final public void LaunchSetup() {
        /*
        This method opens the title panel with playing a transition
        It is called when the program is first run
         */

        //Opens title panel
        TitleScreenWindowSetup();
        //Title panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(TitleScreenPanel);

    }

    private final JLayeredPane TitleScreenPanel = new JLayeredPane();

    private void TitleScreenWindowSetup() {
        /*
        This method opens the title panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(TitleScreenPanel);
        //If the program is not connected to the database
        if (!Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
            //Setup connect to database area components 
            ConnectToDatabaseAreaSetup();
        }
        //Setup title options buttons
        TitleButtonsSetup();
        //Setup title panel 
        FinalSetup();
    }

    private void ConnectToDatabaseAreaSetup() {
        /*
        This method sets the properties of the connect to database area components
        It then adds them to the title panel
         */

        //Sets the properties of the connect to database area
        final JPanel ConnectToDatabaseArea = new JPanel();
        ConnectToDatabaseArea.setBorder(AssetsBorderArray[0]);
        ConnectToDatabaseArea.setBounds(20, 644, 990, 80);
        ConnectToDatabaseArea.setBackground(AssetsColorArray[0]);
        //Adds the connect to database area at the back most position
        TitleScreenPanel.add(ConnectToDatabaseArea, new Integer(1));
        //Sets the properties of the connect to database title
        final JTextField ConnectToDatabaseTitle = new JTextField();
        ConnectToDatabaseTitle.setBounds(20, 644, 990, 60);
        ConnectToDatabaseTitle.setFont(AssetsFontArray[5]);
        ConnectToDatabaseTitle.setEditable(false);
        ConnectToDatabaseTitle.setText("Connect to database");
        ConnectToDatabaseTitle.setHorizontalAlignment(JTextField.CENTER);
        ConnectToDatabaseTitle.setOpaque(false);
        ConnectToDatabaseTitle.setBorder(null);
        //Adds the connect to database title at the front most position
        TitleScreenPanel.add(ConnectToDatabaseTitle, new Integer(2));
        //Sets the connect to database description
        final JTextField ConnectToDatabaseDescription = new JTextField();
        ConnectToDatabaseDescription.setBounds(20, 688, 990, 36);
        ConnectToDatabaseDescription.setFont(AssetsFontArray[2]);
        ConnectToDatabaseDescription.setEditable(false);
        ConnectToDatabaseDescription.setText("Certain parts of the program will be blocked until the program is restarted while connected to the database");
        ConnectToDatabaseDescription.setHorizontalAlignment(JTextField.CENTER);
        ConnectToDatabaseDescription.setOpaque(false);
        ConnectToDatabaseDescription.setBorder(null);
        //Adds the connect to database at the front most position
        TitleScreenPanel.add(ConnectToDatabaseDescription, new Integer(2));
    }

    private void TitleButtonsSetup() {
        /*
        This method sets the properties of title options buttons
        It then adds them to the title panel
         */

        //Setup login to account button properties
        final JButton LoginToAccount = new JButton("Login to account");
        LoginToAccount.setFont(AssetsFontArray[0]);
        LoginToAccount.setBounds(20, 734, 490, 100);
        LoginToAccount.setBackground(AssetsColorArray[0]);
        LoginToAccount.setBorder(AssetsBorderArray[0]);
        LoginToAccount.setFocusPainted(false);
        //Adds mouse press listener to login to account button
        LoginToAccount.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and program is connected to the database
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
                LoginToAccount.removeActionListener(LoginToAccount.getActionListeners()[0]);
                LoginToAccount.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to account login panel
                Shape_Shooter_TD.MainClass.StartAccountLoginWindow(2);
            }
        });
        //Adds hover listener to login to account button that highlights it
        AddButtonHoverColorHighlight(LoginToAccount, null);
        //If the program is not connected to the database
        if (!Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
            //Highlights login to account button red
            LoginToAccount.setBackground(AssetsColorArray[2]);
        }
        //Adds login to account button at front most positions
        TitleScreenPanel.add(LoginToAccount, new Integer(1));

        //Setup login as guest button properties
        final JButton LoginAsGuest = new JButton("Login as guest");
        LoginAsGuest.setFont(AssetsFontArray[0]);
        LoginAsGuest.setBounds(20, 844, 490, 100);
        LoginAsGuest.setBackground(AssetsColorArray[0]);
        LoginAsGuest.setBorder(AssetsBorderArray[0]);
        LoginAsGuest.setFocusPainted(false);
        //Adds mouse press listener to login as guest button
        LoginAsGuest.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                LoginAsGuest.removeActionListener(LoginAsGuest.getActionListeners()[0]);
                LoginAsGuest.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to guest login panel
                Shape_Shooter_TD.MainClass.StartGuestLoginWindow(2);
            }
        });
        //Adds hover listener to login as guest button that highlights it
        AddButtonHoverColorHighlight(LoginAsGuest, null);
        //Adds login as guest button at front most positions
        TitleScreenPanel.add(LoginAsGuest, new Integer(1));

        //Setup register account button properties
        final JButton RegisterAccount = new JButton("Register account");
        RegisterAccount.setFont(AssetsFontArray[0]);
        RegisterAccount.setBounds(520, 734, 490, 100);
        RegisterAccount.setBackground(AssetsColorArray[0]);
        RegisterAccount.setBorder(AssetsBorderArray[0]);
        RegisterAccount.setFocusPainted(false);
        //Adds mouse press listener to register account button
        RegisterAccount.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and program is connected to the database
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
                RegisterAccount.removeActionListener(RegisterAccount.getActionListeners()[0]);
                RegisterAccount.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to account registration primary panel
                Shape_Shooter_TD.MainClass.StartAccountRegistrationPrimaryWindow(2);
            }
        });
        //Adds hover listener to register account button that highlights it
        AddButtonHoverColorHighlight(RegisterAccount, null);
        //If the program is not connected to the database
        if (!Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
            //Highlights register account button red
            RegisterAccount.setBackground(AssetsColorArray[2]);
        }
        //Adds register account button at front most positions
        TitleScreenPanel.add(RegisterAccount, new Integer(1));

        //Setup recover account button properties
        final JButton RecoverAccount = new JButton("Recover account");
        RecoverAccount.setFont(AssetsFontArray[0]);
        RecoverAccount.setBounds(520, 844, 490, 100);
        RecoverAccount.setBackground(AssetsColorArray[0]);
        RecoverAccount.setBorder(AssetsBorderArray[0]);
        RecoverAccount.setFocusPainted(false);
        //Adds mouse press listener to recover account button
        RecoverAccount.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and program is connected to the database
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
                RecoverAccount.removeActionListener(RecoverAccount.getActionListeners()[0]);
                RecoverAccount.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to account recovery primary panel
                Shape_Shooter_TD.MainClass.StartAccountRecoveryPrimaryWindow(2);
            }
        });
        //Adds hover listener to recover account button that highlights it
        AddButtonHoverColorHighlight(RecoverAccount, null);
        //If the program is not connected to the database
        if (!Shape_Shooter_TD.MainClass.isConnectedToDatabase()) {
            //Highlights recover account button red
            RecoverAccount.setBackground(AssetsColorArray[2]);
        }
        //Adds recover account button at front most positions
        TitleScreenPanel.add(RecoverAccount, new Integer(1));
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the title panel
        It then adds it to the main panel
         */

        //Sets title panel properties
        TitleScreenPanel.setBounds(0, 0, 1030, 964);
        //Adds the title panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(TitleScreenPanel, new Integer(1));
    }

}
