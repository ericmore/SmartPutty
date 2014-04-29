package Model;

import org.eclipse.swt.SWT;

public final class BorderData {
public int region = SWT.CENTER; // 默认为中间
	public BorderData() {
	}
	public BorderData(int region) {
	   this.region = region;
	}
}
