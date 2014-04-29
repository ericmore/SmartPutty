package Model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
public class BorderLayout extends Layout {
// 定义存放在不同位置的5个控件
private Control north;
private Control south;
private Control east;
private Control west;
private Control center;

protected Point computeSize(Composite composite, int wHint, int hHint,
    boolean flushCache) {
   getControls(composite);
   // 定义面板的宽和高
   int width = 0, height = 0;
   // 计算面板的宽度
   width += west == null ? 0 : getSize(west, flushCache).x;
   width += east == null ? 0 : getSize(east, flushCache).x;
   width += center == null ? 0 : getSize(center, flushCache).x;
   // 如果上部和下部都有控件，则宽取较大值
   if (north != null) {
    Point pt = getSize(north, flushCache);
    width = Math.max(width, pt.x);
   }
   if (south != null) {
    Point pt = getSize(south, flushCache);
    width = Math.max(width, pt.x);
   }
   // 计算面板的高度
   height += north == null ? 0 : getSize(north, flushCache).y;
   height += south == null ? 0 : getSize(south, flushCache).y;
   int heightOther = center == null ? 0 : getSize(center, flushCache).y;
   if (west != null) {
    Point pt = getSize(west, flushCache);
    heightOther = Math.max(heightOther, pt.y);
   }
   if (east != null) {
    Point pt = getSize(east, flushCache);
    heightOther = Math.max(heightOther, pt.y);
   }
   height += heightOther;
   // 计算的宽和高与默认的宽和高作比较，返回之中较大的
   return new Point(Math.max(width, wHint), Math.max(height, hHint));
}

protected void layout(Composite composite, boolean flushCache) {
   getControls(composite);
   // 获得当前面板可显示的区域
   Rectangle rect = composite.getClientArea();
   int left = rect.x, right = rect.width, top = rect.y, bottom = rect.height;
   // 将各个控件放置到面板中
   if (north != null) {
    Point pt = getSize(north, flushCache);
    north.setBounds(left, top, rect.width, pt.y);
    top += pt.y;
   }
   if (south != null) {
    Point pt = getSize(south, flushCache);
    south.setBounds(left, rect.height - pt.y, rect.width, pt.y);
    bottom -= pt.y;
   }
   if (east != null) {
    Point pt = getSize(east, flushCache);
    east.setBounds(rect.width - pt.x, top, pt.x, (bottom - top));
    right -= pt.x;
   }
   if (west != null) {
    Point pt = getSize(west, flushCache);
    west.setBounds(left, top, pt.x, (bottom - top));
    left += pt.x;
   }
   if (center != null) {
    center.setBounds(left, top, (right - left), (bottom - top));
   }
}
// 计算某一控件当前的大小，长和宽
protected Point getSize(Control control, boolean flushCache) {
   return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
}
// 设置该类中每个位置控件的属性的方法
protected void getControls(Composite composite) {
   // 获得当前面板中所有的控件对象
   Control[] children = composite.getChildren();
   // 循环所有控件，并将每个控件所放的位置对号入座
   for (int i = 0; i < children.length; i++) {
    Control child = children[i];
    BorderData borderData = (BorderData) child.getLayoutData();
    if (borderData.region == SWT.TOP) {
     north = child;
    } else if (borderData.region == SWT.BOTTOM) {
     south = child;
    } else if (borderData.region == SWT.RIGHT) {
     east = child;
    } else if (borderData.region == SWT.LEFT) {
     west = child;
    } else {
     center = child;
    }
   }
}
}

