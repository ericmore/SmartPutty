package UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

public class MImage {
	public final static Image mainImage = new Image(MainFrame.display, "icon\\main.png");
	public final static Image newImage = new Image(MainFrame.display, "icon\\new.png");
	public final static Image openImage = new Image(MainFrame.display, "icon\\open.png");
	public final static Image enableProxyImage = new Image(MainFrame.display, "icon\\enableProxy.png");
	public final static Image disableProxyImage = new Image(MainFrame.display, "icon\\disableProxy.png");
	public final static Image RemoteDeskImage = new Image(MainFrame.display, "icon\\remoteDesk.png");
	public final static Image captureImage = new Image(MainFrame.display, "icon\\capture.png");
	public final static Image bsoImage = new Image(MainFrame.display, "icon\\bso.png");
	public final static Image bsoImageGo = new Image(MainFrame.display, "icon\\bso_go.png");
	public final static Image reloadImage = new Image(MainFrame.display, "icon\\reload_session.png");
	public final static Image helpImage = new Image(MainFrame.display, "icon\\help.png");
	public final static Image calculator = new Image(MainFrame.display, "icon\\calculator.png");
	public final static Image notepad = new Image(MainFrame.display, "icon\\notepad.png");
	public final static Image key = new Image(MainFrame.display, "icon\\key.png");
	public final static Image windows = new Image(MainFrame.display, "icon\\windows.png");
	public final static Image linux = new Image(MainFrame.display, "icon\\linux.png");
	public final static Image folder = new Image(MainFrame.display, "icon\\folder.png");
	
	public final static Image cloneImage = new Image(MainFrame.display, "icon\\clone.png");
	public final static Image transferImage = new Image(MainFrame.display, "icon\\transfer.png");
	public final static Image vncImage = new Image(MainFrame.display, "icon\\vnc.png");
	public final static Image addImage = new Image(MainFrame.display, "icon\\add.png");
	public final static Image editImage = new Image(MainFrame.display, "icon\\edit.png");
	public final static Image deleteImage = new Image(MainFrame.display, "icon\\delete.png");
	public final static Image connectImage = new Image(MainFrame.display, "icon\\connect_ssh.png");
	public final static Image puttyImage = new Image(MainFrame.display, "icon\\putty.png");
	
	public static Image getGreenImage(){
		Image image = new Image(MainFrame.display, 10, 10);
		GC gc = new GC(image);
		gc.setBackground(MainFrame.display.getSystemColor(SWT.COLOR_GREEN));
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		return image;
	}
	public static Image getYellowImage(){
		Image image = new Image(MainFrame.display, 10, 10);
		GC gc = new GC(image);
		gc.setBackground(MainFrame.display.getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		return image;
	}
	public static Image getRedImage(){
		Image image = new Image(MainFrame.display, 10, 10);
		GC gc = new GC(image);
		gc.setBackground(MainFrame.display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		return image;
	}
}
