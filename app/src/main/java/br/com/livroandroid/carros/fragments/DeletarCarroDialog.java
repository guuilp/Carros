package br.com.livroandroid.carros.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroDB;

/**
 * Created by Guilherme on 25-Oct-15.
 */
public class DeletarCarroDialog extends DialogFragment {
    private Callback callback;
    private Carro carro;
    public static interface Callback{
        public void onCarroDeleted(Carro carro);
    }

    public static void show(FragmentManager fm, Carro carro, Callback callback){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("deletar_carro");
        if(prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DeletarCarroDialog frag = new DeletarCarroDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable("carro", carro);
        frag.setArguments(args);
        frag.show(ft, "deletar_carro");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.carro = (Carro) getArguments().getSerializable("carro");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Deleta o carro
                        CarroDB db = new CarroDB(getActivity());
                        db.delete(carro);
                        if(callback != null){
                            callback.onCarroDeleted(carro);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deletar esse carro?");
        builder.setPositiveButton("Sim", dialogClickListener);
        builder.setNegativeButton("Não", dialogClickListener);
        return builder.create();
    }
}
