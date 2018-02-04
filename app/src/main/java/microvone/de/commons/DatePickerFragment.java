package microvone.de.commons;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jörn on 08.01.2018.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";

    private EditText editText;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d(TAG, "Year: "+view.getYear()+" Month: "+view.getMonth()+" Day: "+view.getDayOfMonth());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, view.getYear());
        c.set(Calendar.MONTH, view.getMonth());
        c.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());

        if(editText != null) {
            editText.setText(simpleDateFormat.format(c.getTime()));
        }

    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
