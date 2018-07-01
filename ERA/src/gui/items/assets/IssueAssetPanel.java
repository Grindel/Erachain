package gui.items.assets;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import controller.Controller;
import core.account.Account;
import core.account.PrivateKeyAccount;
import core.item.ItemCls;
import core.item.assets.AssetCls;
import core.item.assets.AssetType;
import core.transaction.IssueAssetTransaction;
import core.transaction.Transaction;
import gui.MainFrame;
import gui.PasswordPane;
import gui.library.Issue_Confirm_Dialog;
import gui.library.M_DecimalFormatedTextField;
import gui.library.My_Add_Image_Panel;
import gui.library.library;
import gui.models.AccountsComboBoxModel;
import lang.Lang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Саша
 */
public class IssueAssetPanel extends javax.swing.JPanel {
    
    protected byte[] imgButes;
    protected byte[] iconButes;
    private IssueAssetPanel th;
    // Variables declaration - do not modify
    private javax.swing.JLabel Title_jLabel;
    private javax.swing.JLabel account_jLabel;
    private javax.swing.JTextArea txtareaDescription;
    private javax.swing.JLabel description_jLabel;
    private javax.swing.JLabel fee_jLabel;
    private javax.swing.JTextField txtFeePow;
    private javax.swing.JButton issue_jButton;
    private JComboBox<Account> cbxFrom;
    private JComboBox<AssetType> cbxAssetType;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel name_jLabel;
    private javax.swing.JTextField txtName;
    private javax.swing.JLabel quantity_jLabel;
    private M_DecimalFormatedTextField txtQuantity;
    private javax.swing.JLabel scale_jLabel;
    
    private My_Add_Image_Panel add_Image_Panel;
    private My_Add_Image_Panel add_Logo_Icon_Panel;
    private AssetTypesComboBoxModel assetTypeModet;
    private JLabel type_jLabel;
    private JTextArea textaresAssetTypeDescription;
    private JComboBox<String> txtScale;
    
    /**
     * Creates new form Issue_Asset_Panel_01
     */
    public IssueAssetPanel() {
        th = this;
        initComponents();
        
        account_jLabel.setText(Lang.getInstance().translate("Account") + ":");
        
        name_jLabel.setText(Lang.getInstance().translate("Name") + ":");
        txtName.setText("");
        Title_jLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Title_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Title_jLabel.setText(Lang.getInstance().translate("Issue Asset"));
        Title_jLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        description_jLabel.setText(Lang.getInstance().translate("Description") + ":");
        type_jLabel.setText(Lang.getInstance().translate("Type") + ":");
        txtareaDescription.setLineWrap(true);
        txtareaDescription.setText("");
        quantity_jLabel.setText(Lang.getInstance().translate("Quantity") + ":");
        
        txtQuantity.setMaskType(txtQuantity.maskLong);
        txtQuantity.setText("1");
        scale_jLabel.setText(Lang.getInstance().translate("Scale") + ":");
        fee_jLabel.setText(Lang.getInstance().translate("Fee Power") + ":");
        txtFeePow.setText("0");
        issue_jButton.setText(Lang.getInstance().translate("Issue"));
        issue_jButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                onIssueClick();
            }
        });
        // select combobox Asset type
        cbxAssetType.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
                JComboBox ss = (JComboBox) e.getSource();
                AssetType aa = (AssetType) ss.getSelectedItem();
                textaresAssetTypeDescription.setText(aa.getDescription());
            }
        });
        
        // set start text area asset type
        textaresAssetTypeDescription.setText(((AssetType) assetTypeModet.getSelectedItem()).getDescription());
        add_Logo_Icon_Panel.setPreferredSize(new Dimension(250, 50));
        
       
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        account_jLabel = new javax.swing.JLabel();
        cbxFrom = new JComboBox<Account>(new AccountsComboBoxModel());
        cbxAssetType = new JComboBox();
        name_jLabel = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtareaDescription = new javax.swing.JTextArea();
        Title_jLabel = new javax.swing.JLabel();
        description_jLabel = new javax.swing.JLabel();
        type_jLabel = new javax.swing.JLabel();
        quantity_jLabel = new javax.swing.JLabel();
        txtQuantity = new M_DecimalFormatedTextField();
        scale_jLabel = new javax.swing.JLabel();
        txtScale = new JComboBox<String>();
        fee_jLabel = new javax.swing.JLabel();
        txtFeePow = new javax.swing.JTextField();
        issue_jButton = new javax.swing.JButton();
        // size from widht
        add_Image_Panel = new My_Add_Image_Panel(
                Lang.getInstance().translate("Add Image") + (" (max %1%kB)").replace("%1%", "1024"), 250, 250);
        // size from height
        add_Logo_Icon_Panel = new My_Add_Image_Panel(Lang.getInstance().translate("Add Logo"), 50, 50);
        setLayout(new java.awt.GridBagLayout());
        account_jLabel.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 7);
        add(account_jLabel, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(cbxFrom, gridBagConstraints);
        
        name_jLabel.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 7);
        add(name_jLabel, gridBagConstraints);
        
        txtName.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        add(txtName, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(add_Logo_Icon_Panel, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 8, 8);
        
        add_Image_Panel.setPreferredSize(new Dimension(250, 350));
        add(add_Image_Panel, gridBagConstraints);
        
        assetTypeModet = new AssetTypesComboBoxModel();
        cbxAssetType.setModel(assetTypeModet);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(cbxAssetType, gridBagConstraints);
        // description asset type
        textaresAssetTypeDescription = new JTextArea();
        textaresAssetTypeDescription.setLineWrap(true);
        textaresAssetTypeDescription.setWrapStyleWord(true);
        textaresAssetTypeDescription.setEditable(false);
        textaresAssetTypeDescription.setEnabled(false);
        textaresAssetTypeDescription.setColumns(20);
        textaresAssetTypeDescription.setRows(5);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.setViewportView(textaresAssetTypeDescription);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(jScrollPane2, gridBagConstraints);
        
        txtareaDescription.setColumns(20);
        txtareaDescription.setRows(5);
        jScrollPane1.setViewportView(txtareaDescription);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(jScrollPane1, gridBagConstraints);
        
        Title_jLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 6, 9);
        add(Title_jLabel, gridBagConstraints);
        
        description_jLabel.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        add(description_jLabel, gridBagConstraints);
        
        type_jLabel.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        add(type_jLabel, gridBagConstraints);
        
        quantity_jLabel.setText("jLabel6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        add(quantity_jLabel, gridBagConstraints);
        
        txtQuantity.setText("jTextField2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 10);
        add(txtQuantity, gridBagConstraints);
        
        scale_jLabel.setText("jLabel7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        add(scale_jLabel, gridBagConstraints);
        
        txtScale.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16" }));
        txtScale.setSelectedIndex(8);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        add(txtScale, gridBagConstraints);
        
        fee_jLabel.setText("jLabel8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(fee_jLabel, gridBagConstraints);
        
        txtFeePow.setText("jTextField4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        add(txtFeePow, gridBagConstraints);
        
        // issue_jButton.setText("jButton3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(issue_jButton, gridBagConstraints);
    }// </editor-fold>
    
    public void onIssueClick() {
        // DISABLE
        issue_jButton.setEnabled(false);
        
        // CHECK IF WALLET UNLOCKED
        if (!Controller.getInstance().isWalletUnlocked()) {
            // ASK FOR PASSWORD
            String password = PasswordPane.showUnlockWalletDialog(this);
            if (!Controller.getInstance().unlockWallet(password)) {
                // WRONG PASSWORD
                JOptionPane.showMessageDialog(MainFrame.getInstance(), Lang.getInstance().translate("Invalid password"),
                        Lang.getInstance().translate("Unlock Wallet"), JOptionPane.ERROR_MESSAGE);
                
                // ENABLE
                issue_jButton.setEnabled(true);
                
                return;
            }
        }
        
        // READ CREATOR
        Account sender = (Account) this.cbxFrom.getSelectedItem();
        
        int parsestep = 0;
        try {
            
            // READ FEE POW
            int feePow = Integer.parseInt(this.txtFeePow.getText());
            
            // READ SCALE
            parsestep++;
            byte scale = Byte.parseByte((String) this.txtScale.getSelectedItem());
            
            
            // READ QUANTITY
            parsestep++;
            long quantity = Long.parseLong(this.txtQuantity.getText());
            boolean asPack = false;
            
            // CREATE ASSET
            parsestep++;
            // SCALE, ASSET_TYPE, QUANTITY
            PrivateKeyAccount creator = Controller.getInstance().getPrivateKeyAccountByAddress(sender.getAddress());
            int asset_type = 0;
            // if (currency_unmovabl_chk.isSelected()) asset_type = 1;
            // if (claim_right_obligation_chk.isSelected()) asset_type = 2;
            parsestep++;
            asset_type = ((AssetType) assetTypeModet.getSelectedItem()).getId();
            
            IssueAssetTransaction issueAssetTransaction = (IssueAssetTransaction) Controller.getInstance().issueAsset(
                    creator, this.txtName.getText(), this.txtareaDescription.getText(), add_Logo_Icon_Panel.imgButes,
                    add_Image_Panel.imgButes, false, scale, asset_type, quantity, feePow);
            
            AssetCls asset = (AssetCls) issueAssetTransaction.getItem();
            
            // Issue_Asset_Confirm_Dialog cont = new
            // Issue_Asset_Confirm_Dialog(issueAssetTransaction);
            String text = "<HTML><body>";
            text += Lang.getInstance().translate("Confirmation Transaction") + ":&nbsp;"
                    + Lang.getInstance().translate("Issue Asset") + "<br><br><br>";
            text += Lang.getInstance().translate("Creator") + ":&nbsp;" + issueAssetTransaction.getCreator() + "<br>";
            text += "[" + asset.getKey() + "]" + Lang.getInstance().translate("Name") + ":&nbsp;" + asset.viewName()
                    + "<br>";
            text += Lang.getInstance().translate("Quantity") + ":&nbsp;" + asset.getQuantity().toString() + "<br>";
            // text += Lang.getInstance().translate("Movable") +":&nbsp;"+
            // Lang.getInstance().translate(((AssetCls)issueAssetTransaction.getItem()).isMovable()+"")+
            // "<br>";
            text += Lang.getInstance().translate("Asset Type") + ":&nbsp;"
                    + Lang.getInstance().translate(asset.viewAssetType() + "") + "<br>";
            text += Lang.getInstance().translate("Scale") + ":&nbsp;" + asset.getScale() + "<br>";
            text += Lang.getInstance().translate("Description") + ":<br>" + library.to_HTML(asset.getDescription())
                    + "<br>";
            String Status_text = "<HTML>" + Lang.getInstance().translate("Size") + ":&nbsp;"
                    + issueAssetTransaction.viewSize(false) + " Bytes, ";
            Status_text += "<b>" + Lang.getInstance().translate("Fee") + ":&nbsp;"
                    + issueAssetTransaction.getFee().toString() + " COMPU</b><br></body></HTML>";
            
            // System.out.print("\n"+ text +"\n");
            // UIManager.put("OptionPane.cancelButtonText", "Отмена");
            // UIManager.put("OptionPane.okButtonText", "Готово");
            
            // int s = JOptionPane.showConfirmDialog(MainFrame.getInstance(),
            // text, Lang.getInstance().translate("Issue Asset"),
            // JOptionPane.YES_NO_OPTION);
            
            Issue_Confirm_Dialog dd = new Issue_Confirm_Dialog(MainFrame.getInstance(), true, text,
                    (int) (th.getWidth() / 1.2), (int) (th.getHeight() / 1.2), Status_text,
                    Lang.getInstance().translate("Confirmation Transaction"));
            dd.setLocationRelativeTo(th);
            dd.setVisible(true);
            
            // JOptionPane.OK_OPTION
            if (!dd.isConfirm) { // s!= JOptionPane.OK_OPTION) {
                
                issue_jButton.setEnabled(true);
                
                return;
            }
            
            // VALIDATE AND PROCESS
            int result = Controller.getInstance().getTransactionCreator().afterCreate(issueAssetTransaction, asPack);
            
            // CHECK VALIDATE MESSAGE
            switch (result) {
                case Transaction.VALIDATE_OK:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Asset issue has been sent!"),
                            Lang.getInstance().translate("Success"), JOptionPane.INFORMATION_MESSAGE);
                    
                    break;
                
                case Transaction.INVALID_QUANTITY:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid quantity!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                
                case Transaction.NOT_ENOUGH_FEE:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Not enough %fee% balance!").replace("%fee%",
                                    AssetCls.FEE_NAME),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                    break;
                
                case Transaction.INVALID_NAME_LENGTH:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Name must be between %m and %M characters!")
                                    .replace("%m", "" + issueAssetTransaction.getItem().getMinNameLen())
                                    .replace("%M", "" + ItemCls.MAX_NAME_LENGTH),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                    break;
                
                case Transaction.INVALID_DESCRIPTION_LENGTH:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Description must be between 1 and 1000 characters!"),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                    break;
                
                case Transaction.INVALID_PAYMENTS_LENGTH:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid quantity!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                
                case Transaction.CREATOR_NOT_PERSONALIZED:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Issuer account not personalized!"),
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                    break;
                
                default:
                    
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Unknown error") + "[" + result + "]!",
                            Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
                    break;
                
            }
        } catch (Exception e) {
            switch (parsestep) {
                case 0:
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid Fee Power!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case 1:
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid Scale!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid Quantity!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid Type!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            Lang.getInstance().translate("Invalid Asset!"), Lang.getInstance().translate("Error"),
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
        
        // ENABLE
        issue_jButton.setEnabled(true);
    }
    // End of variables declaration
}
