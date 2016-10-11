package industries.muskaqueers.thunderechosaber;

import android.graphics.Bitmap;

/**
 * Created by vincekearney on 06/10/2016.
 */

public class Party {

    private String partyId;
    private String name;
    private String twitterHandle;
    private String imageURL;
    private Bitmap imageBitmap;


    public Party() {}

    public Party(String id, String name, String twitterHandle, String url) {
        this.partyId = id;
        this.name = name;
        this.twitterHandle = twitterHandle;
        this.imageURL = url;
    }

    public String getPartyId() { return partyId; }
    public void setPartyId(String partyId) { this.partyId = partyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTwitterHandle() { return twitterHandle; }
    public void setTwitterHandle(String twitterHandle) { this.twitterHandle = twitterHandle; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public Bitmap getImageBitmap() { return imageBitmap; }
    public void setImageBitmap(Bitmap imageBitmap) { this.imageBitmap = imageBitmap; }
}
