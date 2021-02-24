package org.itiud.appi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;



public class inicio extends AppCompatActivity {
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
       name = findViewById(R.id.nombre);
       this.name.setText("Welcome!");


             }
    }
