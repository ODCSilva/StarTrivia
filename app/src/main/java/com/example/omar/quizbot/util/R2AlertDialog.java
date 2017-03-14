package com.example.omar.quizbot.util;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.example.omar.quizbot.R;

/**
 * This class is in charge of creating a custom dialog that can take a
 * custom message. It also shows R2D2 on the screen :D
 */
public class R2AlertDialog {

    private OnDialogDismissedListener onDialogDismissed;

    public R2AlertDialog() {}

    /**
     * Shows the custom dialog for a short duration of time, then closes it.
     * @param context Application context.
     * @param message The message that will be displayed on the dialog.
     * @param colorId The color of the text in the dialog.
     * @param duration How long the dialog will remain on the screen.
     */
    public void show(Context context, String message, int colorId, long duration) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_answer_result);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        ((TextView)dialog.findViewById(R.id.message)).setText(message);
        ((TextView)dialog.findViewById(R.id.message)).setTextColor(colorId);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (onDialogDismissed != null) {
                    onDialogDismissed.onDismiss();
                }
            }
        };

        final Handler h = new Handler();
        h.postDelayed(runnable, duration);
    }

    /**
     * Setter for the OnDialogDismissed Listener
     * @param onDialogDismissed A new instance of the OnDialogDismissedListener
     */
    public void setOnDialogDismissed(OnDialogDismissedListener onDialogDismissed) {
        this.onDialogDismissed = onDialogDismissed;
    }

    /**
     * OnDialogDismissedListener interface.
     */
    public interface OnDialogDismissedListener {
        void onDismiss();
    }
}
