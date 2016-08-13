package Model;

/**
 * External programs used by "SmartPutty".
 * Some of them can replaceable by another (Putty/KiTTY, Plink/Klink,...), but another not.
 */
public enum Program {
	DEFAULT_APP_PUTTY("app\\putty\\putty.exe"),
	DEFAULT_APP_PLINK("app\\putty\\plink.exe"),
	DEFAULT_APP_KEYGEN("app\\putty\\puttygen.exe"),
	APP_WINSCP ("app\\winscp\\winscp.exe "),
	APP_VNC ("app\\vnc\\vnc.exe "),
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
