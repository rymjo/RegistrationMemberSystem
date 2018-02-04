package microvone.de.barcode;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import microvone.de.registrationmembersystem.R;


/**
 * Created by Jörn on 02.09.2017.
 */

public class BarcodeListAdapter extends BaseAdapter{

    private final Activity context;
    private final ArrayList<BarcodeItem> barcodeItems;

    static class ViewHolder {
        public TextView text_code;
        public TextView text_date;
    }

    public BarcodeListAdapter(Activity context,  ArrayList<BarcodeItem> barcodeItems) {
        this.context = context;
        this.barcodeItems = barcodeItems;
    }

    @Override
    public int getCount() {
        return barcodeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return barcodeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse view
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.barcode_list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text_code = (TextView) rowView.findViewById(R.id.txt_barcode_item_id);
            viewHolder.text_date =  (TextView) rowView.findViewById(R.id.txt_barcode_item_date);
           // viewHolder.btn_remove_item = (Button) rowView.findViewById(R.ID.btn_remove_item);

            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        BarcodeItem  barcodeItem = barcodeItems.get(position);
        String barcodeId = barcodeItem.getCode();
        String currentDate = barcodeItem.formatCurrentDate();
        holder.text_code.setText(barcodeId);
        holder.text_date.setText(currentDate);


       /* holder.btn_remove_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");

                Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
            }
        });
        */

        return rowView;
    }
}
