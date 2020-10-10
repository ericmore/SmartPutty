package com.sp.control;

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
     * 
     */
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        
        /*  */
        Display.getDefault().syncExec(new Thread(){
            public void run(){
                /*  */
                text.append(message +"\n");
            }
        });
    }
    
}