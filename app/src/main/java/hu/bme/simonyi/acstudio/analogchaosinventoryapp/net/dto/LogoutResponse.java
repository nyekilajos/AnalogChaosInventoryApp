package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;

/**
 * Response class for logout.
 *
 * @author Lajos Nyeki
 */
public class LogoutResponse extends GenericServerResponse<Void> {

    public static final Creator<LogoutResponse> CREATOR = new Creator<LogoutResponse>() {
        @Override
        public LogoutResponse createFromParcel(Parcel source) {
            return new LogoutResponse(source);
        }

        @Override
        public LogoutResponse[] newArray(int size) {
            return new LogoutResponse[0];
        }
    };

    public LogoutResponse() {
    }

    public LogoutResponse(Parcel in) {
        readCommonDataFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeCommonDataToParcel(dest);
    }
}
