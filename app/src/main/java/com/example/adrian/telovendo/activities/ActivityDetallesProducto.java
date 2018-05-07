package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.example.adrian.telovendo.viewpager.ViewPagerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDetallesProducto extends AppCompatActivity {

    private int posicionProducto;
    private Producto p;
    private static Usuario u;
    private static Context context;
    private TextView textNombreDetalles;
    private TextView textDescripcionDetalles;
    private TextView textPrecioDetalles;
    private TextView textCategoriaDetalles;
    private TextView textMarcaDetalles;
    private TextView textModeloDetalles;
    private TextView textNombreUsuario;
    private CircleImageView imagenPerfilUsuario;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPageAdapter;

    private static DatabaseReference databaseRef;
    private static FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        firebaseUtils = new FirebaseUtils(ActivityDetallesProducto.this);
        context = ActivityDetallesProducto.this;

        // Instanciamos las views
        textNombreDetalles = findViewById(R.id.textNombreDetalle);
        textDescripcionDetalles = findViewById(R.id.textDescripcionDetalle);
        textPrecioDetalles = findViewById(R.id.textPrecioDetalle);
        textCategoriaDetalles = findViewById(R.id.textCategoriaDetalle);
        textMarcaDetalles = findViewById(R.id.textMarcaDetalle);
        textModeloDetalles = findViewById(R.id.textModeloDetalle);
        textNombreUsuario = findViewById(R.id.textNombreUsuario);
        imagenPerfilUsuario = findViewById(R.id.imagenFotoUsuario);

        // Recibiendo la posicion del conductor
        posicionProducto = getIntent().getIntExtra("posicion", -1);
        p = ActivityListarCategoria.listaProductos.get(posicionProducto);

        // ViewPager
        viewPager = findViewById(R.id.view_pager);
        getUrlsFotos();

        // Recibir el usuario del producto
        getUsuario(p.getUsuario());

        if (p == null) {
            System.out.println(">>>>>>>PRODUCTO NULL");
        }
        else {
            System.out.println(">>>>>>>PRODUCTO LLEGA CORRECTAMENTE");
            cargarInterfaz();
        }
    }

    // Metodo que asigna el viewpager con las fotos
    private void getUrlsFotos (){
        final int totalFotos = p.getFotos().size();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef;
        final ArrayList<String> listDone = new ArrayList<String>();

        for (int i = 0; i < totalFotos; i++) {
            dateRef = storageRef.child(firebaseUtils.STORAGE_PATH_PRODUCTOS + p.getFotos().get(i));
            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    listDone.add(downloadUrl.toString());
                    System.out.println(">>>>>>>>>>>>>>>>>>>>" + downloadUrl.toString());
                    if (listDone.size() == totalFotos) {
                        // Asignamos las imagenes al viewpager
                        String[] fotos = listDone.toArray(new String[listDone.size()]);
                        viewPageAdapter = new ViewPagerAdapter(context, fotos);
                        viewPager.setAdapter(viewPageAdapter);
                    }
                }
            });
        }
    }

    protected void getUsuario(String email) {
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_USUARIOS);
        Query q = databaseRef.orderByChild(firebaseUtils.CAMPO_EMAIL).equalTo(email);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    u = userSnapshot.getValue(Usuario.class);
                    actualizarDatosUsuario(u);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void actualizarDatosUsuario(Usuario u) {
        textNombreUsuario.setText(u.getNombre() + " " + u.getApellidos());

        if (u.getFotoPerfil() != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + u.getFotoPerfil());
            Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .into(imagenPerfilUsuario);
        }
    }

    protected void cargarInterfaz() {
        textNombreDetalles.setText(p.getNombre());
        textDescripcionDetalles.setText(p.getDescripcion());
        textPrecioDetalles.setText(String.valueOf(p.getPrecio()) + " €");
        textCategoriaDetalles.setText(p.getCategoria());
        // Campos no requeridos
        if (p.getMarca() != null) {
            textMarcaDetalles.setText(p.getMarca());
            textMarcaDetalles.setVisibility(View.VISIBLE);
        }
        if (p.getModelo() != null) {
            textModeloDetalles.setText(p.getModelo());
            textModeloDetalles.setVisibility(View.VISIBLE);
        }
    }
}
