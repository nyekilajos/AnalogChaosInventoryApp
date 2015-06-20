package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory;

import android.content.Context;

import com.google.inject.Inject;
import com.unnamed.b.atv.model.TreeNode;

import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;
import roboguice.inject.ContextScopedProvider;

/**
 * Creator class for creating the tree data structure fot the TreeView on the Inventory Fragment.
 *
 * @author Lajos Nyeki
 */
public class TreeCreator {

    private Context context;
    private ContextScopedProvider<InventoryViewHolder> inventoryViewHolderProvider;

    @Inject
    public TreeCreator(Context context, ContextScopedProvider<InventoryViewHolder> inventoryViewHolderProvider) {
        this.context = context;
        this.inventoryViewHolderProvider = inventoryViewHolderProvider;
    }

    /**
     * Creates the tree data structure for the TreeView on the Inventory Fragment.
     *
     * @param items List of Items to create a tree form them.
     * @return The root TreeNode object of the created tree.
     */
    public TreeNode createTree(List<Item> items) {
        TreeNode root = TreeNode.root();
        for (Item item : items) {
            attachAvailableChildren(root, item);
        }
        return root;
    }

    private void attachAvailableChildren(TreeNode root, Item item) {
        if (root != null && item != null) {
            TreeNode itemNode = createTreeNode(item);
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                for (Item child : item.getChildren()) {
                    itemNode.addChild(createTreeNode(child));
                    attachAvailableChildren(itemNode, child);
                }
            }
            root.addChild(itemNode);
        }
    }

    private TreeNode createTreeNode(Item item) {
        return new TreeNode(createHolderFromItem(item)).setViewHolder(inventoryViewHolderProvider.get(context));
    }

    private InventoryViewHolder.InventoryRow createHolderFromItem(Item item) {
        InventoryViewHolder.InventoryRow inventoryRow = new InventoryViewHolder.InventoryRow();
        inventoryRow.id = Integer.toString(item.getId());
        inventoryRow.name = item.getName();
        inventoryRow.categoryText = item.getCategory_text();
        inventoryRow.quantity = Integer.toString(item.getQuantity());
        inventoryRow.barcode = item.getBarcode();
        inventoryRow.comment = item.getComment();
        return inventoryRow;
    }
}
