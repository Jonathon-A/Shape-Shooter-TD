package shape_shooter_td;

final public class UserSettingsTableEntry implements TableEntryInterface {

    private final String Username;
    private final boolean FullscreenSetting;
    private final boolean ImageBackgroundSetting;
    private final boolean AntialiasingSetting;
    private final int RenderQualitySetting;
    private final int MusicVolumeSetting;
    private final int SoundVolumeSetting;
    private final boolean AllSoundMutedSetting;
    private final boolean MuteSoundInBackgroundSetting;

    public UserSettingsTableEntry(final String Username, final boolean FullscreenSetting, final boolean ImageBackgroundSetting, final boolean AntialiasingSetting, final int RenderQualitySetting, final int MusicVolumeSetting, final int SoundVolumeSetting, final boolean AllSoundMutedSetting, final boolean MuteSoundInBackgroundSetting) {
        //Creates new user settings table entry with specified parameters
        this.Username = Username;
        this.FullscreenSetting = FullscreenSetting;
        this.ImageBackgroundSetting = ImageBackgroundSetting;
        this.AntialiasingSetting = AntialiasingSetting;
        this.RenderQualitySetting = RenderQualitySetting;
        this.MusicVolumeSetting = MusicVolumeSetting;
        this.SoundVolumeSetting = SoundVolumeSetting;
        this.AllSoundMutedSetting = AllSoundMutedSetting;
        this.MuteSoundInBackgroundSetting = MuteSoundInBackgroundSetting;
    }

    @Override
    final public String getUsername() {
        //Returns the username of this user settings table entry
        return Username;
    }

    final public boolean isFullscreenSetting() {
        //Returns the fullscreen setting of this user settings table entry
        return FullscreenSetting;
    }

    final public boolean isImageBackgroundSetting() {
        //Returns the image background setting of this user settings table entry
        return ImageBackgroundSetting;
    }

    final public boolean isAntialiasingSetting() {
        //Returns the antialiasing setting of this user settings table entry
        return AntialiasingSetting;
    }

    final public int getRenderQualitySetting() {
        //Returns the render quality setting of this user settings table entry
        return RenderQualitySetting;
    }

    final public int getMusicVolumeSetting() {
        //Returns the music volume setting of this user settings table entry
        return MusicVolumeSetting;
    }

    final public int getSoundVolumeSetting() {
        //Returns the sound effect volume setting of this user settings table entry
        return SoundVolumeSetting;
    }

    final public boolean isAllSoundMutedSetting() {
        //Returns the sound muted setting of this user settings table entry
        return AllSoundMutedSetting;
    }

    final public boolean isMuteSoundInBackgroundSetting() {
        //Returns the sound muted in background setting of this user settings table entry
        return MuteSoundInBackgroundSetting;
    }
}
