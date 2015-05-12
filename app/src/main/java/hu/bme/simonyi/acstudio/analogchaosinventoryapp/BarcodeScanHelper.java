package hu.bme.simonyi.acstudio.analogchaosinventoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;

/**
 * Helper class for barcode scanning operations
 * @author Lajos Nyeki
 */
@ContextSingleton
public class BarcodeScanHelper {

    private static final String ZXING_PLAY_STORE_LINK = "market://details?id=com.google.zxing.client.android";

    private Context context;
    private Intent scanIntent;
    
    @Inject
    public BarcodeScanHelper (Context context) {
        this.context = context;
        scanIntent = new Intent("com.google.zxing.client.android.SCAN");
        scanIntent.setPackage("com.google.zxing.client.android");
    }

    public boolean isZxingInstalled() {
        PackageManager pm = context.getPackageManager();
        return getBarcodeScanIntent().resolveActivity(pm) != null;
    }

    public Intent getBarcodeScanIntent() {
        return scanIntent;
    }

    public void showPlaystore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(ZXING_PLAY_STORE_LINK));
        context.startActivity(intent);

    }

    public void processBarcodeScanResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, data.getStringExtra("SCAN_RESULT"), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, context.getString(R.string.barcode_scan_failed), Toast.LENGTH_LONG).show();
        }
    }
}
