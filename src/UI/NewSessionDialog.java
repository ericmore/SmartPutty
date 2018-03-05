package UI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Model.SmartSession;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import Dao.DBManager;
import Model.ConstantValue;

public class NewSessionDialog implements SelectionListener, MouseListener {

  private OpenSessionDialog sessionDialog = null;
  private MainFrame mainFrame;
  private Shell dialog;
  private Combo comboHost, comboUser, comboProtocol;
  private Text textPassword, textkey, textDescription;
  private Button buttonFile, buttonOk, buttonCancel;

  public NewSessionDialog(MainFrame mainFrame, OpenSessionDialog sessionDialog, String type) {
    this.mainFrame = mainFrame;
    this.sessionDialog = sessionDialog;
    dialog = new Shell(MainFrame.display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    dialog.setImage(MImage.newImage);
    dialog.setSize(500, 300);
    dialog.setText("New Session Dialog");
    GridLayout gridLayout = new GridLayout(3, true);
//    gridLayout.numColumns = 3;
    dialog.setLayout(gridLayout);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;


    MainFrame.dbm = DBManager.getDBManagerInstance();

    List<SmartSession> sessions = (ArrayList<SmartSession>) MainFrame.dbm.findAllSmartSession();

    Label label = new Label(dialog, SWT.NONE);
    label.setText("Host*");

    comboHost = new Combo(dialog, SWT.None);
    comboHost.setLayoutData(gridData);
    HashSet<String> hs =new HashSet<String>();
    sessions.forEach(item -> hs.add(item.getHost()));

    hs.forEach(e ->comboHost.add(e));




    label = new Label(dialog, SWT.NONE);
    label.setText("User*");
    comboUser = new Combo(dialog, SWT.None);
    comboUser.setLayoutData(gridData);

    label = new Label(dialog, SWT.NONE);
    label.setText("Protocol");
    comboProtocol = new Combo(dialog, SWT.READ_ONLY);
	// Get all protocols and add:
	for (String protocol : new ArrayList<String>(ConstantValue.protocalKV.keySet())){
		comboProtocol.add(protocol);
	}
	comboProtocol.select(3);
    comboProtocol.setText(ConstantValue.DEFAULT_PROTOCOL);
    comboProtocol.addSelectionListener(this);
    comboProtocol.setLayoutData(gridData);

    label = new Label(dialog, SWT.NONE);
    label.setText("SSH Key");
    textkey = new Text(dialog, SWT.BORDER);

    buttonFile = new Button(dialog, SWT.PUSH);
    buttonFile.setText("Browse...");
    buttonFile.addMouseListener(this);

    label = new Label(dialog, SWT.NONE);
    label.setText("Password");
    textPassword = new Text(dialog, SWT.PASSWORD | SWT.BORDER);
    textPassword.setLayoutData(gridData);

    label = new Label(dialog, SWT.NONE);
    label.setText("Description");
    textDescription = new Text(dialog, SWT.BORDER);
    textDescription.setLayoutData(gridData);


    buttonOk = new Button(dialog, SWT.PUSH);
    buttonOk.setText("ok");
    buttonOk.addMouseListener(this);

    buttonCancel = new Button(dialog, SWT.PUSH);
    buttonCancel.setText("cancel");
    buttonCancel.addMouseListener(this);

    if (type.equals("edit")) {
      SmartSession session = sessionDialog.getCurrentSelectSession();
      if (session != null) {
        comboHost.setText(session.getHost());
        comboUser.setText(session.getUser());
        comboProtocol.setText(session.getProtocol());
        textkey.setText(session.getKey());
        textPassword.setText(session.getPassword());
        textDescription.setText(session.getDescription());

      }
    }

    dialog.pack(true);
    dialog.open();

  }



  public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
    // TODO Auto-generated method stub
    if (e.getSource() == buttonOk) {
      String host = comboHost.getText();
      String user = comboUser.getText();
      String password = textPassword.getText();
	  String protocol = comboProtocol.getText();
      String file = textkey.getText().trim();
      String description = textDescription.getText().trim();

      SmartSession smartSession = new SmartSession(host, "22", user, password, protocol, file, description, "alias", ConstantValue.ConfigSessionTypeEnum.SMART_PUTTY_SESSION, "");
      if (!host.trim().equals("") && !user.trim().equals("") && !protocol.equals("")) {
        MainFrame.dbm.deleteSmartSession(smartSession);
        MainFrame.dbm.insertSmartSession(smartSession);
        if (sessionDialog != null)
          sessionDialog.loadTable();
        if (mainFrame != null)
          mainFrame.addSession(null, smartSession);

        dialog.dispose();
      } else {
        MessageDialog.openInformation(dialog, "Warning", "Must set Host, User and Protocol");
      }

    } else if (e.getSource() == buttonCancel) {
      dialog.dispose();
    } else if (e.getSource() == buttonFile) {
      FileDialog fileDlg = new FileDialog(dialog, SWT.OPEN);
      fileDlg.setText("Select SSH key");
      String filePath = fileDlg.open();
      if (null != filePath) {
        textkey.setText(filePath);
      }

    }

  }

  public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void widgetSelected(SelectionEvent e) {

  }

  public void widgetDefaultSelected(SelectionEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
    // TODO Auto-generated method stub

  }

}
