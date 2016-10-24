package industries.muskaqueers.thunderechosaber.Utils;

import java.io.IOException;

import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;

/**
 * Created by vincekearney on 24/10/2016.
 */

public class TwitterThread extends Thread {

    private String userName;

    public TwitterThread(String user) {
        this.userName = user;
    }

    public void run() {
        try {
            TwitterManager.getTweetsForUser(this.userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
