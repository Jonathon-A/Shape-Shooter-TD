package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class ChangeUsernameWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the change username panel panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens change username panel
        ChangeUsernameWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Change username panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(ChangeUsernamePanel);
    }

    private void ChangeUsernameWindowSetup() {
        /*
        This method opens the change username panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(ChangeUsernamePanel);
        //Setup change username area components 
        ChangeUsernameAreaSetup();
        //Setup username entry text field and the username validation indicator
        UsernameEntrySetup();
        //Setup change username and cancel buttons 
        ChangeUsernameAndCancelButtonSetup();
        //Setup change username panel 
        FinalSetup();
    }

    private final JLayeredPane ChangeUsernamePanel = new JLayeredPane();

    private void ChangeUsernameAreaSetup() {
        /*
        This method sets the properties of the change password area components
        It then adds them to the change username panel
         */

        //Sets the properties of the change username area
        final JPanel ChangeUsernameArea = new JPanel();
        ChangeUsernameArea.setBorder(AssetsBorderArray[0]);
        ChangeUsernameArea.setBounds(20, 714, 990, 230);
        ChangeUsernameArea.setBackground(AssetsColorArray[0]);
        //Adds the change username area at the back most position
        ChangeUsernamePanel.add(ChangeUsernameArea, new Integer(1));
        //Sets the properties of the change username title
        final JTextField ChangeUsernameTitle = new JTextField();
        ChangeUsernameTitle.setBounds(20, 714, 990, 60);
        ChangeUsernameTitle.setFont(AssetsFontArray[5]);
        ChangeUsernameTitle.setEditable(false);
        ChangeUsernameTitle.setText("Enter username");
        ChangeUsernameTitle.setHorizontalAlignment(JTextField.CENTER);
        ChangeUsernameTitle.setOpaque(false);
        ChangeUsernameTitle.setBorder(null);
        //Adds the change username title at the front most position
        ChangeUsernamePanel.add(ChangeUsernameTitle, new Integer(2));
        //Sets the properties of the change username description
        final JTextField ChangeUsernameDescription = new JTextField();
        ChangeUsernameDescription.setBounds(20, 758, 990, 36);
        ChangeUsernameDescription.setFont(AssetsFontArray[2]);
        ChangeUsernameDescription.setEditable(false);
        ChangeUsernameDescription.setText("Please enter a different unique username");
        ChangeUsernameDescription.setHorizontalAlignment(JTextField.CENTER);
        ChangeUsernameDescription.setOpaque(false);
        ChangeUsernameDescription.setBorder(null);
        //Adds the change username description at the front most position
        ChangeUsernamePanel.add(ChangeUsernameDescription, new Integer(2));
    }

    private final JTextField UsernameValidationDisplay = new JTextField();
    private final JTextField UsernameEntryField = new JTextField();

    private void UsernameEntrySetup() {
        /*
        This method sets the properties of the username entry text field and the username validation indicator
        This includes what happens when the user uses the text field
        It then adds it to the change username panel
         */

        //Sets the properties of the username entry text field
        UsernameEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameEntryField.setBounds(40, 794, 950, 60);
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
                //Checks validation of entered username
                UsernameEntryUpdate(UsernameEntryField.getText());
                ChangeUsernameButtonUpdate();
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
                //Checks validation of entered username
                UsernameEntryUpdate(UsernameEntryField.getText());
                ChangeUsernameButtonUpdate();
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
        ChangeUsernamePanel.add(UsernameEntryField, new Integer(2));
        //Sets the properties of the username validation indicator
        UsernameValidationDisplay.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameValidationDisplay.setBounds(870, 794, 120, 60);
        UsernameValidationDisplay.setFont(AssetsFontArray[1]);
        UsernameValidationDisplay.setHorizontalAlignment(JTextField.CENTER);
        UsernameValidationDisplay.setEditable(false);
        UsernameValidationDisplay.setBackground(AssetsColorArray[2]);
        UsernameValidationDisplay.setText("Invalid");
        UsernameValidationDisplay.setFocusable(false);
        //Adds the username validation indicator at the front most position
        ChangeUsernamePanel.add(UsernameValidationDisplay, new Integer(3));
    }

    private String NewAccountUsername = "";

    private void UsernameEntryUpdate(final String EnteredUsername) {
        /*
        This method validates the entered username
        It is called whenever the user enters anything into the username entry field
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
        final boolean ValidPasswordLength = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.ValidEncryptedLength(EnteredUsername,
                Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getPassword(), true);
        //If appropriate, valid, correct length, and unique username entered
        if (UniqueUsername && Appropriate && !EnteredUsername.equals("") && ValidUsernameLength && ValidPasswordLength) {
            UsernameValidationDisplay.setBackground(AssetsColorArray[1]);
            //Username validation indicator displays "Valid";
            UsernameValidationDisplay.setText("Valid");
            //currently entered username is valid
            NewAccountUsername = EnteredUsername;
        }//If used or inappropriate or too long username entered
        else {
            UsernameValidationDisplay.setBackground(AssetsColorArray[2]);
            //If username is empty
            if (EnteredUsername.equals("")) {
                //Username validation indicator displays "Invalid";
                UsernameValidationDisplay.setText("Invalid");
            } else {
                if (!ValidUsernameLength || !ValidPasswordLength) {
                    //Username validation indicator displays "Long";
                    UsernameValidationDisplay.setText("Long");
                } else {
                    //If username is used
                    if (Appropriate) {
                        //If the entered username is the same as the accounts current username
                        if (EnteredUsername.equals(Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry().getUsername())) {
                            //Username validation indicator displays "Same";
                            UsernameValidationDisplay.setText("Same");
                        } //If the entered username is not the same as the accounts current username
                        else {
                            //Username validation indicator displays "Taken";
                            UsernameValidationDisplay.setText("Taken");
                        }
                    } //If username is inappropriate
                    else {
                        //Username validation indicator displays "Banned";
                        UsernameValidationDisplay.setText("Banned");
                    }
                }
            }
            //currently entered username is invalid
            NewAccountUsername = "";
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

    private final JButton ChangeUsernameButton = new JButton("Change username");
    private boolean ValidDetailsEntered = false;

    private void ChangeUsernameAndCancelButtonSetup() {
        /*
        This method sets the properties of the change username and cancel buttons
        It then adds them to the change username panel
         */

        //Sets properties of change username button
        ChangeUsernameButton.setFont(AssetsFontArray[1]);
        ChangeUsernameButton.setBounds(520, 864, 470, 60);
        ChangeUsernameButton.setBackground(AssetsColorArray[0]);
        ChangeUsernameButton.setBorder(AssetsBorderArray[0]);
        ChangeUsernameButton.setFocusPainted(false);
        //Adds mouse press listener to change username button
        ChangeUsernameButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If valid details have been entered
                if (ValidDetailsEntered) {
                    ChangeUsernameButton.removeActionListener(ChangeUsernameButton.getActionListeners()[0]);
                    ChangeUsernameButton.setBackground(AssetsColorArray[1]);
                    //Locks text field
                    UsernameEntryField.setEditable(false);
                    //Updates username in all tables
                    Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.UpdateUsername(Shape_Shooter_TD.MainClass.getUsername(), NewAccountUsername);
                    //Adds "FindCurrentUserAccountEntryFromUsername" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserAccountEntryFromUsername", null, null, null, UsernameEntryField.getText());
                    //Add "FindCurrentUserSettingsEntry" operation to operation queue
                    Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserSettingsEntry", null, null, null, UsernameEntryField.getText());
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
        //Adds hover listener to change username button that highlights it
        AddButtonHoverColorHighlight(ChangeUsernameButton, null);
        ChangeUsernameButton.setBackground(AssetsColorArray[2]);
        //Adds change username button at front most position
        ChangeUsernamePanel.add(ChangeUsernameButton, new Integer(2));

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
        //Adds cancel button at front most positions
        ChangeUsernamePanel.add(CancelButton, new Integer(2));
    }

    private void ChangeUsernameButtonUpdate() {
        /*
        This method checks whether or not a valid username has been entered
        Prevents the user from changing their account's username if invalid details have been entered
         */

        //If valid username has been entered
        if (!NewAccountUsername.equals("")) {
            //Unlocks register button
            ChangeUsernameButton.setBackground(AssetsColorArray[0]);
            ValidDetailsEntered = true;
        } //If invalid username has been entered
        else {
            //Locks register button
            ChangeUsernameButton.setBackground(AssetsColorArray[2]);
            ValidDetailsEntered = false;
        }
    }

    private void FinalSetup() {
         /*
        This method sets the properties of the change username panel
        It then adds it to the main panel
         */

        //Sets change username panel properties
        ChangeUsernamePanel.setBounds(0, 0, 1030, 964);
        //Adds the change username panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(ChangeUsernamePanel, new Integer(1));
    }
}
