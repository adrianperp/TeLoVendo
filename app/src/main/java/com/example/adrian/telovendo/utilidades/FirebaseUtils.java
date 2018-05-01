package com.example.adrian.telovendo.utilidades;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.recyclerview.ProductoAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private static DatabaseReference db;
    private static StorageReference ref;

    // Usuarios
    public final String NODO_USUARIOS = "usuarios";
    public final String CAMPO_NOMBRE = "nombre";
    public final String CAMPO_APELLIDOS = "apellidos";
    public final String CAMPO_EMAIL = "email";

    // Productos
    public final String NODO_PRODUCTOS = "productos";
    public final String CAMPO_NOMBRE_PRODUCTO = "nombre";
    public final String CAMPO_DESCRIPCION_PRODUCTO = "descripcion";
    public final String CAMPO_MARCA_PRODUCTO = "marca";
    public final String CAMPO_MODELO_PRODUCTO = "modelo";
    public final String CAMPO_PRECIO_PRODUCTO = "precio";
    public final String CAMPO_CATEGORIA = "categoria";
    public final String CAMPO_VALORACION_PRODUCTO = "valoracion";

    public final String STORAGE_PATH_PRODUCTOS = "productos/";
    public final String STORAGE_PATH_USUARIOS = "usuarios/";

    public FirebaseUtils(Context context){
        this.context = context;
    }

    // ------------------------------------------ Metodos ------------------------------------------
    public void anyadirUsuario(Usuario u){
        // Referencia a la base de datos
        db = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);

        // Se genera una clave para crear el nodo
        String key = db.push().getKey();

        // Creando nuevo nodo con la clave
        db.child(key).setValue(u);
    }

    // Metodo que asigna el usuario que inicia sesion
    public void buscarUsuarioEmail(String email){
        db = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);

        Query q = db.orderByChild(CAMPO_EMAIL).equalTo(email);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ActivityMain.user = ds.getValue(Usuario.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void subirProducto(Producto p){
        db = FirebaseDatabase.getInstance().getReference(NODO_PRODUCTOS);
        String key = db.push().getKey();
        db.child(key).setValue(p);
    }

    public void actualizarUsuario(Usuario u) {

    }

    public void subirImagenUsuario(Uri uri){
        String dir = STORAGE_PATH_USUARIOS;
        final String nombreArchivo = getFileName(uri);
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);

        fileToUpload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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

                        Toast.makeText(context, "La subida de imágenes no se ha podido completar", Toast.LENGTH_SHORT).show();

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

    public ArrayList<Producto> getListaProductos(String categoria, final ProductoAdapter adapter){
        final ArrayList listaProductos = new ArrayList<Producto>();

        System.out.println(">>>>>>>>>>>>>>>>>Entra en getListaProductos");
        db = FirebaseDatabase.getInstance().getReference(NODO_PRODUCTOS);
        Query q = db.orderByChild(CAMPO_CATEGORIA).equalTo(categoria);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto p;
                // Recuperamos los productos de esa categoria
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    p = datasnap.getValue(Producto.class);
                    listaProductos.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return listaProductos;
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
