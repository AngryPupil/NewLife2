package net.pupil.newlife.crimialintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
