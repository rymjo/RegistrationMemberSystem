package microvone.de.barcode;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import microvone.de.database.DbHelper;
import microvone.de.database.RegistrationColumns;
import microvone.de.registrationmembersystem.R;

import static microvone.de.utils.Status.SAVE;

/**
 * Created by Jörn on 10.09.2017.
 */

public class BarcodeAddTask extends AsyncTask<ArrayList<BarcodeItem>, Void, String> {

    private final String TAG = BarcodeAddTask.class.getSimpleName();

    private ProgressDialog dialog;
    private Context context;
    private long idCategory;
    private BarcodeListAdapter adapter;
    private ArrayList<BarcodeItem> barcodeItems;

    public BarcodeAddTask(Context context, final long idCategory) {
        this.context = context;
        this.idCategory = idCategory;
        dialog = new ProgressDialog(context);
    }


    @Override
    protected String doInBackground(ArrayList<BarcodeItem>... params) {
        String msg =  context.getString(R.string.txt_saving_successfull);            barcodeItems = params[0];

        if(params != null) {
            Log.i(TAG, "Save params!" + barcodeItems);
            boolean error = saveData(barcodeItems);
            if(error) {
                msg = context.getString(R.string.txt_error_saving);
            }
        }

        return msg;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getString(R.string.txt_progressdialogmessage));
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog.isShowing())
            dialog.dismiss();
        Log.i(TAG, "Save Items!" + result);
        if (result != null && result.length() > 0) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            barcodeItems.clear();
            adapter.notifyDataSetChanged();
        } else{
            Toast.makeText(context, context.getString(R.string.txt_not_save_data), Toast.LENGTH_LONG).show();
        }

    }

    private boolean saveData(ArrayList<BarcodeItem> barcodeItems) {
        boolean error = false;
        DbHelper db = DbHelper.getInstance(context);
        SQLiteDatabase dbm = db.getWritableDatabase();
        dbm.beginTransaction();
        try {
            for (BarcodeItem item : barcodeItems) {
                Log.i(TAG, item.getCode());
                Log.i(TAG, item.formatCurrentDate());

                ContentValues cv = new ContentValues();
                cv.put(RegistrationColumns.CODE,item.getCode());
                cv.put(RegistrationColumns.REG_DATE,item.formatCurrentDate());
                cv.put(RegistrationColumns.STATUS, SAVE.toString());
                cv.put(RegistrationColumns.ID_CATEGORY, idCategory);
                dbm.insertOrThrow(RegistrationColumns.TABLE, null, cv);
            }
            dbm.setTransactionSuccessful();
        } catch(SQLiteException sqle) {
            Log.e(TAG, "Error by Saving...", sqle);
            error = true;
        } finally {
            dbm.endTransaction();
        }

        return error;
    }

    public void setAdapter(BarcodeListAdapter adapter) {
        this.adapter = adapter;
    }
}
