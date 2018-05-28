package gui.transaction;


import controller.Controller;
import core.transaction.GenesisIssuePersonRecord;
import datachain.DCSet;
import lang.Lang;
import utils.MenuPopupUtil;

import javax.swing.*;

@SuppressWarnings("serial")
public class GenesisPersonalizeDetailsFrame extends RecGenesis_DetailsFrame {
    public GenesisPersonalizeDetailsFrame(GenesisIssuePersonRecord record) {
        super(record);

        //LABEL RECIPIENT
        ++labelGBC.gridy;
        JLabel recipientLabel = new JLabel(Lang.getInstance().translate("Recipient") + ":");
        this.add(recipientLabel, labelGBC);

        //RECIPIENT
        ++detailGBC.gridy;
        JTextField recipient = new JTextField(record.viewRecipient());
        recipient.setEditable(false);
        MenuPopupUtil.installContextMenu(recipient);
        this.add(recipient, detailGBC);

        //LABEL PERSON
        ++labelGBC.gridy;
        JLabel assetLabel = new JLabel(Lang.getInstance().translate("Person") + ":");
        this.add(assetLabel, labelGBC);

        //PERSON
        ++detailGBC.gridy;
        JTextField asset = new JTextField(String.valueOf(Controller.getInstance().getPerson(record.getItem().getKey(DCSet.getInstance())).toString()));
        asset.setEditable(false);
        MenuPopupUtil.installContextMenu(asset);
        this.add(asset, detailGBC);

        //PACK
        //	this.pack();
        //     this.setResizable(false);
        //     this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
