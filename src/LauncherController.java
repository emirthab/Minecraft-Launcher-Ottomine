

import com.sun.xml.internal.bind.v2.TODO;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

public class LauncherController implements Initializable {
    private final List<String> libraries = new ArrayList<>();
    @FXML
    public ImageView remind_tick;
    public MediaView background_video;
    public ImageView remind_check;
    public ImageView play_button;
    public ImageView web_site;
    public ImageView bar_settings;
    public ImageView bar_close;
    public ImageView company_logo;
    public ImageView discord_logo;
    public Text warning_text;
    public TextField input_name;
    public ProgressIndicator loading_launch;
    //*****  settings  *****\\
    public String temple_graph = "";
    public TextField xmx_input;
    public TextField xms_input;
    public AnchorPane settings_anchor;
    public ImageView settings_back_icon;
    public ImageView graph_veryhigh_back;
    public ImageView graph_veryhigh_icon;
    public ImageView graph_veryhigh_text;
    public ImageView graph_high_back;
    public ImageView graph_high_icon;
    public ImageView graph_high_text;
    public ImageView graph_medium_back;
    public ImageView graph_medium_icon;
    public ImageView graph_medium_text;
    public ImageView graph_low_back;
    public ImageView graph_low_icon;
    public ImageView graph_low_text;
    public ImageView graph_verylow_back;
    public ImageView graph_verylow_icon;
    public ImageView graph_verylow_text;
    public ImageView graph_current_image;
    //*****  settings end  *****\\

    List<String> version_path_list_natives = new ArrayList<>();
    private Media me;
    private MediaPlayer mp;
    private Thread thread;
    private JSONArray FilesArray;
    private Process proc = null;
    public String SetuserName(){
        String username = input_name.getText();
        return username;
    }

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
        UpdaterMain.stopUpdaterStage();
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
        try {
            if (GetLauncherOptions().getBoolean("remindme")){
                String name = GetLauncherOptions().getString("name");
                input_name.setText(name);
                remind_tick.setOpacity(1.0);
            }else{
                remind_tick.setOpacity(0.0);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        //*****  SETTINGS  *****\\
        try {
            String jsonGraphSetting = GetLauncherOptions().getString("graphic_settings");
            if (jsonGraphSetting.equals("veryhigh")){
            temple_graph = "veryhigh";
            graph_select_veryhigh();
            }else
            if (jsonGraphSetting.equals("high")){
                temple_graph = "high";
                graph_select_high();
            }else
            if (jsonGraphSetting.equals("low")){
                temple_graph = "low";
                graph_select_low();
            }else
            if (jsonGraphSetting.equals("verylow")){
                temple_graph = "verylow";
                graph_select_verylow();
            }else{
                    temple_graph = "medium";
                    graph_select_medium();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        try {
            xmx_input.setText(String.valueOf(GetLauncherOptions().getInt("xmx")));
            xms_input.setText(String.valueOf(GetLauncherOptions().getInt("xms")));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        graph_veryhigh_text.setOnMouseReleased((event) -> {temple_graph = "veryhigh";graph_select_veryhigh();});
        graph_veryhigh_icon.setOnMouseReleased((event) -> {temple_graph = "veryhigh";graph_select_veryhigh();});
        graph_veryhigh_back.setOnMouseReleased((event) -> {temple_graph = "veryhigh";graph_select_veryhigh();});
        graph_high_back.setOnMouseReleased((event) -> {temple_graph = "high";graph_select_high();});
        graph_high_icon.setOnMouseReleased((event) -> {temple_graph = "high";graph_select_high();});
        graph_high_text.setOnMouseReleased((event) -> {temple_graph = "high";graph_select_high();});
        graph_medium_back.setOnMouseReleased((event) -> {temple_graph = "medium";graph_select_medium();});
        graph_medium_text.setOnMouseReleased((event) -> {temple_graph = "medium";graph_select_medium();});
        graph_medium_icon.setOnMouseReleased((event) -> {temple_graph = "medium";graph_select_medium();});
        graph_low_back.setOnMouseReleased((event) -> {temple_graph = "low";graph_select_low();});
        graph_low_icon.setOnMouseReleased((event) -> {temple_graph = "low";graph_select_low();});
        graph_low_text.setOnMouseReleased((event) -> {temple_graph = "low";graph_select_low();});
        graph_verylow_back.setOnMouseReleased((event) -> {temple_graph = "verylow";graph_select_verylow();});
        graph_verylow_text.setOnMouseReleased((event) -> {temple_graph = "verylow";graph_select_verylow();});
        graph_verylow_icon.setOnMouseReleased((event) -> {temple_graph = "verylow";graph_select_verylow();});


        bar_settings.setOnMouseReleased((event) -> {
            try {
                xmx_input.setText(String.valueOf(GetLauncherOptions().getInt("xmx")));
                xms_input.setText(String.valueOf(GetLauncherOptions().getInt("xms")));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            settings_anchor.setVisible(true);
            fadeOutSettingsAnchor();
        });
        settings_back_icon.setOnMouseReleased((event) -> {
            String inp_xmx = xmx_input.getText();
            String inp_xms = xms_input.getText();
            try {
                if (inp_xmx.length()<6 && inp_xmx.length()>2 && inp_xms.length()<6 && inp_xms.length()>2 ) {
                    if (inp_xmx.matches("^[0-9]+$") && inp_xms.matches("^[0-9]+$")) {
                        SetLauncherOption_settings(temple_graph,Integer.parseInt(inp_xmx),Integer.parseInt(inp_xms));
                    }else{System.out.println("değerlerde yanlış karakter");}
                }else{System.out.println("değerlerde yanlış uzunluk");}
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            fadeInSettingsAnchor();
        });

        //*****  SETTINGS END  *****\\
        readJson_libraries_downloads_classifiers_natives_Y("versions" + File.separator + "ottomine" + File.separator + "ottomine.json");
        readJson_twitch_natives("versions" + File.separator + "ottomine" + File.separator + "ottomine.json");
        bar_close.setOnMouseReleased((event) -> {
            UpdaterMain.stopLauncherStage();
            Platform.exit();
            System.exit(0);
        });
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
            try {
                if (GetLauncherOptions().getBoolean("remindme")) {
                    remind_tick.setOpacity(0);
                    SetLauncherOption_remindme(false);
                } else {
                    remind_tick.setOpacity(1.0);
                    SetLauncherOption_remindme(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        me = new Media(new File("launcher_back.mp4").toURI().toString());
        mp = new MediaPlayer(me);
        background_video.setMediaPlayer(mp);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.setAutoPlay(true);

        fadeIn(discord_logo);
        fadeOut(discord_logo);
        fadeIn(company_logo);
        fadeOut(company_logo);
        fadeIn(play_button);
        fadeOut(play_button);
    }//end of initialize

    @FXML
    public void launchGame() throws InterruptedException, IOException, JSONException {
        Thread threadlauncher = new Thread() {
            public void run() {
                loading_launch.setOpacity(1.0);
                try {
                    RunnerScript.runNetControl();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    get_graphOptions(temple_graph);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String NativesDir = "versions" + File.separator + "ottomine" + File.separator + "natives" + File.separator;

                    String OperatingSystemToUse = getOS();
                    String gameDirectory = new File(LauncherController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().toString();
                    String AssetsRoot = "assets";

                    int Xmx = GetLauncherOptions().getInt("xmx");
                    int Xms = GetLauncherOptions().getInt("xms");
                    String JavaPath = GetLauncherOptions().getString("java_path");
                    String versionName = "1.12.2-LiteLoader1.12.2-1.12.2-forge1.12.2-14.23.5.2847";
                    String assetsIndexId = "1.12";

                    String VersionType = "release";
                    String GameAssets = "assets";
                    String AuthSession = "OFFLINE";

                    String[] HalfArgument = generateMinecraftArguments(SetuserName(), versionName, gameDirectory, AssetsRoot, assetsIndexId, uuidgenerator(), "0", "{}", "mojang", VersionType, GameAssets, AuthSession);
                    String MinecraftJar = "versions" + File.separator + "ottomine" + File.separator + "ottomine.jar";
                    String FullLibraryArgument = generateLibrariesArguments(OperatingSystemToUse) + getArgsDiv(OperatingSystemToUse) + MinecraftJar;
                    String mainClass = "net.minecraft.launchwrapper.Launch";

                    String[] cmds = {"-Xmx" + Xmx + "M", "-XX:-UseAdaptiveSizePolicy", "-Xms" + Xms + "M", "-Djava.library.path=" + NativesDir, "-cp", FullLibraryArgument, mainClass};

                    String[] javaPathArr = {JavaPath};
                    cmds = Stream.concat(Arrays.stream(javaPathArr), Arrays.stream(cmds)).toArray(String[]::new);

                    String[] finalArgs = Stream.concat(Arrays.stream(cmds), Arrays.stream(HalfArgument)).toArray(String[]::new);

                    try {
                        proc = Runtime.getRuntime().exec(finalArgs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//TODO BURAYA VERİTABANI İŞLEMLERİ GELECEK(GİRİŞ YAP ve kullanıcıyı geçici veritabanına yazdır)
                    if (proc != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line;
                        BufferedReader stdError = new BufferedReader(new
                                InputStreamReader(proc.getErrorStream()));
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                    UpdaterMain.stopLauncherStage();//launcheri kapat
                            }
                        });
                        Platform.exit();
                        try {
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                            while ((line = stdError.readLine()) != null) {
                                System.out.println(line);
                            }
                            proc.destroy();
                            UpdaterMain.getInstance().stop();
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
        };
        if (SetuserName().length()<17 && SetuserName().length()>2) {
            if (SetuserName().matches("^[a-zA-Z0-9_]+$")) {
                SetLauncherOption_name(SetuserName());
                threadlauncher.start();
            } else {
                fadeWarning();
            }
        }else{fadeWarning();}
    }

    /***** Util Methods  *****/

    String generateLibrariesArguments(String OS) {
        StringBuilder cp = new StringBuilder();

        List<String> list = new ArrayList<>(getLibraries());

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

        String cmdArgs = readJson_minecraftArguments("versions" + File.separator + "ottomine" + File.separator + "ottomine.json");

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
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                json.append(line).append('\n');
            }
            JSONObject jsonObject = new JSONObject(json.toString());

            return jsonObject.getString("minecraftArguments");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "N/A";
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
                JsonStringBuilder.append(JSON_string).append('\n');
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
                    libraries.add(CurrentJSONpath);
                }
                if (CurrentJSONpath.startsWith("mods")) {
                    libraries.add(CurrentJSONpath);
                }
            }
        }
    }

    List<String> versionCheck() {
        List<String> list = new ArrayList<>(version_path_list_natives);

        List<String> removeList = new ArrayList<>();

        Collections.sort(list, (a, b) -> {
            if (a == null || b == null) return 0;
            File aFile = new File(a);
            File bFile = new File(b);
            String aname = aFile.getName();
            if (aname.isEmpty()) return 0;
            String aremoved = aname.substring(0, aname.lastIndexOf('.'));
            String bname = bFile.getName();
            if (bname.isEmpty()) return 0;
            String bremoved = bname.substring(0, bname.lastIndexOf('.'));
            for (String str : aremoved.split("-")) {
                if (isInteger(str)) {
                    if (Integer.parseInt(str) > 1000) {
                        aremoved = aremoved.replaceAll("-" + str, "");
                    }
                }
            }
            for (String str : bremoved.split("-")) {
                if (isInteger(str)) {
                    if (Integer.parseInt(str) > 1000) {
                        bremoved = bremoved.replaceAll("-" + str, "");
                    }
                }
            }
            int versiona = Integer.parseInt(aremoved.replaceAll("[\\D]", ""));
            int versionB = Integer.parseInt(bremoved.replaceAll("[\\D]", ""));
            String formattedvera = aremoved.replaceAll(getNatives_OS(getOS()), "").replaceAll("[A-Za-z]?", "").replaceAll("-", "");
            String formattedverb = bremoved.replaceAll(getNatives_OS(getOS()), "").replaceAll("[A-Za-z]?", "").replaceAll("-", "");
            if (!aname.replaceAll(getNatives_OS(getOS()), "").replaceAll(formattedvera, "").equals(
                    bname.replaceAll(getNatives_OS(getOS()), "").replaceAll(formattedverb, ""))) return 0;
            if (versiona == versionB) return 0;
            if (versiona > versionB) {
                if (!removeList.contains(b)) {
                    removeList.add(b);
                }
                return 1;
            }
            if (versiona < versionB) {
                if (!removeList.contains(a)) removeList.add(a);
                return -1;
            }
            return 0;
        });

        List<String> sortedList = list;
        sortedList.removeAll(removeList);

        return sortedList;
    }

    String getNatives_OS(String natives_OS) {
        try {
            if (natives_OS.equals("Linux")) {
                return natives_OS.replace("Linux", "natives-linux");
            } else if (natives_OS.equals("Windows")) {
                return natives_OS.replace("Windows", "natives-windows");
            } else if (natives_OS.equals("Mac")) {
                return natives_OS.replace("Mac", "natives-osx");
            } else {
                return "N/A";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "N/A";
        }
    }

    boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void readJson_twitch_natives(String path) {
        try {

            String natives_OS;
            if (getOS().equals("Linux")) {
                natives_OS = "linux";
            } else if (getOS().equals("Windows")) {
                natives_OS = "windows";
            } else if (getOS().equals("Mac")) {
                natives_OS = "osx";
            } else {
                natives_OS = "N/A";
            }
            String content = new Scanner(new File(path)).useDelimiter("\\Z").next();

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            String script_js = "var getJsonLibrariesDownloadsClassifiersNativesX=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].classifies[s].url+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesY=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].classifies[s].path+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesZ=function(r){var s=r,a=JSON.parse(s),e=\"\",n=0;for(i=0;i<500;i++)try{a.libraries[n].natives?(e=e+a.libraries[n].name+\"\\n\",n+=1):n+=1}catch(t){n+=1}return e};";

            engine.eval(script_js);

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("getJsonLibrariesDownloadsClassifiersNativesY", content, natives_OS);

            for (String retval : result.toString().split("\n")) {
                if (!retval.isEmpty()) version_path_list_natives.add(retval);
            }
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void readJson_libraries_downloads_classifiers_natives_Y(String path) {

        try {
            String natives_OS;
            if (getOS().equals("Linux")) {
                natives_OS = "linux";
            } else if (getOS().equals("Windows")) {
                natives_OS = "windows";
            } else if (getOS().equals("Mac")) {
                natives_OS = "osx";
            } else {
                natives_OS = "N/A";
            }
            String content = new Scanner(new File(path)).useDelimiter("\\Z").next();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            try {

                String script_js = "var getJsonLibrariesDownloadsClassifiersNativesX=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].url+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesY=function(s,r){var a=JSON.parse(s),e=\"\",t=0;for(i=0;i<a.libraries.length+1;i++)try{e+=a.libraries[t].classifies[\"windows\"].path + \"\\n\",t+=1}catch(i){t+=1}return e},getJsonLibrariesDownloadsClassifiersNativesZ=function(r){var s=r,a=JSON.parse(s),e=\"\",n=0;for(i=0;i<500;i++)try{a.libraries[n].natives?(e=e+a.libraries[n].classifies +\"\\n\",n+=1):n+=1}catch(t){n+=1}return e};";

                File file = new File("./.script.js");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(script_js);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            engine.eval(new FileReader("./.script.js"));

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("getJsonLibrariesDownloadsClassifiersNativesY", content, natives_OS);

            for (String retval : result.toString().split("\n")) {
                version_path_list_natives.add(retval);
            }
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    /***** Util Methods  *****/

    /***** FADES   *****/
    public void fadeInSettingsAnchor(){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), settings_anchor);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0.0);
        fadeIn.play();
        fadeIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                settings_anchor.setVisible(false);
            }
        });
    }
    public void fadeOutSettingsAnchor(){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), settings_anchor);
        fadeOut.setFromValue(0.0);
        fadeOut.setToValue(1.0);
        fadeOut.play();
    }
    public void fadeWarning(){
        FadeTransition fadeWarn = new FadeTransition(Duration.seconds(3.5), warning_text);
        fadeWarn.setFromValue(1);
        fadeWarn.setToValue(0);
        fadeWarn.play();
    }

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
    public void SetLauncherOption_settings(String graph,int xmx,int xms) throws IOException, JSONException {
        JSONObject obj = GetLauncherOptions();
        obj.put("graphic_settings",graph);
        obj.put("xmx",xmx);
        obj.put("xms",xms);
        FileWriter writer = new FileWriter("launcher-options.json");
        writer.write(obj.toString());
        writer.close();
    }
    public void SetLauncherOption_name(String value) throws IOException, JSONException {
        JSONObject obj = GetLauncherOptions();
        boolean rmndme = obj.getBoolean("remindme");
        if (rmndme){
            obj.put("name",value);
            FileWriter writer = new FileWriter("launcher-options.json");
            writer.write(obj.toString());
            writer.close();
        }
    }
    public void SetLauncherOption_remindme(boolean value) throws IOException, JSONException {
        JSONObject obj = GetLauncherOptions();
        obj.put("remindme",value);
        FileWriter writer = new FileWriter("launcher-options.json");
        writer.write(obj.toString());
        writer.close();
    }
    public static JSONObject GetLauncherOptions() throws JSONException, IOException {
        File option_path = new File("launcher-options.json");
        if (option_path.exists()){
            String line;
            BufferedReader MyBR = new BufferedReader(new FileReader("launcher-options.json"));
            StringBuilder MySB = new StringBuilder();
            while ((line = MyBR.readLine()) != null)
            {
                MySB.append(line).append('\n');
            }
            JSONObject obj = new JSONObject(MySB.toString());
            return obj;
        }else{
            JSONObject obj = new JSONObject();
            obj.put("remindme",false);
            obj.put("name","Bir Kullanıcı Adı Giriniz...");
            obj.put("graphic_settings","medium");
            obj.put("java_path","java");
            obj.put("xmx",3000);
            obj.put("xms",3000);
            FileWriter writer = new FileWriter("launcher-options.json");
            writer.write(obj.toString());
            writer.close();
            return obj;
        }
    }
    public static String uuidgenerator() throws IOException, JSONException {
        File uuidJsonFile = new File("uuid.json");
        if (uuidJsonFile.exists()){
            String line;
            BufferedReader MyBR = new BufferedReader(new FileReader("uuid.json"));
            StringBuilder MySB = new StringBuilder();
            while ((line = MyBR.readLine()) != null)
            {
                MySB.append(line + '\n');
            }
            JSONObject obj = new JSONObject(MySB.toString());
            String uuidjson = obj.getString("uuid");
            return uuidjson;
        }else{
            String[] stringutils = {"1", "a", "2", "b", "3", "4", "5", "c", "6", "d", "7", "e", "8", "f", "9"};
            String uuid;
            Random irandom = new Random();
            String array1 = new String();//8
            String array2 = new String();
            String array3 = new String();
            String array4 = new String();
            String array5 = new String();
            for (int i = 0; i < 8; i++) {
                int whichstr = irandom.nextInt(stringutils.length);
                String gen = stringutils[whichstr];
                array1 += gen;
            }
            for (int i = 0; i < 4; i++) {
                int whichstr = irandom.nextInt(stringutils.length);
                String gen = stringutils[whichstr];
                array2 += gen;
            }
            for (int i = 0; i < 4; i++) {
                int whichstr = irandom.nextInt(stringutils.length);
                String gen = stringutils[whichstr];
                array3 += gen;
            }
            for (int i = 0; i < 4; i++) {
                int whichstr = irandom.nextInt(stringutils.length);
                String gen = stringutils[whichstr];
                array4 += gen;
            }
            for (int i = 0; i < 12; i++) {
                int whichstr = irandom.nextInt(stringutils.length);
                String gen = stringutils[whichstr];
                array5 += gen;
            }
            uuid = array1 + "-" + array2 + "-" + array3 + "-" + array4 + "-" + array5;
            JSONObject obj = new JSONObject();
            obj.put("uuid",uuid);
            FileWriter writer = new FileWriter("uuid.json");
            writer.write(obj.toString());
            writer.close();
            return uuid;
        }
    }
    public void graph_select_veryhigh(){
        graph_current_image.setImage(new Image("resources/launcher_res/settings/graphs_res/veryhigh.png"));
        graph_veryhigh_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/Current.png"));
        graph_high_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_medium_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_low_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_verylow_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
    }
    public void graph_select_high(){
        graph_current_image.setImage(new Image("resources/launcher_res/settings/graphs_res/high.png"));
        graph_high_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/Current.png"));
        graph_veryhigh_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_medium_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_low_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_verylow_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
    }
    public void graph_select_medium(){
        graph_current_image.setImage(new Image("resources/launcher_res/settings/graphs_res/medium.png"));
        graph_medium_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/Current.png"));
        graph_high_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_veryhigh_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_low_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_verylow_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
    }
    public void graph_select_low(){
        graph_current_image.setImage(new Image("resources/launcher_res/settings/graphs_res/low.png"));
        graph_low_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/Current.png"));
        graph_high_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_medium_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_veryhigh_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_verylow_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
    }
    public void graph_select_verylow(){
        graph_current_image.setImage(new Image("resources/launcher_res/settings/graphs_res/verylow.png"));
        graph_verylow_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/Current.png"));
        graph_high_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_medium_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_low_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
        graph_veryhigh_back.setImage(new Image("resources/launcher_res/settings/graphic/backs/None.png"));
    }

    public void get_graphOptions(String graph_value) throws InterruptedException {
        RunnerScript.runNetControl();
        String dirWeb = "https://ottomine.net/Launcher/graph_options/" + graph_value;
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + "/options.txt").openStream());
             FileOutputStream fileOS = new FileOutputStream("options.txt")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RunnerScript.runNetControl();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + "/optionsof.txt").openStream());
             FileOutputStream fileOS = new FileOutputStream("optionsof.txt")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RunnerScript.runNetControl();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + "/optionsshaders.txt").openStream());
             FileOutputStream fileOS = new FileOutputStream("optionsshaders.txt")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


