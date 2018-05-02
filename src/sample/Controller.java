package sample;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Timer timer;
    private Timer secondTimer;
    private double tTime = 0;
    private double onePercent;


    @FXML
    private JFXSlider volumeSlider;

    @FXML
    private AnchorPane menu;

    @FXML
    private JFXTextField whatSongfield;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private JFXToggleButton loopToggle;

    @FXML
    private JFXSpinner spinner;


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
    void testM(){
        try {
            mediaPlayer.setOnPlaying(() -> movingText());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void movingText() {
        try {
                song = whatSongfield.getText();
                int textLength = song.length();
                secondTimer = new Timer(100, e -> {
                    counter ++;
                    if(counter > textLength){
                        whatSongfield.setText("");
                        whatSongfield.setPromptText("");
                        counter = 0;
                        if((int)mediaPlayer.getCurrentTime().toSeconds()== (int)mediaPlayer.getTotalDuration().toSeconds()){
                            secondTimer.stop();
                        }
                    }else {
                        whatSongfield.setText(song.substring(0,counter));
                    }
                });
                secondTimer.start();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void showMediaTime() {
        tTime = 0;
        timer = null;

        onePercent = (0.01* mediaPlayer.getTotalDuration().toMillis());





        timer = new Timer((int)onePercent, e ->{
                progressBar.setProgress(tTime);
                tTime = tTime + 0.01;
           // System.out.println(mediaPlayer.getCurrentTime().toSeconds()/100 );
            if(tTime > 1){
                try {
                    timer.stop();
                    progressBar.setProgress(0);
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
                mediaPlayer.setAutoPlay(true);
            });

            mediaPlayer.setOnPlaying(() -> {
                try{
                       movingText();
                       showMediaTime();
                }catch (Exception e){
                    System.out.println(e);
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {

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
                timer.stop();
                secondTimer.stop();
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

                timer.stop();
                secondTimer.stop();
                mediaPlayer.stop();
                k = k - 1;
                Object element = iterator.previous();
                String path = element.toString().replace("\\", "/");
                path = URLEncoder.encode(path, "UTF-8").replace("+", "%20");
                Media media = new Media("file:///" + path);

                mediaPlayer = new MediaPlayer(media);
                mediaName = selectedFiles.get(k).getName();
                whatSongfield.setText(mediaName);

                mediaPlayer.setOnReady(() -> {
                    mediaPlayer.setAutoPlay(true);
                });

                mediaPlayer.setOnPlaying(() -> {
                    try{
                        movingText();
                        showMediaTime();
                    }catch (Exception e){
                        System.out.println(e);
                    }
                });

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
                    timer.stop();
                    secondTimer.stop();
                    whatSongfield.setText("You've stopped the song");
                    mediaPlayer.stop();
                    //I know should be pause method now, but I am also working on the stop button, which is much simpler in my view
            }
        }catch (Exception e){
            System.out.println("The error name:\n"+e);
            whatSongfield.setText("No songs on the list!");
        }

    }



    @FXML
    void setVolume(){
        try {
            mediaPlayer.setVolume(volumeSlider.getValue()/100);
        }catch (Exception e){
            System.out.println("The error name:\n"+e);
            whatSongfield.setText("No songs on the list!");
        }

    }

    @FXML
    void setLoop(){
        try {
            if(spinner.isVisible()!=true){
                spinner.setVisible(true);
            }
            else{
                spinner.setVisible(false);
            }
        }catch (Exception e){
            System.out.println("The error name:\n"+e);
        }

    }



    @FXML
    void resumeSong(){
        try {
            prevSong();
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



