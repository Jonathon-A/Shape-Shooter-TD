package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class AccountRegistrationPrimaryWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the account registration primary panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens account recovery primary panel
        AccountRegistrationPrimaryWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Account registration primary panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(AccountRegistrationPrimaryPanel);
    }

    private final JLayeredPane AccountRegistrationPrimaryPanel = new JLayeredPane();

    private void AccountRegistrationPrimaryWindowSetup() {
        /*
        This method opens the account registration primary panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(AccountRegistrationPrimaryPanel);
        //Setup email and confirmation code entry area components 
        EnterEmailAndConfirmationCodeAreaSetup();
        //Setup email address entry text field and the email address validation indicator
        EmailAddressEntrySetup();
        //Setup confirmation code entry text field and confirmation code send button
        ConfirmationCodeEntrySetup();
        //Setup next and cancel buttons 
        NextAndCancelButtonSetup();
        //Setup account registration primary panel 
        FinalSetup();

    }

    private void EnterEmailAndConfirmationCodeAreaSetup() {
        /*
        This method sets the properties of the email and confirmation code entry area components
        It then adds them to the account registration primary panel
         */

        //Sets the properties of the email and confirmation code entry area
        final JPanel EnterEmailAndConfirmationCodeArea = new JPanel();
        EnterEmailAndConfirmationCodeArea.setBorder(AssetsBorderArray[0]);
        EnterEmailAndConfirmationCodeArea.setBounds(20, 644, 990, 300);
        EnterEmailAndConfirmationCodeArea.setBackground(AssetsColorArray[0]);
        //Adds the email and confirmation code entry area at the back most position
        AccountRegistrationPrimaryPanel.add(EnterEmailAndConfirmationCodeArea, new Integer(1));
        //Sets the properties of the email and confirmation code entry title
        final JTextField EnterEmailAndConfirmationCodeTitle = new JTextField();
        EnterEmailAndConfirmationCodeTitle.setBounds(20, 644, 990, 60);
        EnterEmailAndConfirmationCodeTitle.setFont(AssetsFontArray[5]);
        EnterEmailAndConfirmationCodeTitle.setEditable(false);
        EnterEmailAndConfirmationCodeTitle.setText("Enter email address and confirm ownership");
        EnterEmailAndConfirmationCodeTitle.setHorizontalAlignment(JTextField.CENTER);
        EnterEmailAndConfirmationCodeTitle.setOpaque(false);
        EnterEmailAndConfirmationCodeTitle.setBorder(null);
        //Adds the email and confirmation code entry title at the front most position
        AccountRegistrationPrimaryPanel.add(EnterEmailAndConfirmationCodeTitle, new Integer(2));
        //Sets the properties of the email and confirmation code entry description
        final JTextField EnterEmailAndConfirmationCodeDescription = new JTextField();
        EnterEmailAndConfirmationCodeDescription.setBounds(20, 688, 990, 36);
        EnterEmailAndConfirmationCodeDescription.setFont(AssetsFontArray[2]);
        EnterEmailAndConfirmationCodeDescription.setEditable(false);
        EnterEmailAndConfirmationCodeDescription.setText("Please enter a unique email address and confirmation code that will be emailed to you");
        EnterEmailAndConfirmationCodeDescription.setHorizontalAlignment(JTextField.CENTER);
        EnterEmailAndConfirmationCodeDescription.setOpaque(false);
        EnterEmailAndConfirmationCodeDescription.setBorder(null);
        //Adds the email and confirmation code entry description at the front most position
        AccountRegistrationPrimaryPanel.add(EnterEmailAndConfirmationCodeDescription, new Integer(2));
    }

    private final JTextField EmailAddressValidationDisplay = new JTextField();
    private final JTextField EmailAddressEntryField = new JTextField();

    private void EmailAddressEntrySetup() {
        /*
        This method sets the properties of the email address entry text field and the email address validation indicator
        This includes what happens when the user uses the text field
        It then adds them to the account registration primary panel
         */

        //Sets the properties of the email address entry text field
        EmailAddressEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        EmailAddressEntryField.setBounds(40, 724, 836, 60);
        EmailAddressEntryField.setBackground(AssetsColorArray[0]);
        EmailAddressEntryField.setForeground(AssetsColorArray[8]);
        EmailAddressEntryField.setFont(AssetsFontArray[1]);
        EmailAddressEntryField.setText("Enter email address...");
        //Adds use listener to the email address entry text field
        EmailAddressEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the email address entry text field
            @Override
            public void insertUpdate(final DocumentEvent de) {
                //Checks validation of entered email address
                EmailAddressEntryUpdate(EmailAddressEntryField.getText());
            }

            //When a character is removed from the email address entry text field
            @Override
            public void removeUpdate(final DocumentEvent de) {
                //Checks validation of entered email address
                EmailAddressEntryUpdate(EmailAddressEntryField.getText());
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the email address entry text field
        EmailAddressEntryField.addFocusListener(new FocusListener() {
            //When the email address entry text field gains focus
            @Override
            public void focusGained(final FocusEvent focusEvent) {
                //If the email address entry text field displays "Enter email address..."
                if (EmailAddressEntryField.getText().equals("Enter email address...")) {
                    //Empties the email address entry text field
                    EmailAddressEntryField.setText("");
                    //Changes email address entry text field text colour to black
                    EmailAddressEntryField.setForeground(AssetsColorArray[3]);
                }
            }

            //When the username entry text field loses focus
            @Override
            public void focusLost(final FocusEvent focusEvent) {
                //If the email address entry text field is empty
                if (EmailAddressEntryField.getText().isEmpty()) {
                    //Email address entry text field displays "Enter email address..."
                    EmailAddressEntryField.setText("Enter email address...");
                    //Changes email address entry text field text colour to grey
                    EmailAddressEntryField.setForeground(AssetsColorArray[8]);
                }
            }
        });
        //Adds the email address entry text field at the front most position
        AccountRegistrationPrimaryPanel.add(EmailAddressEntryField, new Integer(2));
        //Sets the properties of the email address validation indicator
        EmailAddressValidationDisplay.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        EmailAddressValidationDisplay.setBounds(870, 724, 120, 60);
        EmailAddressValidationDisplay.setFont(AssetsFontArray[1]);
        EmailAddressValidationDisplay.setHorizontalAlignment(JTextField.CENTER);
        EmailAddressValidationDisplay.setEditable(false);
        EmailAddressValidationDisplay.setBackground(AssetsColorArray[2]);
        EmailAddressValidationDisplay.setText("Invalid");
        EmailAddressValidationDisplay.setFocusable(false);
        //Adds the email address validation indicator at the front most position
        AccountRegistrationPrimaryPanel.add(EmailAddressValidationDisplay, new Integer(3));
    }

    private String ValidEmailAddress = null;

    private void EmailAddressEntryUpdate(final String EnteredEmailAddress) {
        /*
        This method validates the entered email address
        It is called whenever the user enters anything into the email address entry field
         */

        //Checks if email is unique
        final boolean UniqueEmailAddress = !Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.FindAccountWithEmailAddress(EnteredEmailAddress);
        //Checks if encrypted email is less than 65 characters long
        final boolean ValidEmailAddressLength = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.ValidEncryptedLength("", EnteredEmailAddress, false);
        //Checks if email has valid format
        //Pattern from http://regexlib.com/Search.aspx?k=email
        final Pattern regExPattern = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2}|aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel)$");
        final Matcher matcher = regExPattern.matcher(EnteredEmailAddress);
        //If valid and unique email address entered
        if (matcher.matches() && UniqueEmailAddress && !EnteredEmailAddress.equals("") && ValidEmailAddressLength) {
            EmailAddressValidationDisplay.setBackground(AssetsColorArray[1]);
            ConfirmationCodeSendButton.setBackground(AssetsColorArray[1]);
            //Indicator displays "Valid";
            EmailAddressValidationDisplay.setText("Valid");
            //Generates confirmation code
            ConfirmationCode = GenerateConfirmationCode();
            //Entered email address is valid
            ValidEmailAddress = EnteredEmailAddress;
        } //If invalid or used email address entered
        else {
            EmailAddressValidationDisplay.setBackground(AssetsColorArray[2]);
            ConfirmationCodeSendButton.setBackground(AssetsColorArray[2]);
            //If invalid email
            if (UniqueEmailAddress) {
                //If email has incorrect format 
                if (!matcher.matches()) {
                    //Indicator displays "Invalid";
                    EmailAddressValidationDisplay.setText("Invalid");
                } //If email has correct format
                else {
                    //Indicator displays "Long";
                    EmailAddressValidationDisplay.setText("Long");
                }
            }//If used email address
            else {
                //Indicator displays "Taken";
                EmailAddressValidationDisplay.setText("Taken");
            }
            //Entered email address is not valid
            ValidEmailAddress = null;
        }
        //Updates confirmation code send  and next button
        ConfirmationCodeSendButton.setText("Send");
        //Incorrect confirmation code is entered
        NextButtonUpdate("");
        //Plays key press sound effect
        if (!"Enter email address...".equals(EnteredEmailAddress)) {
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
        }
    }

    private final JButton ConfirmationCodeSendButton = new JButton("Send");
    private final JTextField ConfirmationCodeEntryField = new JTextField();

    private void ConfirmationCodeEntrySetup() {
        /*
        This method sets the properties of the confirmation code entry text field and the confirmation code send button
        This includes what happens when the user uses the text field
        It then adds them to the account registration primary panel
         */

        //Sets the properties of the confirmation code entry text field
        ConfirmationCodeEntryField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        ConfirmationCodeEntryField.setBounds(40, 794, 836, 60);
        ConfirmationCodeEntryField.setBackground(AssetsColorArray[0]);
        ConfirmationCodeEntryField.setForeground(AssetsColorArray[8]);
        ConfirmationCodeEntryField.setFont(AssetsFontArray[1]);
        ConfirmationCodeEntryField.setText("Enter confirmation code...");
        //Adds use listener to the confirmation code entry text field
        ConfirmationCodeEntryField.getDocument().addDocumentListener(new DocumentListener() {
            //When a character is inserted into the confirmation code entry text field
            @Override
            public void insertUpdate(final DocumentEvent de) {
                //Checks if correct confirmation code is entered
                NextButtonUpdate(ConfirmationCodeEntryField.getText());
                //If the confirmation code entry text field does not display "Enter confirmation code..."
                if (!"Enter confirmation code...".equals(ConfirmationCodeEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
            }

            //When a character is removed from the confirmation code entry text field
            @Override
            public void removeUpdate(final DocumentEvent de) {
                //Checks if correct confirmation code is entered
                NextButtonUpdate(ConfirmationCodeEntryField.getText());
                //If the confirmation code entry text field does not display "Enter confirmation code..."
                if (!"Enter confirmation code...".equals(ConfirmationCodeEntryField.getText())) {
                    //Plays key press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[0]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                }
            }

            @Override
            public void changedUpdate(final DocumentEvent de) {
            }
        });
        //Adds focus listener to the confirmation code entry text field
        ConfirmationCodeEntryField.addFocusListener(new FocusListener() {
            //When the confirmation code entry text field gains focus
            @Override
            public void focusGained(final FocusEvent focusEvent) {
                //If the confirmation code entry text field displays "Enter confirmation code..."
                if (ConfirmationCodeEntryField.getText().equals("Enter confirmation code...")) {
                    //Empties the confirmation code entry text field
                    ConfirmationCodeEntryField.setText("");
                    //Changes confirmation code entry text field text colour to black
                    ConfirmationCodeEntryField.setForeground(AssetsColorArray[3]);
                }
            }

            //When the confirmation code entry text field loses focus
            @Override
            public void focusLost(final FocusEvent focusEvent) {
                //If the confirmation code entry text field is empty
                if (ConfirmationCodeEntryField.getText().isEmpty()) {
                    //Confirmation code entry text field displays "Enter confirmation code..."
                    ConfirmationCodeEntryField.setText("Enter confirmation code...");
                    //Changes confirmation code entry text field text colour to grey
                    ConfirmationCodeEntryField.setForeground(AssetsColorArray[8]);
                }
            }
        });
        //Adds the confirmation code entry text field at the front most position
        AccountRegistrationPrimaryPanel.add(ConfirmationCodeEntryField, new Integer(2));
        //Sets the properties of the confirmation code send button
        ConfirmationCodeSendButton.setBorder(AssetsBorderArray[0]);
        ConfirmationCodeSendButton.setBounds(870, 794, 120, 60);
        ConfirmationCodeSendButton.setFont(AssetsFontArray[1]);
        ConfirmationCodeSendButton.setBackground(AssetsColorArray[1]);
        ConfirmationCodeSendButton.setFocusPainted(false);
        //Adds mouse press listener to confirmation code send button
        ConfirmationCodeSendButton.addActionListener((final ActionEvent ae) -> {
            //If a valid email address is entered
            if (ValidEmailAddress != null) {
                //Sends a message containing the confirmation code via email to the entered email address
                ConfirmationCodeSendButton.setText("Resend");
                Shape_Shooter_TD.MainClass.SendConfirmationEmail(ValidEmailAddress, "Use the following 5 digit pin to confirm ownership of this email address: " + ConfirmationCode
                        + "\nPlease be aware that the confirmation pin is only valid for the email address entered at the time of sending and will become invalid if a different address is entered.");
            }
        });
        //Adds hover listener to confirmation code send button that highlights it
        AddButtonHoverColorHighlight(ConfirmationCodeSendButton, null);
        ConfirmationCodeSendButton.setBackground(AssetsColorArray[2]);
        //Adds the confirmation code send button at the front most position
        AccountRegistrationPrimaryPanel.add(ConfirmationCodeSendButton, new Integer(3));
    }

    private int ConfirmationCode = 0;
    private final Random Rand = new Random();

    private int GenerateConfirmationCode() {
        /*
        This method generates a random 5 digit number 
        This number differs from the current confirmation code
         */

        //Generates a random 5 digit number 
        int NewConfirmationCode = 0;
        do {
            NewConfirmationCode = Rand.nextInt(89999) + 10000;
        } //Generates a random 5 digit number again if it matches the current confirmation code
        while (NewConfirmationCode == ConfirmationCode);
        //Returns the generated 5 digit number
        return NewConfirmationCode;
    }

    private final JButton NextButton = new JButton("Next");
    private boolean ValidDetailsEntered = false;

    private void NextAndCancelButtonSetup() {
        /*
        This method sets the properties of the next and cancel buttons
        It then adds them to the account registration primary panel
         */

        //Sets properties of the next button
        NextButton.setFont(AssetsFontArray[1]);
        NextButton.setBounds(520, 864, 470, 60);
        NextButton.setBackground(AssetsColorArray[0]);
        NextButton.setBorder(AssetsBorderArray[0]);
        NextButton.setFocusPainted(false);
        //Adds mouse press listener to next button
        NextButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If valid details have been entered
                if (ValidDetailsEntered) {
                    NextButton.removeActionListener(NextButton.getActionListeners()[0]);
                    NextButton.setBackground(AssetsColorArray[1]);
                    //Locks text fields
                    EmailAddressEntryField.setEditable(false);
                    ConfirmationCodeEntryField.setEditable(false);
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Partial transition to account registration secondary panel
                    Shape_Shooter_TD.MainClass.StartAccountRegistrationSecondaryWindow(ValidEmailAddress, 2);
                }
            }
        });
        //Adds hover listener to next button that highlights it
        AddButtonHoverColorHighlight(NextButton, null);
        NextButton.setBackground(AssetsColorArray[2]);
        //Adds next button at front most position
        AccountRegistrationPrimaryPanel.add(NextButton, new Integer(2));

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
        AccountRegistrationPrimaryPanel.add(CancelButton, new Integer(2));
    }

    private void NextButtonUpdate(final String EnteredConfirmationCode) {
        /*
        This method checks whether or not the correct confirmation code has been entered
        Prevents the user from continuing with account registration if an incorrect confirmation code has been entered
         */

        //If the correct confirmation code has been entered
        if (EnteredConfirmationCode.equals(String.valueOf(ConfirmationCode))) {
            //Unlocks view account details button
            NextButton.setBackground(AssetsColorArray[0]);
            ValidDetailsEntered = true;
        } //If the correct confirmation code has not been entered
        else {
            //Locks view account details button
            NextButton.setBackground(AssetsColorArray[2]);
            ValidDetailsEntered = false;
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the account registration primary panel
        It then adds it to the main panel
         */

        //Sets account registration primary panel properties
        AccountRegistrationPrimaryPanel.setBounds(0, 0, 1030, 964);
        //Adds the account registration primary panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(AccountRegistrationPrimaryPanel, new Integer(1));
    }

}
