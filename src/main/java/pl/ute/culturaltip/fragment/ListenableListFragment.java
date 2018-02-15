package pl.ute.culturaltip.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.activity.AsynchronousListListener;

/**
 * Created by dominik on 13.02.18.
 */
public class ListenableListFragment extends DefaultListFragment {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        ((AsynchronousListListener) getActivity()).onSelectItem(position);
        ((TextView) getActivity().findViewById(R.id.list_title)).setError(null);

    }

    @Override
    public void setItemsList(List<String> items) {
        super.setItemsList(items);
        ((TextView) getActivity().findViewById(R.id.list_title)).setError(null);
    }
}
