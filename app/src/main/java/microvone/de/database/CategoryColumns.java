package microvone.de.database;

/**
 * Created by jörn on 08.01.2018.
 */

public interface CategoryColumns {

    String TABLE = "Category";
    String ID = "_id";
    String NAME = "name";
    String[] COLUMNS = new String[] {ID, NAME};
    String DEFAULT_ORDER_BY = NAME;
}
