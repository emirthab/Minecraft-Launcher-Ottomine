

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class LauncherUpdater {
    public static void DownloadOperation() throws InterruptedException {
        RunnerScript.runNetControl();
        String dirWeb = "https://ottomine.net/Launcher/";
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + "Ottomine-Launcher.jar").openStream());
             FileOutputStream fileOS = new FileOutputStream("Ottomine-Launcher.jar")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {

        }
        RunnerScript.runNetControl();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + "launcher-version.json").openStream());
             FileOutputStream fileOS = new FileOutputStream("launcher-version.json")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {

        }
    }

}
