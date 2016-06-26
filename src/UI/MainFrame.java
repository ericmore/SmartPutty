package UI;

import Control.Configuration;
import Control.InvokeProgram;
import Dao.DBManager;
import Model.BorderData;
import Model.BorderLayout;
import Model.ConfigSession;
import Model.ConstantValue;
import Model.Protocol;
import Utils.RegistryUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainFrame implements SelectionListener, CTabFolder2Listener,
		MouseListener, ShellListener {
	public static Display display = null;
	public static PrintStream out = System.out;
	public static PrintStream err = System.err;
	public static DBManager dbm;
	public static Shell shell;
	public static Configuration configuration;
	private MenuItem openItem, newItem, bsoItem, proxyItem, captureItem,
			remoteDesktopItem, exitItem, updateItem, connectionBarMenuItem,
			utilitiesBarMenuItem, webcomeMenuItem, reloadPopItem, clonePopItem,
			transferPopItem, scpMenuItem, ftpMenuItem, vncPopItem,
			openPuttyItem;
	private Menu popupmenu;
	private ToolItem itemNew, itemOpen, itemProxy, itemRemoteDesk, itemBSO,
			itemCapture, itemCalculator, itemVNC, itemNotePad, itemKenGen,
			itemHelp;
	private CTabFolder folder;
	private CTabItem welcomeItem;
	private ToolBar utilitiesToolbar;
	private Group connectGroup;
	private Combo sessionCombo;
	private Text hostnameItem, usernameItem;

	public MainFrame() {
		display = new Display();
		shell = new Shell(display);

		checkConf();

		shell.setLayout(new BorderLayout());
		shell.setImage(MImage.mainImage);
		shell.setText(ConstantValue.mainWindowTitle + " [" + ConstantValue.mainWindowVersion + "]");
		shell.setBounds(ConstantValue.screenWidth / 12,
				ConstantValue.screenHeight / 12,
				10 * ConstantValue.screenWidth / 12,
				10 * ConstantValue.screenHeight / 12);
		shell.addShellListener(this);

		// get dbmanager instance
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

		setWebcomeTab();

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

		bsoItem = new MenuItem(filemenu, SWT.PUSH);
		bsoItem.setText("BSO\tCtrl+B");
		bsoItem.setImage(MImage.bsoImage);
		bsoItem.setAccelerator(SWT.CTRL + 'B');
		bsoItem.addSelectionListener(this);

		proxyItem = new MenuItem(filemenu, SWT.PUSH);
		proxyItem.setText("Proxy\tCtrl+P");
		proxyItem.setImage(MImage.enableProxyImage);
		proxyItem.setAccelerator(SWT.CTRL + 'P');
		proxyItem.addSelectionListener(this);

		captureItem = new MenuItem(filemenu, SWT.PUSH);
		captureItem.setText("Capture\tCtrl+C");
		captureItem.setImage(MImage.captureImage);
		captureItem.setAccelerator(SWT.CTRL + 'C');
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

		// Protocol:
		new Label(connectGroup, SWT.RIGHT).setText("Protocol");
		final Combo protocolCombo = new Combo(connectGroup, SWT.LEFT
				| SWT.READ_ONLY);
		// Get all protocols and add:
		for (Protocol protocol : Protocol.values()) {
			protocolCombo.add(protocol.getName());
		}
		protocolCombo.select(0); // Set default value.
		protocolCombo.setToolTipText("Protocol to use");
		protocolCombo.setLayoutData(new RowData(30, 14));

		// Hostname:
		new Label(connectGroup, SWT.RIGHT).setText("Hostname");
		hostnameItem = new Text(connectGroup, SWT.BORDER);
		hostnameItem.setLayoutData(new RowData(180, 14));

		// Port:
		new Label(connectGroup, SWT.RIGHT).setText("Port");
		final Text portItem = new Text(connectGroup, SWT.BORDER);
		portItem.setText("22");
		portItem.setLayoutData(new RowData(30, 14));

		// Username:
		new Label(connectGroup, SWT.RIGHT).setText("Username");
		usernameItem = new Text(connectGroup, SWT.BORDER);
		usernameItem.setLayoutData(new RowData(60, 14));

		// Password
		new Label(connectGroup, SWT.RIGHT).setText("Password");
		final Text passwordItem = new Text(connectGroup, SWT.PASSWORD
				| SWT.BORDER);
		passwordItem.setLayoutData(new RowData(60, 14));

		// Session:
		new Label(connectGroup, SWT.RIGHT).setText("Session");
		sessionCombo = new Combo(connectGroup, SWT.READ_ONLY);
		sessionCombo.setLayoutData(new RowData(80, 14));
		sessionCombo.setToolTipText("Session to use");
		sessionCombo.addSelectionListener(this);
		// Here we want to use default settings which contains important
		// settings where swt can search hwnd
		// sessionCombo.add(""); // Empty entry to use none.
		// Get all "Putty" sessions:
		Enumeration sessions = RegistryUtils.getAllPuttySessions();
		while (sessions.hasMoreElements()) {
			// Add "Putty" session replacing spaces by codes:
			//we do not want default settings to be added to combo.Because -load "Default%20Settings"  will make WarnOnClose to be true,
			//which will lead memory leak if user close tab but later press cancel for Putty's Native Confirm Dialog
			String regEntry = sessions.nextElement().toString();
			if (regEntry.toString().equals("Default%20Settings"))
				continue;

			sessionCombo.add(regEntry.replaceAll("%20", " "));
		}
		sessionCombo.select(0);
		LoadSessionFromReg();

		// Connect button:
		Button connectButton = new Button(connectGroup, SWT.PUSH);
		connectButton.setText("Connect");
		connectButton.setLayoutData(new RowData());
		connectButton.setToolTipText("Connect to defined host");
		connectButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				// String protocol = protocolCombo.getText().toLowerCase(); //
				// Putty wants lower case!
				Protocol protocol = Protocol.values()[protocolCombo
						.getSelectionIndex()];
				String host = hostnameItem.getText();
				String port = portItem.getText();
				String user = usernameItem.getText();
				String password = passwordItem.getText();
				String session = sessionCombo.getText();
				System.out.println("protocol: " + protocol + ", host: " + host
						+ ", port: " + port + ", user: " + user
						+ ", password: " + password + ", session: " + session); // DEBUG
				ConfigSession configSession = new ConfigSession(host, port,
						user, protocol, "", password, session);
				addSession(null, configSession);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent se) {
			}
		});

		// Clear all fields button (aka, set default values):
		Button clearButton = new Button(connectGroup, SWT.PUSH);
		clearButton.setText("Clear");
		clearButton.setLayoutData(new RowData());
		clearButton.setToolTipText("Clear all fields");
		clearButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				protocolCombo.select(0);
				hostnameItem.setText("");
				portItem.setText("22");
				usernameItem.setText("");
				passwordItem.setText("");
				passwordItem.setEnabled(true);
				sessionCombo.select(0);
				LoadSessionFromReg();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent se) {
			}
		});

		// Due only "SSH1" and "SSH2" protocols allows password I must restrict
		// it:
		protocolCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				if (protocolCombo.getSelectionIndex() > 1) {
					passwordItem.setEnabled(false);
				} else {
					passwordItem.setEnabled(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent se) {
			}
		});

		connectGroup.pack();
	}

	/**
	 * Create utilities toolbar.
	 */
	private void createUtilitiesToolbar() {
		// utilitiesToolbar = new ToolBar(shell, SWT.NULL);
		utilitiesToolbar = new ToolBar(shell, SWT.VERTICAL);
		// utilitiesToolbar.setLayoutData(new BorderData(SWT.TOP));
		utilitiesToolbar.setLayoutData(new BorderData(SWT.LEFT));

		itemNew = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemNew.setText("New");
		itemNew.setToolTipText("create a new session");
		itemNew.setImage(MImage.newImage);
		itemNew.addSelectionListener(this);

		itemOpen = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemOpen.setText(" Open ");
		itemOpen.setToolTipText("open existing sessions");
		itemOpen.setImage(MImage.openImage);
		itemOpen.addSelectionListener(this);


		// itemProxy = new ToolItem(bar, SWT.PUSH);
		// itemProxy.setText("Enable Proxy");
		// itemProxy.setToolTipText("open tunnel and enable system proxy for browser");
		// itemProxy.setImage(MImage.enableProxyImage);
		// itemProxy.addSelectionListener(this);

		itemRemoteDesk = new ToolItem(utilitiesToolbar, SWT.PUSH);
		itemRemoteDesk.setText("RemoteDesk");
		itemRemoteDesk.setToolTipText("open system remote desk tool");
		itemRemoteDesk.setImage(MImage.RemoteDeskImage);
		itemRemoteDesk.addSelectionListener(this);

		// itemBSO = new ToolItem(bar, SWT.PUSH);
		// itemBSO.setText("IBM BSO");
		// itemBSO.setToolTipText("Pass IBM BSO");
		// itemBSO.setImage(MImage.bsoImage);
		// itemBSO.addSelectionListener(this);

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

		openPuttyItem = new MenuItem(popupmenu, SWT.PUSH);
		openPuttyItem.setText("open in putty");
		openPuttyItem.setImage(MImage.puttyImage);
		openPuttyItem.addSelectionListener(this);

		clonePopItem = new MenuItem(popupmenu, SWT.PUSH);
		clonePopItem.setText("clone session");
		clonePopItem.setImage(MImage.cloneImage);
		clonePopItem.addSelectionListener(this);

		transferPopItem = new MenuItem(popupmenu, SWT.CASCADE);
		transferPopItem.setText("transfer file");
		transferPopItem.setImage(MImage.transferImage);

		Menu subMenu = new Menu(popupmenu);
		scpMenuItem = new MenuItem(subMenu, SWT.PUSH);
		scpMenuItem.setText("SCP");
		scpMenuItem.addSelectionListener(this);

		ftpMenuItem = new MenuItem(subMenu, SWT.PUSH);
		ftpMenuItem.setText("FTP");
		ftpMenuItem.addSelectionListener(this);

		transferPopItem.setMenu(subMenu);

		vncPopItem = new MenuItem(popupmenu, SWT.PUSH);
		vncPopItem.setText("VNC");
		vncPopItem.setImage(MImage.vncImage);
		vncPopItem.addSelectionListener(this);
	}

	private void checkConf() {
		InvokeProgram.killPuttyWarningsAndErrs();
		configuration = Configuration.getInstance();
	}

	private void setWebcomeTab() {
		if (welcomeItem == null || welcomeItem.isDisposed()) {
			welcomeItem = new CTabItem(folder, SWT.CLOSE);
			Browser browser = new Browser(folder, SWT.NONE);
			browser.setUrl(ConstantValue.HOME_URL);
			welcomeItem.setControl(browser);
			folder.setSelection(welcomeItem);
			welcomeItem.setText("Welcome Page");
		}
	}

	/**
	 * Open a new session in a new tab.
	 * 
	 * @param item
	 * @param session
	 */
	public void addSession(CTabItem item, ConfigSession session) {
		if (item == null)
			item = new CTabItem(folder, SWT.CLOSE);

		Composite composite = new Composite(folder, SWT.EMBEDDED);
		composite.setBackground(new Color(display, 0, 0, 0));
		item.setControl(composite);
		folder.setSelection(item);
		item.setText("connecting");
		item.setImage(MImage.getRedImage());
		Thread t = new InvokeProgram(composite, item, session);
		t.start();
	}

	private void reloadSession() {
		CTabItem tabItem = folder.getSelection();
		int hwnd = Integer.parseInt(String.valueOf(tabItem.getData("hwnd")));
		InvokeProgram.killProcess(hwnd);
		addSession(tabItem, (ConfigSession) tabItem.getData("session"));
	}

	private void cloneSession() {
		CTabItem tabItem = folder.getSelection();
		ConfigSession session = (ConfigSession) tabItem.getData("session");
		addSession(null, session);
	}

	private void openWinscp(String protocol) {
		ConfigSession session = (ConfigSession) folder.getSelection().getData(
				"session");
		String arg = protocol + "://" + session.getUser() + ":"
				+ session.getPassword() + "@" + session.getHost();
		InvokeProgram.invokeWinscp(arg);
	}

	private void OpenPutty() {
		CTabItem tabItem = folder.getSelection();
		ConfigSession session = (ConfigSession) tabItem.getData("session");
		InvokeProgram.invokeSinglePutty(session);
	}

	private void openVNCSession() {
		CTabItem item = folder.getSelection();
		ConfigSession session = (ConfigSession) item.getData("session");
		if (session != null) {
			String host = session.getHost();
			InputDialog inputDialog = new InputDialog(shell,
					"Input VNC Server Host",
					"Example:    xx.swg.usma.ibm.com:1", host + ":1", null);
			if (InputDialog.OK == inputDialog.open()) {
				InvokeProgram.invokeVNC(inputDialog.getValue());
			}
		}
	}

	public void disposeApp() {
		CTabItem[] items = folder.getItems();
		for (CTabItem item : items) {
			if (item.getData("hwnd") != null) {
				int hwnd = Integer
						.parseInt(String.valueOf(item.getData("hwnd")));
				InvokeProgram.killProcess(hwnd);
			}

		}

		dbm.closeDB();
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
		} else if (e.getSource() == itemBSO || e.getSource() == bsoItem) {
			new BSODialog(shell);
		} else if (e.getSource() == itemOpen || e.getSource() == openItem) {
			new OpenSessionDialog(this, shell);
		} else if (e.getSource() == itemProxy || e.getSource() == proxyItem) {
			InvokeProgram.invokeProxy(configuration.getProxyHost(),
					configuration.getProxyUser(),
					configuration.getProxyPassword(),
					configuration.getProxyPort());
		} else if (e.getSource() == itemRemoteDesk
				|| e.getSource() == remoteDesktopItem) {
			InvokeProgram.invokeRemoteDesk();
		} else if (e.getSource() == exitItem) {
			disposeApp();
			System.exit(0);
		} else if (e.getSource() == itemCapture || e.getSource() == captureItem) {
			InvokeProgram.invokeCapture();
			shell.setMinimized(true);
		} else if (e.getSource() == itemCalculator) {
			InvokeProgram.invokeCalculator();
		} else if (e.getSource() == itemVNC) {
			InvokeProgram.invokeVNC("");
		} else if (e.getSource() == itemNotePad) {
			InvokeProgram.invokeNotePad();
		} else if (e.getSource() == itemKenGen) {
			InvokeProgram.invokeGenKey();
		} else if (e.getSource() == itemHelp
				|| e.getSource() == webcomeMenuItem) {
			setWebcomeTab();
		} else if (e.getSource() == updateItem) {
			MessageDialog.openInformation(shell, "Update tool",
					"remaining function");
		} else if (e.getSource() == utilitiesBarMenuItem) {
			setCompositeVisible(utilitiesToolbar,
					utilitiesBarMenuItem.getSelection());
		} else if (e.getSource() == connectionBarMenuItem) {
			setCompositeVisible(connectGroup,
					connectionBarMenuItem.getSelection());
		}
		// menuItem
		else if (e.getSource() == reloadPopItem) {
			reloadSession();
		} else if (e.getSource() == openPuttyItem) {
			OpenPutty();
		} else if (e.getSource() == clonePopItem) {
			cloneSession();
		} else if (e.getSource() == scpMenuItem) {
			openWinscp("scp");
		} else if (e.getSource() == ftpMenuItem) {
			openWinscp("ftp");
		} else if (e.getSource() == vncPopItem) {
			openVNCSession();
		}
		// folder
		else if (e.getSource() == folder) {
			if (folder.getSelection().getData("hwnd") != null) {
				int hwnd = (Integer) folder.getSelection().getData("hwnd");
				InvokeProgram.setWindowFocus(hwnd);
			}

		}

		else if (e.getSource() == sessionCombo) {
			LoadSessionFromReg();
		}
	}
	
	private void LoadSessionFromReg(){
		String sessionName = sessionCombo.getText();
		String hostname = RegistryUtils.ReadSessionProp(sessionName,
				"HostName");
		String username = RegistryUtils.ReadSessionProp(sessionName,
				"UserName");
		System.out.println(String.format(
				"Read settings from session, hostname:%s  username:%s",
				hostname, username));
		// even if no hostname,username existing in session,we will clear UI
		// fields so that won't make mixed when switch selecting among
		// different sessions
		this.hostnameItem.setText(hostname);
		this.usernameItem.setText(username);
	}

	@Override
	public void close(CTabFolderEvent e) {
		// TODO Auto-generated method stub
		if (welcomeItem == folder.getSelection() && e.item == welcomeItem) {
			e.item.dispose();
			e.doit = true;
			shell.setFocus();
		} else if (e.item == folder.getSelection()) {
			MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
					| SWT.YES | SWT.NO);
			messagebox.setText("Confirm Exit");
			messagebox.setMessage("Are you sure to exit session: "
					+ ((ConfigSession) e.item.getData("session")).getHost());
			if (messagebox.open() == SWT.YES) {
				int hwnd = Integer.parseInt(String.valueOf(e.item
						.getData("hwnd")));
				InvokeProgram.killProcess(hwnd);

				e.item.dispose();
				e.doit = true;
			} else
				e.doit = false;
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
			if (selectItem != null && welcomeItem != folder.getSelection()) {
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
			if (objhwnd != null)
				InvokeProgram.setWindowFocus(Integer.parseInt(objhwnd
						.toString()));
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
