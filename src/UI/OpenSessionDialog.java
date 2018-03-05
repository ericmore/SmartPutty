package UI;

import Control.InvokeProgram;
import Dao.DBManager;
import Model.ConfigSession;
import java.util.ArrayList;

import Model.SmartSession;
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
	// Helper to deal with positions until a new layout can be made:
	private static final int X_POS = 404;
	
	public OpenSessionDialog(MainFrame mainFrame, Shell parent){
		this.dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.mainFrame = mainFrame;
		this.dbm = DBManager.getDBManagerInstance();

		init();
	}

	/**
	 * Initialize window in a safer way.
	 * Usefull to avoid "Leaking This In Constructor" warnings.
	 */
	private void init(){
		dialog.setImage(MImage.openImage);
		dialog.setText("Open Session Dialog");
//		dialog.setSize(350,300);
		
		table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(0, 0, 396, 257);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addMouseListener(this);

		TableColumn tableHostColumn = new TableColumn(table, SWT.NONE);
		tableHostColumn.setWidth(166);
		tableHostColumn.setText("Host");

		TableColumn tablePortColumn = new TableColumn(table, SWT.NONE);
		tablePortColumn.setWidth(50);
		tablePortColumn.setText("Port");

		TableColumn tableUserColumn = new TableColumn(table, SWT.NONE);
		tableUserColumn.setWidth(74);
		tableUserColumn.setText("User");
		
		TableColumn tableProtocalColumn = new TableColumn(table, SWT.NONE);
		tableProtocalColumn.setWidth(102);
		tableProtocalColumn.setText("Protocol");

		TableColumn tableDescriptionColumn = new TableColumn(table, SWT.NONE);
		tableDescriptionColumn.setWidth(102);
		tableDescriptionColumn.setText("Description");

		loadTable();
		
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
		addButton.setBounds(X_POS, 5, 80, 27);
		addButton.setText("Add   ");
		addButton.setImage(MImage.addImage);
		addButton.setToolTipText("Add a new connection");
		addButton.addSelectionListener(this);
		
		editButton = new Button(dialog, SWT.LEFT);
		editButton.setBounds(X_POS, 38, 80, 27);
		editButton.setText("Edit ");
		editButton.setImage(MImage.editImage);
		editButton.setToolTipText("Edit selected connection");
		editButton.addSelectionListener(this);
		
		deleteButton = new Button(dialog, SWT.LEFT);
		deleteButton.setBounds(X_POS, 70, 80, 27);
		deleteButton.setText("Delete");
		deleteButton.setImage(MImage.deleteImage);
		deleteButton.setToolTipText("Delete selected connection/s");
		deleteButton.addSelectionListener(this);
		
//		puttyWindow = new Button(dialog, SWT.LEFT);
//		puttyWindow.setBounds(X_POS, 103, 80, 27);
//		puttyWindow.setText("Putty");
//		puttyWindow.setImage(MImage.puttyImage);
//		puttyWindow.setToolTipText("Open selected connection in a single window");
//		puttyWindow.addSelectionListener(this);
		
		connectButton = new Button(dialog, SWT.NONE);
		connectButton.setBounds(X_POS, 235, 80, 27);
		connectButton.setText("Connect");
		connectButton.setImage(MImage.connectImage);
		connectButton.setToolTipText("Open selected connection/s in a tab");
		connectButton.addSelectionListener(this);
		
		dialog.pack();
		dialog.open();
	}

	public SmartSession getCurrentSelectSession(){
		if(table.getSelection().length > 0){
			return (SmartSession)(table.getSelection()[0].getData("session"));
		}else{
			return null;
		}
	}

	public void loadTable(){
		table.removeAll();
		ArrayList<SmartSession> sessions = (ArrayList<SmartSession>) dbm.findAllSmartSession();
		for(SmartSession session : sessions){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData("session",session);
			tableItem.setText(new String[] {session.getHost(), session.getPort(), session.getUser(), session.getProtocol(), session.getDescription()});
		}
	}

	/**
	 * Open all selected sessions in tabs.
	 */
	private void OpenSelectedSessions(){
		TableItem[] tableItems = table.getSelection();
		ArrayList<SmartSession> sessions = new ArrayList<SmartSession>();
		for (TableItem tableItem:tableItems){
			SmartSession smartSession = (SmartSession) tableItem.getData("session");
			sessions.add(smartSession);
			// System.out.println("OpenSelectedSessions() " + csession); //DEBUG
		}
		dialog.dispose();

		for (SmartSession session : sessions){
			this.mainFrame.addSession(null, session);
		}
	}

	/**
	 * Open a Putty session in a window outside program.
	 */
	private void OpenPutty(){
		TableItem[] tableItems = table.getSelection();
		if(tableItems!=null){
			SmartSession csession = (SmartSession) tableItems[0].getData("session");
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
				SmartSession session = (SmartSession) item.getData("session");
				dbm.deleteSmartSession(session);
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
