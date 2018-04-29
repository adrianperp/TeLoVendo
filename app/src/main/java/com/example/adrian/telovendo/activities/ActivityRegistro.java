package com.example.adrian.telovendo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityRegistro extends AppCompatActivity {

    private EditText editNombreRegistro;
    private EditText editApellidosRegistro;
    private EditText editEmailRegistro;
    private EditText editContrasenyaRegistro;
    private Button botonRegistrarRegistro;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Obtiendo la instancia FirebaseAuth:
        firebaseAuth = FirebaseAuth.getInstance();

        // Referenciamos las views
        editNombreRegistro = findViewById(R.id.editRegistroNombre);
        editApellidosRegistro = findViewById(R.id.editRegistroApellidos);
        editEmailRegistro = findViewById(R.id.editRegistroEmail);
        editContrasenyaRegistro = findViewById(R.id.editRegistroContrasenya);
        botonRegistrarRegistro = findViewById(R.id.botonRegistrarRegistro);

        firebaseUtils = new FirebaseUtils(this);

        // Creacion de un progressdialog para el inicio de sesion
        progressDialog = new ProgressDialog(this);

        botonRegistrarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
    }

    // Metodo que registra un nuevo usuario
    private void registerUser() {
        final String nombre, apellidos, email, contrasenya;
        nombre = editNombreRegistro.getText().toString();
        apellidos = editApellidosRegistro.getText().toString();
        email = editEmailRegistro.getText().toString();
        contrasenya = editContrasenyaRegistro.getText().toString();

        // Comprobar que se rellenen los campos
        if(nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()){
            Toast.makeText(ActivityRegistro.this, R.string.toastDatosIncorrectos, Toast.LENGTH_SHORT).show();
            return;
        }
        if(contrasenya.length() < 6){
            Toast.makeText(ActivityRegistro.this, R.string.toastContrasenyaCorta, Toast.LENGTH_SHORT).show();
            return;
        }

        // Al loguearse se muestra un progressDialog
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        // Registro de nuevo usuario con email y contrasenya
        firebaseAuth.createUserWithEmailAndPassword(email, contrasenya)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityRegistro.this, R.string.toastRegistroCorrecto,
                                    Toast.LENGTH_SHORT).show();
                            // Anyadimos el nuevo usuario a la base de datos
                            firebaseUtils.anyadirUsuario(new Usuario(nombre, apellidos, email, contrasenya));
                            firebaseUtils.buscarUsuarioEmail(email);
                            // Accedemos a la activity main
                            startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityRegistro.this, R.string.toastRegistroIncorrecto,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
