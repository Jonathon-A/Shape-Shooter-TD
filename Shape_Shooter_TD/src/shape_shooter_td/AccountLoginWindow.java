package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class AccountLoginWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    public AccountLoginWindow(final int NewRemainingLockedOutTime) {
        RemainingLockedOutTime = NewRemainingLockedOutTime;
    }

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the account login panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens account login panel
        AccountLoginWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Account login panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(AccountLoginPanel);
        //Prevents login attempts for the remaining login timeout time
        Thread T1 = new Thread(() -> {
            //Locks text fields and login button
            LoginButtonLocked = true;
            UsernameEntryField.setEditable(false);
            PasswordEntryField.setEditable(false);
            LoginButton.setBackground(AssetsColorArray[2]);
            UsernameEntryField.setForeground(AssetsColorArray[8]);
            PasswordEntryField.setForeground(AssetsColorArray[8]);
            //The initial login timeout has not yet been used
            boolean InitialTimeOutLoopUsed = false;
            //Loops until the remaining timeout time is zero
            for (int LockedTime = RemainingLockedOutTime; LockedTime > 0; LockedTime--) {
                //If the user is on this account login panel
                if (LoginWindowInUse) {
                    //The initial login timeout was used
                    InitialTimeOutLoopUsed = true;
                    //Reduces the login timeout time by 1 second
                    RemainingLockedOutTime = LockedTime;
                    //Displays timer duration
                    UsernameEntryField.setText("Too many failed attempts, please wait " + (RemainingLockedOutTime) + " seconds");
                    PasswordEntryField.setText("Too many failed attempts, please wait " + (RemainingLockedOutTime) + " seconds");
                    //1 second delay every loop
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println("Initial login time out error : " + ex);
                    }
                } //If the user is not on this account login panel, i.e. on the title panel
                else {
                    //Ends timeout loop
                    LockedTime = 0;
                }
            }
            //If the user is on this account login panel and the initial login timeout was used
            if (InitialTimeOutLoopUsed && LoginWindowInUse) {
                //Text fields display what should be entered into them
                UsernameEntryField.setText("Enter username...");
                PasswordEntryField.setText("Enter password...");
            }
            //If the user is on this account login panel after the initial login timeout has finished
            if (LoginWindowInUse) {
                //No remaining login timeout time
                RemainingLockedOutTime = 0;
            }
            //Unlocks text fields
            UsernameEntryField.setEditable(true);
            PasswordEntryField.setEditable(true);
        });
        T1.start();

    }

    private final JLayeredPane AccountLoginPanel = new JLayeredPane();

    private void AccountLoginWindowSetup() {
        /*
        This method opens the account login panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(AccountLoginPanel);
        //Setup username and password entry area components 
        EnterUsernameAndPasswordAreaSetup();
        //Setup username entry text field 
        UsernameEntrySetup();
        //Setup password entry text field 
        PasswordEntrySetup();
        //Setup login and cancel buttons 
        LoginAndCancelButtonSetup();
        //Setup account login panel 
        FinalSetup();
    }

    private void EnterUsernameAndPasswordAreaSetup() {
        /*
        This method sets the properties of the username and password entry area components
        It then adds them to the account login panel
         */

        //Sets the properties of the username and password entry area
        final JPanel EnterUsernameAndPasswordArea = new JPanel();
        EnterUsernameAndPasswordArea.setBorder(AssetsBorderArray[0]);
        EnterUsernameAndPasswordArea.setBounds(20, 644, 990, 300);
        EnterUsernameAndPasswordArea.setBackground(AssetsColorArray[0]);
        //Adds the username and password entry area at the back most position
        AccountLoginPanel.add(EnterUsernameAndPasswordArea, new Integer(1));
        //Sets the properties of the username and password entry title
        final JTextField EnterUsernameAndPasswordTitle = new JTextField();
        EnterUsernameAndPasswordTitle.setBounds(20, 644, 990, 60);
        EnterUsernameAndPasswordTitle.setFont(AssetsFontArray[5]);
        EnterUsernameAndPasswordTitle.setEditable(false);
        EnterUsernameAndPasswordTitle.setText("Enter username and password");
        EnterUsernameAndPasswordTitle.setHorizontalAlignment(JTextField.CENTER);
        EnterUsernameAndPasswordTitle.setOpaque(false);
        EnterUsernameAndPasswordTitle.setBorder(null);
        //Adds the username and password entry title at the front most position
        AccountLoginPanel.add(EnterUsernameAndPasswordTitle, new Integer(2));
        //Sets the properties of the username and password entry description
        final JTextField EnterUsernameAndPasswordDescription = new JTextField();
        EnterUsernameAndPasswordDescription.setBounds(20, 688, 990, 36);
        EnterUsernameAndPasswordDescription.setFont(AssetsFontArray[2]);
        EnterUsernameAndPasswordDescription.setEditable(false);
        EnterUsernameAndPasswordDescription.setText("Please enter your username and password to login to your account");
        EnterUsernameAndPasswordDescription.setHorizontalAlignment(JTextField.CENTER);
        EnterUsernameAndPasswordDescription.setOpaque(false);
        EnterUsernameAndPasswordDescription.setBorder(null);
        //Adds the username and password entry description at the front most position
        AccountLoginPanel.add(EnterUsernameAndPasswordDescription, new Integer(2));
    }

    private final JTextField UsernameEntryField = new JTextField();

    private void UsernameEntrySetup() {
        /*
        This method sets the properties of the username entry text field
        This includes what happens when the user uses the text field
        It then adds it to the account login panel
         */

        //Sets the properties of the username entry text field
        UsernameEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameEntryField.setBounds(40, 724, 950, 60);
        UsernameEntryField.setBackground(AssetsColorArray[0]);
        UsernameEntryField.setForeground(AssetsColorArray[8]);
        UsernameEntryField.setFont(AssetsFontArray[1]);
        UsernameEntryField.setText("Enter username...");
        //Adds use listener to the username entry text field
        UsernameEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the username entry text field
            @Override
            final public void insertUpdate(final DocumentEvent de) {
                //If "Enter username..." and "Incorrect details entered" are not displayed on the username entry text field
                if (!"Enter username...".equals(UsernameEntryField.getText()) && !"Incorrect details entered".equals(UsernameEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //If the username entry text field is not locked and is not empty
                    if (!UsernameEntryField.getText().equals("") && UsernameEntryField.isEditable()) {
                        //Unlocks the login button
                        LoginButtonLocked = false;
                        LoginButton.setBackground(AssetsColorArray[0]);
                    } //If the username entry text field is locked or is empty and the password entry text field displays "Enter password..."
                    else if (PasswordEntryField.getText().equals("Enter password...")) {
                        //Locks the login button
                        LoginButtonLocked = true;
                        LoginButton.setBackground(AssetsColorArray[2]);
                    }
                }
            }

            //When a character is removed from the username entry text field
            @Override
            final public void removeUpdate(final DocumentEvent de) {
                //If "Enter username..." and "Incorrect details entered" are not displayed on the username entry text field
                if (!"Enter username...".equals(UsernameEntryField.getText()) && !"Incorrect details entered".equals(UsernameEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //If the username entry text field is not locked and is not empty
                    if (!UsernameEntryField.getText().equals("") && UsernameEntryField.isEditable()) {
                        //Unlocks the login button
                        LoginButtonLocked = false;
                        LoginButton.setBackground(AssetsColorArray[0]);
                    } //If the username entry text field is locked or is empty and the password entry text field displays "Enter password..."
                    else if (PasswordEntryField.getText().equals("Enter password...")) {
                        //Locks the login button
                        LoginButtonLocked = true;
                        LoginButton.setBackground(AssetsColorArray[2]);
                    }
                }
            }

            @Override
            final public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the username entry text field
        UsernameEntryField.addFocusListener(new FocusListener() {
            //When the username entry text field gains focus
            @Override
            final public void focusGained(FocusEvent focusEvent) {
                //If the username entry text field displays "Enter username..."
                if (UsernameEntryField.getText().equals("Enter username...")) {
                    //Empties the username entry text field
                    UsernameEntryField.setText("");
                    //Changes username entry text field text colour to black
                    UsernameEntryField.setForeground(AssetsColorArray[3]);
                }
                //If the username entry text field displays "Incorrect details entered"
                if (UsernameEntryField.getText().equals("Incorrect details entered")) {
                    //Password entry text field displays "Enter password..."
                    PasswordEntryField.setText("Enter password...");
                    //Changes password entry text field text colour to grey
                    PasswordEntryField.setForeground(AssetsColorArray[8]);
                    //Empties the username entry text field
                    UsernameEntryField.setText("");
                    //Changes username entry text field text colour to black
                    UsernameEntryField.setForeground(AssetsColorArray[3]);
                }
            }

            //When the username entry text field loses focus
            @Override
            final public void focusLost(final FocusEvent focusEvent) {
                //If the username entry text field is empty
                if (UsernameEntryField.getText().isEmpty()) {
                    //Username entry text field displays "Enter username..."
                    UsernameEntryField.setText("Enter username...");
                    //Changes username entry text field text colour to grey
                    UsernameEntryField.setForeground(AssetsColorArray[8]);
                }
            }
        });
        //Adds the username entry text field at the front most position
        AccountLoginPanel.add(UsernameEntryField, new Integer(2));
    }

    private final JTextField PasswordEntryField = new JTextField();

    private void PasswordEntrySetup() {
        /*
        This method sets the properties of the password entry text field
        This includes what happens when the user uses the text field
        It then adds it to the account login panel
         */

        //Sets the properties of the password entry text field
        PasswordEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        PasswordEntryField.setBounds(40, 794, 950, 60);
        PasswordEntryField.setBackground(AssetsColorArray[0]);
        PasswordEntryField.setForeground(AssetsColorArray[8]);
        PasswordEntryField.setFont(AssetsFontArray[1]);
        PasswordEntryField.setText("Enter password...");
        PasswordEntryField.setFont(AssetsFontArray[1]);
        //Adds use listener to the password entry text field
        PasswordEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the password entry text field
            @Override
            final public void insertUpdate(final DocumentEvent de) {
                //If "Enter password..." and "Incorrect details entered" are not displayed on the password entry text field
                if (!"Enter password...".equals(PasswordEntryField.getText()) && !"Incorrect details entered".equals(PasswordEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //If the password entry text field is not locked and is not empty
                    if (!PasswordEntryField.getText().equals("") && PasswordEntryField.isEditable()) {
                        //Unlocks the login button
                        LoginButtonLocked = false;
                        LoginButton.setBackground(AssetsColorArray[0]);
                    }//If the password entry text field is locked or is empty and the username entry text field displays "Enter username..."
                    else if (UsernameEntryField.getText().equals("Enter username...")) {
                        //Locks the login button
                        LoginButtonLocked = true;
                        LoginButton.setBackground(AssetsColorArray[2]);
                    }
                }
            }

            //When a character is removed from the password entry text field
            @Override
            final public void removeUpdate(final DocumentEvent de) {
                //If "Enter password..." and "Incorrect details entered" are not displayed on the text fields
                if (!"Enter password...".equals(PasswordEntryField.getText()) && !"Incorrect details entered".equals(PasswordEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //If the password entry text field is not locked and is not empty
                    if (!PasswordEntryField.getText().equals("") && PasswordEntryField.isEditable()) {
                        //Unlocks the login button
                        LoginButtonLocked = false;
                        LoginButton.setBackground(AssetsColorArray[0]);
                    } //If the password entry text field is locked or is empty and the username entry text field displays "Enter username..."
                    else if (UsernameEntryField.getText().equals("Enter username")) {
                        //Locks the login button
                        LoginButtonLocked = true;
                        LoginButton.setBackground(AssetsColorArray[2]);
                    }
                }
            }

            @Override
            final public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the password entry text field
        PasswordEntryField.addFocusListener(new FocusListener() {
            //When the password entry text field gains focus
            @Override
            final public void focusGained(FocusEvent focusEvent) {
                //If the password entry text field displays "Enter password..."
                if (PasswordEntryField.getText().equals("Enter password...")) {
                    //Empties the password entry text field
                    PasswordEntryField.setText("");
                    //Changes password entry text field text colour to black
                    PasswordEntryField.setForeground(AssetsColorArray[3]);
                }
                //If the password entry text field displays "Incorrect details entered"
                if (PasswordEntryField.getText().equals("Incorrect details entered")) {
                    //Empties the password entry text field
                    PasswordEntryField.setText("");
                    //Changes password entry text field text colour to black
                    PasswordEntryField.setForeground(AssetsColorArray[3]);
                    //Username entry text field displays "Enter username..."
                    UsernameEntryField.setText("Enter username...");
                    //Changes username entry text field text colour to grey
                    UsernameEntryField.setForeground(AssetsColorArray[8]);
                }
            }

            //When the password entry text field loses focus
            @Override
            final public void focusLost(final FocusEvent focusEvent) {
                //If the password entry text field is empty
                if (PasswordEntryField.getText().isEmpty()) {
                    //Password entry text field displays "Enter password..."
                    PasswordEntryField.setText("Enter password...");
                    //Changes password entry text field text colour to grey
                    PasswordEntryField.setForeground(AssetsColorArray[8]);
                }

            }
        });
        //Adds the password entry text field at the front most position
        AccountLoginPanel.add(PasswordEntryField, new Integer(2));

    }

    private final JButton LoginButton = new JButton("Login");
    private boolean LoginButtonLocked = false;

    private int FailedLoginAttempts = 0;

    private void LoginAndCancelButtonSetup() {
        /*
        This method sets the properties of the login and cancel buttons
        It also prevents brute force attacks by preventing logins after 5 failed attempts for 30 seconds
        It then adds them to the account login panel
         */

        //Sets properties of login button
        LoginButton.setFont(AssetsFontArray[1]);
        LoginButton.setBounds(520, 864, 470, 60);
        LoginButton.setBackground(AssetsColorArray[0]);
        LoginButton.setBorder(AssetsBorderArray[0]);
        LoginButton.setFocusPainted(false);
        //Adds mouse press listener to login button
        LoginButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and the login button is not locked
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && !LoginButtonLocked) {
                //Unsuccessful login
                boolean CorrectLoginDetailsEntered = false;
                //Checks if account exist with entered username
                boolean AccountWithUsernameFound = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.FindAccountWithUsername(UsernameEntryField.getText());
                //If account is found
                if (AccountWithUsernameFound) {
                    //Retrieves said accounts login details
                    Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.FindCurrentUserAccountEntryFromUsername(UsernameEntryField.getText());
                    //If details correct for login
                    if (Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getPassword().equals(PasswordEntryField.getText())) {
                        //Successful login
                        CorrectLoginDetailsEntered = true;
                    }
                }
                //If successful login
                if (CorrectLoginDetailsEntered) {
                    LoginButton.removeActionListener(LoginButton.getActionListeners()[0]);
                    LoginButton.setBackground(AssetsColorArray[1]);
                    //Locks text fields
                    UsernameEntryField.setEditable(false);
                    PasswordEntryField.setEditable(false);
                    //Adds "FindCurrentUserAccountEntryFromUsername" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserAccountEntryFromUsername", null, null, null, UsernameEntryField.getText());
                    //Add "FindCurrentUserSettingsEntry" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserSettingsEntry", null, null, null, UsernameEntryField.getText());
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Full transition to menu panel
                    Shape_Shooter_TD.MainClass.StartMenuWindow(0);

                } //If unsuccessful login
                else {
                    //Increments failed login attempts
                    FailedLoginAttempts++;
                    //If 5 failed login attempts
                    if (FailedLoginAttempts >= 5) {
                        //Resets failed login attempts counter
                        FailedLoginAttempts = 0;
                        //Locks login button for 30 second
                        Thread T1 = new Thread(() -> {
                            LoginTimeOut();
                        });
                        T1.start();
                    } //If less than 5 failed login attempts
                    else {
                        //Locks login button
                        LoginButtonLocked = true;
                        //Displays "Incorrect details entered"
                        LoginButton.setBackground(AssetsColorArray[2]);
                        UsernameEntryField.setForeground(AssetsColorArray[8]);
                        PasswordEntryField.setForeground(AssetsColorArray[8]);
                        UsernameEntryField.setText("Incorrect details entered");
                        PasswordEntryField.setText("Incorrect details entered");
                    }
                }
            }
        });
        //Adds hover listener to login button that highlights it
        AddButtonHoverColorHighlight(LoginButton, null);
        LoginButton.setBackground(AssetsColorArray[2]);
        //Adds login button at front most position
        AccountLoginPanel.add(LoginButton, new Integer(2));

        //Sets properties of cancel button
        final JButton CancelButton = new JButton("Cancel");
        CancelButton.setFont(AssetsFontArray[1]);
        CancelButton.setBounds(40, 864, 470, 60);
        CancelButton.setBackground(AssetsColorArray[0]);
        CancelButton.setBorder(AssetsBorderArray[0]);
        CancelButton.setFocusPainted(false);
        //Adds mouse press listener to cancel button
        CancelButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                CancelButton.removeActionListener(CancelButton.getActionListeners()[0]);
                CancelButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                LoginWindowInUse = false;
                //Partial transition to title panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(2);
            }

        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        AccountLoginPanel.add(CancelButton, new Integer(2));
    }

    private int RemainingLockedOutTime = 0;

    final public int getRemainingLockedOutTime() {
        //Returns remaining login timeout time 
        return RemainingLockedOutTime;
    }

    private boolean LoginWindowInUse = true;

    private void LoginTimeOut() {
        /*
        This method prevents login attempts for 30 seconds
         */

        //Locks text fields and login button
        LoginButtonLocked = true;
        UsernameEntryField.setEditable(false);
        PasswordEntryField.setEditable(false);
        LoginButton.setBackground(AssetsColorArray[2]);
        UsernameEntryField.setForeground(AssetsColorArray[8]);
        PasswordEntryField.setForeground(AssetsColorArray[8]);
        //Loops 30 times
        for (int TimeLockedOut = 0; TimeLockedOut < 30; TimeLockedOut++) {
            //If the user is on this account login panel
            if (LoginWindowInUse) {
                //Reduces login timeout time by 1 second
                RemainingLockedOutTime = 30 - TimeLockedOut;
                //Displays timer duration
                UsernameEntryField.setText("Too many failed attempts, please wait " + (30 - TimeLockedOut) + " seconds");
                PasswordEntryField.setText("Too many failed attempts, please wait " + (30 - TimeLockedOut) + " seconds");
                //1 second delay every loop
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    System.out.println("Login time out error : " + ex);
                }
            }//If the user is not on this account login panel, i.e. on the title panel
            else {
                //Ends timeout loop
                TimeLockedOut = 100;
            }
        }
        //If the user is on this account login panel after the login timeout has finished
        if (LoginWindowInUse) {
            //Text fields display what should be entered into them
            UsernameEntryField.setText("Enter username...");
            PasswordEntryField.setText("Enter password...");
            //No remaining login timeout time
            RemainingLockedOutTime = 0;
        }
        //Unlocks text fields
        UsernameEntryField.setEditable(true);
        PasswordEntryField.setEditable(true);

    }

    private void FinalSetup() {
        /*
        This method sets the properties of the account login panel
        It then adds it to the main panel
         */

        //Sets account login panel properties
        AccountLoginPanel.setBounds(0, 0, 1030, 964);
        //Adds the account login panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(AccountLoginPanel, new Integer(1));
    }
}
