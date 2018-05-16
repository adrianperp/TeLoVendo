package com.example.adrian.telovendo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Mensaje;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder>{

    public List<Mensaje> listaMensajes;

    public MensajeAdapter(List<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mensaje;
        private TextView hora;
        private TextView emisor;
        //ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.textMensaje);
            hora = itemView.findViewById(R.id.textHora);
            emisor = itemView.findViewById(R.id.textEmisor);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mensaje.setText(listaMensajes.get(position).getTexto());
        holder.hora.setText(getDateDif(new Date(), listaMensajes.get(position).getFechaEnvio()));
        holder.emisor.setText(listaMensajes.get(position).getEmisor());
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

    // Devuelve la diferencia de fechas
    public static String getDateDif(Date d1, Date d2) {
        String dif = null;
        try {
            long result = d1.getTime() - d2.getTime();
            if (result < 60000)
                dif = "Ahora";
            else if (result < 3600000)
                dif = String.valueOf(result / 60000) + "m";
            else if (result < 86400000)
                dif = String.valueOf(result / 3600000) + "h";
            else
                dif = String.valueOf(result / 86400000) + "d";
            return dif;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
