package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDrawable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;

/**
 * Created by Lajos_Nyeki on 10/14/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class InventoryViewHolderTest {

    private static final String TEST_BARCODE = "123456789";
    private static final String TEST_CATEGORY_TEXT = "Test category";
    private static final String TEST_COMMENT = "This is an awesome item.";
    private static final String TEST_NAME = "TestRow";
    private static final String TEST_ROW_ID = "1";
    private static final String TEST_QUANTITY = "3";

    private InventoryViewHolder sut;

    private Context mockContext = RuntimeEnvironment.application;

    @Mock
    private TreeNode mockTreeNode;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new InventoryViewHolder(mockContext);
    }

    @Test
    public void testTreeViewShouldShowArrowIfItHasChildren() {
        InventoryViewHolder.InventoryRow inventoryRow = createInventoryRow();
        inventoryRow.hasChildren = true;

        View view = sut.createNodeView(mockTreeNode, inventoryRow);

        assertEquals("Arrow should be visible", View.VISIBLE, view.findViewById(R.id.items_row_arrow).getVisibility());
    }

    @Test
    public void testTreeViewShouldShowArrowIfItHasNoChildren() {
        InventoryViewHolder.InventoryRow inventoryRow = createInventoryRow();
        inventoryRow.hasChildren = false;

        View view = sut.createNodeView(mockTreeNode, inventoryRow);

        assertNotEquals("Arrow should be invisible", View.VISIBLE, view.findViewById(R.id.items_row_arrow).getVisibility());
    }

    @Test
    public void testTreeViewShouldSetDataFromInventoryView() {
        View view = sut.createNodeView(mockTreeNode, createInventoryRow());

        assertEquals("Row ID should be set form InventoryRow", TEST_ROW_ID, ((TextView) view.findViewById(R.id.items_row_id)).getText().toString());
        assertEquals("Row ID should be set form InventoryRow", TEST_BARCODE,
                ((TextView) view.findViewById(R.id.items_row_barcode)).getText().toString());
        assertEquals("Row ID should be set form InventoryRow", TEST_CATEGORY_TEXT,
                ((TextView) view.findViewById(R.id.items_row_category_text)).getText().toString());
        assertEquals("Row ID should be set form InventoryRow", TEST_COMMENT,
                ((TextView) view.findViewById(R.id.items_row_comment)).getText().toString());
        assertEquals("Row ID should be set form InventoryRow", TEST_NAME, ((TextView) view.findViewById(R.id.items_row_name)).getText().toString());
        assertEquals("Row ID should be set form InventoryRow", TEST_QUANTITY,
                ((TextView) view.findViewById(R.id.items_row_quantity)).getText().toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToggleShouldCrashIfViewNotCreated() {
        sut.toggle(true);
    }

    @Test
    public void testToggleOnce() {
        View view = sut.createNodeView(mockTreeNode, createInventoryRow());
        sut.toggle(true);

        ShadowDrawable shadowDrawable = Shadows.shadowOf(((ImageView) view.findViewById(R.id.items_row_arrow)).getDrawable());
        assertEquals("Arraw should be pointing down if the node is expanded", R.drawable.ic_chevron_down_grey600_36dp,
                shadowDrawable.getCreatedFromResId());

    }

    @Test
    public void testToggleTwice() {
        View view = sut.createNodeView(mockTreeNode, createInventoryRow());
        sut.toggle(true);
        sut.toggle(false);

        ShadowDrawable shadowDrawable = Shadows.shadowOf(((ImageView) view.findViewById(R.id.items_row_arrow)).getDrawable());
        assertEquals("Arraw should be pointing up if the node is expanded", R.drawable.ic_chevron_right_grey600_36dp,
                shadowDrawable.getCreatedFromResId());
    }

    @NonNull
    private InventoryViewHolder.InventoryRow createInventoryRow() {
        InventoryViewHolder.InventoryRow inventoryRow = new InventoryViewHolder.InventoryRow();
        inventoryRow.barcode = TEST_BARCODE;
        inventoryRow.categoryText = TEST_CATEGORY_TEXT;
        inventoryRow.comment = TEST_COMMENT;
        inventoryRow.id = TEST_ROW_ID;
        inventoryRow.name = TEST_NAME;
        inventoryRow.quantity = TEST_QUANTITY;
        return inventoryRow;
    }

}
