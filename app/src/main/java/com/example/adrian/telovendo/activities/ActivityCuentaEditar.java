package com.example.adrian.telovendo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adrian.telovendo.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.adrian.telovendo.activities.ActivityMain.user;

public class ActivityCuentaEditar extends AppCompatActivity {

    private static CircleImageView imagenPerfilEditar;
    private EditText editNombreCuenta;
    private EditText editApellidosCuenta;
    private EditText editPaisCuenta;
    private EditText editCiudadCuenta;

    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_editar);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mi cuenta");

        // Instanciamos las views
        imagenPerfilEditar = findViewById(R.id.imagenFotoCuentaEditar);
        editNombreCuenta = findViewById(R.id.editNombreCuenta);
        editApellidosCuenta = findViewById(R.id.editApellidosCuenta);
        editPaisCuenta = findViewById(R.id.editPaisCuenta);
        editCiudadCuenta = findViewById(R.id.editCiudadCuenta);

        cargarDatosInterfaz();

        imagenPerfilEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen();
            }
        });
    }

    protected void elegirImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {
            Uri uri = data.getData();
            // Se actualiza la imagen del layout
            imagenPerfilEditar.setImageURI(uri);
            Toast.makeText(this, "Imagen actualizada", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo que recoje la informacion y la anyade a la activity
    protected void cargarDatosInterfaz(){
        if (user != null) {
            String nombre = user.getNombre();
            String apellidos = user.getApellidos();
            String pais = user.getPais();
            String ciudad = user.getCiudad();

            if (nombre != null) {
                nombre = nombre.trim().replaceAll("\\s+", " ");
                if (!nombre.isEmpty())
                    editNombreCuenta.setText(nombre);
            }
            if (apellidos != null) {
                apellidos = apellidos.trim().replaceAll("\\s+", " ");
                if (!apellidos.isEmpty())
                    editApellidosCuenta.setText(apellidos);
            }
            if (pais != null) {
                pais = pais.trim().replaceAll("\\s+", " ");
                if (!pais.isEmpty())
                    editPaisCuenta.setText(pais);
            }
            if (ciudad != null) {
                ciudad = ciudad.trim().replaceAll("\\s+", " ");
                if (!ciudad.isEmpty())
                    editCiudadCuenta.setText(ciudad);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menu
        getMenuInflater().inflate(R.menu.activity_cuenta_editar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_cuenta_guardar:

                break;
            case android.R.id.home:
                // Mostrar un alert de confirmacion
                if (cambiosRealizados())
                    mostrarAviso();
                else
                    finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Metodo que comprueba si han variado los datos
    public boolean cambiosRealizados() {
        String nombre = (user.getNombre() != null)? user.getNombre() : "";
        String apellidos = (user.getApellidos() != null)? user.getApellidos() : "";
        String pais = (user.getPais() != null)? user.getPais() : "";
        String ciudad = (user.getCiudad() != null)? user.getCiudad() : "";
        String nuevoNombre = editNombreCuenta.getText().toString();
        String nuevoApellidos = editApellidosCuenta.getText().toString();
        String nuevoPais = editPaisCuenta.getText().toString();
        String nuevoCiudad = editCiudadCuenta.getText().toString();
        if (!nombre.equals(nuevoNombre) || !apellidos.equals(nuevoApellidos)
                || !pais.equals(nuevoPais) || !ciudad.equals(nuevoCiudad)){
            return true;
        }
        return false;
    }

    public void mostrarAviso() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityCuentaEditar.this);
        alert.setTitle("Descartar cambios");
        alert.setMessage("¿Desea descartar los cambios realizados?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
}
