import java.io.File;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.IOException;

public class SoundEffects {
	//objects
	private Clip clip;
	private Clip music;
	//objects
	
	//boolean
	private boolean songPlaying = false;
	//boolean
	
	public void playLevelSong(boolean playSong) {
	    if (playSong) {
	        String songPath = "C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\sound\\arcade_music.wav";
	        try {
	            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(songPath));
	            Clip newClip = AudioSystem.getClip();
	            newClip.open(audioInputStream);
	            newClip.start(); // Start playing the audio

	            // Add a listener to detect when the song has finished playing
	            newClip.addLineListener((LineListener) new LineListener() {
	                public void update(LineEvent event) {
	                	//if at the end of the song
	                    if (event.getType() == LineEvent.Type.STOP && songPlaying) {
	                        playLevelSong(true);
	                    }
	                }
	            });

	            // Stop the previous clip (if any)
	            if (this.music != null) {
	                this.music.stop();
	            }

	            this.music = newClip;  // Assign the new clip to the class-level variable
	        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	            e.printStackTrace();
	        }
	    } 
	        else {
	        // Check if music is not null before stopping
	        if (this.music != null) {
	            stopMusic();
	        }
	    }
	}


	
	public void stopMusic() {
		songPlaying = false;
		this.music.stop();
	}


	        public void ShootingSoundEffect() {
	    		String shooting = "C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\sound\\Shooting.wav";
	    		try {
	    			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(shooting));
	    			 clip = AudioSystem.getClip();
	                    clip.open(audioInputStream);
	                    clip.start(); // Start playing the audio
	    		}
	                catch(IOException e2) {
	                	 e2.printStackTrace();
	                } catch (UnsupportedAudioFileException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
	    		catch (LineUnavailableException e3) {
	    		    e3.printStackTrace();
	    		}
	    	
	    }
	        public void DeathSoundEffect() {
	    		String shooting = "C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\sound\\GameOver.wav";
	    		try {
	    			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(shooting));
	    			 clip = AudioSystem.getClip();
	                    clip.open(audioInputStream);
	                    clip.start(); // Start playing the audio
	    		}
	                catch(IOException e2) {
	                	 e2.printStackTrace();
	                } catch (UnsupportedAudioFileException e1) {
	    				e1.printStackTrace();
	    			}
	    		catch (LineUnavailableException e3) {
	    		    e3.printStackTrace();
	    		}
	    	
	    }
}
