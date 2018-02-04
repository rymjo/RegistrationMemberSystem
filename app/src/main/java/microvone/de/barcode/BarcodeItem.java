package microvone.de.barcode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jörn on 02.09.2017.
 */

public class BarcodeItem  {

    private final Date currentDate;
    private final String code;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");

    private BarcodeItem(Builder builder){
        this.code = builder.code;
        this.currentDate = builder.currentDate;
    }

    public String getCode() {
        return code;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public String formatCurrentDate() {
        return dateFormatter.format(currentDate);
    }


    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof BarcodeItem)) {
            return false;
        }

        BarcodeItem user = (BarcodeItem) o;
        return user.code.equals(code);
    }

    //Idea from effective Java : Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + code.hashCode();
        return result;
    }



    /**
     * Static builder class
     */
    public static class Builder {

        private final String code;
        private final Date currentDate;

        public Builder(String code, Date currentDate) {
            this.code = code;
            this.currentDate = currentDate;
        }

        public BarcodeItem build() {
            return new BarcodeItem(this);
        }

    }

}
