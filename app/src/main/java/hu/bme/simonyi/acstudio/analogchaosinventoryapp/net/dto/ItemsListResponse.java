package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;

/**
 * Response class for getting the inventory items list from the server.
 *
 * @author Lajos Nyeki
 */
public class ItemsListResponse extends GenericServerResponse<List<Item>> {
    @Override
    public int describeContents() {
        return 0;
    }

    public ItemsListResponse(Parcel in) {
        readCommonDataFromParcel(in);
        readItemsListFromParcel(in);
    }

    private void readItemsListFromParcel(Parcel in) {
        List<Item> items = new ArrayList<>();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            items.add(new Item(in));
        }
        setResult(items);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeCommonDataToParecel(dest);
        writeItemsListToParcel(dest, flags);
    }


    private void writeItemsListToParcel(Parcel dest, int flags) {
        dest.writeInt(getResult().size());
        for (Item item : getResult()) {
            item.writeToParcel(dest, flags);
        }
    }
}
