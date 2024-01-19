package net.maxxqc.cserverupdater;

import javax.swing.*;

public class ServerUpdater
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            ServerUpdaterApp app = new ServerUpdaterApp();
            app.setVisible(true);
            app.toFront();
        });
    }
}