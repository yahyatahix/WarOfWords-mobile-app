package com.tahix.pendu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout container ;
    private Button btnSend ;
    private TextView lettreTapez ;
    private ImageView image;
    private EditText letter ;
    private String word ;
    private int found ;
    private int error ;
    private List<Character> listOfLetters = new ArrayList<>();
    private boolean win ;
    private List<String> wordlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.wordContainer);
        btnSend = findViewById(R.id.btnSend);
        letter = findViewById(R.id.eTextLetter) ;
        image = findViewById(R.id.ImViewPendu) ;
        lettreTapez = findViewById(R.id.TxtViewLettreTapez) ;
        
        initGame();
        btnSend.setOnClickListener(this);


    }

    public void initGame(){
        word = generateWord();
        win = false;
        error = 0;
        found = 0;
        lettreTapez.setText("");
        image.setBackgroundResource(R.drawable.first);
        listOfLetters = new ArrayList<>();
        container.removeAllViews();
        
        for (int i = 0; i < word.length(); i++){

            TextView oneLetter = (TextView) getLayoutInflater().inflate(R.layout.textview,null);

            container.addView(oneLetter);

        }
    }

    @Override
    public void onClick(View v){
        String letterFromInput = letter.getText().toString().toUpperCase();
        letter.setText("");

        if (letterFromInput.length() > 0){
            if (!letterAlreadyUsed(letterFromInput.charAt(0), listOfLetters)){
                listOfLetters.add(letterFromInput.charAt(0));
                checkIfLetterIsInWord(letterFromInput, word);
            }
            //la partie est gagné
            if (found == word.length()){
                win = true;
                createDialog(win);

            }
            //la lettrre n'est pas dans le mot
            if (!word.contains(letterFromInput)){
                error++;
            }
            setImage(error);
            if (error == 6){
                win = false;
                createDialog(win);

            }
            // Affichage des lettres entrées
            showAllLetters(listOfLetters);
        }

    }
    public boolean letterAlreadyUsed(char a, List<Character> listOfletters) {

        for (int i = 0; i < listOfletters.size(); i++) {

            if (listOfletters.get(i) == a) {
                Toast.makeText(getApplicationContext(), "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show();
                return true;

            }
        }

        return false;
    }

    public void checkIfLetterIsInWord(String letter, String word){
        for (int i = 0; i < word.length(); i++){
            if (letter.equals(String.valueOf(word.charAt(i)))){
                TextView tv = (TextView) container.getChildAt(i);
                tv.setText((String.valueOf(word.charAt(i))));
                found ++ ;

            }
        }

    }
    public  void showAllLetters(List<Character> listOfLetters ){
        String chaine = "";
        for (int i = 0; i < listOfLetters.size(); i++){
            chaine +=listOfLetters.get(i) + "\n";
        }
        if (!chaine.equals("")){
            lettreTapez.setText(chaine);
        }
    }

    public void setImage(int error){

        switch (error){
            case 1:
                image.setBackgroundResource(R.drawable.second);
                break;

            case 2:
                image.setBackgroundResource(R.drawable.third);
                break;

            case 3:
                image.setBackgroundResource(R.drawable.fourth);
                break;

            case 4:
                image.setBackgroundResource(R.drawable.fifth);
                break;

            case 5:
                image.setBackgroundResource(R.drawable.sixth);
                break;

            case 6:
                image.setBackgroundResource(R.drawable.seventh);
                break;
        }
    }

    public void  createDialog(boolean win){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vous avez gagné !");

        if (!win){
            builder.setTitle("Vous avez perdu !");
            builder.setMessage("Le mot à truver était :" + word);
        }
        builder.setPositiveButton(getResources().getString(R.string.rejouer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();

            }
        });

        builder.create().show();

    }

    public List<String> getWordlist(){

        try{
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getAssets().open("pendu_liste.txt")));
            String line;
            while((line = buffer.readLine()) != null){
                wordlist.add(line);
            }
            buffer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return wordlist;

    }
    public String generateWord(){
        wordlist = getWordlist();
        int random = (int) (Math.floor(Math.random() * wordlist.size()));
        String word = wordlist.get(random).trim();

        return word;

    }
}

