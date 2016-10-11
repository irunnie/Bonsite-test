package com.application.closed.testapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TestActivity extends Activity implements View.OnClickListener {

    EditText editText;
    Button okButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        editText = (EditText) findViewById(R.id.editText);

        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (editText.getText().toString().equals("")){
            Toast.makeText(this, "Please, enter the link", Toast.LENGTH_LONG).show();
        }

        else{
            Intent intent = new Intent();
            intent.setAction("com.example.applicationb.SHOW");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setComponent(new ComponentName("com.example.applicationb", "com.example.applicationb.ImageActivity"));
            intent.putExtra("link", editText.getText().toString());
            try{
                startActivity(intent);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
