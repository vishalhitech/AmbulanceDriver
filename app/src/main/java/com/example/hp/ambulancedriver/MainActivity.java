package com.example.hp.ambulancedriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn = (Button) findViewById(R.id.btn);
        final EditText deviceId = (EditText) findViewById(R.id.DeviceID);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btnText = (String) btn.getText();

                if(btnText == "ON") {
                    deviceId.setEnabled(false);
                    btn.setText("OFF");


                }
                else {
                    deviceId.setEnabled(true);
                    deviceId.setText("");
                    deviceId.setHint("Enter Your ID : ");
                    btn.setText("ON");




                }



            }
        });


    }
}
