package UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * To define which locations has programs to use.
 * @author Carlos SS
 */
public class ProgramsLocationsDialog implements SelectionListener, MouseListener {
	private Shell dialog = null;
	final private Button puttyButton, plinkButton, keygeneratorButton, saveButton, cancelButton;
	final private Text puttyPathItem, plinkPathItem, keygeneratorPathItem;

	public ProgramsLocationsDialog(Shell parent){
		this.dialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		// Setup a layout:
		GridLayout layout = new GridLayout(3, false);

		dialog.setImage(MImage.openImage); // TODO: setup a good image!
		dialog.setText("Configure programs locations");
		dialog.setLayout(layout);

		// Putty/KiTTY location:
		GridData gd1 = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gd1.widthHint = 100;
		Label puttyLabel = new Label(dialog, SWT.RIGHT);
		puttyLabel.setText("Putty/KiTTY");
		puttyLabel.setLayoutData(gd1);

		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd2.widthHint = 300;
		puttyPathItem = new Text(dialog, SWT.BORDER);
		puttyPathItem.setText(MainFrame.configuration.getPuttyExecutable()); // Get current value.
		puttyPathItem.setLayoutData(gd2);

		GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd3.widthHint = 50;
		gd3.heightHint = 20;
		puttyButton = new Button(dialog, SWT.CENTER);
		puttyButton.setText("Browse");
		puttyButton.setToolTipText("Search for Putty or KiTTY executable");
		puttyButton.addSelectionListener(this);
		puttyButton.setLayoutData(gd3);

		// Plink/Klink location:
		GridData gd4 = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gd4.widthHint = 100;
		Label plinkLabel = new Label(dialog, SWT.RIGHT);
		plinkLabel.setText("Plink/Klink");
		plinkLabel.setLayoutData(gd4);

		GridData gd5 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd5.widthHint = 300;
		plinkPathItem = new Text(dialog, SWT.BORDER);
		plinkPathItem.setText(MainFrame.configuration.getPlinkExecutable()); // Get current value.
		plinkPathItem.setLayoutData(gd5);

		GridData gd6 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd6.widthHint = 50;
		gd6.heightHint = 20;
		plinkButton = new Button(dialog, SWT.CENTER);
		plinkButton.setText("Browse");
		plinkButton.setToolTipText("Search for Plink or Klink executable");
		plinkButton.addSelectionListener(this);
		plinkButton.setLayoutData(gd6);

		// Key generator location:
		GridData gd7 = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gd7.widthHint = 100;
		Label keygeneratorLabel = new Label(dialog, SWT.RIGHT);
		keygeneratorLabel.setText("Key generator");
		keygeneratorLabel.setLayoutData(gd7);

		GridData gd8 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd8.widthHint = 300;
		keygeneratorPathItem = new Text(dialog, SWT.BORDER);
		keygeneratorPathItem.setText(MainFrame.configuration.getKeyGeneratorExecutable()); // Get current value.
		keygeneratorPathItem.setLayoutData(gd8);

		GridData gd9 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd9.widthHint = 50;
		gd9.heightHint = 20;
		keygeneratorButton = new Button(dialog, SWT.CENTER);
		keygeneratorButton.setText("Browse");
		keygeneratorButton.setToolTipText("Search for a key generator executable");
		keygeneratorButton.addSelectionListener(this);
		keygeneratorButton.setLayoutData(gd9);

		// Blank space:
		GridData gd98 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd98.heightHint = 20;
		gd98.horizontalSpan = 3;

		Label empty1 = new Label(dialog, SWT.NONE);
		empty1.setLayoutData(gd98);

		// Main buttons:
		GridData gd99 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true);
		gd99.widthHint = 60;
		gd99.heightHint = 30;
		// gd5.horizontalSpan = 3;

		// Unused phantom item:
		Label empty2 = new Label(dialog, SWT.NONE);
		empty2.setLayoutData(gd99);

		saveButton = new Button(dialog, SWT.CENTER);
		saveButton.setText("Save");
		saveButton.setToolTipText("Save changes");
		saveButton.addSelectionListener(this);
		saveButton.setLayoutData(gd99);

		cancelButton = new Button(dialog, SWT.CENTER);
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Cancel changes");
		cancelButton.addSelectionListener(this);
		cancelButton.setLayoutData(gd99);

		dialog.pack();
		dialog.open();
	}

	/**
	 * Search for a executable path.
	 */
	private void searchPuttyDialog(String type){
		FileDialog puttyFileDialog = new FileDialog(dialog, SWT.OPEN);
		String[] filterExtensions = null;
		String[] filterNames = null;

		switch (type){
			case "putty":
				filterExtensions = new String[] {"putty.exe", "kitty*.exe"};
				filterNames = new String[] {"Putty (putty.exe)", "KiTTY (kitty*.exe)"};
				break;

			case "plink":
				filterExtensions = new String[] {"plink.exe", "klink.exe"};
				filterNames = new String[] {"Plink (plink.exe)", "Klink (klink.exe)"};
		}

		puttyFileDialog.setFilterExtensions(filterExtensions);
		puttyFileDialog.setFilterNames(filterNames);

		String path = puttyFileDialog.open();
		if (path != null){
			puttyPathItem.setText(path);
		}
	}

	/**
	 * Search for a key generator executable path.
	 */
	private void searchKeygenDialog(){
		FileDialog puttyFileDialog = new FileDialog(dialog, SWT.OPEN);
		String[] filterExtensions = new String[] {"*.exe", "*"};
		String[] filterNames = new String[] {"Key generator (*.exe)", "All Files (*)"};

		puttyFileDialog.setFilterExtensions(filterExtensions);
		puttyFileDialog.setFilterNames(filterNames);

		String path = puttyFileDialog.open();
		if (path != null){
			keygeneratorPathItem.setText(path);
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e){
		if (e.getSource() == puttyButton){
			searchPuttyDialog("putty");
		} else if (e.getSource() == plinkButton){
			searchPuttyDialog("plink");
		} else if (e.getSource() == keygeneratorButton){
			searchKeygenDialog();
		} else if (e.getSource() == saveButton){
			// Save changes to configuration:
			MainFrame.configuration.setPuttyExecutable(puttyPathItem.getText());
			MainFrame.configuration.setPlinkExecutable(plinkPathItem.getText());
			MainFrame.configuration.saveConfiguration();

			dialog.dispose();
		} else if (e.getSource() == cancelButton){
			dialog.dispose();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e){
	}

	@Override
	public void mouseDoubleClick(MouseEvent e){
	}

	@Override
	public void mouseDown(MouseEvent e){
	}

	@Override
	public void mouseUp(MouseEvent e){
	}
}
