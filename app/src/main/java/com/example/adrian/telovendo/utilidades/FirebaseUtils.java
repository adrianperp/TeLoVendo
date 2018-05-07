package com.example.adrian.telovendo.utilidades;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.adrian.telovendo.activities.ActivityCuenta;
import com.example.adrian.telovendo.activities.ActivityCuentaEditar;
import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.recyclerview.ProductoAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.UUID;

public class FirebaseUtils {

    Context context;

    private static DatabaseReference databaseRef;
    private static StorageReference storageRef;
    private static FirebaseAuth mAuth;

    // Usuarios
    public final String NODO_USUARIOS = "usuarios";
    public final String CAMPO_NOMBRE = "nombre";
    public final String CAMPO_APELLIDOS = "apellidos";
    public final String CAMPO_EMAIL = "email";

    // Productos
    public final String NODO_PRODUCTOS = "productos";
    public final String NODO_PRODUCTOS_BORRADOR = "productos_borrador";
    public final String CAMPO_NOMBRE_PRODUCTO = "nombre";
    public final String CAMPO_DESCRIPCION_PRODUCTO = "descripcion";
    public final String CAMPO_MARCA_PRODUCTO = "marca";
    public final String CAMPO_MODELO_PRODUCTO = "modelo";
    public final String CAMPO_PRECIO_PRODUCTO = "precio";
    public final String CAMPO_CATEGORIA = "categoria";
    public final String CAMPO_VALORACION_PRODUCTO = "valoracion";
    public final String CAMPO_USUARIO = "usuario";

    public final String STORAGE_PATH_PRODUCTOS = "productos/";
    public final String STORAGE_PATH_PRODUCTOS_BORRADOR = "productos_borrador/";
    public final String STORAGE_PATH_USUARIOS = "usuarios/";

    public FirebaseUtils(Context context){
        this.context = context;
    }

    // ------------------------------------------ Metodos ------------------------------------------
    public void anyadirUsuario(Usuario u){
        // Referencia a la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);

        // Se genera una clave para crear el nodo
        String key = databaseRef.push().getKey();

        // Creando nuevo nodo con la clave
        databaseRef.child(key).setValue(u);
    }

    // Metodo que retorna el usuario que inicia sesion
    public FirebaseUser getUsuarioLogueado() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }

    public void subirProducto(Producto p){
        databaseRef = FirebaseDatabase.getInstance().getReference(NODO_PRODUCTOS);
        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(p);
    }

    public void subirProductoBorrador(Producto p){
        databaseRef = FirebaseDatabase.getInstance().getReference(NODO_PRODUCTOS_BORRADOR);
        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(p);
    }

    public void subirImagenUsuario(String nombreArchivo, Uri uri){
        String dir = STORAGE_PATH_USUARIOS;

        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);

        fileToUpload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println(">>>>>>>>>>>>>>>Imagen subid en storage");
                ActivityCuenta.cargarFotoPerfil();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "La subida de imágenes no se ha podido completar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void subirImagenesProductos(ArrayList<Uri> listaUris, ArrayList<String> listaNombres){
        // Obteniendo la ruta donde iran las imagenes
        String dir = STORAGE_PATH_PRODUCTOS;
        final int total = listaUris.size();
        final ArrayList<String> fileDoneList = new ArrayList<String>();

        try {
            Uri uri;
            for (int i = 0 ; i < total; i++){
                uri = listaUris.get(i);
                final String nombreArchivo = listaNombres.get(i);

                // Referencia de almacenamiento
                StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);

                fileToUpload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.add(nombreArchivo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "Imagen no subida", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        /*int totalRestantes = total - fileDoneList.size();
                        dialog.setMessage("Subiendo fotos: " + totalRestantes + " restantes");*/
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Ha ocurrido una excepción", Toast.LENGTH_SHORT).show();
        }
    }

    public void subirImagenesProductosBorrador(ArrayList<Uri> listaUris, ArrayList<String> listaNombres){
        // Obteniendo la ruta donde iran las imagenes
        String dir = STORAGE_PATH_PRODUCTOS_BORRADOR;
        final int total = listaUris.size();
        final ArrayList<String> fileDoneList = new ArrayList<String>();

        try {
            Uri uri;
            for (int i = 0 ; i < total; i++){
                uri = listaUris.get(i);
                final String nombreArchivo = listaNombres.get(i);

                // Referencia de almacenamiento
                StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);

                fileToUpload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.add(nombreArchivo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "Imagen no subida", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        /*int totalRestantes = total - fileDoneList.size();
                        dialog.setMessage("Subiendo fotos: " + totalRestantes + " restantes");*/
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Ha ocurrido una excepción", Toast.LENGTH_SHORT).show();
        }
    }

    // Retorna un nombre aleatorio junto a la extension de la imagen
    public String getFileName(Uri uri) {
        return UUID.randomUUID().toString() + "." + getImageExt(uri);
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
