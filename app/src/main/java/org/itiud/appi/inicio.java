    package org.itiud.appi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class inicio extends AppCompatActivity {
    private String nombre="";
    private String nombSala ="";
    private FirebaseUser user ;
    private ListView listView;
    private Button button;
    private List<String> roomlist;
    private FirebaseDatabase database;
    private DatabaseReference roomRef;
    private DatabaseReference roomsRef;
    private DatabaseReference PuntajeRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        listView = findViewById(R.id.ListView);
        button = findViewById(R.id.button);
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        roomlist = new ArrayList<>();
        nombre = user.getDisplayName();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("CREANDO SALA");
                button.setEnabled(false);
                nombSala=nombre;
                roomRef = database.getReference("rooms/" +nombSala + "/jugador1");
                AddRoomEventListener();
                roomRef.setValue(nombre);
                PuntajeRef= database.getReference("rooms/" + nombSala + "/puntaje2");
                PuntajeRef.setValue("guest : 0");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
              nombSala = roomlist.get(position);
                roomRef = database.getReference("rooms/" +nombSala + "/jugador2");
                AddRoomEventListener();
                roomRef.setValue(nombre);
            }
        });
 addRoomsEventListener();
    }

    private void AddRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button.setText("CREAR SALA");
                button.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(),bm.class);
                intent.putExtra("roomName",nombSala);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              button.setText("CREAR SALA");
              button.setEnabled(true);
            }
        });
    }
    private void addRoomsEventListener(){
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomlist.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for(DataSnapshot snapshot1 : rooms){
                    roomlist.add(snapshot1.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>
                            (inicio.this, android.R.layout.simple_list_item_1,roomlist);
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
