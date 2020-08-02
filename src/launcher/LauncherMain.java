package launcher;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class LauncherMain extends Application{
    public static Stage primaryStage2 = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        startLauncherStage();
    }
    public static void main(String[] args){
        launch(args);
    }
    public static void startLauncherStage() throws IOException {
        Parent root2 = FXMLLoader.load(LauncherMain.class.getResource("launcher.fxml"));
        primaryStage2.setTitle("Ottomine Launcher");
        primaryStage2.setScene(new Scene(root2, 475, 650));
        primaryStage2.initStyle(StageStyle.TRANSPARENT);
        primaryStage2.show();
    }
}
