package updater;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloaderScript {



    public static int CurrentFileCount;
    public static String CurrentJSONpath;
    private static int totalByte;
    private static int currentByte;
    public int getCurrentFileCount() {return CurrentFileCount;}
    public String getCurrentJSONpath() {return CurrentJSONpath;}
    public void setCurrentJSONpath(String currentJSONpath) { CurrentJSONpath = currentJSONpath;}
    public int getTotalByte() {return totalByte;}
    public void setTotalByte(int totalByte) {DownloaderScript.totalByte = totalByte;}
    public int getCurrentByte() {return currentByte;}
    public void DownloadOperation() throws IOException, InterruptedException {

        RunnerScript.runNetControl();
        String dirWeb = "https://ottomine.net/ottomine-files/";
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(dirWeb + CurrentJSONpath).openStream());
             FileOutputStream fileOS = new FileOutputStream("../" + CurrentJSONpath)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
                currentByte += byteContent;
            }CurrentFileCount ++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
