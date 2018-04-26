package com.example.adrian.telovendo.recyclerview;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Referenciamos objetos objeto de la position que nos pasan
        private ImageView imagenProducto;
        private TextView textNombreLista;
        private TextView textDescripcionLista;
        private TextView textPrecioLista;

        public ViewHolder(View v) {
            super(v);
            imagenProducto = v.findViewById(R.id.imagenProductoItem);
            textNombreLista = v.findViewById(R.id.textNombreItem);
            textDescripcionLista = v.findViewById(R.id.textDescripcionItem);
            textPrecioLista = v.findViewById(R.id.textPrecioItem);
        }
    }

    public List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> productos) {
        listaProductos = productos;
    }

    // Inflamos contenido de los items para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textNombreLista.setText(listaProductos.get(position).getNombre().toString());
        holder.textDescripcionLista.setText(listaProductos.get(position).getDescripcion().toString());
        holder.textPrecioLista.setText(String.valueOf(listaProductos.get(position).getPrecio()));
        holder.imagenProducto.setImageResource(R.drawable.caja_sombreada);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Listener para la lista
    public interface OnItemClickListener {
        public void onItemClick(Producto p, int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
