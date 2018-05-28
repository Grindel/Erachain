package gui.items.other;

import gui.ConsolePanel;
import lang.Lang;
import settings.Settings;

import javax.swing.*;

@SuppressWarnings("serial")
public class Other_Console_Panel extends JPanel {

    private ConsolePanel debugTabPane;

    public Other_Console_Panel() {
        //CREATE FRAME
        //	setTitle(Lang.getInstance().translate("Erachain.org") + " - " + Lang.getInstance().translate("Debug"));
        //	setModal(true);


        //DEBUG TABPANE

        //ADD TABS
        if (Settings.getInstance().isGuiConsoleEnabled()) {
            this.debugTabPane = new ConsolePanel();


            java.awt.GridBagConstraints gridBagConstraints;
            setLayout(new java.awt.GridBagLayout());

            JLabel jLabel1 = new javax.swing.JLabel();
            jLabel1.setText(Lang.getInstance().translate("Console"));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
            add(jLabel1, gridBagConstraints);


            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 0.1;
            gridBagConstraints.weighty = 0.1;
            gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
            add(debugTabPane, gridBagConstraints);

        }

        this.setVisible(true);

    }
}
