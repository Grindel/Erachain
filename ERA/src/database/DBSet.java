package database;
// 30/03 ++

import org.mapdb.DB;
import org.mapdb.DBMaker;
import settings.Settings;

import java.io.File;

//import org.mapdb.Serializer;

public class DBSet implements IDB {
    private static final File DATA_FILE = new File(Settings.getInstance().getLocalDir(), "data.dat");

    private static final String VERSION = "version";

    private static DB database;
    private static int uses;
    private static DBSet instance;
    private PeerMap peerMap;

    private DBSet() {
        this.peerMap = new PeerMap(this, this.database);
    }

    public static boolean exists() {
        return DATA_FILE.exists();
    }

    public static DBSet getinstanse() {
        if (instance == null) {
            reCreateDatabase();
        }

        return instance;

    }

    public static void reCreateDatabase() {

        //OPEN DB
        //OPEN WALLET
        DATA_FILE.getParentFile().mkdirs();

        database = DBMaker.newFileDB(DATA_FILE)
                .closeOnJvmShutdown()
                //.cacheDisable()
                .checksumEnable()
                .mmapFileEnableIfSupported()
                /// ICREATOR
                .commitFileSyncDisable()
                .transactionDisable()
                .make();

        uses = 0;

        //CREATE INSTANCE
        instance = new DBSet();


    }

    public PeerMap getPeerMap() {
        return this.peerMap;
    }

    public int getVersion() {
        this.uses++;
        int u = this.database.getAtomicInteger(VERSION).intValue();
        this.uses--;
        return u;
    }

    public void setVersion(int version) {
        this.uses++;
        this.database.getAtomicInteger(VERSION).set(version);
        this.uses--;
    }

    public void addUses() {
        this.uses++;

    }

    public void outUses() {
        this.uses--;
    }

    public boolean isBusy() {
        if (this.uses > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void commit() {
        this.uses++;
        this.database.commit();
        this.uses--;

    }

    public void close() {
        if (this.database != null) {
            if (!this.database.isClosed()) {
                this.uses++;
                this.database.commit();
                this.database.close();
                this.uses--;

            }
        }
    }
}