
import javafx.application.Platform;
import org.json.JSONException;

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
        }else{
            LauncherUpdater.DownloadOperation();
            System.exit(0);
        }
    }
        public static void threader () {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    try {
                        RunnerScript.Starter();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    UpdaterMain.StartLauncherStage();
                                    Thread.sleep(500);
                                    UpdaterMain.stopUpdaterStage();
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException | JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            t1.start();
        }



}
