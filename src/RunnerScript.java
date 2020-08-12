
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.application.Platform;
import org.json.JSONException;
import java.io.IOException;

public class RunnerScript extends Thread {
    public int getTextStatus() {return TextStatus;}
    private static int TextStatus;


        public static void DCrichPresence() {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "723090896878436412";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) -> System.out.println("Ready!");
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            presence.details = "ottomine.net";
            presence.state = "Tarih yeniden yazılıyor...";
            presence.largeImageKey = "otto-logo2";
            lib.Discord_UpdatePresence(presence);
            // in a worker thread
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}
                }
            }, "RPC-Callback-Handler").start();
        }
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
                                } catch (IOException e) {
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
