package com.sp.ui;

import java.awt.*;

/**
 * Created by eric on 2017/4/16.
 */
public class Splash  {
    static void renderSplashFrame(Graphics2D g, String msg) {
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+msg+"...", 120, 150);
    }

}
