package updater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class WriterData {
    private static JSONArray ArrayDownloadTask = new JSONArray();
    private static JSONArray ArrayNonFolders = new JSONArray();
    private static JSONArray FilesArray;
    private static JSONObject versionJSONweb;
    private static JSONObject versionJSONlocal;
    /**  GETTER SETTER */
    public JSONArray getArrayDownloadTask() {
        return ArrayDownloadTask;
    }
    public JSONArray getArrayNonFolders() {
        return ArrayNonFolders;
    }
    public static JSONArray getFilesArray() {
        return FilesArray;
    }
    public static void setFilesArray(JSONArray filesArray) {
        FilesArray = filesArray;
    }
    public static void GetJsonWeb() throws JSONException, IOException, InterruptedException {
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
            do{
                NetworkControl.NetIsAvailable();
                if (NetworkControl.NetIsAvailable()){
                    GetJsonWeb();
                }
                Thread.sleep(1000);
            }while(FilesArray == null);
        }

    }

    public static void OperationLister() throws IOException, JSONException, InterruptedException {
            DownloaderScript $ = new DownloaderScript();
            for (int i = 0; i < FilesArray.length(); i++) {
                JSONObject CurrentJSONobject = (JSONObject) FilesArray.get(i);
                String CurrentJSONpath = (String) CurrentJSONobject.get("path");
                String CurrentJSONtype = (String) CurrentJSONobject.get("type");
                int CurrentJSONsize = (int)CurrentJSONobject.get("size");
                File CurrentFile = new File("../" + CurrentJSONpath);
                int CurrentFileSize = (int)CurrentFile.length();

                if (CurrentJSONtype.equals("file")) {
                    if (CurrentFile.exists()) {
                        if (CurrentJSONsize != CurrentFileSize) {
                            ArrayDownloadTask.put(CurrentJSONobject);
                            int totalbyte = $.getTotalByte() + CurrentJSONsize;
                            $.setTotalByte(totalbyte);
                        }
                    } else {
                        ArrayDownloadTask.put(CurrentJSONobject);
                        int totalbyte = $.getTotalByte() + CurrentJSONsize;
                        $.setTotalByte(totalbyte);
                    }

                }
                if (CurrentJSONtype.equals("folder")) {
                    if (!CurrentFile.exists()) {
                        ArrayNonFolders.put(CurrentJSONobject);
                    }
                }
            }
        }

        public static boolean launcherIsUpToDate() throws IOException, InterruptedException, JSONException {
            URL versionJSONwebURL = new URL("https://www.ottomine.net/Launcher/launcher-version.json");
            String JSON_string;
            try {
                InputStream is = versionJSONwebURL.openConnection().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder JsonStringBuilder = new StringBuilder();
                while ((JSON_string = reader.readLine()) != null) {
                    JsonStringBuilder.append(JSON_string + '\n');
                }
                versionJSONweb = new JSONObject(JsonStringBuilder.toString());
                reader.close();
            } catch (UnknownHostException | SocketException | JSONException HostError) {
                do {
                    NetworkControl.NetIsAvailable();
                    if (NetworkControl.NetIsAvailable()) {
                        launcherIsUpToDate();
                    }
                    Thread.sleep(1000);
                } while (FilesArray == null);
            }
            File getVersionJson = new File("launcher-version.json");
            if (getVersionJson.exists()) {
                BufferedReader MyBR = new BufferedReader(new FileReader("launcher-version.json"));
                String line;
                StringBuilder MySB = new StringBuilder();
                while ((line = MyBR.readLine()) != null) {
                    MySB.append(line + '\n');
                }
                versionJSONlocal = new JSONObject(MySB.toString());
            } else {
                boolean isUpToDate = false;
                return isUpToDate;
            }
            String localLauncherVersion = versionJSONlocal.getString("version-hash");
            String webLauncherVersion =  versionJSONweb.getString("version-hash");
            if (webLauncherVersion.equals(localLauncherVersion)){
            return true;
            }else{
                return false;
            }

        }
    }






