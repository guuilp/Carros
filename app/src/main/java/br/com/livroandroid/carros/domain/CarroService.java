package br.com.livroandroid.carros.domain;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.R;
import livroandroid.lib.utils.FileUtils;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.IOUtils;

/**
 * Created by Guilherme on 06-Sep-15.
 */
public class CarroService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroService";
    private static final String URL = "http://www.livroandroid.com.br/livro/carros/carros_{tipo}.json";

    public static List<Carro> getCarros(Context context, String tipo) throws IOException {
        List<Carro> carros = getCarrosFromArquivo(context, tipo);
        if(carros != null && carros.size() > 0 ){
            //Encontrou o arquivo
            return carros;
        }
        //Se não encontrar, busca no webservice
        carros = getCarrosFromWebService(context, tipo);
        return carros;
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    private static List<Carro> getCarrosFromWebService(Context context, String tipo) throws IOException{
        String url = URL.replace("{tipo}", tipo);
        Log.d(TAG, "URL: " + url);
        String json = HttpHelper.doGet(url);
        salvaArquivoNaMemoriaInterna(context, url, json);
        List<Carro> carros = parserJSON(context, json);
        return carros;
    }

    //Faz a requisição HTTP, cria a lista de carros e salva o JSON em arquivo
    private static List<Carro> getCarrosFromArquivo(Context context, String tipo) throws IOException {
        String fileName = String.format("carros_%s.json", tipo);
        Log.d(TAG, "Abrindo arquivo: " + fileName);
        //Lê o arquivo da memória interna
        String json = FileUtils.readFile(context, fileName, "UTF-8");
        if(json == null){
            Log.d(TAG, "Arquivo " +fileName+ "não encontrado.");
            return null;
        }
        List<Carro> carros = parserJSON(context, json);
        Log.d(TAG, "Carros lidos do arquivo " +fileName+".");
        return carros;
    }

    public static void salvaArquivoNaMemoriaInterna(Context context, String url, String json){
        String fileName = url.substring(url.lastIndexOf("/")+1);
        File file = FileUtils.getFile(context, fileName);
        IOUtils.writeString(file, json);
        Log.d(TAG, "Arquivo salvo: " + file);
    }

    private static String readFileFromTipo(Context context, String tipo) throws IOException {
        if ("classicos".equals(tipo)) {
            return FileUtils.readRawFileString(context, R.raw.carros_classicos, "UTF-8");
        } else if ("esportivos".equals(tipo)) {
            return FileUtils.readRawFileString(context, R.raw.carros_esportivos, "UTF-8");
        }
        return FileUtils.readRawFileString(context, R.raw.carros_luxo, "UTF-8");
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