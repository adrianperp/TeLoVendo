package com.example.adrian.telovendo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Categoria;
import com.example.adrian.telovendo.clases.Producto;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombreCategoria);
            foto = itemView.findViewById(R.id.imgCategoria);
        }
    }

    public List<Categoria> listaCategorias;

    public RecyclerViewAdapter(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    //inflamos contenido de los items para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Implementamos el OnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(mListener!=null) {
                    mListener.onItemClick(listaCategorias.get(position), position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(listaCategorias.get(position).getNombre().toString());
        holder.foto.setImageResource(listaCategorias.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    // Listener para la lista
    public interface OnItemClickListener {
        public void onItemClick(Categoria c, int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
