package updater;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UpdaterMain extends Application{
    public static Stage primaryStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{

    }
    public static void main(String[] args){
        RunnerScript.threader();
    }
    public static void startUpdaterStage()throws IOException {
        Parent root = FXMLLoader.load(UpdaterMain.class.getResource("updater_scene.fxml"));
        primaryStage.setTitle("Ottomine Launcher");
        primaryStage.setScene(new Scene(root, 600, 373));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
    public static void stopUpdaterStage(){
        primaryStage.close();
    }
}
