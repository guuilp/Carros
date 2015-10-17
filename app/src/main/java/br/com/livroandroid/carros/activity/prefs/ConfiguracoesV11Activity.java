package br.com.livroandroid.carros.activity.prefs;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import br.com.livroandroid.carros.R;

/**
 * Created by Guilherme on 17-Oct-15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ConfiguracoesV11Activity extends android.app.Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Adiciona o fragment de configurações
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, new PrefsFragment());
        ft.commit();
    }

    private class PrefsFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Carrega as configurações
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
