package com.plagh.ggxx.helloworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.plagh.ggxx.helloworld.call.CallRecord;
import com.plagh.ggxx.helloworld.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements CallItemFragment.OnListFragmentInteractionListener, CallRecordFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fm;
    private Fragment currentFragment;
    private String currentTag = "";
    private final String TAG_DUMMY = "TAG_DUMMY";
    private final String TAG_CALL = "TAG_CALL";
    private final String TAG_ABOUT = "TAG_ABOUT";
    private final String TAG_KEY = "TAG_KEY";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    addOrShowFragment(TAG_DUMMY);
                    return true;
                case R.id.navigation_dashboard:
                    addOrShowFragment(TAG_CALL);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            String tag = savedInstanceState.getString(TAG_KEY);
            if (tag != null) {
                currentFragment = fm.findFragmentByTag(tag);
                addOrShowFragment(tag);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void addOrShowFragment(String tag) {
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment f = fm.findFragmentByTag(tag);
        if (f == null) {
            f = createFragment(tag);
        }
        if (f.isAdded()) {
            transaction.hide(currentFragment);
            transaction.show(f);
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.content, f, tag);
            transaction.show(f);
        }
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
        currentFragment = f;
        currentTag = tag;
    }

    private Fragment createFragment(String tag) {
        switch (tag) {
            case TAG_DUMMY:
                return new CallItemFragment();
            case TAG_CALL:
                return new CallRecordFragment();
            case TAG_ABOUT:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        //Toast toast = Toast.makeText(this, item.content, Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Override
    public void onListFragmentInteraction(CallRecord item) {
        //Toast toast = Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_KEY, currentTag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
    }
}
