package microvone.de.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by jörn on 08.01.2018.
 */

public class SqliteLoader extends AsyncTaskLoader<Cursor> {

    private Cursor lastCursor = null;

    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;
    private String mTable;
    private String[] mColumns;
    private SQLiteDatabase mDb;

    public SqliteLoader(Context context, SQLiteDatabase db, String table, String[] columns,
                        String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy) {
        super(context);
        if( db == null ) {
            throw new IllegalArgumentException("db must not be null");
        }
        if( table == null ) {
            throw new IllegalArgumentException("table must not be null");
        }
        mDb = db;
        mTable = table;
        mColumns = columns;
        mGroupBy = groupBy;
        mHaving = having;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mOrderBy = orderBy;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        this.mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.mSelectionArgs = selectionArgs;
    }

    public String getGroupBy() {
        return mGroupBy;
    }

    public void setGroupBy(String groupBy) {
        this.mGroupBy = groupBy;
    }

    public String getHaving() {
        return mHaving;
    }

    public void setHaving(String having) {
        this.mHaving = having;
    }

    public String getOrderBy() {
        return mOrderBy;
    }

    public void setOrderBy(String orderBy) {
        this.mOrderBy = orderBy;
    }

    public String getTable() {
        return mTable;
    }

    public void setTable(String table) {
        this.mTable = table;
    }

    public String[] getColumns() {
        return mColumns;
    }

    public void setColumns(String[] columns) {
        this.mColumns = columns;
    }

    /**
     * Fuehrt die DB Query aus.
     * @return Cursor auf Ergebnismenge. null, wenn nichts gefunden.
     */
    @Override
    public Cursor loadInBackground() {
        Log.i("SqliteLoader","Run query in thread: "+Thread.currentThread().toString());
        Log.i("SqliteLoader","Start slow query");
        Cursor res =  mDb.query(mTable, mColumns, mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy);
        Log.i("SqliteLoader","Done query");

/*    if (res != null) {
      res.getCount();
      Log.i("SqliteLoader", "Done count()");
    }
*/
        return res;
    }


    /**
     * Runs on the UI thread, routing the results from the
     * background thread to whatever is using the Cursor
     * (e.g., a CursorAdapter).
     */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        Cursor oldCursor = lastCursor;
        lastCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Starts an asynchronous load of the list data.
     * When the result is ready the callbacks will be called
     * on the UI thread. If a previous load has been completed
     * and is still valid the result may be passed to the
     * callbacks immediately.
     * <p/>
     * Must be called from the UI thread.
     */
    @Override
    protected void onStartLoading() {
        if (lastCursor != null) {
            deliverResult(lastCursor);
        }

        if (takeContentChanged() || lastCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread, triggered by a
     * call to stopLoading().
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Must be called from the UI thread, triggered by a
     * call to cancel(). Here, we make sure our Cursor
     * is closed, if it still exists and is not already closed.
     */
    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    /**
     * Must be called from the UI thread, triggered by a
     * call to reset(). Here, we make sure our Cursor
     * is closed, if it still exists and is not already closed.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (lastCursor != null && !lastCursor.isClosed()) {
            lastCursor.close();
        }

        lastCursor = null;
    }
}
