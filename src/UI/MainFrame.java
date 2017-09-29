package UI;

import Control.Configuration;
import Control.InvokeProgram;
import Dao.DBManager;
import Model.*;
import Model.BorderLayout;
import Utils.RegistryUtils;
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.awt.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.List;

public class MainFrame implements SelectionListener, CTabFolder2Listener, MouseListener, ShellListener {
	final static Logger logger = Logger.getLogger(MainFrame.class);
	public static Display display = new Display();
	public static DBManager dbm;
	final public static Shell shell = new Shell(display);
	public static Configuration configuration;
	private MenuItem openItem, newItem, captureItem, remoteDesktopItem, exitItem, updateItem, webcomeMenuItem,
			reloadPopItem, clonePopItem, transferPopItem, scpMenuItem, ftpMenuItem, sftpMenuItem, vncPopItem,
			openPuttyItem, configProgramsLocationsItem, utilitiesBarMenuItem, connectionBarMenuItem,
			bottomQuickBarMenuItem;
	private Menu popupmenu;
	private ToolItem itemNew, itemOpen, itemRemoteDesk, itemCapture, itemCalculator, itemVNC, itemNotePad, itemKenGen,
			itemHelp;
	private CTabFolder folder;
	private CTabItem welcomeItem, dictItem;
	private ToolBar utilitiesToolbar;
	private Group connectGroup, quickBottomGroup;

	// connect bar components
	private Button connectButton;
	private Text usernameItem, passwordItem, portItem;
	private Combo sessionCombo;

	// bottom util bar components
	private Text pathItem, dictText;
	private Button win2UnixButton, unix2WinButton, openPathButton, dictButton;

	// Empty constructor to no use splash screen.
	public MainFrame(){
		RegistryUtils.createPuttyKeys();

		loadConfiguration();

		shell.setLayout(new BorderLayout());
		shell.setImage(MImage.mainImage);
		shell.setText(ConstantValue.MAIN_WINDOW_TITLE + " [" + ConstantValue.MAIN_WINDOW_VERSION + "]");
		shell.setBounds(configuration.getWindowPositionSize());
		shell.addShellListener(this);

		// Get dbmanager instance:
		dbm = DBManager.getDBManagerInstance();
//		bar.setSelection(3);

		// Main menu:
		createMainMenu(shell);
//		bar.setSelection(4);

		// Create upper connection toolbar:
		createConnectionBar(shell);

		// Create upper utilities toolbar:
		createUtilitiesToolbar(shell);

		// Create bottom utilities toolbar:
		createBottomUtilitiesToolBar(shell);

		// Create lower tabs zone:
		createTabs(shell);
		createTabPopupMenu(shell); // Right button menu

		// Show/Hide toolbars based on configuration file values:
		setVisibleComponents();
		if (configuration.getWelcomePageVisible())
			showWelcomeTab(ConstantValue.HOME_URL);
		applyFeatureToggle();
	}

	public MainFrame(SplashScreen splash, Graphics2D g){
		Splash.renderSplashFrame(g, "Creating registery");
		splash.update();
		RegistryUtils.createPuttyKeys();

		Splash.renderSplashFrame(g, "Loading config");
		splash.update();
		loadConfiguration();

		shell.setLayout(new BorderLayout());
		shell.setImage(MImage.mainImage);
		shell.setText(ConstantValue.MAIN_WINDOW_TITLE + " [" + ConstantValue.MAIN_WINDOW_VERSION + "]");
		shell.setBounds(configuration.getWindowPositionSize());
		shell.addShellListener(this);

        Splash.renderSplashFrame(g, "Loading database");
        splash.update();
		// Get dbmanager instance:
		dbm = DBManager.getDBManagerInstance();
//		bar.setSelection(3);
		// Main menu:
        Splash.renderSplashFrame(g, "Create menus");
        splash.update();
		createMainMenu(shell);
//		bar.setSelection(4);

        Splash.renderSplashFrame(g, "Create toolbar");
        splash.update();
		// Create upper connection toolbar:
		createConnectionBar(shell);

		// Create upper utilities toolbar:
		createUtilitiesToolbar(shell);

        Splash.renderSplashFrame(g, "Create utilities toolbar");
        splash.update();
		// Create bottom utilities toolbar:
		createBottomUtilitiesToolBar(shell);

        Splash.renderSplashFrame(g, "Create tabs");
        splash.update();
		// Create lower tabs zone:
		createTabs(shell);
		createTabPopupMenu(shell); // Right button menu

        Splash.renderSplashFrame(g, "Init features");
        splash.update();
		// Show/Hide toolbars based on configuration file values:
		setVisibleComponents();
		if (configuration.getWelcomePageVisible())
			showWelcomeTab(ConstantValue.HOME_URL);
		applyFeatureToggle();
        Splash.renderSplashFrame(g, "Init complete");
        splash.update();
	}

	public void open(){
		shell.open();
	}
//	public void setVisible(boolean b){
//		shell.setVisible(b);
//	}

	/**
	 * Main menu.
	 */
	private void createMainMenu(Shell shell) {
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

		bottomQuickBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
		bottomQuickBarMenuItem.setText("Bottom Quick bar");
		bottomQuickBarMenuItem.setSelection(true);
		bottomQuickBarMenuItem.addSelectionListener(this);

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
			// menuItem.setToolTipText(path + " " + argument);
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
	private void createConnectionBar(Shell shell) {
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
		usernameItem = new Text(connectGroup, SWT.BORDER);
		usernameItem.setText(configuration.getDefaultPuttyUsername());
		usernameItem.setLayoutData(new RowData(100, 20));

		// Password
		new Label(connectGroup, SWT.RIGHT).setText("Password");
		passwordItem = new Text(connectGroup, SWT.PASSWORD | SWT.BORDER);
		passwordItem.setLayoutData(new RowData(80, 20));

		// Port:
		new Label(connectGroup, SWT.RIGHT).setText("Port");
		portItem = new Text(connectGroup, SWT.BORDER);
		portItem.setText("22");
		portItem.setLayoutData(new RowData(20, 20));

		// Session:
		new Label(connectGroup, SWT.RIGHT).setText("Session");
		sessionCombo = new Combo(connectGroup, SWT.READ_ONLY);
		sessionCombo.setLayoutData(new RowData());
		sessionCombo.setToolTipText("Session to use");
		sessionCombo.add(""); // Empty entry to use none.
		// Get all "Putty" sessions:
		List<String> sessions = RegistryUtils.getAllPuttySessions();
		for (String session : sessions) {
			sessionCombo.add(session);
		}

		// Connect button:
		connectButton = new Button(connectGroup, SWT.PUSH);
		connectButton.setText("Connect");
		connectButton.setImage(MImage.puttyImage);
		connectButton.setLayoutData(new RowData());
		connectButton.setToolTipText("Connect to host");
		connectButton.addSelectionListener(this);
		connectGroup.pack();
	}

	private void createBottomUtilitiesToolBar(Shell shell) {
		quickBottomGroup = new Group(shell, SWT.BAR);
		RowLayout layout1 = new RowLayout();
		layout1.marginTop = 3;
		layout1.marginBottom = 3;
		layout1.marginLeft = 1;
		layout1.marginRight = 1;
		layout1.spacing = 5;
		layout1.wrap = false;
		layout1.center = true;
		quickBottomGroup.setLayout(layout1);
		quickBottomGroup.setLayoutData(new BorderData(SWT.BOTTOM));
		// Path:
		new Label(quickBottomGroup, SWT.RIGHT).setText("Path");
		pathItem = new Text(quickBottomGroup, SWT.BORDER);
		pathItem.setText("");
		pathItem.setLayoutData(new RowData(250, 20));

		win2UnixButton = new Button(quickBottomGroup, SWT.PUSH);
		win2UnixButton.setText("->Linux");
		win2UnixButton.setImage(MImage.linux);
		win2UnixButton.setToolTipText("Convert Windows Path to Linux");
		win2UnixButton.setLayoutData(new RowData());
		win2UnixButton.addSelectionListener(this);

		unix2WinButton = new Button(quickBottomGroup, SWT.PUSH);
		unix2WinButton.setText("->Windows");
		unix2WinButton.setToolTipText("Convert Linux path to Windows");
		unix2WinButton.setImage(MImage.windows);
		unix2WinButton.setLayoutData(new RowData());
		unix2WinButton.addSelectionListener(this);

		openPathButton = new Button(quickBottomGroup, SWT.PUSH);
		openPathButton.setText("Open");
		openPathButton.setImage(MImage.folder);
		openPathButton.setToolTipText("Open directory/file");
		openPathButton.addSelectionListener(this);

		// Dictionary
		new Label(quickBottomGroup, SWT.RIGHT).setText("Dictionary");
		dictText = new Text(quickBottomGroup, SWT.BORDER);
		dictText.setLayoutData(new RowData(100, 20));
		dictText.addListener(SWT.Traverse, (event) -> {
			if (event.detail == SWT.TRAVERSE_RETURN) {
				String keyword = dictText.getText().trim();
				OpenDictTab(keyword);
			}
		});

		dictButton = new Button(quickBottomGroup, SWT.PUSH);
		dictButton.setText("Search");
		dictButton.setToolTipText("Search Keywork in Dictionary");
		dictButton.setImage(MImage.dictImage);
		dictButton.addSelectionListener(this);
		quickBottomGroup.pack();
	}

	/**
	 * Create utilities toolbar.
	 */
	private void createUtilitiesToolbar(Shell shell) {
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
	private void createTabs(Shell shell) {
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
	private void createTabPopupMenu(Shell shell) {
		popupmenu = new Menu(shell, SWT.POP_UP);
		reloadPopItem = new MenuItem(popupmenu, SWT.PUSH);
		reloadPopItem.setText("reload session");
		reloadPopItem.setImage(MImage.reloadImage);
		reloadPopItem.addSelectionListener(this);

//		 openPuttyItem = new MenuItem(popupmenu, SWT.PUSH);
//		 openPuttyItem.setText("open in putty");
//		 openPuttyItem.setImage(MImage.puttyImage);
//		 // openPuttyItem.setToolTipText("Opens connection on a single
//		 window");
//		 openPuttyItem.addSelectionListener(this);

		clonePopItem = new MenuItem(popupmenu, SWT.PUSH);
		clonePopItem.setText("clone session");
		clonePopItem.setImage(MImage.cloneImage);
		clonePopItem.addSelectionListener(this);

		vncPopItem = new MenuItem(popupmenu, SWT.PUSH);
		vncPopItem.setText("VNC");
		vncPopItem.setImage(MImage.vncImage);
		vncPopItem.addSelectionListener(this);

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


	}

	private void loadConfiguration() {
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
		} else {
			folder.setSelection(welcomeItem);
		}
	}

	private void OpenDictTab(String keyword) {
		if (dictItem == null || dictItem.isDisposed()) {
			dictItem = new CTabItem(folder, SWT.CLOSE);
			dictItem.setImage(MImage.dictImage);
			Browser browser = new Browser(folder, SWT.NONE);
			browser.setUrl(configuration.getDictionaryBaseUrl() + keyword);
			dictItem.setControl(browser);
			folder.setSelection(dictItem);
			dictItem.setText("Dictionary");
		} else {
			folder.setSelection(dictItem);
			((Browser) dictItem.getControl()).setUrl(configuration.getDictionaryBaseUrl() + keyword);
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
		bottomQuickBarMenuItem.setSelection(configuration.getBottomQuickBarVisible());
		bottomQuickBarMenuItem.notifyListeners(SWT.Selection, event);

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
	private void setCompositeVisible(Composite composite, Shell shell, boolean visible) {
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

	/**
	 * check the feature toggle, dispose the features who equals to "false"
	 */
	private void applyFeatureToggle() {
		Properties props = configuration.getFeatureToggleProps();
		boolean bVnc = "true".equalsIgnoreCase(props.getProperty("vnc", "true"));
		if (!bVnc) {
			this.vncPopItem.dispose();
			this.itemVNC.dispose();
		}

		boolean bTransfer = "true".equalsIgnoreCase(props.getProperty("transfer", "true"));
		if (!bTransfer) {
			this.transferPopItem.dispose();
		}
	}

	/**
	 * Prepare the global variables for the other splash functions.
	 * Use with this VM option: -splash:icon/splash.jpg
	 */
	private static void splashInit(){
		final MainFrame main;
		final SplashScreen splash = SplashScreen.getSplashScreen();

		// If no splash image has been defined in VM options...
        if (splash == null){
			logger.info("No splash image defined!");
			main = new MainFrame();
		} else {
			Graphics2D g = splash.createGraphics();
			if (g == null){
				logger.info("Graphics for splash image can't be created!");
				main = new MainFrame();
			} else {
				main = new MainFrame(splash, g);
				splash.close();
			}
		}

		main.open();
	}

	public static void main(String[] args){
		// Initialize splash image:
		splashInit();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
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
			setCompositeVisible(utilitiesToolbar, shell, visible);
			configuration.setUtilitiesBarVisible(String.valueOf(visible));
		} else if (e.getSource() == connectionBarMenuItem) {
			Boolean visible = connectionBarMenuItem.getSelection();
			setCompositeVisible(connectGroup, shell, visible);
			configuration.setConnectionBarVisible(String.valueOf(visible));
		} else if (e.getSource() == bottomQuickBarMenuItem) {
			Boolean visible = bottomQuickBarMenuItem.getSelection();
			setCompositeVisible(quickBottomGroup, shell, visible);
			configuration.setBottomQuickBarVisible(String.valueOf(visible));
		}

		else if (e.getSource() == configProgramsLocationsItem) {
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
		} else if (StringUtils.endsWith(e.getSource().getClass().toString(), "MenuItem")
				&& "dynamicApplication".equals(((MenuItem) e.getSource()).getData("type"))) {
			String path = ((MenuItem) e.getSource()).getData("path").toString();
			String argument = ((MenuItem) e.getSource()).getData("argument").toString();
			InvokeProgram.runCMD(path, argument);
		} else if (e.getSource() == connectButton) {
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
		} else if (e.getSource() == win2UnixButton) {
			String path = pathItem.getText().trim();
			if (StringUtils.isBlank(path)) {
				MessageDialog.openInformation(shell, "Info", "Please input correct path!");
				return;
			}
			path = StringUtils.stripStart(path, "/\\" + configuration.getWinPathBaseDrive());
			pathItem.setText("/" + FilenameUtils.separatorsToUnix(path));
		} else if (e.getSource() == unix2WinButton) {
			String path = pathItem.getText().trim();
			if (StringUtils.isBlank(path)) {
				MessageDialog.openInformation(shell, "Info", "Please input correct path!");
				return;
			}
			path = StringUtils.stripStart(path, "/\\" + configuration.getWinPathBaseDrive());
			pathItem.setText(configuration.getWinPathBaseDrive() + "\\" + FilenameUtils.separatorsToWindows(path));
		} else if (e.getSource() == openPathButton) {
			String path = pathItem.getText().trim();
			if (StringUtils.isBlank(path)) {
				MessageDialog.openInformation(shell, "Info", "Please input correct path!");
				return;
			}
			path = StringUtils.stripStart(path, "/\\" + configuration.getWinPathBaseDrive());
			path = configuration.getWinPathBaseDrive() + "\\" + FilenameUtils.separatorsToWindows(path);
			pathItem.setText(path);
			if (!InvokeProgram.openFolder(path)) {
				MessageDialog.openError(shell, "Error", "Path not exist!");
			}
		} else if (e.getSource() == dictButton) {
			String keyword = dictText.getText().trim();
			OpenDictTab(keyword);
		}
	}

	@Override
	public void close(CTabFolderEvent e) {
		if (e.item == folder.getSelection()) {
			if ( e.item.getData("session") != null) {
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
