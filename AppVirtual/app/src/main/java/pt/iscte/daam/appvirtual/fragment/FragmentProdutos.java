package pt.iscte.daam.appvirtual.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import pt.iscte.daam.appvirtual.R;
import pt.iscte.daam.appvirtual.adapter.ProdutoRecyclerAdapter;
import pt.iscte.daam.appvirtual.entity.Produto;
import pt.iscte.daam.appvirtual.util.Constantes;
import cz.msebera.android.httpclient.Header;

/**
 * Created by amane on 06/05/2017.
 */
public class FragmentProdutos extends Fragment {

    private RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_produtos, container, false);

        final RelativeLayout lytLoading = (RelativeLayout) viewRoot.findViewById(R.id.lytLoading);
        lytLoading.setVisibility(View.VISIBLE);

        rv = (RecyclerView) viewRoot.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        new AsyncHttpClient().get(Constantes.URL_WS_BASE + "produto/list", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response != null) {
                    Type type = new TypeToken<List<Produto>>() {
                    }.getType();
                    List<Produto> produtos = new Gson().fromJson(response.toString(), type);

                    ProdutoRecyclerAdapter adapter = new ProdutoRecyclerAdapter(produtos);
                    rv.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Houve um erro no carregamento da lista de profissões...", Toast.LENGTH_SHORT).show();
                }

                lytLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), "Falha: " + responseString, Toast.LENGTH_SHORT).show();
            }
        });

        return viewRoot;
    }
}
