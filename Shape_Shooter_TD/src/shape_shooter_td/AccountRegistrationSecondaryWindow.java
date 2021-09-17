package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class AccountRegistrationSecondaryWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    private String NewAccountEmailAddress = "";

    public AccountRegistrationSecondaryWindow(final String ValidEmailAddress) {
        NewAccountEmailAddress = ValidEmailAddress;
    }

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the account registration secondary panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens account recovery secondary panel
        AccountRegistrationSecondaryWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Account registration secondary panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(AccountRegistrationSecondaryPanel);
    }

    private final JLayeredPane AccountRegistrationSecondaryPanel = new JLayeredPane();

    private void AccountRegistrationSecondaryWindowSetup() {
        /*
        This method opens the account registration secondary panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(AccountRegistrationSecondaryPanel);
        //Setup username and password entry area components 
        EnterUsernameAndPasswordAreaSetup();
        //Setup username entry text field and the username validation indicator
        UsernameEntrySetup();
        //Setup password entry text field, generate password button, and the password validation indicator
        PasswordEntrySetup();
        //Setup register and cancel buttons 
        RegisterAndCancelButtonSetup();
        //Setup account registration secondary panel 
        FinalSetup();
    }

    private void EnterUsernameAndPasswordAreaSetup() {
        /*
        This method sets the properties of the username and password entry area components
        It then adds them to the account registration secondary panel
         */

        //Sets the properties of the username and password entry area
        final JPanel EnterUsernameAndPasswordArea = new JPanel();
        EnterUsernameAndPasswordArea.setBorder(AssetsBorderArray[0]);
        EnterUsernameAndPasswordArea.setBounds(20, 644, 990, 300);
        EnterUsernameAndPasswordArea.setBackground(AssetsColorArray[0]);
        //Adds the username and password entry area at the back most position
        AccountRegistrationSecondaryPanel.add(EnterUsernameAndPasswordArea, new Integer(1));
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
        AccountRegistrationSecondaryPanel.add(EnterUsernameAndPasswordTitle, new Integer(2));
        //Sets the properties of the username and password entry description
        final JTextField EnterUsernameAndPasswordDescription = new JTextField();
        EnterUsernameAndPasswordDescription.setBounds(20, 688, 990, 36);
        EnterUsernameAndPasswordDescription.setFont(AssetsFontArray[2]);
        EnterUsernameAndPasswordDescription.setEditable(false);
        EnterUsernameAndPasswordDescription.setText("Please enter a unique username and enter or generate a password");
        EnterUsernameAndPasswordDescription.setHorizontalAlignment(JTextField.CENTER);
        EnterUsernameAndPasswordDescription.setOpaque(false);
        EnterUsernameAndPasswordDescription.setBorder(null);
        //Adds the username and password entry description at the front most position
        AccountRegistrationSecondaryPanel.add(EnterUsernameAndPasswordDescription, new Integer(2));
    }

    private final JTextField UsernameValidationDisplay = new JTextField();
    private final JTextField UsernameEntryField = new JTextField();

    private void UsernameEntrySetup() {
        /*
        This method sets the properties of the username entry text field and the username validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the account registration secondary panel
         */

        //Sets the properties of the username entry text field
        UsernameEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameEntryField.setBounds(40, 724, 836, 60);
        UsernameEntryField.setBackground(AssetsColorArray[0]);
        UsernameEntryField.setForeground(AssetsColorArray[8]);
        UsernameEntryField.setFont(AssetsFontArray[1]);
        UsernameEntryField.setText("Enter username...");
        //Adds use listener to the username entry text field
        UsernameEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the username entry text field
            @Override
            public void insertUpdate(final DocumentEvent de) {
                //Plays key press sound effect
                if (!"Enter username...".equals(UsernameEntryField.getText())) {
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
                //Checks validation of entered details
                UsernameOrPasswordEntryUpdate(UsernameEntryField.getText(), PasswordEntryField.getText());
                RegisterButtonUpdate();
            }

            //When a character is removed from the username entry text field
            @Override
            public void removeUpdate(final DocumentEvent de) {
                //Plays key press sound effect
                if (!"Enter username...".equals(UsernameEntryField.getText())) {
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
                //Checks validation of entered details
                UsernameOrPasswordEntryUpdate(UsernameEntryField.getText(), PasswordEntryField.getText());
                RegisterButtonUpdate();
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the username entry text field
        UsernameEntryField.addFocusListener(new FocusListener() {
            //When the username entry text field gains focus
            @Override
            final public void focusGained(final FocusEvent focusEvent) {
                //If the username entry text field displays "Enter username..."
                if (UsernameEntryField.getText().equals("Enter username...")) {
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
        AccountRegistrationSecondaryPanel.add(UsernameEntryField, new Integer(2));
        //Sets the properties of the username validation indicator
        UsernameValidationDisplay.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameValidationDisplay.setBounds(870, 724, 120, 60);
        UsernameValidationDisplay.setFont(AssetsFontArray[1]);
        UsernameValidationDisplay.setHorizontalAlignment(JTextField.CENTER);
        UsernameValidationDisplay.setEditable(false);
        UsernameValidationDisplay.setBackground(AssetsColorArray[2]);
        UsernameValidationDisplay.setText("Invalid");
        UsernameValidationDisplay.setFocusable(false);
        //Adds the username validation indicator at the front most position
        AccountRegistrationSecondaryPanel.add(UsernameValidationDisplay, new Integer(3));
    }

    private String NewAccountUsername = "";

    private void UsernameOrPasswordEntryUpdate(final String EnteredUsername, final String EnteredPassword) {
        /*
        This method validates the entered username and password
        It is called whenever the user enters anything into the username or password entry field
         */

        //Checks if username is unique
        final boolean UniqueUsername = !Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.FindAccountWithUsername(EnteredUsername);
        //Checks if username is appropriate
        final boolean Appropriate = CheckAppropriateness(EnteredUsername);
        //Checks if username is less than 65 characters long
        boolean ValidUsernameLength = true;
        if (EnteredUsername.length() > 64) {
            ValidUsernameLength = false;
        }
        //Checks if encrypted password is less than 65 characters long
        final boolean ValidPasswordLength = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.ValidEncryptedLength(EnteredUsername, EnteredPassword, true);
        //If appropriate, valid, correct length, and unique username entered
        if (UniqueUsername && Appropriate && !EnteredUsername.equals("") && !EnteredUsername.equals("Enter username...") && ValidUsernameLength) {
            UsernameValidationDisplay.setBackground(AssetsColorArray[1]);
            //Username validation indicator displays "Valid";
            UsernameValidationDisplay.setText("Valid");
            //currently entered username is valid
            NewAccountUsername = EnteredUsername;
        }//If used or inappropriate or too long username entered
        else {
            UsernameValidationDisplay.setBackground(AssetsColorArray[2]);
            //If username is nothing
            if (EnteredUsername.equals("") || EnteredUsername.equals("Enter username...")) {
                //Username validation indicator displays "Invalid";
                UsernameValidationDisplay.setText("Invalid");
            } //If username is not nothing
            else {
                //If username is too long
                if (!ValidUsernameLength) {
                    //Username validation indicator displays "Long";
                    UsernameValidationDisplay.setText("Long");
                } //If username is correct length
                else {
                    //If username is used
                    if (Appropriate) {
                        //Username validation indicator displays "Taken";
                        UsernameValidationDisplay.setText("Taken");
                    } //If username is inappropriate
                    else {
                        //Username validation indicator displays "Banned";
                        UsernameValidationDisplay.setText("Banned");
                    }
                }
            }
            //Current entered username is invalid
            NewAccountUsername = "";
        }
        //If valid and correct length password entered
        if (!EnteredPassword.equals("") && !EnteredPassword.equals("Enter password...") && ValidPasswordLength) {
            PasswordValidationDisplay.setBackground(AssetsColorArray[1]);
            //Password validation indicator displays "Valid";
            PasswordValidationDisplay.setText("Valid");
            //Current entered password is valid
            NewAccountPassword = EnteredPassword;
        } else {
            PasswordValidationDisplay.setBackground(AssetsColorArray[2]);
            //If password is too long
            if (!ValidPasswordLength) {
                ///Password validation indicator displays "Long";
                PasswordValidationDisplay.setText("Long");
            }//If password correct length
            else {
                ///Password validation indicator displays "Invalid";
                PasswordValidationDisplay.setText("Invalid");
            }
            //Current entered password is invalid
            NewAccountPassword = "";
        }
    }

    private boolean CheckAppropriateness(final String EnteredUsername) {
        /*
        This method checks if the username is appropriate by comparing it against a text file containing a list of inappropriate words
        It then returns whether or not the username is appropriate
         */

        //Username is appropriate
        boolean Appropriate = true;
        String inputLine;
        try {
            BufferedReader read = new BufferedReader(new FileReader(InappropriateWordsFileDirectory));
            //Loops through all inappropriate words in text file containing all inappropriate words
            while ((inputLine = read.readLine()) != null && Appropriate) {
                //If the username contains an inappropriate word
                if (EnteredUsername.contains(inputLine)) {
                    //Username is not appropriate
                    Appropriate = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Check Appropriateness Error: " + e);
        }
        //Returns whether or not the username is appropriate
        return Appropriate;
    }

    private final JButton GeneratePasswordButton = new JButton("Generate");
    private final JTextField PasswordValidationDisplay = new JTextField();
    private final JTextField PasswordEntryField = new JTextField();

    private String NewAccountPassword = "";

    private void PasswordEntrySetup() {
        /*
        This method sets the properties of the password entry text field, generate password button, and the password validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the account registration secondary panel
         */

        //Sets the properties of the password entry text field
        PasswordEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        PasswordEntryField.setBounds(40, 794, 722, 60);
        PasswordEntryField.setBackground(AssetsColorArray[0]);
        PasswordEntryField.setForeground(AssetsColorArray[8]);
        PasswordEntryField.setFont(AssetsFontArray[1]);
        PasswordEntryField.setText("Enter password...");
        //Adds use listener to the password entry text field
        PasswordEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the password entry text field
            @Override
            public void insertUpdate(final DocumentEvent de) {
                //Plays key press sound effect
                if (!"Enter password...".equals(PasswordEntryField.getText())) {
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
                //Checks validation of entered details
                UsernameOrPasswordEntryUpdate(UsernameEntryField.getText(), PasswordEntryField.getText());
                RegisterButtonUpdate();
            }

            //When a character is removed from the password entry text field
            @Override
            public void removeUpdate(final DocumentEvent de) {
                //Plays key press sound effect
                if (!"Enter password...".equals(PasswordEntryField.getText())) {
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
                //Checks validation of entered details
                UsernameOrPasswordEntryUpdate(UsernameEntryField.getText(), PasswordEntryField.getText());
                RegisterButtonUpdate();
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the password entry text field
        PasswordEntryField.addFocusListener(new FocusListener() {
            //When the password entry text field gains focus
            @Override
            public void focusGained(final FocusEvent focusEvent) {
                //If the password entry text field displays "Enter password..."
                if (PasswordEntryField.getText().equals("Enter password...")) {
                    //Empties the password entry text field
                    PasswordEntryField.setText("");
                    //Changes password entry text field text colour to black
                    PasswordEntryField.setForeground(AssetsColorArray[3]);
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
        AccountRegistrationSecondaryPanel.add(PasswordEntryField, new Integer(2));
        //Sets the properties of the generate password button
        GeneratePasswordButton.setBorder(AssetsBorderArray[0]);
        GeneratePasswordButton.setBounds(756, 794, 120, 60);
        GeneratePasswordButton.setFont(AssetsFontArray[1]);
        GeneratePasswordButton.setBackground(AssetsColorArray[0]);
        GeneratePasswordButton.setFocusPainted(false);
        //Adds mouse press listener to generate password button
        GeneratePasswordButton.addActionListener((final ActionEvent ae) -> {
            //Generates password
            PasswordEntryField.setText(GeneratePassword());
            PasswordEntryField.setForeground(AssetsColorArray[3]);
        });
        //Adds hover listener to generate password button that highlights it
        AddButtonHoverColorHighlight(GeneratePasswordButton, null);
        //Adds generate password button at front most position
        AccountRegistrationSecondaryPanel.add(GeneratePasswordButton, new Integer(4));
        //Sets the properties of the password validation indicator
        PasswordValidationDisplay.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        PasswordValidationDisplay.setBounds(870, 794, 120, 60);
        PasswordValidationDisplay.setFont(AssetsFontArray[1]);
        PasswordValidationDisplay.setHorizontalAlignment(JTextField.CENTER);
        PasswordValidationDisplay.setEditable(false);
        PasswordValidationDisplay.setBackground(AssetsColorArray[2]);
        PasswordValidationDisplay.setText("Invalid");
        PasswordValidationDisplay.setFocusable(false);
        //Adds the password validation indicator at the front most position
        AccountRegistrationSecondaryPanel.add(PasswordValidationDisplay, new Integer(3));
    }

    private String GeneratePassword() {
        /*
        This method generates a random password
        It then returns the generated password
         */

        final Random Rand = new Random();
        //Calculates random password length
        int GeneratedPasswordLength = Rand.nextInt(4) + 8;
        String GeneratedPassword = "";
        //List of all potential password characters
        String AllCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\"!@#1234567890";
        //Loops through password characters
        for (int GeneratedPasswordIndex = 0; GeneratedPasswordIndex < GeneratedPasswordLength; GeneratedPasswordIndex++) {
            //Places random character from list of all potential password characters at generated password index
            GeneratedPassword = GeneratedPassword + AllCharacters.charAt(Rand.nextInt(AllCharacters.length()));
        }
        //Returns the generated password
        return GeneratedPassword;
    }

    private final JButton RegisterButton = new JButton("Register");
    private boolean ValidDetailsEntered = false;

    private void RegisterAndCancelButtonSetup() {
        /*
        This method sets the properties of the register and cancel buttons
        It then adds them to the account registration secondary panel
         */

        //Sets properties of register button
        RegisterButton.setFont(AssetsFontArray[1]);
        RegisterButton.setBounds(520, 864, 470, 60);
        RegisterButton.setBackground(AssetsColorArray[0]);
        RegisterButton.setBorder(AssetsBorderArray[0]);
        RegisterButton.setFocusPainted(false);
        //Adds mouse press listener to register button
        RegisterButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If valid details have been entered
                if (ValidDetailsEntered) {
                    RegisterButton.removeActionListener(RegisterButton.getActionListeners()[0]);
                    RegisterButton.setBackground(AssetsColorArray[1]);
                    //Locks text fields
                    UsernameEntryField.setEditable(false);
                    PasswordEntryField.setEditable(false);
                    //Adds "AddOrUpdateAccountsEntry" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("AddOrUpdateAccountsEntry", new UserAccountsTableEntry(NewAccountUsername, NewAccountPassword, NewAccountEmailAddress), null, null, "");
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Partial transition to title screen panel
                    Shape_Shooter_TD.MainClass.StartTitleWindow(2);
                }
            }
        });
        //Adds hover listener to register button that highlights it
        AddButtonHoverColorHighlight(RegisterButton, null);
        RegisterButton.setBackground(AssetsColorArray[2]);
        //Adds register button at front most position
        AccountRegistrationSecondaryPanel.add(RegisterButton, new Integer(2));

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
                //Partial transition to title screen panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(2);
            }
        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        AccountRegistrationSecondaryPanel.add(CancelButton, new Integer(2));
    }

    private void RegisterButtonUpdate() {
        /*
        This method checks whether or not valid details have been entered
        Prevents the user from registering an account if invalid details have been entered
         */

        //If valid details have been entered
        if (!NewAccountUsername.equals("") && !NewAccountPassword.equals("")) {
            //Unlocks register button
            RegisterButton.setBackground(AssetsColorArray[0]);
            ValidDetailsEntered = true;
        } //If invalid details have been entered
        else {
            //Locks register button
            RegisterButton.setBackground(AssetsColorArray[2]);
            ValidDetailsEntered = false;
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the account registration primary panel
        It then adds it to the main panel
         */

        //Sets account registration secondary panel properties
        AccountRegistrationSecondaryPanel.setBounds(0, 0, 1030, 964);
        //Adds the account registration secondary panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(AccountRegistrationSecondaryPanel, new Integer(1));
    }
}
