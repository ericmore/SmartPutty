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
import UI.MImage;
import UI.MainFrame;

public class InvokeProgram extends Thread {

  public final static String APP_PUTTY = "app\\putty\\putty.exe ";
  public final static String APP_PLINK = "app\\putty\\plink.exe";
  public final static String APP_WINSCP = "app\\winscp\\winscp.exe ";
  public final static String APP_VNC = "app\\vnc\\vnc.exe ";
  public final static String APP_GENKEY = "app\\putty\\PUTTYGEN.EXE ";
  public final static String APP_NOTEPAD = "notepad.exe ";
  public final static String APP_CAPTURE = "app\\capture\\capture.exe";
  private Composite composite = null;
  private ConfigSession session = null;
  private CTabItem item = null;

  public InvokeProgram(Composite composite, CTabItem item, ConfigSession session) {
    this.composite = composite;
    this.item = item;
    this.session = session;
  }

  public void run() {
    MainFrame.display.syncExec(new Runnable() {
      public void run() {
        invokePutty(session);
      }

    });

  }

  public static void setWindowFocus(int hwnd) {
    //System.out.println("set window focus "+hwnd);
    //OS.SendMessage(hwnd, OS.WM_SETFOCUS, 0, 0);
    //OS.SetCapture(hwnd);
    OS.SetForegroundWindow(hwnd);
    OS.SetCursor(hwnd);
  }

  public static void setMainWinowFous() {
    int hwndMainWindow = isMainWindowRunning();
    setWindowFocus(hwndMainWindow);
  }

  public static int isMainWindowRunning() {
    return (int) OS.FindWindow(null, new TCHAR(0, ConstantValue.mainWindowTitle, true));
  }

  public void invokePutty(ConfigSession session) {

    String host = session.getHost();
    String user = session.getUser();
    String password = session.getPassword();
    String file = session.getKey();
    String protocol = session.getProtocol();
    String hostinfo = "";
    String args = "";

    hostinfo = user + "@" + host;
    if (!password.equals("")) {
      args = hostinfo + " -pw " + password + " -" + protocol + " -loghost " + hostinfo;
    }else{
      args = hostinfo + " -i " + file + " -" + protocol + " -loghost " + hostinfo;
    }
    System.out.println(args);
    int hHeap = (int) OS.GetProcessHeap();
    TCHAR buffer = new TCHAR(0, APP_PUTTY, true);
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
    if (result == false) {
      MessageDialog.openInformation(MainFrame.shell, "OPEN PUTTY ERROR", "can not open session: " + hostinfo);
      return;
    }
    int waitingTime = Integer.parseInt(Configuration.getInstance().getWaitForInitTime());
    try {
      Thread.sleep(waitingTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int hwndalert = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true));

    if (hwndalert != 0) {
      int waitingForOperation = 10000;
      while (waitingForOperation > 0) {
        try {
          if (OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true)) == 0) {
            break;
          }
          Thread.sleep(500);
          waitingForOperation -= 500;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    int hwnd = (int) OS.FindWindow(new TCHAR(0, "PuTTY", true), new TCHAR(0, hostinfo + " - PuTTY", true));
    int oldStyle = OS.GetWindowLong(hwnd, OS.GWL_STYLE);
    OS.SetWindowLong(hwnd, OS.GWL_STYLE, oldStyle & ~OS.WS_BORDER);

    OS.SetParent(hwnd, composite.handle);
    OS.SendMessage(hwnd, OS.WM_SYSCOMMAND, OS.SC_MAXIMIZE, 0);

    if (hwnd != 0) {
      item.setImage(MImage.getGreenImage());
      item.setText(user + "@" + host);
      item.setData("hwnd", hwnd);
      item.setData("session", session);
      //System.out.println("start process: "+hwnd);
      setWindowFocus(hwnd);
    } else {
      item.dispose();
    }
  }

  public static void killProcess(int hwnd) {
    OS.SendMessage(hwnd, OS.WM_CLOSE, null, 0);
    //System.out.println("kill process: "+hwnd);
  }

  public static void killPuttyWarningsAndErrs() {
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

  public static void invokeVNC(String arg) {
    String cmd = APP_VNC + arg;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeWinscp(String arg) {
    String cmd = APP_WINSCP + arg;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeProxy(String host, String user, String password, String port) {
    String cmd = "cmd /c start " + APP_PLINK + " -D " + port + " -pw " + password + " -N " + user + "@" + host;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeSinglePutty(ConfigSession session) {
    String user = session.getUser();
    String host = session.getHost();
    String password = session.getPassword();
    String key = session.getKey();
    String cmd = "";
    if(!password.equals("")){
      cmd = "cmd /c start " + APP_PUTTY + " -pw " + password + " " + user + "@" + host;
    }else{
      cmd = "cmd /c start " + APP_PUTTY + " -i " + key + " " + user + "@" + host;
    }
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeNotePad() {
    String cmd = "cmd /c start " + APP_NOTEPAD;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void invokeGenKey() {
    String cmd = "cmd /c start " + APP_GENKEY;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeCapture() {
    String cmd = "cmd /c start " + APP_CAPTURE;
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeCalculator() {
    String cmd = "cmd /c start calc";
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void invokeRemoteDesk() {
    String cmd = "cmd /c start mstsc";
    try {
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void startProxy(String arg) {

  }

}
