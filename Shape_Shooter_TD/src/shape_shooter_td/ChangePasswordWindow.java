package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class ChangePasswordWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the change password panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens change password panel
        ChangePasswordWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Change password panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(ChangePasswordPanel);
    }

    private void ChangePasswordWindowSetup() {
        /*
        This method opens the change password panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(ChangePasswordPanel);
        //Setup change password area components 
        ChangePasswordAreaSetup();
        //Setup password entry text field, generate password button, and the password validation indicator
        PasswordEntrySetup();
        //Setup change password and cancel buttons 
        ChangePasswordAndCancelButtonsSetup();
        //Setup change password panel 
        FinalSetup();
    }

    private final JLayeredPane ChangePasswordPanel = new JLayeredPane();

    private void ChangePasswordAreaSetup() {
        /*
        This method sets the properties of the change password area components
        It then adds them to the change password panel
         */

        //Sets the properties of the change password area
        final JPanel ChangePasswordArea = new JPanel();
        ChangePasswordArea.setBorder(AssetsBorderArray[0]);
        ChangePasswordArea.setBounds(20, 714, 990, 230);
        ChangePasswordArea.setBackground(AssetsColorArray[0]);
        //Adds the change password area at the back most position
        ChangePasswordPanel.add(ChangePasswordArea, new Integer(1));
        //Sets the properties of the change password title
        final JTextField ChangePasswordTitle = new JTextField();
        ChangePasswordTitle.setBounds(20, 714, 990, 60);
        ChangePasswordTitle.setFont(AssetsFontArray[5]);
        ChangePasswordTitle.setEditable(false);
        ChangePasswordTitle.setText("Enter password");
        ChangePasswordTitle.setHorizontalAlignment(JTextField.CENTER);
        ChangePasswordTitle.setOpaque(false);
        ChangePasswordTitle.setBorder(null);
        //Adds the change password title at the front most position
        ChangePasswordPanel.add(ChangePasswordTitle, new Integer(2));
        //Sets the properties of the change password description
        final JTextField ChangePasswordDescription = new JTextField();
        ChangePasswordDescription.setBounds(20, 758, 990, 36);
        ChangePasswordDescription.setFont(AssetsFontArray[2]);
        ChangePasswordDescription.setEditable(false);
        ChangePasswordDescription.setText("Please enter or generate a different password");
        ChangePasswordDescription.setHorizontalAlignment(JTextField.CENTER);
        ChangePasswordDescription.setOpaque(false);
        ChangePasswordDescription.setBorder(null);
        //Adds the change password description at the front most position
        ChangePasswordPanel.add(ChangePasswordDescription, new Integer(2));
    }

    private final JButton GeneratePasswordButton = new JButton("Generate");
    private final JTextField PasswordValidationDisplay = new JTextField();
    private final JTextField PasswordEntryField = new JTextField();
    private String NewAccountPassword = "";

    private void PasswordEntrySetup() {
        /*
        This method sets the properties of the password entry text field, generate password button, and the password validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the change password panel
         */

        //Sets the properties of the password entry text field
        PasswordEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        PasswordEntryField.setBounds(40, 794, 950, 60);
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
                //Checks validation of entered password
                PasswordEntryUpdate(PasswordEntryField.getText());
                ChangePasswordButtonUpdate();
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
                PasswordEntryUpdate(PasswordEntryField.getText());
                ChangePasswordButtonUpdate();
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
        ChangePasswordPanel.add(PasswordEntryField, new Integer(2));
        //Sets the properties of the generate password button
        GeneratePasswordButton.setBorder(AssetsBorderArray[0]);
        GeneratePasswordButton.setBounds(756, 794, 120, 60);
        GeneratePasswordButton.setFont(AssetsFontArray[1]);
        GeneratePasswordButton.setBackground(AssetsColorArray[0]);
        GeneratePasswordButton.setFocusPainted(false);
        //Adds mouse press listener to generate password button
        GeneratePasswordButton.addActionListener((final ActionEvent ae) -> {
            //Generates passwords
            PasswordEntryField.setText(GeneratePassword());
            PasswordEntryField.setForeground(AssetsColorArray[3]);
        });
        //Adds hover listener to generate password button that highlights it
        AddButtonHoverColorHighlight(GeneratePasswordButton, null);
        //Adds generate password button at front most position
        ChangePasswordPanel.add(GeneratePasswordButton, new Integer(4));
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
        ChangePasswordPanel.add(PasswordValidationDisplay, new Integer(3));
    }

    private String GeneratePassword() {
        /*
        This method generates a random password
        It then returns the generated password
         */

        String GeneratedPassword = "";
        final Random Rand = new Random();
        do {
            //Calculates random password length
            int GeneratedPasswordLength = Rand.nextInt(4) + 8;
            //List of all potential password characters
            String AllCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\"!@#1234567890";
            //Loops through password characters
            for (int GeneratedPasswordIndex = 0; GeneratedPasswordIndex < GeneratedPasswordLength; GeneratedPasswordIndex++) {
                //Places random character from list of all potential password characters at generated password index
                GeneratedPassword = GeneratedPassword + AllCharacters.charAt(Rand.nextInt(AllCharacters.length()));
            }
        } //Generates password again if it matches the users current password
        while (GeneratedPassword.equals(Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getPassword()));
        //Returns the generated password
        return GeneratedPassword;
    }

    private void PasswordEntryUpdate(final String EnteredPassword) {
        /*
        This method validates the entered password
        It is called whenever the user enters anything into the password entry field
         */

        //Checks if entered password differs from the account's currently password
        final boolean DifferentPassword = !EnteredPassword.equals(Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getPassword());
        //Checks if encrypted password is less than 65 characters long
        final boolean ValidPasswordLength = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.ValidEncryptedLength(Shape_Shooter_TD.MainClass.getUsername(), EnteredPassword, true);
        //If valid and correct length password entered
        if (!EnteredPassword.equals("") && !EnteredPassword.equals("Enter password...") && ValidPasswordLength && DifferentPassword) {
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
                //If the entered password is the same as the accounts current password
                if (!DifferentPassword) {
                    //Password validation indicator displays "Same";
                    PasswordValidationDisplay.setText("Same");
                }//If the entered password is not the same as the accounts current password
                else {
                    ///Password validation indicator displays "Invalid";
                    PasswordValidationDisplay.setText("Invalid");
                }
            }
            //Current entered password is invalid
            NewAccountPassword = "";
        }
    }

    private final JButton ChangePasswordButton = new JButton("Change password");
    private boolean ValidDetailsEntered = false;

    private void ChangePasswordAndCancelButtonsSetup() {
        /*
        This method sets the properties of the change password and cancel buttons
        It then adds them to the change password panel
         */

        //Sets properties of change password button
        ChangePasswordButton.setFont(AssetsFontArray[1]);
        ChangePasswordButton.setBounds(520, 864, 470, 60);
        ChangePasswordButton.setBackground(AssetsColorArray[0]);
        ChangePasswordButton.setBorder(AssetsBorderArray[0]);
        ChangePasswordButton.setFocusPainted(false);
        //Adds mouse press listener to change password button
        ChangePasswordButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If valid details have been entered
                if (ValidDetailsEntered) {
                    ChangePasswordButton.removeActionListener(ChangePasswordButton.getActionListeners()[0]);
                    ChangePasswordButton.setBackground(AssetsColorArray[1]);
                    //Locks text field
                    PasswordEntryField.setEditable(false);
                    UserAccountsTableEntry ChangedUserAccountsTableEntry = new UserAccountsTableEntry(Shape_Shooter_TD.MainClass.getUsername(),
                            NewAccountPassword, Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getEmailAddress());
                    //Adds "AddOrUpdateAccountsEntry" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("AddOrUpdateAccountsEntry", ChangedUserAccountsTableEntry, null, null, "");
                    //Adds "FindCurrentUserAccountEntryFromUsername" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserAccountEntryFromUsername", null, null, null, Shape_Shooter_TD.MainClass.getUsername());
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Full transition to settings panel
                    Shape_Shooter_TD.MainClass.StartSettingsWindow(0);
                }
            }
        });
        //Adds hover listener to change password button that highlights it
        AddButtonHoverColorHighlight(ChangePasswordButton, null);
        ChangePasswordButton.setBackground(AssetsColorArray[2]);
        //Adds change password button at front most position
        ChangePasswordPanel.add(ChangePasswordButton, new Integer(2));

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
                //Full transition to settings panel
                Shape_Shooter_TD.MainClass.StartSettingsWindow(0);
            }
        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        ChangePasswordPanel.add(CancelButton, new Integer(2));
    }

    private void ChangePasswordButtonUpdate() {
        /*
        This method checks whether or not a valid password has been entered
        Prevents the user from changing their account's password if invalid details have been entered
         */

        //If valid password has been entered
        if (!NewAccountPassword.equals("")) {
            //Unlocks register button
            ChangePasswordButton.setBackground(AssetsColorArray[0]);
            ValidDetailsEntered = true;
        } //If invalid password has been entered
        else {
            //Locks register button
            ChangePasswordButton.setBackground(AssetsColorArray[2]);
            ValidDetailsEntered = false;
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the change password panel
        It then adds it to the main panel
         */

        //Sets change password panel properties
        ChangePasswordPanel.setBounds(0, 0, 1030, 964);
        //Adds the change password panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(ChangePasswordPanel, new Integer(1));
    }
}
