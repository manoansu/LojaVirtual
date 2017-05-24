package pt.iscte.daam.appvirtual;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.iscte.daam.appvirtual.adapter.CustomProdutoAdapter;
import pt.iscte.daam.appvirtual.entity.Produto;
import pt.iscte.daam.appvirtual.util.Constantes;
import pt.iscte.daam.appvirtual.util.UTF8ParseJson;

public class ListaProdutosActivity extends AppCompatActivity implements CustomProdutoAdapter.ProdutoListAdapterListener {

    private ListView lstProdutos;
    private Button btnCheckout;

    private CustomProdutoAdapter customProdutoAdapter;
    private List<Produto> produtos = new ArrayList<>();

    private ProgressDialog progressDialog;

    private List<PayPalItem> produtosCarrinho = new ArrayList<>();

    private PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(Constantes.PAYPAL_ENV)
            .clientId(Constantes.PAYPAL_CLIENT_ID)
            .languageOrLocale("pt");
//            .languageOrLocale("pt_BR");

    private static final int COD_PAGTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_produtos);
        setTitle("Lista de Produtos");

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstProdutos = (ListView) findViewById(R.id.lstProdutos);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);

        customProdutoAdapter = new CustomProdutoAdapter(this, produtos, this);
        lstProdutos.setAdapter(customProdutoAdapter);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intent);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produtosCarrinho.isEmpty()) {
                    Toast.makeText(ListaProdutosActivity.this, "O carrinho está vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    executarPagtoPayPal();
                }
            }
        });

        buscarListaProdutos();
    }

    /**
     * Mehod: buscarListaProdutos() busca lista de produtos.
     */
    private void buscarListaProdutos() {
        showProgressDialog();

        produtos.clear();
        JsonArrayRequest request = new UTF8ParseJson(Constantes.URL_WS_PRODUTOS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type type = new TypeToken<List<Produto>>() {
                }.getType();
                List<Produto> produtos = new Gson().fromJson(response.toString(), type);
                ListaProdutosActivity.this.produtos.addAll(produtos);

                customProdutoAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Erro no carregamento de produtos", error);
                        Toast.makeText(ListaProdutosActivity.this, "Erro na listagem de produtos.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        LojaVirtualApp.getInstance().addToRequestQueue(request);
    }

    private PayPalPayment montarPagtoFinal() {
        PayPalItem[] itens = new PayPalItem[produtosCarrinho.size()];
        itens = produtosCarrinho.toArray(itens);

        // Valor total do pagto
        BigDecimal total = PayPalItem.getItemTotal(itens);

        PayPalPaymentDetails detalhes = new PayPalPaymentDetails(BigDecimal.ZERO, total, BigDecimal.ZERO);

        PayPalPayment payPalPayment =
                new PayPalPayment(total, Constantes.PAYPAL_CURRENCY, "Transação de compra em processamento...", Constantes.PAYPAL_INTENT);

        payPalPayment.items(itens).paymentDetails(detalhes);

        payPalPayment.custom("Compra de Produtos - Loja ISCTE");

        return payPalPayment;
    }

    private void executarPagtoPayPal() {
        PayPalPayment pagto = montarPagtoFinal();

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, pagto);

        startActivityForResult(intent, COD_PAGTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COD_PAGTO) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Toast.makeText(ListaProdutosActivity.this, confirm.toJSONObject().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ListaProdutosActivity.this, confirm.getPayment().toJSONObject().toString(), Toast.LENGTH_SHORT).show();

                        String idPagto = confirm.toJSONObject().getJSONObject("response").getString("id");

                        String jsonClientePagto = confirm.getPayment().toJSONObject().toString();

                        checkPagtoInServer(idPagto, jsonClientePagto);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ListaProdutosActivity.this, "Ex: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ListaProdutosActivity.this, "Usuário cancelou a operação.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(ListaProdutosActivity.this, "O pagamento é inválido, rever parâmetros.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPagtoInServer(final String idPagto, final String jsonClientePagto) {
        showProgressDialog();

        StringRequest request = new StringRequest(Request.Method.POST, Constantes.URL_WS_CHECK_PAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resposta = new JSONObject(response);
                            boolean erro = resposta.getBoolean("erro");
                            String msg = resposta.getString("msg");

                            Toast.makeText(ListaProdutosActivity.this, msg, Toast.LENGTH_SHORT).show();

                            if (!erro) {
                                produtos.clear();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListaProdutosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idPagto", idPagto);
                params.put("clientePagtoJson", jsonClientePagto);
                params.put("idUsuario", "1");

                return params;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        LojaVirtualApp.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onAddCarrinhoPressed(Produto produto) {
        PayPalItem payPalItem = new PayPalItem(
                produto.getTitulo(),
                1,
                produto.getValor(),
                Constantes.PAYPAL_CURRENCY,
                produto.getSku()
        );

        produtosCarrinho.add(payPalItem);

        Toast.makeText(ListaProdutosActivity.this, "Produto " + produto.getTitulo() + " adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
