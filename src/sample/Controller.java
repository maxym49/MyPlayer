package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javax.swing.Timer;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;




public class Controller {

    private static MediaPlayer mediaPlayer;
    private static FileChooser fileChooser;
    private List<File> selectedFiles = new LinkedList<>();
    private ListIterator iterator;
    private int k=0;
    private int counter = 0;
    private String song;
    private String mediaName;
    private boolean isPlaying = false;
    private Timer timer;
    private double tTime = 0;


    @FXML
    private AnchorPane menu;

    @FXML
    private JFXTextField whatSongfield;

    @FXML
    private ProgressBar progressBar;


    @FXML
    void exitProgram(){
            System.exit(0);
    }

    @FXML
    void showMenu(){
        if(menu.isVisible()==false) {
            menu.setVisible(true);
        }
        else{
            menu.setVisible(false);
        }
    }
    @FXML
    void movingText(){
        song = whatSongfield.getText();
        int textLength = song.length();
        Timer timer = new Timer(100, e -> {
            counter ++;
            if(counter > textLength){
                whatSongfield.setText("");
                whatSongfield.setPromptText("");
                counter = 0;
            }else {
                whatSongfield.setText(song.substring(0,counter));
            }
        });
        timer.start();

    }

    @FXML
    void showMediaTime() {
        tTime = 0;
        timer = null;
        timer = new Timer(1000, e ->{
                progressBar.setProgress(tTime);
                tTime = tTime + 0.1;
            if(tTime > 1){
                try {
                    timer.stop();
                }catch (RuntimeException r){
                    System.out.println(r);
                }
            }
            });
            timer.start();
    }

    @FXML
    void play() {


        try {
            Object element = iterator.next();
            String path = element.toString().replace("\\", "/");
            System.out.println(path);
            path = URLEncoder.encode(path,"UTF-8").replace("+", "%20");
            System.out.println(path);
            System.out.println();
            Media media = new Media("file:///" + path);

            mediaPlayer = new MediaPlayer(media);
            mediaName = selectedFiles.get(k).getName();
            whatSongfield.setText(mediaName);
            k++;

            mediaPlayer.setOnReady(() -> {
                movingText();
                isPlaying = true;
                mediaPlayer.setAutoPlay(true);
            });

            mediaPlayer.setOnPlaying(new Runnable() {
                @Override
                public void run() {
                    try{

                            showMediaTime();


                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                    isPlaying = false;
                    mediaPlayer.stop();
                    whatSongfield.setText("");
                if (iterator.hasNext()==true){
                    play();
                    System.out.println( mediaPlayer.getTotalDuration().toSeconds());
                }
                else
                    whatSongfield.setText("No more songs!");
                    mediaPlayer.stop();
                System.out.println( mediaPlayer.getTotalDuration().toSeconds());
            });

        } catch (Exception e) {
            System.out.println("You need to choose the music file");
            System.out.println("The error name:\n" + e);
        }
    }

    @FXML
    void nextSong(){
        try {
            if (iterator.hasNext()==true){
                mediaPlayer.stop();
                play();
            }
            else{
                whatSongfield.setText("No more songs!");
            }
        }catch (Exception e){
            System.out.println(e);
        }
}

    @FXML
    void prevSong(){
        try {
            if(iterator.hasPrevious()) {

                mediaPlayer.stop();
                k = k - 1;
                Object element = iterator.previous();
                String path = element.toString().replace("\\", "/");
                path = URLEncoder.encode(path, "UTF-8").replace("+", "%20");
                Media media = new Media("file:///" + path);

                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mediaName = selectedFiles.get(k).getName();
                whatSongfield.setText(mediaName);


                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    whatSongfield.setText("");
                    if (iterator.hasNext() == true) {
                        play();

                    } else
                        whatSongfield.setText("No more songs!");
                        mediaPlayer.stop();

                });
            }
        }catch (Exception e){
            System.out.println(e);
        }


    }
    @FXML
    void pauseSong(){
        try {
            if (mediaPlayer.isAutoPlay()==true){
                mediaPlayer.pause();
            }
        }catch (Exception e){
            System.out.println("The error name:\n"+e);
            whatSongfield.setText("No songs on the list!");
        }

    }

    @FXML
    void stopSong(){
        try {
            if (mediaPlayer.isAutoPlay()==true){
                mediaPlayer.stop();
            }
        }catch (Exception e){
            System.out.println("The error name:\n"+e);
            whatSongfield.setText("No songs on the list!");
        }

    }

    @FXML
    void resumeSong(){

    mediaPlayer.getCurrentTime();
    mediaPlayer.pause();
    //mediaPlayer.pla
        if (iterator.hasNext()==true){
            play();
        }
        else{
            System.out.println("No more songs!");
        }
    }

    @FXML
    void openDirectory(){
        try {

                fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
                fileChooser.getExtensionFilters().add(extensionFilter);
                selectedFiles = fileChooser.showOpenMultipleDialog(null);
                iterator = selectedFiles.listIterator();
                k=0;
                play();

        }catch (Exception e){
            System.out.println("No file");
            System.out.println(e);
        }




        if(selectedFiles == null){
            System.out.println("The file is not valid or you have not selected the file");
        }

    }



}



