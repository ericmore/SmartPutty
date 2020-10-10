package com.sp.ui;

import org.eclipse.swt.graphics.Image;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class MImage {
    public static Image mainImage;
    public static Image newImage;
    public static Image openImage;
    public static Image saveImage;
    public static Image RemoteDeskImage;
    public static Image captureImage;
    public static Image reloadImage;
    public static Image helpImage;
    public static Image calculator;
    public static Image notepad;
    public static Image key;
    public static Image windows;
    public static Image linux;
    public static Image folder;

    public static Image cloneImage;
    public static Image transferImage;
    public static Image vncImage;
    public static Image addImage;
    public static Image editImage;
    public static Image deleteImage;
    public static Image connectImage;
    public static Image puttyImage;
    public static Image dictImage;
    public static Image configImage;
    static {
        try {

            mainImage = new Image(MainFrame.display, new ClassPathResource("icon/main.png").getInputStream());
            newImage = new Image(MainFrame.display, new ClassPathResource("icon/new.png").getInputStream());
            openImage = new Image(MainFrame.display, new ClassPathResource("icon/open.png").getInputStream());
            saveImage = new Image(MainFrame.display, new ClassPathResource("icon/save.png").getInputStream());
            RemoteDeskImage = new Image(MainFrame.display, new ClassPathResource("icon/remotedesk.png").getInputStream());
            captureImage = new Image(MainFrame.display, new ClassPathResource("icon/capture.png").getInputStream());
            reloadImage = new Image(MainFrame.display, new ClassPathResource("icon/reload_session.png").getInputStream());
            helpImage = new Image(MainFrame.display, new ClassPathResource("icon/help.png").getInputStream());
            calculator = new Image(MainFrame.display, new ClassPathResource("icon/calculator.png").getInputStream());
            notepad = new Image(MainFrame.display, new ClassPathResource("icon/notepad.png").getInputStream());
            key = new Image(MainFrame.display, new ClassPathResource("icon/key.png").getInputStream());
            windows = new Image(MainFrame.display, new ClassPathResource("icon/windows.png").getInputStream());
            linux = new Image(MainFrame.display, new ClassPathResource("icon/linux.png").getInputStream());
            folder = new Image(MainFrame.display, new ClassPathResource("icon/folder.png").getInputStream());
            cloneImage = new Image(MainFrame.display, new ClassPathResource("icon/clone.png").getInputStream());
            transferImage = new Image(MainFrame.display, new ClassPathResource("icon/transfer.png").getInputStream());
            vncImage = new Image(MainFrame.display, new ClassPathResource("icon/vnc.png").getInputStream());
            addImage = new Image(MainFrame.display, new ClassPathResource("icon/add.png").getInputStream());
            editImage = new Image(MainFrame.display, new ClassPathResource("icon/edit.png").getInputStream());
            deleteImage = new Image(MainFrame.display, new ClassPathResource("icon/delete.png").getInputStream());
            connectImage = new Image(MainFrame.display, new ClassPathResource("icon/connect_ssh.png").getInputStream());
            puttyImage = new Image(MainFrame.display, new ClassPathResource("icon/putty.png").getInputStream());
            dictImage = new Image(MainFrame.display, new ClassPathResource("icon/dict.png").getInputStream());
            configImage = new Image(MainFrame.display, new ClassPathResource("icon/configuration.png").getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MImage() {

    }
}
