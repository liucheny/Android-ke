package com.example.duihuakuang;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duihuakuang);
    }

    public void onButtonClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View viewDialog= LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null,false);
        //从布局文件生成

        builder.setTitle("登录对话框")
                .setView(viewDialog)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTextUserId = viewDialog.findViewById(R.id.userId);
                        EditText editTextPassword = viewDialog.findViewById(R.id.password);
                        if(editTextUserId.getText().toString().equals("abc")&&
                                editTextPassword.getText().toString().equals("123"))
                            Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
                    }
                });
        //显示对话框
        builder.create().show();
    }
}