package fr.masciulli.drinks.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.DataProvider;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LiquorsFragment extends Fragment implements Callback<List<Liquor>> {
    private static final String TAG = LiquorsFragment.class.getSimpleName();

    private DataProvider provider;
    private Call<List<Liquor>> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new DataProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_liquors, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelPreviousCall();
        call = provider.getLiquors();
        call.enqueue(this);
    }

    private void cancelPreviousCall() {
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public void onResponse(Response<List<Liquor>> response, Retrofit retrofit) {
        Log.d(TAG, response.body().toString());
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
