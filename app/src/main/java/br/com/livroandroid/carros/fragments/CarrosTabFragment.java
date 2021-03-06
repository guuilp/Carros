package br.com.livroandroid.carros.fragments;

import android.app.backup.BackupManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.adapter.TabsAdapter;
import livroandroid.lib.utils.Prefs;

/**
 * Created by Guilherme on 12-Sep-15.
 */
public class CarrosTabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private BackupManager backupManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Gerenciador de backup
        backupManager = new BackupManager(getContext());
        View view = inflater.inflate(R.layout.fragment_carros_tab, container, false);
        //ViewPager
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new TabsAdapter(getContext(), getChildFragmentManager()));
        //Tabs
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        //Cor branca no texto (o fundo azul foi definido no layout)
        tabLayout.setTabTextColors(cor, cor);
        //Adiciona as Tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.classicos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.esportivos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.luxo));
        //Listener para tratar eventos de clique na tab
        tabLayout.setOnTabSelectedListener(this);
        //Se mudar o ViewPager, atualiza a tab selecionada
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        int tabIdx = Prefs.getInteger(getContext(), "tabIdx");
        mViewPager.setCurrentItem(tabIdx);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Se alterar a tab, atualiza o ViewPager
        mViewPager.setCurrentItem(tab.getPosition());
        //Salva o índice da página/tab sleecionada
        Prefs.setInteger(getContext(), "tabIdx", mViewPager.getCurrentItem());
        backupManager.dataChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
