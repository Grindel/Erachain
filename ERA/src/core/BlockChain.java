package core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mapdb.Fun.Tuple2;
import controller.Controller;
import core.account.Account;
import core.block.Block;
import core.block.GenesisBlock;
import core.crypto.Base58;
import core.transaction.ArbitraryTransaction;
import core.transaction.Transaction;
import datachain.BlockHeightsMap;
import datachain.BlockMap;
import datachain.BlockSignsMap;
import datachain.DCSet;
import datachain.TransactionMap;
import network.Peer;
import network.message.MessageFactory;
import ntp.NTP;
import settings.Settings;
import utils.Pair;

public class BlockChain
{

	//public static final int START_LEVEL = 1;
	public static final boolean DEVELOP_USE = true;
	public static final boolean HARD_WORK = false;
	public static final boolean PERSON_SEND_PROTECT = true;
	//public static final int BLOCK_COUNT = 10000; // max count Block (if =<0 to the moon)

	public static final int TESTNET_PORT = DEVELOP_USE?9065:9045;
	public static final int MAINNET_PORT = DEVELOP_USE?9066:9046;

	public static final int DEFAULT_WEB_PORT = DEVELOP_USE?9067:9047;
	public static final int DEFAULT_RPC_PORT = DEVELOP_USE?9068:9048;

	//public static final String TIME_ZONE = "GMT+3";
	//
	public static final boolean ROBINHOOD_USE = false;
	public static final int NEED_PEERS_FOR_UPDATE = HARD_WORK?2:1;
	
	public static final int MAX_ORPHAN = 1000; // max orphan blocks in chain
	public static final int SYNCHRONIZE_PACKET = 300; // when synchronize - get blocks packet by transactions
	public static final int TARGET_COUNT = 100;
	public static final int BASE_TARGET = 1024 * 3;
	public static final int REPEAT_WIN = DEVELOP_USE?5:40; // GENESIS START TOP ACCOUNTS
	
	// RIGHTs 
	public static final int GENESIS_ERA_TOTAL = 10000000;
	public static final int GENERAL_ERA_BALANCE = GENESIS_ERA_TOTAL / 100;
	public static final int MAJOR_ERA_BALANCE = 33000;
	public static final int MINOR_ERA_BALANCE = 1000;
	public static final int MIN_GENERATING_BALANCE = 100;
	public static final BigDecimal MIN_GENERATING_BALANCE_BD = new BigDecimal(MIN_GENERATING_BALANCE);
	//public static final int GENERATING_RETARGET = 10;
	public static final int GENERATING_MIN_BLOCK_TIME = DEVELOP_USE?120:288; // 300 PER DAY
	public static final int GENERATING_MIN_BLOCK_TIME_MS = BlockChain.GENERATING_MIN_BLOCK_TIME * 1000;
	public static final int WIN_BLOCK_BROADCAST_WAIT_MS = 10000; // 

	public static final int BLOCKS_PER_DAY = 24 * 60 * 60 / GENERATING_MIN_BLOCK_TIME; // 300 PER DAY
	//public static final int GENERATING_MAX_BLOCK_TIME = 1000;
	public static final int MAX_BLOCK_BYTES = 2<<21; //4 * 1048576;
	public static final int MAX_REC_DATA_BYTES = MAX_BLOCK_BYTES>>1;
	public static final int GENESIS_WIN_VALUE = DEVELOP_USE?3000:22000;
	public static final String[] GENESIS_ADMINS = new String[]{"78JFPWVVAVP3WW7S8HPgSkt24QF2vsGiS5",
			"7B3gTXXKB226bxTxEHi8cJNfnjSbuuDoMC"};

	public static final byte[][] WIPED_RECORDS = new byte[][]{
		Base58.decode("2yTFTetbUrpZzTU3Y1kRSg3nfdetJDC2diwLJTGosnG7sScTkGaFudrTf6iyCkTfUDjP2rXP7pR1o5Y8M4DuwLe3"),
		Base58.decode("zDLLXWRmL8qhrU9DaxTTG4xrLHgb7xLx5fVrC2NXjRaw2vhzB1PArtgqNe2kxp655saohUcWcsSZ8Bo218ByUzH"),
		//Base58.decode("585CPBAusjDWpx9jyx2S2hsHByTd52wofYB3vVd9SvgZqd3igYHSqpS2gWu2THxNevv4LNkk4RRiJDULvHahPRGr"),
		//Base58.decode("4xDHswuk5GsmHAeu82qysfdq9GyTxZ798ZQQGquprirrNBr7ACUeLZxBv7c73ADpkEvfBbhocGMhouM9y13sP8dK"),
		//Base58.decode("2Y81A7YjBji7NDKxYWMeNapSqFWFr8D4PSxBc4dCxSrCCVia6HPy2ZsezYKgeqZugNibAMra6DYT7NKCk6cSVUWX"),
		//Base58.decode("4drnqT2e8uYdhqz2TqscPYLNa94LWHhMZk4UD2dgjT5fLGMuSRiKmHyyghfMUMKreDLMZ5nCK2EMzUGz3Ggbc6W9")
		//Base58.decode("3t9wdnPfDdKxjxjTr7yhVq21ygX5oBMiJLkDFLxp4ZDwH7CiBA8vVcPAk455ec17xub71jYXNtxaMNe7KyERK97N")
		};

	public static final byte[][] VALID_RECORDS = new byte[][]{
		};


	public static HashSet<String> TRUSTED_FOR_ANONYMOUS_SEND = new HashSet<String>();
	public static HashSet<String> FOUNDATION_ADDRESSES = new HashSet<String>();
	public static HashMap<String, int[][]> FREEZED_BALANCES = new HashMap<String, int[][]>();
	public static HashMap<String, Pair<Integer, byte[]>> NOVA_ASSETS = new HashMap<String, Pair<Integer, byte[]>>();


	// CHAIN
	public static final int CONFIRMS_HARD = 3; // for reference by signature 
	// MAX orphan CHAIN
	public static final int CONFIRMS_TRUE = MAX_ORPHAN; // for reference by ITEM_KEY

	//TESTNET 
												//   1486444444444l
										   //	 1487844444444   1509434273     1509434273 
	public static final long DEFAULT_MAINNET_STAMP = DEVELOP_USE?1511164500000l:1487844793333l;

	//public static final int FEE_MIN_BYTES = 200;
	public static final int FEE_PER_BYTE = 64;
	public static final int FEE_SCALE = 8;
	public static final BigDecimal FEE_RATE = BigDecimal.valueOf(1, FEE_SCALE);
	public static final BigDecimal MIN_FEE_IN_BLOCK = BigDecimal.valueOf(FEE_PER_BYTE * 8 * 128, FEE_SCALE);
	public static final float FEE_POW_BASE = (float)1.5;
	public static final int FEE_POW_MAX = 6;
	public static final int ISSUE_MULT_FEE = 1<<10;
	public static final int ISSUE_ASSET_MULT_FEE = 1<<8; 
	//
	public static final int FEE_INVITED_DEEP = 4; // levels for deep
	public static final int FEE_INVITED_SHIFT = 5; // 2^5 = 64 - total FEE -> fee for Forger and fee for Inviter
	public static final int FEE_INVITED_SHIFT_IN_LEVEL = 3;
	public static final int FEE_FOR_ANONIMOUSE = 33;

	// issue PERSON
	//public static final BigDecimal PERSON_MIN_ERA_BALANCE = BigDecimal.valueOf(10000000).setScale(8);

	// SERTIFY
	// need RIGHTS for non PERSON account
	public static final BigDecimal MAJOR_ERA_BALANCE_BD = BigDecimal.valueOf(MAJOR_ERA_BALANCE).setScale(8);
	// need RIGHTS for PERSON account
	public static final BigDecimal MINOR_ERA_BALANCE_BD = BigDecimal.valueOf(MINOR_ERA_BALANCE).setScale(8);
	// GIFTS for R_SertifyPubKeys
	public static final int GIFTED_COMPU_AMOUNT = FEE_PER_BYTE<<8;
	public static final BigDecimal GIFTED_COMPU_AMOUNT_BD = BigDecimal.valueOf(GIFTED_COMPU_AMOUNT, FEE_SCALE);
	public static final int GIFTED_COMPU_AMOUNT_FOR_PERSON = GIFTED_COMPU_AMOUNT<<3;
	public static final BigDecimal GIFTED_COMPU_AMOUNT_FOR_PERSON_BD = BigDecimal.valueOf(GIFTED_COMPU_AMOUNT_FOR_PERSON, FEE_SCALE);

	static Logger LOGGER = Logger.getLogger(BlockChain.class.getName());
	private GenesisBlock genesisBlock;
	private long genesisTimestamp;

	private Block waitWinBuffer;
	//private int checkPoint = DEVELOP_USE?1:32400;
	public static final Tuple2<Integer, byte[]> CHECKPOINT = new Tuple2<Integer, byte[]>(36654,
			Base58.decode("4MhxLvzH3svg5MoVi4sX8LZYVQosamoBubsEbeTo2fqu6Fcv14zJSVPtZDuu93Tc7RuS2nPJDYycWjpvdSYdmm1W"));

	//private int target = 0;
	//private byte[] lastBlockSignature;
	//private Tuple2<Integer, Long> HWeight;


	//private DBSet dcSet;
	
	// dcSet_in = db() - for test
	public BlockChain(DCSet dcSet_in) throws Exception
	{	
		//CREATE GENESIS BLOCK
		genesisBlock = new GenesisBlock();
		genesisTimestamp = genesisBlock.getTimestamp(null);

		// GENERAL TRUST
		TRUSTED_FOR_ANONYMOUS_SEND.add("7R2WUFaS7DF2As6NKz13Pgn9ij4sFw6ymZ");

		// TIKER = KEY + CREATOR
		NOVA_ASSETS.put("BTC",
				new Pair<Integer, byte[]>(21, new Account("7JS4ywtcqrcVpRyBxfqyToS2XBDeVrdqZL").getShortBytes()));
		NOVA_ASSETS.put("@@USD",
				new Pair<Integer, byte[]>(95, new Account("7JS4ywtcqrcVpRyBxfqyToS2XBDeVrdqZL").getShortBytes()));
		NOVA_ASSETS.put("¤¤RUB",
				new Pair<Integer, byte[]>(93, new Account("7JS4ywtcqrcVpRyBxfqyToS2XBDeVrdqZL").getShortBytes()));
		NOVA_ASSETS.put("ERARUB",
				new Pair<Integer, byte[]>(91, new Account("7JS4ywtcqrcVpRyBxfqyToS2XBDeVrdqZL").getShortBytes()));
		NOVA_ASSETS.put("ERAUSD",
				new Pair<Integer, byte[]>(85, new Account("7JS4ywtcqrcVpRyBxfqyToS2XBDeVrdqZL").getShortBytes()));
		
		// TEST
		FOUNDATION_ADDRESSES.add("7F9cZPE1hbzMT21g96U8E1EfMimovJyyJ7");
		
		// ERACHAIN FUNDATION
		FOUNDATION_ADDRESSES.add("74a73pykkNwmuwkZdh5Lt2xTbK7anG5B6i");
		FOUNDATION_ADDRESSES.add("7QTDHp15vcHN3F4zP2BTcDXJkeotzQZkG4");
		FOUNDATION_ADDRESSES.add("7FiXN8VTgjMsLrZUQY9ZBFNfek7SsDP6Uc");
		FOUNDATION_ADDRESSES.add("74QcLxHgPkuMSPsKTh7zGpJsd5aAxpWpFA");
		FOUNDATION_ADDRESSES.add("7BAXHMTuk1vh6AiZU65oc7kFVJGqNxLEpt");
		FOUNDATION_ADDRESSES.add("7P3HR8kdj4ojXPvpTnEtVnpEwenipvrcH1");
		FOUNDATION_ADDRESSES.add("75Mb8cGchcG4DF31wavhNrnoycWsoLQqP4");
		FOUNDATION_ADDRESSES.add("75LzKAoxx4TgAAkpMRStve26YEY625TCRE");

		FOUNDATION_ADDRESSES.add("73QYndpFQeFvyMvwBcMUwJRDTp7XaxkSmZ"); // STOLEN
		FOUNDATION_ADDRESSES.add("7FJUV5GLMuVdopUHSwTLsjmKF4wkPwFEcG"); // LOSED
		FOUNDATION_ADDRESSES.add("75LK84g7JHoLG2jRUmbJA6srLrFkaXEU5A"); // FREEZED
		

		// TEST
		//FREEZED_BALANCES.put("7F9cZPE1hbzMT21g96U8E1EfMimovJyyJ7",
		//		new int[][]{{9000, 110000}, {3200, 90000}, {138000, 7000}, {547500, 5000}});

		// TEAM 2
		FREEZED_BALANCES.put("77QMFKSdY4ZsG8bFHynYdFNCmis9fNw5yP",
				new int[][]{{219000, 110000}, {328500, 90000}, {438000, 70000}, {547500, 50000}});
		FREEZED_BALANCES.put("7N7d8juuSSeEd92rkcEsfXhdi9WXE8zYXs",
				new int[][]{{219000, 110000}, {328500, 90000}, {438000, 70000}, {547500, 50000}});
		FREEZED_BALANCES.put("7LETj4cW4rLWBCN52CaXmzQDnhwkEcrv9G",
				new int[][]{{219000, 110000}, {328500, 90000}, {438000, 70000}, {547500, 50000}});

		// TEAM 3
		FREEZED_BALANCES.put("7GMENsugxjV8PToyUyHNUQF7yr9Gy6tJou",
				new int[][]{{219000, 150000}, {328500, 100000}, {438000, 70000}, {547500, 30000}});
		FREEZED_BALANCES.put("7DMJcs8kw7EXUSeEFfNwznRKRLHLrcXJFm",
				new int[][]{{219000, 150000}, {328500, 100000}, {438000, 70000}, {547500, 30000}});
		FREEZED_BALANCES.put("7QUeuMiWQjoQ3MZiriwhKfEG558RJWUUis",
				new int[][]{{219000, 150000}, {328500, 100000}, {438000, 70000}, {547500, 30000}});
		FREEZED_BALANCES.put("7MxscS3mS6VWim8B9K3wEzFAUWYbsMkVon",
				new int[][]{{219000, 140000}, {328500, 90000}, {438000, 60000}, {547500, 30000}});
		FREEZED_BALANCES.put("79NMuuW7thad2JodQ5mKxbMoyf1DjNT9Ap",
				new int[][]{{219000, 130000}, {328500, 90000}, {438000, 50000}, {547500, 20000}});
		FREEZED_BALANCES.put("7MhifBHaZsUcjgckwFN57bAE9fPJVDLDQq",
				new int[][]{{219000, 110000}, {328500, 80000}, {438000, 50000}, {547500, 20000}});
		FREEZED_BALANCES.put("7FRWJ4ww3VstdyAyKFwYfZnucJBK7Y4zmT",
				new int[][]{{219000, 100000}, {328500, 70000}, {438000, 40000}, {547500, 20000}});
		FREEZED_BALANCES.put("7FNAphtSYXtP5ycn88B2KEywuHXzM3XNLK",
				new int[][]{{219000, 90000}, {328500, 60000}, {438000, 30000}, {547500, 20000}});
		FREEZED_BALANCES.put("79ZVGgCFrQPoVTsFm6qCNTZNkRbYNsTY4u",
				new int[][]{{219000, 80000}, {328500, 60000}, {438000, 30000}, {547500, 20000}});
		
		// TEAM 1
		FREEZED_BALANCES.put("7Jhh3TPmfoLag8FxnJRBRYYfqnUduvFDbv",
				new int[][]{{219000, 200000}, {328500, 150000}, {438000, 100000}, {547500, 50000}});
		FREEZED_BALANCES.put("78JFPWVVAVP3WW7S8HPgSkt24QF2vsGiS5",
				new int[][]{{115000, 0}, {219000, 1000000}, {328500, 750000}, {438000, 500000}, {547500, 250000}});
		
		DCSet dcSet = dcSet_in;
		if (dcSet == null) {
			dcSet = DCSet.getInstance();
		}

		if(Settings.getInstance().isTestnet()) {
			LOGGER.info( ((GenesisBlock)genesisBlock).getTestNetInfo() );
		}
		
		int height = dcSet.getBlockMap().size();
		if(height == 0 )
		// process genesis block
		{
			if(dcSet_in == null && dcSet.getBlockMap().getLastBlockSignature() != null)
			{
				LOGGER.info("reCreate Database...");	
		
	        	try {
	        		dcSet.close();
	        		dcSet = Controller.getInstance().reCreateDC();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(),e);
				}
			}

        	//PROCESS
        	genesisBlock.process(dcSet);

        } else {
			if (!dcSet.getBlockSignsMap().contains(genesisBlock.getSignature()) ) {
				
				throw new Exception("wrong DB for GENESIS BLOCK");
			}

        }
		
		//lastBlockSignature = dcSet.getBlockMap().getLastBlockSignature();
		//HWeight = dcSet.getBlockSignsMap().get(lastBlockSignature);
		
	}
	
	public GenesisBlock getGenesisBlock() {
		return this.genesisBlock;
	}
	public long getGenesisTimestamp() {
		return this.genesisTimestamp;
	}
	public long getTimestamp(int height) {
		return this.genesisTimestamp + (long)height * GENERATING_MIN_BLOCK_TIME_MS;
	}
	public long getTimestamp(DCSet dcSet) {
		return this.genesisTimestamp + (long)getHeight(dcSet) * GENERATING_MIN_BLOCK_TIME_MS;
	}

	// BUFFER of BLOCK for WIN solving
	public Block getWaitWinBuffer() {
		return this.waitWinBuffer;
	}
	public void clearWaitWinBuffer() {
		this.waitWinBuffer = null;
	}
	public Block popWaitWinBuffer() {
		Block block = this.waitWinBuffer; 
		this.waitWinBuffer = null;
		return block;
	}

	public int compareNewWin(DCSet dcSet, Block block) {
		return this.waitWinBuffer == null?-1: this.waitWinBuffer.compareWin(block, dcSet);
	}

	// SOLVE WON BLOCK
	// 0 - unchanged;
	// 1 - changed, need broadcasting;
	public synchronized boolean setWaitWinBuffer(DCSet dcSet, Block block) {
				
		LOGGER.info("try set new winBlock: " + block.toString(dcSet));
		
		if (this.waitWinBuffer == null
				|| block.compareWin(waitWinBuffer, dcSet) > 0) {

			this.waitWinBuffer = block;

			LOGGER.info("new winBlock setted! Transactions: " + block.getTransactionCount());
			return true;
		}
		
		LOGGER.info("new winBlock ignored!");
		return false;
	}
	
	// 
	public static int getHeight(DCSet dcSet) {
		
		//GET LAST BLOCK
		///byte[] lastBlockSignature = dcSet.getBlockMap().getLastBlockSignature();
		///return dcSet.getBlockSignsMap().getHeight(lastBlockSignature);
		return dcSet.getBlockMap().size();
	}

	/*
	//public synchronized Tuple2<Integer, Long> getHWeight(DCSet dcSet, boolean withWinBuffer) {
	public Tuple2<Integer, Long> getHWeight(DCSet dcSet, boolean withWinBuffer) {
		
		if (dcSet.isStoped())
			return null;
		
		//GET LAST BLOCK
		byte[] lastBlockSignature = dcSet.getBlockMap().getLastBlockSignature();
		// test String b58 = Base58.encode(lastBlockSignature);
		
		int height;
		long weight;
		if (withWinBuffer && this.waitWinBuffer != null) {
			// with WIN BUFFER BLOCK
			height = 1;
			weight = this.waitWinBuffer.calcWinValueTargeted(dcSet);
		} else {
			height = 0;
			weight = 0l;				
		}
		
		if (lastBlockSignature == null) {
			height++;
		} else {
			height += dcSet.getBlockSignsMap().getHeight(lastBlockSignature);
			weight += dcSet.getBlockSignsMap().getFullWeight();
		}
		
		return  new Tuple2<Integer, Long>(height, weight);
		
	}
	*/
	
	public Tuple2<Integer, Long> getHWeightFull(DCSet dcSet) {
		Tuple2<Integer, Long> hWeight = dcSet.getBlockSignsMap().get(this.getLastBlockSignature(dcSet));
		if (hWeight == null || hWeight.a == -1) return new Tuple2<Integer, Long>(-1, -1L);

		/*
		if (hWeight.b < hWeight.a * 2000) {
			dcSet.getBlockSignsMap().recalcWeightFull(dcSet);
		}
		*/
		return new Tuple2<Integer, Long>(hWeight.a, dcSet.getBlockSignsMap().getFullWeight());
	}

	public long getFullWeight(DCSet dcSet) {
		
		return dcSet.getBlockSignsMap().getFullWeight();
	}

	public static int getCheckPoint(DCSet dcSet) {
		
		Tuple2<Integer, Long> item = dcSet.getBlockSignsMap().get(CHECKPOINT.b);
		if (item == null || item.a == -1)
			return 2;
		
		int heightCheckPoint = item.a;
		int dynamicCheckPoint = getHeight(dcSet) - BlockChain.MAX_ORPHAN;
				
		if (dynamicCheckPoint > heightCheckPoint)
			return dynamicCheckPoint;
		return heightCheckPoint;
	}
	
	/*
	public void setCheckPoint(int checkPoint) {
		
		if (checkPoint > 1)
			this.checkPoint = checkPoint;
	}
	*/
	
	public static int getNetworkPort() {
		if(Settings.getInstance().isTestnet()) {
			return BlockChain.TESTNET_PORT;
		} else {
			return BlockChain.MAINNET_PORT;
		}
	}

	public List<byte[]> getSignatures(DCSet dcSet, byte[] parentSignature) {
		
		//LOGGER.debug("getSignatures for ->" + Base58.encode(parent));
		
		List<byte[]> headers = new ArrayList<byte[]>();
		
		//CHECK IF BLOCK EXISTS
		Tuple2<Integer, Long> heightWT = dcSet.getBlockSignsMap().get(parentSignature);
		if(heightWT != null && heightWT.a > 0)
		{
			
			int packet;
			if (Arrays.equals(parentSignature, this.genesisBlock.getSignature())
					|| Arrays.equals(parentSignature, CHECKPOINT.b)) {
				packet = 3;
			} else {
				packet = SYNCHRONIZE_PACKET;
			}
			//BlockHeightsMap childsMap = dcSet.getBlockHeightsMap();
			BlockHeightsMap map = dcSet.getBlockHeightsMap();
			int counter = 0;
			int height = heightWT.a;
			while(parentSignature != null && counter++ < packet)
			{				
				headers.add(parentSignature);
				parentSignature = map.get(++height);
			}
			//LOGGER.debug("get size " + counter);
		} else if (Arrays.equals(parentSignature, this.CHECKPOINT.b)) {
			headers.add(parentSignature);			
		} else {
			//LOGGER.debug("*** getSignatures NOT FOUND !");
		}
		
		return headers;		
	}

	public Block getBlock(DCSet dcSet, byte[] header) {

		return dcSet.getBlockSignsMap().getBlock(header);
	}
	public Block getBlock(DCSet dcSet, int height) {

		return dcSet.getBlockMap().get(height);
	}

	public int isNewBlockValid(DCSet dcSet, Block block, Peer peer) {
		
		//CHECK IF NOT GENESIS
		if(block.getVersion() == 0 || block instanceof GenesisBlock)
		{
			LOGGER.debug("isNewBlockValid ERROR -> as GenesisBlock");
			return -100;
		}
		
		//CHECK IF SIGNATURE IS VALID
		if(!block.isSignatureValid())
		{
			LOGGER.debug("isNewBlockValid ERROR -> signature");
			return -200;
		}
		
		//int height = dcSet.getBlockHeightsMap().getSize();
		BlockSignsMap dbMap = dcSet.getBlockSignsMap();
		//byte[] lastSignature = dcSet.getBlockHeightsMap().last();
		byte[] lastSignature = dcSet.getBlockMap().getLastBlockSignature();

		byte[] newBlockReference = block.getReference();
		if(!Arrays.equals(lastSignature, newBlockReference)) {
			
			//CHECK IF WE KNOW THIS BLOCK
			if(dbMap.contains(block.getSignature()))
			{
				LOGGER.debug("isNewBlockValid IGNORE -> already in DB #" + block.getHeight(dcSet));
				return 3;
			}

			Block lastBlock = dcSet.getBlockMap().last();
			if(Arrays.equals(lastBlock.getReference(), block.getReference())) {
				// CONCURENT for LAST BLOCK
				if (block.calcWinValue(dcSet) > lastBlock.calcWinValue(dcSet)) {
					LOGGER.debug("isNewBlockValid -> reference to PARENT last block >>> TRY WIN");
					return 4;
				} else {
					LOGGER.debug("isNewBlockValid -> reference to PARENT last block >>> weak...");
					peer.sendMessage(MessageFactory.getInstance().createWinBlockMessage(lastBlock));
					return -4;
				}
			}
			
			Block winBlock = this.getWaitWinBuffer();
			if (winBlock == null) {
				LOGGER.debug("isNewBlockValid ERROR -> reference NOT to last block AND win BLOCK is NULL");			
				return -6;
			}
			
			if (Arrays.equals(winBlock.getSignature(), newBlockReference)) {
				// this new block for my winBlock + 1 Height
				if (this.getTimestamp(dcSet) + BlockChain.GENERATING_MIN_BLOCK_TIME_MS - (GENERATING_MIN_BLOCK_TIME_MS>>8) > NTP.getTime()) {
					// BLOCK from FUTURE
					LOGGER.debug("isNewBlockValid ERROR -> reference to WIN block in BUFFER and IT from FUTURE");
					return -5;
				} else {
					// LETS FLUSH winBlock and set newBlock as waitWIN
					LOGGER.debug("isNewBlockValid ERROR -> reference to WIN block in BUFFER try SET IT");
					return 5;
				}
			}
			
			LOGGER.debug("isNewBlockValid ERROR -> reference NOT to last block -9");			
			return -7;
		}
		
		return 0;
	}
	
	public Pair<Block, List<Transaction>> scanTransactions(DCSet dcSet, Block block, int blockLimit, int transactionLimit, int type, int service, Account account) 
	{	
		//CREATE LIST
		List<Transaction> transactions = new ArrayList<Transaction>();
		int counter = 0;
		
		//IF NO BLOCK START FROM GENESIS
		if(block == null)
		{
			block = new GenesisBlock();
		}
		
		//START FROM BLOCK
		int scannedBlocks = 0;
		do
		{		
			//FOR ALL TRANSACTIONS IN BLOCK
			for(Transaction transaction: block.getTransactions())
			{
				//CHECK IF ACCOUNT INVOLVED
				if(account != null && !transaction.isInvolved(account))
				{
					continue;
				}
				
				//CHECK IF TYPE OKE
				if(type != -1 && transaction.getType() != type)
				{
					continue;
				}
				
				//CHECK IF SERVICE OKE
				if(service != -1 && transaction.getType() == Transaction.ARBITRARY_TRANSACTION)
				{
					ArbitraryTransaction arbitraryTransaction = (ArbitraryTransaction) transaction;
					
					if(arbitraryTransaction.getService() != service)
					{
						continue;
					}
				}
				
				//ADD TO LIST
				transactions.add(transaction);
				counter++;
			}
			
			//SET BLOCK TO CHILD
			block = block.getChild(dcSet);
			scannedBlocks++;
		}
		//WHILE BLOCKS EXIST && NOT REACHED TRANSACTIONLIMIT && NOT REACHED BLOCK LIMIT
		while(block != null && (counter < transactionLimit || transactionLimit == -1) && (scannedBlocks < blockLimit || blockLimit == -1)); 
		
		//CHECK IF WE REACHED THE END
		if(block == null)
		{
			block = this.getLastBlock(dcSet);
		}
		else
		{
			block = block.getParent(dcSet);
		}
		
		//RETURN PARENT BLOCK AS WE GET CHILD RIGHT BEFORE END OF WHILE
		return new Pair<Block, List<Transaction>>(block, transactions);
	}
	
	public Block getLastBlock(DCSet dcSet) 
	{	
		return dcSet.getBlockMap().last();
	}
	public byte[] getLastBlockSignature(DCSet dcSet) 
	{	
		return dcSet.getBlockMap().getLastBlockSignature();
	}

	// get last blocks for target
	public List<Block> getLastBlocksForTarget_old(DCSet dcSet) 
	{	

		Block last = dcSet.getBlockMap().last();
		
		/*
		if (this.lastBlocksForTarget != null
				&& Arrays.equals(this.lastBlocksForTarget.get(0).getSignature(), last.getSignature())) {
			return this.lastBlocksForTarget;
		}
		*/
		
		List<Block> list =  new ArrayList<Block>();

		if (last == null || last.getVersion() == 0) {
			return list;
		}

		for (int i=0; i < TARGET_COUNT && last.getVersion() > 0; i++) {
			list.add(last);
			last = last.getParent(dcSet);
		}
		
		return list;
	}
	
	// ignore BIG win_values
	public static long getTarget(DCSet dcSet, Block block)
	{
		
		if (block == null)
			return 1000l;
		/*
		int height = block.getParentHeight(dcSet);
		if (block.getTargetValue() > 0)
			return block.getTargetValue();
		 */
		
		long min_value = 0;
		long win_value = 0;
		Block parent = block.getParent(dcSet);
		int i = 0;
		long value = 0;
		
		while (parent != null && parent.getVersion() > 0 && i < BlockChain.TARGET_COUNT)
		{
			i++;
			value = parent.calcWinValue(dcSet);
			if (min_value==0
					|| min_value > value) {
				min_value = value;
			}
			
			if (min_value + (min_value<<1) < value)
				value = min_value + (min_value<<1);

			parent = parent.getParent(dcSet);
		}
		
		if (i == 0) {
			return block.calcWinValue(dcSet);
		}		

		int height = block.getHeightByParent(dcSet);
		min_value = min_value<<1;

		parent = block.getParent(dcSet);
		i = 0;
		while (parent != null && parent.getVersion() > 0 && i < BlockChain.TARGET_COUNT)
		{
			i++;
			value = parent.calcWinValue(dcSet);
			if (height > TARGET_COUNT && min_value < value)
				value = min_value;
			win_value += value;
			
			parent = parent.getParent(dcSet);
		}

		long target = win_value / i;
		
		return target;
	}

	// calc Target by last blocks in chain
	public long getTarget(DCSet dcSet)
	{	
		return getTarget(dcSet, this.getLastBlock(dcSet));
	}

	// GET MIN TARGET
	// TODO GENESIS_CHAIN
	// SEE core.block.Block.calcWinValue(DBSet, Account, int, int)
	public static int getMinTarget(int height) {
		int base;
		if ( height < BlockChain.REPEAT_WIN)
			// FOR not repeated WINS - not need check BASE_TARGET
			/////base = BlockChain.BASE_TARGET>>1;
			base = BlockChain.BASE_TARGET - (BlockChain.BASE_TARGET>>2); // ONLY UP
		else if (DEVELOP_USE)
			base = BlockChain.BASE_TARGET >>1;
		else if ( height < BlockChain.TARGET_COUNT)
			base = (BlockChain.BASE_TARGET>>1) + (BlockChain.BASE_TARGET>>2);
		else if ( height < BlockChain.TARGET_COUNT <<5)
			base = (BlockChain.BASE_TARGET>>1) + (BlockChain.BASE_TARGET>>3);
		else if ( height < 32100)
			base = (BlockChain.BASE_TARGET>>1) + (BlockChain.BASE_TARGET>>4);
		else if ( height < 105000)
			base = (BlockChain.BASE_TARGET>>1) - (BlockChain.BASE_TARGET>>4);
		else
			base = (BlockChain.BASE_TARGET>>1) + (BlockChain.BASE_TARGET>>4);

		return base;

	}

	// CLEAR UNCONFIRMED TRANSACTION from Invalid and DEAD
	public void clearUnconfirmedRecords(Controller ctrl, DCSet dcSetOriginal) {
		
		if (true)
			return;
		
		long startTime = System.currentTimeMillis();

		long timestamp = GENERATING_MIN_BLOCK_TIME_MS + this.getTimestamp(dcSetOriginal);

		TransactionMap unconfirmedMap = dcSetOriginal.getTransactionMap();
		Iterator<byte[]> iterator = unconfirmedMap.getIterator(0, false);

		//CREATE FORK OF GIVEN DATABASE
		///DCSet dcFork = dcSetOriginal.fork();
		Transaction transaction;
		byte[] key;

		while(iterator.hasNext())
		{
							
			if (ctrl.isOnStopping()) {
				return;
			}

			key = iterator.next();
			transaction = unconfirmedMap.get(key);
			
			//CHECK TRANSACTION DEADLINE
			if(transaction.getDeadline() < timestamp) {
				unconfirmedMap.delete(key);
				continue;
			}
			
			//CHECK IF VALID
			if(!transaction.isSignatureValid(DCSet.getInstance())) {
				// INVALID TRANSACTION
				unconfirmedMap.delete(key);
                continue;
			}
				
			/*
				transaction.setDB(dcFork, false);
				
				if (transaction.isValid(dcFork, null) != Transaction.VALIDATE_OK) {
					// INVALID TRANSACTION
					unconfirmedMap.delete(transaction);
					continue;
				}
			}
			*/
		}
		
		LOGGER.debug("timerUnconfirmed ----------------  work: "
				+ (System.currentTimeMillis() - startTime)
				+ " new SIZE: " + dcSetOriginal.getUncTxCounter());

	}
}
