package industries.muskaqueers.thunderechosaber;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SOCIAL_TAB = 0;
    private static final int MLA_TAB = 1;
    private static final int AREA_TAB = 2;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager fragmentPager;

    // ---------- Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fragmentPager = (ViewPager) findViewById(R.id.fragment_pager);

        setSupportActionBar(toolbar);
        setupPager();
        setupTabLayout();

    }

    // ---------- Fragment Pager Methods
    protected static class MainAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments = {new SocialFragment(), new MLAFragment(), new AreaFragment()};
        protected Context context;

        public MainAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

    }

    protected void setupPager() {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), getApplicationContext());
        fragmentPager.setAdapter(mainAdapter);
        fragmentPager.setCurrentItem(MLA_TAB);
        if (tabLayout.getTabCount() > 0) {
            updateTabTitle(tabLayout.getTabAt(fragmentPager.getCurrentItem()));
        }

    }

    public void setupTabLayout() {
        tabLayout.setupWithViewPager(fragmentPager);

        int[] imageResIds = new int[]{R.drawable.newsicon, R.drawable.councilloricon, R.drawable.mapicon};
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(imageResIds[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabTitle(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        updateTabTitle(tabLayout.getTabAt(fragmentPager.getCurrentItem()));
    }

    protected void updateTabTitle(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0: {
                getSupportActionBar().setTitle("Social");
                fragmentPager.setCurrentItem(tab.getPosition());
                break;
            }
            case 1: {
                getSupportActionBar().setTitle("MLAs");
                fragmentPager.setCurrentItem(tab.getPosition());
                break;
            }
            case 2: {
                getSupportActionBar().setTitle("My Area");
                fragmentPager.setCurrentItem(tab.getPosition());
                break;
            }
            default:
                break;
        }
    }

}
