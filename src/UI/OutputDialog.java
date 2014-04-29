package UI;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import Control.Configuration;
import Control.MyPrintStream;
import Control.Telnet;
import Model.Intranet;


public class OutputDialog{

	private Text text;
	private Shell outputDialog = null;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public OutputDialog(Shell shell, ArrayList<Intranet> intranets) {
		
		outputDialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM |SWT.APPLICATION_MODAL);
		outputDialog.setSize(500,500);
		outputDialog.setText("OutputDialog");	
		text = new Text(outputDialog, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		text.setBounds(0, 0, 500, 500);
		text.setBackground(new Color(MainFrame.display,0,0,0));
		text.setForeground(new Color(MainFrame.display,0,255,0));
		MyPrintStream guiPrintStream = new MyPrintStream(System.out, text);	
		System.setOut(guiPrintStream);
		System.setErr(guiPrintStream);
		outputDialog.pack();
		outputDialog.open();
		long timeout = Long.parseLong(Configuration.getInstance().getTimeout());
		for(Intranet intranet : intranets){
			String host = intranet.getDesthost();
			String username =  intranet.getIntranetID();
			String password = intranet.getIntranetPassword();
			Thread t = new Telnet (host, 23, username, password, timeout);
			t.start();
		}	
		outputDialog.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				 System.setOut(MainFrame.out);
			     System.setErr(MainFrame.err);
			   
			     outputDialog.dispose();
			}
		} );
		
	}

}
