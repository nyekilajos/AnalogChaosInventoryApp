package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter class for converting between ForeignCollection and List.
 *
 * @author Lajos Nyeki
 */
public class ForeignCollectionConverter<T> {

    /**
     * Converts from List to ForeignCollection.
     *
     * @param list      List object to be converted.
     * @param fieldName Filed name of the ForeignCollection object used in the database model class.
     * @param dao       DAO class for creating an empty ForeignCollection object
     * @return The converted ForeignCollection object
     * @throws SQLException
     */
    public ForeignCollection<T> convertToForegnCollection(List<T> list, String fieldName, Dao<T, Integer> dao) throws SQLException {
        ForeignCollection<T> foreignCollection = dao.getEmptyForeignCollection(fieldName);
        foreignCollection.addAll(list);
        return foreignCollection;
    }

    /**
     * Converts from ForeignCollection to List.
     *
     * @param foreignCollection ForeignCollection object to be converted
     * @return The converted List obejct
     */
    public List<T> convertToList(ForeignCollection<T> foreignCollection) {
        return new ArrayList<>(foreignCollection);
    }
}
