package industries.muskaqueers.thunderechosaber;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class Counsellor {

    private String counsellorID;
    private String firstName;
    private String lastName;
    private String imageURL;
    private String partyAbbreviation;
    private String partyName;
    private String title;
    private String twitterHandle;
    private String constituency;

    public Counsellor() {}

    public Counsellor(String firstName, String lastName, String imageURL, String partyAbbreviation,
                      String title, String partyName, String constituency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.partyAbbreviation = partyAbbreviation;
        this.partyName = partyName;
        this.title = title;
        this.constituency = constituency;
    }

    public String getCounsellorID() { return this.counsellorID; }
    public void setCounsellorID(String counsellorID) {this.counsellorID = counsellorID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Return the MLA's full name
    public String getFullName() { return getFirstName() + " " + getLastName(); }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getPartyAbbreviation() { return partyAbbreviation; }
    public void setPartyAbbreviation(String partyAbbreviation) { this.partyAbbreviation = partyAbbreviation; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTwitterHandle() { return twitterHandle; }
    public void setTwitterHandle(String twitterHandle) { this.twitterHandle = twitterHandle; }

    public String getConstituency() { return constituency; }
    public void setConstituency(String constituency) { this.constituency = constituency;}
}
