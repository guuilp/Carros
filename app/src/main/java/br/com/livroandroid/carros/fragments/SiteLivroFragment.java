package br.com.livroandroid.carros.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import br.com.livroandroid.carros.R;

public class SiteLivroFragment extends BaseFragment{
    private static final String URL_SOBRE = "http://www.livroandroid.com.br/sobre.htm";
    private WebView webview;
    private ProgressBar progress;
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_site_livro, container, false);
        webview = (WebView) view.findViewById(R.id.webview);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        setWebViewClient(webview);
        //Carrega a página
        webview.loadUrl(URL_SOBRE);

        //Swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview.reload(); //Atualiza a página
            }
        };
    }

    private void setWebViewClient(WebView webview) {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webview, String url, Bitmap favicon){
                super.onPageStarted(webview, url, favicon);
                //Liga o progress
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Desliga o progress
                progress.setVisibility(View.INVISIBLE);
                //Termina a animação do Swipe to Refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("livroandroid", "webview url: " + url);
                if(url != null && url.endsWith("sobre.htm")){
                    AboutDialog.showAbout(getFragmentManager());
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
