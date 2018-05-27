package com.example.adrian.telovendo.utilidades;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.activities.ActivityCuenta;
import com.example.adrian.telovendo.activities.ActivityCuentaEditar;
import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.clases.Chat;
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
    public final String CAMPO_LISTA_CHATS = "listaChats";

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

    // Chat
    public final String NODO_CHATS = "chats";
    public final String CAMPO_LISTA_MENSAJES = "listaMensajes";

    public final String STORAGE_PATH_PRODUCTOS = "productos/";
    public final String STORAGE_PATH_PRODUCTOS_BORRADOR = "productos_borrador/";
    public final String STORAGE_PATH_USUARIOS = "usuarios/";

    public FirebaseUtils(Context context){
        this.context = context;
    }

    // ------------------------------------------ Metodos Usuarios ------------------------------------------
    public void anyadirUsuario(Usuario u){
        // Referencia a la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);
        // Obtenemos la key
        String key = u.getIdUsuario();
        // Creando nuevo nodo con la clave
        databaseRef.child(key).setValue(u);
    }

    // Metodo que retorna el usuario que inicia sesion
    public FirebaseUser getUsuarioLogueado() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }

    // Metodo que actualiza los chats de dos usuarios
    public void actualizarChatsUsuarios(final Usuario u1, final Usuario u2) {
        // Creamos la referencia a la base de datos
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS).child(u1.getIdUsuario()).child(CAMPO_LISTA_CHATS);
        ref.setValue(u1.getListaChats());

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS).child(u2.getIdUsuario()).child(CAMPO_LISTA_CHATS);
        ref2.setValue(u2.getListaChats());
    }

    public void anyadirChat(Chat chat) {
        databaseRef = FirebaseDatabase.getInstance().getReference(NODO_CHATS);
        String key = chat.getChatId();
        databaseRef.child(key).setValue(chat);
    }

    public void subirImagenUsuario(String nombreArchivo, Uri uri){
        String dir = STORAGE_PATH_USUARIOS;

        if (uri != null) {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);

            fileToUpload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ActivityCuenta.cargarFotoPerfil();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, R.string.toast_subida_fallida, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // ------------------------------------------ Metodos Productos ------------------------------------------

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

                        Toast.makeText(context, R.string.toast_imagen_nosubida, Toast.LENGTH_SHORT).show();

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
            Toast.makeText(context, R.string.toast_excepcion, Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(context, R.string.toast_imagen_nosubida, Toast.LENGTH_SHORT).show();

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
            Toast.makeText(context, R.string.toast_excepcion, Toast.LENGTH_SHORT).show();
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
