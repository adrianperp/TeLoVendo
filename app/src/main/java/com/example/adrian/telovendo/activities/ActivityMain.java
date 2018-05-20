package com.example.adrian.telovendo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adrian.telovendo.R;
import com.example.adrian.telovendo.clases.Categoria;
import com.example.adrian.telovendo.clases.Usuario;
import com.example.adrian.telovendo.recyclerview.ProductoAdapter;
import com.example.adrian.telovendo.recyclerview.RecyclerViewAdapter;
import com.example.adrian.telovendo.utilidades.FirebaseUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FirebaseUser firebaseUser;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static Usuario user;
    public static Context context;
    private static FirebaseUtils firebaseUtils;

    // Contenido
    private Button botonPerfil;
    private Toolbar toolbar;

    // Recycler
    private RecyclerView listaCategorias;
    private RecyclerViewAdapter adaptadorCategorias;

    // Drawer
    private NavigationView navigationView;
    private static ImageView imageUserDrawer;
    private static TextView textNombreDrawer;
    private static TextView textEmailDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = ActivityMain.this;
        firebaseUtils = new FirebaseUtils(ActivityMain.this);

        // Drawer
        navigationView = findViewById(R.id.nav_view);
        imageUserDrawer = navigationView.getHeaderView(0).findViewById(R.id.imageUserDrawer);
        textNombreDrawer = navigationView.getHeaderView(0).findViewById(R.id.textNombreDrawer);
        textEmailDrawer = navigationView.getHeaderView(0).findViewById(R.id.textEmailDrawer);

        // Recibiendo usuario logueado
        firebaseUser = firebaseUtils.getUsuarioLogueado();
        // Actualizamos user
        getMiUsuario(firebaseUser);

        // RecyclerView
        listaCategorias = findViewById(R.id.recyclerCategorias);
        listaCategorias.setLayoutManager(new LinearLayoutManager(this));
        adaptadorCategorias = new RecyclerViewAdapter(obtenerCategorias());
        listaCategorias.setAdapter(adaptadorCategorias);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Categorías");

        // Boton flotante
        FloatingActionButton fab = findViewById(R.id.fab);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        // Respuesta al pulsar sobre algun elemento de la lista
        adaptadorCategorias.setOnItemListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria c, int position) {
                Intent activityDetalles = new Intent(ActivityMain.this, ActivityListarCategoria.class);
                activityDetalles.putExtra("categoria", c.getNombre());
                startActivity(activityDetalles);
            }
        });
    }

    public static void cargarInfoUsuarioDrawer() {

        System.out.println("textNombreDrawer == null?? " + (textNombreDrawer == null));
        System.out.println("textEmailDrawer == null?? " + (textEmailDrawer == null));

        if (textNombreDrawer != null) textNombreDrawer.setText(user.getNombre() + " " + user.getApellidos());
        if (textEmailDrawer != null) textEmailDrawer.setText(user.getEmail());
        // Comprobar que el usuario tiene foto
        if (user.getFotoPerfil() != null && imageUserDrawer != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(firebaseUtils.STORAGE_PATH_USUARIOS + user.getFotoPerfil());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(imageUserDrawer);
        }

    }

    public void getMiUsuario(FirebaseUser firebaseUser) {
        // Obteniendo referencia al usuario
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(firebaseUtils.NODO_USUARIOS);
        final String email = firebaseUser.getEmail();

        Query q = databaseRef.orderByChild(firebaseUtils.CAMPO_EMAIL).equalTo(email);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    user = userSnapshot.getValue(Usuario.class);
                    // Cargara la informacion del usuario en el drawer
                    //System.out.println(user.toString());
                    cargarInfoUsuarioDrawer();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // rellenar lista de categorias
    public List<Categoria> obtenerCategorias(){
        int[] idsFotos = {0, R.drawable.cat_motor2, R.drawable.cat_hogar2, R.drawable.cat_electronica2,
                R.drawable.cat_juegos2, R.drawable.cat_hobbies2, R.drawable.cat_ropa2, R.drawable.cat_infantil2,
                R.drawable.cat_animales2, R.drawable.cat_electrodomesticos2, R.drawable.cat_libros2};
        int[] idsColores = {0, R.color.catRojo, R.color.catAzul, R.color.catAmarillo,
                R.color.catMorado, R.color.catEsmeralda, R.color.catRosa, R.color.catAzul,
                R.color.catGris, R.color.catMorado, R.color.catAzul};
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        String[] arrayCat = getResources().getStringArray(R.array.array_categorias);

        // Se descarta la primera opcion (Selecciona)
        for (int i = 1; i < arrayCat.length; i++)   {
            listaCategorias.add(new Categoria(arrayCat[i],idsFotos[i],idsColores[i]));
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
    /*
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
/*
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_inicio:

                break;
            case R.id.nav_notificaciones:

                break;
            case R.id.nav_chat:
                startActivity(new Intent(ActivityMain.this, ActivityListaChats.class));
                break;
            case R.id.nav_cuenta:
                Intent activityCuenta = new Intent(ActivityMain.this, ActivityCuenta.class);
                activityCuenta.putExtra("usuario", user);
                startActivity(activityCuenta);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
