package hu.bme.simonyi.acstudio.analogchaosinventoryapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Database table class for inventory item objects.
 *
 * @author Lajos Nyeki
 */
@DatabaseTable(tableName = "items")
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

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private int category;

    @DatabaseField
    private String categoryText;

    @DatabaseField
    private int quantity;

    @DatabaseField
    private int parent;

    @DatabaseField
    private String barcode;

    @DatabaseField
    private String comment;

    @DatabaseField
    private int broken;

    @Expose
    @ForeignCollectionField
    private ForeignCollection<Item> childrenItems;

    private ArrayList<Item> children;

    public Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readInt();
        categoryText = in.readString();
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
            children.add(new Item(in));
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
        dest.writeString(categoryText);
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

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
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

    public ForeignCollection<Item> getChildrenItems() {
        return childrenItems;
    }

    public void setChildrenItems(ForeignCollection<Item> childrenItems) {
        this.childrenItems = childrenItems;
    }

    public ArrayList<Item> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Item> children) {
        this.children = children;
    }
}
