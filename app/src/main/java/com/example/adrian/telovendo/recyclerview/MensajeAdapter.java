package com.example.adrian.telovendo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.activities.ActivityChat;
import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.clases.Mensaje;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.adrian.telovendo.utilidades.FechasUtils.getDateDif;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder>{

    public static FirebaseUtils firebaseUtils = new FirebaseUtils(ActivityChat.context);
    public List<Mensaje> listaMensajes;

    public MensajeAdapter(List<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mensaje;
        private TextView hora;
        private TextView emisor;
        CircleImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.textMensaje);
            hora = itemView.findViewById(R.id.textHora);
            emisor = itemView.findViewById(R.id.textEmisor);
            foto = itemView.findViewById(R.id.imageFotoUsuarioMensaje);
        }
    }

    //inflamos contenido de los items para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Implementamos el OnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(mListener!=null) {
                    mListener.onItemClick(listaMensajes.get(position), position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mensaje.setText(listaMensajes.get(position).getTexto());
        holder.hora.setText(getDateDif(new Date(), listaMensajes.get(position).getFechaEnvio()));
        // Comprobar si emisor es usuario logueado
        holder.emisor.setText((
                listaMensajes.get(position).getEmisor().equals(ActivityMain.user.getEmail())? "Tú" :
                        listaMensajes.get(position).getEmisor()));

        // Comprobar si tiene foto
        if (listaMensajes.get(position).getFoto() != null) {
            // Referencia a la foto
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + listaMensajes.get(position).getFoto());
            Glide.with(ActivityChat.context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(holder.foto);
        }
        else {
            holder.foto.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    // Listener para la lista
    public interface OnItemClickListener {
        public void onItemClick(Mensaje m, int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemListener(OnItemClickListener listener) {
        mListener = listener;
    }



}
