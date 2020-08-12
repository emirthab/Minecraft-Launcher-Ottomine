
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UpdaterMain extends Application{
    public static Stage primaryStage = new Stage();
    public static Stage primaryStage2 = new Stage();
    private static final UpdaterMain instance = new UpdaterMain();

    @Override
    public void start(Stage primaryStage){
    }
    public static void main(String[] args){
        RunnerScript.DCrichPresence();
        RunnerScript.threader();
        launch(args);
    }
    public static void startUpdaterStage() throws IOException {
        Parent root = FXMLLoader.load(UpdaterMain.class.getResource("updater_scene.fxml"));
        primaryStage.setTitle("Ottomine Launcher");
        primaryStage.setScene(new Scene(root, 800, 350, Color.TRANSPARENT));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
    public static void StartLauncherStage() throws IOException {
        Parent root2 = FXMLLoader.load(UpdaterMain.class.getResource("launcher.fxml"));
        primaryStage2.setTitle("Ottomine Launcher");
        primaryStage2.setScene(new Scene(root2, 1156, 650));
        primaryStage2.initStyle(StageStyle.TRANSPARENT);
        primaryStage2.show();
    }
    public static UpdaterMain getInstance() {
        return instance;
    }
    public static void stopLauncherStage(){
        primaryStage2.close();
    }
    public static void stopUpdaterStage(){
        primaryStage.close();
    }
}
