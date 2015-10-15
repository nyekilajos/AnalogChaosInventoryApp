package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.Logger;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.LoggerFactory;

/**
 * Helper class for connection ORM Lite with SQLite and creating DAO object.
 *
 * @author Lajos Nyeki
 */
public class ACOrmLiteSqliteOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final Logger LOGGER = LoggerFactory.createLogger(ACOrmLiteSqliteOpenHelper.class);

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Item, Integer> itemsDao = null;

    @Inject
    public ACOrmLiteSqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Item.class);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //There was no release yet, only one schema exists for the database
    }

    /**
     * Returns a Dao class that gives access the the database
     *
     * @return Dao class
     * @throws SQLException
     */
    public Dao<Item, Integer> getDao() throws SQLException {
        if (itemsDao == null) {
            itemsDao = getDao(Item.class);
        }
        return itemsDao;
    }

    @Override
    public void close() {
        super.close();
        itemsDao = null;
    }
}
