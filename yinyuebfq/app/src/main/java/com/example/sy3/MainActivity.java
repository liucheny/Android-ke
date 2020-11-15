package com.example.sy3;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {


    private static final int INTERNAL_TIME = 1000;
    final MediaPlayer mp = new MediaPlayer();//实例化播放器
    String song_path = "";
    private SeekBar seekBar;//进度条
    private TextView songName;//当前播放曲目
    boolean isStop = true;
    private int currentposition;//当前音乐播放的进度
    private ArrayList<String> listName;
    private ArrayList<String> listPath;//音乐路径，用于初次启动后扫描设备所得
    private ArrayList<String> listNameRefresh;//音乐名称，用于后续刷新后扫描
    private ArrayList<String> listPathRefresh;//音乐路径，用于后续刷新后扫描
    private Button switcher;
    private Button delete;
    private Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songName = findViewById(R.id.songName);
        seekBar = (SeekBar) findViewById(R.id.music_seekbar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        listPath = (ArrayList<String>) MusicUtils.getMusicData(MainActivity.this);   //音乐路径
        listName = (ArrayList<String>) MusicUtils.getMusicName(MainActivity.this);   //音乐名字
        listPathRefresh = (ArrayList<String>) MusicUtils.getMusicData(MainActivity.this);   //音乐路径
        listNameRefresh = (ArrayList<String>) MusicUtils.getMusicName(MainActivity.this);   //音乐名字
        switcher = findViewById(R.id.switcher);
        switcher.setText("顺序");
        if (switcher.getText().toString() == "顺序") {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentposition = currentposition + 1;
                    changeMusic(currentposition);
                }
            });         //顺序播放
        }
        else if (switcher.getText().toString()=="循环"){
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentposition = (int)(0 + Math.random() * (listPath.size() - 1 - 0 + 1));
                    changeMusic(currentposition);
                }
            });         //循环播放

        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (switcher.getText().toString() == "顺序") {
                    switcher.setText("随机");
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            currentposition = (int)(0 + Math.random() * (listPath.size() - 1 - 0 + 1));
                            changeMusic(currentposition);
                        }
                    });
                }
               else if (switcher.getText().toString() == "随机") {
                    switcher.setText("顺序");
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            currentposition = currentposition + 1;
                            changeMusic(currentposition);
                        }
                    });
                }
            }
        });
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_selectable_list_item, listName);
        final ArrayAdapter<String> adapterRefresh = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_selectable_list_item, listNameRefresh);

        delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listName.remove(currentposition);
                listPath.remove(currentposition);
                adapter.notifyDataSetChanged();
                if (listName.size() != 0)
                    changeMusic(currentposition);
                else {
                    mp.stop();
                    songName.setText("当前无音乐播放！");
                    Toast.makeText(MainActivity.this, "请添加歌曲！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ListView li = (ListView) findViewById(R.id.listView1);
        li.setAdapter(adapter);
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.setAdapter(adapterRefresh);
            }
        });


        final Button btnpause = (Button) findViewById(R.id.btn_pause);

        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                song_path = ((TextView) view).getText().toString();
                currentposition = position;
                changeMusic(currentposition);
                try {
                    mp.reset();    //重置
                    mp.setDataSource(song_path);        //获取音乐路径
                    mp.prepare();     //准备
                    mp.start(); //播放

                    seekBar.setMax(mp.getDuration());
                    isStop = false;


                } catch (Exception e) {
                }
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song_path.isEmpty())
                    Toast.makeText(getApplicationContext(), "先选收歌曲先听听", Toast.LENGTH_SHORT).show();
                if (mp.isPlaying()) {
                    mp.pause();  //暂停
                    isStop = true;
                    btnpause.setText("播放");

                } else if (!song_path.isEmpty()) {
                    mp.start();   //继续播放
                    btnpause.setText("暂停");
                    isStop = false;

                }
            }
        });

        final Button previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentposition = currentposition - 1;
                changeMusic(currentposition);
            }
        });

        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.getText().toString() == "顺序") {

                    currentposition = currentposition + 1;
                    changeMusic(currentposition);

                }
                else if (switcher.getText().toString() == "随机") {
                    currentposition = (int)(0 + Math.random() * (listPath.size() - 1 - 0 + 1));
                    changeMusic(currentposition);
                }
            }
        });
    }

    private void changeMusic(int position) {
        if (position < 0) {         //list头部，修改位置至尾部
            currentposition = listPath.size() - 1;
        } else if (position == listPath.size()) {       //；list尾部，修改位置至头部
            currentposition = 0;
        }

        if(listName.size() == listNameRefresh.size()) {         //判断当前所使用的list对象
            song_path = listPath.get(currentposition);
            songName.setText(listName.get(currentposition));

            try {
                mp.reset();

                mp.setDataSource(song_path);

                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            seekBar.setProgress(0);//将进度条初始化
            seekBar.setMax(mp.getDuration());//设置进度条最大值为歌曲总时间

            updateProgress();//更新进度条
        }
        else {
            song_path = listPathRefresh.get(currentposition);
            songName.setText(listNameRefresh.get(currentposition));

            try {
                mp.reset();
                // 设置播放源
                mp.setDataSource(song_path);
                // 开始播放前的准备工作，加载多媒体资源，获取相关信息
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            seekBar.setProgress(0);//将进度条初始化
            seekBar.setMax(mp.getDuration());//设置进度条最大值为歌曲总时间

            updateProgress();//更新进度条
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void  handleMessage(Message message) {// 展示给进度条
            int progress = mp.getCurrentPosition();
            seekBar.setProgress(progress);
            updateProgress();
        }
    };

    private void updateProgress() {
        // 使用Handler每间隔1s发送一次空消息，通知进度条更新
        Message msg = Message.obtain();// 获取一个现成的消息
        // 使用MediaPlayer获取当前播放时间除以总时间的进度
        int progress = mp.getCurrentPosition();
        msg.arg1 = progress;
        mHandler.sendMessageDelayed(msg, INTERNAL_TIME);
    }


    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
            if(fromUser){
                mp.seekTo(progress);
            }
        }
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        Toast.makeText(getApplicationContext(), "已退出应用！", Toast.LENGTH_SHORT).show();
    }

}

