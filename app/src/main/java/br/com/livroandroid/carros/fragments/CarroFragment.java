package br.com.livroandroid.carros.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

/**
 * Created by Guilherme on 12-Sep-15.
 */
public class CarroFragment extends BaseFragment {
    private Carro carro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carro, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    //Método público chamado pela activity para atualizar os dados do carro
    public void setCarro(Carro carro){
        if(carro != null) {
            this.carro = carro;
            setTextString(R.id.tDesc, carro.desc);
            final ImageView imgView = (ImageView) getView().findViewById(R.id.img);
            Picasso.with(getContext()).load(carro.urlFoto).fit().into(imgView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_carro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_edit) {
            EditarCarroDialog.show(getFragmentManager(), carro, new EditarCarroDialog.Callback() {
                @Override
                public void onCarroUpdated(Carro carro) {
                    toast("Carro [" + carro.nome + "] atualizado.");
                    CarrosApplication.getInstance().setPrecisaAtualizar(carro.tipo, true);
                    //Atualiza o título com o novo nome
                    getActionBar().setTitle(carro.nome);
                }
            });
            return true;
        } else if(item.getItemId() == R.id.action_remove) {
            DeletarCarroDialog.show(getFragmentManager(), carro, new DeletarCarroDialog.Callback() {
                @Override
                public void onCarroDeleted(Carro carro) {
                    toast("Carro [" + carro.nome + "] deletado.");
                    CarrosApplication.getInstance().setPrecisaAtualizar(carro.tipo, true);
                    //Fecha a activity
                    getActivity().finish();
                }
            });
            return true;
        } else if(item.getItemId() == R.id.action_share) {
            toast("Compartilhar");
        } else if(item.getItemId() == R.id.action_maps) {
            toast("Mapa");
        } else if(item.getItemId() == R.id.action_video) {
            toast("Vídeo");
        }
        return super.onOptionsItemSelected(item);
    }
}
