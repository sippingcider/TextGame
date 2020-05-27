package textGame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

/*
 * the mouse motion listener used in this game to handle user inputs
 * used by MainGame class
 */

public class Mouse implements MouseListener, MouseMotionListener
{
    boolean pressed,released,dragged;
    int x;
    int y;
    
    public Mouse() {
        pressed = false;
        released = false;
        dragged = false;
    }
    
    @Override
    public void mouseClicked(final MouseEvent arg0) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent arg0) {
    }
    
    @Override
    public void mouseExited(final MouseEvent arg0) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
    	pressed = true;
    	x = e.getX();
    	y = e.getY();
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
    	released = true;
    	x = e.getX();
    	y = e.getY();
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
    	dragged = true;
    	x = e.getX();
    	y = e.getY();
    }
    
    @Override
    public void mouseMoved(final MouseEvent arg0) {
    }
}
