package updater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class ReaderData {
    DownloaderScript $ = new DownloaderScript();
    updater.WriterData ƒ = new updater.WriterData();

    public void OperationNonFolder() throws JSONException {
        if (ƒ.getArrayNonFolders().length() > 0) {
        for (int i = 0; i < ƒ.getArrayNonFolders().length(); i++) {
            JSONObject CurrentNonFolderArray = (JSONObject) ƒ.getArrayNonFolders().get(i);
            String CurrentJSONpath = (String) CurrentNonFolderArray.get("path");
            File CurrentFile = new File("../" + CurrentJSONpath);
            CurrentFile.mkdir();
        }
        }
    }

        public void OperationDownloadTask() throws IOException, JSONException, InterruptedException {
            if (ƒ.getArrayDownloadTask().length() > 0){
            for (int i = 0; i < ƒ.getArrayDownloadTask().length(); i++) {
                JSONObject CurrentNonFileArray = (JSONObject) ƒ.getArrayDownloadTask().get(i);
                $.setCurrentJSONpath((String) CurrentNonFileArray.get("path"));
                $.DownloadOperation();
            }
        }
        }
}

