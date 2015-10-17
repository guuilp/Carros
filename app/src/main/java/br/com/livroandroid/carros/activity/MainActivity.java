package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.prefs.ConfiguracoesActivity;
import br.com.livroandroid.carros.activity.prefs.ConfiguracoesV11Activity;
import br.com.livroandroid.carros.adapter.NavDrawerMenuAdapter;
import br.com.livroandroid.carros.adapter.NavDrawerMenuItem;
import br.com.livroandroid.carros.fragments.AboutDialog;
import br.com.livroandroid.carros.fragments.CarrosTabFragment;
import br.com.livroandroid.carros.fragments.SiteLivroFragment;
import livroandroid.lib.fragment.NavigationDrawerFragment;
import livroandroid.lib.utils.AndroidUtils;

public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavDrawerFragment;
    private NavDrawerMenuAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        // Nav Drawer
        mNavDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);

        // Configura o drawer layout
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        mNavDrawerFragment.setUp(drawerLayout);

        //Cor de fundo da barra de status
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about) {
            AboutDialog.showAbout(getSupportFragmentManager());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public NavigationDrawerFragment.NavDrawerListView getNavDrawerView(NavigationDrawerFragment navDrawerFrag, LayoutInflater layoutInflater, ViewGroup container) {
        View view = layoutInflater.inflate(R.layout.nav_drawer_listview, container, false);
        //Preenche o cabeçalho com a foto, nome e e-mail
        navDrawerFrag.setHeaderValues(view, R.id.listViewContainer, R.drawable.nav_drawer_header, R.drawable.ic_logo_user, R.string.nav_drawer_username, R.string.nav_drawer_email);
        return new NavigationDrawerFragment.NavDrawerListView(view,R.id.listView);
    }

    @Override
    public ListAdapter getNavDrawerListAdapter(NavigationDrawerFragment navigationDrawerFragment) {
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        // Deixa o primeiro item selecionado
        list.get(0).selected = true;
        this.listAdapter = new NavDrawerMenuAdapter(this, list);
        return listAdapter;
    }

    @Override
    public void onNavDrawerItemSelected(NavigationDrawerFragment navigationDrawerFragment, int position) {
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        NavDrawerMenuItem selectedItem = list.get(position);
        //Seleciona a linha
        this.listAdapter.setSelected(position, true);
        if(position == 0){
            replaceFragment(new CarrosTabFragment());
        } else if(position == 1){
            replaceFragment(new SiteLivroFragment());
        } else if(position == 2){
            if(AndroidUtils.isAndroid3Honeycomb()){
                startActivity(new Intent(this, ConfiguracoesV11Activity.class));
            } else {
                startActivity(new Intent(this, ConfiguracoesActivity.class));
            }
        } else {
            Log.e("livroandroid", "Item de menu inválido");
        }
    }

    private void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_drawer_container, frag, "TAG").commit();
    }
}
