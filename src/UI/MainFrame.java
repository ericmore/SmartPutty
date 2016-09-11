package UI;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import Control.Configuration;
import Control.InvokeProgram;
import Dao.DBManager;
import Model.BorderData;
import Model.BorderLayout;
import Model.ConfigSession;
import Model.ConstantValue;
import Model.Program;
import Utils.RegistryUtils;

public class MainFrame implements SelectionListener, CTabFolder2Listener, MouseListener, ShellListener {
	final static Logger logger = Logger.getLogger(MainFrame.class);
	public static Display display = null;
	public static DBManager dbm;
	public static Shell shell;
	public static Configuration configuration;
	private MenuItem openItem, newItem, captureItem, remoteDesktopItem, exitItem, updateItem, webcomeMenuItem,
			reloadPopItem, clonePopItem, transferPopItem, scpMenuItem, ftpMenuItem, sftpMenuItem, vncPopItem,
			openPuttyItem, configProgramsLocationsItem, utilitiesBarMenuItem, connectionBarMenuItem;
	private Menu popupmenu;
	private ToolItem itemNew, itemOpen, itemRemoteDesk, itemCapture, itemCalculator, itemVNC, itemNotePad, itemKenGen,
			itemHelp;
	private CTabFolder folder;
	private CTabItem welcomeItem, dictItem;
	private ToolBar utilitiesToolbar;
	private Group connectGroup;

	public MainFrame() {
		display = new Display();
		shell = new Shell(display);

		// Load configuration:
		checkConf();

		shell.setLayout(new BorderLayout());
		shell.setImage(MImage.mainImage);
		shell.setText(ConstantValue.mainWindowTitle + " [" + ConstantValue.mainWindowVersion + "]");
		shell.setBounds(configuration.getWindowPositionSize());
		shell.addShellListener(this);

		// Get dbmanager instance:
		dbm = DBManager.getDBManagerInstance();

		// Main menu:
		createMainMenu();

		// Create upper connection toolbar:
		createConnectionBar();

		// Create upper utilities toolbar:
		createUtilitiesToolbar();

		// Create lower tabs zone:
		createTabs();
		createTabPopupMenu(); // Right button menu

		// Show/Hide toolbars based on configuration file values:
		setVisibleComponents();
		showWelcomeTab(ConstantValue.HOME_URL);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Main menu.
	 */
	private void createMainMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		// Menu: File
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText("File");
		Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);

		newItem = new MenuItem(filemenu, SWT.PUSH);
		newItem.setText("New\tCtrl+N");
		newItem.setImage(MImage.newImage);
		newItem.setAccelerator(SWT.CTRL + 'N');
		newItem.addSelectionListener(this);

		openItem = new MenuItem(filemenu, SWT.PUSH);
		openItem.setText("Open\tCtrl+O");
		openItem.setImage(MImage.openImage);
		openItem.setAccelerator(SWT.CTRL + 'O');
		openItem.addSelectionListener(this);

		captureItem = new MenuItem(filemenu, SWT.PUSH);
		captureItem.setText("Capture\tCtrl+C");
		captureItem.setImage(MImage.captureImage);
		captureItem.addSelectionListener(this);

		remoteDesktopItem = new MenuItem(filemenu, SWT.PUSH);
		remoteDesktopItem.setText("Remote Desktop\tCtrl+R");
		remoteDesktopItem.setImage(MImage.RemoteDeskImage);
		remoteDesktopItem.setAccelerator(SWT.CTRL + 'R');
		remoteDesktopItem.addSelectionListener(this);

		// Separator:
		new MenuItem(filemenu, SWT.SEPARATOR);

		exitItem = new MenuItem(filemenu, SWT.PUSH);
		exitItem.setText("Exit\tCtrl+X");
		exitItem.setImage(null);
		exitItem.setAccelerator(SWT.CTRL + 'X');
		exitItem.addSelectionListener(this);

		// Menu: View
		MenuItem view = new MenuItem(menu, SWT.CASCADE);
		view.setText("View");
		Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
		view.setMenu(viewMenu);

		utilitiesBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
		utilitiesBarMenuItem.setText("Utilities bar");
		utilitiesBarMenuItem.setSelection(true);
		utilitiesBarMenuItem.addSelectionListener(this);

		connectionBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
		connectionBarMenuItem.setText("Connection bar");
		connectionBarMenuItem.setSelection(true);
		connectionBarMenuItem.addSelectionListener(this);

		// Menu: Options
		MenuItem configurationMenuItem = new MenuItem(menu, SWT.CASCADE);
		configurationMenuItem.setText("Configuration");
		Menu optionsMenu = new Menu(shell, SWT.DROP_DOWN);
		configurationMenuItem.setMenu(optionsMenu);

		configProgramsLocationsItem = new MenuItem(optionsMenu, SWT.PUSH);
		configProgramsLocationsItem.setText("Programs locations");
		configProgramsLocationsItem.setImage(MImage.RemoteDeskImage);
		// configProgramsItem.setAccelerator(SWT.CTRL + 'R'); // TODO: setup a
		// key and enable!
		configProgramsLocationsItem.addSelectionListener(this);

		// Menu: Application
		MenuItem application = new MenuItem(menu, SWT.CASCADE);
		application.setText("Application");
		Menu applicationMenu = new Menu(shell, SWT.DROP_DOWN);
		application.setMenu(applicationMenu);
		List<HashMap<String, String>> listMenuItems = configuration.getBatchConfig();
		for (HashMap<String, String> menuHashMap : listMenuItems) {
			String type = menuHashMap.get("type");
			if (type == null || type.equals("seperator")) {
				new MenuItem(applicationMenu, SWT.SEPARATOR);
				continue;
			}
			String path = menuHashMap.get("path") == null ? "N/A" : menuHashMap.get("path");
			String argument = menuHashMap.get("argument") == null ? "N/A" : menuHashMap.get("argument");
			String description = menuHashMap.get("description") == null ? "N/A" : menuHashMap.get("description");
			MenuItem menuItem = new MenuItem(applicationMenu, SWT.PUSH);
			menuItem.setText(description);
			menuItem.setToolTipText(path + " " + argument);
			menuItem.setData("path", path);
			menuItem.setData("argument", argument);
			menuItem.setData("description", description);
			menuItem.setData("type", "dynamicApplication");
			menuItem.addSelectionListener(this);
		}

		// Menu: About
		MenuItem about = new MenuItem(menu, SWT.CASCADE);
		about.setText("About");
		Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
		about.setMenu(aboutMenu);

		webcomeMenuItem = new MenuItem(aboutMenu, SWT.PUSH);
		webcomeMenuItem.setText("Welcome Page");
		webcomeMenuItem.addSelectionListener(this);

		updateItem = new MenuItem(aboutMenu, SWT.PUSH);
		updateItem.setText("Update");
		updateItem.addSelectionListener(this);
	}

	/**
	 * Create bottom connection bar.
	 */
	private void createConnectionBar() {
		connectGroup = new Group(shell, SWT.NONE);

		RowLayout layout = new RowLayout();
		layout.marginTop = 3;
		layout.marginBottom = 3;
		layout.marginLeft = 1;
		layout.marginRight = 1;
		layout.spacing = 5;
		layout.wrap = false;
		layout.center = true;

		connectGroup.setLayout(layout);
		connectGroup.setLayoutData(new BorderData(SWT.TOP));
		connectGroup.setText("Quick Connect");

		// Username:
		new Label(connectGroup, SWT.RIGHT).setText("Username");
		final Text usernameItem = new Text(connectGroup, SWT.BORDER);
		usernameItem.setLayoutData(new RowData(100, 20));

		// Password
		new Label(connectGroup, SWT.RIGHT).setText("Password");
		final Text passwordItem = new Text(connectGroup, SWT.PASSWORD | SWT.BORDER);
		passwordItem.setLayoutData(new RowData(80, 20));

		// Port:
		new Label(connectGroup, SWT.RIGHT).setText("Port");
		final Text portItem = new Text(connectGroup, SWT.BORDER);
		portItem.setText("22");
		portItem.setLayoutData(new RowData(20, 20));

		// Session:
		new Label(connectGroup, SWT.RIGHT).setText("Session");
		final Combo sessionCombo = new Combo(connectGroup, SWT.READ_ONLY);
		sessionCombo.setLayoutData(new RowData());
		sessionCombo.setToolTipText("Session to use");
		sessionCombo.add(""); // Empty entry to use none.
		// Get all "Putty" sessions:
		List<String> sessions = RegistryUtils.getAllPuttySessions();
		for (String session : sessions) {
			sessionCombo.add(session);
		}

		// Connect button:
		Button connectButton = new Button(connectGroup, SWT.PUSH);
		connectButton.setText("Connect");
		connectButton.setImage(MImage.puttyImage);
		connectButton.setLayoutData(new RowData());
		connectButton.setToolTipText("Connect to host");
		connectButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				// String protocol = protocolCombo.getText().toLowerCase(); //
				// Putty wants lower case!
				String port = portItem.getText();
				String user = usernameItem.getText();
				String password = passwordItem.getText();
				String session = sessionCombo.getText();
				if (session.trim().isEmpty()) {
					MessageDialog.openInformation(MainFrame.shell, "Infomation", "please select a putty sesion first!");
					return;
				}
				ConfigSession configSession = new ConfigSession(user, password, port, session);
				addSession(null, configSession);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent se) {
			}
		});

		Group toolGroup = new Group(shell, SWT.BAR);
		RowLayout layout1 = new RowLayout();
		layout1.marginTop = 3;
		layout1.marginBottom = 3;
		layout1.marginLeft = 1;
		layout1.marginRight = 1;
		layout1.spacing = 5;
		layout1.wrap = false;
		layout1.center = true;
		toolGroup.setLayout(layout);
		toolGroup.setLayoutData(new BorderData(SWT.BOTTOM));
		// Path:
		new Label(toolGroup, SWT.RIGHT).setText("Path");
		final Text pathItem = new Text(toolGroup, SWT.BORDER);
		pathItem.setText("");
		pathItem.setLayoutData(new RowData(250, 20));

		Button win2UnixButton = new Button(toolGroup, SWT.PUSH);
		 win2UnixButton.setText("->Linux");
		win2UnixButton.setImage(MImage.linux);
		win2UnixButton.setToolTipText("Convert Windows Path to Linux");
		win2UnixButton.setLayoutData(new RowData());
		win2UnixButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				String path = pathItem.getText().trim();
				if (StringUtils.isBlank(path)) {
					MessageDialog.openInformation(shell, "Info", "Please input correct path!");
					return;
				}
				path = StringUtils.stripStart(path, "\\/");
				pathItem.setText("/" + FilenameUtils.separatorsToUnix(path));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		Button unix2WinButton = new Button(toolGroup, SWT.PUSH);
		 unix2WinButton.setText("->Windows");
		unix2WinButton.setToolTipText("Convert Linux path to Windows");
		unix2WinButton.setImage(MImage.windows);
		unix2WinButton.setLayoutData(new RowData());
		unix2WinButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				String path = pathItem.getText().trim();
				if (StringUtils.isBlank(path)) {
					MessageDialog.openInformation(shell, "Info", "Please input correct path!");
					return;
				}
				path = StringUtils.stripStart(path, "\\/");
				pathItem.setText("\\\\" + FilenameUtils.separatorsToWindows(path));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		Button openPathButton = new Button(toolGroup, SWT.PUSH);
		openPathButton.setText("Open");
		openPathButton.setImage(MImage.folder);
		openPathButton.setToolTipText("Open directory/file");
		openPathButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				String path = pathItem.getText().trim();
				if (StringUtils.isBlank(path)) {
					MessageDialog.openInformation(shell, "Info", "Please input correct path!");
					return;
				}
				path = StringUtils.stripStart(path, "\\/");
				path = "\\\\" + FilenameUtils.separatorsToWindows(path);
				pathItem.setText(path);
				if (!InvokeProgram.openFolder(path)) {
					MessageDialog.openError(shell, "Error", "Path not exist!");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		//Dictionary
		new Label(toolGroup, SWT.RIGHT).setText("Dictionary");
		final Text dictText = new Text(toolGroup, SWT.BORDER);
		dictText.setLayoutData(new RowData(100, 20));
		dictText.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					String keyword = dictText.getText().trim();
					OpenDictTab(keyword);
				}
			}
		});

		Button dictButton = new Button(toolGroup, SWT.PUSH);
		dictButton.setText("Search");
		dictButton.setToolTipText("Search Keywork in Dictionary");
		dictButton.setImage(MImage.dictImage);
		dictButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				String keyword = dictText.getText().trim();
				OpenDictTab(keyword);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		connectGroup.pack();
	}

	/**
	 * Create utilities toolbar.
	 */
	private void createUtilitiesToolbar() {
		utilitiesToolbar = new ToolBar(shell, SWT.VERTICAL);
		utilitiesToolbar.setLayoutData(new BorderData(SWT.LEFT));

		itemNew = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemNew.setText("New  ");
		itemNew.setToolTipText("create a new session");
		itemNew.setImage(MImage.newImage);
		itemNew.addSelectionListener(this);

		itemOpen = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemOpen.setText("Open  ");
		itemOpen.setToolTipText("open existing sessions");
		itemOpen.setImage(MImage.openImage);
		itemOpen.addSelectionListener(this);

		itemRemoteDesk = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemRemoteDesk.setText("RemoteDesk");
		itemRemoteDesk.setToolTipText("open system remote desk tool");
		itemRemoteDesk.setImage(MImage.RemoteDeskImage);
		itemRemoteDesk.addSelectionListener(this);

		itemCapture = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemCapture.setText("Capture");
		itemCapture.setToolTipText("Open FastStone Capture");
		itemCapture.setImage(MImage.captureImage);
		itemCapture.addSelectionListener(this);

		itemCalculator = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemCalculator.setText("Calculator");
		itemCalculator.setToolTipText("open microsoft calculator");
		itemCalculator.setImage(MImage.calculator);
		itemCalculator.addSelectionListener(this);

		itemVNC = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemVNC.setText("VNC");
		itemVNC.setToolTipText("open VNC");
		itemVNC.setImage(MImage.vncImage);
		itemVNC.addSelectionListener(this);

		itemNotePad = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemNotePad.setText("NotePad");
		itemNotePad.setToolTipText("open NotePad");
		itemNotePad.setImage(MImage.notepad);
		itemNotePad.addSelectionListener(this);

		itemKenGen = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemKenGen.setText("KenGen");
		itemKenGen.setToolTipText("Convert SSH Key");
		itemKenGen.setImage(MImage.key);
		itemKenGen.addSelectionListener(this);

		itemHelp = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemHelp.setText("Help");
		itemHelp.setToolTipText("help document");
		itemHelp.setImage(MImage.helpImage);
		itemHelp.addSelectionListener(this);

		utilitiesToolbar.pack();
	}

	/**
	 * Create tabs zone.
	 */
	private void createTabs() {
		folder = new CTabFolder(shell, SWT.BORDER);

		folder.setLayoutData(new BorderData());
		folder.setSimple(false);
		folder.setUnselectedCloseVisible(false);
		folder.addCTabFolder2Listener(this);
		folder.addMouseListener(this);
		folder.addSelectionListener(this);
	}

	/**
	 * Tab popup menu.
	 */
	private void createTabPopupMenu() {
		popupmenu = new Menu(shell, SWT.POP_UP);
		reloadPopItem = new MenuItem(popupmenu, SWT.PUSH);
		reloadPopItem.setText("reload session");
		reloadPopItem.setImage(MImage.reloadImage);
		reloadPopItem.addSelectionListener(this);

		// openPuttyItem = new MenuItem(popupmenu, SWT.PUSH);
		// openPuttyItem.setText("open in putty");
		// openPuttyItem.setImage(MImage.puttyImage);
		// // openPuttyItem.setToolTipText("Opens connection on a single
		// window");
		// openPuttyItem.addSelectionListener(this);

		clonePopItem = new MenuItem(popupmenu, SWT.PUSH);
		clonePopItem.setText("clone session");
		clonePopItem.setImage(MImage.cloneImage);
		clonePopItem.addSelectionListener(this);

		transferPopItem = new MenuItem(popupmenu, SWT.CASCADE);
		transferPopItem.setText("transfer file");
		transferPopItem.setImage(MImage.transferImage);

		Menu subMenu = new Menu(popupmenu);
		ftpMenuItem = new MenuItem(subMenu, SWT.PUSH);
		ftpMenuItem.setText("FTP");
		// ftpMenuItem.setToolTipText("Simple FTP");
		ftpMenuItem.addSelectionListener(this);

		scpMenuItem = new MenuItem(subMenu, SWT.PUSH);
		scpMenuItem.setText("SCP");
		// scpMenuItem.setToolTipText("FTP over SSH");
		scpMenuItem.addSelectionListener(this);

		sftpMenuItem = new MenuItem(subMenu, SWT.PUSH);
		sftpMenuItem.setText("SFTP");
		// sftpMenuItem.setToolTipText("Secure FTP");
		sftpMenuItem.addSelectionListener(this);

		transferPopItem.setMenu(subMenu);

		vncPopItem = new MenuItem(popupmenu, SWT.PUSH);
		vncPopItem.setText("VNC");
		vncPopItem.setImage(MImage.vncImage);
		vncPopItem.addSelectionListener(this);
	}

	private void checkConf() {
		InvokeProgram.killPuttyWarningsAndErrs();
		configuration = new Configuration();
	}

	private void showWelcomeTab(String url) {
		if (welcomeItem == null || welcomeItem.isDisposed()) {
			welcomeItem = new CTabItem(folder, SWT.CLOSE);
			Browser browser = new Browser(folder, SWT.NONE);
			browser.setUrl(url);
			welcomeItem.setControl(browser);
			folder.setSelection(welcomeItem);
			welcomeItem.setText("Welcome Page");
		}
	}

	private void OpenDictTab(String keyword) {
		if (dictItem == null || dictItem.isDisposed()) {
			dictItem = new CTabItem(folder, SWT.CLOSE);
			dictItem.setImage(MImage.dictImage);
			Browser browser = new Browser(folder, SWT.NONE);
			browser.setUrl(ConstantValue.DICT_URL_BASE + keyword);
			dictItem.setControl(browser);
			folder.setSelection(dictItem);
			dictItem.setText("Dictionary");
		} else {
			folder.setSelection(dictItem);
			((Browser) dictItem.getControl()).setUrl(ConstantValue.DICT_URL_BASE + keyword);
		}

	}

	/**
	 * Show/Hide toolbars based on configuration file values.
	 */
	private void setVisibleComponents() {
		Event event = new Event();

		utilitiesBarMenuItem.setSelection(configuration.getUtilitiesBarVisible());
		utilitiesBarMenuItem.notifyListeners(SWT.Selection, event);
		connectionBarMenuItem.setSelection(configuration.getConnectionBarVisible());
		connectionBarMenuItem.notifyListeners(SWT.Selection, event);
	}

	/**
	 * Open a new session in a new tab.
	 * 
	 * @param item
	 * @param session
	 */
	public void addSession(CTabItem item, ConfigSession session) {
		if (item == null) {
			item = new CTabItem(folder, SWT.CLOSE);
		}

		Composite composite = new Composite(folder, SWT.EMBEDDED);
		composite.setBackground(new Color(display, 0, 0, 0));
		item.setControl(composite);
		item.setData("TYPE", "session");
		folder.setSelection(item);
		item.setText("connecting");
		item.setImage(MImage.puttyImage);
		Thread t = new InvokeProgram(composite, item, session);
		t.start();
	}

	private void reloadSession() {

		CTabItem tabItem = folder.getSelection();
		if (tabItem.getData("hwnd") == null)
			return;
		int hwnd = Integer.parseInt(String.valueOf(tabItem.getData("hwnd")));
		InvokeProgram.killProcess(hwnd);
		addSession(tabItem, (ConfigSession) tabItem.getData("session"));
	}

	private void cloneSession() {
		CTabItem tabItem = folder.getSelection();
		if (tabItem.getData("session") == null)
			return;
		ConfigSession session = (ConfigSession) tabItem.getData("session");
		addSession(null, session);
	}

	private void openWinscp(String protocol) {
		if (folder.getSelection().getData("session") == null)
			return;
		ConfigSession session = (ConfigSession) folder.getSelection().getData("session");
		String arg = protocol + "://" + session.getUser() + ":" + session.getPassword() + "@" + session.getHost() + ":"
				+ session.getPort();

		InvokeProgram.runProgram(Program.APP_WINSCP, arg);
	}

	private void OpenPutty() {
		CTabItem tabItem = folder.getSelection();
		if (tabItem.getData("session") == null)
			return;
		ConfigSession session = (ConfigSession) tabItem.getData("session");
		InvokeProgram.invokeSinglePutty(session);
	}

	private void openVNCSession() {
		CTabItem item = folder.getSelection();
		if (item.getData("session") == null)
			return;
		ConfigSession session = (ConfigSession) item.getData("session");
		if (session != null) {
			String host = session.getHost();
			InputDialog inputDialog = new InputDialog(shell, "Input VNC Server Host",
					"Example:    xx.swg.usma.ibm.com:1", host + ":1", null);
			if (InputDialog.OK == inputDialog.open()) {
				InvokeProgram.runProgram(Program.APP_VNC, inputDialog.getValue());
			}
		}
	}

	public void disposeApp() {
		CTabItem[] items = folder.getItems();
		for (CTabItem item : items) {
			if (item.getData("hwnd") != null) {
				int hwnd = Integer.parseInt(String.valueOf(item.getData("hwnd")));
				InvokeProgram.killProcess(hwnd);
			}
		}

		// Close in-memory database:
		dbm.closeDB();

		// Save configuration:
		configuration.saveConfiguration();
	}

	/**
	 * Show or hide a group of components.
	 * 
	 * @param visible
	 */
	private void setCompositeVisible(Composite composite, boolean visible) {
		// Show/Hide all composite children:
		for (Control control : composite.getChildren()) {
			control.setVisible(visible);
			control.setBounds(composite.getClientArea());
			control.getParent().layout();
		}

		// Show/Hide composite:
		composite.setVisible(visible);

		// Re-layout main screen to maximize tabs zone:
		composite.layout(true, true);
		shell.layout(true, true);
	}

	public static void main(String[] args) {
		RegistryUtils.createPuttyKeys();

		new MainFrame();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == newItem || e.getSource() == itemNew) {
			new NewSessionDialog(this, null, "add");
		} else if (e.getSource() == itemOpen || e.getSource() == openItem) {
			new OpenSessionDialog(this, shell);
		} else if (e.getSource() == itemRemoteDesk || e.getSource() == remoteDesktopItem) {
			InvokeProgram.runProgram(Program.APP_REMOTE_DESK, null);
		} else if (e.getSource() == exitItem) {
			disposeApp();
			System.exit(0);
		} else if (e.getSource() == itemCapture || e.getSource() == captureItem) {
			InvokeProgram.runProgram(Program.APP_CAPTURE, null);
			shell.setMinimized(true);
		} else if (e.getSource() == itemCalculator) {
			InvokeProgram.runProgram(Program.APP_CALCULATOR, null);
		} else if (e.getSource() == itemVNC) {
			InvokeProgram.runProgram(Program.APP_VNC, null);
		} else if (e.getSource() == itemNotePad) {
			InvokeProgram.runProgram(Program.APP_NOTEPAD, null);
		} else if (e.getSource() == itemKenGen) {
			InvokeProgram.runCMD(configuration.getKeyGeneratorExecutable(), null);
		} else if (e.getSource() == itemHelp || e.getSource() == webcomeMenuItem) {
			showWelcomeTab(ConstantValue.HOME_URL);
		} else if (e.getSource() == updateItem) {
			MessageDialog.openInformation(shell, "Update tool", "remaining function");
		} else if (e.getSource() == utilitiesBarMenuItem) {
			Boolean visible = utilitiesBarMenuItem.getSelection();
			setCompositeVisible(utilitiesToolbar, visible);
			configuration.setUtilitiesBarVisible(String.valueOf(visible));
		} else if (e.getSource() == connectionBarMenuItem) {
			Boolean visible = connectionBarMenuItem.getSelection();
			setCompositeVisible(connectGroup, visible);
			configuration.setConnectionBarVisible(String.valueOf(visible));
		} else if (e.getSource() == configProgramsLocationsItem) {
			new ProgramsLocationsDialog(shell);
			// menuItem
		} else if (e.getSource() == reloadPopItem) {
			reloadSession();
		} else if (e.getSource() == openPuttyItem) {
			OpenPutty();
		} else if (e.getSource() == clonePopItem) {
			cloneSession();
		} else if (e.getSource() == ftpMenuItem) {
			openWinscp("ftp");
		} else if (e.getSource() == scpMenuItem) {
			openWinscp("scp");
		} else if (e.getSource() == sftpMenuItem) {
			openWinscp("sftp");
		} else if (e.getSource() == vncPopItem) {
			openVNCSession();
			// folder
		} else if (e.getSource() == folder) {
			if (folder.getSelection().getData("hwnd") != null) {
				int hwnd = (Integer) folder.getSelection().getData("hwnd");
				InvokeProgram.setWindowFocus(hwnd);
			}
		} else if (((MenuItem) e.getSource()).getData("type").equals("dynamicApplication")) {
			String path = ((MenuItem) e.getSource()).getData("path").toString();
			String argument = ((MenuItem) e.getSource()).getData("argument").toString();
			InvokeProgram.runCMD(path, argument);
		}
	}

	@Override
	public void close(CTabFolderEvent e) {
		if (e.item == folder.getSelection()) {
			if ((ConfigSession) e.item.getData("session") != null) {
				MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messagebox.setText("Confirm Exit");
				messagebox.setMessage(
						"Are you sure to exit session: " + ((ConfigSession) e.item.getData("session")).getHost());
				if (messagebox.open() == SWT.YES) {
					int hwnd = Integer.parseInt(String.valueOf(e.item.getData("hwnd")));
					InvokeProgram.killProcess(hwnd);

					e.item.dispose();
					e.doit = true;
				} else
					e.doit = false;
			}

		} else {
			e.item.dispose();
			e.doit = true;
			shell.setFocus();
		}
	}

	/**
	 * Shows a popup window on program start. Usefull to hide definitively
	 * "Welcome Page" tab.
	 */
	// private void showStartPopup(){
	// final Shell dialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM |
	// SWT.APPLICATION_MODAL);
	//
	// RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
	// rowLayout.marginTop = 10;
	// rowLayout.marginBottom = 10;
	// rowLayout.marginLeft = 5;
	// rowLayout.marginRight = 5;
	// rowLayout.spacing = 20;
	// dialog.setLayout(rowLayout);
	//
	// // dialog.setImage(MImage.newImage); // TODO: setup an image?
	// dialog.setSize(400, 160);
	// dialog.setText("Welcome tab");
	//
	// final Button checkButton = new Button(dialog, SWT.CHECK | SWT.LEFT);
	// checkButton.setText("Do not show \"Welcome page\" next time");
	// checkButton.setLayoutData(new RowData(250, 20));
	//
	// Button buttonClose = new Button(dialog, SWT.CENTER);
	// buttonClose.setText("Close");
	// buttonClose.setLayoutData(new RowData(50, 20));
	// buttonClose.addSelectionListener(new SelectionListener(){
	// @Override
	// public void widgetSelected(SelectionEvent se){
	// if (checkButton.getSelection()){
	// configuration.setWelcomePageVisible("false"); // "Welcome page" no to be
	// shown any more.
	// configuration.saveConfiguration();
	// }
	// dialog.dispose();
	// }
	// @Override
	// public void widgetDefaultSelected(SelectionEvent se){
	// }
	// });
	//
	// dialog.pack();
	// dialog.open();
	// }

	@Override
	public void maximize(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void minimize(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void restore(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showList(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDoubleClick(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.button == 3) {
			CTabItem selectItem = folder.getItem(new Point(e.x, e.y));
			if (selectItem != null
					&& StringUtils.equalsIgnoreCase(String.valueOf(folder.getSelection().getData("TYPE")), "session")) {
				folder.setSelection(selectItem);
				popupmenu.setVisible(true);
			} else {
				popupmenu.setVisible(false);
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shellActivated(ShellEvent e) {
		// TODO Auto-generated method stub
		if (folder.getSelection() != null) {
			Object objhwnd = folder.getSelection().getData("hwnd");
			if (objhwnd != null) {
				InvokeProgram.setWindowFocus(Integer.parseInt(objhwnd.toString()));
			}
		}
	}

	@Override
	public void shellClosed(ShellEvent e) {
		// TODO Auto-generated method stub
		// disableProxy();
		disposeApp();
	}

	@Override
	public void shellDeactivated(ShellEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shellDeiconified(ShellEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shellIconified(ShellEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

}
