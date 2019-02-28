package engine.input;

import java.util.Iterator;
import java.util.Vector;


import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.*;

public class EventHandler {
	private final RenderWindow window;
	private Vector<KeyListener> kl;
	private Vector<MouseListener> ml;

	public EventHandler(RenderWindow w){
		window = w;
		kl = new Vector<>(); 
		ml = new Vector<>();
	}

	/**
	 * poll the associated window's events
	 */
	public void handleEvents(){
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

	
	/**
	 * @param m add a listener that will react to mouse input
	 */
	public void attachMouseListener(MouseListener m){
		ml.addElement(m);
	}
	
	/**
	 * @param k add a listener that will react to keyboard input
	 */
	public void attachKeyListener(KeyListener k){
		kl.addElement(k);
	}

}
