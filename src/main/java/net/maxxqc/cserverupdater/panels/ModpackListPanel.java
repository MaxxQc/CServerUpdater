package net.maxxqc.cserverupdater.panels;

import net.maxxqc.cserverupdater.Modpack;
import net.maxxqc.cserverupdater.ModpackLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.List;

public class ModpackListPanel extends JPanel
{
    private final int SCROLL_LOAD_THRESHOLD = 75;
    private final DefaultListModel<Modpack> LIST_MODPACKS = new DefaultListModel<>();
    private final JList<Modpack> ITEM_LIST = new JList<>(LIST_MODPACKS);
    private final JScrollPane scrollPane = new JScrollPane(ITEM_LIST);

    private int currentPage = 0;
    private boolean loadingMoreModpacks = false;

    public ModpackListPanel()
    {
        setLayout(new BorderLayout());

        ITEM_LIST.setCellRenderer(new ModpackListCellRenderer());

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new ScrollListener());
        scrollPane.addMouseWheelListener(new MouseAdapter()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                super.mouseWheelMoved(e);

                if (loadingMoreModpacks) {
                    return;
                }

                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                int extent = verticalScrollBar.getModel().getExtent();
                int maximum = verticalScrollBar.getMaximum();
                int value = verticalScrollBar.getValue();

                if ((value + extent) >= (maximum * SCROLL_LOAD_THRESHOLD / 100))
                {
                    loadNewPage();
                }
            }
        });

        JTextField searchBar = new JTextField();
        //TODO
        searchBar.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        add(searchBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadNewPage();
    }

    private void loadNewPage()
    {
        Cursor cursor = getCursor();
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loadingMoreModpacks = true;

        ModpackLoader modpackLoader = new ModpackLoader(currentPage++);
        modpackLoader.addPropertyChangeListener(evt ->
        {
            if (SwingWorker.StateValue.DONE == evt.getNewValue())
            {
                try
                {
                    List<Modpack> modpacks = modpackLoader.get();
                    for (Modpack modpack : modpacks) {
                        LIST_MODPACKS.addElement(modpack);
                    }

                    ITEM_LIST.revalidate();
                    ITEM_LIST.repaint();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    scrollPane.setCursor(cursor);
                    loadingMoreModpacks = false;
                }
            }
        });

        modpackLoader.execute();
    }

    private static class ModpackListCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Modpack) {
                Modpack modpack = (Modpack) value;
                setIcon(modpack.getImageIcon());
                setText("<html><b>" + modpack.getName() + "</b><br>Latest: " + modpack.getLatestVersion() + "</html>");
            }

            return this;
        }
    }

    private class ScrollListener implements AdjustmentListener
    {
        @Override
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
            if (!loadingMoreModpacks && e.getValueIsAdjusting() && isScrollBarAtBottom(e))
            {
                loadNewPage();
            }
        }

        private boolean isScrollBarAtBottom(AdjustmentEvent e)
        {
            JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
            int extent = scrollBar.getModel().getExtent();
            int maximum = scrollBar.getMaximum();
            int value = e.getValue() + extent;
            return value >= maximum * SCROLL_LOAD_THRESHOLD / 100;
        }
    }
}