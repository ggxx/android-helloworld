package com.plagh.ggxx.helloworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.plagh.ggxx.helloworld.call.CallRecord;
import com.plagh.ggxx.helloworld.dummy.DummyContent;
import com.plagh.ggxx.helloworld.util.PermissionHelper;

public class MainActivity extends AppCompatActivity implements CallItemFragment.OnListFragmentInteractionListener, CallRecordFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fm;
    private Fragment currentFragment;
    private String currentTag = "";
    private final String TAG_DUMMY = "TAG_DUMMY";
    private final String TAG_CALL = "TAG_CALL";
    private final String TAG_ABOUT = "TAG_ABOUT";
    private final String TAG_KEY = "TAG_KEY";

    //返回值
    private static final int CALL_RESULT_CODE = 12;
    //权限名称
    private static final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;

    //权限检测类
    private PermissionHelper mPermissionHelper;

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

        PermissionHelper.getPermissions(this);
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();

        //判断权限授权状态
        boolean b = mPermissionHelper.checkPermission(CALL_PERMISSION);

        //如果没有获取到权限,则尝试获取权限
        if (!b) {
            mPermissionHelper.permissionsCheck(CALL_PERMISSION, CALL_RESULT_CODE);
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CALL_RESULT_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    //如果请求失败
                    //mPermissionHelper.startAppSettings();
                }
                break;
        }
    }
    */

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

        Snackbar.make(this.getCurrentFocus(), item.getName(), Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
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
