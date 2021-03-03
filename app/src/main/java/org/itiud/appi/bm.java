package org.itiud.appi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.itiud.appi.logica.BuscaMInas;
import org.itiud.appi.logica.Celda;

public class bm extends AppCompatActivity {
    Button button;
    String nombre ="";
    String roomName="";
    String role ="";
    String message="";
    FirebaseDatabase database;
    DatabaseReference messageRef;
    DatabaseReference PuntajeRef;
    DatabaseReference Puntajes;
    private FirebaseUser user ;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean move;
    public TextView Phost;
    public TextView Text2;
    String p = "0";
    //
    int width =0;
    int height=0;
    private GridLayout gridLayout;
    TextView celdas[][] = new TextView[8][8];
    private BuscaMInas bm = new BuscaMInas();
    private  Celda[][] celda  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bm);
        button=findViewById(R.id.button2);
        button.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        nombre = user.getDisplayName();
        Bundle extras = getIntent().getExtras();
        Phost = findViewById(R.id.textView);
        Text2 = findViewById(R.id.textView2);
        ///
        this.gridLayout = findViewById(R.id.Grid);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width =size.x;
        height=size.y;
        this.Text2.setX(width-300);
        this.button.setX(width/2-78);
        this.button.setY(height-247);
        this.button.setBackgroundColor(Color.BLACK);
        this.button.setTextColor(Color.WHITE);
        move = false;


        ///
        if(extras!=null){
            roomName= extras.getString("roomName");
            if(roomName.equals(nombre)){
                role="host";
            }else{
                role="guest";
            }
        }
        playing();

        messageRef= database.getReference("rooms/" + roomName + "/message");
        message = role + ":Poked!";
        messageRef.setValue(message);
        AddRoomEventListener(true);

        Puntajes = database.getReference().child("rooms/" + roomName);
        Puntajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role=="host"){
                String h = snapshot.child("puntaje1").getValue(String.class).replace("host","Tu puntaje :");
                Phost.setText(h);
                 p = snapshot.child("puntaje2").getValue(String.class).replace("guest","Puntaje oponente :");
                 Text2.setText(p);
                }else{
                    String h = snapshot.child("puntaje2").getValue(String.class).replace("guest","Tu puntaje :");
                    Phost.setText(h);
                    String p = snapshot.child("puntaje1").getValue(String.class).replace("host","Puntaje oponente :");
                    Text2.setText(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void AddRoomEventListener(boolean m) {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("host")){
                    if(snapshot.getValue(String.class).contains("guest:")){
                       move=true;
                       if(m) {
                           Toast.makeText(bm.this, "Tu turno", Toast.LENGTH_SHORT).show();
                       }

                    }
                }else{
                    if(snapshot.getValue(String.class).contains("host:")){
                      move = true;
                      if(m) {
                          Toast.makeText(bm.this, "Tu turno", Toast.LENGTH_SHORT).show();
                      }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messageRef.setValue(message);
            }
        });
    }



    private void dothis(){
        if(role.equals("host")){
            PuntajeRef= database.getReference("rooms/" + roomName + "/puntaje1");
            message = role + " "+  this.bm.GoodCells();
            PuntajeRef.setValue(message);

        }else{
            PuntajeRef= database.getReference("rooms/" + roomName + "/puntaje2");
            message = role + " "+  this.bm.GoodCells();
            PuntajeRef.setValue(message);
        }

    }

    public void playing(){
        this.gridLayout.removeAllViews();
        this.celda = this.bm.getMatrix();
        boolean end = bm.isEnd();
        boolean win = bm.win();
        dothis();
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable();
        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this,android.R.color.darker_gray).withAlpha(15));
        shapeDrawable.setStroke(1.0f, ContextCompat.getColor(this,R.color.black));

        MaterialShapeDrawable shapeDrawabl = new MaterialShapeDrawable();
        shapeDrawabl.setFillColor(ContextCompat.getColorStateList(this,android.R.color.darker_gray).withAlpha(75));
        shapeDrawabl.setStroke(1.5f, ContextCompat.getColor(this,R.color.white));

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                this.celdas[i][j] = new TextView(this);
                ViewCompat.setBackground(this.celdas[i][j],shapeDrawable);
                if(!this.celda[i][j].isEstado()){
                    if(this.celda[i][j].getValue().equals("0")
                    ){
                        this.celdas[i][j].setText(" ");
                        ViewCompat.setBackground(this.celdas[i][j],shapeDrawabl);
                    }else{
                        this.celdas[i][j].setText(this.celda[i][j].getValue());
                        ViewCompat.setBackground(this.celdas[i][j],shapeDrawable);
                    }
                }
                this.celdas[i][j].setLayoutParams(new LinearLayout.LayoutParams(width/8,height/8-50));
                this.celdas[i][j].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                this.celdas[i][j].setTextSize(30);
                this.gridLayout.addView(celdas[i][j]);
                this.celdas[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction()==MotionEvent.ACTION_DOWN) {
                            GridLayout parent = (GridLayout) v.getParent();
                            int x = parent.indexOfChild(v) / parent.getColumnCount();
                            int y = parent.indexOfChild(v) % parent.getColumnCount();
                            if (move){
                                if (!win) {
                                    if (end) {
                                        celda = bm.play(x, y);
                                        move=false;
                                        message = role + ":Poked";
                                        messageRef.setValue(message);
                                        AddRoomEventListener(false);
                                        playing();

                                    } else {

                                        Toast.makeText(getApplicationContext(), "YOU LOSE", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "YOU WIN", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

}