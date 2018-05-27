package com.example.adrian.telovendo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.adrian.telovendo.R;

import java.util.HashMap;

public class ActivityBuscar extends AppCompatActivity {

    private HashMap<String, String> searchMap;

    private EditText editTextBuscar;
    private EditText editPrecioDesde;
    private EditText editPrecioHasta;
    private Spinner spinnerSearchCategoria;
    private Spinner spinnerSearchProvincia;
    private EditText editMarcaSearch;
    private EditText editModeloSearch;
    private Button botonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se asigna el titulo
        getSupportActionBar().setTitle(R.string.title_activity_buscar);

        // Referenciamos las views
        editTextBuscar = findViewById(R.id.editTextBuscar);
        editPrecioDesde = findViewById(R.id.editPrecioDesde);
        editPrecioHasta = findViewById(R.id.editPrecioHasta);
        spinnerSearchCategoria = findViewById(R.id.spinnerSearchCategoria);
        spinnerSearchProvincia = findViewById(R.id.spinnerSearchProvincia);
        editMarcaSearch = findViewById(R.id.editMarcaSearch);
        editModeloSearch = findViewById(R.id.editModeloSearch);
        botonSearch = findViewById(R.id.botonSearch);

        botonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rellenamos el mapa con la informacion
                searchMap = getSearchMap();

                Intent activityListarCategoria = new Intent(ActivityBuscar.this, ActivityListarCategoria.class);
                activityListarCategoria.putExtra("searchMap", searchMap);
                startActivity(activityListarCategoria);
            }
        });

    }

    private HashMap<String,String> getSearchMap() {

        String textoBusqueda = editTextBuscar.getText().toString().trim();
        String precioDesde = editPrecioDesde.getText().toString().trim();
        String precioHasta = editPrecioHasta.getText().toString().trim();
        String categoria = spinnerSearchCategoria.getSelectedItem().toString();
        String provincia = spinnerSearchProvincia.getSelectedItem().toString();
        String marca = editMarcaSearch.getText().toString().trim();
        String modelo = editModeloSearch.getText().toString().trim();

        HashMap<String, String> searchMap = new HashMap<>();
        if (!categoria.equalsIgnoreCase("Todas"))
            searchMap.put("categoria", categoria);
        if (!provincia.equalsIgnoreCase("Cualquiera"))
            searchMap.put("provincia", provincia);
        if (!textoBusqueda.isEmpty())
            searchMap.put("textoBusqueda", textoBusqueda);
        if (!precioDesde.isEmpty())
            searchMap.put("precioDesde", precioDesde);
        if (!precioHasta.isEmpty())
            searchMap.put("precioHasta", precioHasta);
        if (!marca.isEmpty())
            searchMap.put("marca", marca);
        if (!modelo.isEmpty())
            searchMap.put("modelo", modelo);

        return searchMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
