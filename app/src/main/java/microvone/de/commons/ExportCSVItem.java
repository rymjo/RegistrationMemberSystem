package microvone.de.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jörn on 08.01.2018.
 */

public class ExportCSVItem {
    private List<String> valuesPerLine;

    public ExportCSVItem(ArrayList<String> valuesPerLine) {
        this.valuesPerLine = valuesPerLine;
    }


    public List<String> getValuesPerLine() {
        return valuesPerLine;
    }

    public void setValuesPerLine(List<String> valuesPerLine) {
        this.valuesPerLine = valuesPerLine;
    }
}
