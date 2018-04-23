package com.example.adrian.telovendo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.adrian.telovendo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCuenta extends AppCompatActivity {

    private static CircleImageView imagenPerfil;
    private static TextView textNombre;
    private static TextView textEmail;
    private static TextView textPaisCiudad;
    private static RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menu
        getMenuInflater().inflate(R.menu.activity_cuenta, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
