package Model;

/**
 * Protocols allowed by "Putty" or "KiTTY".
 * @author Carlos SS
 */
public enum Protocol {
	// Following order is also used on lists to be showed:
	SSH ("SSH", "-ssh -1"),
	SSH2 ("SSH2", "-ssh -2"),
	TELNET ("Telnet", "-telnet"),
	RLOGIN ("Rlogin", "-rlogin"),
	RAW ("Raw", "-raw"),
	SERIAL ("Serial", "-serial");

	// Visible name:
	private final String name;
	// Command-line parameter to be passed to "Putty" or "KiTTY":
	private final String parameter;

	// Constructor:
    private Protocol(String name, String parameter){
         this.name = name;
         this.parameter = parameter;
     }

	/**
	 * Get name to be showed in lists.
	 * @return 
	 */
	public String getName(){
		return name;
	}

	/**
	 * Get parameter.
	 * @return 
	 */
	public String getParameter(){
		return parameter;
	}
}
