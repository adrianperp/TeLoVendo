package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Chat;
import com.example.adrian.telovendo.recyclerview.ChatAdapter;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityListaChats extends AppCompatActivity {

    RecyclerView recyclerChat;
    LinearLayoutManager layoutManager;
    ChatAdapter chatAdapter;

    DatabaseReference databaseRef;
    FirebaseUtils firebaseUtils;
    public static Context context;
    public static ArrayList<Chat> listaChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chats);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Cambiamos titulo de la toolbar
        getSupportActionBar().setTitle("Chats");

        listaChats = new ArrayList<>();
        context = ActivityListaChats.this;
        firebaseUtils = new FirebaseUtils(ActivityListaChats.this);

        // RecyclerView
        recyclerChat = findViewById(R.id.recyclerChats);
        chatAdapter = new ChatAdapter(listaChats);
        layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true); // Que la lista apunte siempre al ultimo elemento
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());
        recyclerChat.setAdapter(chatAdapter);

        // Para responder al evento de clic...
        chatAdapter.setOnItemListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat c, int position) {
                Intent activityDetallesProducto = new Intent(ActivityListaChats.this, ActivityChat.class);
                activityDetallesProducto.putExtra("chat", c);
                startActivity(activityDetallesProducto);
            }
        });

        // Listener para la lista
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaChats.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    System.out.println("KEY------------------" + snap.getKey());
                    Chat c = snap.getValue(Chat.class);
                    // Comprobar que chats pertenecen al usuario
                    if (ActivityMain.user.getListaChats().contains(c.getChatId())) {
                        // Carga chats con mensajes
                        if (!c.getListaMensajes().isEmpty()) {
                            listaChats.add(c);
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
