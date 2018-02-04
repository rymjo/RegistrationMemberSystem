package microvone.de.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jörn on 08.01.2018.
 */

public class Registration implements Parcelable {

    private long id;
    private String code;
    private String regdate;
    private String status;
    private String categoryName;


    /**
     * Default constructor
     */
    public Registration() {
        super();
    }

    private Registration(final Parcel in) {
        readFromParcel(in);
    }

    /**
     *
     */
    public static final Parcelable.Creator<Registration> CREATOR = new Parcelable.Creator<Registration>() {

        @Override
        public Registration createFromParcel(Parcel source) {
            return new Registration(source);
        }

        @Override
        public Registration[] newArray(int size) {
            return new Registration[size];
        }

    };

    /**
     * @param in
     */
    public void readFromParcel(final Parcel in) {
        code = in.readString();
        regdate = in.readString();
        status = in.readString();
        categoryName = in.readString();
    }

    /**
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(regdate);
        dest.writeString(status);
        dest.writeString(categoryName);
    }

    // +++  GET AND SETTER

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return registration date
     */
    public String getRegdate() {
        return regdate;
    }

    /**
     * @param regdate registration date in yyyy-MM-dd HH:mm:ss
     */
    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setSend(String status) {
        this.status = status;
    }

    /**
     * @return
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "ID='" + id + '\'' +
                ", code='" + code + '\'' +
                ", regdate='" + regdate + '\'' +
                ", status='" + status + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
