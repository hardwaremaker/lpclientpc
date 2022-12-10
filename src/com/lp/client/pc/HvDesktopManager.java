package com.lp.client.pc;

import java.beans.PropertyVetoException;
import java.lang.ref.WeakReference;

import javax.swing.DefaultDesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.plaf.UIResource;

/**
 * Soll im Windows LaF verwendet werden, anstelle des WindowsDesktopManager
 * Die Methode {@link #activateFrame(JInternalFrame) activateFrame} verh&auml;lt sich
 * gleich wie im {@link com.sun.java.swing.plaf.windows.WindowsDesktopManager WindowsDesktopManager}
 * Die Referenz des aktuellen Frames wird jedoch in dieser Klasse beim Schließen
 * eines Frames zur&uuml;ckgesetzt.
 * 
 * @author andi
 *
 */
public class HvDesktopManager extends DefaultDesktopManager implements UIResource {

	private static final long serialVersionUID = -6989244368422435470L;

	private WeakReference<JInternalFrame> currentFrameRef;
	
    @Override
	public void closeFrame(JInternalFrame f) {
		super.closeFrame(f);
		currentFrameRef = null;
	}

    /**
     * Gleich zu {@link com.sun.java.swing.plaf.windows.WindowsDesktopManager#activateFrame(JInternalFrame) 
     * WindowsDesktopManager.activateFrame}
     */
	public void activateFrame(JInternalFrame f) {
         JInternalFrame currentFrame = currentFrameRef != null ?
             currentFrameRef.get() : null;
         try {
             super.activateFrame(f);
             if (currentFrame != null && f != currentFrame) {
                 // If the current frame is maximized, transfer that
                 // attribute to the frame being activated.
                 if (currentFrame.isMaximum() &&
                     (f.getClientProperty("JInternalFrame.frameType") !=
                     "optionDialog") ) {
                     //Special case.  If key binding was used to select next
                     //frame instead of minimizing the icon via the minimize
                     //icon.
                     if (!currentFrame.isIcon()) {
                         currentFrame.setMaximum(false);
                         if (f.isMaximizable()) {
                             if (!f.isMaximum()) {
                                 f.setMaximum(true);
                             } else if (f.isMaximum() && f.isIcon()) {
                                 f.setIcon(false);
                             } else {
                                 f.setMaximum(false);
                             }
                         }
                     }
                 }
                 if (currentFrame.isSelected()) {
                     currentFrame.setSelected(false);
                 }
             }
 
             if (!f.isSelected()) {
                 f.setSelected(true);
             }
         } catch (PropertyVetoException e) {}
         if (f != currentFrame) {
             currentFrameRef = new WeakReference<JInternalFrame>(f);
         }
     }
}
