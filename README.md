# Minecraft-Launcher-Ottomine

## What Is Ottomine

Ottomine roleplay mode is an audio role-playing game mode that takes the Ottoman era style in a utopian way. It does not fully reflect reality, historically and geographically. Only the basics in history have been treated as an Ottomine roleplay mode.

## Launcher Features

- Updates the json data according to the file size with a specific url on the website. Detects and synchronizes missing or excess files.
- It adds minecraft mods as a library and launches minecraft.
- In the graphics settings tab, it places the files from the same website url on every launch. The currently selected graphics settings are the exact file in the url.
- You can change the java arguments for initialization in graphic settings. (xmx, xms)
- With the remember me option, you can register your name at the next opening.

- NOTE: All settings launcher is primarily in the file launcher-options.json.
 
- NOTE: Launcher can also update itself. This system is made with the simple version system. If the version text in the json file in the internet urls does not match, the launcher jar file downloads itself from the internet and restarts.

## Change files location
```java
On DownloaderScript.java

public void DownloadOperation() throws IOException, InterruptedException {
        String dirWeb = "https://ottomine.net/ottomine-files/";
```

## Change files database json
```java
On WriterData.java

 public static void GetJsonWeb() throws JSONException, IOException, InterruptedException {
        String JSON_string;
        URL JSONurl = new URL("https://www.ottomine.net/ottomine-files/FilesArray.json");
```
## Media



![alt text](https://i.resmim.net/i/Ekran-Resmi-2021-05-03-01.48.16.png)
![alt text](https://i.resmim.net/i/Ekran-Resmi-2021-05-03-01.48.24.png)
![alt text](https://i.resmim.net/i/Ekran-Resmi-2021-05-03-01.47.55.png)
