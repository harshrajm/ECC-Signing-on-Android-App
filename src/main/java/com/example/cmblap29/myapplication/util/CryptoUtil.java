package com.example.cmblap29.myapplication.util;

import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by CMBLAP 29 on 30-10-2017.
 */

public class CryptoUtil {

    public static PublicKey getPublicKeyFrmCer(String certInStr) throws Exception {
        // Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        //PublicKey publicKey = null;
        //FileInputStream fis = new FileInputStream(filePath);
        InputStream fis = new ByteArrayInputStream(certInStr.getBytes());
        //BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());

        // while (bis.available() > 0) {
        Certificate cert = cf.generateCertificate(fis);
        return cert.getPublicKey();
        //}
        // return publicKey;
    }

    public PublicKey getPublicKey(String pubKey){
        // String publicInBase64 = null;


        String publicKeyPEM = pubKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("\n-----END PUBLIC KEY-----", "");


        byte[] encoded = new byte[0];
        try {
            encoded =  Base64.decode(publicKeyPEM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PublicKey publicKey = null;
        try {
            publicKey = kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }


    public String sign(PrivateKey priKey, String textToSign) throws Exception{
        Signature ecdsa;
        ecdsa = Signature.getInstance("SHA1withECDSA",new BouncyCastleProvider());
        ecdsa.initSign(priKey);
        byte[] baText = textToSign.getBytes("UTF-8");
        ecdsa.update(baText);

        byte[] baSignature = ecdsa.sign();
        // textView.append("\n"+baSignature.toString());

        return android.util.Base64.encodeToString(baSignature, android.util.Base64.NO_WRAP);
    }

    public boolean verify(PublicKey pubKey,String data,String signature) throws Exception{

        Signature signature1 = Signature.getInstance("SHA1withECDSA",new BouncyCastleProvider());
        signature1.initVerify(pubKey);
        signature1.update(data.getBytes());

        return signature1.verify(android.util.Base64.decode(signature, android.util.Base64.NO_WRAP));
    }


    public PrivateKey getPrivateKey(String priKey){

        //String privateinBase64 = ;

        String privKeyPEM = priKey.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privKeyPEM = privKeyPEM.replace("\n-----END PRIVATE KEY-----", "");
        byte[] encoded = new byte[0];
        try {
            encoded =  Base64.decode(privKeyPEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // PKCS8 decode the encoded RSA private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PrivateKey privKey = null;
        try {
            privKey = kf.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privKey;

    }

    static PrivateKey getPrivateKeyFromPFX(InputStream inputStream) throws Exception{
        Log.i("main is",inputStream.toString());
        // KeyFix.fixKeyLength();
        String password = "your_keystore_pass";
        // InputStream inputStream = new ByteArrayInputStream(s.getBytes());
        KeyStore ks1 = KeyStore.getInstance("PKCS12",new BouncyCastleProvider());
        Log.i("main ks",ks1.toString());

        ks1.load(inputStream, password.toCharArray());
        String alias1 = ks1.aliases().nextElement();

        PrivateKey pKey = (PrivateKey)ks1.getKey(alias1, password.toCharArray());
        return pKey;
    }

    public static PrivateKey getPrivateKeyFromPFX(String s) throws Exception{
        Log.i("main is",s);
        // KeyFix.fixKeyLength();
        String password = "your_keystore_pass";
        InputStream inputStream = new ByteArrayInputStream(s.getBytes());
        KeyStore ks1 = KeyStore.getInstance("PKCS12",new BouncyCastleProvider());
        Log.i("main ks",ks1.toString());

        ks1.load(inputStream, password.toCharArray());
        String alias1 = ks1.aliases().nextElement();

        PrivateKey pKey = (PrivateKey)ks1.getKey(alias1, password.toCharArray());
        return pKey;
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
