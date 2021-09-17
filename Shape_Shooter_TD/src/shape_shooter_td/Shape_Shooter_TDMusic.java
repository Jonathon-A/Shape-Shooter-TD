package shape_shooter_td;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.Clip;

final public class Shape_Shooter_TDMusic extends Shape_Shooter_TDAssets {

    private Clip CurrentMusicTrack;
    private int MusicIndex;
    private int PreviousMusicVolume = -1;

    public Shape_Shooter_TDMusic() {
        final Random Rand = new Random();
        //Picks a random music track
        MusicIndex = Rand.nextInt(AssetsMusicDirectoryArray.length);
        CurrentMusicTrack = GetNewClip(AssetsMusicDirectoryArray[MusicIndex]);
        CurrentMusicTrack.setMicrosecondPosition(0);
        //Starts music loop
        MusicLoop();

    }

    private void MusicLoop() {
        /*
        This method plays a particular music track
        It pauses and resumes the music track when necessary
        When the music track is finished playing, the next track is selected and the method recurses
         */

        //Sets the volume of the music track
        SetVolume(CurrentMusicTrack, Shape_Shooter_TD.MainClass.getMusicVolume());
        //Starts the new music track
        CurrentMusicTrack.start();
        //Loops while the music track has not finished playing
        while (CurrentMusicTrack.getMicrosecondLength() > CurrentMusicTrack.getMicrosecondPosition()) {
            //20 millisecond delay every loop
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("Music loop error: " + ex);
            }
            //If all sound is muted or sound is muted in the background while the GUI's window is unfocused or music volume is set to zero
            if (!Shape_Shooter_TD.MainClass.isWindowFocused() && Shape_Shooter_TD.MainClass.isMuteSoundInBackground() || Shape_Shooter_TD.MainClass.isAllSoundMuted() || Shape_Shooter_TD.MainClass.getMusicVolume() == 0) {
                //If the music track is playing
                if (CurrentMusicTrack.isActive()) {
                    //Pauses the music track
                    CurrentMusicTrack.stop();
                }
            } //If the music track should not be paused
            else {
                //If the music volume has been changed
                if (PreviousMusicVolume != Shape_Shooter_TD.MainClass.getMusicVolume()) {
                    //Updates the volume of the music track
                    SetVolume(CurrentMusicTrack, Shape_Shooter_TD.MainClass.getMusicVolume());
                    PreviousMusicVolume = Shape_Shooter_TD.MainClass.getMusicVolume();
                }
                //If the music track is paused
                if (!CurrentMusicTrack.isActive()) {
                    //Resumes the music track
                    CurrentMusicTrack.start();
                }
            }
        }
        //Selects next music track
        MusicIndex++;
        if (MusicIndex >= AssetsMusicDirectoryArray.length) {
            MusicIndex = 0;
        }
        CurrentMusicTrack = GetNewClip(AssetsMusicDirectoryArray[MusicIndex]);
        //Recurses method
        MusicLoop();
    }
}
