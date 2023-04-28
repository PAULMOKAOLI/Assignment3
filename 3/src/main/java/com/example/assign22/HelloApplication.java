package com.example.assign22;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String style=getClass().getResource("/Style.css").toExternalForm();
        BorderPane root=new BorderPane();
        root.setId("root");
        HBox media=new HBox();
        root.setCenter(media);
        HBox hBox=new HBox();
        hBox.setId("hbox");
        hBox.setPrefSize(600,25);
        root.setBottom(hBox);
        VBox vBox=new VBox();
        root.setBottom(vBox);

        Button FileChooser=new Button("File Chooser");
        Button Play=new Button("Play");
        Button Stop=new Button("Stop");
        Button Pause=new Button("Pause");

        final String[] Path = new String[1];
        MediaPlayer mediaPlayer = null;
        MediaView mediaView=new MediaView();
        Slider slider = new Slider();
        Slider volume = new Slider();

//==========Taking Media From Files=======================================================//
        FileChooser.setOnAction(actionEvent -> {
            FileChooser file=new FileChooser();
            var File=file.showOpenDialog(stage);
            Path[0] =File.toURI().toString();

            if(Path[0] != null){
                Media media1=new Media(Path[0]);
                MediaPlayer MediaPlayer = new MediaPlayer(media1);
                mediaView.setMediaPlayer(MediaPlayer);

                DoubleProperty width=mediaView.fitWidthProperty();
                DoubleProperty height=mediaView.fitHeightProperty();
                width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
                height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"hieght"));

//=================Slider PrograssBar========================================//
                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        slider.setValue(newValue.toSeconds());
                    }
                });
                slider.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(slider.getValue()));
                    }
                });
                slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(slider.getValue()));
                    }});
                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        Duration total=media1.getDuration();
                        slider.setMax(total.toSeconds());
                    }
                });
//===============Volume Slider===============================================//
                volume.setValue(mediaPlayer.getVolume() * 100);
                volume.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        mediaPlayer.setVolume(volume.getValue()/100);
                    }
                });
                mediaPlayer.play();
            }
        });
//==========Stop Button=======================================================*/
        Stop.setOnAction(actionEvent -> {
            mediaPlayer.stop();
        });
//==========Play Button=======================================================//
        Play.setOnAction(actionEvent -> {
            mediaPlayer.play();
        });
//==========Pause Button=======================================================//
        Pause.setOnAction(actionEvent -> {
            mediaPlayer.pause();
        });

        vBox.getChildren().addAll(slider,hBox);
        hBox.getChildren().addAll(FileChooser,Play,Stop,Pause,volume);

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(style);
        stage.setTitle("Assignment 3");
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    stage.setFullScreen(true);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}