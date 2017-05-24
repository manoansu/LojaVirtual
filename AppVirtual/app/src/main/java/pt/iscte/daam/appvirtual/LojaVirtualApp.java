package pt.iscte.daam.appvirtual;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import pt.iscte.daam.appvirtual.util.BitmapCache;

/**
 * Created by amane on 06/05/2017.
 */

public class LojaVirtualApp extends Application {

    private static LojaVirtualApp devMediaApp;

    private RequestQueue requestQueue; // variavel fila de requisições..
    private ImageLoader imageLoader; // variavel que carega a imagem.

    @Override
    public void onCreate() {
        super.onCreate();
        devMediaApp = this;

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }

    public static synchronized LojaVirtualApp getInstance() {
        return devMediaApp;
    }

    public RequestQueue getRequestQueue() { // pega a requisicao da fila.
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() { // pega imagem a ser caregada.
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(), new BitmapCache());
        }
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void cancelarRequestPending() { // cancela o pedido pendente..
        if (requestQueue != null) {
            requestQueue.cancelAll(LojaVirtualApp.class.getSimpleName());
        }
    }
}
