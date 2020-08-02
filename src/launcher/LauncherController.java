package launcher;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

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
    public void launchGame() {

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


