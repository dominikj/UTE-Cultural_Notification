package pl.ute.culturaltip.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.ute.culturaltip.R;

import static android.R.layout.simple_list_item_1;

public class DefaultListFragment extends ListFragment implements OnItemClickListener {
    private int currentSelectedPosition = -1;
    public static final int NONE_SELECTED = -1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setItemsList(List<String> items) {
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), simple_list_item_1);
        adapter.addAll(items);
        setListAdapter(adapter);
        currentSelectedPosition = -1;
        getListView().setOnItemClickListener(this);
    }

    public void addItemToList(String item) {
        ((ArrayAdapter) getListAdapter()).add(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentSelectedPosition = position;
    }

    public int getSelectedPosition() {
        return this.currentSelectedPosition;
    }

    public void setListName(String name) {
        ((TextView) getActivity().findViewById(R.id.list_title)).setText(name);
    }
}