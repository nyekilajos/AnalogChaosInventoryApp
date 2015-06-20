package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Helper class for performing CRUD operations on the CouachbaseLite Database.
 *
 * @author Lajos Nyeki
 */
@Singleton
public class CouchbaseLiteHelper {

    private static final String ITEMS_DATABASE_NAME = "items_couchbase_db";

    private Context context;
    private LocalSettingsService localSettingsService;
    private Database database;

    @Inject
    public CouchbaseLiteHelper(Context context, LocalSettingsService localSettingsService) throws IOException, CouchbaseLiteException {
        this.context = context;
        this.localSettingsService = localSettingsService;
        initDocument();
    }

    private void initDocument() throws IOException, CouchbaseLiteException {
        Manager couchbaseManager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        database = couchbaseManager.getDatabase(ITEMS_DATABASE_NAME);
    }

    /**
     * Writes Items to the database and deletes the items already there.
     *
     * @param itemsList The list of Items to be stored in the database.
     * @throws CouchbaseLiteException Thrown when database can not be accessed or data can not be written.
     */
    public void writeItemsToDb(List<Item> itemsList) throws CouchbaseLiteException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<String> documentIds = new HashSet<>();
        for (Item item : itemsList) {
            String docId = Integer.toString(item.getId());
            Document document = database.getDocument(docId);
            document.purge();
            document.putProperties(objectMapper.convertValue(item, Map.class));
            documentIds.add(docId);
        }
        localSettingsService.setItemsCouchbaseLiteDocumentIds(documentIds);
    }

    /**
     * Returns the Items from the database.
     *
     * @return The list of the Items in the database.
     * @throws CouchbaseLiteException
     */
    public List<Item> getItemsList() throws CouchbaseLiteException {
        List<Item> items = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Set<String> documentIds = localSettingsService.getItemsCouchbaseLiteDocumentIds();
        for (String docId : documentIds) {
            items.add(objectMapper.convertValue(database.getDocument(docId).getUserProperties(), Item.class));
        }
        return items;
    }
}
