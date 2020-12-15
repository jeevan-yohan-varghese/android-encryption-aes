package com.dev.jeevanyohan.sampleaesencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    TextInputEditText etTxt,etKey;
    MaterialButton btnEncrypt,btnDecrypt;
    TextView tvResult;
    ImageView imgCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTxt=(TextInputEditText)findViewById(R.id.et_txt);
        etKey=(TextInputEditText)findViewById(R.id.et_key);
        btnEncrypt=(MaterialButton)findViewById(R.id.btn_encrpyt);
        btnDecrypt=(MaterialButton)findViewById(R.id.btn_decrypt);

        tvResult=(TextView)findViewById(R.id.tv_result);
        imgCopy=(ImageView)findViewById(R.id.img_copy);

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    SecretKey secretKey=generateKey(etKey.getText().toString());
                    String strResult=encryptMsg(etTxt.getText().toString(),secretKey);
                    tvResult.setText(strResult);
                    imgCopy.setVisibility(View.VISIBLE);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SecretKey secretKey=generateKey(etKey.getText().toString());
                    String strResult=decryptMsg(etTxt.getText().toString(),secretKey);
                    tvResult.setText(strResult);
                    imgCopy.setVisibility(View.VISIBLE);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvResult.getText().toString());
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v,"Copied to clipboard",Snackbar.LENGTH_SHORT).show();
            }
        });


    }



    public String encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }
    public String decryptMsg(String cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decode = Base64.decode(cipherText, Base64.NO_WRAP);
        String decryptString = new String(cipher.doFinal(decode), "UTF-8");
        return decryptString;
    }
    public static SecretKey generateKey(String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeySpec secret;
        secret = new SecretKeySpec(key.getBytes(), "AES");
        return  secret;
    }
}