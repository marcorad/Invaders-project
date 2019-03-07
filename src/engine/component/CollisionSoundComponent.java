package engine.component;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource.Status;

import engine.entity.Entity;

public class CollisionSoundComponent extends OnCollisionComponent {
	private Sound sound;

	public CollisionSoundComponent(Entity entity, Sound sound) {
		super(entity);
		this.sound = sound;
	}

	@Override
	public void notifyAction() {
		if(sound.getStatus() != Status.PLAYING){
			System.out.println("PLAYING");
			sound.play();
		}
	}

}
