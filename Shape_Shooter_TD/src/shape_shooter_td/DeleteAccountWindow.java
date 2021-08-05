package shape_shooter_td;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final public class DeleteAccountWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the delete account panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens delete account panel
        DeleteAccountWindowSetup();
        //Removes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Delete account panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(DeleteAccountPanel);
    }

    private final JLayeredPane DeleteAccountPanel = new JLayeredPane();

    private void DeleteAccountWindowSetup() {
        /*
        This method opens the delete account panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(DeleteAccountPanel);
        //Setup delete account area components 
        DeleteAccountAreaSetup();
        //Setup delete account and cancel buttons 
        DeleteAccountAndCancelButtonSetup();
        //Setup delete account panel 
        FinalSetup();
    }

    private void DeleteAccountAreaSetup() {
        /*
        This method sets the properties of the change password area components
        It then adds them to the delete account panel
         */

        //Sets the properties of the delete account area
        final JPanel DeleteAccountArea = new JPanel();
        DeleteAccountArea.setBorder(AssetsBorderArray[0]);
        DeleteAccountArea.setBounds(20, 714, 990, 230);
        DeleteAccountArea.setBackground(AssetsColorArray[0]);
        //Adds the delete account area at the back most position
        DeleteAccountPanel.add(DeleteAccountArea, new Integer(1));
        //Sets the properties of the delete account title
        final JTextField DeleteAccountTitle = new JTextField();
        DeleteAccountTitle.setBounds(20, 714, 990, 60);
        DeleteAccountTitle.setFont(AssetsFontArray[5]);
        DeleteAccountTitle.setEditable(false);
        DeleteAccountTitle.setText("Are you sure you want to delete your account?");
        DeleteAccountTitle.setHorizontalAlignment(JTextField.CENTER);
        DeleteAccountTitle.setOpaque(false);
        DeleteAccountTitle.setBorder(null);
        //Adds the delete account title at the front most position
        DeleteAccountPanel.add(DeleteAccountTitle, new Integer(2));
        //Sets the properties of the start of the delete account description
        final JTextField DeleteAccountDescriptionPart1 = new JTextField();
        DeleteAccountDescriptionPart1.setBounds(20, 758, 990, 36);
        DeleteAccountDescriptionPart1.setFont(AssetsFontArray[2]);
        DeleteAccountDescriptionPart1.setEditable(false);
        DeleteAccountDescriptionPart1.setText("If you continue to delete your account, all account info, settings info, and");
        DeleteAccountDescriptionPart1.setHorizontalAlignment(JTextField.CENTER);
        DeleteAccountDescriptionPart1.setOpaque(false);
        DeleteAccountDescriptionPart1.setBorder(null);
        //Adds the start of the delete account description at the front most position
        DeleteAccountPanel.add(DeleteAccountDescriptionPart1, new Integer(2));
        //Sets the properties of the end of the delete account description
        final JTextField DeleteAccountDescriptionPart2 = new JTextField();
        DeleteAccountDescriptionPart2.setBounds(20, 782, 990, 36);
        DeleteAccountDescriptionPart2.setFont(AssetsFontArray[2]);
        DeleteAccountDescriptionPart2.setEditable(false);
        DeleteAccountDescriptionPart2.setText("leaderboard entries for this account will be deleted");
        DeleteAccountDescriptionPart2.setHorizontalAlignment(JTextField.CENTER);
        DeleteAccountDescriptionPart2.setOpaque(false);
        DeleteAccountDescriptionPart2.setBorder(null);
        //Adds the end of the delete account description at the front most position
        DeleteAccountPanel.add(DeleteAccountDescriptionPart2, new Integer(3));
    }

    private void DeleteAccountAndCancelButtonSetup() {
        /*
        This method sets the properties of the delete account and cancel buttons
        It then adds them to the delete account panel
         */

        //Sets properties of delete account button
        final JButton DeleteAccountButton = new JButton("Delete account");
        DeleteAccountButton.setFont(AssetsFontArray[1]);
        DeleteAccountButton.setBounds(520, 864, 470, 60);
        DeleteAccountButton.setBackground(AssetsColorArray[0]);
        DeleteAccountButton.setBorder(AssetsBorderArray[0]);
        DeleteAccountButton.setFocusPainted(false);
        //Adds mouse press listener to delete account button
        DeleteAccountButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                DeleteAccountButton.removeActionListener(DeleteAccountButton.getActionListeners()[0]);
                DeleteAccountButton.setBackground(AssetsColorArray[1]);
                //Adds "DeleteAccount" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("DeleteAccount", null, null, null, Shape_Shooter_TD.MainClass.getUsername());
                //Adds "FindCurrentUserAccountEntryFromUsername" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserAccountEntryFromUsername", null, null, null, "");
                //Adds "FindCurrentUserSettingsEntry" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserSettingsEntry", null, null, null, "");
                UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), false, false, true, 3, 0, 50, false, false);
                //Adds "UpdateSettings" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Partial transition to title panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(2);
            }
        });
        //Adds hover listener to delete account button that highlights it
        AddButtonHoverColorHighlight(DeleteAccountButton, null);
        //Adds delete account button at front most position
        DeleteAccountPanel.add(DeleteAccountButton, new Integer(2));

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
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to settings panel
                Shape_Shooter_TD.MainClass.StartSettingsWindow(0);
            }
        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        DeleteAccountPanel.add(CancelButton, new Integer(2));
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the delete account panel
        It then adds it to the main panel
         */

        //Sets delete account panel properties
        DeleteAccountPanel.setBounds(0, 0, 1030, 964);
        //Adds the delete account panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(DeleteAccountPanel, new Integer(1));
    }
}
