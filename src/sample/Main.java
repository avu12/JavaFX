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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application implements EventHandler<ActionEvent> {
    public static void main(String[] args) {
        launch(args);
    }
    Button testpage_b;
    Button finish_b;
    Button diceroller_b;
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
        if (actionEvent.getSource() == testpage_b) {
            Label label1 = new Label("Name:");
            GridPane layout2 = new GridPane();
            layout2.setPadding(new Insets(10, 10, 10, 10));
            layout2.setVgap(5);
            layout2.setHgap(5);
            finish_b = new Button();
            finish_b.setOnAction(this);
            finish_b.setText("Finish");
            TextField textField = new TextField();
            GridPane.setConstraints(label1, 0, 0);
            GridPane.setConstraints(textField, 1, 0);
            GridPane.setConstraints(finish_b,2,0);
            layout2.getChildren().addAll(label1, textField, finish_b);
            CustomScene c = new CustomScene();
            c.scene = new Scene(layout2, 300, 250);
            c.title = "test";
            scenelist.add(c);
            loadScene("test");
        }else if(actionEvent.getSource() == finish_b ){
            Platform.exit();
        }else if(actionEvent.getSource()== diceroller_b) {
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
        testpage_b = new Button();
        testpage_b.setText("Click me");
        diceroller_b = new Button();
        diceroller_b.setText("Dice Roller app");
        diceroller_b.setOnAction(this);
        testpage_b.setOnAction(this);
        GridPane startlayout = new GridPane();
        startlayout.add(testpage_b,0,0);
        startlayout.add(diceroller_b,1,0);
        CustomScene c = new CustomScene();
        c.scene = new Scene(startlayout, 450, 350);
        c.title = "Menu";
        scenelist.add(c);
    }
    public void createDiceScene() throws FileNotFoundException {
        scenelist.add(DiceScene());
    }
    public CustomScene  DiceScene() throws FileNotFoundException {

        Button rerollbutton= new Button();
        Button save = new Button();
        ArrayList<ExtentedButton> bl = new ArrayList<ExtentedButton>();
        Spinner<Integer> intSpinner = new Spinner<Integer>();
        SpinnerValueFactory<Integer> intSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,15,3);
        GridPane dicePane = new GridPane();
        ColumnConstraints col = new ColumnConstraints();
        Scene scene = null;
        Random r = new Random();


        intSpinner.setValueFactory(intSpinnerFactory);
        intSpinner.setEditable(true);
        save.setText("Save data");
        rerollbutton.setText("Reroll");

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

        for (int i =0; i< howManyDice;i++){
            dicePane.getColumnConstraints().add(col);
        }

        dicePane.getChildren().add(intSpinner);
        dicePane.add(rerollbutton,1,0);
        dicePane.add(save,2,0);

        for(int i=0;i<howManyDice;i++){
            ExtentedButton eb = new ExtentedButton();
            eb.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int posrandom = r.nextInt(6)+1;
                    eb.data = posrandom;
                    eb.setGraphic(new ImageView(new Image(getClass().getResource("/res/"+posrandom+".png").toString(),50,50,true,true)));
                }
            });
            int posrandom = r.nextInt(6)+1;
            eb.data = posrandom;
            eb.setGraphic(new ImageView(new Image(getClass().getResource("/res/"+posrandom+".png").toString(),50,50,true,true)));
            bl.add(eb);
        }
        for(int i=0;i<bl.size();i++){
            dicePane.add(bl.get(i),i,1);
        }
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                writeButtonListToFile(bl,"sample.txt");
            }
        });


        CustomScene c = new CustomScene();
        c.scene = new Scene(dicePane, 450, 350);
        c.title = "dice";
        return c;
    }


    public void writeButtonListToFile(ArrayList<ExtentedButton> bl ,String filename){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i=0;i<bl.size();i++){

            String s = Integer.toString(bl.get(i).data);

            try {
                out.write((s+"\t").getBytes() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            out.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}


