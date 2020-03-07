package org.erachain.gui.items.statuses;

import org.erachain.core.item.ItemCls;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.core.item.statuses.StatusCls;
import org.erachain.gui.items.SearchItemSplitPanel;
import org.erachain.gui.items.assets.ExchangePanel;
import org.erachain.gui.library.MainPanelInterface;
import org.erachain.gui2.MainPanel;
import org.erachain.lang.Lang;
import org.erachain.settings.Settings;
import org.erachain.utils.URLViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchStatusesSplitPanel extends SearchItemSplitPanel implements MainPanelInterface {

    /**
     *
     */
    private String iconFile = "images/pageicons/SearchStatusesSplitPanel.png";
    private static final long serialVersionUID = 1L;
    private static StatusesItemsTableModel tableModelUnions = new StatusesItemsTableModel();

    public SearchStatusesSplitPanel() {
        super(tableModelUnions, "Search Statuses", "Search Statuses");

        JMenuItem setSeeInBlockexplorer = new JMenuItem(Lang.getInstance().translate("Check in Blockexplorer"));

        setSeeInBlockexplorer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    URLViewer.openWebpage(new URL("http://" + Settings.getInstance().getBlockexplorerURL()
                            + ":" + Settings.getInstance().getWebPort() + "/index/blockexplorer.html"
                            + "?status=" + itemTableSelected.getKey()));
                } catch (MalformedURLException e1) {
                    logger.error(e1.getMessage(), e1);
                }
            }
        });

        menuTable.add(setSeeInBlockexplorer);

    }

    //show details
    @Override
    protected Component getShow(ItemCls item) {
        StatusInfo info = new StatusInfo();
        info.show_001((StatusCls) item);
        return info;

    }
    @Override
    public Icon getIcon() {
        {
            try {
                return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconFile));
            } catch (Exception e) {
                return null;
            }
        }
    }

}
