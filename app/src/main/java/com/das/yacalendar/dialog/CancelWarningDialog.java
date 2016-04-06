package com.das.yacalendar.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.das.yacalendar.R;
import com.das.yacalendar.yacalendar;

/**
 * Created by yaturner on 4/6/2016.
 */
public class CancelWarningDialog extends DialogFragment {
    Context mContext;
    AlertDialog mWarningCancelDialog = null;
    View mWarningCancelView = null;

    public CancelWarningDialog() {
        mContext = getActivity();
    }
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        mWarningCancelView = factory.inflate(R.layout.dialog_warning_cancel, null);
        mWarningCancelDialog = new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setView(mWarningCancelView)
                .setPositiveButton(R.string.Menu_Yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        yacalendar.getInstance().HideDayView();
                    }
                }).setNegativeButton(R.string.Menu_Cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.dismiss();
                    }
                }).create();

        return mWarningCancelDialog;

    }
}