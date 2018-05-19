package com.example.adrian.telovendo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.activities.ActivityListaChats;
import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.clases.Chat;
import com.example.adrian.telovendo.clases.Mensaje;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    DatabaseReference databaseRef;
    FirebaseUtils firebaseUtils = new FirebaseUtils(ActivityListaChats.context);

    public List<Chat> listaChats;

    public ChatAdapter(List<Chat> listaChats) {
        this.listaChats = listaChats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textMensajeChat;
        private TextView textUsuarioChat;
        private CircleImageView imageFotoUsuarioChat;

        public ViewHolder(View itemView) {
            super(itemView);
            textMensajeChat = itemView.findViewById(R.id.textMensajeChat);
            textUsuarioChat = itemView.findViewById(R.id.textEmisorChat);
            imageFotoUsuarioChat = itemView.findViewById(R.id.imageFotoUsuarioChat);
        }
    }

    //inflamos contenido de los items para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Implementamos el OnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(mListener!=null) {
                    mListener.onItemClick(listaChats.get(position), position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String currentUserEmail = ActivityMain.firebaseUser.getEmail();
        // Recoger posicion y email usuario participante
        int posParticipante = (currentUserEmail.equals(listaChats.get(position).getListaParticipantes().get(0)) ? 1 : 0);
        String emailParticipante = listaChats.get(position).getListaParticipantes().get(posParticipante);

        holder.textMensajeChat.setText(listaChats.get(position).getListaMensajes().get(0).getTexto());
        // Recoger usuario participante
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_USUARIOS);
        Query q = databaseRef.orderByChild(firebaseUtils.CAMPO_EMAIL).equalTo(emailParticipante);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    Usuario u = datasnap.getValue(Usuario.class);
                    String primerApellido = (u.getApellidos() + " ").split(" ")[0];
                    holder.textUsuarioChat.setText(u.getNombre() + " " + primerApellido);
                    // Comprobar que el usuario tenga foto
                    if (u.getFotoPerfil() != null) {
                        // Referencia a la foto
                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + u.getFotoPerfil());
                        Glide.with(ActivityListaChats.context)
                                .using(new FirebaseImageLoader())
                                .load(mStorageRef)
                                .into(holder.imageFotoUsuarioChat);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    // Listener para la lista
    public interface OnItemClickListener {
        public void onItemClick(Chat c, int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemListener(ChatAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

}
