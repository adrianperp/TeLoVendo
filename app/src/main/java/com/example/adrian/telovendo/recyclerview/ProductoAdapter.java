package com.example.adrian.telovendo.recyclerview;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.activities.ActivityListarCategoria;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private static FirebaseUtils firebaseUtils = new FirebaseUtils(ActivityListarCategoria.context);

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

        public void bind(Producto p) {
            // Comprobar que el texto no se exceda del maximo
            String nombre = p.getNombre();
            String descripcion = p.getDescripcion();
            if (nombre.length() > 15) { // 15 carac
                textNombreLista.setText(nombre.substring(0,15) + "...");
            }
            else {
                textNombreLista.setText(nombre);
            }
            if (descripcion.length() > 160) {// 160 carac
                textDescripcionLista.setText(descripcion.substring(0, 160) + "...");
            }
            else {
                textDescripcionLista.setText(descripcion);
            }
            textPrecioLista.setText(String.valueOf(p.getPrecio()) + " €");
            // Asiganmos la foto al item
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_PRODUCTOS + p.getFotos().get(0));
            Glide.with(ActivityListarCategoria.context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(imagenProducto);
        }
    }

    public List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> productos) {
        listaProductos = productos;
    }

    // Inflamos contenido de los items para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(mListener!=null) {
                    mListener.onItemClick(listaProductos.get(position), position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.bind(listaProductos.get(position));
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
