package com.example.adrian.telovendo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityCuentaEditar extends AppCompatActivity {

    private static CircleImageView imagenPerfilEditar;
    private static EditText editNombreCuenta;
    private static EditText editApellidosCuenta;
    private static EditText editPaisCuenta;
    private static EditText editCiudadCuenta;

    private static FirebaseUtils firebaseUtils;
    private static Usuario user;
    private static Uri uri;
    private static Context context;

    DatabaseReference databaseRef;

    public static final int SELECT_PIC_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_editar);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mi cuenta");

        firebaseUtils = new FirebaseUtils(ActivityCuentaEditar.this);
        context = ActivityCuentaEditar.this;
        user = ActivityMain.user;

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

    protected void actualizarUsuario() {
        final Usuario newUser = getUsuario();
        user = newUser;

        // Se actualiza el usuario en la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_USUARIOS);
        // Creando query
        Query q = databaseRef.orderByChild(firebaseUtils.CAMPO_EMAIL).equalTo(newUser.getEmail());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String clave;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    clave = ds.getKey();
                    // Actualizamos el usuario
                    databaseRef.child(clave).setValue(newUser);
                    System.out.println(">>>>>>>>>>>USUARIO ACTUALIZADO");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        // Se sube la nueva foto
        if (newUser.getFotoPerfil() != null) {
            firebaseUtils.subirImagenUsuario(newUser.getFotoPerfil(), uri);
        }

        // Devolvemos resultado
        Intent returnIntent = new Intent();
        // Usuario a retornar
        returnIntent.putExtra("usuario", newUser);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    // Retorna un usuario a partir de la informacion proporcionada por el usuario
    private static Usuario getUsuario(){
        System.out.println("----------------entra en get usuario-------------");
        Usuario u = new Usuario();
        String email = user.getEmail();
        String contrasenya = user.getContrasenya();
        String nombre = editNombreCuenta.getText().toString().trim();
        String apellidos = editApellidosCuenta.getText().toString().trim();
        String pais = editPaisCuenta.getText().toString().trim();
        String ciudad = editCiudadCuenta.getText().toString().trim();

        // Construimos el usuario
        u.setEmail(email);
        u.setContrasenya(contrasenya);
        if (!nombre.isEmpty())
            u.setNombre(nombre);
        if (!apellidos.isEmpty())
            u.setApellidos(apellidos);
        if (!pais.isEmpty())
            u.setPais(pais);
        if (!ciudad.isEmpty())
            u.setCiudad(ciudad);
        if(uri != null) {
            String foto = firebaseUtils.getFileName(uri);
            u.setFotoPerfil(foto);
        }

        return u;
    }

    protected void elegirImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PIC_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PIC_CODE && resultCode == RESULT_OK && data.getData() != null) {
            uri = data.getData();
            // Se actualiza la imagen del layout
            imagenPerfilEditar.setImageURI(uri);
        }
    }

    // Metodo que recoje la informacion y la anyade a la activity
    protected void cargarDatosInterfaz(){
        if (user != null) {
            String nombre = user.getNombre();
            String apellidos = user.getApellidos();
            String pais = user.getPais();
            String ciudad = user.getCiudad();
            String foto = user.getFotoPerfil();
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
            if (foto != null) {
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + user.getFotoPerfil());

                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(mStorageRef)
                        .into(imagenPerfilEditar);
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
                // Comprobar que se hayan hecho cambios
                if (cambiosRealizados()) {
                    actualizarUsuario();
                    ActivityMain.user = user;
                }
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
                || !pais.equals(nuevoPais) || !ciudad.equals(nuevoCiudad) ||
                uri != null){
            return true;
        }
        return false;
    }

    public void mostrarAviso() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
