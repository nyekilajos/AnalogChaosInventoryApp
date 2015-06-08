package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.task;

import android.content.Context;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.ACOrmLiteSqliteOpenHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.ForeignCollectionConverter;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.Logger;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.LoggerFactory;
import roboguice.util.RoboAsyncTask;

/**
 * Async task for updating the inventory database
 *
 * @author Lajos Nyeki
 */
public class UpdateItemsTableTask extends RoboAsyncTask<Void> {

    private static final Logger LOGGER = LoggerFactory.createLogger(UpdateItemsTableTask.class);

    @Inject
    private ACOrmLiteSqliteOpenHelper acOrmLiteSqliteOpenHelper;
    @Inject
    private ForeignCollectionConverter<Item> converter;

    private List<Item> items;

    @Inject
    protected UpdateItemsTableTask(Context context) {
        super(context);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public Void call() throws Exception {
        Dao<Item, Integer> itemsDao = acOrmLiteSqliteOpenHelper.getDao();
        try {
            convertToForegnCollection(itemsDao);
            deleteOldData(itemsDao);
            insertNewData(itemsDao);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        } finally {
            acOrmLiteSqliteOpenHelper.close();
        }

        return null;
    }

    private void convertToForegnCollection(Dao<Item, Integer> itemsDao) throws SQLException {
        for (Item item : items) {
            if (item.getChildren() != null) {
                item.setChildrenItems(converter.convertToForegnCollection(item.getChildren(), "childrenItems", itemsDao));
            }
        }
    }

    private void insertNewData(Dao<Item, Integer> itemsDao) throws java.sql.SQLException {
        for (Item item : items) {
            itemsDao.create(item);
        }
    }

    private void deleteOldData(Dao<Item, Integer> itemsDao) throws java.sql.SQLException {
        List<Item> oldItems = itemsDao.queryForAll();
        for (Item item : oldItems) {
            itemsDao.delete(item);
        }
    }


}
