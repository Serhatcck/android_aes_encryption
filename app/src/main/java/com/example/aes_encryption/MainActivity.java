package com.example.aes_encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.BugreportManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class MainActivity extends AppCompatActivity {

    private Button randomEncryptBtn;
    private Button encryptBtn;
    private TextView clearText;
    private TextView secretText;
    private IvParameterSpec ivParameterSpec;

    SecretKey secretKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            secretKey = null;
            ivParameterSpec = null;

            randomEncryptBtn = (Button) findViewById(R.id.random_encryp_btn);
            encryptBtn = (Button) findViewById(R.id.encrpyt_btn);
            clearText = (TextView)findViewById(R.id.clearText);
            secretText = (TextView) findViewById(R.id.secret_text_view);


            randomEncryptBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    runEncryptWithRandomKey();
                }
            });

            encryptBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    runEncrypt();
                }
            });




    }

    private void runEncryptWithRandomKey(){
        try{
            SecretKey key = generateKey(128);
            IvParameterSpec ivParameter = generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            String cipherText = encrypt(algorithm, clearText.getText().toString(), key, ivParameter);
            secretText.setText(cipherText);
        }catch (Exception ex){

        }

    }

    private void runEncrypt(){
        try{
            if(secretKey == null){
                secretKey = generateKey(128);
            }
            if(ivParameterSpec == null){
                ivParameterSpec = generateIv();
            }
            String algorithm = "AES/CBC/PKCS5Padding";
            String cipherText = encrypt(algorithm, clearText.getText().toString(), secretKey, ivParameterSpec);
            secretText.setText(cipherText);
        }catch (Exception ex){

        }
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        String returnValue = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            returnValue = Base64.getEncoder().encodeToString(cipherText);
        }
        return returnValue;
    }
}