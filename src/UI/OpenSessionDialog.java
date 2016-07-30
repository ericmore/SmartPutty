package UI;

import Control.InvokeProgram;
import Dao.DBManager;
import Model.ConfigSession;
import java.util.ArrayList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class OpenSessionDialog  implements SelectionListener, MouseListener{
	private MainFrame mainFrame = null;
	private Shell dialog = null;
	protected Object result;
	private Table table;
	private DBManager dbm;
	private Button addButton,editButton,deleteButton,connectButton,puttyWindow;
	
	public OpenSessionDialog(MainFrame mainFrame, Shell parent){
		dialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM|SWT.APPLICATION_MODAL);
		this.mainFrame = mainFrame;
		dbm = DBManager.getDBManagerInstance();
		dialog.setImage(MImage.openImage);
		dialog.setText("Open Session Dialog");
		dialog.setSize(300,300);
		
		//锟斤拷始锟斤拷table
		table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		table.setBounds(0, 0, 348, 257);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addMouseListener(this);
		TableColumn tableHostColumn = new TableColumn(table, SWT.NONE);
		tableHostColumn.setWidth(166);
		tableHostColumn.setText("Host");
		
		TableColumn tableUserColumn = new TableColumn(table, SWT.NONE);
		tableUserColumn.setWidth(74);
		tableUserColumn.setText("User");
		
		TableColumn tableTimeColumn = new TableColumn(table, SWT.NONE);
		tableTimeColumn.setWidth(102);
		tableTimeColumn.setText("Protocol");
		//锟斤拷锟絪essions锟斤拷锟�
		loadTable();
		
		//锟揭硷拷锟捷菜碉拷
		Menu popupmenu = new Menu(dialog, SWT.POP_UP);
		MenuItem addPopItem = new MenuItem(popupmenu, SWT.PUSH);
		addPopItem.setText("add session");
		addPopItem.setImage(MImage.addImage);
		addPopItem.addSelectionListener(this);
		MenuItem editPopItem = new MenuItem(popupmenu, SWT.PUSH);
		editPopItem.setText("edit session");
		editPopItem.setImage(MImage.editImage);
		editPopItem.addSelectionListener(this);
		MenuItem deletePopItem = new MenuItem(popupmenu, SWT.PUSH);
		deletePopItem.setText("delete session");
		deletePopItem.setImage(MImage.deleteImage);
		deletePopItem.addSelectionListener(this);
		table.setMenu(popupmenu);
		
		//button
		addButton = new Button(dialog, SWT.LEFT);
		addButton.setBounds(354, 5, 80, 27);
		addButton.setText("Add   ");
		addButton.setImage(MImage.addImage);
		addButton.setToolTipText("Add a new connection");
		addButton.addSelectionListener(this);
		
		editButton = new Button(dialog, SWT.LEFT);
		editButton.setBounds(354, 38, 80, 27);
		editButton.setText("Edit ");
		editButton.setImage(MImage.editImage);
		editButton.setToolTipText("Edit selected connection");
		editButton.addSelectionListener(this);
		
		deleteButton = new Button(dialog, SWT.LEFT);
		deleteButton.setBounds(354, 70, 80, 27);
		deleteButton.setText("Delete");
		deleteButton.setImage(MImage.deleteImage);
		deleteButton.setToolTipText("Delete selected connection/s");
		deleteButton.addSelectionListener(this);
		
		puttyWindow = new Button(dialog, SWT.LEFT);
		puttyWindow.setBounds(354, 103, 80, 27);
		puttyWindow.setText("Putty");
		puttyWindow.setImage(MImage.puttyImage);
		puttyWindow.setToolTipText("Open selected connection in a single window");
		puttyWindow.addSelectionListener(this);
		
		connectButton = new Button(dialog, SWT.NONE);
		connectButton.setBounds(354, 235, 80, 27);
		connectButton.setText("Connect");
		connectButton.setImage(MImage.connectImage);
		connectButton.setToolTipText("Open selected connection/s in a tab");
		connectButton.addSelectionListener(this);
		
		dialog.pack();
		dialog.open();
	}

	//锟斤拷锟窖★拷锟斤拷锟斤拷蚍祷氐锟揭伙拷锟�
	public ConfigSession getCurrentSelectSession(){
		if(table.getSelection().length > 0){
			return (ConfigSession)(table.getSelection()[0].getData("session"));
		}else{
			return null;
		}
	}

	public void loadTable(){
		table.removeAll();
		ArrayList<ConfigSession> sessions = (ArrayList<ConfigSession>) dbm.getAllCSessions();		
		for(ConfigSession session : sessions){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData("session",session);
			tableItem.setText(new String[] {session.getHost(), session.getUser(), session.getProtocol().getName()});
		}
	}

	/**
	 * Open all selected sessions in tabs.
	 */
	private void OpenSelectedSessions(){
		TableItem[] tableItems = table.getSelection();
		ArrayList<ConfigSession> sessions = new ArrayList<ConfigSession>();
		for (TableItem tableItem:tableItems){
			ConfigSession csession = dbm.queryCSessionBySession((ConfigSession) tableItem.getData("session"));
			sessions.add(csession);
			System.out.println("OpenSelectedSessions() " + csession); //DEBUG
		}
		dialog.dispose();

		for (ConfigSession session : sessions){
			this.mainFrame.addSession(null, session);
		}
	}

	/**
	 * Open a Putty session in a window outside program.
	 */
	private void OpenPutty(){
		TableItem[] tableItems = table.getSelection();
		if(tableItems!=null){
			ConfigSession csession = dbm.queryCSessionBySession((ConfigSession) tableItems[0].getData("session"));
			InvokeProgram.invokeSinglePutty(csession);
			dialog.dispose();
		}
	}

	
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	//	System.out.println(e.getSource().toString());
		if(e.getSource() == addButton){		
				new NewSessionDialog( null, this, "add");	
		} else if (e.getSource() == editButton){
			if (table.getSelection().length == 1) // Only one record must be editable.
				new NewSessionDialog( null, this, "edit");
			else
				MessageDialog.openInformation(dialog, "Warning", "Please select one record!");
		}else if(e.getSource() == deleteButton){
			if(this.table.getSelection().length == 0){
				MessageDialog.openInformation(dialog, "Warning", "Please select at least one record!");
				return;
			}
			TableItem[] tableItems = table.getSelection();
			for(TableItem item:tableItems){
				ConfigSession session = (ConfigSession) item.getData("session");
				dbm.deleteCSession(session);
			}
			loadTable();
		}else if(e.getSource() == puttyWindow){
			if(this.table.getSelection().length == 0){
				MessageDialog.openInformation(dialog, "Warning", "Please select one record!");
				return;
			}
			OpenPutty();
		}
		else if(e.getSource() == connectButton){
			if(this.table.getSelection().length == 0){
				MessageDialog.openInformation(dialog, "Warning", "Please select at least one record!");
				return;
			}
			OpenSelectedSessions();
		}
	}
	
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(table)){
			OpenSelectedSessions();
		}
		
	}
	
	public void mouseDown(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
	}
	
	public void mouseUp(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
	}
}
