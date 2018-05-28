package datachain;


import org.mapdb.DB;
import utils.ObserverMessage;

public class AddressStatement_Refs extends AddressItem_Refs {
    static final String NAME = "statement";

    public AddressStatement_Refs(DCSet databaseSet, DB database) {
        super(databaseSet, database,
                NAME,
                ObserverMessage.RESET_STATEMENT_TYPE,
                ObserverMessage.ADD_STATEMENT_TYPE,
                ObserverMessage.REMOVE_STATEMENT_TYPE,
                ObserverMessage.LIST_STATEMENT_TYPE
        );
    }

    public AddressStatement_Refs(AddressStatement_Refs parent) {
        super(parent);
    }

}
