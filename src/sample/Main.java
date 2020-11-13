package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application implements EventHandler<ActionEvent> {
    public static void main(String[] args) {
        launch(args);
    }

    Button button;
    Button finish;
    Button diceroller;
    Stage window;
    ArrayList<Scene> scenelist;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scenelist = new ArrayList<>();
        createStartScene(primaryStage);
        loadScene(0,"Menu");
        //primaryStage.isResizable(false);
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == button) {
            Label label1 = new Label("Name:");
            GridPane layout2 = new GridPane();
            layout2.setPadding(new Insets(10, 10, 10, 10));
            layout2.setVgap(5);
            layout2.setHgap(5);
            finish = new Button();
            finish.setOnAction(this);
            finish.setText("Finish");
            TextField textField = new TextField();
            GridPane.setConstraints(label1, 0, 0);
            GridPane.setConstraints(textField, 1, 0);
            GridPane.setConstraints(finish,2,0);
            layout2.getChildren().addAll(label1, textField, finish);

            scenelist.add(new Scene(layout2, 300, 250));
            loadScene(1,"test");
        }else if(actionEvent.getSource() == finish ){
            Platform.exit();
        }else if(actionEvent.getSource()== diceroller){
            GridPane dicePane = new GridPane();
            ImageView imageView = null;
            Group group = null;
            Scene scene = null;
            ArrayList<Image> imagelist = new ArrayList<Image>();
            for (int i=1;i<=2;i++){
                try {
                    Image image = new Image(new FileInputStream(i+".png"),50,50,true,true);
                    imagelist.add(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Random r = new Random();
            int index = r.nextInt(imagelist.size());
            imageView = new ImageView(imagelist.get(index));
            group = new Group(imageView);
            scene = new Scene(group, 300, 250);
            window.setScene(scene);
        }
    }

    public void loadScene(int id,String title){
        window.setTitle(title);
        window.setScene(scenelist.get(id));
        window.show();
    }

    public void createStartScene(Stage primaryStage) throws IOException {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window.setScene(new Scene(root, 300, 275));
        button = new Button();
        button.setText("Click me");
        diceroller = new Button();
        diceroller.setText("Dice Roller app");
        diceroller.setOnAction(this);
        button.setOnAction(this);
        GridPane startlayout = new GridPane();
        startlayout.add(button,0,0);
        startlayout.add(diceroller,1,0);
        scenelist.add(new Scene(startlayout, 300, 250));
    }
}

