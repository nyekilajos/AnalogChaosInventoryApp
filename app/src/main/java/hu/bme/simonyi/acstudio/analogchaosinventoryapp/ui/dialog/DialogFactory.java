package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;

/**
 * Dialog factory for creating alert dialogs.
 *
 * @author Lajos Nyeki
 */
public class DialogFactory {

    private Context context;

    @Inject
    public DialogFactory(Context context) {
        this.context = context;
    }

    public AlertDialog createAlertDialog(String errorText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.alert_circle);
        builder.setTitle(errorText);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
