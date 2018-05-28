package gui.transaction;

import core.item.templates.TemplateCls;
import core.transaction.GenesisIssueTemplateRecord;
import lang.Lang;
import utils.MenuPopupUtil;

import javax.swing.*;

@SuppressWarnings("serial")
public class GenesisIssueTemplateDetailsFrame extends RecGenesis_DetailsFrame {
    public GenesisIssueTemplateDetailsFrame(GenesisIssueTemplateRecord templateIssue) {
        super(templateIssue);

        TemplateCls template = (TemplateCls) templateIssue.getItem();

        //LABEL NAME
        ++labelGBC.gridy;
        JLabel nameLabel = new JLabel(Lang.getInstance().translate("Name") + ":");
        this.add(nameLabel, labelGBC);

        //NAME
        ++detailGBC.gridy;
        JTextField name = new JTextField(template.viewName());
        name.setEditable(false);
        MenuPopupUtil.installContextMenu(name);
        this.add(name, detailGBC);

        //LABEL DESCRIPTION
        ++labelGBC.gridy;
        JLabel descriptionLabel = new JLabel(Lang.getInstance().translate("Description") + ":");
        this.add(descriptionLabel, labelGBC);

        //DESCRIPTION
        ++detailGBC.gridy;
        JTextArea txtAreaDescription = new JTextArea(template.getDescription());
        txtAreaDescription.setRows(4);
        txtAreaDescription.setBorder(name.getBorder());
        txtAreaDescription.setEditable(false);
        MenuPopupUtil.installContextMenu(txtAreaDescription);
        this.add(txtAreaDescription, detailGBC);

        //PACK
        //	this.pack();
        //    this.setResizable(false);
        //    this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
