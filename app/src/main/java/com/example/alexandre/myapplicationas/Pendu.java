package com.example.alexandre.myapplicationas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class Pendu extends AppCompatActivity {

    //Création de sharedPreferences pour sauvegarde les victoires et les défaites qui sont des données persistante
    protected static final String DATA_SAVE = "DATA_SAVE";
    SharedPreferences.Editor editor;

    int etapePendu=0;
    int tabNomEtapePendu[] ={
            R.drawable.pendu1, R.drawable.pendu2, R.drawable.pendu3,
            R.drawable.pendu4, R.drawable.pendu5, R.drawable.pendu6,
            R.drawable.pendu7, R.drawable.pendu8, R.drawable.pendu9,
    };
    String toucheClavier[]={
            "a","z","e","r","t","y","u","i","o","p",
            "q","s","d","f","g","h","j","k","l","m",
            "w","x","c","v","b","n"
    };
    String tabMots[]={
            "mathematique","professeur","anglais","francais",
            "arithmetique","algorithme","geometrie","regle",
            "crayon","trousse","telephone","appartement",
            "maison","helicoptere","avion","bateau",
            "voiture","ordinateur","ecran","souris",
            "clavier","chemise","pantalon","tomate",
            "carotte","salade","chocolat","baguette",
            "armoire","meuble","chaussette","chaussure",
            "camion","dictionnaire","carte","physique",
            "film","television","lecteur","smartphone",
            "casque","fenetre","armure","lapin",
            "universite","ecole","ingenieur","license",
            "diplome","stage","android","windows"
    };

    Random rand = new Random();
    int nombreAleatoire = rand.nextInt(tabMots.length);
    String motCache;
    String stock="";
    int progression=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendu);

        //Parametre pour le style des boutons
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(130, AbsListView.LayoutParams.WRAP_CONTENT);

        //Parametre pour mettre un margin entre chaque lettre ou trous
        //Il suffiera de faire par exemple   >>textView.setLayoutParams(lp);<<   pour l'appliquer
        int left=30;int right=30;int top = 0;int bottom =0;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        //Capture de la zone du clavier
        final GridLayout MonClavier = (GridLayout) findViewById(R.id.clavier);

        //Capture de la zone des trous et de lettres à trouvé
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        Intent intent = getIntent();
        String motDonnee = intent.getStringExtra(MainActivity.EXTRA_MOT);

        /*------------------------------------------------------*/
        /*----------------Gestion du mode de jeu----------------*/
            /*
             * Si aucun mot n'a pu être récupéré
             * alors c'est une partie normale avec des mots aléatoire
             * à deviner
             */
            if (motDonnee==null){
                motCache= tabMots[nombreAleatoire];
            }
            /*
             * Sinon, si un mot à été récupéré
             * alors c'est un mode où l'on doit deviner le mot donnée précédemment
             */
            else
                motCache = motDonnee;
        /*------------------------------------------------------*/
        /*------------------------------------------------------*/

        //Barre de progression, sa taille est la même que celle du mot à trouver
        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setMax(motCache.length());


        //----------------Initialisation des trous----------------
        for(int i = 0; i < motCache.length(); i++)
        {
            TextView textView = new TextView(Pendu.this);
            textView.setText("_");
            stock+=("_");
            textView.setLayoutParams(lp);
            textView.setId(i);
            textView.setTextSize(24);
            linearLayout.addView(textView);
        }
        //--------------------------------------------------------

        //------------------Creation du clavier------------------
        for(int i = 0; i < toucheClavier.length; i++)
        {
            final Button touche = new Button(Pendu.this);
            touche.setText(toucheClavier[i]);
            touche.setLayoutParams(params);
            touche.setId(i);
            touche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = new TextView(Pendu.this);
                    textView.setTextSize(24);
                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
                    linearLayout.removeAllViews();
                    gestionLettres(((touche.getText()).toString()), motCache);
                    linearLayout.addView(textView);
                    touche.setEnabled(false);
                }
            });
            MonClavier.addView(touche);
        }
        //-------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * @param lettre [string] - Lettre choisie par le joueur
     * @param cible [string] - Mot à trouvé
     */
    public void gestionLettres(String lettre,String cible){
        //Structure de donnees persistante
        SharedPreferences shared = getSharedPreferences(DATA_SAVE,MODE_PRIVATE);
        editor=shared.edit();

        //Capture de la zone des trous et de lettres à trouvé
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        //On vide la zone
        linearLayout.removeAllViews();

        //Parametre pour mettre un margin entre chaque lettre ou trous
        int left=30;int right=30;int top = 0;int bottom =0;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        //Capture de la barre de progression
        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);

        boolean gagner = true; //Va permettre de savoir si on a gagner la partie ou pas
        boolean trouve = false;//Va permettre de savoir si on a trouvé la lettre dans le mot à trouvé

        for (int i=0;i<cible.length();i++)
        {
            //Ici la lettre choisie a été trouvé dans le mot à trouvé
            if((cible.charAt(i))==lettre.charAt(0))
            {
                trouve=true;
                //Incrémentation du la barre de progression
                progression+=1;
                mProgress.setProgress(progression);
                //On remplace le trou '_' par la lettre choisie
                StringBuffer buffer = new StringBuffer(stock);
                buffer.setCharAt(i, lettre.charAt(0));
                stock = buffer.toString();
            }
        }
        if(trouve==false)
        {
            ImageView imagePendu = (ImageView) findViewById(R.id.imageDuPendu);
            if(etapePendu<tabNomEtapePendu.length-1)
            {
                //Passage à l'étape supérieur du montage du pendu
                etapePendu+=1;
                imagePendu.setImageResource(tabNomEtapePendu[etapePendu]);
            }
            else
            {
                //Affichage de l'image indiquant un échec
                imagePendu.setImageResource(R.drawable.penduperdu);

                //Mettre une seule colonne pour obliger l'ajout d'élément en colonne
                GridLayout clavier = (GridLayout)findViewById(R.id.clavier);
                clavier.removeAllViews();
                clavier.setColumnCount(1);
                clavier.setRowCount(4);

                //Ajout du message annonçant l'échec
                TextView msg = new TextView(Pendu.this);
                msg.setTextSize(18);
                msg.setText("Dommage ! Vous avez perdu ! Le mot à trouver était : ");
                clavier.addView(msg);

                /*
                * Ajout du mot qui devait être trouvé
                * ce mot a été ajouté indépendamment pour pemettre une
                 * mise en valeur de celui-ci
                **/
                TextView revelation  = new TextView(Pendu.this);
                revelation.setTextSize(28);
                int a_int=255; int r_int =255; int g_int=255; int b_int=255;
                revelation.setTextColor(Color.argb(a_int, r_int, g_int, b_int));
                revelation.setText(motCache);
                clavier.addView(revelation);

                //Ajout d'un bouton pour revenir au menu principal
                Button bouton = new Button(Pendu.this);
                bouton.setText("Retour au menu");
                bouton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Context context = getApplicationContext();
                        CharSequence text = "Retour au menu principal";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 950);
                        toast.show();
                        Intent intent = new Intent(Pendu.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                clavier.addView(bouton);

                //Création d'un id unique et d'une date
                String uniqueID = UUID.randomUUID().toString();
                Date maDate = new Date();

                //Ecriture dans la structure de données persistante
                editor.putString(uniqueID, "Defaite - " + maDate.toString());
                editor.commit();
            }
        }

        for (int k=0;k<stock.length();k++)
        {
            TextView textView = new TextView(Pendu.this);
            textView.setText(Character.toString(stock.charAt(k)));
            textView.setLayoutParams(lp);
            textView.setId(k);
            textView.setTextSize(24);
            linearLayout.addView(textView);

            if(stock.charAt(k)==("_").charAt(0))
            {
                gagner=false;
            }
        }
        if(gagner==true)
        {
            alert("Gagner !");

            GridLayout clavier = (GridLayout)findViewById(R.id.clavier);
            clavier.removeAllViews();
            clavier.setColumnCount(1);
            clavier.setRowCount(4);

            //Ajout du message annonçant la victoire
            TextView msg = new TextView(Pendu.this);
            msg.setTextSize(18);
            msg.setText("Bien joué ! Vous avez gagner !");
            clavier.addView(msg);

            //Ajout d'un bouton pour revenir au menu principal
            Button bouton = new Button(Pendu.this);
            bouton.setText("Retour au menu");
            bouton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Context context = getApplicationContext();
                    CharSequence text = "Retour au menu principal";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 950);
                    toast.show();
                    Intent intent = new Intent(Pendu.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            clavier.addView(bouton);

            //Création d'un id unique et d'une date
            String uniqueID = UUID.randomUUID().toString();
            Date maDate = new Date();

            //Ecriture dans la structure de données persistante
            editor.putString(uniqueID, "Victoire - " + maDate.toString());
            editor.commit();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
