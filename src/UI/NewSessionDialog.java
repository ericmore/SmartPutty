package UI;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import Dao.DBManager;
import Model.ConfigSession;
import Model.ConstantValue;

public class NewSessionDialog implements SelectionListener, MouseListener {

  private OpenSessionDialog sessionDialog = null;
  private MainFrame mainFrame;
  private Shell dialog;
  private Combo comboHost, comboUser, comboProtocol;
  private Text textPassword, textkey;
  private Button buttonFile, buttonOk, buttonCancel;

  public NewSessionDialog(MainFrame mainFrame, OpenSessionDialog sessionDialog, String type) {
    this.mainFrame = mainFrame;
    this.sessionDialog = sessionDialog;
    dialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    dialog.setImage(MImage.newImage);
    dialog.setSize(400, 160);
    dialog.setText("New Session Dialog");
    MainFrame.dbm = DBManager.getDBManagerInstance();
    Rectangle rect = dialog.getBounds();
    int x = rect.width;
    int y = rect.height;
    ArrayList<ConfigSession> sessions = (ArrayList<ConfigSession>) MainFrame.dbm.getAllCSessions();

    Label lable = new Label(dialog, SWT.NONE);
    lable.setText("Host");
    lable.setBounds(0, 0, x / 3, y / 6);
    comboHost = new Combo(dialog, SWT.None);
    //hashset ≈≈≥˝÷ÿ∏¥host
    HashSet<String> hs = new HashSet<String>();
    for (ConfigSession item : sessions) {
      hs.add(item.getHost());
    }
    for (String item : hs) {
      comboHost.add(item);
    }
    comboHost.setBounds(x / 3, 0, 2 * x / 3, y / 6);
    comboHost.addSelectionListener(this);

    lable = new Label(dialog, SWT.NONE);
    lable.setText("User");
    lable.setBounds(0, y / 6, x / 3, y / 6);
    comboUser = new Combo(dialog, SWT.None);
    comboUser.setBounds(x / 3, y / 6, 2 * x / 3, y / 6);
    comboUser.addSelectionListener(this);

    lable = new Label(dialog, SWT.NONE);
    lable.setText("Protocol");
    lable.setBounds(0, 2 * y / 6, x / 3, y / 6);
    comboProtocol = new Combo(dialog, SWT.READ_ONLY);
    comboProtocol.setItems(new String[] { "ssh" });
    comboProtocol.setBounds(x / 3, 2 * y / 6, 2 * x / 3, y / 6);
    comboProtocol.setText(ConstantValue.defaultProtocol);
    comboProtocol.addSelectionListener(this);

    lable = new Label(dialog, SWT.NONE);
    lable.setText("SSH Key");
    lable.setBounds(0, 3 * y / 6, x / 3, y / 6);
    textkey = new Text(dialog, SWT.BORDER);
    textkey.setBounds(x / 3, 3 * y / 6, 3 * x / 6, y / 6);
    buttonFile = new Button(dialog, SWT.PUSH);
    buttonFile.setBounds(5 * x / 6, 3 * y / 6, x / 6, y / 6);
    buttonFile.setText("browse");
    buttonFile.addMouseListener(this);

    lable = new Label(dialog, SWT.NONE);
    lable.setText("Password");
    lable.setBounds(0, 4 * y / 6, x / 3, y / 6);
    textPassword = new Text(dialog, SWT.PASSWORD | SWT.BORDER);
    textPassword.setBounds(x / 3, 4 * y / 6, 2 * x / 3, y / 6);

    buttonOk = new Button(dialog, SWT.PUSH);
    buttonOk.setText("ok");
    buttonOk.setBounds(2 * x / 3, 6 * y / 6, 50, y / 6);
    buttonOk.addMouseListener(this);

    buttonCancel = new Button(dialog, SWT.PUSH);
    buttonCancel.setText("cancel");
    buttonCancel.setBounds(2 * x / 3 + 60, 6 * y / 6, 50, y / 6);
    buttonCancel.addMouseListener(this);

    if (type.equals("edit")) {
      ConfigSession session = sessionDialog.getCurrentSelectSession();
      if (session != null) {
        comboHost.setText(session.getHost());
        comboUser.setText(session.getUser());
        comboProtocol.setText(session.getProtocol());
        textkey.setText(session.getKey());
        textPassword.setText(session.getPassword());
      }
    }

    dialog.pack();
    dialog.open();

  }

  public void widgetDefaultSelected(SelectionEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void widgetSelected(SelectionEvent e) {

    if (e.getSource() == comboHost) {
      String host = comboHost.getText();
      if (!host.trim().equals("")) {
        comboUser.removeAll();
        ArrayList<ConfigSession> sessions = (ArrayList<ConfigSession>) MainFrame.dbm.queryCSessionByHost(host);
        for (ConfigSession item : sessions) {
          comboUser.add(item.getUser());
        }
        if (sessions.size() == 1) {
          comboUser.setText(sessions.get(0).getUser());
          comboProtocol.setText(sessions.get(0).getProtocol());
          textPassword.setText(sessions.get(0).getPassword());
        } else {
          comboUser.setText("");
          comboProtocol.setText(ConstantValue.defaultProtocol);
          textPassword.setText("");
        }

      }

    } else if (e.getSource() == comboUser) {
      String host = comboHost.getText();
      String user = comboUser.getText();
      if (!host.trim().equals("") && !user.trim().equals("")) {
        ArrayList<ConfigSession> sessions = (ArrayList<ConfigSession>) MainFrame.dbm.queryCSessionByHostUser(host, user);
        if (sessions.size() == 1) {
          comboProtocol.setText(sessions.get(0).getProtocol());
          textPassword.setText(sessions.get(0).getPassword());
        } else {
          comboProtocol.setText(ConstantValue.defaultProtocol);
          textPassword.setText("");
        }
      }
    } else if (e.getSource() == comboProtocol) {
      String host = comboHost.getText();
      String user = comboUser.getText();
      String protocol = comboProtocol.getText();
      ConfigSession session = MainFrame.dbm.queryCSessionByHostUserProtocol(host, user, protocol);
      if (session != null)
        textPassword.setText(session.getPassword());
      else
        textPassword.setText("");
    }

  }

  public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
    // TODO Auto-generated method stub
    if (e.getSource() == buttonOk) {
      String host = comboHost.getText();
      String user = comboUser.getText();
      String password = textPassword.getText();
      String protocol = comboProtocol.getText();
      String file = textkey.getText().trim();
      ConfigSession session = new ConfigSession(host, user, protocol,file, password);

      if (!host.trim().equals("") && !user.trim().equals("") && !protocol.equals("")) {
        dialog.dispose();
        MainFrame.dbm.insertCSession(session);
        if (sessionDialog != null)
          sessionDialog.loadTable();
        if (mainFrame != null)
          mainFrame.addSession(null, session);
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

}
