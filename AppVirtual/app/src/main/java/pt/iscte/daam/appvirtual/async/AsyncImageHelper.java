package pt.iscte.daam.appvirtual.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Diogo on 03/04/2016.
 */
public class AsyncImageHelper extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public AsyncImageHelper(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imgUrl = urls[0];
        Bitmap bitmapImg = null;

        try {
            InputStream inputStrem = new URL(imgUrl).openStream();
            bitmapImg = BitmapFactory.decodeStream(inputStrem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapImg;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
