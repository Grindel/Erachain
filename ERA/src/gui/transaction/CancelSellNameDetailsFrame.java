package gui.transaction;

import core.transaction.CancelSellNameTransaction;
import lang.Lang;
import utils.MenuPopupUtil;

import javax.swing.*;

@SuppressWarnings("serial")
public class CancelSellNameDetailsFrame extends Rec_DetailsFrame {
    public CancelSellNameDetailsFrame(CancelSellNameTransaction cancelNameSale) {
        super(cancelNameSale);

        //LABEL NAME
        ++labelGBC.gridy;
        JLabel nameLabel = new JLabel(Lang.getInstance().translate("Name") + ":");
        this.add(nameLabel, labelGBC);

        //NAME
        ++detailGBC.gridy;
        JTextField name = new JTextField(cancelNameSale.getName());
        name.setEditable(false);
        MenuPopupUtil.installContextMenu(name);
        this.add(name, detailGBC);

        //PACK
        //	this.pack();
        //    this.setResizable(false);
        //    this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
