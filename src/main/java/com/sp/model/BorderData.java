package com.sp.model;

import org.eclipse.swt.SWT;

/**
 * Indicates the region that a control belongs to.
 */
public final class BorderData {
	public int region = SWT.CENTER;

	public BorderData() {
	}

	public BorderData(int region) {
	   this.region = region;
	}
}
