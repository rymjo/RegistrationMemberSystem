package microvone.de.database;

/**
 * Created by jörn on 08.01.2018.
 */

public interface RegistrationColumns {

    String ID = "_id";

    String CODE = "code";
    String REG_DATE = "regdate";
    String STATUS = "status";
    String ID_CATEGORY = "idcategory";

    String TABLE = "Registration";
    String[] COLUMNS = new String[] {ID,CODE,REG_DATE,STATUS,ID_CATEGORY};
    String DEFAULT_ORDER_BY = CODE;
}
