package com.sp.UI;

import com.sp.Dao.SmartSessionManager;
import com.sp.Model.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SystemConfigDialog implements SelectionListener, MouseListener {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigDialog.class);
    private SmartSessionManager smartSessionManager;
    final private Shell dialog;
    Table table;
    TableEditor editor;
    int EDITABLECOLUMN = 2;
    Button saveButton;


    List<SystemConfig> cachedSystemConfigList = new ArrayList<>();  // TO be updated to DB cache

    // Constructor:
    public SystemConfigDialog(Shell parent) {
        this.smartSessionManager = new SmartSessionManager();
        this.dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

        init();

        dialog.pack();
        dialog.open();
        dialog.setLocation(MainFrame.shell.getLocation());
    }

    private void init() {

        saveButton = new Button(dialog, SWT.LEFT);
        saveButton.setBounds(0, 5, 80, 27);
        saveButton.setText("Save");
        saveButton.setImage(MImage.saveImage);
        saveButton.addSelectionListener(this);

        table = new Table(dialog, SWT.BORDER);
        dialog.setText("System Configuration Dialog");
        dialog.setSize(350, 300);

        table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        table.setBounds(0, 30, 550, 300);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.addMouseListener(this);
        table.addSelectionListener(this);

        TableColumn tableIdColumn = new TableColumn(table, SWT.NONE);
        tableIdColumn.setWidth(50);
        tableIdColumn.setText("Id");

        TableColumn tableKeyColumn = new TableColumn(table, SWT.NONE);
        tableKeyColumn.setWidth(200);
        tableKeyColumn.setText("Key");

        TableColumn tableValueColumn = new TableColumn(table, SWT.NONE);
        tableValueColumn.setWidth(300);
        tableValueColumn.setText("Value");

        editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        // editing the second column
        final int EDITABLECOLUMN = 1;

        loadTableData();
    }

    private void loadTableData() {
        table.removeAll();
        List<SystemConfig> configList = smartSessionManager.getAllSystemConfigs();
        for (SystemConfig config : configList) {
            TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setData("config", config);
            tableItem.setText(new String[]{config.getId().toString(), config.getKey(), config.getValue()});
        }
    }

    private boolean isChangedConfig(SystemConfig old, String newVal) {
        if (!StringUtils.equals(old.getValue(), newVal)) {
            return true;
        }
        return false;
    }


    @Override
    public void mouseDoubleClick(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {

    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == table) {
            // Clean up any previous editor control
            Control oldEditor = editor.getEditor();

            if (oldEditor != null)
                oldEditor.dispose();

            // Identify the selected row
            TableItem item = (TableItem) e.item;
            if (item == null) {
                return;
            }


            // The control that will be the editor must be a child of the
            // Table
            Text newEditor = new Text(table, SWT.NONE);
            newEditor.setText(item.getText(EDITABLECOLUMN));
            newEditor.addModifyListener(me -> {
                String newText = ((Text) editor.getEditor()).getText();
//                smartSessionManager.update(new SystemConfig(oldConfig.getId(), oldConfig.getKey(), newText));
                editor.getItem().setText(EDITABLECOLUMN, newText);
            });
            newEditor.selectAll();
            newEditor.setFocus();
            editor.setEditor(newEditor, item, EDITABLECOLUMN);
        } else if (e.getSource() == saveButton) {
            String diffMsg = "";
            for (TableItem item : table.getItems()) {
                SystemConfig oldConfig = (SystemConfig) item.getData("config");
                if (isChangedConfig(oldConfig, item.getText(2))) {
                    cachedSystemConfigList.add(new SystemConfig(oldConfig.getId(), oldConfig.getKey(), item.getText(2)));
                    diffMsg += String.format("%s %s->%s", oldConfig.getKey(), oldConfig.getValue(), item.getText(2)) + "\n";
                    logger.debug("changed id:{},key:{},value:{}", item.getText(0), item.getText(1), item.getText(2));

                }

            }
            if (!cachedSystemConfigList.isEmpty()) {
                MessageBox messageBox = new MessageBox(this.dialog, SWT.ICON_QUESTION | SWT.WRAP | SWT.YES | SWT.NO);
                messageBox.setMessage("Are you sure you want to save the changes?\n" + diffMsg);
                messageBox.setText("Config Update");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    for (SystemConfig config : cachedSystemConfigList) {
                        smartSessionManager.update(config);
                    }
                }
            } else {
                MessageBox messagebox = new MessageBox(this.dialog, SWT.ICON_INFORMATION | SWT.OK);
                messagebox.setMessage("Nothing changed, please edit value in table content then press save");
                messagebox.open();
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {

    }
}
