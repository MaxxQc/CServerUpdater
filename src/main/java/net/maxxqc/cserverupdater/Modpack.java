package net.maxxqc.cserverupdater;

import javax.swing.*;
import java.util.Date;

public class Modpack
{
    private final int ID;
    private final String NAME;
    private final String SUMMARY;
    private final String THUMBNAIL_URL;
    private final String LATEST_VERSION;
    private final Date LATEST_VERSION_DATE;
    private final ImageIcon IMAGE_ICON;

    public Modpack(int id, String name, String summary, String thumbnailURL, String latestVersion, Date latestVersionDate)
    {
        this.ID = id;
        this.NAME = name;
        this.SUMMARY = summary;
        this.THUMBNAIL_URL = thumbnailURL;
        this.LATEST_VERSION = latestVersion;
        this.LATEST_VERSION_DATE = latestVersionDate;
        this.IMAGE_ICON = Helper.createImageIcon(thumbnailURL, 48, 48);
    }

    public int getId()
    {
        return ID;
    }

    public String getName()
    {
        return NAME;
    }

    public String getSummary()
    {
        return SUMMARY;
    }

    public String getThumbnailURL()
    {
        return THUMBNAIL_URL;
    }

    public String getLatestVersion()
    {
        return LATEST_VERSION;
    }

    public Date getLatestVersionDate()
    {
        return LATEST_VERSION_DATE;
    }

    public ImageIcon getImageIcon()
    {
        return IMAGE_ICON;
    }
}