package com.example.adrian.telovendo.firebase;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adrian.telovendo.activities.ActivityMain;
import com.example.adrian.telovendo.activities.ActivityNuevo;
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

public class FirebaseUtils {

    Context context;

    private static DatabaseReference db;
    private static StorageReference ref;

    // Usuarios
    private final static String NODO_USUARIOS = "usuarios";
    private final static String CAMPO_NOMBRE = "nombre";
    private final static String CAMPO_APELLIDOS = "apellidos";
    private final static String CAMPO_EMAIL = "email";


    // Productos
    private final static String NODO_PRODUCTOS = "productos";
    private final static String CAMPO_NOMBRE_PRODUCTO = "nombre";
    private final static String CAMPO_DESCRIPCION_PRODUCTO = "descripcion";
    private final static String CAMPO_MARCA_PRODUCTO = "marca";
    private final static String CAMPO_MODELO_PRODUCTO = "modelo";
    private final static String CAMPO_PRECIO_PRODUCTO = "precio";
    private final static String CAMPO_VALORACION_PRODUCTO = "valoracion";

    private final static String STORAGE_PATH = "productos/";

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

    public void subirImagenes(ArrayList<Uri> listaUris){
        // Obteniendo la ruta donde iran las imagenes
        String dir = STORAGE_PATH;
        final int total = listaUris.size();
        System.out.println(">>>>>>>>>>>>>>>total uris" + " hay " + total + " uris");
        final ArrayList<String> fileDoneList = new ArrayList<String>();

        try {
            // Progress dialog
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle("Subiendo imagen");
            dialog.show();
            System.out.println(">>>>>>>>>>>>>>>dialogo" + "se muestra dialogo");

            for (Uri uri : listaUris) {
                System.out.println(">>>>>>>>>>>>>>>uri num " + listaUris.indexOf(uri) + " " + uri.toString());
                final String nombreArchivo = getFileName(uri);
                System.out.println(">>>>>>>>>>>>>>>nombre archivo" + nombreArchivo);
                // Referencia de almacenamiento
                StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                System.out.println(">>>>>>>>>>>>>>>storage reference" + "creando referencia de almacenamiento");
                // ruta / nombre del archivo
                StorageReference fileToUpload = mStorage.child(dir).child(nombreArchivo);
                System.out.println(">>>>>>>>>>>>>>>filetoupload" + "indicando donde se subira el archivo");
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
                        int totalRestantes = total - fileDoneList.size();
                        dialog.setMessage("Subiendo fotos: " + totalRestantes + " restantes");
                    }
                });
            }
            dialog.dismiss();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Ha ocurrido una excepción", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /*public ArrayList<Producto> getListaProductos(final ProductoAdapter adapter){
        final ArrayList<Producto> listaProductos = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference(NODO_PRODUCTOS);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto p;
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    // Vamos recogiendo productos
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
    }*/
}
