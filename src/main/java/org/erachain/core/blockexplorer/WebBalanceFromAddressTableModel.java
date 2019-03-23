package org.erachain.core.blockexplorer;

import org.erachain.controller.Controller;
import org.erachain.core.account.Account;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.database.SortableList;
import org.erachain.lang.Lang;
import org.erachain.utils.NumberAsString;
import org.erachain.utils.ObserverMessage;
import org.erachain.utils.Pair;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple5;

import javax.swing.table.AbstractTableModel;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("serial")
public class WebBalanceFromAddressTableModel extends AbstractTableModel implements Observer {
    //private static final int COLUMN_ADDRESS = 0;
    public static final int COLUMN_C = 4;
    public static final int COLUMN_B = 3;
    public static final int COLUMN_A = 2;
    public static final int COLUMN_ASSET_NAME = 1;
    public static final int COLUMN_ASSET_KEY = 0;
    List<Account> accounts;
    Account account;
    Pair<Tuple2<String, Long>, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>> balance;
    Object tab_Balances;
    Tuple2<Long, String> asset;
    private long key;
    private String[] columnNames = Lang.getInstance().translate(new String[]{"key Asset", "Asset", "Balance A", "Balance B", "Balance C"});
    // balances;
    private SortableList<Tuple2<String, Long>, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>> balances;
    private ArrayList<Pair<Account, Pair<Long, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>>> table_balance;
    private ArrayList<Pair<Account, Pair<Long, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>>> table_balance1;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public WebBalanceFromAddressTableModel(Account account) {


        //this.key = key;
        Controller.getInstance().addObserver(this);

        //	 table_balance = new List();
        table_balance = new ArrayList<>();//Pair();
        table_balance1 = new ArrayList<>();
        HashSet<Long> item;
        item = new HashSet();


        this.balances = Controller.getInstance().getBalances(account); //.getBalances(key);
        for (int ib = 0; this.balances.size() > ib; ib++) {
            balance = this.balances.get(ib);
            if (balance.getA().b == 5)
                continue;

            table_balance1.add(new Pair(account, new Pair(balance.getA().b, balance.getB())));
            item.add(balance.getA().b);
        }


        for (Long i : item) {
            BigDecimal sumA = new BigDecimal(0);
            BigDecimal sumB = new BigDecimal(0);
            BigDecimal sumC = new BigDecimal(0);
            BigDecimal sumD = new BigDecimal(0);
            BigDecimal sumE = new BigDecimal(0);
            for (Pair<Account, Pair<Long, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>> k : this.table_balance1) {
                if (k.getB().getA() == i) {

                    sumA = sumA.add(k.getB().getB().a.b);
                    sumB = sumB.add(k.getB().getB().b.b);
                    sumC = sumC.add(k.getB().getB().c.b);
                    sumD = sumD.add(k.getB().getB().d.b);
                    sumE = sumD.add(k.getB().getB().e.b);
                }

            }

            table_balance.add(new Pair(account, new Pair(i, new Tuple5(sumA, sumB, sumC, sumD, sumE))));

        }


        //((SortableList<Tuple2<String, Long>, Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>) this.balances).registerObserver();
    }


    public Class<? extends Object> getColumnClass(int c) {     // set column type
        Object o = getValueAt(0, c);
        return o == null ? Null.class : o.getClass();
    }


    public AssetCls getAsset(int row) {
        return Controller.getInstance().getAsset(table_balance.get(row).getB().getA());
    }

    public String getAccount(int row) {
        // TODO Auto-generated method stub
        return table_balance.get(row).getA().getAddress();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public int getRowCount() {

        return table_balance.size();
    }

    @Override
    public Object getValueAt(int row, int column) {


        if (table_balance == null || row > table_balance.size() - 1) {
            return null;
        }

        AssetCls asset = Controller.getInstance().getAsset(table_balance.get(row).getB().getA());
        if (asset == null)
            return "";


        Pair<Tuple2<String, Long>, BigDecimal> sa;
        switch (column) {
            case COLUMN_ASSET_KEY:

                return asset.getKey();

            case COLUMN_A:

                return NumberAsString.formatAsString(table_balance.get(row).getB().getB().a);

            case COLUMN_B:

                return NumberAsString.formatAsString(table_balance.get(row).getB().getB().b);

            case COLUMN_C:

                return NumberAsString.formatAsString(table_balance.get(row).getB().getB().c);

            case COLUMN_ASSET_NAME:

                return asset.getName();


        }

        return null;
    }


    public Object getBalanceAt(int row, int column) {
        if (table_balance == null || row > table_balance.size() - 1) {
            return null;
        }

        switch (column) {
            case COLUMN_A:

                return table_balance.get(row).getB().getB().a;

            case COLUMN_B:

                return table_balance.get(row).getB().getB().b;

            case COLUMN_C:

                return table_balance.get(row).getB().getB().c;

        }

        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            this.syncUpdate(o, arg);
        } catch (Exception e) {
            //GUI ERROR
        }
    }

    public synchronized void syncUpdate(Observable o, Object arg) {
        ObserverMessage message = (ObserverMessage) arg;

        //CHECK IF LIST UPDATED
        if ((message.getType() == ObserverMessage.NETWORK_STATUS && (int) message.getValue() == Controller.STATUS_OK)
                || (Controller.getInstance().getStatus() == Controller.STATUS_OK &&
                (message.getType() == ObserverMessage.ADD_BALANCE_TYPE || message.getType() == ObserverMessage.REMOVE_BALANCE_TYPE))) {
            this.fireTableDataChanged();
        }
    }

    public void removeObservers() {
        //this.balances.removeObserver();
        Controller.getInstance().deleteObserver(this);
    }


}
