package hu.bme.simonyi.acstudio.analogchaosinventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


public class MainActivity extends RoboActionBarActivity {

    private static final int REQUEST_BARCODE_SCAN = 100;

    @InjectView(R.id.scan_barcode)
    private Button scanBarcodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                startActivityForResult(intent, REQUEST_BARCODE_SCAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE_SCAN) {
            processBarcodeScanResult(resultCode, data);
        }
    }

    private void processBarcodeScanResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, data.getStringExtra("SCAN_RESULT"), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.barcode_scan_failed), Toast.LENGTH_LONG).show();
        }
    }
}
