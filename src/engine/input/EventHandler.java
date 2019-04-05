package engine.input;

import java.util.Iterator;
import java.util.Vector;


import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.*;

import engine.entity.Entity;

/**A class that handles key and mouse events at a lower level.
 * @author Marco
 *
 */
public class EventHandler {
	private final RenderWindow window;
	private Vector<KeyListener> kl;
	private Vector<MouseListener> ml;
	
	private Vector<KeyListener> kl_add = new Vector<>();
	private Vector<MouseListener> ml_add = new Vector<>();
	
	private static Vector2f CURRENT_MOUSE_WORLD_POS = Vector2f.ZERO;

	/**Construct an event handler listening to a specific window
	 * @param w The window.
	 */
	public EventHandler(RenderWindow w){
		window = w;
		kl = new Vector<>(); 
		ml = new Vector<>();
	}
	
	

	/**
	 * Poll the associated window's events and send appropriate messages
	 */
	public void handleEvents(){
		
		//add mouse listeners. The additional lists are required to avoid ComodificationExceptions
		for(MouseListener m : ml_add){
			ml.add(m);
		}
		ml_add.clear();
		
		for(KeyListener k : kl_add){
			kl.add(k);
		}
		kl_add.clear();
		
		
		for (Event e : window.pollEvents()){
			//when window must close
			if(e.type == Event.Type.CLOSED){
				window.close();
				return;
			}
			//remove if necessary
			Iterator<KeyListener> kit = kl.iterator();
			Iterator<MouseListener> mit = ml.iterator();
			
			while(kit.hasNext()){
				if(kit.next().isUseless())
					kit.remove();
			}
			
			while(mit.hasNext()){
				if(mit.next().isUseless())
					mit.remove();
			}
					
			//convert to different type of events
			MouseButtonEvent mb = e.asMouseButtonEvent(); //mouse buttons
			MouseWheelEvent mw = e.asMouseWheelEvent(); //mouse wheel
			MouseEvent me = (e.type == Event.Type.MOUSE_MOVED) ? e.asMouseEvent() : null;//mouse move must be checked since MouseEvent is generic for all mouse events
			KeyEvent ke =  e.asKeyEvent();

			//with likelihood of event as priority, do check to see if it is an applicable event. events are mutually exclusive
			if(me != null){
				CURRENT_MOUSE_WORLD_POS = Entity.graphics.toWorldSpace(me.position);
				for(MouseListener m : ml){
					m.onMouseMoved(me);
				}

			} else if(ke != null){			 
				
				 for(KeyListener k : kl){
					if (ke.type == Event.Type.KEY_PRESSED)
					k.onKeyPress(ke);
					if (ke.type == Event.Type.KEY_RELEASED)
					k.onKeyRelease(ke);
				}

			} else if(mb != null){
				for(MouseListener m : ml){
					if(mb.type == Event.Type.MOUSE_BUTTON_PRESSED)
						m.onMousePress(mb);
					if(mb.type == Event.Type.MOUSE_BUTTON_RELEASED)
						m.onMouseRelease(mb);
				}
			} else if(mw != null){
				for(MouseListener m : ml){
					m.onMouseWheelMoved(mw);
				}
			}
		}
	}

	
	/**Add a mouse listener that will react to the handler's input
	 * @param m The mouse listener
	 */
	public void attachMouseListener(MouseListener m){
		ml_add.addElement(m);
	}
	
	/**Add a key listener that will react to the handler's input
	 * @param k The key listener
	 */
	public void attachKeyListener(KeyListener k){
		kl_add.addElement(k);
	}
	
	public static Vector2f getCurrentMouseWorldPosition(){
		return CURRENT_MOUSE_WORLD_POS;
	}

}
