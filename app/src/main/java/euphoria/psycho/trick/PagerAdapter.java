package euphoria.psycho.trick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private final List<String> mList;


    @Override
    public int getCount() {

        return mList.size();
    }

    public void switchData(List<String> ls) {

        mList.clear();
        mList.addAll(ls);

    }

    @Override
    public Fragment getItem(int position) {

        ViewFragment viewFragment = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", mList.get(position));
        viewFragment.setArguments(bundle);
        return viewFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position);
    }

    public PagerAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        mList = list;
    }
}
