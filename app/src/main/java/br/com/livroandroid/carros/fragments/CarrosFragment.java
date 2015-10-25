package br.com.livroandroid.carros.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.CarroActivity;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
import livroandroid.lib.utils.AndroidUtils;

public class CarrosFragment extends BaseFragment{
    protected RecyclerView recyclerView;
    private List<Carro> carros;
    private LinearLayoutManager mLayoutManager;
    private String tipo;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //Swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener());
        swipeRefreshLayout.setColorSchemeColors(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Valida se existe conexão ao fazer o gesto Pull to Refresh
                if(AndroidUtils.isNetworkAvailable(getContext())){
                    //Atualiza ao fazer o gesto Pull to Refresh
                    taskCarros(true);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    alert(R.string.error_conexao_indisponivel);
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.tipo = getArguments().getString("tipo");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        taskCarros(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (CarrosApplication.getInstance().isPrecisaAtualizar(this.tipo)) {
            // Faz a leitura novamente do banco de dados
            taskCarros(false);
            toast("Lista de carros atualizada!");
        }
    }

    private void taskCarros(boolean pullToRefresh) {
        //Busca os carros: dispara a task
        startTask("carros", new GetCarrosTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }

    private CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener() {
            @Override
            public void onClickCarro(View view, int idx) {
                Carro c = carros.get(idx);
                Intent intent = new Intent(getContext(), CarroActivity.class);
                intent.putExtra("carro", c);
                startActivity(intent);
                //Toast.makeText(getContext(), "Carro: " + c.nome, Toast.LENGTH_LONG).show();
            }
        };
    }

    private class GetCarrosTask implements TaskListener<List<Carro>> {

        private boolean refresh;
        public GetCarrosTask(boolean refresh){
            this.refresh = refresh;
        }

        @Override
        public List<Carro> execute() throws Exception {
            //Busca os carros em background (Thread)
            return CarroService.getCarros(getContext(), tipo, refresh);
        }

        @Override
        public void updateView(List<Carro> carros) {
            if (carros != null) {
                //Salva a lista de carros no atributo da classe
                CarrosFragment.this.carros = carros;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }

        @Override
        public void onError(Exception e) {
            //Qualquer exceção lançada no método execute, vai cair aqui.
            alert("Ocorreu algum erro ao buscar os dados.");
        }

        @Override
        public void onCancelled(String s) {

        }
    }
}