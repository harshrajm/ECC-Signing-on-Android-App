package com.example.cmblap29.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by CMBLAP 29 on 19-03-2018.
 */

public class DialogFragment1 extends android.support.v4.app.DialogFragment {

    private UserDtlListener userDtlListener;
    interface UserDtlListener {
        void onSubmit1(int seleted);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UserDtlListener) {
            userDtlListener = (UserDtlListener) context;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Key to Sign")

                .setItems(R.array.pk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userDtlListener.onSubmit1(which);
                    }
                });
        return builder.create();
    }

}
