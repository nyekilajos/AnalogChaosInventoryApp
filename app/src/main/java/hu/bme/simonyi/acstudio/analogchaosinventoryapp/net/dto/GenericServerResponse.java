package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Generic DTO for Server Response
 *
 * @author Lajos Nyeki
 */
public abstract class GenericServerResponse<T> implements Parcelable {

    @Expose
    private boolean success;
    @Expose
    private int code;
    @Expose
    private String text;
    @Expose
    private T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    protected void writeCommonDataToParecel(Parcel dest) {
        dest.writeInt(success ? 1 : 0);
        dest.writeInt(code);
        dest.writeString(text);
    }

    protected void readCommonDataFromParcel(Parcel in) {
        success = in.readInt() != 0;
        code = in.readInt();
        text = in.readString();
    }

}
