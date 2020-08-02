package launcher;

import com.sun.org.apache.xerces.internal.xs.StringList;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import updater.NetworkControl;
import updater.WriterData;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

public class LauncherController implements Initializable {
    @FXML
    public ImageView remind_tick;
    public MediaView background_video;
    public ImageView remind_check;
    public ImageView input_name;
    public ImageView play_button;
    public ImageView web_site;
    public ImageView bar_settings;
    public ImageView bar_close;
    public ImageView bar_under;
    public ImageView company_logo;
    public ImageView discord_logo;
    private Media me;
    private MediaPlayer mp;
    private Thread thread;
    private JSONArray FilesArray;
    private final List<String> libraries = new ArrayList<>();
    private Process proc = null;

    public static void openBrowserUrl(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (WriterData.libraries.isEmpty()) {
            thread = new Thread(() -> {
                try {
                    GetJsonWeb();
                    OperationLister();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }

        discord_logo.setOnMouseReleased((event) -> {
            openBrowserUrl("https://discord.gg/U2MkdfC");
        });
        company_logo.setOnMouseReleased((event) -> {
            openBrowserUrl("https://www.wehoog.com");
        });
        web_site.setOnMouseReleased((event) -> {
            openBrowserUrl("https://www.ottomine.net");
        });
        remind_tick.setOnMouseReleased((event) -> {
            if (remind_tick.getOpacity() == 1.0) {
                remind_tick.setOpacity(0.0);
            } else {
                remind_tick.setOpacity(1.0);
            }
        });

        Thread loopVideo = new Thread() {
            public void run() {
                boolean loop = true;
                do {
                    String pathMedia = new File("src/resources/launcher_res/launcher_back.mp4").getAbsolutePath();
                    me = new Media(new File(pathMedia).toURI().toString());
                    mp = new MediaPlayer(me);
                    background_video.setMediaPlayer(mp);
                    mp.play();
                    try {
                        Thread.sleep(11000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (loop);
            }

        };
        loopVideo.start();
        fadeIn(discord_logo);
        fadeOut(discord_logo);
        fadeIn(company_logo);
        fadeOut(company_logo);
        fadeIn(play_button);
        fadeOut(play_button);
    }//end of initialize

    @FXML
    public void launchGame() throws InterruptedException {
        if (thread != null) thread.join();
        try {
            File librarydir = new File("libraries" + File.separator);
            ArrayList<String> names = new ArrayList<>(Arrays.asList(Objects.requireNonNull(librarydir.list())));
            names.replaceAll(s -> librarydir.getPath() + "\\" + s);

            String OperatingSystemToUse = getOS();
            String gameDirectory = "";
            String AssetsRoot = "assets";

            int Xmx = 1024;
            String JavaPath = "java";
            String versionName = "1.12.2-LiteLoader1.12.2-1.12.2-forge1.12.2-14.23.5.2847";
            String assetsIndexId = "1.12.2-LiteLoader1.12.2-1.12.2-forge1.12.2-14.23.5.2847";

            String VersionType = "release";
            String GameAssets = "assets";
            String AuthSession = "OFFLINE";

            String[] HalfArgument = generateMinecraftArguments("Dantero", versionName, gameDirectory, AssetsRoot, assetsIndexId, "0", "0", "{}", "mojang", VersionType, GameAssets, AuthSession);
            String MinecraftJar = "versions" + File.separator + "1.12.2-forge1.12.2-14.23.5.2847" + File.separator + "1.12.2-forge1.12.2-14.23.5.2847.jar";
            String FullLibraryArgument = generateLibrariesArguments(OperatingSystemToUse) + getArgsDiv(OperatingSystemToUse) + MinecraftJar;
            String mainClass = "net.minecraft.launchwrapper.Launch";
            String NativesDir = "versions" + File.separator + "1.12.2-forge1.12.2-14.23.5.2847" + File.separator + "natives";

            String[] cmds = {"-Xmx" + Xmx + "M", "-XX:+UseConcMarkSweepGC", "-XX:+CMSIncrementalMode", "-XX:-UseAdaptiveSizePolicy", "-Xmn128M", "-Djava.library.path=" + NativesDir, "-cp", FullLibraryArgument, mainClass};

            String[] javaPathArr = {JavaPath};
            cmds = Stream.concat(Arrays.stream(javaPathArr), Arrays.stream(cmds)).toArray(String[]::new);

            String[] finalArgs = Stream.concat(Arrays.stream(cmds), Arrays.stream(HalfArgument)).toArray(String[]::new);

            try {
                proc = Runtime.getRuntime().exec(finalArgs);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (proc != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line;
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(proc.getErrorStream()));
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    while ((line = stdError.readLine()) != null) {
                        System.out.println(line);
                    }
                    proc.destroy();
                    LauncherMain.getInstance().stop();
                    Platform.exit();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String generateLibrariesArguments(String OS) {
        StringBuilder cp = new StringBuilder();

        List<String> list = new ArrayList<>(libraries);

        List<String> sorted = new ArrayList<>(list);

        for (int i = 0; i < sorted.size(); i++) {

            if (cp.toString().contains(sorted.get(i))) continue;

            if (i == sorted.size() - 1) {

                cp.append(sorted.get(i));

            } else {
                cp.append(sorted.get(i)).append(getArgsDiv(OS));

            }
        }
        return cp.toString();
    }

    String getArgsDiv(String OS) {
        if (OS.equals("Windows")) {
            return (";");
        }
        if (OS.equals("Linux")) {
            return (":");
        }
        if (OS.equals("Mac")) {
            return (":");
        }

        return "N/A";
    }

    String[] generateMinecraftArguments(String auth_player_name, String version_name, String game_directory, String assets_root, String assets_index_name, String auth_uuid, String auth_access_token, String user_properties, String user_type, String version_type, String game_assets, String auth_session) {

        String cmdArgs = readJson_minecraftArguments(getMineCraft_Versions_X_X_json(version_name));

        cmdArgs = cmdArgs.replaceAll(" +", " ");
        String[] tempArgsSplit = cmdArgs.split(" ");
        for (int i = 0; i < tempArgsSplit.length; i++) {
            if (tempArgsSplit[i].equals("${auth_player_name}")) {
                tempArgsSplit[i] = auth_player_name;
            }
            if (tempArgsSplit[i].equals("${version_name}")) {
                tempArgsSplit[i] = version_name;
            }
            if (tempArgsSplit[i].equals("${game_directory}")) {
                tempArgsSplit[i] = game_directory;
            }
            if (tempArgsSplit[i].equals("${assets_root}")) {
                tempArgsSplit[i] = assets_root;
            }
            if (tempArgsSplit[i].equals("${assets_index_name}")) {
                tempArgsSplit[i] = assets_index_name;
            }
            if (tempArgsSplit[i].equals("${auth_uuid}")) {
                tempArgsSplit[i] = auth_uuid;
            }
            if (tempArgsSplit[i].equals("${auth_access_token}")) {
                tempArgsSplit[i] = auth_access_token;
            }
            if (tempArgsSplit[i].equals("${user_properties}")) {
                tempArgsSplit[i] = user_properties;
            }
            if (tempArgsSplit[i].equals("${user_type}")) {
                tempArgsSplit[i] = user_type;
            }
            if (tempArgsSplit[i].equals("${version_type}")) {
                tempArgsSplit[i] = version_type;
            }
            if (tempArgsSplit[i].equals("${game_assets}")) {
                tempArgsSplit[i] = game_assets;
            }
            if (tempArgsSplit[i].equals("${auth_session}")) {
                tempArgsSplit[i] = auth_session;
            }
        }
        return tempArgsSplit;
    }

    private String readJson_minecraftArguments(String path) {
        try {
            FileReader reader = new FileReader(path);
            JSONObject jsonObject = new JSONObject(reader);

            return jsonObject.getString("minecraftArguments");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private String getMineCraft_Versions_X_X_json(String VersionNumber) {
        return ("versions" + File.separator + VersionNumber + File.separator + VersionNumber + ".json");
    }

    private String getOS() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            return ("Mac");
        } else if (OS.indexOf("win") >= 0) {
            return ("Windows");
        } else if (OS.indexOf("nux") >= 0) {
            return ("Linux");
        } else {
            //bring support to other OS.
            //we will assume that the OS is based on linux.
            return ("Linux");
        }
    }

    private List<String> getLibraries() {
        if (WriterData.libraries.isEmpty()) {
            return libraries;
        } else {
            return WriterData.libraries;
        }
    }

    void GetJsonWeb() throws JSONException, IOException, InterruptedException {
        String JSON_string;
        URL JSONurl = new URL("https://www.ottomine.net/ottomine-files/FilesArray.json");
        try {
            InputStream is = JSONurl.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder JsonStringBuilder = new StringBuilder();
            while ((JSON_string = reader.readLine()) != null) {
                JsonStringBuilder.append(JSON_string + '\n');
            }
            FilesArray = new JSONArray(JsonStringBuilder.toString());
            reader.close();
        } catch (UnknownHostException | SocketException HostError) {
            do {
                NetworkControl.NetIsAvailable();
                if (NetworkControl.NetIsAvailable()) {
                    GetJsonWeb();
                }
                Thread.sleep(1000);
            } while (FilesArray == null);
        }
    }

    private void OperationLister() throws JSONException {
        for (int i = 0; i < FilesArray.length(); i++) {
            JSONObject CurrentJSONobject = (JSONObject) FilesArray.get(i);
            String CurrentJSONpath = (String) CurrentJSONobject.get("path");
            String CurrentJSONtype = (String) CurrentJSONobject.get("type");

            if (CurrentJSONtype.equals("file")) {
                if (CurrentJSONpath.startsWith("libraries")) {
                    libraries.add(CurrentJSONpath.substring("libraries/".length() - 1));
                }
                if (CurrentJSONpath.startsWith("mods")) {
                    libraries.add(CurrentJSONpath.substring("mods/".length() - 1));
                }
            }
        }
    }

    /***** FADES   *****/
    public void fadeIn(ImageView current_fading) {
        current_fading.setOnMouseEntered((event) -> {
            if (current_fading == play_button) {
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), play_button);
                fadeIn.setFromValue(0.7);
                fadeIn.setToValue(1);
                fadeIn.play();
            } else {
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), current_fading);
                fadeIn.setFromValue(1.0);
                fadeIn.setToValue(0.0);
                fadeIn.play();
            }
        });
    }

    public void fadeOut(ImageView current_fading) {
        current_fading.setOnMouseExited((event) -> {
            if (current_fading == play_button) {
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), play_button);
                fadeIn.setFromValue(1);
                fadeIn.setToValue(0.7);
                fadeIn.play();
            } else {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), current_fading);
                fadeOut.setFromValue(0.0);
                fadeOut.setToValue(1.0);
                fadeOut.play();
            }
        });
    }

    /***** FADES   *****/
}


