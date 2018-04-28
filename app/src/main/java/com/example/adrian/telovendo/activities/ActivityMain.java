package com.example.adrian.telovendo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Categoria;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.recyclerview.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Usuario user;
    private Button botonPerfil;
    private RecyclerView listaCategorias;
    private RecyclerViewAdapter adaptadorCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------------------Lanzamos la activity listar a lo GUARRO------------------
        startActivity(new Intent(ActivityMain.this, ActivityListarCategoria.class));

        // RecyclerView
        listaCategorias = findViewById(R.id.recyclerCategorias);
        listaCategorias.setLayoutManager(new LinearLayoutManager(this));

        adaptadorCategorias = new RecyclerViewAdapter(obtenerCategorias());
        listaCategorias.setAdapter(adaptadorCategorias);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent activityNuevo = new Intent(ActivityMain.this, ActivityNuevo.class);
                startActivity(activityNuevo);
            }
        });

        // Menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // rellenar lista de categorias
    public List<Categoria> obtenerCategorias(){
        int[] idsFotos = {0, R.drawable.cat_motor, R.drawable.cat_hogar, R.drawable.cat_electronica,
                R.drawable.cat_juegos, R.drawable.cat_hobbies, R.drawable.cat_ropa, R.drawable.cat_libros,
                R.drawable.cat_infantil, R.drawable.cat_animales, R.drawable.cat_electrodomesticos};
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        String[] arrayCat = getResources().getStringArray(R.array.array_categorias);

        // Se descarta la primera opcion (Selecciona)
        for (int i = 1; i < arrayCat.length; i++)   {
            listaCategorias.add(new Categoria(arrayCat[i],idsFotos[i]));
        }
        return listaCategorias;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        MenuItem item = menu.findItem(R.id.itemBuscar);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra a medida que se va escribiendo
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Handle the camera action
        } else if (id == R.id.nav_notificaciones) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_cuenta) {
            startActivity(new Intent(ActivityMain.this, ActivityCuenta.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
