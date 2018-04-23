package com.example.adrian.telovendo.activities;

import android.app.ProgressDialog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.firebase.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityNuevo extends AppCompatActivity {

    private static EditText editNuevoNombre;
    private static EditText editNuevoDescripcion;
    private static EditText editNuevoMarca;
    private static EditText editNuevoModelo;
    private static EditText editNuevoPrecio;
    private static EditText editNuevoPeso;
    private ImageButton botonNuevoAnyadirFoto;
    private Button botonNuevoPublicar;
    private LinearLayout layoutFotos;
    public static final int REQUEST_CODE = 1234;
    private final static String STORAGE_PATH = "productos/";
    private static Intent fotosGaleria;

    private static FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        // Asignamos las views
        editNuevoNombre = findViewById(R.id.editNuevoNombre);
        editNuevoDescripcion = findViewById(R.id.editNuevoDescripcion);
        editNuevoMarca = findViewById(R.id.editNuevoMarca);
        editNuevoModelo = findViewById(R.id.editNuevoModelo);
        editNuevoPrecio = findViewById(R.id.editNuevoPrecio);
        editNuevoPeso = findViewById(R.id.editNuevoPeso);
        botonNuevoAnyadirFoto = findViewById(R.id.botonNuevoAnyadirFoto);
        //botonNuevoGuardarBorrador = findViewById(R.id.botonNuevoGuardarBorrador);
        botonNuevoPublicar = findViewById(R.id.botonNuevoPublicar);
        layoutFotos = findViewById(R.id.layoutFotos);

        firebaseUtils = new FirebaseUtils(this);

        botonNuevoPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyadirProducto();
            }
        });

        botonNuevoAnyadirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen();
            }
        });
    }

    protected void anyadirProducto(){
        // Se suben las imagenes al Storage
        firebaseUtils.subirImagenes(fotosGaleria);
        // Obtenemos el producto a partir de los datos facilitados por el usuario
        Producto p = getProducto();
        // Subimos el producto a la base de datos
        firebaseUtils.subirProducto(p);

        finish();
    }

    // Metodo que abre la galeria para la seleccion de imagenes
    private void elegirImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecciona alguna imagen"), REQUEST_CODE);
    }

    // Metodo que recibe la imagen de la galeria y la inserta en el layout
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (data.getClipData() != null) {
                    // Nos guardamos las imagenes que vamos a subir
                    fotosGaleria = data;
                    // Insertamos las imagenes en el layout
                    anyadirImagenesLayout();
                }
                else if (data.getData() != null) {
                    Toast.makeText(getApplicationContext(), "Selected Single File", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Metodo que anyade una imagen en el layout
    private void anyadirImagenesLayout() {
        int totalItemsSeleccionados = fotosGaleria.getClipData().getItemCount();
        Bitmap bitmap;
        Uri uri;
        ImageView image;
        try {
            for (int i = 0; i < totalItemsSeleccionados; i++) {
                uri = fotosGaleria.getClipData().getItemAt(i).getUri();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image = new ImageView(ActivityNuevo.this);
                // Ajustamos la dimension
                image.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
                // Se asigna el contenido a la imagen
                image.setImageBitmap(bitmap);
                // Lo anyadimos al layout
                layoutFotos.addView(image);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Metodo que recoje los datos de los imput y retorna el producto
    private Producto getProducto () {
        String nombre = editNuevoNombre.getText().toString();
        String descripcion = editNuevoDescripcion.getText().toString();
        String marca = editNuevoMarca.getText().toString();
        String modelo = editNuevoModelo.getText().toString();
        double precio = Double.parseDouble(editNuevoPrecio.getText().toString());
        double peso = Double.parseDouble(editNuevoPeso.getText().toString());
        return new Producto(nombre, descripcion, marca, modelo, precio, peso, getFileNameList());
    }

    // Metodo que retorna el nombre de un archivo a partir de una uri

    private ArrayList<String> getFileNameList(){
        ArrayList<String> listaNombres = new ArrayList<String>();
        int totalItemsSeleccionados = fotosGaleria.getClipData().getItemCount();
        Uri uri;
        for (int i = 0; i < totalItemsSeleccionados; i++) {
            uri = fotosGaleria.getClipData().getItemAt(i).getUri();
            listaNombres.add(firebaseUtils.getFileName(uri));
        }
        return listaNombres;
    }
}