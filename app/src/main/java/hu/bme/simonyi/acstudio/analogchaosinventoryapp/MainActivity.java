package hu.bme.simonyi.acstudio.analogchaosinventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


public class MainActivity extends RoboActionBarActivity {

    private static final int REQUEST_BARCODE_SCAN = 100;

    @InjectView(R.id.scan_barcode)
    private Button scanBarcodeButton;

    @Inject
    private BarcodeScanHelper barcodeScanHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE_SCAN) {
            barcodeScanHelper.processBarcodeScanResult(resultCode, data);
        }
    }
}
