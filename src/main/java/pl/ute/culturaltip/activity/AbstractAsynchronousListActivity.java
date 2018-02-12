package pl.ute.culturaltip.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import java.util.List;

import pl.ute.culturaltip.fragment.DefaultListFragment;

/**
 * Created by dominik on 11.02.18.
 */

public abstract class AbstractAsynchronousListActivity extends AbstractNavigationActivity {

    private DefaultListFragment listFragment;
    private BroadcastReceiver receiverActivity;
    private List ListElements;


    public AbstractAsynchronousListActivity(int forwardButtonId, int backButtonId) {
        super(forwardButtonId, backButtonId);

    }

    public void setIntentReceiver(BroadcastReceiver receiver, String intentCode) {
        receiverActivity = receiver;
        IntentFilter filter = new IntentFilter(intentCode);
        registerReceiver(receiverActivity, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverActivity);

    }

    protected void onResume(boolean forwardButtonIsEnabled, boolean backButtonIsEnabled) {
        super.onResume(forwardButtonIsEnabled, backButtonIsEnabled);
    }

    protected DefaultListFragment getListFragment() {
        return listFragment;
    }

    protected void setListFragment(DefaultListFragment listFragment) {
        this.listFragment = listFragment;
    }

    public List getListElements() {
        return ListElements;
    }

    public void setListElements(List listElements) {
        ListElements = listElements;
    }
}
