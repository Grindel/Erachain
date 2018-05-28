package gui.models;

import core.account.Account;
import core.item.assets.AssetCls;
import core.voting.PollOption;
import datachain.DCSet;
import lang.Lang;
import utils.NumberAsString;
import utils.Pair;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@SuppressWarnings("serial")
public class VotesTableModel extends AbstractTableModel {
    public static final int COLUMN_VOTES = 2;
    private static final int COLUMN_ADDRESS = 0;
    private static final int COLUMN_OPTION = 1;
    private String[] columnNames = Lang.getInstance().translate(new String[]{"Account", "Option", "Votes"});
    private List<Pair<Account, PollOption>> votes;
    private AssetCls asset;

    public VotesTableModel(List<Pair<Account, PollOption>> votes, AssetCls asset) {
        this.votes = votes;
        this.asset = asset;
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int index) {
        return this.columnNames[index];
    }

    @Override
    public int getRowCount() {
        return this.votes.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (this.votes == null || row > this.votes.size() - 1) {
            return null;
        }

        Pair<Account, PollOption> vote = this.votes.get(row);

        switch (column) {
            case COLUMN_ADDRESS:

                return vote.getA().getPersonAsString();

            case COLUMN_OPTION:

                return vote.getB().getName();

            case COLUMN_VOTES:

                return NumberAsString.formatAsString(vote.getA().getBalanceUSE(asset.getKey(DCSet.getInstance())));

        }

        return null;
    }

    public void setAsset(AssetCls asset) {
        this.asset = asset;
        this.fireTableDataChanged();
    }
}
