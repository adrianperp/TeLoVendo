package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.utilidades.FechasUtils;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;

import java.util.ArrayList;
import java.util.Date;

public class ActivityNuevo extends AppCompatActivity {

    private EditText editNuevoNombre;
    private EditText editNuevoDescripcion;
    private EditText editNuevoMarca;
    private EditText editNuevoModelo;
    private EditText editNuevoPrecio;
    private EditText editNuevoPeso;
    private Spinner spinnerNuevoCategorias;
    private ImageButton botonNuevoAnyadirFoto;
    private LinearLayout layoutFotos;

    public static final int REQUEST_CODE = 1234;
    private final static String STORAGE_PATH = "productos/";

    private static FirebaseUtils firebaseUtils;
    private ArrayList<Uri> listaUris;
    private ArrayList<Uri> listaUrisLayout;

    public static Context context;

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
        spinnerNuevoCategorias = findViewById(R.id.spinnerNuevoCategorias);
        botonNuevoAnyadirFoto = findViewById(R.id.botonNuevoAnyadirFoto);
        layoutFotos = findViewById(R.id.layoutFotos);

        context = ActivityNuevo.this;
        firebaseUtils = new FirebaseUtils(ActivityNuevo.this);

        listaUris = new ArrayList<Uri>();
        listaUrisLayout = new ArrayList<Uri>();

        botonNuevoAnyadirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menu
        getMenuInflater().inflate(R.menu.activity_nuevo, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_publicar:
                // Comprobar que se hayan introducido datos antes de anyadir producto
                if(isDatosValidos()) {
                    anyadirProducto();
                }
                else {
                    Toast.makeText(ActivityNuevo.this, "Algunos campos obligatorios están vacíos", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Comprueba que los datos hayan sido introducidos correctamente
    public boolean isDatosValidos(){
        String nombre = editNuevoNombre.getText().toString().trim();
        String descripcion = editNuevoDescripcion.getText().toString().trim();
        String precio = editNuevoPrecio.getText().toString().trim();
        String categoria = spinnerNuevoCategorias.getSelectedItem().toString();
        if(nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || categoria.equals("Selecciona")) {
            return false;
        }
        return true;
    }

    // Sube el producto a la base de datos
    protected void anyadirProducto(){
        // Obtenemos el producto a partir de los datos facilitados por el usuario
        Producto p = getProducto();
        p.setFechaPublicado(FechasUtils.getFechaActual());
        p.setHoraPublicado(FechasUtils.getHoraActual());
        // Se suben las imagenes al Storage
        firebaseUtils.subirImagenes(listaUris, p.getFotos());
        // Subimos el producto a la base de datos
        firebaseUtils.subirProducto(p);
        Toast.makeText(this, "Producto subido", Toast.LENGTH_SHORT).show();
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

    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                actualizarListaUris(data);
                // Insertamos las imagenes en el layout
                anyadirImagenesLayout();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Inserta uris cada vez que se anyade una foto nueva
    private void actualizarListaUris(Intent fotosGaleria){
        Uri uri;
        // Comprobamos cuantas fotos se han seleccionado en la galeria
        if (fotosGaleria.getData() != null) {
            uri = fotosGaleria.getData();
            listaUris.add(uri);
        }
        else {
            int totalItemsSeleccionados = fotosGaleria.getClipData().getItemCount();
            for (int i = 0; i < totalItemsSeleccionados; i++) {
                uri = fotosGaleria.getClipData().getItemAt(i).getUri();
                listaUris.add(uri);
            }
        }
    }

    // Inserta las imagenes en forma de vista en el layout
    private void anyadirImagenesLayout() {
        for (Uri uri : listaUris) {
            // Comprobamos que la foto no este ya insertada
            if (!listaUrisLayout.contains(uri)){
                listaUrisLayout.add(uri);
                incrustarImagen(uri);
            }
        }
    }

    // Metodo que anyade una imagen en el layout
    private void incrustarImagen(Uri uri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ImageView image = new ImageView(ActivityNuevo.this);
            // Ajustamos la dimension
            image.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
            // Se asigna el contenido a la imagen
            image.setImageBitmap(bitmap);
            // Lo anyadimos al layout
            layoutFotos.addView(image);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo que recoje los datos de los imput y retorna el producto
    private Producto getProducto () {
        String nombre = editNuevoNombre.getText().toString().trim();
        String descripcion = editNuevoDescripcion.getText().toString().trim();
        String marca = editNuevoMarca.getText().toString().trim();
        String modelo = editNuevoModelo.getText().toString().trim();
        double precio = Double.parseDouble(editNuevoPrecio.getText().toString());
        String categoria = spinnerNuevoCategorias.getSelectedItem().toString();
        // Comprobaciones de campos no obligatorios
        double peso = (editNuevoPeso.getText().toString().isEmpty()) ?
                -1 : Double.parseDouble(editNuevoPeso.getText().toString());

        if (marca.isEmpty()) {
            marca = null;
        }
        if (modelo.isEmpty()) {
            modelo = null;
        }

        return new Producto(nombre, descripcion, marca, modelo, precio, peso, categoria, getFileNameList());
    }

    // Metodo que retorna los nombres de las fotos para la creacion del producto
    private ArrayList<String> getFileNameList(){
        ArrayList<String> listaNombres = new ArrayList<String>();

        for (Uri uri : listaUris) {
            listaNombres.add(firebaseUtils.getFileName(uri));
        }
        return listaNombres;
    }
}