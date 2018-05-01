package com.example.adrian.telovendo.activities;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCuenta extends AppCompatActivity {

    private static CircleImageView imagenPerfil;
    private static TextView textNombre;
    private static TextView textEmail;
    private static TextView textPaisCiudad;
    private static RatingBar ratingBar;

    private FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        firebaseUtils = new FirebaseUtils(ActivityCuenta.this);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mi cuenta");

        textNombre = findViewById(R.id.textNombreCuenta);
        textEmail = findViewById(R.id.textEmailCuenta);
        textPaisCiudad = findViewById(R.id.textPaisCiudadCuenta);

        cargarInterfaz();
    }

    // Metodo que asigna contenido a la interfaz
    public void cargarInterfaz(){
        String nombre = ActivityMain.user.getNombre();
        String apellidos = ActivityMain.user.getApellidos();
        String pais = ActivityMain.user.getPais();
        String ciudad = ActivityMain.user.getCiudad();

        // Asignamos contenido a los textview
        textNombre.setText(nombre + " " + apellidos);
        textEmail.setText(ActivityMain.user.getEmail());
        if(pais == null){
            textPaisCiudad.setText("Desconocido");
        }
        else if (ciudad == null) {
            textPaisCiudad.setText(pais);
        }
        else{
            textPaisCiudad.setText(ActivityMain.user.getPais() + ", " + ActivityMain.user.getCiudad());
        }
        // Asiganmos la foto al item
        String userImg = ActivityMain.user.getFotoPerfil();
        if (userImg != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + ActivityMain.user.getFotoPerfil());

            Glide.with(ActivityListarCategoria.context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(imagenPerfil);
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
                startActivity(new Intent(ActivityCuenta.this, ActivityCuentaEditar.class));
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    
}
