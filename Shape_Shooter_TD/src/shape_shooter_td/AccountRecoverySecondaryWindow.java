package shape_shooter_td;

import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final public class AccountRecoverySecondaryWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the account recovery secondary panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens account recovery secondary panel
        AccountRecoverySecondaryWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Account recovery secondary panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(AccountRecoverySecondaryPanel);

    }
    private final JLayeredPane AccountRecoverySecondaryPanel = new JLayeredPane();

    private void AccountRecoverySecondaryWindowSetup() {
        /*
        This method opens the account recovery secondary panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(AccountRecoverySecondaryPanel);
        //Setup details area components 
        DetailsAreaSetup();
        //Setup details display
        DetailsDisplaySetup();
        //Setup next button
        NextButtonSetup();
        //Setup account recovery secondary panel 
        FinalSetup();
    }

    private void DetailsAreaSetup() {
        /*
        This method sets the properties of details area components
        It then adds them to the account recovery secondary panel
         */

        //Sets the properties of the details area
        final JPanel DetailsArea = new JPanel();
        DetailsArea.setBorder(AssetsBorderArray[0]);
        DetailsArea.setBounds(20, 644, 990, 300);
        DetailsArea.setBackground(AssetsColorArray[0]);
        //Adds the details area at the back most position
        AccountRecoverySecondaryPanel.add(DetailsArea, new Integer(1));
        //Sets the properties of the details title
        final JTextField DetailsAreaTitle = new JTextField();
        DetailsAreaTitle.setBounds(20, 644, 990, 60);
        DetailsAreaTitle.setFont(AssetsFontArray[5]);
        DetailsAreaTitle.setEditable(false);
        DetailsAreaTitle.setText("Recovered account");
        DetailsAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        DetailsAreaTitle.setOpaque(false);
        DetailsAreaTitle.setBorder(null);
        //Adds the details title at the front most position
        AccountRecoverySecondaryPanel.add(DetailsAreaTitle, new Integer(2));
        //Sets the properties of the details description
        final JTextField DetailsDescription = new JTextField();
        DetailsDescription.setBounds(20, 688, 990, 36);
        DetailsDescription.setFont(AssetsFontArray[2]);
        DetailsDescription.setEditable(false);
        DetailsDescription.setText("Your account details are below");
        DetailsDescription.setHorizontalAlignment(JTextField.CENTER);
        DetailsDescription.setOpaque(false);
        DetailsDescription.setBorder(null);
        //Adds the details description at the front most position
        AccountRecoverySecondaryPanel.add(DetailsDescription, new Integer(2));
    }

    private void DetailsDisplaySetup() {
        /*
        This method sets the properties of the details display text fields
        It then adds them to the account recovery secondary panel
         */

        //Finds the user's account's details
        final UserAccountsTableEntry UserDetails = Shape_Shooter_TD.MainClass.Shape_Shooter_TD_Database.getCurrentUserAccountEntry();
        //Sets the properties of the username display text field
        final JTextField UsernameDisplayField = new JTextField();
        UsernameDisplayField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameDisplayField.setBounds(40, 724, 950, 60);
        UsernameDisplayField.setBackground(AssetsColorArray[0]);
        UsernameDisplayField.setForeground(AssetsColorArray[3]);
        UsernameDisplayField.setFont(AssetsFontArray[1]);
        UsernameDisplayField.setEditable(false);
        //Displays the user's account's username
        UsernameDisplayField.setText("Username: " + UserDetails.getUsername());
        //Adds the username display text field at the front most position
        AccountRecoverySecondaryPanel.add(UsernameDisplayField, new Integer(2));
        //Sets the properties of the password display text field
        final JTextField PasswordDisplayField = new JTextField();
        PasswordDisplayField.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        PasswordDisplayField.setBounds(40, 794, 950, 60);
        PasswordDisplayField.setBackground(AssetsColorArray[0]);
        PasswordDisplayField.setForeground(AssetsColorArray[3]);
        PasswordDisplayField.setFont(AssetsFontArray[1]);
        PasswordDisplayField.setEditable(false);
        //Displays the user's account's password
        PasswordDisplayField.setText("Password: " + UserDetails.getPassword());
        //Adds the password display text field at the front most position
        AccountRecoverySecondaryPanel.add(PasswordDisplayField, new Integer(2));
    }

    private void NextButtonSetup() {
        /*
        This method sets the properties of the next button
        It then adds it to the account recovery secondary panel
         */

        //Sets properties of the next button
        final JButton NextButton = new JButton("Next");
        NextButton.setFont(AssetsFontArray[1]);
        NextButton.setBounds(40, 864, 950, 60);
        NextButton.setBackground(AssetsColorArray[0]);
        NextButton.setBorder(AssetsBorderArray[0]);
        NextButton.setFocusPainted(false);
        //Adds mouse press listener to next button
        NextButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                NextButton.removeActionListener(NextButton.getActionListeners()[0]);
                NextButton.setBackground(AssetsColorArray[1]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to title panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(2);
            }

        });
        //Adds hover listener to next button that highlights it
        AddButtonHoverColorHighlight(NextButton, null);
        //Adds next button at front most position
        AccountRecoverySecondaryPanel.add(NextButton, new Integer(2));
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the account recovery secondary panel
        It then adds it to the main panel
         */

        //Sets account recovery secondary panel properties
        AccountRecoverySecondaryPanel.setBounds(0, 0, 1030, 964);
        //Adds the account recovery secondary panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(AccountRecoverySecondaryPanel, new Integer(1));
    }
}
