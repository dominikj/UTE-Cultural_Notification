package pl.ute.culturaltip.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.example.dominik.ute.R;

import java.util.List;

import static android.R.layout.simple_list_item_1;

public class DefaultListFragment extends ListFragment implements OnItemClickListener {
    private int currentSelectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setFriendsList(List<String> items) {
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), simple_list_item_1);
        adapter.addAll(items);
        setListAdapter(adapter);
        currentSelectedPosition = -1;
        getListView().setOnItemClickListener(this);
    }

    public void addFriendToList(String item) {
        ((ArrayAdapter) getListAdapter()).add(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentSelectedPosition = position;
    }

    public int getSelectedPosition() {
        return this.currentSelectedPosition;
    }
}