package pt.iscte.daam.appvirtual.gcm;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Diogo on 28/04/2016.
 */
public class InstanceListenerIDService extends InstanceIDListenerService {

    private static final String TAG = InstanceListenerIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Novo token de usuário!");

        Intent i = new Intent(this, RegistrationIntentService.class);
        startService(i);
    }
}
