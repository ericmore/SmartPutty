package com.sp.Model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * Lays out a composite, arranging and resizing its components to fit in five
 * regions: north, south, east, west, and center.
 */
public class BorderLayout extends Layout {
	// Region constants:
	private Control north;
	private Control south;
	private Control east;
	private Control west;
	private Control center;

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache){
		getControls(composite);
		// 
		int width = 0, height = 0;
		// 
		width += west == null ? 0 : getSize(west, flushCache).x;
		width += east == null ? 0 : getSize(east, flushCache).x;
		width += center == null ? 0 : getSize(center, flushCache).x;
		// 
		if (north != null){
			Point pt = getSize(north, flushCache);
			width = Math.max(width, pt.x);
		}
		if (south != null){
			Point pt = getSize(south, flushCache);
			width = Math.max(width, pt.x);
		}
		// 
		height += north == null ? 0 : getSize(north, flushCache).y;
		height += south == null ? 0 : getSize(south, flushCache).y;
		int heightOther = center == null ? 0 : getSize(center, flushCache).y;
		if (west != null){
			Point pt = getSize(west, flushCache);
			heightOther = Math.max(heightOther, pt.y);
		}
		if (east != null){
			Point pt = getSize(east, flushCache);
			heightOther = Math.max(heightOther, pt.y);
		}
		height += heightOther;
		// 
		return new Point(Math.max(width, wHint), Math.max(height, hHint));
	}

	@Override
	protected void layout(Composite composite, boolean flushCache){
		getControls(composite);
		// 
		Rectangle rect = composite.getClientArea();
		int left = rect.x, right = rect.width, top = rect.y, bottom = rect.height;
		// 
		if (north != null){
			if (!north.isVisible()){//
				// Reset bounds://
				north.setBounds(left, right, 0, 0);
			} else {//
				Point pt = getSize(north, flushCache);
				north.setBounds(left, top, rect.width, pt.y);
				top += pt.y;
			}
		}
		if (south != null){
			if (!south.isVisible()){//
				// Reset bounds://
				south.setBounds(left, right, 0, 0);
			} else {//
				Point pt = getSize(south, flushCache);
				south.setBounds(left, rect.height - pt.y, rect.width, pt.y);
				bottom -= pt.y;
			}//
		}
		if (east != null){
			if (!east.isVisible()){//
				// Reset bounds://
				east.setBounds(0, top, 0, (bottom - top));//
			} else {//
				Point pt = getSize(east, flushCache);
				east.setBounds(rect.width - pt.x, top, pt.x, (bottom - top));
				right -= pt.x;
			}//
		}
		if (west != null){
			if (!west.isVisible()){//
				// Reset bounds://
				west.setBounds(left, top, 0, (bottom - top));//
			} else {//
				Point pt = getSize(west, flushCache);
				west.setBounds(left, top, pt.x, (bottom - top));
				left += pt.x;
			}//
		}
		if (center != null){
			center.setBounds(left, top, (right - left), (bottom - top));
		}
	}

	protected Point getSize(Control control, boolean flushCache){
		return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
	}

	protected void getControls(Composite composite){
		// 
		Control[] children = composite.getChildren();
		// 
		for (int i = 0; i < children.length; i++){
			Control child = children[i];
			BorderData borderData = (BorderData) child.getLayoutData();
			if (borderData.region == SWT.TOP){
				north = child;
			} else if (borderData.region == SWT.BOTTOM){
				south = child;
			} else if (borderData.region == SWT.RIGHT){
				east = child;
			} else if (borderData.region == SWT.LEFT){
				west = child;
			} else {
				center = child;
			}
		}
	}
}
