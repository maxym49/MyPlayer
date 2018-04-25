package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class Controller {

    private static MediaPlayer mediaPlayer;
    private static FileChooser fileChooser;
    private List<File> selectedFiles = new LinkedList<>();
    private ListIterator iterator;
    private int k=0;
    private int x=0;
    private int counter = 0;
    private String piosnka;
    private String mediaName;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button menuButton;

    @FXML
    private AnchorPane menu;

    @FXML
    private Button playButton;

    @FXML
    private Button exitButton;

    @FXML
    private JFXTextField whatSongfield;

    @FXML
    private JFXButton openB;


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
    void znaki(){

        piosnka = whatSongfield.getText();
        int dlugoscTekstu = piosnka.length();

        Timer timer = new Timer(100, e -> {
            counter ++;
            if(counter > dlugoscTekstu){
                whatSongfield.setText("");
                whatSongfield.setPromptText("");
                counter = 0;
            }else {
                whatSongfield.setText(piosnka.substring(0,counter));
            }

        });
        timer.start();

    }
    @FXML
    void znak(){
        try {
            piosnka = whatSongfield.getText();
            int dlugoscTekstu = piosnka.length();

            Timer timer = new Timer(150, e -> {
                counter++;
                if (counter > dlugoscTekstu) {
                    whatSongfield.setText("");
                    counter = 0;
                    whatSongfield.setText(mediaName);
                } else {
                    piosnka = whatSongfield.getText() + " ";
                    whatSongfield.setText(" " + piosnka);
                }
            });
            timer.start();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    @FXML
    void movingText(){
        try{
            Thread.sleep(100);
            x = x + 10;
            if(x>whatSongfield.getWidth()){
                x=0;
            }

        }catch (Exception e){
            System.out.println(e);
        }
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
            mediaPlayer.setAutoPlay(true);
            mediaName = selectedFiles.get(k).getName();
            whatSongfield.setText(mediaName);


            znaki();






            k++;

            mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    whatSongfield.setText("");
                if (iterator.hasNext()==true){
                    play();

                }
                else
                    whatSongfield.setText("No more songs!");
                    mediaPlayer.stop();

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
                System.out.println(path);
                path = URLEncoder.encode(path, "UTF-8").replace("+", "%20");
                System.out.println(path);
                System.out.println();
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



