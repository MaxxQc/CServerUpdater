package net.maxxqc.cserverupdater;

import javax.swing.*;
import java.util.List;

public class ModpackLoader extends SwingWorker<List<Modpack>, Void>
{
    private final int page;

    public ModpackLoader(int page) {
        this.page = page;
    }

    @Override
    protected List<Modpack> doInBackground()
    {
        return CurseForgeAPI.getAllModpacks(page);
    }
}