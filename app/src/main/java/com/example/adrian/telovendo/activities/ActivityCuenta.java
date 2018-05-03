package com.example.adrian.telovendo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCuenta extends AppCompatActivity {

    private FirebaseUtils firebaseUtils;

    private static CircleImageView imagenPerfil;
    private static TextView textNombre;
    private static TextView textEmail;
    private static TextView textPaisCiudad;
    private static RatingBar ratingBar;

    private static Usuario usuario;
    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        firebaseUtils = new FirebaseUtils(ActivityCuenta.this);

        usuario = ActivityMain.user;

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mi cuenta");

        textNombre = findViewById(R.id.textNombreCuenta);
        textEmail = findViewById(R.id.textEmailCuenta);
        textPaisCiudad = findViewById(R.id.textPaisCiudadCuenta);
        imagenPerfil = findViewById(R.id.imagenFotoCuenta);

        System.out.println("--------------------Cargando interfaz al abrir la activity--------------------");
        cargarInterfaz();
    }

    // Metodo que asigna contenido a la interfaz
    public void cargarInterfaz(){
        if (usuario != null) {
            String nombre = usuario.getNombre();
            String apellidos = usuario.getApellidos();
            String pais = usuario.getPais();
            String ciudad = usuario.getCiudad();

            // Asignamos contenido a los textview
            textNombre.setText(nombre + " " + apellidos);
            textEmail.setText(usuario.getEmail());
            if (pais == null) {
                textPaisCiudad.setText("Desconocido");
            } else if (ciudad == null) {
                textPaisCiudad.setText(pais);
            } else {
                textPaisCiudad.setText(usuario.getPais() + ", " + usuario.getCiudad());
            }
            // Asiganmos la foto al item
            String userImg = usuario.getFotoPerfil();
            if (userImg != null) {
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + usuario.getFotoPerfil());

                Glide.with(ActivityCuenta.this)
                        .using(new FirebaseImageLoader())
                        .load(mStorageRef)
                        .into(imagenPerfil);
            }
        }
        else {
            Toast.makeText(this, "No se pudieron obtener los datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menu
        getMenuInflater().inflate(R.menu.activity_cuenta, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_editar:
                Intent activityCuentaEditar = new Intent(ActivityCuenta.this, ActivityCuentaEditar.class);
                startActivityForResult(activityCuentaEditar, REQUEST_CODE);
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Comprobamos si se ha editado el usuario para actualizar la informacion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Toast.makeText(this, "Se ha actualizado la información", Toast.LENGTH_SHORT).show();
            usuario = ActivityMain.user;
            cargarInterfaz();
        }
    }
}
