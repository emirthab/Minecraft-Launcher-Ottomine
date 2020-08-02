package updater;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Stop;
import launcher.LauncherMain;
import org.json.JSONException;
import sun.misc.Launcher;

import java.io.IOException;

public class RunnerScript extends Thread {
    public int getTextStatus() {return TextStatus;}
    private static int TextStatus;
    public static void runNetControl() throws InterruptedException {
        do{
            NetworkControl.NetIsAvailable();
            if(!NetworkControl.NetIsAvailable()){
                Thread.sleep(3000);
            }
        }while(!NetworkControl.NetIsAvailable());
    }
    public void runWriter() {
        try {
            WriterData.GetJsonWeb();
            WriterData.OperationLister();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void runReader() throws IOException, JSONException, InterruptedException {
        ReaderData reader1 = new ReaderData();
        reader1.OperationNonFolder();
        reader1.OperationDownloadTask();
    }

    public static void Starter() throws InterruptedException, IOException, JSONException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdaterMain.startUpdaterStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        RunnerScript Runner = new RunnerScript();
        TextStatus = 1;
        Runner.runNetControl();
        if (WriterData.launcherIsUpToDate()) {
            Thread.sleep(2000);
            TextStatus = 2;
            Runner.runWriter();
            Thread.sleep(400);
            TextStatus = 3;
            Thread.sleep(300);//100den büyük thread zorunlu
            TextStatus = 4;
            Runner.runReader();
            TextStatus = 5;
            Platform.runLater(() -> {
                UpdaterMain.stopUpdaterStage();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    LauncherMain.startLauncherStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else{
            LauncherUpdater.DownloadOperation();
            System.exit(0);
        }
    }

        public static void threader () {
            Thread t1 = new Thread() {
                public void run() {
                    try {
                        RunnerScript.Starter();
                    } catch (InterruptedException | JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            t1.start();
        }


}
