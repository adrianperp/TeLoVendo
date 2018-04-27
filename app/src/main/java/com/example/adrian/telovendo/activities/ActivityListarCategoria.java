package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Producto;
import com.example.adrian.telovendo.firebase.FirebaseUtils;
import com.example.adrian.telovendo.recyclerview.ProductoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityListarCategoria extends AppCompatActivity {

    RecyclerView recyclerProductos;
    RecyclerView.LayoutManager miLayoutManager;
    ProductoAdapter productoAdapter;
    Producto p;
    DatabaseReference ref;
    FirebaseUtils firebaseUtils;
    public List<Producto> listaProductos;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categoria);

        listaProductos = new ArrayList<Producto>();
        firebaseUtils = new FirebaseUtils(ActivityListarCategoria.this);

        context = ActivityListarCategoria.this;

        // Adaptador
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProductos.setItemAnimator(new DefaultItemAnimator());
        recyclerProductos.setAdapter(productoAdapter);

        // Cargar datos desde la base de datos
        ref = FirebaseDatabase.getInstance().getReference("productos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaProductos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    p = ds.getValue(Producto.class);
                    listaProductos.add(p);
                }
                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Para responder al evento de clic...
        productoAdapter.setOnItemListener(new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto p, int position) {

            }
        });
    }
}
