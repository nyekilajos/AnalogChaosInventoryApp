package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.unnamed.b.atv.model.TreeNode;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;

/**
 * ViewHolder class for the TreeView on the Inventory Fragment.
 *
 * @author Lajos Nyeki
 */
public class InventoryViewHolder extends TreeNode.BaseNodeViewHolder<InventoryViewHolder.InventoryRow> {

    private Context context;

    @Inject
    public InventoryViewHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View createNodeView(TreeNode treeNode, InventoryRow inventoryRow) {
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.item_inventory_row, null, false);
        setupViews(inventoryRow, view);
        return view;
    }

    private void setupViews(InventoryRow inventoryRow, View view) {
        final TextView tvId = (TextView) view.findViewById(R.id.items_row_id);
        tvId.setText(inventoryRow.id);
        final TextView tvName = (TextView) view.findViewById(R.id.items_row_name);
        tvName.setText(inventoryRow.name);
        final TextView tvCategoryText = (TextView) view.findViewById(R.id.items_row_category_text);
        tvCategoryText.setText(inventoryRow.categoryText);
        final TextView tvQuantity = (TextView) view.findViewById(R.id.items_row_quantity);
        tvQuantity.setText(inventoryRow.quantity);
        final TextView tvBarcode = (TextView) view.findViewById(R.id.items_row_barcode);
        tvBarcode.setText(inventoryRow.barcode);
        final TextView tvComment = (TextView) view.findViewById(R.id.items_row_comment);
        tvComment.setText(inventoryRow.comment);
    }

    public static class InventoryRow {
        public String id;
        public String name;
        public String categoryText;
        public String quantity;
        public String barcode;
        public String comment;

    }
}
