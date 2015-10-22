package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    /**
     * Creates a custom alert dialog with a neutral OK button, circular alert icon and the given text.
     *
     * @param errorText The text to be shown on the alert dialog.
     * @return The created AlertDialog object.
     */
    public AlertDialog createAlertDialog(String errorText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.dialog_alert, new LinearLayout(context), false);
        TextView tv = (TextView) dialogView.findViewById(R.id.tv_dialog_alert);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert_circle_grey600_48dp, 0, 0, 0);
        tv.setText(errorText);
        builder.setView(dialogView);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
