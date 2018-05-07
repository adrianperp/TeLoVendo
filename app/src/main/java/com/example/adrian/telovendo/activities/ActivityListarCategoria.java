package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.example.adrian.telovendo.recyclerview.ProductoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityListarCategoria extends AppCompatActivity {

    RecyclerView recyclerProductos;
    ProductoAdapter productoAdapter;
    String categoria;
    Producto p;
    DatabaseReference ref;
    FirebaseUtils firebaseUtils;
    public static List<Producto> listaProductos = new ArrayList<Producto>();

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categoria);

        // Recibiendo la categoria seleccionada
        categoria = getIntent().getStringExtra("categoria");

        getSupportActionBar().setTitle(categoria);

        context = ActivityListarCategoria.this;

        firebaseUtils = new FirebaseUtils(ActivityListarCategoria.this);

        // Adaptador
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProductos.setItemAnimator(new DefaultItemAnimator());
        recyclerProductos.setAdapter(productoAdapter);

        // Para responder al evento de clic...
        productoAdapter.setOnItemListener(new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto p, int position) {
                System.out.println(">>>>>>>>>>>>>Entra en onitemclick");
                Intent activityDetallesProducto = new Intent(ActivityListarCategoria.this, ActivityDetallesProducto.class);
                activityDetallesProducto.putExtra("posicion", position);
                startActivity(activityDetallesProducto);
            }
        });

        // Realizamos una consulta a la base de datos y recuperamos los productos de una categoria
        ref = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_PRODUCTOS);
        Query q = ref.orderByChild(firebaseUtils.CAMPO_CATEGORIA).equalTo(categoria);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto p;
                // Vaciamos la lista
                listaProductos.clear();
                // Recuperamos los productos de esa categoria
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    p = datasnap.getValue(Producto.class);
                    listaProductos.add(p);
                }
                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
