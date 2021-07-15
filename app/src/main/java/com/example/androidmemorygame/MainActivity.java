package com.example.androidmemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidmemorygame.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    String webURL;
    String htmlContent;
    List<String> imgURLs;
    List<File> destFiles;
    int[] ids = new int[]{R.id.imageview1, R.id.imageview2, R.id.imageview3, R.id.imageview4, R.id.imageview5, R.id.imageview6, R.id.imageview7, R.id.imageview8,
            R.id.imageview9, R.id.imageview10, R.id.imageview11, R.id.imageview12, R.id.imageview13, R.id.imageview14, R.id.imageview15, R.id.imageview16, R.id.imageview17,R.id.imageview18,R.id.imageview19,R.id.imageview20};
    int count=0;
    ArrayList<Integer> selected = new ArrayList<>();
    ProgressBar progressbar;
    TextView progressbartext;
    boolean firstTime = true;
    Thread bgThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webURL = ((EditText)findViewById(R.id.webURL)).getText().toString();
        Button fetch = findViewById(R.id.fetch);
        fetch.setOnClickListener(v->{

            if(bgThread != null){
                bgThread.interrupt();
                for(int i=0; i<20; i++){

                    ImageView imageview = findViewById(ids[i]);
                    imageview.setImageBitmap(null);
                }
            }

            if(firstTime == false){
                for(int i=0; i<20; i++){
                    count=0;
                    ImageView imageview = findViewById(ids[i]);
                    imageview.setColorFilter(null);
                    imageview.setImageBitmap(null);

                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("%s.jpg", i+1));
                    if(file.exists()){
                        file.delete();
                    }
                }
            }

            bgThread = new Thread(new Runnable(){
                @Override
                public void run(){

                    if(downloadWeb(((EditText)findViewById(R.id.webURL)).getText().toString())){
                        List<File> destfiles = createFilesDir();
                        downloadImg(imgURLs, destfiles);
                    }
                    firstTime = false;
                }
            });
            bgThread.start();
        });

        progressbar = findViewById(R.id.progressbar);
        progressbar.setMax(100);
        progressbartext = findViewById(R.id.progressbartext);

    }


    public boolean downloadWeb(String webURL){

        try{
            URL url = new URL(webURL);
            BufferedReader br = new BufferedReader
                    (new InputStreamReader(url.openStream()));

            imgURLs = new ArrayList<String>();
            htmlContent = "";
            String line = br.readLine();
            while (line != null){
                htmlContent += line;
                line = br.readLine();
            }
            br.close();

            if (Thread.interrupted()){
                return false;
            }


            //<img src="https://cdn.stocksnap.io/img-thumbs/280h/bed-family_JHFDNCSWTX.jpg"
            Pattern pattern = Pattern.compile("<img src=\"([^\"]+)\\.(jpg|png|jpeg)");
            Matcher matcher = pattern.matcher(htmlContent);
            for(int i=0; i<20; i++) {
                boolean matchFound = matcher.find();
                if (matchFound) {
                    String tag = matcher.group();
                    String imgURL = tag.substring(10);
                    imgURLs.add(imgURL);

                    if (Thread.interrupted()){
                        return false;
                    }

                }
            }

            return true;

        }
        catch(Exception e){
            return false;
        }

    }


    public boolean downloadImg(List<String> imgURLs, List<File> desFiles){

        try{
            for(int i=0; i<20; i++) {

                URL url = new URL(imgURLs.get(i));
                URLConnection conn = url.openConnection();

                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(desFiles.get(i));

                byte[] buf = new byte[1024];
                int byteRead = -1;
                while ((byteRead = in.read(buf)) != -1) {
                    out.write(buf, 0, byteRead);
                }
                out.close();
                in.close();

                if (Thread.interrupted()){
                    return false;
                }

                displayImage(desFiles, i);

            }
            return true;

        }
        catch(Exception e){
            return false;
        }

    }

    public List<File> createFilesDir(){
        destFiles = new ArrayList<File>();
        for(int i = 0; i<20; i++){
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("%s.jpg", i+1));
            destFiles.add(file);
        }
        return destFiles;

    }

    public void displayImage(List<File> desFiles, int i){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){

                int[] ids = new int[]{R.id.imageview1, R.id.imageview2, R.id.imageview3, R.id.imageview4, R.id.imageview5, R.id.imageview6, R.id.imageview7, R.id.imageview8,
                        R.id.imageview9, R.id.imageview10, R.id.imageview11, R.id.imageview12, R.id.imageview13, R.id.imageview14, R.id.imageview15, R.id.imageview16, R.id.imageview17,R.id.imageview18,R.id.imageview19,R.id.imageview20};

                Bitmap bitmap = BitmapFactory.decodeFile(desFiles.get(i).getAbsolutePath());
                ImageView imageview = findViewById(ids[i]);
                imageview.setImageBitmap(bitmap);

                imageview.setOnClickListener(v -> {
                    if(imageview.getColorFilter() == null){
                        imageview.setColorFilter(MainActivity.this.getResources().getColor(R.color.purple_200));
                        selected.add(imageview.getId());
                        count++;

                        if(count==6){
                            Intent intent = new Intent(MainActivity.this, NextActivity.class);
                            intent.putIntegerArrayListExtra("selected", selected);
                            startActivity(intent);
                        }

                    }

                    else{
                        imageview.setColorFilter(null);
                        selected.remove((Object)imageview.getId());
                        count--;
                    }
                });

                if (i >= 19) {
                    progressbar.setVisibility(View.GONE);
                    progressbartext.setText("");
                } else {
                    progressbar.setVisibility(View.VISIBLE);
                    progressbar.setProgress(100/20*(i+1));
                    progressbartext.setText(String.format("Downloading %s of 20 images", i+1));
                }
            }
        });
    }

}