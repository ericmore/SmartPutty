package UI;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import Dao.DBManager;
import Model.Intranet;

public class BSODialog implements DisposeListener, SelectionListener, MouseListener {
  private Shell dialog = null;
  private Table table = null;
  private Shell dialogCreateIntranet = null;
  private DBManager dbm = null;
  private Combo comboIntranetID = null, comboDesthost = null;
  private Text textPassword = null;
  private Button buttonAdd, buttonEdit, buttonDelete, buttonGo, buttonSave;

  public BSODialog(Shell shell) {
    // this.mainFrame = mainFrame;
    dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    dbm = DBManager.getDBManagerInstance();
    dialog.setImage(MImage.bsoImage);
    dialog.setText("BSO Dialog");
    dialog.setSize(400, 400);

    table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
    table.setBounds(0, 0, 400, 300);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.addMouseListener(this);
    TableColumn tableHostColumn = new TableColumn(table, SWT.NONE);
    tableHostColumn.setWidth(200);
    tableHostColumn.setText("Intranet ID");

    TableColumn tableUserColumn = new TableColumn(table, SWT.NONE);
    tableUserColumn.setWidth(200);
    tableUserColumn.setText("BSO Host");
    loadTable();

    // button
    buttonAdd = new Button(dialog, SWT.PUSH);
    buttonAdd.setText("   add");
    buttonAdd.setBounds(0, 300, 80, 30);
    buttonAdd.setImage(MImage.addImage);
    buttonAdd.addSelectionListener(this);
    buttonEdit = new Button(dialog, SWT.PUSH);
    buttonEdit.setText("   edit");
    buttonEdit.setBounds(85, 300, 80, 30);
    buttonEdit.setImage(MImage.editImage);
    buttonEdit.addSelectionListener(this);
    buttonDelete = new Button(dialog, SWT.PUSH);
    buttonDelete.setText("delete");
    buttonDelete.setBounds(170, 300, 80, 30);
    buttonDelete.setImage(MImage.deleteImage);
    buttonDelete.addSelectionListener(this);
    buttonGo = new Button(dialog, SWT.PUSH);
    buttonGo.setText("Pass BSO");
    buttonGo.setImage(MImage.bsoImageGo);
    buttonGo.setBounds(320, 300, 80, 30);
    buttonGo.addSelectionListener(this);

    dialog.pack();
    dialog.open();
  }

  public void createFirewallDialog(Shell shell, Intranet intranet) {
    dialogCreateIntranet = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    dialogCreateIntranet.setSize(350, 100);
    dialogCreateIntranet.setImage(MImage.newImage);
    dialogCreateIntranet.setText("BSO Dialog");
    dialogCreateIntranet.addDisposeListener(this);
    Rectangle rect = dialogCreateIntranet.getBounds();
    int x = rect.width;
    int y = rect.height;
    String destHost = "", intranetId = "", intranetPassword = "";
    if (intranet != null) {
      destHost = intranet.getDesthost();
      intranetId = intranet.getIntranetID();
      intranetPassword = intranet.getIntranetPassword();
    }

    Label lable = new Label(dialogCreateIntranet, SWT.NONE);
    lable.setText("IntranetID");
    lable.setBounds(0, 0, x / 3, y / 4);
    comboIntranetID = new Combo(dialogCreateIntranet, SWT.None);
    comboIntranetID.setText(intranetId);
    comboIntranetID.setBounds(x / 3, 0, 2 * x / 3, y / 4);

    lable = new Label(dialogCreateIntranet, SWT.NONE);
    lable.setText("IntranetPassword");
    lable.setBounds(0, y / 4, x / 3, y / 4);
    textPassword = new Text(dialogCreateIntranet, SWT.PASSWORD | SWT.BORDER);
    textPassword.setText(intranetPassword);
    textPassword.setBounds(x / 3, y / 4, 2 * x / 3, y / 4);

    lable = new Label(dialogCreateIntranet, SWT.NONE);
    lable.setText("Dest Host");
    lable.setBounds(0, 2 * y / 4, x / 3, y / 4);
    comboDesthost = new Combo(dialogCreateIntranet, SWT.None);
    comboDesthost.setText(destHost);
    comboDesthost.setBounds(x / 3, 2 * y / 4, 2 * x / 3, y / 4);

    buttonSave = new Button(dialogCreateIntranet, SWT.PUSH);
    buttonSave.setText("save");
    buttonSave.setBounds(5 * x / 6, 3 * y / 4, 50, y / 4);
    buttonSave.addSelectionListener(this);

    dialogCreateIntranet.pack();
    dialogCreateIntranet.open();
  }

  private void loadTable() {
    table.removeAll();
    ArrayList<Intranet> intranetArrayList = dbm.getAllIntranets();
    for (Intranet item : intranetArrayList) {
      TableItem tableItem = new TableItem(table, SWT.NONE);
      tableItem.setData("IntranetID", item.getIntranetID());
      tableItem.setData("Desthost", item.getDesthost());
      tableItem.setData("Password", item.getIntranetPassword());
      tableItem.setText(new String[] { item.getIntranetID(), item.getDesthost() });
    }

  }

  private Intranet getSelection() {
    TableItem item = table.getSelection()[0];
    Intranet intranet = new Intranet(item.getData("IntranetID").toString(), item.getData("Password").toString(), item.getData("Desthost").toString());

    return intranet;
  }

  private void deleteSelections() {
    TableItem[] items = table.getSelection();
    for (TableItem item : items) {
      Intranet intranet = new Intranet(item.getData("IntranetID").toString(), item.getData("Password").toString(), item.getData("Desthost").toString());
      dbm.deleteIntranet(intranet);
    }
    loadTable();
  }

  private void go() {
    TableItem[] items = table.getSelection();

    ArrayList<Intranet> intranets = new ArrayList<Intranet>();
    for (TableItem item : items) {
      Intranet intranet = dbm.queryIntranet(new Intranet(item.getData("IntranetID").toString(), item.getData("Password").toString(), item.getData("Desthost").toString()));
      intranets.add(intranet);
    }
    dialog.dispose();
    new OutputDialog(dialog, intranets);

  }

  public void widgetDefaultSelected(SelectionEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void widgetSelected(SelectionEvent e) {
    // TODO Auto-generated method stub
    if (e.getSource() == buttonAdd) {
      createFirewallDialog(dialog, null);
    } else if (e.getSource() == buttonEdit) {
      if (table.getSelection().length > 0)
        createFirewallDialog(dialog, getSelection());
    } else if (e.getSource() == buttonDelete) {
      deleteSelections();
    } else if (e.getSource() == buttonSave) {
      Intranet intranet = new Intranet(comboIntranetID.getText(), textPassword.getText(), comboDesthost.getText());
      intranet.printObj();
      dbm.deleteIntranet(intranet);
      dbm.insertIntranet(intranet);
      loadTable();
      dialogCreateIntranet.dispose();
    } else if (e.getSource() == buttonGo) {
      if (table.getSelectionCount() > 0)
        go();
    }
  }

  public void mouseDoubleClick(MouseEvent arg0) {
    // TODO Auto-generated method stub
    if (table.getSelectionCount() > 0)
      createFirewallDialog(dialog, getSelection());
  }

  public void mouseDown(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  public void mouseUp(MouseEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void widgetDisposed(DisposeEvent e) {
    // TODO Auto-generated method stub
    if (e.getSource().equals(dialog)) {
      dbm.closeDB();
    } else if (e.getSource().equals(dialogCreateIntranet)) {
      loadTable();
    }
  }

}
