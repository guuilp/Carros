package br.com.livroandroid.carros.domain;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.R;
import livroandroid.lib.utils.FileUtils;
import livroandroid.lib.utils.XMLUtils;

/**
 * Created by Guilherme on 06-Sep-15.
 */
public class CarroService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroService";

    public static List<Carro> getCarros(Context context, String tipo){
        try{
            String xml = readFile(context, tipo);
            List<Carro> carros = parserXML(context, xml);
            return carros;
        } catch (Exception e) {
            //TODO explicar excpetion
            Log.e(TAG, "Erro ao ler os carros: " + e.getMessage(), e);
            return null;
        }
    }

    //Faz a leitura do arquivoq ue está na pasta /res/raw
    private static String readFile(Context context, String tipo) throws IOException{
        if ("classicos".equals(tipo)){
            return FileUtils.readRawFileString(context, R.raw.carros_classicos, "UTF-8");
        } else if ("esportivos".equals(tipo)){
            return FileUtils.readRawFileString(context, R.raw.carros_esportivos, "UTF-8");
        }
        return FileUtils.readRawFileString(context, R.raw.carros_luxo, "UTF-8");
    }

    //Faz o parser do XML e cria a lista de carros
    private static List<Carro> parserXML(Context context, String xml) {
        List<Carro> carros = new ArrayList<Carro>();
        Element root = XMLUtils.getRoot(xml, "UTF-8");
        //Le todas as tags <carro>
        List<Node> nodeCarros = XMLUtils.getChildren(root, "carro");
        //Insere cada carro na lista
        for(Node node : nodeCarros){
            Carro c = new Carro();
            //Lê as informações de cada carro
            c.nome = XMLUtils.getText(node, "nome");
            c.desc = XMLUtils.getText(node, "desc");
            c.urlFoto = XMLUtils.getText(node, "url_foto");
            c.urlInfo = XMLUtils.getText(node, "url_info");
//            c.urlVideo = XMLUtils.getText(node, "urlVideo");
//            c.latitude = XMLUtils.getText(node, "latitude");
//            c.longitude = XMLUtils.getText(node, "longitude");
            if(LOG_ON){
                Log.d(TAG, "Carro" + c.nome + " > " + c.urlFoto);
            }
            carros.add(c);
        }
        if(LOG_ON){
            Log.d(TAG, carros.size() + " encontrados.");
        }
        return carros;
    }
}
