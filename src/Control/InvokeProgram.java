package Control;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Composite;

import Model.ConfigSession;
import Model.ConstantValue;
import Model.Program;
import UI.MImage;
import UI.MainFrame;

public class InvokeProgram extends Thread {
	private Composite composite = null;
	private ConfigSession session = null;
	private CTabItem tabItem = null;

	// Constructor:
	public InvokeProgram(Composite composite, CTabItem tabItem, ConfigSession session){
		this.composite = composite;
		this.tabItem = tabItem;
		this.session = session;
	}

	@Override
	public void run(){
		MainFrame.display.syncExec(new Runnable(){
			@Override
			public void run(){
				invokePutty(session);
			}
		});
	}

	public static void setWindowFocus(int hwnd){
		//System.out.println("set window focus "+hwnd);
		//OS.SendMessage(hwnd, OS.WM_SETFOCUS, 0, 0);
		//OS.SetCapture(hwnd);
		OS.SetForegroundWindow(hwnd);
		OS.SetCursor(hwnd);
	}

	public static void setMainWinowFous(){
		int hwndMainWindow = isMainWindowRunning();
		setWindowFocus(hwndMainWindow);
	}

	public static int isMainWindowRunning(){
		return (int) OS.FindWindow(null, new TCHAR(0, ConstantValue.mainWindowTitle, true));
	}

	/**
	 * Helper to mount Putty command-line parameters.
	 * @return 
	 */
	private static String setPuttyParameters(ConfigSession session){
		String args = "";
		
		String host = session.getHost();
		String port = session.getPort();
		String user = session.getUser();
		String password = session.getPassword();
		String file = session.getKey();
		String protocol = session.getProtocol() ==null? "-ssh -2" : session.getProtocol().getParameter();
		String puttySession = session.getSession();
		

		if (session.getConfigSessionType() == ConstantValue.ConfigSessionTypeEnum.PURE_PUTTY_SESSION){
			// Putty session must the very first parameter to work well.
			args = " -load \"" + puttySession + "\"";
			if(!user.isEmpty()) args += String.format(" -l \"%s\"", user);
			if(!password.isEmpty()) args += String.format(" -pw \"%s\"", password);
			if(!port.isEmpty()) args += String.format(" -P %s ", port);
		}else{
			args = String.format(" %s %s -l %s " , protocol, host, user);
			
			if (!password.isEmpty()){args += String.format(" -pw \"%s\"", password);}
			//private key
			if (!file.isEmpty())String.format(" -i \"%s\"", file);
			if(!port.isEmpty()) args += String.format(" -P %s ", port);
			
		}

		

		 System.out.println("Putty parameters: " + args); //DEBUG

		return args;
	}

	/**
	 * Start Putty in a tab.
	 * @param session 
	 */
	public void invokePutty(ConfigSession session){
		// Mount command-line Putty parameters:
		String tabDisplayName = "session";
		if (session.getConfigSessionType() == ConstantValue.ConfigSessionTypeEnum.PURE_PUTTY_SESSION){
			tabDisplayName = session.getSession();
		}else{
			tabDisplayName = session.getHost();
		}
		
		
		String args = setPuttyParameters(session);

		int hHeap = (int) OS.GetProcessHeap();
		TCHAR buffer = new TCHAR(0, MainFrame.configuration.getPuttyExecutable(), true);
		int byteCount = buffer.length() * TCHAR.sizeof;
		int lpFile = (int) OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		TCHAR buffer1 = new TCHAR(0, args, true);
		int byteCount1 = buffer1.length() * TCHAR.sizeof;
		int lpParameters = (int) OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount1);

		OS.MoveMemory(lpFile, buffer, byteCount);
		OS.MoveMemory(lpParameters, buffer1, byteCount1);

		SHELLEXECUTEINFO info = new SHELLEXECUTEINFO();
		info.cbSize = SHELLEXECUTEINFO.sizeof;
		info.lpFile = lpFile;
		info.lpParameters = lpParameters;

		//info.nShow = OS.SW_HIDE;

		boolean result = OS.ShellExecuteEx(info);

		if (lpFile != 0)
			OS.HeapFree(hHeap, 0, lpFile);

		if (lpParameters != 0)
			OS.HeapFree(hHeap, 0, lpParameters);

		if (result == false){
			MessageDialog.openInformation(MainFrame.shell, "OPEN PUTTY ERROR", String.format("Failed cmd: %s %s",MainFrame.configuration.getPuttyExecutable(), args));
			return;
		}

		
		

		int hwndalert = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true));

		if (hwndalert != 0){
			int waitingForOperation = 10000;
			while (waitingForOperation > 0){
				try {
					if (OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true)) == 0){
						break;
					}

					Thread.sleep(500);
					waitingForOperation -= 500;
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		int count = 3;
		int hwnd = 0;
		while(count>0 &&(hwnd = (int) OS.FindWindow(new TCHAR(0, "PuTTY", true), null)) == 0){
			int waitingTime = Integer.parseInt(MainFrame.configuration.getWaitForInitTime());
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			count--;
		}
		if(count == 0){
			MessageDialog.openError(MainFrame.shell, "OPEN PUTTY ERROR", String.format("Failed cmd: %s %s",MainFrame.configuration.getPuttyExecutable(), args));
		}
		int oldStyle = OS.GetWindowLong(hwnd, OS.GWL_STYLE);
		OS.SetWindowLong(hwnd, OS.GWL_STYLE, oldStyle & ~OS.WS_BORDER);

		OS.SetParent(hwnd, composite.handle);
		OS.SendMessage(hwnd, OS.WM_SYSCOMMAND, OS.SC_MAXIMIZE, 0);

		if (hwnd != 0){
			tabItem.setImage(MImage.getGreenImage());
			tabItem.setText(tabDisplayName);
			tabItem.setData("hwnd", hwnd);
			tabItem.setData("session", session);
			//System.out.println("start process: "+hwnd);
			setWindowFocus(hwnd);
		} else {
			tabItem.dispose();
		}
	}

	public static void killProcess(int hwnd){
		OS.SendMessage(hwnd, OS.WM_CLOSE, null, 0);
		//System.out.println("kill process: "+hwnd);
	}

	public static void killPuttyWarningsAndErrs(){
		int hwndalert = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true));
		if (hwndalert != 0)
			killProcess(hwndalert);

		int hwndError = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Error", true));
		if (hwndError != 0)
			killProcess(hwndError);

		hwndError = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Fatal Error", true));
		if (hwndError != 0)
			killProcess(hwndError);
	}

	/**
	 * Execute an utility from left bar.
	 * @param program
	 * @param arg 
	 */
	public static void runProgram(Program program, String arg){
		String cmd;

		if (arg != null){
			cmd = program.getPath() + arg;
		} else {
			cmd = program.getPath();
		}

		// System.out.println("Command line: " + cmd); //DEBUG

		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException ex){
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Execute an utility from left bar.
	 * @param program
	 * @param arg 
	 */
	public static void runCMD(String program, String arg){
		String cmd;

		if (arg != null){
			cmd = program + " " +  arg;
		} else {
			cmd = program;
		}

		// System.out.println("Command line: " + cmd); //DEBUG

		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException ex){
			System.err.println(ex.getMessage());
		}
	}

  public static void invokeProxy(String host, String user, String password, String port) {
    String cmd = "cmd /c start " + MainFrame.configuration.getPlinkExecutable() + " -D " + port + " -pw " + password + " -N " + user + "@" + host;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

	/**
	 * Start Putty in a single window.
	 * @param session 
	 */
	public static void invokeSinglePutty(ConfigSession session){
		// Mount command-line Putty parameters:
		String args = setPuttyParameters(session);
		String cmd = "cmd /c start " + MainFrame.configuration.getPuttyExecutable() + args;

		// System.out.println("Command line: " + cmd); //DEBUG

		try {
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startProxy(String arg){

	}
}
