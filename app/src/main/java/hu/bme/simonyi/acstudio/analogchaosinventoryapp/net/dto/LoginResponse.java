package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;

/**
 * Response DTO object of the login request. The result contains the session ID.
 *
 * @author Lajos Nyeki
 */
public class LoginResponse extends GenericServerResponse<String> {

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    public LoginResponse() {
    }

    private LoginResponse(Parcel in) {
        readCommonDataFromParcel(in);
        setResult(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeCommonDataToParcel(dest);
        dest.writeString(getResult());
    }
}
