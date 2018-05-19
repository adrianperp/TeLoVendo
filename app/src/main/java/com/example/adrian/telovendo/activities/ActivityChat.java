package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Chat;
import com.example.adrian.telovendo.clases.Mensaje;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.recyclerview.MensajeAdapter;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ActivityChat extends AppCompatActivity {

    EditText editMensaje;
    FloatingActionButton fab;
    RecyclerView recyclerChat;
    LinearLayoutManager layoutManager;
    MensajeAdapter mAdapter;

    public static Usuario emisor;
    public static Usuario receptor;

    Chat chat;
    public static Context context;
    DatabaseReference databaseRef;
    FirebaseUtils firebaseUtils;

    public static ArrayList<Mensaje> listaMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = ActivityChat.this;
        firebaseUtils = new FirebaseUtils(this);
        listaMensajes = new ArrayList<>();
        editMensaje = findViewById(R.id.editMensaje);
        fab = findViewById(R.id.fabSend);

        // RecyclerView
        recyclerChat = findViewById(R.id.recyclerChat);
        mAdapter = new MensajeAdapter(listaMensajes);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true); // Que la lista apunte siempre al ultimo elemento
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());
        recyclerChat.setAdapter(mAdapter);

        // Recogemos los datos
        emisor = (Usuario)getIntent().getSerializableExtra("emisor");
        receptor = (Usuario)getIntent().getSerializableExtra("receptor");

        // Cambiamos titulo de la toolbar
        getSupportActionBar().setTitle(receptor.getNombre() + " " + receptor.getApellidos().charAt(0) + ".");

        // Comprobar si chat existe
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    String key = datasnap.getKey();
                    System.out.println("Entra en SNAPSHOT: " + datasnap.getValue(Chat.class).getChatId());
                    System.out.println("Emisor contiene?: " + (emisor.getListaChats().contains(key)));
                    System.out.println("Receptor contiene?: " + (receptor.getListaChats().contains(key)));
                    if (emisor.getListaChats().contains(key) && receptor.getListaChats().contains(key)) {
                        chat = datasnap.getValue(Chat.class);

                        System.out.println("------------Chat encontrado------------");
                        break;
                    }
                }
                cargarInterfaz();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Mantener lista actualizada
        /*databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS).child(chat.getChatId()).child(firebaseUtils.CAMPO_LISTA_MENSAJES);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMensajes.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    System.out.println("-------------ENTRA EN DATASNAPSHOT-------------");
                    Mensaje m = snap.getValue(Mensaje.class);
                    if(!m.isEmpty()) {
                        // Anyadiendo a lista
                        listaMensajes.add(m);
                    }
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
                // Vaciamos el send box
                editMensaje.setText("");
            }
        });
    }

    public void cargarInterfaz() {
        System.out.println("ENTRA EN CARGARINTERFAZ");
        if (chat == null) {
            // Se crea un nuevo chat
            chat = new Chat(emisor.getEmail(), receptor.getEmail());
            // Anyadir nuevo chat a la bd
            String key = chat.getChatId();
            FirebaseDatabase.getInstance().getReference().child(firebaseUtils.NODO_CHATS).child(key).setValue(chat);
            // Anyadir chat a usuarios
            emisor.getListaChats().add(key);
            receptor.getListaChats().add(key);
            databaseRef = FirebaseDatabase.getInstance().getReference().child(firebaseUtils.NODO_USUARIOS);
            databaseRef.child(emisor.getIdUsuario()).child(firebaseUtils.CAMPO_LISTA_CHATS).setValue(emisor.getListaChats());
            databaseRef.child(receptor.getIdUsuario()).child(firebaseUtils.CAMPO_LISTA_CHATS).setValue(receptor.getListaChats());
        }

        // Listener para la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS).child(chat.getChatId()).child(firebaseUtils.CAMPO_LISTA_MENSAJES);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listaMensajes.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        //System.out.println("-------------ENTRA EN DATASNAPSHOT-------------");
                        Mensaje m = snap.getValue(Mensaje.class);
                        if (!m.isEmpty()) {
                            // Anyadiendo a lista
                            listaMensajes.add(m);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void enviarMensaje() {
        String mensaje = editMensaje.getText().toString().trim();
        if (!mensaje.isEmpty()) {
            databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS).child(chat.getChatId()).child(firebaseUtils.CAMPO_LISTA_MENSAJES);
            listaMensajes.add(0, new Mensaje(mensaje, new Date(), emisor.getEmail()));

            databaseRef.setValue(listaMensajes);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menu
        getMenuInflater().inflate(R.menu.activity_chat, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_chat_update:
                mAdapter.notifyDataSetChanged();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
