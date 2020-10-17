package com.example.shiyan2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list= (ListView)findViewById(R.id.listView1);
        registerForContextMenu(list);
        data = new WordsDBHelper(this);
        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);
    }

    protected void onDestry(){
        super.onDestroy();
        data.close();
    }
//增加适配器

    private void setWordsListView(ArrayList<Map<String,String>>items){
        SimpleAdapter adapter= new SimpleAdapter(this,items,R.layout.item,
                new String[]{Words.Word._ID,Words.Word.COLUMN_NAME_WORD,Words.Word.COLUMN_NAME_WORD,Words.Word.COLUMN_NAME_MEANING,
                        Words.Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.txtWord,R.id.txtMeaning,R.id.txtSample});
        ListView list =(ListView)findViewById(R.id.listView1);
        list.setAdapter(adapter);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find, menu);
        return true;
    }
    public boolean OptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.action_search:

                return true;
            case R.id.action_insert:
                InsertDialog();
               return true;
        }
    return super.onOptionsItemSelected(item);

    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getMenuInflater().inflate(R.menu.sxiu, menu);

    }
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.action_delete:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                }
                DeleteDialog(strId);
                break;
            case R.id.action_update:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                textWord =(TextView)itemView.findViewById(R.id.textViewWord);
                textMeaning =(TextView)itemView.findViewById(R.id.textViewMeaning);
                textSample =(TextView)itemView.findViewById(R.id.textViewSample);
                if(textId!=null && textWord!=null && textMeaning!=null && textSample!=null){
                    String strId=textId.getText().toString();
                    String strWord=textWord.getText().toString();
                    String strMeaning=textMeaning.getText().toString();
                    String strSample=textSample.getText().toString();
                    UpdateDialog(strId, strWord, strMeaning, strSample);
                }
                break;


        }
        return true;

    }

    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        InsertSql(strWord, strMeaning, strSample);

                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample){
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        Update(strId, strNewWord, strNewMeaning, strNewSample);

                        //既可以使用Sql语句更新，也可以使用使用update方法更新
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }


    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Delete(strId);


            }
        })
                .create()
                .show();
    }

    private void  InsertSql(String strWord, String strMeaning, String strSample){
        String sql="insert into  words(word,meaning,sample) values(?,?,?)";
        SQLiteDatabase db = data.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }
    private void Update(String strId,String strWord, String strMeaning, String strSample){
        SQLiteDatabase db = data.getReadableDatabase();
            // New value for one column
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};
        int count = db.update(
                Words.Word.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private void Delete(String strId) {
        SQLiteDatabase db = data.getReadableDatabase();    // 定义where子句
        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);

    }



}


