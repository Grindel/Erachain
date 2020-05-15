package org.erachain.core.transaction;

import com.google.common.primitives.Bytes;
import org.erachain.core.BlockChain;
import org.erachain.core.account.Account;
import org.erachain.core.account.PrivateKeyAccount;
import org.erachain.core.account.PublicKeyAccount;
import org.erachain.core.block.Block;
import org.erachain.core.item.ItemCls;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public abstract class IssueItemRecord extends Transaction implements Itemable {

    static Logger LOGGER = LoggerFactory.getLogger(IssueItemRecord.class.getName());

    //private static final int BASE_LENGTH = Transaction.BASE_LENGTH;

    protected ItemCls item;
    protected Long key;

    public IssueItemRecord(byte[] typeBytes, String NAME_ID, PublicKeyAccount creator, ItemCls item, byte feePow, long timestamp, Long reference) {
        super(typeBytes, NAME_ID, creator, feePow, timestamp, reference);
        this.item = item;
    }

    public IssueItemRecord(byte[] typeBytes, String NAME_ID, PublicKeyAccount creator, ItemCls item, byte feePow, long timestamp, Long reference, byte[] signature) {
        this(typeBytes, NAME_ID, creator, item, feePow, timestamp, reference);
        this.signature = signature;
        if (true || item.getReference() == null) item.setReference(signature); // set reference
        //item.resolveKey(DLSet.getInstance());
        //if (timestamp > 1000 ) this.calcFee(); // not asPaack
    }

    public IssueItemRecord(byte[] typeBytes, String NAME_ID, PublicKeyAccount creator, ItemCls item, byte[] signature) {
        this(typeBytes, NAME_ID, creator, item, (byte) 0, 0L, null);
        this.signature = signature;
        if (true || this.item.getReference() == null) this.item.setReference(signature);
        //item.resolveKey(DLSet.getInstance());
    }

    //GETTERS/SETTERS
    //public static String getName() { return "Issue Item"; }

    @Override
    public ItemCls getItem() {
        return this.item;
    }

    /** нужно для отображение в блокэксплорере
     *  - не участвует в Протоколе, так как перед выпуском неизвестно его значение
     * @return
     */
    @Override
    public long getKey() {
        if (key == null) {
            key = item.getKey(dcSet);
        }

        return key;
    }

    @Override
    public String getTitle() {
        return this.item.getName();
    }

    @Override
    public void makeItemsKeys() {
        // запомним что тут две сущности
        if (creatorPersonDuration != null) {
            itemsKeys = new Object[][]{
                    new Object[]{ItemCls.PERSON_TYPE, creatorPersonDuration.a},
                    new Object[]{item.getItemType(), key}
            };
        }
    }

    @Override
    public String viewItemName() {
        return item.toString();
    }

    public String getItemDescription() {
        return item.getDescription();
    }

    @Override
    public boolean hasPublicText() {
        return true;
    }

    //@Override
    @Override
    public void sign(PrivateKeyAccount creator, int forDeal) {
        super.sign(creator, forDeal);
        if (this.getType() != ItemCls.IMPRINT_TYPE
                // in IMPRINT reference already setted before sign
                || this.item.getReference() == null) this.item.setReference(this.signature);
    }

    //PARSE CONVERT


    @SuppressWarnings("unchecked")
    @Override
    public JSONObject toJson() {
        //GET BASE
        JSONObject transaction = this.getJsonBase();

        //ADD CREATOR/NAME/DISCRIPTION/QUANTITY/DIVISIBLE
        transaction.put("item", this.item.toJson());

        return transaction;
    }

    @Override
    public byte[] toBytes(int forDeal, boolean withSignature) {
        byte[] data = super.toBytes(forDeal, withSignature);

        // without reference
        data = Bytes.concat(data, this.item.toBytes(false, false));

        return data;
    }

    @Override
    public int getDataLength(int forDeal, boolean withSignature) {
        // not include item reference

        int base_len;
        if (forDeal == FOR_MYPACK)
            base_len = BASE_LENGTH_AS_MYPACK;
        else if (forDeal == FOR_PACK)
            base_len = BASE_LENGTH_AS_PACK;
        else if (forDeal == FOR_DB_RECORD)
            base_len = BASE_LENGTH_AS_DBRECORD;
        else
            base_len = BASE_LENGTH;

        if (!withSignature)
            base_len -= SIGNATURE_LENGTH;

        return base_len + this.item.getDataLength(false);

    }

    //VALIDATE

    //@Override
    @Override
    public int isValid(int asDeal, long flags) {

        if (height < BlockChain.ALL_VALID_BEFORE) {
            return VALIDATE_OK;
        }

        //CHECK NAME LENGTH
        String name = this.item.getName();
        // TEST ONLY CHARS
        int nameLen = name.length();

        if (nameLen < item.getMinNameLen()) {
            // IF is NEW NOVA
            if (this.item.isNovaAsset(this.creator, this.dcSet) <= 0) {
                return INVALID_NAME_LENGTH_MIN;
            }
        }

        // TEST ALL BYTES for database FIELD
        if (name.getBytes(StandardCharsets.UTF_8).length > ItemCls.MAX_NAME_LENGTH) {
            return INVALID_NAME_LENGTH_MAX;
        }

        //CHECK ICON LENGTH
        int iconLength = this.item.getIcon().length;
        if (iconLength < 0) {
            return INVALID_ICON_LENGTH_MIN;
        }
        if (iconLength > ItemCls.MAX_ICON_LENGTH) {
            return INVALID_ICON_LENGTH_MAX;
        }

        //CHECK IMAGE LENGTH
        int imageLength = this.item.getImage().length;
        if (imageLength < 0) {
            return INVALID_IMAGE_LENGTH_MIN;
        }
        if (imageLength > ItemCls.MAX_IMAGE_LENGTH) {
            return INVALID_IMAGE_LENGTH_MAX;
        }

        //CHECK DESCRIPTION LENGTH
        int descriptionLength = this.item.getDescription().getBytes(StandardCharsets.UTF_8).length;
        if (descriptionLength > BlockChain.MAX_REC_DATA_BYTES) {
            return INVALID_DESCRIPTION_LENGTH_MAX;
        }

        return super.isValid(asDeal, flags);

    }

    //PROCESS/ORPHAN
    //@Override
    @Override
    public void process(Block block, int asDeal) {
        //UPDATE CREATOR
        super.process(block, asDeal);

        // SET REFERENCE if not setted before (in Imprint it setted)
        if (this.getType() != ItemCls.IMPRINT_TYPE
                // in IMPRINT reference already setted before sign
                || this.item.getReference() == null) this.item.setReference(this.signature);

        //INSERT INTO DATABASE
        key = this.item.insertToMap(this.dcSet, this.item.getStartKey());

    }

    //@Override
    @Override
    public void orphan(Block block, int asDeal) {
        //UPDATE CREATOR
        super.orphan(block, asDeal);

        //logger.debug("<<<<< org.erachain.core.transaction.IssueItemRecord.orphan 1");
        //DELETE FROM DATABASE
        long key = this.item.deleteFromMap(this.dcSet, item.getStartKey());
        //logger.debug("<<<<< org.erachain.core.transaction.IssueItemRecord.orphan 2");
    }

    @Override
    public HashSet<Account> getInvolvedAccounts() {
        HashSet<Account> accounts = this.getRecipientAccounts();
        accounts.add(this.creator);
        return accounts;
    }

    @Override
    public HashSet<Account> getRecipientAccounts() {
        HashSet<Account> accounts = new HashSet<>(3, 1);
        if (!this.item.getOwner().equals(this.creator)) {
            accounts.add(this.item.getOwner());
        }
        return accounts;
    }

    @Override
    public boolean isInvolved(Account account) {

        String address = account.getAddress();

        if (address.equals(this.creator.getAddress())) {
            return true;
        } else if (address.equals(this.item.getOwner().getAddress())) {
            return true;
        }

        return false;
    }

}
