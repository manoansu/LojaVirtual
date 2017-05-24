package pt.iscte.daam.appvirtual.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import pt.iscte.daam.appvirtual.MainActivity;
import pt.iscte.daam.appvirtual.async.AsyncUsuarioHttpClient;
import pt.iscte.daam.appvirtual.util.Util;
import pt.iscte.daam.appvirtual.validation.LoginValidation;

/**
 * Created by Diogo Souza on 10/11/2015.
 */
public class LoginService {

    public void validarCamposLogin(final LoginValidation validation) {
        final Activity activity = validation.getActivity();
        boolean resultado = true;
        if (validation.getLogin() == null || "".equals(validation.getLogin())) {
            validation.getEdtLogin().setError("Campo obrigatório!");
            resultado = false;
        } else if (validation.getLogin().length() < 2) {
            validation.getEdtLogin().setError("Campo deve ter no mínimo 3 caracteres!");
        }

        if (validation.getSenha() == null || "".equals(validation.getSenha())) {
            validation.getEdtSenha().setError("Campo obrigatório!");
            resultado = false;
        }

        if (resultado) {
            RequestParams params = new RequestParams();
            params.put("usuario", validation.getLogin());
            params.put("senha", validation.getSenha());

            AsyncUsuarioHttpClient.post("user/login", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                    Log.e(LoginService.class.getName(), "Erro no login do usuario! Http Code: " + statusCode, throwable);
                    Util.showMsgSimpleToast(activity, "Erro no login do usuário!");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String resultado) {
                    if (Boolean.valueOf(resultado)) {
                        SharedPreferences.Editor editor = activity.getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                        editor.putString("login", validation.getLogin());
                        editor.putString("senha", validation.getSenha());
                        editor.commit();

                        Intent i = new Intent(activity, MainActivity.class);
                        activity.startActivity(i);
                        activity.finish();
                    } else {
                        Util.showMsgSimpleToast(activity, "Login/Senha inválidos!");
                    }
                }
            });

            // new AsyncUsuario(validation).execute(Constantes.URL_WS_LOGIN);
        }
    }

    public void deslogar() {

    }

}
