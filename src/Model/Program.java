package Model;

/**
 * Programs used by "SmartPutty" in "Utilities" bar.
 */
public enum Program {
	APP_PUTTY ("app\\putty\\putty.exe"),
	APP_PLINK ("app\\putty\\plink.exe"),
	APP_WINSCP ("app\\winscp\\winscp.exe "),
	APP_VNC ("app\\vnc\\vnc.exe "),
	APP_GENKEY ("app\\putty\\PUTTYGEN.EXE"),
	APP_NOTEPAD ("notepad.exe"),
	APP_CAPTURE ("app\\capture\\capture.exe"),
	APP_CALCULATOR ("calc.exe"),
	APP_REMOTE_DESK ("mstsc.exe");

	// Program path:
	private final String path;

	// Constructor:
    private Program(String path){
		this.path = path;
	}

	/**
	 * Get path to execute.
	 * @return 
	 */
	public String getPath(){
		return path;
	}
}
