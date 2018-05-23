package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActivityListarCategoria extends AppCompatActivity {

    // Recycler
    RecyclerView recyclerProductos;
    ProductoAdapter productoAdapter;

    String categoria;
    Producto p;

    DatabaseReference ref;
    FirebaseUtils firebaseUtils;

    // searchMap
    HashMap<String, String> searchMap;

    public static List<Producto> listaProductos = new ArrayList<Producto>();

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categoria);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recibiendo los datos de busqueda
        searchMap = (HashMap<String, String>) getIntent().getSerializableExtra("searchMap");

        // Recibiendo la categoria seleccionada
        categoria = getIntent().getStringExtra("categoria");

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

        ref = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_PRODUCTOS);
        // METODO 1: Productos de una categoria sin filtros
        if (searchMap == null) {
            // Se anyade titulo a la toolbar
            getSupportActionBar().setTitle(categoria);

            // Realizamos una consulta a la base de datos y recuperamos los productos de una categoria
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
        // METODO 2: Busqueda personalizada
        else {
            // Se anyade titulo a la toolbar
            getSupportActionBar().setTitle("Resultados");

            // Busqueda de productos aplicando filtros
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Producto p;
                    // Vaciamos la lista
                    listaProductos.clear();
                    // Recuperamos los productos
                    for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                        p = datasnap.getValue(Producto.class);
                        if (isProductoBuscado(p)) {
                            //System.out.println("**************PRODUCTO VALIDADO " + p.getNombre());
                            listaProductos.add(p);
                        }
                    }
                    if (listaProductos.isEmpty()) {
                        Toast.makeText(ActivityListarCategoria.this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
                    }
                    productoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public boolean isProductoBuscado (Producto p) {
        boolean valid = true;
        if (searchMap.containsKey("categoria")) {
            if (!(searchMap.get("categoria").equals("Todas") || searchMap.get("categoria").equals(p.getCategoria()))) {
                valid = false;
            }

        }
        if (searchMap.containsKey("provincia")) {
            if (!(p.getProvincia() != null && (p.getProvincia().equals(searchMap.get("provincia")) || searchMap.get("provincia").equals("Cualquiera")))) {
                valid = false;
            }
        }
        if (searchMap.containsKey("textoBusqueda")) {
            if (!(p.getNombre().toLowerCase().contains(searchMap.get("textoBusqueda").toLowerCase()) || p.getDescripcion().toLowerCase().contains(searchMap.get("textoBusqueda").toLowerCase()))) {
                valid = false;
            }
        }
        if (searchMap.containsKey("precioDesde")) {
            Double precioDesde = Double.parseDouble(searchMap.get("precioDesde"));
            if (precioDesde > p.getPrecio()) {
                valid = false;
            }
        }
        if (searchMap.containsKey("precioHasta")) {
            Double precioHasta = Double.parseDouble(searchMap.get("precioHasta"));
            if (precioHasta < p.getPrecio()) {
                valid = false;
            }
        }
        if (searchMap.containsKey("marca")) {
            if (!(p.getMarca() != null && p.getMarca().equalsIgnoreCase(searchMap.get("marca")))) {
                valid = false;
            }
        }
        if (searchMap.containsKey("modelo")) {
            if (!(p.getModelo() != null && p.getModelo().equalsIgnoreCase(searchMap.get("modelo")))) {
                valid = false;
            }
        }

        return valid;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_listar_categoria, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_ordenar:

                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
