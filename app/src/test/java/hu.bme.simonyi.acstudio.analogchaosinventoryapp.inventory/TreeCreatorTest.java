package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.inject.ContextScopedProvider;

import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TreeCreatorTest {

    private static final String TEST_BARCODE_1 = "100123456789";
    private static final String TEST_BARCODE_11 = "110123456789";
    private static final String TEST_BARCODE_12 = "120123456789";

    private static final int TEST_BROKEN_1 = 1;
    private static final int TEST_CATEGORY_1 = 200;
    private static final String TEST_CATEGORY_TEXT_1 = "This is a test category";
    private static final String TEST_COMMENT_1 = "This is a unit test";
    private static final int TEST_ID_1 = 100;
    private static final String TEST_NAME_1 = "Test item";
    private static final int TEST_QUANTITY_1 = 12;
    private static final int TEST_PARENT_1 = 2;

    @Mock
    private ContextScopedProvider<InventoryViewHolder> mockInventoryViewHolderProvider;
    @Mock
    private InventoryViewHolder mockInventoryViewHolder;

    private final Context mockContext = RuntimeEnvironment.application;

    private TreeCreator sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockInventoryViewHolderProvider.get(mockContext)).thenReturn(mockInventoryViewHolder);
        sut = new TreeCreator(mockContext, mockInventoryViewHolderProvider);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTreeWithNullInput() {
        sut.createTree(null);
    }

    @Test
    public void testCreateTreeWithEmptyList() {
        TreeNode treeNode = sut.createTree(new ArrayList<Item>());
        Assert.assertEquals("createTree invoked with empty list should return a node without children", true, treeNode.getChildren().isEmpty());
    }

    @Test
    public void testCreateTreeWithOneElementList() {
        Item item = new Item();
        item.setBarcode(TEST_BARCODE_1);
        List<Item> input = new ArrayList<>();
        input.add(item);
        TreeNode treeNode = sut.createTree(input);
        Assert.assertEquals("createTree invoked with one element list should return a node with one children", true,
                treeNodeHasGivenBarcode(treeNode.getChildren().get(0), TEST_BARCODE_1));

    }

    @Test
    public void testNodeHasProperViewHolderSetWithEmptyChildren() {
        Item item = new Item();
        item.setBarcode(TEST_BARCODE_1);
        item.setBroken(TEST_BROKEN_1);
        item.setCategory(TEST_CATEGORY_1);
        item.setCategory_text(TEST_CATEGORY_TEXT_1);
        item.setComment(TEST_COMMENT_1);
        item.setId(TEST_ID_1);
        item.setName(TEST_NAME_1);
        item.setQuantity(TEST_QUANTITY_1);
        item.setParent(TEST_PARENT_1);
        item.setChildren(new ArrayList<Item>());

        List<Item> items = new ArrayList<>();
        items.add(item);

        TreeNode treeNode = sut.createTree(items);
        InventoryViewHolder.InventoryRow inventoryRow = (InventoryViewHolder.InventoryRow) treeNode.getChildren().get(0).getValue();
        Assert.assertEquals("InventoryRow should get barcode from item", TEST_BARCODE_1, inventoryRow.barcode);
        Assert.assertEquals("InventoryRow should get categoryText from item", TEST_CATEGORY_TEXT_1, inventoryRow.categoryText);
        Assert.assertEquals("InventoryRow should get barcode from item", TEST_COMMENT_1, inventoryRow.comment);
        Assert.assertEquals("InventoryRow should get barcode from item", TEST_ID_1, Integer.valueOf(inventoryRow.id).intValue());
        Assert.assertEquals("InventoryRow should get barcode from item", TEST_NAME_1, inventoryRow.name);
        Assert.assertEquals("InventoryRow should get barcode from item", TEST_QUANTITY_1, Integer.valueOf(inventoryRow.quantity).intValue());
    }

    @Test
    public void testCreateTreeWithAComplexTree() {
        List<Item> items = new ArrayList<>();

        Item child1 = new Item();
        child1.setBarcode(TEST_BARCODE_1);
        Item child11 = new Item();
        child11.setBarcode(TEST_BARCODE_11);
        Item child12 = new Item();
        child12.setBarcode(TEST_BARCODE_12);

        List<Item> secondChildren = new ArrayList<>();
        secondChildren.add(child11);
        secondChildren.add(child12);
        child1.setChildren(secondChildren);
        items.add(child1);

        TreeNode treeNode = sut.createTree(items);

        Assert.assertEquals("First item should be the root item", treeNode, treeNode.getRoot());
        Assert.assertEquals("The root item should have 1 children", 1, treeNode.size());
        Assert.assertEquals("The first child item should have 2 children", 2, treeNode.getChildren().get(0).size());
    }

    private boolean treeNodeHasGivenBarcode(TreeNode treeNode, String barcode) {
        return barcode.equals(((InventoryViewHolder.InventoryRow) treeNode.getValue()).barcode);
    }
}
