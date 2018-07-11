package com.example.cmblap29.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmblap29.myapplication.util.CryptoUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DialogFragment1.UserDtlListener,DialogFragment2.UserDtlListener {


    @BindView(R.id.etInput)
    EditText etInput;

    @BindView(R.id.btnSign)
    Button btnSign;

    @BindView(R.id.tvSig)
    TextView tvSig;

    @BindView(R.id.btnVerify)
    Button btnVerify;

    @BindView(R.id.imageView)
    ImageView imageView;

    String signature = null;

    DialogFragment1 dialogFragment1;
    DialogFragment2 dialogFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialogFragment1 = new DialogFragment1();
        dialogFragment2 = new DialogFragment2();

    }


    @OnClick(R.id.btnSign)
    void onSignBtnClicked(){
        hideKeyboard();
        imageView.setVisibility(View.INVISIBLE);
        if(etInput.getText().toString().equals("")){
            Toast.makeText(this, "Type Message to sign", Toast.LENGTH_SHORT).show();
            return;
        }
        dialogFragment1.show(getSupportFragmentManager(),"qwerty");
    }

    private String loadStrFrmFile(int file) {
        InputStream is = getResources().openRawResource(file);

        String s1 = null;

        try {
            s1 = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(is);
        return s1;
    }

    @OnClick(R.id.btnVerify)
    void btnVerifyClkd(){
        hideKeyboard();
        dialogFragment2.show(getSupportFragmentManager(),"ytrewq");
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onSubmit1(int seleted) {
        //Toast.makeText(this, "selected : "+seleted, Toast.LENGTH_SHORT).show();
        String str = null;
        if(seleted == 0){
           str = loadStrFrmFile(R.raw.private_key_1);
            //Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
        }else if(seleted == 1){
           str = loadStrFrmFile(R.raw.private_key_2);
            //Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();

        }
        //code for sign
        PrivateKey privateKey = new CryptoUtil().getPrivateKey(str);


        try {
            signature = new CryptoUtil().sign(privateKey,etInput.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvSig.setText("Signature : \n\n"+signature+"\n\n");
        tvSig.setVisibility(View.VISIBLE);
        btnVerify.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSubmit2(int seleted) {
        //Toast.makeText(this, ""+seleted, Toast.LENGTH_SHORT).show();
        String str = null;
        if(seleted == 0){
            str = loadStrFrmFile(R.raw.cer_node1);
        }else if(seleted == 1){
            str = loadStrFrmFile(R.raw.cer_node2);
        }

        //load PubKey From Cer
        //String str = loadStrFrmFile(R.raw.cer_node1);
        PublicKey publicKey = null;
        try {
            publicKey = CryptoUtil.getPublicKeyFrmCer(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //load Pub Key
        /*String str = loadStrFrmFile(R.raw.public_key);
        PublicKey publicKey = null;
        try {
            publicKey = new CryptoUtil().getPublicKey(str);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        boolean b = false;
        try {
            b = new CryptoUtil().verify(publicKey,etInput.getText().toString(),signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(b){
            Toast.makeText(this, "Verification Successful", Toast.LENGTH_LONG).show();
            imageView.setImageResource(R.drawable.checkmark);
            imageView.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Verification Unsuccessful", Toast.LENGTH_LONG).show();
            imageView.setImageResource(R.drawable.delete);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
