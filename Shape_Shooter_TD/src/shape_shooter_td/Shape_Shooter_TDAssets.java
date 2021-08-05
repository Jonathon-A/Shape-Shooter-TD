package shape_shooter_td;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

public abstract class Shape_Shooter_TDAssets {
    
    //Director of project on user's computer
    final String UserDirectory = System.getProperty("user.dir") + "\\dist\\";
    //Array of all tile icons
    final protected Icon[] AssetsTileIconArray = {
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteEndTile.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteStartTile.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_L_R.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_U_D.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_L_D.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_L_U.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_R_D.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_U_R.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_R.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_L.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_U.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\whiteTile_D.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\YellowTileTurretBase.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\OrangeTileTurretBase.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RedTileTurretBase.png")};
    //Array of all button icons
    final protected Icon[] AssetsButtonIconArray = {
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret1Button.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret1ButtonSelected.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret2Button.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret2ButtonSelected.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret3Button.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret3ButtonSelected.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RecycleButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RecycleButtonSelected.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RedPlusButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastForwards.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastForwardsOrange.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastForwardsGreen.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastBackwards.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastBackwardsOrange.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastBackwardsGreen.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\PauseButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ResumeButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\MenuButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\MenuButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\MenuButtonPressed.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonLeft.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonLeftHighlighted.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonRight.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonRightHighlighted.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonLeftInvalid.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TutorialButtonRightInvalid.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ExitButton.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ExitButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ExitButtonPressed.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastForwardsHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\FastBackwardsHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\PauseButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ResumeButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret1ButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret2ButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\Turret3ButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RecycleButtonHover.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\RedPlusButtonHover.png")
    };
    //Array of all image icons
    final protected Icon[] AssetsImageIconArray = {
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\EnemyCoverStart.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\EnemyCoverEnd.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\MapInvalid.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionQuestionMark.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionPlayGame2.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionSettings.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionSignOut.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionLeaderboard.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\DescriptionTutorial.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\HeaderTitle1.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\TitleImage.png"),
        new ImageIcon(UserDirectory + "Assets\\ImageAssets\\ProfileIcon.png")};
    //Array of common colours
    final protected Color[] AssetsColorArray = {
        new Color(255, 255, 255), //Standard White
        new Color(107, 229, 103), //Standard Green
        new Color(229, 107, 103), //Standard Red
        new Color(0, 0, 0), //Standard Black
        new Color(238, 37, 44),//Dark Red
        new Color(224, 224, 224),//Standard Grey
        new Color(184, 207, 229),//Light green
        new Color(103, 103, 103),//Dark grey
        new Color(155, 155, 155)};//Light grey
    //Array of common borders
    final protected Border[] AssetsBorderArray = {
        BorderFactory.createLineBorder(Color.BLACK, 6, false), //Standard black border
        BorderFactory.createEmptyBorder(12, 12, 12, 12), //Standard padding border
        BorderFactory.createLineBorder(AssetsColorArray[1], 7, false),//Green border
        createEmptyBorder(),//Empty border
        BorderFactory.createEmptyBorder(3, 3, 3, 3), //Small padding border
        BorderFactory.createLineBorder(AssetsColorArray[4], 7, false)};  ////Tile red border
    //Array of common fonts
    final protected Font[] AssetsFontArray = {
        new Font("Verdana", Font.BOLD, 32), //Standard very large bold font
        new Font("Verdana", Font.BOLD, 20), //Standard bold font
        new Font("Verdana", Font.PLAIN, 17),//Standard font
        new Font("Verdana", Font.PLAIN, 10),//Standard small font
        new Font("Verdana", Font.PLAIN, 8),//Standard very small font
        new Font("Verdana", Font.BOLD, 24)//Standard large bold font
    };
    //Array of music track directories
    final protected String[] AssetsMusicDirectoryArray = {
        UserDirectory + "Assets\\SoundAssets\\A.wav",
        UserDirectory + "Assets\\SoundAssets\\B.wav",
        UserDirectory + "Assets\\SoundAssets\\C.wav",
        UserDirectory + "Assets\\SoundAssets\\D.wav",
        UserDirectory + "Assets\\SoundAssets\\E.wav",
        UserDirectory + "Assets\\SoundAssets\\F.wav"
    };
    //Array of sound effect directories
    final protected String[] AssetsSoundDirectoryArray = {
        UserDirectory + "Assets\\SoundAssets\\KeyPress.wav",
        UserDirectory + "Assets\\SoundAssets\\LaserFire.wav",
        UserDirectory + "Assets\\SoundAssets\\PlayerLaserFire.wav",
        UserDirectory + "Assets\\SoundAssets\\ButtonPress.wav"
    };
    //Directory of text file containing list of inappropriate words
    String InappropriateWordsFileDirectory = UserDirectory + "Assets\\TextAssets\\InappropriateWords.txt";
    //ANTIALIASING RenderingHints
    final protected RenderingHints ANTIALIASING = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    //SPEED_RENDER RenderingHints
    final protected RenderingHints SPEED_RENDER = new RenderingHints(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_SPEED);
    //DEFAULT_RENDER RenderingHints
    final protected RenderingHints DEFAULT_RENDER = new RenderingHints(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_DEFAULT);
    //QUALITY_RENDER RenderingHints
    final protected RenderingHints QUALITY_RENDER = new RenderingHints(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);

    final protected BufferedImage CreateBufferedImage(final String ImagePath) throws IOException {
        //Returns buffered image created from image specified from given file path
        return ImageIO.read(new File(UserDirectory + "Assets\\ImageAssets\\" + ImagePath));
    }

    final protected void AddButtonHoverColorHighlight(final JButton btn, final Icon HoverIcon) {
        /*
        This method highlights a button light blue when the mouse cursor hovers over it
        Buttons with icons are highlighted using a specified hover icon
         */

        //Finds original icon of the button
        final Icon StandardIcon = btn.getIcon();
        //Finds original background colour of the button
        final Color StandardColor = btn.getBackground();
        //Removes previous mouse cursor hover listeners
        if (btn.getChangeListeners().length == 0) {
            btn.removeChangeListener(btn.getChangeListeners()[0]);
        }
        //Adds mouse cursor hover listener
        btn.getModel().addChangeListener((final ChangeEvent e) -> {
            final ButtonModel model = (ButtonModel) e.getSource();
            //If the mouse cursor is hovering over the button
            if (model.isRollover()) {
                //If the button does not have an icon
                if (HoverIcon != null) {
                    //If the button is not already highlighted
                    if (btn.getIcon().equals(StandardIcon)) {
                        //Highlights the button with the specified hover icon
                        btn.setIcon(HoverIcon);
                    }
                } //If the button has an icon
                else {
                    //If the button is not already highlighted
                    if (btn.getBackground().equals(StandardColor)) {
                        //Highlights button background light blue
                        btn.setBackground(AssetsColorArray[6]);
                    }
                }
            } //If the mouse cursor is not hovering over the button
            else {
                //If the button does not have an icon
                if (HoverIcon != null) {
                    //If the button is already highlighted
                    if (btn.getIcon().equals(HoverIcon)) {
                        //Return button to previous icon
                        btn.setIcon(StandardIcon);
                    }

                }//If the button has an icon
                else {
                    //If the button is already highlighted
                    if (btn.getBackground().equals(AssetsColorArray[6])) {
                        //Return button background to previous colour
                        btn.setBackground(StandardColor);
                    }
                }
            }
        });
    }

    final protected void TitleSetup(final JLayeredPane Panel) {
        /*
        This method sets the properties of the title image
        It then adds the title image to the specified panel
         */

        //Setup properties of the title image
        final JLabel TitleImage = new JLabel();
        TitleImage.setBounds(20, 20, 990, 627);
        TitleImage.setIcon(AssetsImageIconArray[10]);
        //Adds the title image to the specified panel at back most position
        Panel.add(TitleImage, new Integer(0));

    }

    final protected String GetDisplayedUsername() {
        /*
        This method calculates how much of the username should be displayed
        Returns the amount of the username that should be displayed
         */

        String DisplayedUsername = Shape_Shooter_TD.MainClass.getUsername();
        //If logged in as guest
        if (DisplayedUsername.equals("")) {
            //Sets displayed username to "guest"
            DisplayedUsername = "Guest";
        } //If logged in to an account
        else {
            //Calculates pixel width of username
            AffineTransform affinetransform = new AffineTransform();
            int UsernamePixelWidth = (int) (new Font("Verdana", Font.BOLD, 20).getStringBounds(DisplayedUsername, new FontRenderContext(affinetransform, true, true)).getWidth());
            //If the pixel width of the username is equal to or larger than the size of the display area
            if (UsernamePixelWidth >= 116) {
                int MaxUsernameDisplayLength = DisplayedUsername.length();
                //Loops through all character in the username
                for (int UsernameCharacterIndex = 1; UsernameCharacterIndex < DisplayedUsername.length(); UsernameCharacterIndex++) {
                    //If the pixel width of the substring of the username between 0 and UsernameCharacterIndex is equal to or larger than the size of the display area 
                    if ((int) new Font("Verdana", Font.BOLD, 20).getStringBounds(DisplayedUsername.substring(0, UsernameCharacterIndex), new FontRenderContext(affinetransform, true, true)).getWidth() >= 95) {
                        //Finds the maximum length of the substring of the username between 0 and UsernameCharacterIndex that can be displayed
                        MaxUsernameDisplayLength = UsernameCharacterIndex - 1;
                        //Ends the loop
                        UsernameCharacterIndex = DisplayedUsername.length();
                    }
                    //Replaces the part of the username that cannot fit on the display with "..."
                    DisplayedUsername = DisplayedUsername.substring(0, MaxUsernameDisplayLength) + "...";
                }
            }
        }
        //Returns the displayable username
        return DisplayedUsername;

    }

    protected JPanel WindowHeaderSetup() {
        /*
        This method creates the standard panel header
        It then returns this panel header
         */

        //Setup header area properties
        final JPanel HeaderArea = new JPanel();
        HeaderArea.setBackground(AssetsColorArray[5]);
        HeaderArea.setBounds(20, 20, 990, 64);
        HeaderArea.setLayout(null);
        //Setup menu button properties
        final JButton MenuButton = new JButton(AssetsButtonIconArray[17]);
        MenuButton.setBounds(0, 0, 200, 64);
        MenuButton.setBorder(null);
        MenuButton.setFocusPainted(false);
        //Adds mouse press listener to menu button
        MenuButton.addActionListener((final ActionEvent ae) -> {
            //If transition between panels is over
            if (Shape_Shooter_TD.MainClass.IsTransitionComplete()) {
                MenuButton.removeActionListener(MenuButton.getActionListeners()[0]);
                MenuButton.setIcon(AssetsButtonIconArray[19]);
                //Plays button press sound effect
                Thread T1 = new Thread(() -> {
                    setVolume(GetNewClip(AssetsSoundDirectoryArray[3]), Shape_Shooter_TD.MainClass.getSoundVolume()).start();
                });
                T1.start();
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
        //Return the panel header
        return HeaderArea;
    }

    final protected Clip GetNewClip(final String Path) {
        /*
        This method returns a sound clip based on the specified file path
         */

        //Creates  sound clip based on the specified file path
        Clip NewClip = null;
        try {
            NewClip = AudioSystem.getClip();
            NewClip.open(AudioSystem.getAudioInputStream(new File(Path)));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Get new clip error " + ex);
        }
        //Returns the sound clip
        return NewClip;
    }

    final protected Clip setVolume(final Clip CurrentClip, final int VolumeSetting) {
        /*
        Changes the volume of the specified sound clip
        It then returns this modified sound clip
         */

        //Calculates volume level as a value between 0 and 1 inclusive
        float Volume = (float) VolumeSetting / 100;
        if (Volume < 0) {
            Volume = 0;
        }
        if (Volume > 1) {
            Volume = 1;
        }
        //If all sound is muted or sound is muted in the background while the GUI's window is unfocused
        if (!Shape_Shooter_TD.MainClass.isWindowFocused() && Shape_Shooter_TD.MainClass.isMuteSoundInBackground() || Shape_Shooter_TD.MainClass.isAllSoundMuted()) {
            //Sets volume to zero so the sound clip is inaudible
            Volume = 0;
        }
        //Sets the volume of the sound clip
        final FloatControl FC = (FloatControl) CurrentClip.getControl(FloatControl.Type.MASTER_GAIN);
        FC.setValue(20f * (float) Math.log10(Volume));
        //Returns the modified sound clip
        return CurrentClip;
    }
}
