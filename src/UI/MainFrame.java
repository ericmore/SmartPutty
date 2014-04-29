package UI;

import java.io.PrintStream;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import Control.Configuration;
import Control.InvokeProgram;
import Dao.DBManager;
import Model.BorderData;
import Model.BorderLayout;
import Model.ConfigSession;
import Model.ConstantValue;

import com.ice.jni.registry.RegDWordValue;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryValue;

public class MainFrame implements SelectionListener, CTabFolder2Listener,
		MouseListener, ShellListener {
	public static Display display = null;
	public static PrintStream out = System.out;
	public static PrintStream err = System.err;
	public static DBManager dbm;
	public static Shell shell;
	public static Configuration configuration;
	private MenuItem openItem, newItem, bsoItem, proxyItem, captureItem,
			remoteDesktopItem, updateItem, webcomeMenuItem, reloadPopItem,
			clonePopItem, transferPopItem, scpMenuItem, ftpMenuItem, vncPopItem, openPuttyItem;
	private Menu popupmenu;
	private ToolBar bar;
	private ToolItem itemNew, itemOpen, itemProxy, itemRemoteDesk, itemBSO,
			itemCapture, itemCalculator, itemVNC, itemNotePad,itemKenGen, itemHelp;
	private CTabFolder folder;
	private CTabItem welcomeItem;

	public MainFrame() {
		display = new Display();
		shell = new Shell(display);
		checkConf();

		shell.setLayout(new BorderLayout());
		shell.setImage(MImage.mainImage);
		shell.setText(ConstantValue.mainWindowTitle);
		shell.setBounds(ConstantValue.screenWidth / 6,
				ConstantValue.screenHeight / 6,
				2 * ConstantValue.screenWidth / 3,
				2 * ConstantValue.screenHeight / 3);
		shell.addShellListener(this);

		// get dbmanager instance
		dbm = DBManager.getDBManagerInstance();
		// Menu
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
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

		// tool bar
		bar = new ToolBar(shell, SWT.NONE);
		itemNew = new ToolItem(bar, SWT.PUSH);
		itemNew.setText("New");
		itemNew.setToolTipText("create a new session");
		itemNew.setImage(MImage.newImage);
		itemNew.addSelectionListener(this);
		itemOpen = new ToolItem(bar, SWT.PUSH);
		itemOpen.setText("Open");
		itemOpen.setToolTipText("open existing sessions");
		itemOpen.setImage(MImage.openImage);
		itemOpen.addSelectionListener(this);
//		itemProxy = new ToolItem(bar, SWT.PUSH);
//		itemProxy.setText("Enable Proxy");
//		itemProxy
//				.setToolTipText("open tunnel and enable system proxy for browser");
//		itemProxy.setImage(MImage.enableProxyImage);
//		itemProxy.addSelectionListener(this);

		itemRemoteDesk = new ToolItem(bar, SWT.PUSH);
		itemRemoteDesk.setText("RemoteDesk");
		itemRemoteDesk.setToolTipText("open system remotedesk tool");
		itemRemoteDesk.setImage(MImage.RemoteDeskImage);
		itemRemoteDesk.addSelectionListener(this);
//		itemBSO = new ToolItem(bar, SWT.PUSH);
//		itemBSO.setText("IBM BSO");
//		itemBSO.setToolTipText("Pass IBM BSO");
//		itemBSO.setImage(MImage.bsoImage);
//		itemBSO.addSelectionListener(this);
		itemCapture = new ToolItem(bar, SWT.PUSH);
		itemCapture.setText("Capture");
		itemCapture.setToolTipText("Open FastStone Capture");
		itemCapture.setImage(MImage.captureImage);
		itemCapture.addSelectionListener(this);
		itemCalculator = new ToolItem(bar, SWT.PUSH);
		itemCalculator.setText("Calculator");
		itemCalculator.setToolTipText("open microsoft calculator");
		itemCalculator.setImage(MImage.calculator);
		itemCalculator.addSelectionListener(this);
		itemVNC = new ToolItem(bar, SWT.PUSH);
		itemVNC.setText("VNC");
		itemVNC.setToolTipText("open VNC");
		itemVNC.setImage(MImage.vncImage);
		itemVNC.addSelectionListener(this);
		itemNotePad = new ToolItem(bar, SWT.PUSH);
		itemNotePad.setText("NotePad");
		itemNotePad.setToolTipText("open NotePad");
		itemNotePad.setImage(MImage.notepad);
		itemNotePad.addSelectionListener(this);
		itemKenGen = new ToolItem(bar, SWT.PUSH);
		itemKenGen.setText("KenGen");
		itemKenGen.setToolTipText("Convert SSH Key");
		itemKenGen.setImage(MImage.key);
		itemKenGen.addSelectionListener(this);
		itemHelp = new ToolItem(bar, SWT.PUSH);
		itemHelp.setText("Help");
		itemHelp.setToolTipText("help document");
		itemHelp.setImage(MImage.helpImage);
		itemHelp.addSelectionListener(this);
		bar.setLayoutData(new BorderData(SWT.TOP));
		bar.pack();

		// tabs
		folder = new CTabFolder(shell, SWT.BORDER);
		folder.setLayoutData(new BorderData());
		folder.setSimple(false);
		folder.setUnselectedCloseVisible(false);
		folder.addCTabFolder2Listener(this);
		folder.addMouseListener(this);
		folder.addSelectionListener(this);

		// right button
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

		setWebcomeTab();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	private void checkConf() {
		InvokeProgram.killPuttyWarningsAndErrs();
		configuration = Configuration.getInstance();
	}

	public void setWebcomeTab() {
		if (welcomeItem == null || welcomeItem.isDisposed()) {
			welcomeItem = new CTabItem(folder, SWT.CLOSE);
			Browser browser = new Browser(folder, SWT.NONE);
			browser.setUrl(ConstantValue.HOME_URL);
			welcomeItem.setControl(browser);
			folder.setSelection(welcomeItem);
			welcomeItem.setText("Welcome Page");
		}
	}

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
	
	private void OpenPutty(){
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

	public static void main(String[] args) {

		try {
			RegistryKey subkey = Registry.HKEY_CURRENT_USER
					.createSubKey(
							"Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings",
							"");
			subkey.setValue(new RegDWordValue(subkey, "WarnOnClose",
					RegistryValue.REG_DWORD, 0));
			subkey.setValue(new RegStringValue(subkey, "LineCodePage", "UTF-8"));
			subkey.setValue(new RegDWordValue(subkey, "ScrollbackLines",
					RegistryValue.REG_DWORD, 5000));
			subkey.setValue(new RegStringValue(subkey, "WinTitle", ""));
			subkey.setValue(new RegDWordValue(subkey, "NoRemoteWinTitle",
					RegistryValue.REG_DWORD, 1));
			subkey.setValue(new RegDWordValue(subkey, "BlinkCur",
					RegistryValue.REG_DWORD, 1));
			subkey.closeKey();
			new MainFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

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
		} else if (e.getSource() == itemHelp) {
			setWebcomeTab();
		} else if (e.getSource() == updateItem) {
			MessageDialog.openInformation(shell, "Update tool",
					"remaining function");
		} else if (e.getSource() == webcomeMenuItem) {
			setWebcomeTab();
		}
		// menuItem ÊÂ¼þ
		else if (e.getSource() == reloadPopItem) {
			reloadSession();
		}else if (e.getSource() == openPuttyItem) {
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
	}

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

	public void maximize(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub

	}

	public void minimize(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub

	}

	public void restore(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub

	}

	public void showList(CTabFolderEvent ctabfolderevent) {
		// TODO Auto-generated method stub

	}

	public void mouseDoubleClick(MouseEvent mouseevent) {
		// TODO Auto-generated method stub

	}

	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub

		if (e.button == 3) {
			CTabItem selectItem = folder.getItem(new Point(e.x, e.y));
			if (selectItem != null&& welcomeItem != folder.getSelection()) {
				folder.setSelection(selectItem);
				popupmenu.setVisible(true);
			} else {
				popupmenu.setVisible(false);
			}
		}

	}

	public void mouseUp(MouseEvent mouseevent) {
		// TODO Auto-generated method stub

	}

	public void shellActivated(ShellEvent e) {
		// TODO Auto-generated method stub

		if (folder.getSelection() != null) {
			Object objhwnd = folder.getSelection().getData("hwnd");
			if (objhwnd != null)
				InvokeProgram.setWindowFocus(Integer.parseInt(objhwnd
						.toString()));
		}
	}

	public void shellClosed(ShellEvent e) {
		// TODO Auto-generated method stub
		// disableProxy();
		disposeApp();
	}

	public void shellDeactivated(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void shellDeiconified(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

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
