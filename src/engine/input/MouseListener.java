package engine.input;

import org.jsfml.window.event.*;

public interface MouseListener {
	
	public void onMousePress(MouseButtonEvent mbe);
	public void onMouseRelease(MouseButtonEvent mbe);
	public void onMouseWheelMoved(MouseWheelEvent mwe);
	public void onMouseMoved(MouseEvent me);
	public boolean isUseless(); //whether the listener should be removed

}
