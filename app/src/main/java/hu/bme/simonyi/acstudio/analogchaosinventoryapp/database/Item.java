package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Class for storing inventory item objects.
 *
 * @author Lajos Nyeki
 */
public class Item implements Parcelable {

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private int category;

    @Expose
    private String category_text;

    @Expose
    private int quantity;

    @Expose
    private int parent;

    @Expose
    private String barcode;

    @Expose
    private String comment;

    @Expose
    private int broken;

    @Expose
    private List<Item> children;

    public Item() {

    }

    public Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readInt();
        category_text = in.readString();
        quantity = in.readInt();
        parent = in.readInt();
        barcode = in.readString();
        comment = in.readString();
        broken = in.readInt();
        readChildrenFromParcel(in);
    }

    private void readChildrenFromParcel(Parcel in) {
        int count = in.readInt();
        children = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Item child = new Item(in);
            children.add(child);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(category);
        dest.writeString(category_text);
        dest.writeInt(quantity);
        dest.writeInt(parent);
        dest.writeString(barcode);
        dest.writeString(comment);
        dest.writeInt(broken);
        writeChildrenToParcel(dest, flags);
    }

    private void writeChildrenToParcel(Parcel dest, int flags) {
        dest.writeInt(children.size());
        for (Item itemDto : children) {
            writeToParcel(dest, flags);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCategory_text() {
        return category_text;
    }

    public void setCategory_text(String category_text) {
        this.category_text = category_text;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getBroken() {
        return broken;
    }

    public void setBroken(int broken) {
        this.broken = broken;
    }

    public List<Item> getChildren() {
        return children;
    }

    public void setChildren(List<Item> children) {
        this.children = children;
    }
}
