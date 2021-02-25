package org.itiud.appi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bm extends AppCompatActivity {
Button button;
String nombre ="";
String roomName="";
String role ="";
String message="";
FirebaseDatabase database;
DatabaseReference messageRef;
private FirebaseUser user ;
private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        if(extras!=null){
            roomName= extras.getString("roomName");
            if(roomName.equals(nombre)){
                role="host";
            }else{
                role="guest";
            }

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                message=role + ":Poked";
                messageRef.setValue(message);
            }
        });
        messageRef= database.getReference("rooms/" + roomName + "/message");
        message = role + ":Poked!";
        messageRef.setValue(message);
        AddRoomEventListener();
        
    }

    private void AddRoomEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("host")){
                    if(snapshot.getValue(String.class).contains("guest:")){
                        button.setEnabled(true);
                        Toast.makeText(bm.this, ""+
                                snapshot.getValue(String.class).replace("guest:",""),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(snapshot.getValue(String.class).contains("host:")){
                        button.setEnabled(true);
                        Toast.makeText(bm.this, ""+
                                snapshot.getValue(String.class).replace("host:",""),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               messageRef.setValue(message);
            }
        });
    }
}