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

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.example.adrian.telovendo.utilidades.FechasUtils.getDate;
import static com.example.adrian.telovendo.utilidades.FechasUtils.getDateDif;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private static FirebaseUtils firebaseUtils = new FirebaseUtils(ActivityListarCategoria.context);

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Referenciamos objetos objeto de la position que nos pasan
        private ImageView imagenProducto;
        private TextView textNombreLista;
        private TextView textDescripcionLista;
        private TextView textPrecioLista;
        private TextView textFechaPublicacionItem;

        public ViewHolder(View v) {
            super(v);
            imagenProducto = v.findViewById(R.id.imagenProductoItem);
            textNombreLista = v.findViewById(R.id.textNombreItem);
            textDescripcionLista = v.findViewById(R.id.textDescripcionItem);
            textPrecioLista = v.findViewById(R.id.textPrecioItem);
            textFechaPublicacionItem = v.findViewById(R.id.textFechaPublicacionItem);
        }

        public void bind(Producto p) {
            // Comprobar que el texto no se exceda del maximo
            String nombre = p.getNombre();
            String descripcion = p.getDescripcion();
            // Nombre
            if (nombre.length() > 14) { // 14 carac
                textNombreLista.setText(nombre.substring(0,14) + "...");
            }
            else {
                textNombreLista.setText(nombre);
            }
            // Descripcion
            if (descripcion.length() > 82) {// 82 carac
                textDescripcionLista.setText(descripcion.substring(0, 82) + "...");
            }
            else {
                textDescripcionLista.setText(descripcion);
            }
            // Precio
            textPrecioLista.setText(String.valueOf(p.getPrecio()) + " €");
            // Fecha
            try {
                textFechaPublicacionItem.setText(getDateDif(new Date(), getDate(p.getFechaPublicado(), p.getHoraPublicado())));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
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
