package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText sseg_input;
    Button sseg_btn;
    EditText lcd_input1;
    EditText lcd_input2;
    Button lcd_btn;

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
        sseg_input = findViewById(R.id.sseg_input);
        sseg_btn = findViewById(R.id.sseg_btn);
        lcd_input1 = findViewById(R.id.lcd_input1);
        lcd_input2 = findViewById(R.id.lcd_input2);
        lcd_btn = findViewById(R.id.lcd_btn);

        sseg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int data = getData();
                SSegmentWrite(data);
                sseg_input.setText("");
                tv.setText("7segment settext to "+ Integer.toString(data));
            }
        });

        lcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = lcd_input1.getText().toString();
                String second = lcd_input2.getText().toString();

                if (first.equals("") || second.equals("")){
                    return;
                } else {
                    LcdWrite(first, second);
                }
            }
        });

        //DotmatrixWrite(7);
    }

    public int getData(){
        return Integer.parseInt(sseg_input.getText().toString());
    }







    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
