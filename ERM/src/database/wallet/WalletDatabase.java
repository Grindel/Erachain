package database.wallet;
// 30/03 ++
import java.io.File;

import org.mapdb.Atomic.Var;

import core.account.Account;
import core.item.ItemCls;
import core.item.assets.AssetCls;
import core.item.imprints.ImprintCls;
import core.item.notes.NoteCls;
import core.item.persons.PersonCls;
import core.item.unions.UnionCls;

import org.mapdb.DB;
import org.mapdb.DBMaker;
//import org.mapdb.Serializer;

import database.IDB;
import settings.Settings;

public class WalletDatabase implements IDB
{
	private static final File WALLET_FILE = new File(Settings.getInstance().getWalletDir(), "wallet.dat");
	
	private static final String VERSION = "version";
	private static final String LAST_BLOCK = "lastBlock";
	
	private DB database;	
	private AccountMap accountMap;
	private TransactionMap transactionMap;
	private BlockMap blockMap;
	private NameMap nameMap;
	private NameSaleMap nameSaleMap;
	private PollMap pollMap;
	private WItemAssetMap assetMap;
	private WItemImprintMap imprintMap;
	private WItemNoteMap noteMap;
	private WItemPersonMap personMap;
	private WItemUnionMap unionMap;
	private OrderMap orderMap;
	private FavoriteItemAsset assetFavoritesSet;
	private FavoriteItemNote noteFavoritesSet;
	private FavoriteItemPerson personFavoritesSet;
	private FavoriteItemUnion unionFavoritesSet;
	
	public static boolean exists()
	{
		return WALLET_FILE.exists();
	}
	
	public WalletDatabase()
	{
		//OPEN WALLET
		WALLET_FILE.getParentFile().mkdirs();
		
		//DELETE TRANSACTIONS
		//File transactionFile = new File(Settings.getInstance().getWalletDir(), "wallet.dat.t");
		//transactionFile.delete();	
		
	    this.database = DBMaker.newFileDB(WALLET_FILE)
	    		.closeOnJvmShutdown()
	    		.cacheSize(2048)
	    		.checksumEnable()
	    		.mmapFileEnableIfSupported()
	            .make();
	    
	    this.accountMap = new AccountMap(this, this.database);
	    this.transactionMap = new TransactionMap(this, this.database);
	    this.blockMap = new BlockMap(this, this.database);
	    this.nameMap = new NameMap(this, this.database);
	    this.nameSaleMap = new NameSaleMap(this, this.database);
	    this.pollMap = new PollMap(this, this.database);
	    this.assetMap = new WItemAssetMap(this, this.database);
	    this.imprintMap = new WItemImprintMap(this, this.database);
	    this.noteMap = new WItemNoteMap(this, this.database);
	    this.personMap = new WItemPersonMap(this, this.database);
	    this.unionMap = new WItemUnionMap(this, this.database);
	    this.orderMap = new OrderMap(this, this.database);
	    this.assetFavoritesSet = new FavoriteItemAsset(this, this.database);
	    this.noteFavoritesSet = new FavoriteItemNote(this, this.database);
	    this.personFavoritesSet = new FavoriteItemPerson(this, this.database);
	    this.unionFavoritesSet = new FavoriteItemUnion(this, this.database);
	}
	
	public void setVersion(int version)
	{
		this.database.getAtomicInteger(VERSION).set(version);
	}
	
	public int getVersion()
	{
		return this.database.getAtomicInteger(VERSION).intValue();
	}
	
	public void setLastBlockSignature(byte[] signature)
	{
		/*
		Var<byte[]> atomic;
		if(database.exists(LAST_BLOCK))
		{
			atomic = database.getAtomicVar(LAST_BLOCK);
		}
		else
		{
			atomic = database.createAtomicVar(LAST_BLOCK, new byte[0], Serializer.BYTE_ARRAY);
		}
		*/
		Var<byte[]> atomic = this.database.getAtomicVar(LAST_BLOCK);
		atomic.set(signature);
	}
	
	public byte[] getLastBlockSignature()
	{
		/*
		Var<byte[]> atomic;
		if(database.exists(LAST_BLOCK))
		{
			atomic = database.getAtomicVar(LAST_BLOCK);
		}
		else
		{
			atomic = database.createAtomicVar(LAST_BLOCK, new byte[0], Serializer.BYTE_ARRAY);
		}
		*/
		Var<byte[]> atomic = this.database.getAtomicVar(LAST_BLOCK);
		return atomic.get();
	}
	
	public AccountMap getAccountMap()
	{
		return this.accountMap;
	}
	
	public TransactionMap getTransactionMap()
	{
		return this.transactionMap;
	}
	
	public BlockMap getBlockMap()
	{
		return this.blockMap;
	}
	
	public NameMap getNameMap()
	{
		return this.nameMap;
	}
	
	public NameSaleMap getNameSaleMap()
	{
		return this.nameSaleMap;
	}
	
	public PollMap getPollMap()
	{
		return this.pollMap;
	}
	public WItemAssetMap getAssetMap()
	{
		return this.assetMap;
	}
	public WItemImprintMap getImprintMap()
	{
		return this.imprintMap;
	}
	public WItemNoteMap getNoteMap()
	{
		return this.noteMap;
	}
	public WItemPersonMap getPersonMap()
	{
		return this.personMap;
	}
	public WItemUnionMap getUnionMap()
	{
		return this.unionMap;
	}
	public WItem_Map getItemMap(ItemCls item)
	{
		if (item instanceof AssetCls) { 
			return this.assetMap;
		} else if (item instanceof ImprintCls) { 
			return this.imprintMap;
		} else if (item instanceof NoteCls) { 
			return this.noteMap;
		} else if (item instanceof PersonCls) { 
			return this.personMap;
		} else if (item instanceof UnionCls) { 
			return this.unionMap;
		} else {
			return null;
		}
	}
	public WItem_Map getItemMap(int type)
	{
		switch(type)
		{
			case ItemCls.ASSET_TYPE:
				return this.assetMap;
			case ItemCls.IMPRINT_TYPE:
				return this.imprintMap;
			case ItemCls.NOTE_TYPE:
				return this.noteMap;
			case ItemCls.PERSON_TYPE:
				return this.personMap;
			case ItemCls.UNION_TYPE:
				return this.unionMap;
		}
		return null;
	}

	public void addItemToFavorite(ItemCls item)
	{
		getItemFavoritesSet(item).add(item.getKey());
	}
	
	public void removeItemFromFavorite(ItemCls item) {
		getItemFavoritesSet(item).delete(item.getKey());
	}
	public boolean isItemFavorite(ItemCls item) {
		return getItemFavoritesSet(item).contains(item.getKey());
	}
	
	/*
	public boolean replace(ItemCls item)
	{
		//if(this.notesFavorites != null) {
		//	this.database.getNoteFavoritesSet().replace(this.notesFavorites.getKeys());	
		//}
		if (item instanceof NoteCls) { 
			return this.noteMap.replace(this.notesFavorites.getKeys();
		} else if (item instanceof AssetCls) { 
			return this.assetMap.replace(this.assetsFavorites.getKeys();
		} else if (item instanceof PersonCls) { 
			return this.personMap.replace(this.personsFavorites.getKeys();
		} else {
			return false;
		}
	}
	*/


	public OrderMap getOrderMap()
	{
		return this.orderMap;
	}
	
	public FavoriteItemAsset getAssetFavoritesSet()
	{
		return this.assetFavoritesSet;
	}
	public FavoriteItemNote getNoteFavoritesSet()
	{
		return this.noteFavoritesSet;
	}
	public FavoriteItemPerson getPersonFavoritesSet()
	{
		return this.personFavoritesSet;
	}
	public FavoriteItemUnion getUnionFavoritesSet()
	{
		return this.unionFavoritesSet;
	}
	public FavoriteItem getItemFavoritesSet(ItemCls item)
	{
		if (item instanceof AssetCls) { 
			return this.assetFavoritesSet;
		//} else if (item instanceof ImprintCls) { 
		//	return this.imprintFavoritesSet;
		} else if (item instanceof NoteCls) { 
			return this.noteFavoritesSet;
		} else if (item instanceof PersonCls) { 
			return this.personFavoritesSet;
		} else if (item instanceof UnionCls) { 
			return this.unionFavoritesSet;
		} else {
			return null;
		}
	}
	
	public void delete(Account account)
	{
		this.accountMap.delete(account);
		this.blockMap.delete(account);
		this.transactionMap.delete(account);
		this.nameMap.delete(account);
		this.nameSaleMap.delete(account);
		this.pollMap.delete(account);
		this.assetMap.delete(account);
		this.imprintMap.delete(account);
		this.noteMap.delete(account);
		this.unionMap.delete(account);
		this.personMap.delete(account);
		this.orderMap.delete(account);
	}
	
	public void commit()
	{
		this.database.commit();
	}
	
	public void close() 
	{
		if(this.database != null)
		{
			if(!this.database.isClosed())
			{
				this.database.commit();
				this.database.close();
			}
		}
	}
}