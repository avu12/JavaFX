package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    ArrayList<CustomScene> scenelist;
    int howManyDice;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scenelist = new ArrayList<>();
        createStartScene(primaryStage);
        loadScene("Menu");
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
            CustomScene c = new CustomScene();
            c.scene = new Scene(layout2, 300, 250);
            c.title = "test";
            scenelist.add(c);
            loadScene("test");
        }else if(actionEvent.getSource() == finish ){
            Platform.exit();
        }else if(actionEvent.getSource()== diceroller) {
            try {
                howManyDice=3;
                createDiceScene();
                loadScene("dice");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadScene(String title){
        for(int i=0;i<scenelist.size();i++){
            if (scenelist.get(i).title == title){
                window.setTitle(title);
                window.setScene(scenelist.get(i).scene);
                window.show();
            }
        }

    }

    public void createStartScene(Stage primaryStage) throws IOException {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window.setScene(new Scene(root, 450, 350));
        button = new Button();
        button.setText("Click me");
        diceroller = new Button();
        diceroller.setText("Dice Roller app");
        diceroller.setOnAction(this);
        button.setOnAction(this);
        GridPane startlayout = new GridPane();
        startlayout.add(button,0,0);
        startlayout.add(diceroller,1,0);
        CustomScene c = new CustomScene();
        c.scene = new Scene(startlayout, 450, 350);
        c.title = "Menu";
        scenelist.add(c);
    }
    public void createDiceScene() throws FileNotFoundException {
        scenelist.add(DiceScene());
    }
    public CustomScene  DiceScene() throws FileNotFoundException {

        Button rerollbutton;
        rerollbutton = new Button();
        rerollbutton.setText("Reroll");

        ArrayList<Button> bl = new ArrayList<Button>();
        Spinner<Integer> intSpinner = new Spinner<Integer>();
        SpinnerValueFactory<Integer> intSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5,3);
        intSpinner.setValueFactory(intSpinnerFactory);
        intSpinner.setEditable(true);


        GridPane dicePane = new GridPane();

        ColumnConstraints col = new ColumnConstraints();
        col.setMaxWidth(100);
        col.setHalignment(HPos.LEFT);
        col.setHgrow(Priority.ALWAYS);
        rerollbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                howManyDice = intSpinnerFactory.getValue();
                try {
                    window.setScene(DiceScene().scene);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        dicePane.getColumnConstraints().addAll(col,col,col,col,col,col);
        dicePane.getChildren().add(intSpinner);
        dicePane.add(rerollbutton,1,0);



        //dicePane.setGridLinesVisible(true);


        Scene scene = null;
        ArrayList<Image> imagelist = new ArrayList<Image>();
        for (int i=1;i<=6;i++){
            Image image = new Image(getClass().getResource("/res/"+i+".png").toString(),50,50,true,true);
            imagelist.add(image);
        }
        Random r = new Random();
        int index = r.nextInt(imagelist.size());
        for(int i=0;i<howManyDice;i++){
            ImageView bview = null;
            Button b = new Button();
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int posrandom = r.nextInt(6)+1;
                    b.setGraphic(new ImageView(new Image(getClass().getResource("/res/"+posrandom+".png").toString(),50,50,true,true)));
                }
            });
            int posrandom = r.nextInt(6)+1;
            b.setGraphic(new ImageView(new Image(getClass().getResource("/res/"+posrandom+".png").toString(),50,50,true,true)));
            bl.add(b);
        }
        for(int i=0;i<bl.size();i++){
            dicePane.add(bl.get(i),i,1);
        }


        CustomScene c = new CustomScene();
        //dicePane.add(group,0,0);
        c.scene = new Scene(dicePane, 450, 350);
        c.title = "dice";
        //scenelist.add(c);
        return c;

    }




}


