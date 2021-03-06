package com.example.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    TextView tvWordResult;
    TextView tvMeanResult;
    TextView tvSentenceResult;
    TextView tvSentenceCHS;
//    TextView tvPhoneticsymbolUS;
//    TextView tvPhoneticsymbolUK;
    Button btnAdd;

    Database database;
    Dictionary dictionary;
    Dictionary new_dictionary;
    WordValue wordValue=null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        Intent intent=new Intent();
        switch (id){
            case R.id.mi_dictionary:
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mi_wordnote:
                intent.setClass(this,WordnoteActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch=findViewById(R.id.et_search);
        btnSearch=findViewById(R.id.btn_search);
        tvWordResult=findViewById(R.id.tv_wordresult);
        tvMeanResult=findViewById(R.id.tv_meanresult);
        tvSentenceResult=findViewById(R.id.tv_sentenceresult);
        tvSentenceCHS=findViewById(R.id.et_sentenceCHS);
//        tvPhoneticsymbolUS=findViewById(R.id.tv_phoneticsymbolUS);
//        tvPhoneticsymbolUK=findViewById(R.id.tv_phoneticsymbolUK);
        btnAdd=findViewById(R.id.btn_add);

        dictionary=new Dictionary(this,"dict");
        new_dictionary=new Dictionary(this,"newdict");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if ("".equals(etSearch.getText().toString())) {
                            //??????????????????
                            showToastInThread("???????????????");
                        } else {

//                            wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());
                            wordValue = dictionary.getWordFromDict(etSearch.getText().toString());


                            if (wordValue != null && wordValue.getInterpret() != null && !"".equals(wordValue.getPsA())) {
                                showResearchWordInterpret(wordValue);

                                Log.d("MainActivity", "if??????");
                                Log.d("MainActivity", "??????"+wordValue.getInterpret());
                            } else {

                                showToastInThread("?????????????????????");
                                Log.d("MainActivity", "else??????");
                            }
                        }
                    }
                }).start();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if ("".equals(etSearch.getText().toString())) {
                            //??????????????????
                            showToastInThread("???????????????");
                            //  Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //????????????
                            //?????????????????????
                            wordValue = dictionary.getWordFromDict(etSearch.getText().toString());
                            if (wordValue == null) {
                                //?????????,?????????????????????????????????
                                //???????????????
                                wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());

                                if (wordValue == null) {
                                    //????????????
                                    showToastInThread("?????????????????????????????????");
                                    // Toast.makeText(MainActivity.this, "?????????????????????????????????", Toast.LENGTH_LONG).show();
                                } else {
                                    //????????????
                                    dictionary.insertWordToDict(wordValue, true);
                                    showToastInThread("????????????????????????");
                                    // Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //???????????????????????????
                                //showToastInThread(word.getInterpret());
                                showToastInThread("???????????????????????????,?????????????????????");
                                //Toast.makeText(MainActivity.this,"???????????????????????????,?????????????????????",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();
            }
        });
    }





    /**
     * ????????????ui??????????????????????????????????????????
     *
     * @param wordValue
     */
    private void showResearchWordInterpret(final WordValue wordValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //????????????ui??????
                tvWordResult.setText(wordValue.getWord());
                tvMeanResult.setText(wordValue.getInterpret());
                if (isLand()) {
                    tvSentenceResult.setText(wordValue.getSentOrig());
                    tvSentenceCHS.setText(wordValue.getSentTrans());
                }
//                else {
//                    tvPhoneticsymbolUS.setText("???["+wordValue.psA+"]");
//                    tvPhoneticsymbolUK.setText("???["+wordValue.psE+"]");
//                }
            }
        });
    }

    /**
     * ??????????????????toast
     *
     * @param string ???????????????????????????
     */
    private void showToastInThread(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }

    //???????????????????????????
    private boolean isLand() {
        Configuration mConfiguration = this.getResources().getConfiguration(); //???????????????????????????
        int ori = mConfiguration.orientation; //??????????????????
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //??????
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //??????
            return false;
        }
        return false;
    }
}
