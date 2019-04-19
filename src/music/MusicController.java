package music;

import org.jsfml.audio.Music;
import org.jsfml.system.Clock;

import game.Game;

public class MusicController extends Thread {

	private Music nextMusic, currentMusic;
	boolean started = false;
	private static final float TRANSITION_TIME = 1f;
	private boolean startTransition = false;
	private boolean inTransition = false;
	private Clock clk = new Clock();
	private boolean volumeChanged = false;
	private boolean previousMusicEnabled = true;

	@Override
	public void run() {		
		while(Game.window.isOpen()){

			if(Game.isMusicEnabled() != previousMusicEnabled){
				previousMusicEnabled = Game.isMusicEnabled();
				volumeChanged = true;
				//System.out.println("VOL CHANGED");
			}

			if(volumeChanged){		
				if(Game.isMusicEnabled()){
					if(inTransition){		
						currentMusic.stop();
						currentMusic = nextMusic;
						nextMusic = null;
						inTransition = false;						
					} 			
					currentMusic.setVolume(100f);
					//System.out.println("VOL 100");
				} else {					
					if(inTransition){
						currentMusic.stop();
						currentMusic = nextMusic;
						nextMusic = null;
						inTransition = false;						
					}	
					currentMusic.setVolume(0f);
					//System.out.println("VOL 0");
				}

				volumeChanged = false;
			}

			if(nextMusic != null){
				synchronized(nextMusic){					
					transitionToNext();						
				}
			}
		}
	}


	private void transitionToNext(){	
		if(startTransition) {
			clk.restart();
			nextMusic.setVolume(0f);
			nextMusic.play();
			startTransition = false;
			inTransition = true;
		} else {			
			if(clk.getElapsedTime().asSeconds() < TRANSITION_TIME){
				if(Game.isMusicEnabled()){

					//fade current music out
					currentMusic.setVolume((100f - 100f/TRANSITION_TIME * clk.getElapsedTime().asSeconds()));
					//fade next music in
					nextMusic.setVolume(100f/TRANSITION_TIME * clk.getElapsedTime().asSeconds());	
				} else {
					currentMusic.setVolume(0f);
					nextMusic.setVolume(0f);
				}
			} else {
				currentMusic.stop();
				currentMusic = nextMusic;
				nextMusic = null;
				inTransition = false;
			}
		}

	}

	/**Queue the next music to transitioned into
	 * @param m The next music
	 */
	public void setNextMusic(Music m){		
		if(nextMusic != null){//currently in transition
			synchronized(nextMusic){ //data race safety
				currentMusic = nextMusic;
				nextMusic = m;
				startTransition = true;
				inTransition = false;
			}
		} else{
			if(m != currentMusic)
				nextMusic = m;
			startTransition = true;
			inTransition = false;
		}
	}

	/**Set the music that this controller starts initially playing
	 * @param m The music
	 */
	public void setInititialMusic(Music m){
		if(!started){
			currentMusic = m;
			currentMusic.setVolume(100f);
			currentMusic.play();
			clk.restart();
			started = true;
		}
	}


}
