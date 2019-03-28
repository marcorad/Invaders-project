package engine.component;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource.Status;

import engine.entity.Entity;
import game.GameData;

public class CollisionSoundComponent extends OnCollisionComponent {
	private Sound sound;

	public CollisionSoundComponent(Entity entity, Sound sound) {
		super(entity);
		this.sound = sound;
	}

	@Override
	public void notifyAction() {
		GameData.playSound(sound);
	}

}
