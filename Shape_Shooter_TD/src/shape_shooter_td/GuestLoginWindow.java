package shape_shooter_td;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final public class GuestLoginWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the guest login panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens guest login panel 
        GuestLoginWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Guest login panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(GuestLoginPanel);
    }

    private final JLayeredPane GuestLoginPanel = new JLayeredPane();

    private void GuestLoginWindowSetup() {
        /*
        This method opens the guest login panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transition is opaque
         */

        //Adds title image
        TitleSetup(GuestLoginPanel);
        //Setup guest login confirmation area components 
        GuestLoginConfirmationAreaSetup();
        //Setup guest login and cancel buttons 
        GuestLoginAndCancelButtonSetup();
        //Setup guest login panel 
        FinalSetup();
    }

    private void GuestLoginConfirmationAreaSetup() {
        /*
        This method sets the properties of the guest login confirmation area components
        It then adds them to the guest login panel
         */

        //Sets the properties of the guest login confirmation area
        final JPanel GuestLoginConfirmationArea = new JPanel();
        GuestLoginConfirmationArea.setBorder(AssetsBorderArray[0]);
        GuestLoginConfirmationArea.setBounds(20, 644, 990, 300);
        GuestLoginConfirmationArea.setBackground(AssetsColorArray[0]);
        //Adds the guest login confirmation area at the back most position
        GuestLoginPanel.add(GuestLoginConfirmationArea, new Integer(1));
        //Sets the properties of the guest login confirmation title
        final JTextField GuestLoginConfirmationTitle = new JTextField();
        GuestLoginConfirmationTitle.setBounds(20, 644, 990, 60);
        GuestLoginConfirmationTitle.setFont(AssetsFontArray[5]);
        GuestLoginConfirmationTitle.setEditable(false);
        GuestLoginConfirmationTitle.setText("Are you sure you want to login as guest?");
        GuestLoginConfirmationTitle.setHorizontalAlignment(JTextField.CENTER);
        GuestLoginConfirmationTitle.setOpaque(false);
        GuestLoginConfirmationTitle.setBorder(null);
        //Adds the guest login confirmation title at the front most position
        GuestLoginPanel.add(GuestLoginConfirmationTitle, new Integer(2));
        //Sets the properties of the guest login confirmation description
        final JTextField GuestLoginConfirmationDescription = new JTextField();
        GuestLoginConfirmationDescription.setBounds(20, 688, 990, 36);
        GuestLoginConfirmationDescription.setFont(AssetsFontArray[2]);
        GuestLoginConfirmationDescription.setEditable(false);
        GuestLoginConfirmationDescription.setText("If you continue to login as guest, no leaderboard or settings info will be saved");
        GuestLoginConfirmationDescription.setHorizontalAlignment(JTextField.CENTER);
        GuestLoginConfirmationDescription.setOpaque(false);
        GuestLoginConfirmationDescription.setBorder(null);
        //Adds the guest login confirmation description at the front most position
        GuestLoginPanel.add(GuestLoginConfirmationDescription, new Integer(2));
    }

    private void GuestLoginAndCancelButtonSetup() {
        /*
        This method sets the properties of the guest login and cancel buttons
        It then adds them to the guest login panel
         */

        //Sets properties of guest login button
        final JButton GuestLoginButton = new JButton("Login as guest");
        GuestLoginButton.setFont(AssetsFontArray[1]);
        GuestLoginButton.setBounds(520, 864, 470, 60);
        GuestLoginButton.setBackground(AssetsColorArray[0]);
        GuestLoginButton.setBorder(AssetsBorderArray[0]);
        GuestLoginButton.setFocusPainted(false);
        //Adds mouse press listener to guest login button
        GuestLoginButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                GuestLoginButton.removeActionListener(GuestLoginButton.getActionListeners()[0]);
                GuestLoginButton.setBackground(AssetsColorArray[1]);
                //Default settings
                UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), false, false, true, 3, 20, 50, false, false);
                //Adds "UpdateSettings" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to menu panel
                Shape_Shooter_TD.MainClass.StartMenuWindow(0);
            }
        });
        //Adds hover listener to guest login button that highlights it
        AddButtonHoverColorHighlight(GuestLoginButton, null);
        //Adds guest login button at front most position
        GuestLoginPanel.add(GuestLoginButton, new Integer(2));
        
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
                 //Partial transition to title screen panel
                Shape_Shooter_TD.MainClass.StartTitleWindow(2);
            }
        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        GuestLoginPanel.add(CancelButton, new Integer(2));
    }

    private void FinalSetup() {
        /*
        This method sets the properties of the guest login panel
        It then adds it to the main panel
         */

        //Sets guest login panel properties
        GuestLoginPanel.setBounds(0, 0, 1030, 964);
        //Adds the guest login panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(GuestLoginPanel, new Integer(1));
    }
}
