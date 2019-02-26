package engine.input;

import org.jsfml.window.event.*;

public interface KeyListener {
	public void onKeyPress(KeyEvent ke);
	public void onKeyRelease(KeyEvent ke);
	public boolean isUseless(); //whether the listener should be removed
}
