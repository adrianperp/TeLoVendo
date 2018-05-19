package com.example.adrian.telovendo.activities;

import android.content.Context;
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
    ChatAdapter mAdapter;

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
        mAdapter = new ChatAdapter(listaChats);
        layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true); // Que la lista apunte siempre al ultimo elemento
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());
        recyclerChat.setAdapter(mAdapter);

        // Listener para la lista
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaChats.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Chat c = snap.getValue(Chat.class);

                    // Anyadiendo a lista
                    if (!c.getListaMensajes().isEmpty()) {
                        listaChats.add(c);
                    }
                }
                mAdapter.notifyDataSetChanged();

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
