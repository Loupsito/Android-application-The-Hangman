package com.example.alexandre.myapplicationas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class historique extends AppCompatActivity {

    String[] data;
    ArrayList<String> laListe = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        SharedPreferences shared = getSharedPreferences(Pendu.DATA_SAVE,MODE_PRIVATE);
        Map<String, ?> allEntries = shared.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet())
        {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            laListe.add(entry.getValue().toString());
        }

        //Les donnees
        ArrayAdapter<String> adapter = new ArrayAdapter (this,android.R.layout.simple_list_item_1,laListe);
        ListView maListe = (ListView) findViewById(R.id.listeHistorique);
        maListe.setAdapter(adapter);

        //Bouton pour revenir au menu principale
        Button retourMenu = (Button)findViewById(R.id.retourMenu);
        retourMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(historique.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Bouton pour supprimer l'historique
        Button supprHistorique = (Button)findViewById(R.id.supprHistorique);
        supprHistorique.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences(Pendu.DATA_SAVE,MODE_PRIVATE);
                shared.edit().clear().commit();
                alert("Sortez et revenez dans l'historique pour mettre l'affichage de l'historique à jour");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * @param message [string] - texte à afficher dans le toast
     * Fonction qui va permettre d'afficher un toast
     * De la meme maniere qu'un alert() en JavaScipt
     */
    public void alert(String message)
    {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 950);
        toast.show();
    }

}
