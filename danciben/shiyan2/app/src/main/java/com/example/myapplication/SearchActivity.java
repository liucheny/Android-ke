package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    WordsDBHelper data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listsearch);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        List<Map<String, String>> items= (List<Map<String, String>>) bundle.getSerializable("result");
        setWordsListView(items);
    }
    private void setWordsListView(List<Map<String, String>> items){//适配器，在列表中显示单词
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item, new String[]{Words.Word._ID,Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.textId,R.id.textViewWord, R.id.textViewMeaning, R.id.textViewSample});

        ListView list = (ListView) findViewById(R.id.listView1);

        list.setAdapter(adapter);
    }

}
