package microvone.de.database;

/**
 * Created by jörn on 08.01.2018.
 */

public interface CategoryColumns {

    String TABLE = "Category";
    String ID = "_id";
    String NAME = "name";
    String ID_CLUB = "idclub";

    String[] COLUMNS = new String[] {ID, NAME, ID_CLUB};
    String DEFAULT_ORDER_BY = NAME;
}
