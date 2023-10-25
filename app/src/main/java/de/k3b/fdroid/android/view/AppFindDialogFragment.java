package de.k3b.fdroid.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import de.k3b.fdroid.android.R;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.util.AndroidVersionName;

public class AppFindDialogFragment extends DialogFragment {
    private static final java.lang.String INSTANCE_STATE_SEARCH_PARAMETER = "searchParameter";

    private AppSearchParameter mAppSearchParameter;
    private EditText edSearch;
    private Spinner cbVersion;

    /****** live cycle ********/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mAppSearchParameter = (AppSearchParameter) savedInstanceState.getSerializable(INSTANCE_STATE_SEARCH_PARAMETER);
        }

        View view = inflater.inflate(R.layout.dlg_app_find, container);
        edSearch = view.findViewById(R.id.edSearch);
        cbVersion = view.findViewById(R.id.cbVersion);
        ArrayAdapter<AndroidVersionName.Item> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item,
                AndroidVersionName.getMap().values().toArray(new AndroidVersionName.Item[0]));
        cbVersion.setAdapter(adapter);

        edSearch.setText(mAppSearchParameter.searchText);
        cbVersion.setSelection(mAppSearchParameter.versionSdk);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAppSearchParameter != null) {
            outState.putSerializable(INSTANCE_STATE_SEARCH_PARAMETER, mAppSearchParameter);
        }
    }

}
