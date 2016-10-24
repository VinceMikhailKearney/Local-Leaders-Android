package industries.muskaqueers.thunderechosaber;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class MLA implements Serializable {

    private String MLA_ID;
    private String firstName;
    private String lastName;
    private String imageURL;
    private Bitmap imageBitmap;
    private String partyAbbreviation;
    private String partyName;
    private String title;
    private String twitterHandle;
    private String emailAddress;
    private String constituency;

    public MLA() {
    }

    public MLA(String firstName, String lastName, String imageURL, String partyAbbreviation,
               String title, String partyName, String constituency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.partyAbbreviation = partyAbbreviation;
        this.partyName = partyName;
        this.title = title;
        this.constituency = constituency;
    }

    public String getMLA_ID() {
        return this.MLA_ID;
    }

    public void setMLA_ID(String MLA_ID) {
        this.MLA_ID = MLA_ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Return the MLA's full name
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getPartyAbbreviation() {
        return partyAbbreviation;
    }

    public void setPartyAbbreviation(String partyAbbreviation) {
        this.partyAbbreviation = partyAbbreviation;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MLA_ID" + getMLA_ID() + "\n");
        stringBuilder.append("firstName" + getFirstName() + "\n");
        stringBuilder.append("lastName" + getLastName() + "\n");
        stringBuilder.append("imageURL" + getImageURL() + "\n");
        stringBuilder.append("partyAbbreviation" + getPartyAbbreviation() + "\n");
        stringBuilder.append("partyName" + getPartyName() + "\n");
        stringBuilder.append("title" + getTitle() + "\n");
        stringBuilder.append("twitterHandle" + getTwitterHandle() + "\n");
        stringBuilder.append("emailAddress" + getEmailAddress() + "\n");
        stringBuilder.append("constituency" + getConstituency() + "\n");
        return stringBuilder.toString();
    }
}
