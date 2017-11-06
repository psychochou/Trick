package euphoria.psycho.trick;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int ID_ADD_CAT = 235;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private void addCat() {

        final EditText editText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String tabName = editText.getText().toString().trim();
                        if (tabName.length() > 0)
                            Databases.getInstance().addTab(tabName);
                        List<String> tabList = Databases.getInstance().getTabList();
                        mPagerAdapter.switchData(tabList);
                        final ActionBar actionBar = getActionBar();
                        actionBar.removeAllTabs();
                        for (int ix = 0; ix < tabList.size(); ix++) {
                            actionBar.addTab(actionBar.newTab().setText(mPagerAdapter.getPageTitle(ix)).setTabListener(MainActivity.this));
                        }


                    }
                });

        builder.show();
    }

    private void initialize() {

        setContentView(R.layout.activity_main);

        Databases.newInstance(this, SharedUtils.getExternalStorageDirectoryFile("trick.dat").getAbsolutePath());
        mViewPager = findViewById(R.id.pager);
        List<String> tabList = Databases.getInstance().getTabList();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabList);
        mViewPager.setAdapter(mPagerAdapter);
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0; i < tabList.size(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void refreshActionbar() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ID_ADD_CAT, 0, "添加分类");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ID_ADD_CAT:
                addCat();
                return true;

        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initialize();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {


        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedUtils.setContext(this);

        int r = SharedUtils.requestPermissions(this);
        if (r == 0) {
            initialize();
        }
    }
}
