package pt.iscte.daam.appvirtual.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import pt.iscte.daam.appvirtual.MainActivity;
import pt.iscte.daam.appvirtual.entity.ProdutoNotification;
import pt.iscte.daam.appvirtual.notification.NotificationUtil;

/**
 * Created by Diogo on 15/05/2016.
 */
public class GCMPushReceiver extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String titulo = bundle.getString("titulo");
        String message = bundle.getString("message");
        String data = bundle.getString("data");
        String idProduto = bundle.getString("id");
        String imgUrl = bundle.getString("imgUrl");
        String iconUrl = bundle.getString("iconUrl");
        String count = bundle.getString("count");

        ProdutoNotification produtoNotification = new ProdutoNotification(Integer.parseInt(idProduto), titulo, message, data);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("nf_produto", produtoNotification);

        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());
        notificationUtil.showBigNotificationMsg(titulo, message, data, i, //
                count != null ? Integer.parseInt(count) : null, iconUrl, imgUrl);
    }
}
