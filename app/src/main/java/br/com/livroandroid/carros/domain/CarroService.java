package br.com.livroandroid.carros.domain;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.HttpHelper;

/**
 * Created by Guilherme on 06-Sep-15.
 */
public class CarroService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroService";
    private static final String URL = "http://www.livroandroid.com.br/livro/carros/carros_{tipo}.json";

    public static List<Carro> getCarros(Context context, String tipo, boolean refresh) throws IOException {
        List<Carro> carros = null;
        boolean buscaNoBancoDeDados = !refresh;
        if (buscaNoBancoDeDados) {
            //Busca no banco de dados
            carros = getCarrosFromBanco(context, tipo);
            if(carros != null && carros.size() > 0 ){
                //Encontrou o arquivo
                return carros;
            }
        }
        //Se não encontrar, busca no webservice
        carros = getCarrosFromWebService(context, tipo);
        return carros;
    }

    private static List<Carro> getCarrosFromBanco(Context context, String tipo) throws IOException{
        CarroDB db = new CarroDB(context);
        try{
            List<Carro> carros = db.findAllByTypo(tipo);
            Log.d(TAG, "Retornando " + carros.size() + " carros [" + tipo + "] do banco.");
            return carros;
        } finally {
            db.close();
        }
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    private static List<Carro> getCarrosFromWebService(Context context, String tipo) throws IOException{
        String url = URL.replace("{tipo}", tipo);
        Log.d(TAG, "URL: " + url);
        String json = HttpHelper.doGet(url);
        List<Carro> carros = parserJSON(context, json);
        //Depois de buscar, salva os carros
        salvarCarros(context, tipo, carros);
        return carros;
    }

    private static void salvarCarros(Context context, String tipo, List<Carro> carros) {
        CarroDB db = new CarroDB(context);
        try{
            //Deleta os carros antigos pelo tipo para limpar o banco
            db.deleteCarrosByTipo(tipo);
            //Salva todos os carros
            for(Carro c : carros){
                c.tipo = tipo;
                Log.d(TAG, "Salvando o carro " + c.nome);
                db.save(c);
            }
        } finally {
            db.close();
        }
    }

    private static List<Carro> parserJSON(Context context, String json) throws IOException {
        List<Carro> carros = new ArrayList<Carro>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCarros = obj.getJSONArray("carro");
            // Insere cada carro na lista
            for (int i = 0; i < jsonCarros.length(); i++) {
                JSONObject jsonCarro = jsonCarros.getJSONObject(i);
                Carro c = new Carro();
                // Lê as informações de cada carro
                c.nome = jsonCarro.optString("nome");
                c.desc = jsonCarro.optString("desc");
                c.latitude = jsonCarro.optString("latitude");
                c.longitude = jsonCarro.optString("longitude");
                c.urlFoto = jsonCarro.optString("url_foto");
                c.urlInfo = jsonCarro.optString("url_info");
                if (LOG_ON) {
                    Log.d(TAG, "Carro " + c.nome + " > " + c.urlFoto);
                }
                carros.add(c);
            }
            if (LOG_ON) {
                Log.d(TAG, carros.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return carros;
    }
}