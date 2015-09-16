package br.com.livroandroid.carros.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.CarroActivity;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;

public class CarrosFragment extends BaseFragment{
    protected RecyclerView recyclerView;
    private List<Carro> carros;
    private LinearLayoutManager mLayoutManager;
    private String tipo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        return view;
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
        taskCarros();
    }

    private void taskCarros() {
        //Busca os carros: dispara a task
        new GetCarrosTask().execute();
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

    private class GetCarrosTask extends AsyncTask<Void, Void, List<Carro>> {
        @Override
        protected List<Carro> doInBackground(Void... params) {
            try{
                //Busca os carros em background (Thread)
                return CarroService.getCarros(getContext(), tipo);
            } catch(IOException e) {
                Log.e("livroandroid", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Carro> carros) {
            if(carros != null){
                CarrosFragment.this.carros = carros;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }
    }
}