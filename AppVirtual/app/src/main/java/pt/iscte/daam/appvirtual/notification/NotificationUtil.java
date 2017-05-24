package pt.iscte.daam.appvirtual.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.iscte.daam.appvirtual.R;

/**
 * Created by Diogo on 15/05/2016.
 */
public class NotificationUtil {

    public static final int NOTIFICATION_ID = 357;

    public static final int BIG_NOTIFICATION_ID = 358;

    private Context context;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void showSmallNotificationMsg(String titulo, String msg, String timestamp, Integer count, String iconUrl, Intent intent) {
        showBigNotificationMsg(titulo, msg, timestamp, intent, count, iconUrl, null);
    }

    public void showBigNotificationMsg(String titulo, String msg, String timestamp, Intent intent, Integer count, String iconUrl, String imgUrl) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        Bitmap icon = null;
        if (iconUrl == null) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri somAlarme = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notification");

        if (TextUtils.isEmpty(imgUrl)) {
            showSmallNotification(builder, icon, titulo, msg, timestamp, count, pendingIntent, somAlarme);
        } else {
            if (imgUrl.length() > 4 && Patterns.WEB_URL.matcher(imgUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imgUrl);
                if (bitmap != null)
                    showBigNotification(bitmap, builder, icon, titulo, msg, count, timestamp, pendingIntent, somAlarme);
                else
                    showSmallNotification(builder, icon, titulo, msg, timestamp, count, pendingIntent, somAlarme);
            }
        }

    }

    private void showSmallNotification(NotificationCompat.Builder builder, //
                                       Bitmap icon, //
                                       String titulo, //
                                       String msg, //
                                       String timestamp, //
                                       Integer count, //
                                       PendingIntent pendingIntent, //
                                       Uri somAlarme) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(msg);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(titulo)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(somAlarme)
                .setWhen(getTimeInMilliSec(timestamp))
                .setLargeIcon(icon)
                .setStyle(inboxStyle);

        if (count != null) {
            builder = builder.setNumber(count);
        }

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void showBigNotification(Bitmap bitmap, //
                                     NotificationCompat.Builder builder, //
                                     Bitmap icon, //
                                     String titulo, //
                                     String msg, //
                                     Integer count, //
                                     String timestamp, //
                                     PendingIntent pendingIntent, //
                                     Uri somAlarme) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(titulo);
        bigPictureStyle.setSummaryText(Html.fromHtml(msg).toString());
        bigPictureStyle.bigPicture(bitmap);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(titulo)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(somAlarme)
                .setWhen(getTimeInMilliSec(timestamp))
                .setLargeIcon(icon)
                .setStyle(bigPictureStyle)
                .build();

        if (count != null) {
            builder = builder.setNumber(count);
        }

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(BIG_NOTIFICATION_ID, notification);

    }

    public static long getTimeInMilliSec(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date data = format.parse(timestamp);
            return data.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap getBitmapFromURL(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
