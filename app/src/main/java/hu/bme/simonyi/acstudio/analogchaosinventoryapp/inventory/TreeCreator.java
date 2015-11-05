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
            attachAvailableChildren(root, item, 0);
        }
        return root;
    }

    private void attachAvailableChildren(TreeNode root, Item item, int depthLevel) {
        if (root != null && item != null) {
            TreeNode itemNode = createTreeNode(item, depthLevel);
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                for (Item child : item.getChildren()) {
                    attachAvailableChildren(itemNode, child, depthLevel + 1);
                }
            }
            root.addChild(itemNode);
        }
    }

    private TreeNode createTreeNode(Item item, int level) {
        return new TreeNode(createHolderFromItem(item, level)).setViewHolder(inventoryViewHolderProvider.get(context));
    }

    private static InventoryViewHolder.InventoryRow createHolderFromItem(Item item, int level) {
        InventoryViewHolder.InventoryRow inventoryRow = new InventoryViewHolder.InventoryRow();
        inventoryRow.id = Integer.toString(item.getId());
        inventoryRow.name = item.getName();
        inventoryRow.categoryText = item.getCategory_text();
        inventoryRow.quantity = Integer.toString(item.getQuantity());
        inventoryRow.barcode = item.getBarcode();
        inventoryRow.comment = item.getComment();
        inventoryRow.hasChildren = item.getChildren() != null && !item.getChildren().isEmpty();
        inventoryRow.level = level;
        return inventoryRow;
    }
}
