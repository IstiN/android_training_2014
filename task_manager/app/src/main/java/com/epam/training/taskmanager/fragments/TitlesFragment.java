package com.epam.training.taskmanager.fragments;

/**
 * Created by IstiN on 12.11.2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.epam.training.taskmanager.R;
import com.epam.training.taskmanager.Shakespeare;

/**
 * This is the "top-level" fragment, showing a list of items that the
 * user can pick.  Upon picking an item, it takes care of displaying the
 * data to the user as appropriate based on the currrent UI layout.
 */

public class TitlesFragment extends ListFragment {

    public static <T> T findFirstResponderFor(Fragment fragment, Class<T> clazz) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null)
            return null;
        if (clazz.isInstance(activity)) {
            return clazz.cast(activity);
        }
        Fragment parentFragment = fragment.getParentFragment();
        while (parentFragment != null) {
            if (clazz.isInstance(parentFragment)) {
                return clazz.cast(parentFragment);
            }
            parentFragment = parentFragment.getParentFragment();
        }
        return null;
    }

    public static interface Callbacks {

        void onShowDetails(int index);

        boolean isDualPane();

    }

    int mCurCheckPosition = 0;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.simple_list_item_checkable_1,
                android.R.id.text1, Shakespeare.TITLES));

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (getCallbacks().isDualPane()) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;
        Callbacks callbacks = getCallbacks();
        if (callbacks.isDualPane()) {
            getListView().setItemChecked(index, true);
        }
        callbacks.onShowDetails(index);

    }

    private Callbacks getCallbacks() {
        return findFirstResponderFor(this, Callbacks.class);
    }
}