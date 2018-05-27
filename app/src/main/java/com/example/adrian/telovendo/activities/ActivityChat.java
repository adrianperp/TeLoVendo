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
import android.widget.Toast;

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

        // Recibiendo el chat
        chat = (Chat)getIntent().getSerializableExtra("chat");

        // Comprobar si hemos recibido un chat de la lista de chats - FORMA 1
        if (chat != null) {
            getSupportActionBar().setTitle(getTituloActionBar());
            cargarInterfaz();
        }
        // Buscamos chat a partir de usuarios - FORMA 2
        else {
            emisor = (Usuario)getIntent().getSerializableExtra("emisor");
            receptor = (Usuario)getIntent().getSerializableExtra("receptor");
            // Comprobaremos si existe chat
            // Despues se asigna
            if (emisor != null && receptor != null)
                cargarChat();
            else
                Toast.makeText(context, R.string.toast_problema_carga_usuarios, Toast.LENGTH_SHORT).show();
        }

        // RecyclerView
        recyclerChat = findViewById(R.id.recyclerChat);
        mAdapter = new MensajeAdapter(listaMensajes);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true); // Que la lista apunte siempre al ultimo elemento
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());
        recyclerChat.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
                // Vaciamos el send box
                editMensaje.setText("");
            }
        });
    }

    private String getTituloActionBar() {
        Usuario emisor = chat.getListaParticipantes().get(0);
        Usuario receptor = chat.getListaParticipantes().get(1);
        if (emisor.getEmail().equals(ActivityMain.firebaseUser.getEmail())) {
            return receptor.getNombre() + " " + receptor.getApellidos().charAt(0) + ".";
        }
        else {
            return emisor.getNombre() + " " + emisor.getApellidos().charAt(0) + ".";
        }
    }

    // Actualiza el chat a partir de los participantes
    public void cargarChat() {
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat ch = null;
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    ch = datasnap.getValue(Chat.class);

                    if(ch != null && ch.isFrom(emisor, receptor)) {
                        chat = ch;
                        getSupportActionBar().setTitle(getTituloActionBar());
                        break;
                    }
                }
                // Si no existe se crea un chat nuevo
                if (chat == null) {
                    chat = new Chat(emisor, receptor);
                    // Se anyade a la base de datos
                    firebaseUtils.anyadirChat(chat);
                    // Se anyade el chat a los usuarios
                    emisor.getListaChats().add(chat.getChatId());
                    receptor.getListaChats().add(chat.getChatId());
                    // Se actualizan los usuarios
                    firebaseUtils.actualizarChatsUsuarios(emisor, receptor);
                    getSupportActionBar().setTitle(getTituloActionBar());
                }
                cargarInterfaz();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Listener de mensajes
    public void cargarInterfaz() {

        // Listener para la base de datos
        // Referencia a la lista de mensajes
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS).child(chat.getChatId()).child(firebaseUtils.CAMPO_LISTA_MENSAJES);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listaMensajes.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
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
            // Creamos referencia a la lista de mensajes del chat
            databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_CHATS).child(chat.getChatId()).child(firebaseUtils.CAMPO_LISTA_MENSAJES);
            listaMensajes.add(0, new Mensaje(mensaje, new Date(), ActivityMain.user.getEmail(), ActivityMain.user.getFotoPerfil()));

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
