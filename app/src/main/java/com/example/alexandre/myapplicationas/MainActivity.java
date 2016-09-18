package com.example.alexandre.myapplicationas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MOT = "mot";
    String alphabet[]={
            "a","z","e","r","t","y","u","i","o",
            "p","q","s","d","f","g","h","j","k",
            "l","m","w","x","c","v","b","n"
    };
    boolean trouve=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bouton qui permet de commencer une partie de pendu
        Button  buttonSuivant = (Button)findViewById(R.id.commencer);
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Pendu.class);
                startActivity(intent);
            }
        });

        /*Bouton qui permet de créer une barre de saisie et de rentrer un mot pour
        * le faire deviner à quelqu'un d'autre
        * */
        Button  boutonDeux = (Button)findViewById(R.id.deviner);
        boutonDeux.setOnClickListener(new View.OnClickListener() {
            boolean present =false;
            public void onClick(View v) {
                if(present==false) {
                    final EditText saisie = new EditText(MainActivity.this);
                    final LinearLayout LaZoneDeSaisie = (LinearLayout) findViewById(R.id.zoneDeSaisie);
                    int a_int = 120;
                    int r_int = 255;
                    int g_int = 255;
                    int b_int = 255;
                    LaZoneDeSaisie.setBackgroundColor(Color.argb(a_int, r_int, g_int, b_int));
                    //android:hint="someText"
                    saisie.setHint("Saisissez un mot");
                    LaZoneDeSaisie.addView(saisie);

                    Button validation = new Button(MainActivity.this);
                    validation.setText("Valider");
                    validation.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, Pendu.class);

                            //--------------------Controle de la valeur du mot saisie--------------------
                                for(int i=0;i<saisie.length();i++) {
                                    trouve=false;
                                    for(int j=0;j<alphabet.length;j++) {
                                        if (saisie.getText().toString().charAt(i) == (alphabet[j]).charAt(0))
                                        {
                                            trouve=true;
                                            break;
                                        }
                                    }
                                    if(trouve==false)
                                        break;
                                }
                                if (trouve==true && (saisie.getText().length()<=12)){
                                    intent.putExtra(EXTRA_MOT, saisie.getText().toString());
                                    startActivity(intent);
                                }
                                else
                                    alert("Le mot choisi ne doit comporter que des lettres de l'alphabet en minuscule et ne peut avoir que jusqu'à 12 lettres");
                            //---------------------------------------------------------------------------
                        }
                    });
                    LaZoneDeSaisie.addView(validation);
                    present=true;
                }
            }
        });
        //Bouton qui permet de lancer l'activité historique
        Button  buttonHistorique = (Button)findViewById(R.id.historique);
        buttonHistorique.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, historique.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
