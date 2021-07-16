package com.example.androidmemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    String modifiecWebURL;
    String htmlContent;
    List<String> imgURLs;
    List<File> destFiles;
    int[] ids = new int[]{R.id.imageview1, R.id.imageview2, R.id.imageview3, R.id.imageview4, R.id.imageview5, R.id.imageview6, R.id.imageview7, R.id.imageview8,
            R.id.imageview9, R.id.imageview10, R.id.imageview11, R.id.imageview12, R.id.imageview13, R.id.imageview14, R.id.imageview15, R.id.imageview16, R.id.imageview17, R.id.imageview18, R.id.imageview19, R.id.imageview20};
    int count = 0;
    ArrayList<String> selected = new ArrayList<>();
    TextView selectText;
    ProgressBar progressbar;
    TextView progressbartext;
    boolean firstTime = true;
    Thread bgThread;
    Button startGame;
    MediaPlayer music;
    MediaPlayer startsound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webURL = ((EditText) findViewById(R.id.webURL)).getText().toString();

        music = MediaPlayer.create(MainActivity.this, R.raw.maplestory);
        music.start();

        selectText=findViewById(R.id.selectText);

        startGame = findViewById(R.id.startGameBtn);
        startGame.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putStringArrayListExtra("selected", selected);
            startActivity(intent);
            music.stop();
        });

        Button fetch = findViewById(R.id.fetch);
        fetch.setOnClickListener(v -> {
            hideSoftKeyboard(MainActivity.this);

            if(webURL==null){
                Toast.makeText(MainActivity.this,"Please enter url",Toast.LENGTH_LONG).show();
            }


            if (bgThread != null) {
                bgThread.interrupt();
                for (int i = 0; i < 20; i++) {

                    ImageView imageview = findViewById(ids[i]);
                    imageview.setImageBitmap(null);
                }
            }

            if (firstTime == false) {
                for (int i = 0; i < 20; i++) {
                    count = 0;
                    ImageView imageview = findViewById(ids[i]);
                    imageview.setColorFilter(null);
                    imageview.setImageBitmap(null);

                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("%s.jpg", i + 1));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            bgThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    music.start();
                    if (downloadWeb(((EditText) findViewById(R.id.webURL)).getText().toString())) {
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

    public boolean downloadWeb(String webURL) {
        if (!webURL.isEmpty()) {

            //check the url's start
            if (webURL.startsWith("http:")) {
                modifiecWebURL = "https:" + webURL.substring(6);
            } else if (!webURL.startsWith("https://")) {
                modifiecWebURL = "https://" + webURL;
            } else {
                modifiecWebURL = webURL;
            }
        }

        try {
            URL url = new URL(modifiecWebURL);
            BufferedReader br = new BufferedReader
                    (new InputStreamReader(url.openStream()));

            imgURLs = new ArrayList<String>();
            htmlContent = "";
            String line = br.readLine();
            while (line != null) {
                htmlContent += line;
                line = br.readLine();
            }
            br.close();

            if (Thread.interrupted()) {
                return false;
            }


            //<img src="https://cdn.stocksnap.io/img-thumbs/280h/bed-family_JHFDNCSWTX.jpg"
            Pattern pattern = Pattern.compile("<img src=\"([^\"]+)\\.(jpg|png|jpeg)");
            Matcher matcher = pattern.matcher(htmlContent);
            for (int i = 0; i < 20; i++) {
                boolean matchFound = matcher.find();
                if (matchFound) {
                    String tag = matcher.group();
                    String imgURL = tag.substring(10);
                    imgURLs.add(imgURL);

                    if (Thread.interrupted()) {
                        return false;
                    }

                }
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }


    public boolean downloadImg(List<String> imgURLs, List<File> desFiles) {

        try {
            for (int i = 0; i < 20; i++) {

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

                if (Thread.interrupted()) {
                    return false;
                }

                displayImage(desFiles, i);

            }
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public List<File> createFilesDir() {
        destFiles = new ArrayList<File>();
        for (int i = 0; i < 20; i++) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("%s.jpg", i + 1));
            destFiles.add(file);
        }
        return destFiles;

    }

    public void displayImage(List<File> desFiles, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progressbartext.setVisibility(View.INVISIBLE);
                //startGame.setVisibility(View.INVISIBLE);
                //selected.clear();
                //count=0;

                int[] ids = new int[]{R.id.imageview1, R.id.imageview2, R.id.imageview3, R.id.imageview4, R.id.imageview5, R.id.imageview6, R.id.imageview7, R.id.imageview8,
                        R.id.imageview9, R.id.imageview10, R.id.imageview11, R.id.imageview12, R.id.imageview13, R.id.imageview14, R.id.imageview15, R.id.imageview16, R.id.imageview17, R.id.imageview18, R.id.imageview19, R.id.imageview20};

                Bitmap bitmap = BitmapFactory.decodeFile(desFiles.get(i).getAbsolutePath());
                ImageView imageview = findViewById(ids[i]);
                imageview.setImageBitmap(bitmap);

                imageview.setOnClickListener(v -> {
                    selectText.setVisibility(View.VISIBLE);

                    if(selected.size()<6){

                        if(imageview.getColorFilter()==null){
                            imageview.setColorFilter(MainActivity.this.getResources().getColor(R.color.grey));
                            for(int i=0; i<20; i++){
                                if(imageview.getId() == ids[i]){
                                    selected.add(desFiles.get(i).getAbsolutePath());
                                    break;
                                }
                            }
                            count++;
                        }

                        else{
                            imageview.setColorFilter(null);
                            selected.remove(desFiles.get(i).getAbsolutePath());
                            count--;
                        }
                    }

                    if(selected.size() == 6 && count==6){
                        startGame.setVisibility(View.VISIBLE);
                        count++;
                    }


                    else if (selected.size() == 6 && count > 6) {
                        if(imageview.getColorFilter()==null){
                            imageview.setClickable(false);
                            imageview.setClickable(true);
                        }
                        else{
                            //imageview.setClickable(true);
                            imageview.setColorFilter(null);
                            selected.remove(desFiles.get(i).getAbsolutePath());
                            startGame.setVisibility(View.INVISIBLE);
                            count=5;
                        }
                    }

                    selectText.setText("Select " + selected.size() + "/" + 6 + " images");

                });

                if (i >= 19) {
                    progressbar.setProgress(100);
                    progressbar.setVisibility(View.GONE);
                    progressbartext.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Please select images to start", Toast.LENGTH_SHORT).show();

                } else {
                    progressbar.setVisibility(View.VISIBLE);
                    progressbar.setProgress(100/20*(i+1));
                    progressbartext.setVisibility(View.VISIBLE);
                    progressbartext.setText(String.format("Downloading %s of 20 images", i + 1));
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}