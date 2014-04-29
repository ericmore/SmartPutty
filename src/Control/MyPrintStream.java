package Control;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class MyPrintStream extends PrintStream {
 
     private Text text;
    
    public MyPrintStream(OutputStream out, Text text) {
        super(out);
        this.text = text;
    }

    /**
     * 在这里重截,所有的打印方法都要调用的方法
     */
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        
        /* SWT非界面线程访问组件的方式 */
        Display.getDefault().syncExec(new Thread(){
            public void run(){
                /* 在这里把信息添加到组件中 */
                text.append(message +"\n");
            }
        });
    }
    
}