package net.maxxqc.cserverupdater;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Helper
{
    public static ImageIcon createImageIcon(String iconURL, int width, int height)
    {
        try
        {
            Image image = ImageIO.read(new URL(iconURL));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        catch (IOException ignored) {}

        return null;
    }
}