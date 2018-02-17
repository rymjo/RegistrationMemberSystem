package microvone.de.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import microvone.de.registrationmembersystem.R;

/**
 * Created by jörn on 08.01.2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static interface ISQLiteActivity{}

    private static final String TAG = DbHelper.class.getSimpleName();

    // Table and column declaration

    public static final int VERSION = 1;

    public static final String DATABASE = "barcodesanner.db";

    private static DbHelper dbHelper;
    private Context context;

    private DbHelper(Context context) {
        super(context, DATABASE, null, VERSION);
        this.context = context;
    }

    public static synchronized DbHelper getInstance(Context context) {
        if(dbHelper == null) {
            dbHelper = new DbHelper(context);
        }

        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "oncreate");
        sql(db, R.raw.db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrade tables: " + oldVersion + " old - new: " + newVersion);
       /* if (oldVersion < 2) {
            sql(db, R.raw.awb_update);
        }*/
    }


    private void sql(SQLiteDatabase db, int id) {
        Log.i(TAG, "sql");

        try (
                InputStream in = context.getResources().openRawResource(id);
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader buf = new BufferedReader(reader)
        ) {

            String line;
            while ((line = buf.readLine()) != null) {
                Log.i("DatabaseManager", "line: " + line);
                db.execSQL(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error to read sql script", e);
        }
    }
}
