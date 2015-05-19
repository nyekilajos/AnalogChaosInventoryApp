package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BarcodeScanHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Fragment for Scanning barcodes
 *
 * @author Lajos Nyeki
 */
public class ScanFragment extends RoboFragment {

    public static final int TITLE_ID = R.string.navigation_scan;
    public static final String TAG = ScanFragment.class.getSimpleName();

    private static final int REQUEST_BARCODE_SCAN = 100;

    @InjectView(R.id.scan_barcode)
    private Button scanBarcodeButton;

    @Inject
    private BarcodeScanHelper barcodeScanHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barcodeScanHelper.isZxingInstalled()) {
                    startActivityForResult(barcodeScanHelper.getBarcodeScanIntent(), REQUEST_BARCODE_SCAN);
                } else {
                    barcodeScanHelper.showPlaystore();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE_SCAN) {
            barcodeScanHelper.processBarcodeScanResult(resultCode, data);
        }
    }
}
