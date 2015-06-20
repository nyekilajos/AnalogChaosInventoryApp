package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Request class for getting the inventory items list from the server.
 *
 * @author Lajos Nyeki
 */
public class ItemsListRequest implements Parcelable {

    @Expose
    private String session;

    public static final Creator<ItemsListRequest> CREATOR = new Creator<ItemsListRequest>() {
        @Override
        public ItemsListRequest createFromParcel(Parcel source) {
            return new ItemsListRequest(source);
        }

        @Override
        public ItemsListRequest[] newArray(int size) {
            return new ItemsListRequest[size];
        }
    };

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public ItemsListRequest(String session) {
        this.session = session;
    }

    public ItemsListRequest(Parcel in) {
        session = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(session);
    }
}