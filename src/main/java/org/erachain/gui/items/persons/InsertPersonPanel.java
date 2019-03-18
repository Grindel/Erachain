package org.erachain.gui.items.persons;

import org.erachain.controller.Controller;
import org.erachain.core.account.Account;
import org.erachain.core.account.PrivateKeyAccount;
import org.erachain.core.crypto.Base58;
import org.erachain.core.item.persons.PersonFactory;
import org.erachain.core.item.persons.PersonHuman;
import org.erachain.core.transaction.IssuePersonRecord;
import org.erachain.core.transaction.Transaction;
import org.erachain.datachain.DCSet;
import org.erachain.gui.MainFrame;
import org.erachain.gui.PasswordPane;
import org.erachain.gui.library.Issue_Confirm_Dialog;
import org.erachain.gui.library.MButton;
import org.erachain.gui.transaction.IssuePersonDetailsFrame;
import org.erachain.gui.transaction.OnDealClick;
import org.erachain.lang.Lang;
import org.erachain.ntp.NTP;
import org.erachain.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimeZone;

public class InsertPersonPanel extends IssuePersonPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txt_Sign;
    protected JTextField txt_public_key;
    protected JTextField txt_info;
    protected JTextField txtGenderTxt;
    protected JTextField txtBirthdayTxt;
    protected JTextField txtDeathdayTxt;

    protected MButton trans_Button;
    protected javax.swing.JLabel label_Sign;
    protected javax.swing.JLabel label_public_key;
    protected javax.swing.JLabel label_info;
    protected javax.swing.JLabel iconLabel;
    protected javax.swing.JPanel jPanel_Paste;
    protected MButton pasteButton;

    // protected IssuePersonRecord issuePersonRecord;
    protected PersonHuman person;

    private InsertPersonPanel th;

    public InsertPersonPanel() {

        super();
        th = this;

        init();
        alive_CheckBox.setSelected(false);
        alive_CheckBox.setVisible(false);
        // jLabel_Dead.setVisible(false);
        // txtDeathdayTxt.setVisible(false);

        this.setMinimumSize(new Dimension(0, 0));
    }

    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
            }
        }
        return result;
    }

    private void init() {

        txt_Sign = new javax.swing.JTextField();
        txt_public_key = new javax.swing.JTextField();
        txt_info = new javax.swing.JTextField();
        txtGenderTxt = new javax.swing.JTextField();
        txtBirthdayTxt = new javax.swing.JTextField();
        txtDeathdayTxt = new javax.swing.JTextField();

        iconLabel = new javax.swing.JLabel();
        label_Sign = new javax.swing.JLabel();
        label_public_key = new javax.swing.JLabel();
        label_info = new javax.swing.JLabel();

        // txtBirthday = new javax.swing.JTextField();
        // txtDeathday = new javax.swing.JTextField();

        txtSNILS.setText("");
        this.txtBirthLatitude.setText("");
        this.txtBirthLongitude.setText("");
        this.txtHeight.setText("");
        this.txtFeePow.setSelectedItem("0");

        // cbxFrom.setEnabled(false);
        // txtFeePow.setEditable(false);
        txtName.setEditable(false);
        txtareaDescription.setEditable(false);
        txtBirthday.setVisible(false); // setEnabled(false);
        txtDeathday.setVisible(false); // setEnabled(false);
        txtBirthdayTxt.setEditable(false);
        txtDeathdayTxt.setVisible(false);
        txtDeathdayTxt.setEditable(false);

        copyButton.setVisible(false);

        add_Image_Panel.setVisible(false);
        txtGender.setVisible(false);
        txtGenderTxt.setEditable(false);
        txtSNILS.setEditable(false);
        txtBirthLatitude.setEditable(false);
        txtBirthLongitude.setEditable(false);
        txtSkinColor.setEditable(false);
        txtEyeColor.setEditable(false);
        txtHairСolor.setEditable(false);
        txtHeight.setEditable(false);
        issueButton.setVisible(false);

        txt_public_key.setEditable(false);

        label_Sign.setText(Lang.getInstance().translate("Signature") + ":");
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
        mainPanel.add(label_Sign, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        txt_Sign.setEditable(false);
        mainPanel.add(txt_Sign, gridBagConstraints);

        label_public_key.setText(Lang.getInstance().translate("Public key") + ":");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
        mainPanel.add(label_public_key, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        mainPanel.add(txt_public_key, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainPanel.add(txtGenderTxt, gridBagConstraints);

        // txtBirthday.setFormatterFactory(new
        // javax.swing.text.DefaultFormatterFactory(new
        // javax.swing.text.DateFormatter(new
        // java.text.SimpleDateFormat("yyyy-MM-dd"))));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        mainPanel.add(txtBirthdayTxt, gridBagConstraints);

        // txtDeathday.setFormatterFactory(new
        // javax.swing.text.DefaultFormatterFactory(new
        // javax.swing.text.DateFormatter(new
        // java.text.SimpleDateFormat("yyyy-MM-dd"))));
        // txtDeathday.addActionListener(new java.awt.event.ActionListener() {
        // public void actionPerformed(java.awt.event.ActionEvent evt) {
        // txtDeathdayActionPerformed(evt);
        // }
        // });

        // txtDeathday.setDateFormatString("yyyy-MM-dd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
        mainPanel.add(txtDeathdayTxt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.05;
        jPanel1.add(iconLabel, gridBagConstraints);

        pasteButton = new MButton(Lang.getInstance().translate("Paste Person from clipboard"), 2);
        pasteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                issueButton.setEnabled(false);
                copyButton.setEnabled(false);

                // TODO Auto-generated method stub
                person = null;
                reset();

                String base58str = getClipboardContents();
                byte[] dataPerson;
                try {
                    dataPerson = Base58.decode(base58str);
                    person = (PersonHuman) PersonFactory.getInstance().parse(dataPerson, false);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, ee.getMessage(), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);

                    issueButton.setEnabled(true);
                    copyButton.setEnabled(true);
                    return;
                }

                txtName.setText(person.viewName());
                // jLabel2.setText("jLabel2");
                ImageIcon image = new ImageIcon(person.getImage());
                // if (image.le)
                int x = image.getIconWidth();
                int y = image.getIconHeight();

                int x1 = 250;
                double k = ((double) x / (double) x1);
                y = (int) ((double) y / k);

                if (y != 0) {
                    Image Im = image.getImage().getScaledInstance(x1, y, 1);

                    iconLabel.setIcon(new ImageIcon(Im));

                }

                // SET ONE TIME ZONE for Birthday
                TimeZone tz = TimeZone.getDefault();
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

                /// txtBirthdayTxt.setText(new Date(person.getBirthday())+ "");
                txtBirthdayTxt.setText(person.getBirthdayStr());
                txtDeathdayTxt.setText(person.getDeathdayStr());
                // if ( dayTimestamp/10 > person.getBirthday()/10) {
                txtDeathdayTxt.setVisible(false);
                jLabel_Dead.setVisible(false);
                if (!person.isAlive(0l)) {
                    // txtDeathdayTxt.setText(new
                    // Date(person.getDeathday())+"");
                    txtDeathdayTxt.setVisible(true);
                    jLabel_Dead.setVisible(true);
                }
                TimeZone.setDefault(tz);

                txtareaDescription.setText(person.getDescription() == null ? "" : person.getDescription());

                txtGender.setSelectedIndex(person.getGender());
                txtGenderTxt.setText(txtGender.getSelectedItem().toString());

                if (person.getRace() != null)
                    txtSNILS.setText(person.getRace());
                txtBirthLatitude.setText("" + person.getBirthLatitude() + ", " + person.getBirthLongitude());
                // txtBirthLongitude.setText("" + person.getBirthLongitude());
                if (person.getSkinColor() != null)
                    txtSkinColor.setText(person.getSkinColor());
                if (person.getEyeColor() != null)
                    txtEyeColor.setText(person.getEyeColor());
                if (person.getHairColor() != null)
                    txtHairСolor.setText(person.getHairColor());
                txtHeight.setText("" + person.getHeight());

                txt_Sign.setText(
                        person.isSignatureValid(DCSet.getInstance()) ? Base58.encode(person.getOwnerSignature())
                                : Lang.getInstance().translate("Wrong signaryte for data owner"));
                txt_public_key.setText(Base58.encode(person.getOwner().getPublicKey()));

            }

        });

        GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 4;
        gridBagConstraints1.gridy = 19;
        // gridBagConstraints1.gridwidth = ;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints1.insets = new java.awt.Insets(20, 0, 0, 0);
        mainPanel.add(pasteButton, gridBagConstraints1);

        trans_Button = new MButton(Lang.getInstance().translate("Check person and insert"), 2);

        trans_Button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                issueButton.setEnabled(false);
                copyButton.setEnabled(false);

                if (person == null)
                    return;

                // CHECK IF WALLET UNLOCKED
                if (!Controller.getInstance().isWalletUnlocked()) {
                    // ASK FOR PASSWORD
                    String password = PasswordPane.showUnlockWalletDialog(th);
                    if (!Controller.getInstance().unlockWallet(password)) {
                        // WRONG PASSWORD
                        JOptionPane.showMessageDialog(null, Lang.getInstance().translate("Invalid password"),
                                Lang.getInstance().translate("Unlock Wallet"), JOptionPane.ERROR_MESSAGE);

                        // ENABLE
                        issueButton.setEnabled(true);
                        copyButton.setEnabled(true);

                        return;
                    }
                }

                // READ CREATOR
                Account creatorAccount = (Account) cbxFrom.getSelectedItem();

                int parse = 0;
                int feePow = 0;

                try {

                    // READ FEE POW
                    feePow = Integer.parseInt((String)txtFeePow.getSelectedItem());
                    // String b = txtFeePow.getText();

                } catch (Exception e) {
                    String mess = "Invalid pars... " + parse;
                    switch (parse) {
                        case 0:
                            mess = "Invalid fee power 0..6";
                            break;
                    }
                    JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate(mess),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);

                    issueButton.setEnabled(true);
                    copyButton.setEnabled(true);
                    return;
                }

                // TODO Auto-generated method stub
                PrivateKeyAccount creator = Controller.getInstance()
                        .getPrivateKeyAccountByAddress(creatorAccount.getAddress());
                Pair<Transaction, Integer> result = Controller.getInstance().issuePersonHuman(creator, feePow, person);

                // CHECK VALIDATE MESSAGE
                if (result.getB() == Transaction.VALIDATE_OK) {
                    String Status_text = "";

                    // System.out.print("\n"+ text +"\n");
                    // UIManager.put("OptionPane.cancelButtonText", "Отмена");
                    // UIManager.put("OptionPane.okButtonText", "Готово");

                    // int s =
                    // JOptionPane.showConfirmDialog(MainFrame.getInstance(),
                    // text, Lang.getInstance().translate("Issue Asset"),
                    // JOptionPane.YES_NO_OPTION);

                    Issue_Confirm_Dialog dd = new Issue_Confirm_Dialog(MainFrame.getInstance(), true, result.getA(),
                            " ",
                            (int) (th.getWidth() / 1.2), (int) (th.getHeight() / 1.2), Status_text,
                            Lang.getInstance().translate("Confirmation transaction issue person"));

                    IssuePersonDetailsFrame ww = new IssuePersonDetailsFrame((IssuePersonRecord) result.getA());
                    // ww.jPanel2.setVisible(false);
                    dd.jScrollPane1.setViewportView(ww);
                    dd.setLocationRelativeTo(th);
                    dd.setVisible(true);
                    // JOptionPane.OK_OPTION
                    if (dd.isConfirm) { // s!= JOptionPane.OK_OPTION) {
                        // VALIDATE AND PROCESS
                        Integer result1 = Controller.getInstance().getTransactionCreator().afterCreate(result.getA(),
                                Transaction.FOR_NETWORK);
                        if (result1 != Transaction.VALIDATE_OK) {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    Lang.getInstance().translate(OnDealClick.resultMess(result1)),
                                    Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    Lang.getInstance().translate("Person issue has been sent!"),
                                    Lang.getInstance().translate("Success"), JOptionPane.INFORMATION_MESSAGE);
                            person = null;
                            erase_Fields();

                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(),
                            Lang.getInstance().translate(OnDealClick.resultMess(result.getB())),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                }

                // ENABLE
                issueButton.setEnabled(true);
                copyButton.setEnabled(true);

            }

        });

        gridBagConstraints1.gridx = 6;
        gridBagConstraints1.gridy = 19;
        // gridBagConstraints1.gridwidth = 15;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints1.insets = new java.awt.Insets(20, 0, 0, 16);
        mainPanel.add(trans_Button, gridBagConstraints1);

    }

    protected void erase_Fields() {
        txtFeePow.setSelectedItem("0");
        txtName.setText("");
        txtareaDescription.setText("");
        txtGenderTxt.setText("");
        txtSNILS.setText("");
        txtBirthLatitude.setText("");
        txtBirthLongitude.setText("");
        txtSkinColor.setText("");
        txtEyeColor.setText("");
        txtHairСolor.setText("");
        txtHeight.setText("");
        //imgButes = null;
        add_Image_Panel.reset();
        txt_Sign.setText("");
        txt_public_key.setText("");
        txtBirthdayTxt.setText("");
        txtDeathdayTxt.setText("");
        txtGenderTxt.setText("");
        //imgButes = null;
        iconLabel.setIcon(null);

    }

}
