import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class mumble {
    public static void StartMumble(String name,String ip) throws IOException, InterruptedException, JSONException {
        if (OsFinder.isWindows()){
            String filepath= "Mumble\\otto_mumble.reg\"";
            String command = "reg import "+filepath;
            final ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c",command);
            builder.start();
            Thread.sleep(100);
            String key = LauncherController.GetLauncherOptions().getString("pushtotalk");
            writeKeyReg(mumbleKeyHex(key),1,1);
            String key2 = LauncherController.GetLauncherOptions().getString("mute");
            writeKeyReg(mumbleKeyHex(key2),3,2);
            System.out.println("Tuşlar yazıldı");
            final ProcessBuilder builder2 = new ProcessBuilder("cmd.exe","/c","Mumble\\mumble.exe mumble://"+name+"@"+ip+"/?version=1.2.0");
            builder2.start();
            System.out.println("mumble başlatıldı");
        }else{
            if (OsFinder.isMac()){
                //todo mac için mumble registery
            }
        }
    }
    public static void writeKeyReg(String code, int data, int key) throws IOException, InterruptedException {
        String datastring = "";
        if (data == 1){
            datastring = "\"data\"=\"@Invalid()\"";
        }else{
            if(data == 3){
                datastring = "\"index\"=dword:00000000\n";
            }
        }
        String regtext = "Windows Registry Editor Version 5.00\n" +
                "[HKEY_CURRENT_USER\\SOFTWARE\\Mumble\\Mumble\\shortcuts\\"+key+"]\n" +
                "\"index\"=dword:0000000"+data+"\n" +
                "\"keys\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,00,00,00,00,\\\n" +
                "  00,00,09,00,00,00,00,00,00,00,01,00,00,00,00,00,00,00,09,00,00,00,00,00,00,\\\n" +
                "  00,02,00,00,00,00,00,00,00,03,00,00,00,00,00,"+code+",00,04,00,00,00,00,00,00,00,\\\n" +
                "  0a,00,00,00,00,00,00,00,4c,00,00,00,7b,00,00,00,36,00,00,00,66,00,00,00,31,\\\n" +
                "  00,00,00,64,00,00,00,32,00,00,00,62,00,00,00,36,00,00,00,31,00,00,00,2d,00,\\\n" +
                "  00,00,64,00,00,00,35,00,00,00,61,00,00,00,30,00,00,00,2d,00,00,00,31,00,00,\\\n" +
                "  00,31,00,00,00,63,00,00,00,66,00,00,00,2d,00,00,00,62,00,00,00,66,00,00,00,\\\n" +
                "  63,00,00,00,37,00,00,00,2d,00,00,00,34,00,00,00,34,00,00,00,34,00,00,00,35,\\\n" +
                "  00,00,00,35,00,00,00,33,00,00,00,35,00,00,00,34,00,00,00,30,00,00,00,30,00,\\\n" +
                "  00,00,30,00,00,00,30,00,00,00,7d,00,29,00\n" +
                "\"suppress\"=\"false\"\n" +
                datastring;
        String pushtotalkReg = "Windows Registry Editor Version 5.00\n" +
                "\n" +
                "[HKEY_CURRENT_USER\\SOFTWARE\\Mumble\\Mumble\\audio]\n" +
                "\"input\"=\"WASAPI\"\n" +
                "\"output\"=\"WASAPI\"\n" +
                "\"ptthold\"=hex(b):0a,00,00,00,00,00,00,00\n" +
                "\"vadmin\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,00,00,00,00,\\\n" +
                "  00,00,87,00,3e,00,8f,00,45,00,1f,00,29,00\n" +
                "\"vadmax\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,00,00,00,00,\\\n" +
                "  00,00,87,00,3f,00,26,00,d3,00,4e,00,29,00\n" +
                "\"maxdistancevolume\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,\\\n" +
                "  00,00,00,00,00,00,87,00,00,00,00,00,00,00,00,00,29,00\n" +
                "\"echomulti\"=\"false\"\n" +
                "\"postransmit\"=\"true\"\n" +
                "\"maxdistance\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,00,00,\\\n" +
                "  00,00,00,00,87,00,41,00,a0,00,cc,00,cd,00,29,00\n" +
                "\"attenuateothers\"=\"true\"\n" +
                "\"attenuateothersontalk\"=\"true\"\n" +
                "\"headphone\"=\"true\"\n" +
                "\"vadsource\"=dword:00000001\n" +
                "\"bloom\"=hex:40,00,56,00,61,00,72,00,69,00,61,00,6e,00,74,00,28,00,00,00,00,00,\\\n" +
                "  00,00,87,00,00,00,00,00,00,00,00,00,29,00\n" +
                "\"quality\"=dword:00011185\n" +
                "\"loudness\"=dword:0000258d\n" +
                "\"noisesupress\"=dword:ffffffc4\n" +
                "\"denoise\"=\"true\"\n" +
                "\"voicehold\"=dword:00000026\n" +
                "\"outputdelay\"=dword:00000004\n" +
                "\"transmit\"=dword:00000002\n" +
                "\n";
        if (data == 1 && !code.equals("")){
            FileWriter writer = new FileWriter("pushtotalk_active.reg");
            writer.write(pushtotalkReg);
            writer.close();
            String filepath= "pushtotalk_active.reg";
            String command = "reg import "+filepath;
            final ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c",command);
            builder.start();
            File myObj = new File("pushtotalk_active.reg");
            /**if (myObj.delete()) {
                System.out.println("Dosya silindi: " + myObj.getName());
            } else {
                System.out.println("Dosya silinirken hata oluştu." + myObj.getName());
            }*/
        }
        FileWriter writer = new FileWriter("key_"+data+".reg");
        writer.write(regtext);
        writer.close();
        System.out.println(regtext);
        String filepath= "key_"+data+".reg";
        String command = "reg import "+filepath;
        final ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c",command);
        builder.start();
        Thread.sleep(200);
        File myObj = new File("key_"+data+".reg");
        if (myObj.delete()) {
            System.out.println("Dosya silindi: " + myObj.getName());
        } else {
            System.out.println("Dosya silinirken hata oluştu." + myObj.getName());
        }
    }

    public static String mumbleKeyHex(String keycode){
        String hexcode = "";
        switch(keycode){
            case "F1":
                hexcode = "3B";
                break;
            case "F2":
                hexcode = "3C";
                break;
            case "F3":
                hexcode = "3D";
                break;
            case "F4":
                hexcode = "3E";
                break;
            case "F5":
                hexcode = "3F";
                break;
            case "F6":
                hexcode = "40";
                break;
            case "F7":
                hexcode = "41";
                break;
            case "F8":
                hexcode = "42";
                break;
            case "F9":
                hexcode = "43";
                break;
            case "F10":
                hexcode = "44";
                break;
            case "F11":
                hexcode = "57";
                break;
            case "F12":
                hexcode = "58";
                break;
            case "PRINTSCREEN":
                hexcode = "B7";
                break;
            case "SCROLL_LOCK":
                hexcode = "46";
                break;
            case "DIGIT1":
                hexcode = "33";
                break;
            case "DIGIT2":
                hexcode = "03";
                break;
            case "DIGIT3":
                hexcode = "04";
                break;
            case "DIGIT4":
                hexcode = "05";
                break;
            case "DIGIT5":
                hexcode = "06";
                break;
            case "DIGIT6":
                hexcode = "07";
                break;
            case "DIGIT7":
                hexcode = "08";
                break;
            case "DIGIT8":
                hexcode = "09";
                break;
            case "DIGIT9":
                hexcode = "0A";
                break;
            case "DIGIT0":
                hexcode = "0B";
                break;
            case "BACK_SPACE":
                hexcode = "0E";
                break;
            case "INSERT":
                hexcode = "D2";
                break;
            case "HOME":
                hexcode = "C7";
                break;
            case "PAGE_UP":
                hexcode = "C9";
                break;
            case "TAB":
                hexcode = "0F";
                break;
            case "Q":
                hexcode = "10";
                break;
            case "W":
                hexcode = "11";
                break;
            case "E":
                hexcode = "12";
                break;
            case "R":
                hexcode = "13";
                break;
            case "T":
                hexcode = "14";
                break;
            case "Y":
                hexcode = "15";
                break;
            case "U":
                hexcode = "16";
                break;
            case "I":
                hexcode = "17";
                break;
            case "O":
                hexcode = "18";
                break;
            case "P":
                hexcode = "19";
                break;
            case "OPEN_BRACKET": //ğ
                hexcode = "1A";
                break;
            case "CLOSE_BRACKET":
                hexcode = "1B";
                break;
            case "DELETE":
                hexcode = "D3";
                break;
            case "END":
                hexcode = "CF";
                break;
            case "PAGE_DOWN":
                hexcode = "D1";
                break;
            case "PLUS":
                hexcode = "4E";
                break;
            case "CAPS":
                hexcode = "3A";
                break;
            case "A":
                hexcode = "1E";
                break;
            case "S":
                hexcode = "1F";
                break;
            case "D":
                hexcode = "20";
                break;
            case "F":
                hexcode = "21";
                break;
            case "G":
                hexcode = "22";
                break;
            case "H":
                hexcode = "23";
                break;
            case "J":
                hexcode = "24";
                break;
            case "K":
                hexcode = "25";
                break;
            case "L":
                hexcode = "26";
                break;
            case "SEMICOLON":
                hexcode = "27";
                break;
            case "QUOTE":
                hexcode = "28";
                break;
            case "BACK_SLASH":
                hexcode = "2B";
                break;
            case "SHIFT":
                hexcode = "2A";
                break;
            case "BACK_QUOTE":
                hexcode = "56";
                break;
            case "ENTER":
                hexcode = "1C";
                break;
            case "Z":
                hexcode = "2C";
                break;
            case "X":
                hexcode = "2D";
                break;
            case "C":
                hexcode = "2E";
                break;
            case "V":
                hexcode = "2F";
                break;
            case "B":
                hexcode = "30";
                break;
            case "N":
                hexcode = "31";
                break;
            case "M":
                hexcode = "32";
                break;
            case "COMMA":
                hexcode = "33";
                break;
            case "PERIOD":
                hexcode = "34";
                break;
            case "SLASH":
                hexcode = "35";
                break;
            case "CONTROL":
                hexcode = "1D";
                break;
            case "ALT":
                hexcode = "38";
                break;
            case "SPACE":
                hexcode = "39";
                break;
            case "ALT_GRAPH":
                hexcode = "1D";
                break;
            case "LEFT":
                hexcode = "CB";
                break;
            case "DOWN":
                hexcode = "D0";
                break;
            case "UP":
                hexcode = "C8";
                break;
            case "RIGHT":
                hexcode = "CD";
                break;
        }
        return hexcode;
    }
}
