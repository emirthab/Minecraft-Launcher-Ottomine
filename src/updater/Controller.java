package updater;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import launcher.LauncherMain;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    WriterData ƒ = new WriterData();
    DownloaderScript $ = new DownloaderScript();
    RunnerScript Rs = new RunnerScript();

    @FXML
    public Text downloading_file;
    public Text download_status;
    public Image check_status;
    public ImageView checker_text;
    public Pane Progress_Black;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    Thread t2 = new Thread(){
            public void run() {
                Rs.getTextStatus();
                do {
                    Rs.getTextStatus();
                    checker_text.setImage(check_status);
                    if (Rs.getTextStatus() == 1) {
                        check_status = new Image("resources/updater_res/Network_check.png");
                    }
                    if (Rs.getTextStatus() == 2) {
                        check_status = new Image("resources/updater_res/Update_check.png");
                    }
                    if (Rs.getTextStatus() == 3) {
                        check_status = new Image("resources/updater_res/Update_downloading.png");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (Rs.getTextStatus() < 4);
                int totalFilesCount = ƒ.getArrayDownloadTask().length();
                do{
                    if(ƒ.getArrayDownloadTask().length()>0) {
                        try {
                            downloading_file.setText($.getCurrentJSONpath());
                            download_status.setText($.getCurrentFileCount() + " / " + totalFilesCount);
                            float progress_width = ((float) $.getCurrentByte() / (float) $.getTotalByte()) * 465;
                            Progress_Black.setPrefWidth(465 - progress_width);
                        } catch (ArithmeticException arEx) {
                            //aritmetik hata
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }while(Rs.getTextStatus() == 4);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        t2.start();
    }



}


