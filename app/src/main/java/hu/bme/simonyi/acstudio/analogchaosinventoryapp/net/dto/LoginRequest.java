package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * DTO class for login request
 *
 * @author Lajos Nyeki
 */
public class LoginRequest implements Parcelable {
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String rememberme;

    public static final Creator<LoginRequest> CREATOR = new Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };

    public LoginRequest(String email, String password, String rememberme) {
        this.email = email;
        this.password = password;
        this.rememberme = rememberme;
    }

    public LoginRequest(Parcel in) {
        email = in.readString();
        password = in.readString();
        rememberme = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberme() {
        return rememberme;
    }

    public void setRememberme(String rememberme) {
        this.rememberme = rememberme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(rememberme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LoginRequest that = (LoginRequest) o;

        if (email != null ? !email.equals(that.email) : that.email != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        return !(rememberme != null ? !rememberme.equals(that.rememberme) : that.rememberme != null);

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (rememberme != null ? rememberme.hashCode() : 0);
        return result;
    }
}
