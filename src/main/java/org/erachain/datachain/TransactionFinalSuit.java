package org.erachain.datachain;

import org.erachain.dbs.IteratorCloseable;

/**
 * Iterators for this TAB
 */
public interface TransactionFinalSuit {

    void deleteForBlock(Integer height);

    IteratorCloseable<Long> getBlockIterator(Integer height);

    IteratorCloseable<Long> getIteratorByRecipient(byte[] addressShort, boolean descending);

    IteratorCloseable<Long> getIteratorByCreator(byte[] addressShort, boolean descending);

    IteratorCloseable<Long> getIteratorByCreator(byte[] addressShort, Long fromSeqNo, boolean descending);

    /**
     * @param addressShort
     * @param type         - TRANSACTION type
     * @param isCreator    True - only CREATORS, False - only RECIPIENTS
     * @param descending
     * @return
     */
    IteratorCloseable<Long> getIteratorByAddressAndType(byte[] addressShort, Integer type, Boolean isCreator, boolean descending);

    /**
     * Здесь обязательно нужно задавать тип транзакции и получатель или создатель - иначе по FROM_ID работать не будет в RocksDB
     *
     * @param addressShort
     * @param type         - TRANSACTION type
     * @param isCreator    True - only CREATORS, False - only RECIPIENTS
     * @param fromID
     * @param descending
     * @return
     */
    IteratorCloseable<Long> getIteratorByAddressAndTypeFrom(byte[] addressShort, Integer type, Boolean isCreator, Long fromID, boolean descending);

    IteratorCloseable<Long> getIteratorByTitle(String filter, boolean asFilter, String fromWord, Long fromSeqNo, boolean descending);

    IteratorCloseable<Long> getIteratorByAddress(byte[] addressShort, boolean descending);

    IteratorCloseable<Long> getBiDirectionIterator(Long fromSeqNo, boolean descending);

    IteratorCloseable<Long> getBiDirectionAddressIterator(byte[] addressShort, Long fromSeqNo, boolean descending);

    }
