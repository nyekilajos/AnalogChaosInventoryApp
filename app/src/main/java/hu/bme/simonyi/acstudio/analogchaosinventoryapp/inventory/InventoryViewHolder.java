package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        final View view = layoutInflater.inflate(R.layout.item_inventory_row, new LinearLayout(context), false);
        setupViews(inventoryRow, view);
        return view;
    }

    private void setupViews(InventoryRow inventoryRow, View view) {

        final ImageView ivArrow = (ImageView) view.findViewById(R.id.items_row_arrow);
        ivArrow.setVisibility(inventoryRow.hasChildren ? View.VISIBLE : View.INVISIBLE);
        initIndentation(inventoryRow, ivArrow);

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

    private void initIndentation(InventoryRow inventoryRow, View firstViewOfRow) {
        float dp = context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        Resources resources = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) firstViewOfRow.getLayoutParams();
        marginLayoutParams.setMargins(inventoryRow.level * px, 0, 0, 0);
        firstViewOfRow.setLayoutParams(marginLayoutParams);
    }

    /**
     * POJO class for holding data for the rows of the TreeView on the inventory fragment.
     */
    public static class InventoryRow {
        public String id;
        public String name;
        public String categoryText;
        public String quantity;
        public String barcode;
        public String comment;
        public boolean hasChildren;
        public int level;

    }
}
