package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Request class fro logout
 *
 * @author Lajos Nyeki
 */
public class LogoutRequest implements Parcelable {

    private String session;

    public static final Creator<LogoutRequest> CREATOR = new Creator<LogoutRequest>() {
        @Override
        public LogoutRequest createFromParcel(Parcel source) {
            return new LogoutRequest(source);
        }

        @Override
        public LogoutRequest[] newArray(int size) {
            return new LogoutRequest[size];
        }
    };

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public LogoutRequest(String session) {
        this.session = session;
    }

    public LogoutRequest(Parcel in) {
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
