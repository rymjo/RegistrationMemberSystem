package microvone.de.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import microvone.de.utils.CSVUtils;
import microvone.de.utils.FileUtils;

/**
 * Created by jörn on 08.01.2018.
 */

public class ExportFileTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = ExportFileTask.class.getSimpleName();
    private List<ExportCSVItem> values;
    private final ProgressDialog dialog;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy_hhMMss");

    public ExportFileTask(Context ctx, ArrayList<ExportCSVItem> values) {
        this.values = values;
        this.dialog = new ProgressDialog(ctx);
    }

    @Override
    protected String doInBackground(Void... params) {
        if(values != null && FileUtils.isExternalStorageWritable()) {
            Date now = new Date();
            String fileName = "export_registration_" + simpleDateFormat.format(now) + ".csv";
            File f = new File(FileUtils.getExternalDownloadFile(), fileName);
            if(!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                }
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(f);
                for(ExportCSVItem item : values) {
                    CSVUtils.writeLine(writer, item.getValuesPerLine());
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;
    }


    // to show Loading dialog box
    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting result...");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

    }
}
