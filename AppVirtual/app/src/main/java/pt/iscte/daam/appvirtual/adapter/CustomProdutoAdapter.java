package pt.iscte.daam.appvirtual.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.NumberFormat;
import java.util.List;

import pt.iscte.daam.appvirtual.LojaVirtualApp;
import pt.iscte.daam.appvirtual.R;
import pt.iscte.daam.appvirtual.entity.Produto;
import pt.iscte.daam.appvirtual.util.Constantes;


/**
 * Created by amane on 06/05/2017.
 */
public class CustomProdutoAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<Produto> produtos;
    private final ProdutoListAdapterListener produtoListAdapterListener;

    private LayoutInflater inflater;
    private ImageLoader imageLoader = LojaVirtualApp.getInstance().getImageLoader();

    public CustomProdutoAdapter(
            final Activity activity,
            final List<Produto> produtos,
            final ProdutoListAdapterListener produtoListAdapterListener) {
        this.activity = activity;
        this.produtos = produtos;
        this.produtoListAdapterListener = produtoListAdapterListener;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.linha_produto2, null);
        }

        NetworkImageView imgProduto = (NetworkImageView) convertView.findViewById(R.id.imgProduto);
        TextView txtTitulo = (TextView) convertView.findViewById(R.id.txtTitulo);
        TextView txtDescricao = (TextView) convertView.findViewById(R.id.txtDescricao);
        TextView txtPreco = (TextView) convertView.findViewById(R.id.txtPreco);
        Button btnAddCarrinho = (Button) convertView.findViewById(R.id.btnAddCarrinho);

        final Produto produto = produtos.get(position);

        imgProduto.setImageUrl(Constantes.URL_WEB_BASE + produto.getUrlImg(), imageLoader);

        txtTitulo.setText(produto.getTitulo());
        txtDescricao.setText(produto.getDescricao());

        String valor = NumberFormat.getCurrencyInstance().format(produto.getValor());

        txtPreco.setText(valor);

        btnAddCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produtoListAdapterListener.onAddCarrinhoPressed(produto);
            }
        });

        return convertView;
    }

    public interface ProdutoListAdapterListener {
        public void onAddCarrinhoPressed(Produto produto);
    }
}
