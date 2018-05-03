package com.example.adrian.telovendo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {

    private EditText editEmail;
    private EditText editContrasenya;
    private Button botonAcceder;
    private Button botonRegistrar;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtiendo la instancia FirebaseAuth:
        firebaseAuth = FirebaseAuth.getInstance();

        // Referenciamos las views
        editEmail = findViewById(R.id.editUsuarioLogin);
        editContrasenya = findViewById(R.id.editPasswordLogin);
        botonAcceder = findViewById(R.id.buttonAccederLogin);
        botonRegistrar = findViewById(R.id.buttonRegistrarLogin);

        // Creacion de un progressdialog para el inicio de sesion
        progressDialog = new ProgressDialog(this);

        firebaseUtils = new FirebaseUtils(this);

        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamada al metodo loguear
                loginUser();
            }
        });

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrimos la activity registro
                Intent activityRegistro = new Intent(ActivityLogin.this, ActivityRegistro.class);
                startActivity(activityRegistro);
            }
        });

        //LOGUEAMOS AL USER EN PLAN GUARRO
        editEmail.setText("user@gmail.com");
        editContrasenya.setText("123456");
        loginUser();


    }

    private void loginUser() {
        // Obtenemos las credenciales de usuario
        final String email = editEmail.getText().toString().trim();
        String contrasenya = editContrasenya.getText().toString().trim();

        // Comprobamos que se hayan introducido datos
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(contrasenya)){
            Toast.makeText(this, R.string.toastDatosIncorrectos, Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        // Al loguearse se muestra un progressDialog
        progressDialog.setMessage("Iniciando sesion...");
        progressDialog.show();

        // Inicio de sesion con email y contrasenya
        firebaseAuth.signInWithEmailAndPassword(email, contrasenya)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        // Asignamos el usuario logueado
                        //firebaseUtils.buscarUsuarioEmail(email);
                        Intent mainActivity = new Intent(ActivityLogin.this, ActivityMain.class);
                        startActivity(mainActivity);
                    }
                    else {
                        Toast.makeText(ActivityLogin.this, R.string.toastErrorLogin, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
    }
}
