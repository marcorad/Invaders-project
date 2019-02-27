package engine.component;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import org.jsfml.window.event.MouseWheelEvent;

import engine.entity.Entity;
import engine.input.EventHandler;
import engine.input.MouseListener;

/**
 * Specifies a component that will listen to mouse input.
 * @author Marco
 *
 */
public abstract class MouseMoveControlComponent extends Component implements MouseListener {
	
	/**
	 * @param entity The active entity
	 */
	protected MouseMoveControlComponent(Entity entity) {
		super(entity);
		Entity.eventhandler.attachMouseListener(this);
	}
	
		
	public abstract void onRightMousePress(Vector2f worldpos);
	public abstract void onLeftMousePress(Vector2f worldpos);
	public abstract void onRightMouseRelease(Vector2f worldpos);
	public abstract void onLeftMouseRelease(Vector2f worldpos);
	public abstract void onMouseMove(Vector2f worldpos);
	
	

	@Override
	public void onMousePress(MouseButtonEvent mbe) {
		if(mbe.button == Mouse.Button.LEFT){
			onLeftMousePress(Entity.graphics.toWorldSpace(mbe.position));			
		} else if(mbe.button == Mouse.Button.RIGHT){
			onRightMousePress(Entity.graphics.toWorldSpace(mbe.position));
		}		
	}


	@Override
	public void onMouseRelease(MouseButtonEvent mbe) {
		if(mbe.button == Mouse.Button.LEFT){
			onLeftMouseRelease(Entity.graphics.toWorldSpace(mbe.position));			
		} else if(mbe.button == Mouse.Button.RIGHT){
			onRightMouseRelease(Entity.graphics.toWorldSpace(mbe.position));
		}	
	}


	@Override
	public void onMouseMoved(MouseEvent me) {
		onMouseMove(Entity.graphics.toWorldSpace(me.position));
	}


	@Override
	public boolean isUseless() {
		return entity.shouldBeRemoved();
	} 
	
	public void onMouseWheelMoved(MouseWheelEvent mwe) {		
	}
}
