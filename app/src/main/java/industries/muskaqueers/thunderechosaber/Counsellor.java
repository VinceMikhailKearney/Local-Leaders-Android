package industries.muskaqueers.thunderechosaber;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class Counsellor
{
    private String name;
    private String age;
    private String hero;

    public Counsellor() {}

    public Counsellor(String name, String age, String hero)
    {
        this.name = name;
        this.age = age;
        this.hero = hero;
    }

    public String getName() { return this.name; }
    public String getAge() { return this.age; }
    public String getHero() { return this.hero; }
}
