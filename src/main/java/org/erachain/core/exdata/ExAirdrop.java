package org.erachain.core.exdata;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.erachain.controller.Controller;
import org.erachain.core.BlockChain;
import org.erachain.core.account.Account;
import org.erachain.core.account.PublicKeyAccount;
import org.erachain.core.block.Block;
import org.erachain.core.crypto.Base58;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.core.transaction.RSignNote;
import org.erachain.core.transaction.Transaction;
import org.erachain.core.transaction.TransactionAmount;
import org.erachain.datachain.DCSet;
import org.erachain.lang.Lang;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mapdb.Fun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Simple pay - for all same amount
 */

public class ExAirdrop {

    public static final byte BASE_LENGTH = 4 + 8 + 8 + 2;

    public static final int MAX_COUNT = Short.MAX_VALUE;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExAirdrop.class);

    /**
     * 0 - version; 1 - flags;
     */
    private final int flags; // byte[2]

    private long assetKey;
    private final BigDecimal amount;
    private final int balancePos;
    final boolean backward;
    private final byte[][] addresses;

    /////////////////
    DCSet dcSet;
    private int height;
    AssetCls asset;
    BigDecimal totalPay;

    public String errorValue;

    public ExAirdrop(int flags, long assetKey, BigDecimal amount, int balancePos, boolean backward, byte[][] addresses) {
        this.flags = flags;
        this.assetKey = assetKey;
        this.amount = amount;
        this.balancePos = balancePos;
        this.backward = backward;
        this.addresses = addresses;

        totalPay = amount.multiply(BigDecimal.valueOf(addresses.length));

    }

    public Long getAssetKey() {
        return assetKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AssetCls getAsset() {
        return asset;
    }


    public void setDC(DCSet dcSet) {
        if (this.dcSet == null || !this.dcSet.equals(dcSet)) {
            this.dcSet = dcSet;
            this.asset = this.dcSet.getItemAssetMap().get(this.assetKey);
        }
    }

    public byte[] toBytes() throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        outStream.write(Ints.toByteArray(flags));

        byte[] buff;
        outStream.write(Longs.toByteArray(this.assetKey));

        outStream.write(this.amount.scale());
        buff = this.amount.unscaledValue().toByteArray();
        outStream.write(buff.length);
        outStream.write(buff);

        outStream.write(Ints.toByteArray(addresses.length));
        for (int i = 0; i < addresses.length; i++) {
            outStream.write(addresses[i]);
        }

        return outStream.toByteArray();

    }

    public int length() {
        return BASE_LENGTH + addresses.length * Account.ADDRESS_SHORT_LENGTH;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ExAirdrop parse(byte[] data, int pos) throws Exception {

        int scale;
        int len;

        int flags = Ints.fromByteArray(Arrays.copyOfRange(data, pos, pos + Integer.BYTES));
        pos += Integer.BYTES;

        Long assetKey = null;
        int balancePos = 0;
        boolean backward = false;

        assetKey = Longs.fromByteArray(Arrays.copyOfRange(data, pos, pos + Long.BYTES));
        pos += Long.BYTES;

        balancePos = data[pos++];
        backward = data[pos++] > 0;

        scale = data[pos++];
        len = data[pos++];
        BigDecimal payValue = new BigDecimal(new BigInteger(Arrays.copyOfRange(data, pos, pos + len)), scale);
        pos += len;

        len = Ints.fromByteArray(Arrays.copyOfRange(data, pos, pos + Integer.BYTES));
        pos += Integer.BYTES;

        byte[][] addresses = new byte[len][];
        for (int i = 0; i < len; i++) {
            System.arraycopy(data, pos, addresses[i], 0, Account.ADDRESS_SHORT_LENGTH);
            pos += Account.ADDRESS_SHORT_LENGTH;
        }

        return new ExAirdrop(flags, assetKey, payValue, balancePos, backward, addresses);
    }

    public static Fun.Tuple2<ExAirdrop, String> make(Long assetKey, int balancePos, boolean backward,
                                                     String amountStr, String[] addressesStr) {

        int steep = 0;
        BigDecimal amount;

        Controller cntr = Controller.getInstance();
        BlockChain chain = cntr.getBlockChain();

        try {
            amount = amountStr == null || amountStr.isEmpty() ? null : new BigDecimal(amountStr);
        } catch (Exception e) {
            String error;
            switch (steep) {
                case 0:
                    error = "Wrong amount";
                    break;
                default:
                    error = e.getMessage();
            }
            return new Fun.Tuple2<>(null, error);
        }

        if (assetKey == null || assetKey == 0L) {
            return new Fun.Tuple2<>(null, "Wrong assetKey (null or ZERO)");
        } else if (amount == null || amount.signum() == 0) {
            return new Fun.Tuple2<>(null, "Wrong payMethodValue (null or ZERO)");
        }

        Fun.Tuple2<Account, String> result;
        byte[][] addresses = new byte[addressesStr.length][];
        for (int i = 0; i < addressesStr.length; i++) {
            result = Account.tryMakeAccount(addressesStr[i]);
            if (result.a == null) {
                return new Fun.Tuple2<>(null, i + ":" + addressesStr[i] + " - " + result.b);
            }
            addresses[i] = result.a.getShortAddressBytes();
        }

        int flags = 0;
        return new Fun.Tuple2<>(new ExAirdrop(flags, assetKey, amount, balancePos, backward, addresses), null);

    }

    /**
     * Version 2 maker for BlockExplorer
     */
    public JSONObject makeJSONforHTML(JSONObject langObj) {
        JSONObject json = toJson();

        json.put("asset", asset.getName());

        json.put("Label_Counter", Lang.T("Counter", langObj));
        json.put("Label_Total_Amount", Lang.T("Total Amount", langObj));
        json.put("Label_Additional_Fee", Lang.T("Additional Fee", langObj));

        return json;

    }

    public JSONObject toJson() {

        JSONObject toJson = new JSONObject();

        toJson.put("flags", flags);
        toJson.put("assetKey", assetKey);
        toJson.put("amount", amount);
        toJson.put("balancePosition", balancePos);
        toJson.put("backward", backward);

        JSONArray array = new JSONArray();
        for (byte[] address : addresses) {
            array.add(Base58.encode(address));
        }
        toJson.put("addresses", array);

        return toJson;
    }

    public Fun.Tuple2<Integer, String> checkValidList(DCSet dcSet, int height, AssetCls asset, Account creator) {

        Account recipient;
        //byte[] signature = rNote.getSignature();
        boolean creatorIsPerson = creator.isPerson(dcSet, height);

        // возьмем знаки (минус) для создания позиции баланса такой
        Fun.Tuple2<Integer, Integer> signs = Account.getSignsForBalancePos(balancePos);
        long key = signs.a * assetKey;

        // комиссию не проверяем так как она не правильно считается внутри?
        long actionFlags = Transaction.NOT_VALIDATE_FLAG_FEE;

        Fun.Tuple2<Integer, String> result;
        byte[] signature = new byte[0];
        int index = 0;
        for (byte[] recipientShort : addresses) {

            recipient = new Account(recipientShort);

            result = TransactionAmount.isValidAction(dcSet, height, creator, signature,
                    key, asset, amount, recipient,
                    backward, BigDecimal.ZERO, null, creatorIsPerson, actionFlags);

            if (result.a != Transaction.VALIDATE_OK) {
                errorValue = "Airdrop: address[" + index + "] -> " + recipient.getAddress();
                return result;
            }

            ++index;
        }

        return new Fun.Tuple2<>(Transaction.VALIDATE_OK, null);
    }

    public int isValid(RSignNote rNote) {

        if (this.assetKey == 0L) {
            errorValue = "Airdrop: assetKey == null or ZERO";
            return Transaction.INVALID_ITEM_KEY;
        } else if (this.balancePos < TransactionAmount.ACTION_SEND || this.balancePos > TransactionAmount.ACTION_SPEND) {
            errorValue = "Airdrop: balancePos out off range";
            return Transaction.INVALID_BALANCE_POS;
        } else if (amount.signum() < 0) {
            errorValue = "Airdrop: payMethodValue < 0";
            return Transaction.INVALID_AMOUNT;
        }


        height = rNote.getBlockHeight();

        Account recipient = new Account(addresses[0]);
        PublicKeyAccount creator = rNote.getCreator();
        byte[] signature = rNote.getSignature();
        boolean creatorIsPerson = creator.isPerson(dcSet, height);

        // возьмем знаки (минус) для создания позиции баланса такой
        Fun.Tuple2<Integer, Integer> signs = Account.getSignsForBalancePos(balancePos);
        long key = signs.a * assetKey;

        // комиссию не проверяем так как она не правильно считается внутри?
        long actionFlags = Transaction.NOT_VALIDATE_FLAG_FEE;

        BigDecimal totalFeeBG = rNote.getFee();
        // проверим как будто всю сумму одному переводим - с учетом комиссии полной
        Fun.Tuple2<Integer, String> result = TransactionAmount.isValidAction(dcSet, height, creator, signature,
                key, asset, signs.b > 0 ? totalPay : totalPay.negate(), recipient,
                backward, totalFeeBG, null, creatorIsPerson, actionFlags);
        if (result.a != Transaction.VALIDATE_OK) {
            errorValue = "Airdrop: totalPay + totalFee = " + totalPay.toPlainString() + " / " + totalFeeBG.toPlainString();
            return result.a;
        }

        ////////// TODO NEED CHECK ALL
        boolean needCheckAllList = false;
        if (needCheckAllList) {

            result = checkValidList(dcSet, height, asset, creator);
            if (result.a != Transaction.VALIDATE_OK) {
                return result.a;
            }
        }

        return Transaction.VALIDATE_OK;
    }

    public void processBody(Transaction rNote, boolean asOrphan, Block block) {
        PublicKeyAccount creator = rNote.getCreator();

        long absKey = assetKey;

        // возьмем знаки (минус) для создания позиции баланса такой
        Fun.Tuple2<Integer, Integer> signs = Account.getSignsForBalancePos(balancePos);
        Long actionPayKey = signs.a * assetKey;
        boolean isAmountNegate;
        BigDecimal actionPayAmount;
        boolean incomeReverse = balancePos == Account.BALANCE_POS_HOLD;
        boolean reversedBalancesInPosition = asset.isReverseBalancePos(balancePos);
        boolean backwardAction;

        Account recipient;
        for (byte[] address : addresses) {

            recipient = new Account(address);
            if (recipient == null)
                break;

            actionPayAmount = amount;

            isAmountNegate = actionPayAmount.signum() < 0;
            backwardAction = (reversedBalancesInPosition ^ backward) ^ isAmountNegate;

            if (!asOrphan && block != null) {
                rNote.addCalculated(block, recipient, absKey, actionPayAmount,
                        asset.viewAssetTypeAction(backwardAction, balancePos, asset.getMaker().equals(creator)));
            }

            if (creator.equals(recipient))
                // пропустим себя в любом случае - хотя КАлькулейтед оставим для виду
                continue;

            // сбросим направлени от фильтра
            actionPayAmount = actionPayAmount.abs();
            // зазадим направление от Действия нашего
            actionPayAmount = signs.b > 0 ? actionPayAmount : actionPayAmount.negate();

            TransactionAmount.processAction(dcSet, asOrphan, creator, recipient, balancePos, absKey,
                    asset, actionPayKey, actionPayAmount, backwardAction,
                    incomeReverse);

        }

    }

    public void process(Transaction rNote, Block block) {

        height = rNote.getBlockHeight();
        asset = dcSet.getItemAssetMap().get(assetKey);

        processBody(rNote, false, block);

    }

    public void orphan(Transaction rNote) {

        height = rNote.getBlockHeight();
        asset = dcSet.getItemAssetMap().get(assetKey);

        processBody(rNote, true, null);

    }

}
