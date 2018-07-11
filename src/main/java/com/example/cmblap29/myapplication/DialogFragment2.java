package com.example.cmblap29.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by CMBLAP 29 on 19-03-2018.
 */

public class DialogFragment2 extends DialogFragment {

    private DialogFragment2.UserDtlListener userDtlListener;
    interface UserDtlListener {
        void onSubmit2(int seleted);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DialogFragment2.UserDtlListener) {
            userDtlListener = (DialogFragment2.UserDtlListener) context;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Certificate to Verify")

                .setItems(R.array.cer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userDtlListener.onSubmit2(which);
                    }
                });
        return builder.create();
    }

}
