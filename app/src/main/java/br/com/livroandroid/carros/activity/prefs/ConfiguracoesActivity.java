package br.com.livroandroid.carros.activity.prefs;

import android.os.Bundle;

import br.com.livroandroid.carros.R;

/**
 * Created by Guilherme on 17-Oct-15.
 */
@SuppressWarnings("deprecation")
public class ConfiguracoesActivity extends android.preference.PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Carrega as configuracoes
        addPreferencesFromResource(R.xml.preferences);
    }
}
