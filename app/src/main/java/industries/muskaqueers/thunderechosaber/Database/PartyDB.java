package industries.muskaqueers.thunderechosaber.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by andrewcunningham on 11/8/16.
 */

@Entity(indexes = {
        @Index(value = "ID, " +
                "partyId, " +
                "name, " +
                "twitterHandle, " +
                "imageURL, " +
                "imageBitmap", unique = true)
})
public class PartyDB {

    @Id
    private Long ID;

    @NotNull
    private String partyId;
    private String name;
    private String twitterHandle;
    private String imageURL;
    private byte[] imageBitmap;

    @Generated(hash = 940636813)
    public PartyDB(Long ID, @NotNull String partyId, String name,
                   String twitterHandle, String imageURL, byte[] imageBitmap) {
        this.ID = ID;
        this.partyId = partyId;
        this.name = name;
        this.twitterHandle = twitterHandle;
        this.imageURL = imageURL;
        this.imageBitmap = imageBitmap;
    }

    @Generated(hash = 783271331)
    public PartyDB() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getPartyId() {
        return this.partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTwitterHandle() {
        return this.twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public byte[] getImageBitmap() {
        return this.imageBitmap;
    }

    public void setImageBitmap(byte[] imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

}
