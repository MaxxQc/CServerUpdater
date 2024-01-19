package net.maxxqc.cserverupdater;

import net.maxxqc.cserverupdater.panels.ModpackListPanel;

import javax.swing.*;
import java.awt.*;

public class ServerUpdaterApp extends JFrame
{
    public ServerUpdaterApp()
    {
        setTitle("Cursed Server Updater");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //TODO setIconImage();

        // TODO add credits / report bugs / etc toolbar

        ModpackListPanel modpackListPanel = new ModpackListPanel();
        JPanel rightPanel = new JPanel();

        rightPanel.add(new JLabel(Helper.createImageIcon("https://media.forgecdn.net/avatars/902/338/638350403793040080.png", 32, 32)));

        setLayout(new BorderLayout());

        modpackListPanel.setPreferredSize(new Dimension(300, getHeight()));
        rightPanel.setPreferredSize(new Dimension(getWidth() - 300, getHeight()));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, modpackListPanel, rightPanel);

        splitPane.setDividerLocation((int) (getWidth() * 0.4));
        splitPane.setEnabled(false);

        add(splitPane, BorderLayout.CENTER);
    }
}