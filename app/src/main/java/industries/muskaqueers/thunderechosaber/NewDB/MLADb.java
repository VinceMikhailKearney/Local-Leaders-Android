package industries.muskaqueers.thunderechosaber.NewDB;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by andrewcunningham on 11/8/16.
 */

@Entity(indexes = {
        @Index(value =
                "ID, " +
                        "firstName, " +
                        "lastName, " +
                        "imageURL, " +
                        "imageBitmap, " +
                        "partyAbbreviation, " +
                        "partyName, " +
                        "title, " +
                        "twitterHandle, " +
                        "emailAddress, " +
                        "constituency", unique = true)
})
public class MLADb {

    @Id
    private Long ID;

    @NotNull
    private String MLA_ID;
    private String firstName;
    private String lastName;
    private String imageURL;
    private byte[] imageBitmap;
    private String partyAbbreviation;
    private String partyName;
    private String title;
    private String twitterHandle;
    private String emailAddress;
    private String constituency;

    @Generated(hash = 63553626)
    public MLADb(Long ID, @NotNull String MLA_ID, String firstName, String lastName,
                 String imageURL, byte[] imageBitmap, String partyAbbreviation,
                 String partyName, String title, String twitterHandle,
                 String emailAddress, String constituency) {
        this.ID = ID;
        this.MLA_ID = MLA_ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.imageBitmap = imageBitmap;
        this.partyAbbreviation = partyAbbreviation;
        this.partyName = partyName;
        this.title = title;
        this.twitterHandle = twitterHandle;
        this.emailAddress = emailAddress;
        this.constituency = constituency;
    }

    @Generated(hash = 752671726)
    public MLADb() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getMLA_ID() {
        return this.MLA_ID;
    }

    public void setMLA_ID(String MLA_ID) {
        this.MLA_ID = MLA_ID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPartyAbbreviation() {
        return this.partyAbbreviation;
    }

    public void setPartyAbbreviation(String partyAbbreviation) {
        this.partyAbbreviation = partyAbbreviation;
    }

    public String getPartyName() {
        return this.partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTwitterHandle() {
        return this.twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getConstituency() {
        return this.constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

}
