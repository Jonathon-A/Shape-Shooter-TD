package shape_shooter_td;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

final public class SettingsWindow extends Shape_Shooter_TDAssets implements WindowFormatInterface {

    @Override
    final public void StartSetup(final int TransitionType) {
        /*
        This method plays the transition between panels
        It also opens the settings panel and closes the previous panel while the transition is opaque
         */

        //Plays start of transition
        Shape_Shooter_TD.MainClass.TransitionStart(TransitionType);
        //Opens settings panels
        SettingsWindowSetup();
        //Closes previous panel before playing end of transition
        Shape_Shooter_TD.MainClass.TransitionEnd();
        //Settings panel is currently displayed
        Shape_Shooter_TD.MainClass.setCurrentDisplayedPanel(SettingsPanel);
    }

    private final JLayeredPane SettingsPanel = new JLayeredPane();

    private void SettingsWindowSetup() {
        /*
        This method opens the settings panel
        This includes adding all components that should be on the panel initially
        It is called whenever this panel is opened and the transitionn is opaque
         */

        //Gets original settings details
        GetSettingsInfo();
        //Setup system settings buttons
        SystemSettingsOptionsSetup();
        //Setup account settings buttons
        AccountSettingsOptionsSetup();
        //Setup sound settings buttons
        SoundSettingsSetup();
        //Setup apply and cancel buttons 
        ApplyAndCancelButtonSetup();
        //Setup settings panel 
        FinalSetup();
    }

    private void GetSettingsInfo() {
        /*
        This method retrieves the original settings before changes are made
        These are later compared to the changed settings
         */

        //Original fullscreen setting
        FullscreenSettingOriginal = Shape_Shooter_TD.MainClass.isFullScreen();
        FullscreenSetting = FullscreenSettingOriginal;
        //Original image background setting
        ImageBackgroundSettingOriginal = Shape_Shooter_TD.MainClass.isImageBackground();
        ImageBackgroundSetting = ImageBackgroundSettingOriginal;
        //Original antialiasing setting
        AntialiasingSettingOriginal = Shape_Shooter_TD.MainClass.isANTIALIASING();
        AntialiasingSetting = AntialiasingSettingOriginal;
        //Original render quality setting
        RenderQualitySettingOriginal = Shape_Shooter_TD.MainClass.getRenderQuality();
        RenderQualitySetting = RenderQualitySettingOriginal;
        //Original music volume setting
        MusicVolumeSettingOriginal = Shape_Shooter_TD.MainClass.getMusicVolume();
        MusicVolumeSetting = MusicVolumeSettingOriginal;
        //Original sound volume setting
        SoundVolumeSettingOriginal = Shape_Shooter_TD.MainClass.getSoundVolume();
        SoundVolumeSetting = SoundVolumeSettingOriginal;
        //Original sound muted setting
        AllSoundMutedSettingOriginal = Shape_Shooter_TD.MainClass.isAllSoundMuted();
        AllSoundMutedSetting = AllSoundMutedSettingOriginal;
        //Original sound muted in background setting
        MuteSoundInBackgroundSettingOriginal = Shape_Shooter_TD.MainClass.isMuteSoundInBackground();
        MuteSoundInBackgroundSetting = MuteSoundInBackgroundSettingOriginal;
    }

    private boolean FullscreenSetting;
    private boolean FullscreenSettingOriginal;

    private boolean ImageBackgroundSetting;
    private boolean ImageBackgroundSettingOriginal;

    private boolean AntialiasingSetting;
    private boolean AntialiasingSettingOriginal;

    private int RenderQualitySetting;
    private int RenderQualitySettingOriginal;

    private int MusicVolumeSetting;
    private int MusicVolumeSettingOriginal;

    private int SoundVolumeSetting;
    private int SoundVolumeSettingOriginal;

    private boolean AllSoundMutedSetting;
    private boolean AllSoundMutedSettingOriginal;

    private boolean MuteSoundInBackgroundSetting;
    private boolean MuteSoundInBackgroundSettingOriginal;

    private void SystemSettingsOptionsSetup() {
        /*
        This method sets the properties of the system settings buttons
        It then adds them to the settings panel at the front most position
         */

        //Setup system settings area properties
        final JPanel SystemSettingsArea = new JPanel();
        SystemSettingsArea.setBounds(20, 94, 490, 430);
        SystemSettingsArea.setBackground(AssetsColorArray[0]);
        SystemSettingsArea.setBorder(AssetsBorderArray[0]);
        //Adds system settings area at back most position
        SettingsPanel.add(SystemSettingsArea, new Integer(0));

        //Setup system settings area title properties
        final JTextField SystemSettingsAreaTitle = new JTextField();
        SystemSettingsAreaTitle.setBounds(20, 94, 490, 60);
        SystemSettingsAreaTitle.setFont(AssetsFontArray[5]);
        SystemSettingsAreaTitle.setEditable(false);
        SystemSettingsAreaTitle.setText("System options");
        SystemSettingsAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        SystemSettingsAreaTitle.setOpaque(false);
        SystemSettingsAreaTitle.setBorder(null);
        //Adds system settings area title at front most position
        SettingsPanel.add(SystemSettingsAreaTitle, new Integer(2));

        //Setup fullscreen setting button properties
        final JButton FullscreenButton = new JButton("Fullscreen:");
        FullscreenButton.setBounds(40, 154, 450, 80);
        FullscreenButton.setFont(AssetsFontArray[1]);
        FullscreenButton.setBackground(AssetsColorArray[0]);
        FullscreenButton.setBorder(AssetsBorderArray[0]);
        FullscreenButton.setFocusPainted(false);
        if (FullscreenSetting) {
            FullscreenButton.setText("Fullscreen: ON");
        } else {
            FullscreenButton.setText("Fullscreen: OFF");
        }
        //Adds mouse press listener to fullscreen setting button
        FullscreenButton.addActionListener((final ActionEvent ae) -> {
            //Toggles fullscreen setting
            if (!FullscreenSetting) {
                FullscreenButton.setText("Fullscreen: ON");
                FullscreenSetting = true;
            } else {

                FullscreenButton.setText("Fullscreen: OFF");
                FullscreenSetting = false;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds hover listener to fullscreen setting button that highlights it
        AddButtonHoverColorHighlight(FullscreenButton, null);
        //Adds fullscreen setting button at fronts most position
        SettingsPanel.add(FullscreenButton, new Integer(1));

        //Setup image background setting button properties
        final JButton ImageBackgroundButton = new JButton("Image background:");
        ImageBackgroundButton.setBounds(40, 244, 450, 80);
        ImageBackgroundButton.setFont(AssetsFontArray[1]);
        ImageBackgroundButton.setBackground(AssetsColorArray[0]);
        ImageBackgroundButton.setBorder(AssetsBorderArray[0]);
        ImageBackgroundButton.setFocusPainted(false);
        if (ImageBackgroundSetting) {
            ImageBackgroundButton.setText("Image background: ON");
        } else {
            ImageBackgroundButton.setText("Image background: OFF");
        }
        //Adds mouse press listener to image background setting button
        ImageBackgroundButton.addActionListener((final ActionEvent ae) -> {
            //Toggles image background setting
            if (!ImageBackgroundSetting) {
                ImageBackgroundButton.setText("Image background: ON");
                ImageBackgroundSetting = true;
            } else {

                ImageBackgroundButton.setText("Image background: OFF");
                ImageBackgroundSetting = false;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds hover listener to image background setting button that highlights it
        AddButtonHoverColorHighlight(ImageBackgroundButton, null);
        //Adds image background setting button at fronts most position
        SettingsPanel.add(ImageBackgroundButton, new Integer(1));

        //Setup antialiasing setting button properties
        final JButton AntialiasingButton = new JButton("Antialiasing:");
        AntialiasingButton.setBounds(40, 334, 450, 80);
        AntialiasingButton.setFont(AssetsFontArray[1]);
        AntialiasingButton.setBackground(AssetsColorArray[0]);
        AntialiasingButton.setBorder(AssetsBorderArray[0]);
        AntialiasingButton.setFocusPainted(false);
        if (AntialiasingSetting) {
            AntialiasingButton.setText("Antialiasing: ON");
        } else {
            AntialiasingButton.setText("Antialiasing: OFF");
        }
        //Adds mouse press listener to antialiasing setting button
        AntialiasingButton.addActionListener((final ActionEvent ae) -> {
            //Toggles antialiasing setting
            if (!AntialiasingSetting) {
                AntialiasingButton.setText("Antialiasing: ON");
                AntialiasingSetting = true;
            } else {
                AntialiasingButton.setText("Antialiasing: OFF");
                AntialiasingSetting = false;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds hover listener to antialiasing setting button that highlights it
        AddButtonHoverColorHighlight(AntialiasingButton, null);
        //Adds antialiasing setting button at fronts most position
        SettingsPanel.add(AntialiasingButton, new Integer(1));

        //Setup render quality setting combo box properties
        String DropDownOptions[] = {"Render quality: High", "Render quality: Medium", "Render quality: Low"};
        final JComboBox RenderQualityDropDown = new JComboBox(DropDownOptions);
        RenderQualityDropDown.setBounds(40, 424, 450, 80);
        ((JLabel) RenderQualityDropDown.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        RenderQualityDropDown.setFont(AssetsFontArray[1]);
        RenderQualityDropDown.setBackground(AssetsColorArray[0]);
        RenderQualityDropDown.setBorder(AssetsBorderArray[0]);
        RenderQualityDropDown.setFocusable(false);
        switch (RenderQualitySetting) {
            case (1):
                RenderQualityDropDown.setSelectedItem("Render quality: Low");
                break;
            case (2):
                RenderQualityDropDown.setSelectedItem("Render quality: Medium");
                break;
            case (3):
            default:
                RenderQualityDropDown.setSelectedItem("Render quality: High");
                break;
        }
        //Adds selection change listener to render quality setting combo box
        RenderQualityDropDown.addItemListener((final ItemEvent ie) -> {
            //Changes render quality setting
            final String SelectedDropDown = String.valueOf(RenderQualityDropDown.getSelectedItem());
            switch (SelectedDropDown) {
                case ("Render quality: Low"):
                    RenderQualitySetting = 1;
                    break;
                case ("Render quality: Medium"):
                    RenderQualitySetting = 2;
                    break;
                case ("Render quality: High"):
                default:
                    RenderQualitySetting = 3;
                    break;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds render quality setting combo box at fronts most position
        SettingsPanel.add(RenderQualityDropDown, new Integer(1));
    }

    private void AccountSettingsOptionsSetup() {
        /*
        This method sets the properties of the account settings buttons
        It then adds them to the settings panel at the front most position
         */

        //Setup account settings area properties
        final JPanel AccountSettingsArea = new JPanel();
        AccountSettingsArea.setBounds(520, 94, 490, 430);
        AccountSettingsArea.setBackground(AssetsColorArray[0]);
        AccountSettingsArea.setBorder(AssetsBorderArray[0]);
        //Adds account settings area at back most position
        SettingsPanel.add(AccountSettingsArea, new Integer(0));

        //Setup account settings area title properties
        final JTextField AccountSettingsAreaTitle = new JTextField();
        AccountSettingsAreaTitle.setBounds(520, 94, 490, 60);
        AccountSettingsAreaTitle.setFont(AssetsFontArray[5]);
        AccountSettingsAreaTitle.setEditable(false);
        AccountSettingsAreaTitle.setText("Account options");
        AccountSettingsAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        AccountSettingsAreaTitle.setOpaque(false);
        AccountSettingsAreaTitle.setBorder(null);
        //Adds account settings area title at front most position
        SettingsPanel.add(AccountSettingsAreaTitle, new Integer(2));

        //Setup change email button properties
        final JButton ChangeEmailButton = new JButton("Change email address");
        ChangeEmailButton.setBounds(540, 154, 450, 80);
        ChangeEmailButton.setFont(AssetsFontArray[1]);
        ChangeEmailButton.setBackground(AssetsColorArray[0]);
        ChangeEmailButton.setBorder(AssetsBorderArray[0]);
        ChangeEmailButton.setFocusPainted(false);
        //Adds mouse press listener to change email button
        ChangeEmailButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and not logged in as guest
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && !Shape_Shooter_TD.MainClass.getUsername().equals("")) {
                ChangeEmailButton.removeActionListener(ApplyButton.getActionListeners()[0]);
                ChangeEmailButton.setBackground(AssetsColorArray[1]);
                //If settings have been changed
                if (SettingsChanged) {
                    //Saves settings
                    SaveSettingChanges();
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to change email address panel
                Shape_Shooter_TD.MainClass.StartChangeEmailAddressWindow(0);
            }
        });
        //Adds hover listener to change email button that highlights it
        AddButtonHoverColorHighlight(ChangeEmailButton, null);
        //If logged in as guest
        if (Shape_Shooter_TD.MainClass.getUsername().equals("")) {
            //Highlights change email button red
            ChangeEmailButton.setBackground(AssetsColorArray[2]);
        }
        //Adds change email button at fronts most position
        SettingsPanel.add(ChangeEmailButton, new Integer(1));

        //Setup change username button properties
        final JButton ChangeUsernameButton = new JButton("Change username");
        ChangeUsernameButton.setBounds(540, 244, 450, 80);
        ChangeUsernameButton.setFont(AssetsFontArray[1]);
        ChangeUsernameButton.setBackground(AssetsColorArray[0]);
        ChangeUsernameButton.setBorder(AssetsBorderArray[0]);
        ChangeUsernameButton.setFocusPainted(false);
        //Adds mouse press listener to change username button
        ChangeUsernameButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and not logged in as guest
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && !Shape_Shooter_TD.MainClass.getUsername().equals("")) {
                ChangeUsernameButton.removeActionListener(ApplyButton.getActionListeners()[0]);
                ChangeUsernameButton.setBackground(AssetsColorArray[1]);
                //If settings have been changed
                if (SettingsChanged) {
                    //Saves settings
                    SaveSettingChanges();
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to change username panel
                Shape_Shooter_TD.MainClass.StartChangeUsernameWindow(0);
            }
        });
        //Adds hover listener to change username button that highlights it
        AddButtonHoverColorHighlight(ChangeUsernameButton, null);
        //If logged in as guest
        if (Shape_Shooter_TD.MainClass.getUsername().equals("")) {
            //Highlights change username button red
            ChangeUsernameButton.setBackground(AssetsColorArray[2]);
        }
        //Adds change username button at fronts most position
        SettingsPanel.add(ChangeUsernameButton, new Integer(1));

        //Setup change password button properties
        final JButton ChangePasswordButton = new JButton("Change password");
        ChangePasswordButton.setBounds(540, 334, 450, 80);
        ChangePasswordButton.setFont(AssetsFontArray[1]);
        ChangePasswordButton.setBackground(AssetsColorArray[0]);
        ChangePasswordButton.setBorder(AssetsBorderArray[0]);
        ChangePasswordButton.setFocusPainted(false);
        //Adds mouse press listener to change password button
        ChangePasswordButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and not logged in as guest
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && !Shape_Shooter_TD.MainClass.getUsername().equals("")) {
                ChangePasswordButton.removeActionListener(ApplyButton.getActionListeners()[0]);
                ChangePasswordButton.setBackground(AssetsColorArray[1]);
                //If settings have been changed
                if (SettingsChanged) {
                    //Saves settings
                    SaveSettingChanges();
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to change password panel
                Shape_Shooter_TD.MainClass.StartChangePasswordWindow(0);
            }
        });
        //Adds hover listener to change password button that highlights it
        AddButtonHoverColorHighlight(ChangePasswordButton, null);
        //If logged in as guest
        if (Shape_Shooter_TD.MainClass.getUsername().equals("")) {
            //Highlights change password button red
            ChangePasswordButton.setBackground(AssetsColorArray[2]);
        }
        //Adds change password button at fronts most position
        SettingsPanel.add(ChangePasswordButton, new Integer(1));

        //Setup delete account button properties
        final JButton DeleteAccountButton = new JButton("Delete account");
        DeleteAccountButton.setBounds(540, 424, 450, 80);
        DeleteAccountButton.setFont(AssetsFontArray[1]);
        DeleteAccountButton.setBackground(AssetsColorArray[0]);
        DeleteAccountButton.setBorder(AssetsBorderArray[0]);
        DeleteAccountButton.setFocusPainted(false);
        //Adds mouse press listener to delete account button
        DeleteAccountButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over and not logged in as guest
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete() && !Shape_Shooter_TD.MainClass.getUsername().equals("")) {
                DeleteAccountButton.removeActionListener(ApplyButton.getActionListeners()[0]);
                DeleteAccountButton.setBackground(AssetsColorArray[1]);
                //If settings have been changed
                if (SettingsChanged) {
                    //Saves settings
                    SaveSettingChanges();
                }
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to delete account panel
                Shape_Shooter_TD.MainClass.StartDeleteAccountWindow(0);
            }

        });
        //Adds hover listener to delete account button that highlights it
        AddButtonHoverColorHighlight(DeleteAccountButton, null);
        //If logged in as guest
        if (Shape_Shooter_TD.MainClass.getUsername().equals("")) {
            //Highlights delete account button red
            DeleteAccountButton.setBackground(AssetsColorArray[2]);
        }
        //Adds delete account button at fronts most position
        SettingsPanel.add(DeleteAccountButton, new Integer(1));
    }
    private final JSlider MusicVolumeSlider = new JSlider();
    private final JSlider SoundEffectsVolumeSlider = new JSlider();

    private void SoundSettingsSetup() {
        /*
        This method sets the properties of the sound settings options
        It then adds them to the settings panel at the front most position
         */

        //Setup sound settings area properties
        final JPanel SoundSettingsArea = new JPanel();
        SoundSettingsArea.setBounds(20, 534, 990, 340);
        SoundSettingsArea.setBackground(AssetsColorArray[0]);
        SoundSettingsArea.setBorder(AssetsBorderArray[0]);
        //Adds sound settings area at back most position
        SettingsPanel.add(SoundSettingsArea, new Integer(0));

        //Setup sound settings area title properties
        final JTextField SoundSettingsAreaTitle = new JTextField();
        SoundSettingsAreaTitle.setBounds(20, 534, 990, 60);
        SoundSettingsAreaTitle.setFont(AssetsFontArray[5]);
        SoundSettingsAreaTitle.setEditable(false);
        SoundSettingsAreaTitle.setText("Sound options");
        SoundSettingsAreaTitle.setHorizontalAlignment(JTextField.CENTER);
        SoundSettingsAreaTitle.setOpaque(false);
        SoundSettingsAreaTitle.setBorder(null);
        //Adds sound settings area title at front most position
        SettingsPanel.add(SoundSettingsAreaTitle, new Integer(2));

        //Setup music volume title properties
        final JTextArea MusicVolumeTitle = new JTextArea();
        MusicVolumeTitle.setBounds(40, 695, 120, 80);
        MusicVolumeTitle.setFont(AssetsFontArray[1]);
        MusicVolumeTitle.setEditable(false);
        MusicVolumeTitle.setText("Music" + "\n" + "volume:");
        MusicVolumeTitle.setOpaque(false);
        MusicVolumeTitle.setBorder(null);
        //Adds music volume title at front most position
        SettingsPanel.add(MusicVolumeTitle, new Integer(2));

        //Setup music volume slider properties
        MusicVolumeSlider.setBackground(AssetsColorArray[0]);
        MusicVolumeSlider.setFont(AssetsFontArray[1]);
        MusicVolumeSlider.setBounds(150, 684, 830, 80);
        MusicVolumeSlider.setMajorTickSpacing(10);
        MusicVolumeSlider.setMinorTickSpacing(1);
        MusicVolumeSlider.setPaintTrack(true);
        MusicVolumeSlider.setPaintTicks(true);
        MusicVolumeSlider.setPaintLabels(true);
        MusicVolumeSlider.setMaximum(100);
        MusicVolumeSlider.setMinimum(0);
        MusicVolumeSlider.setValue(MusicVolumeSettingOriginal);
        //Adds change listener to music volume slider
        MusicVolumeSlider.addChangeListener((final ChangeEvent e) -> {
            //If sound is not muted
            if (!AllSoundMutedSetting) {
                //Changes music volume setting
                MusicVolumeSetting = MusicVolumeSlider.getValue();
                //Changes system properties based on settings
                PreviewSettingChanges();
                //Checks if settings have been changed from the original settings
                ApplyButtonUpdate();
            }
        });
        //Adds music volume slider at front most position
        SettingsPanel.add(MusicVolumeSlider, new Integer(1));

        //Setup sound effects volume title properties
        final JTextArea SoundEffectsVolumeTitle = new JTextArea();
        SoundEffectsVolumeTitle.setBounds(40, 785, 120, 80);
        SoundEffectsVolumeTitle.setFont(AssetsFontArray[1]);
        SoundEffectsVolumeTitle.setEditable(false);
        SoundEffectsVolumeTitle.setText("Sound FX" + "\n" + "volume:");
        SoundEffectsVolumeTitle.setOpaque(false);
        SoundEffectsVolumeTitle.setBorder(null);
        //Adds sound effects volume title at front most position
        SettingsPanel.add(SoundEffectsVolumeTitle, new Integer(2));

        //Setup sound effects volume slider properties
        SoundEffectsVolumeSlider.setBackground(AssetsColorArray[0]);
        SoundEffectsVolumeSlider.setFont(AssetsFontArray[1]);
        SoundEffectsVolumeSlider.setBounds(150, 774, 830, 80);
        SoundEffectsVolumeSlider.setMajorTickSpacing(10);
        SoundEffectsVolumeSlider.setMinorTickSpacing(1);
        SoundEffectsVolumeSlider.setPaintTrack(true);
        SoundEffectsVolumeSlider.setPaintTicks(true);
        SoundEffectsVolumeSlider.setPaintLabels(true);
        SoundEffectsVolumeSlider.setMaximum(100);
        SoundEffectsVolumeSlider.setMinimum(0);
        SoundEffectsVolumeSlider.setValue(SoundVolumeSettingOriginal);
        //Adds change listener to sound effects volume slider
        SoundEffectsVolumeSlider.addChangeListener((final ChangeEvent e) -> {
            //If sound is not muted
            if (!AllSoundMutedSetting) {
                //Changes sound effects volume setting
                SoundVolumeSetting = SoundEffectsVolumeSlider.getValue();
                //Changes system properties based on settings
                PreviewSettingChanges();
                //Checks if settings have been changed from the original settings
                ApplyButtonUpdate();
            }
        });
        //Adds sound effects volume slider at front most position
        SettingsPanel.add(SoundEffectsVolumeSlider, new Integer(1));

        //Setup mute button properties
        final JButton MuteButton = new JButton("Sound muted always:");
        MuteButton.setBounds(40, 594, 470, 80);
        MuteButton.setFont(AssetsFontArray[1]);
        MuteButton.setBackground(AssetsColorArray[0]);
        MuteButton.setBorder(AssetsBorderArray[0]);
        MuteButton.setFocusPainted(false);
        if (AllSoundMutedSetting) {
            MuteButton.setText("Sound muted always: ON");
            MusicVolumeSlider.setValue(0);
            SoundEffectsVolumeSlider.setValue(0);
            MusicVolumeSlider.setEnabled(false);
            SoundEffectsVolumeSlider.setEnabled(false);
        } else {
            MuteButton.setText("Sound muted always: OFF");
        }
        //Adds mouse press listener to mute button
        MuteButton.addActionListener((final ActionEvent ae) -> {
            //Toggles sound muted setting
            if (!AllSoundMutedSetting) {
                MuteButton.setText("Sound muted always: ON");
                AllSoundMutedSetting = true;
                MusicVolumeSlider.setValue(0);
                SoundEffectsVolumeSlider.setValue(0);
                MusicVolumeSlider.setEnabled(false);
                SoundEffectsVolumeSlider.setEnabled(false);
            } else {
                MuteButton.setText("Sound muted always: OFF");
                AllSoundMutedSetting = false;

                MusicVolumeSlider.setValue(MusicVolumeSetting);
                SoundEffectsVolumeSlider.setValue(SoundVolumeSetting);

                MusicVolumeSlider.setEnabled(true);
                SoundEffectsVolumeSlider.setEnabled(true);
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds hover listener to mute button that highlights it
        AddButtonHoverColorHighlight(MuteButton, null);
        //Adds mute button at front most position
        SettingsPanel.add(MuteButton, new Integer(1));

        //Setup mute in background button properties
        final JButton MuteSoundInBackgroundButton = new JButton("Sound muted in background:");
        MuteSoundInBackgroundButton.setBounds(520, 594, 470, 80);
        MuteSoundInBackgroundButton.setFont(AssetsFontArray[1]);
        MuteSoundInBackgroundButton.setBackground(AssetsColorArray[0]);
        MuteSoundInBackgroundButton.setBorder(AssetsBorderArray[0]);
        MuteSoundInBackgroundButton.setFocusPainted(false);
        if (MuteSoundInBackgroundSetting) {
            MuteSoundInBackgroundButton.setText("Sound muted in background: ON");
        } else {
            MuteSoundInBackgroundButton.setText("Sound muted in background: OFF");
        }
        //Adds mouse press listener to mute in background button
        MuteSoundInBackgroundButton.addActionListener((final ActionEvent ae) -> {
            //Toggles sound muted in background setting
            if (!MuteSoundInBackgroundSetting) {
                MuteSoundInBackgroundButton.setText("Sound muted in background: ON");
                MuteSoundInBackgroundSetting = true;
            } else {
                MuteSoundInBackgroundButton.setText("Sound muted in background: OFF");
                MuteSoundInBackgroundSetting = false;
            }
            //Plays button press sound effect
            Thread T1 = new Thread(() -> {
                SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
            });
            T1.start();
            //Changes system properties based on settings
            PreviewSettingChanges();
            //Checks if settings have been changed from the original settings
            ApplyButtonUpdate();
        });
        //Adds hover listener to mute in background button that highlights it
        AddButtonHoverColorHighlight(MuteSoundInBackgroundButton, null);
        //Adds mute in background button at front most position
        SettingsPanel.add(MuteSoundInBackgroundButton, new Integer(1));
    }

    private final JButton ApplyButton = new JButton();

    private void ApplyAndCancelButtonSetup() {
        /*
        This method sets the properties of the apply and cancel buttons
        It then adds them to the settings panel
         */

        //Sets properties of the apply button
        ApplyButton.setBorder(AssetsBorderArray[0]);
        ApplyButton.setBounds(520, 884, 490, 60);
        ApplyButton.setBackground(AssetsColorArray[0]);
        ApplyButton.setFont(AssetsFontArray[1]);
        ApplyButton.setText("Apply");
        ApplyButton.setFocusPainted(false);
        //Adds mouse press listener to apply button
        ApplyButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                //If settings have been changed
                if (SettingsChanged) {
                    ApplyButton.removeActionListener(ApplyButton.getActionListeners()[0]);
                    ApplyButton.setBackground(AssetsColorArray[1]);
                    //Saves settings
                    SaveSettingChanges();
                    //Plays button press sound effect
                    Thread T1 = new Thread(() -> {
                        SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                    });
                    T1.start();
                    //Full transition to menu panel
                    Shape_Shooter_TD.MainClass.StartMenuWindow(0);
                }
            }
        });
        //Adds hover listener to apply button that highlights it
        AddButtonHoverColorHighlight(ApplyButton, null);
        ApplyButton.setBackground(AssetsColorArray[2]);
        //Adds apply button at front most position
        SettingsPanel.add(ApplyButton, new Integer(1));

        //Sets properties of cancel button
        final JButton CancelButton = new JButton("Cancel");
        CancelButton.setFont(AssetsFontArray[1]);
        CancelButton.setBounds(20, 884, 490, 60);
        CancelButton.setBackground(AssetsColorArray[0]);
        CancelButton.setBorder(AssetsBorderArray[0]);
        CancelButton.setFocusPainted(false);
        //Adds mouse press listener to cancel button
        CancelButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                CancelButton.removeActionListener(CancelButton.getActionListeners()[0]);
                CancelButton.setBackground(AssetsColorArray[1]);
                //User's original settings
                UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), FullscreenSettingOriginal, ImageBackgroundSettingOriginal, AntialiasingSettingOriginal, RenderQualitySettingOriginal, MusicVolumeSettingOriginal, SoundVolumeSettingOriginal, AllSoundMutedSettingOriginal, MuteSoundInBackgroundSettingOriginal);
                //Adds "UpdateSettings" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //Full transition to menu panel
                Shape_Shooter_TD.MainClass.StartMenuWindow(0);
            }
        });
        //Adds hover listener to cancel button that highlights it
        AddButtonHoverColorHighlight(CancelButton, null);
        //Adds cancel button at front most position
        SettingsPanel.add(CancelButton, new Integer(2));
    }

    private void PreviewSettingChanges() {
        /*
        This method changes the system properties to represent the current settings
         */

        //Current settings
        UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), FullscreenSetting, ImageBackgroundSetting, AntialiasingSetting, RenderQualitySetting, MusicVolumeSetting, SoundVolumeSetting, AllSoundMutedSetting, MuteSoundInBackgroundSetting);
        //Changes the system properties based on these settings
        Shape_Shooter_TD.MainClass.UpdateAllSettings(NewUserSettingsTableEntry);
    }

    private boolean SettingsChanged = false;

    private void ApplyButtonUpdate() {
        /*
        This method checks if settings have been changed from the original settings
        It prevent the user from applying setting if they have not changed
         */

        //If settings have been changed from the original settings
        if (FullscreenSetting != FullscreenSettingOriginal
                || ImageBackgroundSetting != ImageBackgroundSettingOriginal
                || AntialiasingSetting != AntialiasingSettingOriginal
                || (!AllSoundMutedSetting && (MusicVolumeSetting != MusicVolumeSettingOriginal))
                || (!AllSoundMutedSetting && (SoundVolumeSetting != SoundVolumeSettingOriginal))
                || AllSoundMutedSetting != AllSoundMutedSettingOriginal
                || MuteSoundInBackgroundSetting != MuteSoundInBackgroundSettingOriginal
                || RenderQualitySetting != RenderQualitySettingOriginal) {
            //Unlocks apply button
            ApplyButton.setBackground(AssetsColorArray[0]);
            SettingsChanged = true;
        } //If settings have not been changed from the original settings
        else {
            //Locks apply button
            ApplyButton.setBackground(AssetsColorArray[2]);
            SettingsChanged = false;
        }
    }

    private void SaveSettingChanges() {
        /*
        This method adds or updates a user settings entry in the user settings table if not logged in as guest
         */

        //User's changed settings
        final UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), FullscreenSetting, ImageBackgroundSetting, AntialiasingSetting, RenderQualitySetting, MusicVolumeSetting, SoundVolumeSetting, AllSoundMutedSetting, MuteSoundInBackgroundSetting);
        //If not logged in as guest
        if (!Shape_Shooter_TD.MainClass.getUsername().equals("")) {
            //Adds "AddOrUpdateSettingsEntry" operation to operation queue
            Shape_Shooter_TD.MainClass.AddOperationToQueue("AddOrUpdateSettingsEntry", null, NewUserSettingsTableEntry, null, "");
            //Adds "FindCurrentUserSettingsEntry" operation to operation queue
            Shape_Shooter_TD.MainClass.AddOperationToQueue("FindCurrentUserSettingsEntry", null, null, null, NewUserSettingsTableEntry.getUsername());
        } //If  logged in as guest
        else {
            //Adds "UpdateSettings" operation to operation queue
            Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
        }
    }

    private void FinalSetup() {
        /*
        This method sets the properties the settings panel
        It then adds it to the main panel
         */

        //Sets properties of settings panel
        SettingsPanel.setBounds(0, 0, 1030, 964);
        //Creates header and adds it to settings panel
        SettingsPanel.add(WindowHeaderSetup(), new Integer(2));
        //Adds the settings panel to the main panel, behind the transition panel
        Shape_Shooter_TD.MainClass.MainWindowPanel.add(SettingsPanel, new Integer(1));
    }

    @Override
    final protected JPanel WindowHeaderSetup() {
        /*
        This method overrides the standard WindowHeaderSetup method
        It creates the panel header for the settings panel
        It then returns this panel header
         */

        //Setup header area properties
        final JPanel HeaderArea = new JPanel();
        HeaderArea.setBackground(AssetsColorArray[5]);
        HeaderArea.setBounds(20, 20, 990, 64);
        HeaderArea.setLayout(null);
        //Setup menu button properties
        final JButton MenuButton = new JButton();
        MenuButton.setBounds(0, 0, 200, 64);
        MenuButton.setBorder(null);
        MenuButton.setIcon(AssetsButtonIconArray[17]);
        MenuButton.setFocusPainted(false);
        //Adds mouse press listener to menu button
        MenuButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                MenuButton.removeActionListener(MenuButton.getActionListeners()[0]);
                MenuButton.setIcon(AssetsButtonIconArray[19]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    SetVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
                //User's original settings
                UserSettingsTableEntry NewUserSettingsTableEntry = new UserSettingsTableEntry(Shape_Shooter_TD.MainClass.getUsername(), FullscreenSettingOriginal, ImageBackgroundSettingOriginal, AntialiasingSettingOriginal, RenderQualitySettingOriginal, MusicVolumeSettingOriginal, SoundVolumeSettingOriginal, AllSoundMutedSettingOriginal, MuteSoundInBackgroundSettingOriginal);
                //Adds "UpdateSettings" operation to operation queue
                Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, NewUserSettingsTableEntry, null, "");
                //Full transition to menu panel
                Shape_Shooter_TD.MainClass.StartMenuWindow(0);
            }
        });
        AddButtonHoverColorHighlight(MenuButton, AssetsButtonIconArray[18]);
        //Adds menu button to header
        HeaderArea.add(MenuButton);
        //Setup profile icon properties
        final JLabel ProfileIcon = new JLabel(AssetsImageIconArray[11]);
        ProfileIcon.setBounds(790, 0, 64, 64);
        //Adds profile icon to header
        HeaderArea.add(ProfileIcon);
        //Setup username area properties
        final JTextField UsernameArea = new JTextField(GetDisplayedUsername());
        UsernameArea.setFont(AssetsFontArray[1]);
        UsernameArea.setBounds(840, 0, 150, 64);
        UsernameArea.setBorder(BorderFactory.createCompoundBorder(AssetsBorderArray[0], AssetsBorderArray[1]));
        UsernameArea.setBackground(AssetsColorArray[0]);
        UsernameArea.setEditable(false);
        UsernameArea.setHorizontalAlignment(JTextField.LEFT);
        //Adds username area to header
        HeaderArea.add(UsernameArea);
        //Setup title properties
        final JLabel TitleArea = new JLabel();
        TitleArea.setBounds(210, 0, 570, 64);
        TitleArea.setIcon(AssetsImageIconArray[9]);
        //Adds title to header
        HeaderArea.add(TitleArea);
        //Return the settings panel header
        return HeaderArea;
    }
}
