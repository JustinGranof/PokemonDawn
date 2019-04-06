import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public enum Sounds {

    // Create constants that will hold the path to their sound file.
    LEVEL_UP("levelupsound.wav"),
    HEAL("healsound.wav"),
    BATTLE("battlemusic.wav"),
    CATCH("catchsound.wav"),
    INTRO("intromusic.wav"),
    GAME_MUSIC("gamemusic.wav"),
    DIALOG("dialogsound.wav"),
    START("start.wav");

    // Create a variable to hold the clip that will play the sound effect/song.
    private Clip clip;

    /**
     * Constructor for each static sound.
     * @param path the path to the sound's music file.
     */
    Sounds(String path) {
        // Create a new file using the filename parameter.
        File sound = new File("music" + File.separator + path);
        // Make sure the file exists.
        if (!sound.exists()) {
            System.out.println("[ERROR] " + path + " not found!");
            return;
        }
        try {
            // Get the audio stream from the sound file.
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
            // Get the clip
            clip = AudioSystem.getClip();
            // Open the audio stream.
            clip.open(stream);
        } catch (Exception e) {
        }
    }

    /**
     * Method to play the sound
     * @param loop true if the sound should loop, false if it shouldn't.
     * @param overlay true if the sound should overlay with other sounds, false if it should cancel other sounds.
     */
    public void play(boolean loop, boolean overlay) {
        // Check if the sound should overlay with other sounds
        if(!overlay) {
            // If the sound should not overlay, stop all other sounds.
            stopAll();
        }

        // Check if the clip is currently running
        if (clip.isRunning()) {
            // If the clip is running stop the clip.
            clip.stop();
        }

        // Set the sound to start from the beginning of the sound clip
        clip.setFramePosition(0);

        // If the sound should loop...
        if (loop) {
            // Loop the clip continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            // Start the clip
            clip.start();
        }
    }

    /**
     * Method to get the clip of a certain sound
     * @return the clip variable of a sound
     */
    public Clip getClip(){
        return this.clip;
    }

    /**
     * Method to initialize all the sounds
     */
    static void init(){
        values();
    }

    /**
     * Method to stop all the sounds currently playing
     */
    static void stopAll(){
        for(Sounds sound : values()){
            sound.getClip().stop();
        }
    }

}
