package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText input;
    Button btn;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("7segment");
        System.loadLibrary("lcd");
        //System.loadLibrary("dotmatrix");
    }

    public native int SSegmentWrite(int data);
    public native int LcdWrite(String first, String second);
    //public native int DotmatrixWrite(int data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = findViewById(R.id.sample_text);
        input = findViewById(R.id.input);
        btn = findViewById(R.id.button);

        LcdWrite("hi", "android");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int data = getData();
                SSegmentWrite(data);
                input.setText("");
                tv.setText("7segment settext to "+ Integer.toString(data));
            }
        });

        //DotmatrixWrite(7);
    }

    public int getData(){
        return Integer.parseInt(input.getText().toString());
    }







    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
