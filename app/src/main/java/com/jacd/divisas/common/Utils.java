package com.jacd.divisas.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.jacd.divisas.R;
import com.jacd.divisas.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {


    private static String CHANNEL_ID = "12";


   @SuppressLint("ResourceAsColor")
   public static void getMessageSnackBar(@NonNull View view, String msg, @NonNull Context context, @NonNull boolean isMsgError){
       msg = (""+msg).replace("null","No Message");
       Snackbar snackbar = Snackbar.make(view, ""+msg, Snackbar.LENGTH_LONG);
       snackbar.setActionTextColor( (isMsgError) ?context.getResources().getColor(R.color.colorControlNormal) :context.getResources().getColor(R.color.colorControlNormal) );
       View sbView = snackbar.getView();
       sbView.setBackgroundColor(ContextCompat.getColor(context, (isMsgError) ?R.color.error_toast :R.color.colorPrimary));
       snackbar.show();
   }

    public static Dialog customDialog(Context context, String message, String lottieAnim) {
        if (context != null) {
            Dialog mDialog = new Dialog(context);
            try {

                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setCancelable(false);
                mDialog.setContentView(R.layout.dialog_anim);


                TextView mMessageDialog = (TextView) mDialog.findViewById(R.id.dialog_anim_text);
                mMessageDialog.setText(message);
                LottieAnimationView mLottie = mDialog.findViewById(R.id.dialog_anim_animation);
                mLottie.setAnimation(lottieAnim);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mDialog;
        }
        return null;
    }

    public interface LoadDialogInterface {
        void onLoadDialog(boolean error);

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


    public static Dialog customDialog(Context context, String message, String lottieAnim, LoadDialogInterface load) {
        if (context != null) {
            Dialog mDialog = new Dialog(context);
            try {

                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setCancelable(false);
                mDialog.setContentView(R.layout.dialog_anim);


                TextView mMessageDialog = (TextView) mDialog.findViewById(R.id.dialog_anim_text);
                mMessageDialog.setText(message);
                LottieAnimationView mLottie = mDialog.findViewById(R.id.dialog_anim_animation);
                mLottie.setAnimation(lottieAnim);
                load.onLoadDialog(true);

            } catch (Exception e) {
                e.printStackTrace();
                load.onLoadDialog(false);

            }
            return mDialog;
        }
        return null;
    }


    /**
     * Verify string is numeric
     *
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String setHTMLMessageNotification(String title, String origin, String destination, String time, String coordinates, String env, String type, String user, String email, String price, String distance, String phone) {

        String line = "<br>";
        String head = " <h1 style=\"background-color:Orange;\">" + title + "</h1> " + line;
        String usr = "<strong>Usuario: </strong>" + user + line;
        String ema = "<strong>Correo Electrónico: </strong>" + email + line;
        String ori = "<strong>Desde: </strong>" + origin + line;
        String des = "<strong>Hasta: </strong>" + destination + line;
        String tim = "<strong>Fecha y Hora: </strong>" + time + line;
        String prc = "<strong>Precio: </strong>" + price + line;
        String dst = "<strong>Distancia: </strong>" + distance + line;
        String telf = "<strong>Teléfono: </strong>" + phone + line;
        String en = "<strong>Entorno: </strong>" + env + line;
        String imMap = "<br><br><img width=\"600\" src=\"https://maps.googleapis.com/maps/api/staticmap?center=" + origin.replaceAll(" ", "+") + "&zoom=13&scale=1&size=600x300&maptype=roadmap&key=AIzaSyAIiXVbt3z9zRyjpAW2-b7eB9JIgWP7PGI&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff965c%7Clabel:O%7C" + origin.replaceAll(" ", "+") + "&markers=size:mid%7Ccolor:0x3f66ff%7Clabel:D%7C" + destination.replaceAll(" ", "+") + "\" alt=\"Google Map VE\">";


        String coord = "https://www.google.com/maps/dir/?api=1&origin=" + origin.replaceAll(" ", "+") + "&destination=" + destination.replaceAll(" ", "+") + "&travelmode=car";
        String coord2 = "<a href=\"" + coord + "\">Ver en Google Maps</a>";
        return head + usr + ema + telf + ori + des + tim + prc + dst + coord2 + imMap;
    }



    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validate Email
     *
     * @param emailStr
     * @return
     */

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    /**
     * Get Date in format dd-MM-yyyy or format required
     *
     * @param indate
     * @param outputDFormat
     * @return
     */
    public static String getDateDay(String indate, String outputDFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date1 = sdf.parse(indate);
            outputDFormat = outputDFormat == null || outputDFormat.isEmpty() ? "dd-MM-yyyy" : outputDFormat;
            SimpleDateFormat time = new SimpleDateFormat(outputDFormat);
            String time_s = time.format(date1);
            return time_s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToDate(String format, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String formatDecimal(double value) {
        //  DecimalFormat df = new DecimalFormat("#,##");
        //  return df.format(value);


        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%(,.2f", value);
        // System.out.println("Amount is - " + sb);

        String parsed_val = sb.toString().replace(".", "&");
        parsed_val = parsed_val.replaceAll(",", ".");
        parsed_val = parsed_val.replaceAll("&", ",");

        return parsed_val;
    }
    public static double formatFiveDecimal(double value) {
        //  DecimalFormat df = new DecimalFormat("#,##");
        //  return df.format(value);


        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%.5f", value);
        Double vvv = Double.parseDouble(sb.toString());
        // System.out.println("Amount is - " + sb);

        String parsed_val = sb.toString().replace(".", "&");
        parsed_val = parsed_val.replaceAll(",", ".");
        parsed_val = parsed_val.replaceAll("&", ",");

        return vvv;
    }

    public static String epochToDate(long epoch, String formatOut){
        SimpleDateFormat sdf;
        if (formatOut == null)
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        else
            sdf = new SimpleDateFormat(formatOut + "");

        Date dt = null;
        try {

            sdf = new SimpleDateFormat(formatOut);
            return sdf.format(new Date(epoch));


        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }


    }

    public static long dateToEpoch(String format, String date) {
        SimpleDateFormat sdf;
        if (format == null)
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        else
            sdf = new SimpleDateFormat(format + "");


        Date dt = null;
        try {


            dt = sdf.parse(date);
            long epoch = dt.getTime();
            int num = (int) (epoch / 1000);
            return epoch;


        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }


    }

    /**
     * Verify if is valid email
     *
     * @param email
     * @return
     */

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Convert a text file to String
     *
     * @param is
     * @return
     * @throws IOException
     */
//InputStream is = getResources().getAssets().open("SQLScript.sql");
//String sql= convertStreamToString(is);
    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    public static String getToday(String format) {
        String f = format != null ? format : "yyyy/MM/dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(f);
        Date date = new Date();
        return dateFormat.format(date);

    }


    public static  double StringFormatedToDouble(String num){

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            num= num.replace(".","@");
            num= num.replace(",",".");
            num=  num.replace("@",",");
            Number number = format.parse(num);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Encode image Bitmap to Base64
     *
     * @param bm
     * @return
     */
    public static String encodeImage(Bitmap bm) {
        Log.w("encodeImage", "image bounds: " + bm.getWidth() + ", " + bm.getHeight());

        if (bm.getHeight() <= 400 && bm.getWidth() <= 400) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
        }
        int mHeight = 400;
        int mWidth = 400;

        if (bm.getHeight() > bm.getWidth()) {
            float div = (float) bm.getWidth() / ((float) bm.getHeight());
            float auxW = div * 480;
            mHeight = 480;
            mWidth = Math.round(auxW);
            Log.w("encodeImage", "new high: " + mHeight + " width: " + mWidth);
        } else {
            float div = ((float) bm.getHeight()) / (float) bm.getWidth();
            float auxH = div * 480;
            mWidth = 480;
            mHeight = 360;
            mHeight = Math.round(auxH);
            Log.w("encodeImage", "new high: " + mHeight + " width: " + mWidth);
        }

        bm = Bitmap.createScaledBitmap(bm, mWidth, mHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
    }


    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        //   dialog.setContentView(R.layout.dialog);

        // TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        //  text.setText(msg);


        dialog.show();

    }


    /**
     * Create a notification with custom title and content text
     *
     * @param context
     * @param title
     * @param content
     */
    public static void CreateNotification(Context context, CharSequence title, CharSequence content) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.i("notification", alarmSound.toString());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(new long[]{1000, 1000});


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, mBuilder.build());

    }

    public static void setToast(Context context, String message, int type) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.color.error_toast);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
        toast.show();
    }


    public static String dateToString(Date d, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format != null ? format + "" : "yyyy-MM-dd HH:mm:ss");
        String stringDate = dateFormat.format(d);
        return stringDate;
    }



    public static boolean isNumberOrDecimal(String string) {
        try {
            float amount = Float.parseFloat(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * return 1 - obj 2 - array  -1 - error
     *
     * @param response
     * @return
     */
    public static int isArrayOrObjectJSON(String response) {
        try {
            String data = response.contains("error 500") ? response.substring(8) : response;
            Object json = new JSONTokener(data).nextValue();
            if (json instanceof JSONObject) {
                JSONObject object = new JSONObject(data);
                return 1;
            } else if (json instanceof JSONArray) {

                JSONArray array = new JSONArray(data);
                return 2;
            }

        } catch (JSONException jsex) {
            jsex.printStackTrace();
            return -1;
        }
        return 0;

    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{11}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{4}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{4}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }


    public static Dialog commonDialogWait(Context context, String text, boolean cancelable) {

        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_wait);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        TextView mMessageDialog = (TextView) mDialog.findViewById(R.id.dialog_wait_text);
        mMessageDialog.setText(text);
        LottieAnimationView mLottie = mDialog.findViewById(R.id.dialog_wait_animation);
        mLottie.setAnimation("waves2.json");


        return mDialog;
    }

    /**
     * Math round with decimals
     *
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public static Dialog imageDialog(Context context, String title, Bitmap bitmap) {
        Dialog mDialogImage = new Dialog(context);
        mDialogImage.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        mDialogImage.setContentView(R.layout.dialog_image);

        ImageView mImageOnDialog = mDialogImage.findViewById(R.id.dialog_img_image);
        mImageOnDialog.setImageBitmap(bitmap);
        Button mClose = mDialogImage.findViewById(R.id.dialog_img_close);
        mClose.setOnClickListener(v -> {
            mDialogImage.dismiss();
        });
        return mDialogImage;

    }

    public enum typeValue {
        INTEGER, BOOLEAN, STRING, FLOAT, DOUBLE, LONG
    }


    public static Object getShared(Context context, int stringKey, typeValue type) {

        if (typeValue.STRING == type)
            return context.getSharedPreferences(context.getString(R.string.shared_key), 0).getString(context.getString(stringKey), "");
        else if (typeValue.BOOLEAN == type)
            return context.getSharedPreferences(context.getString(R.string.shared_key), 0).getBoolean(context.getString(stringKey), false);
        else if (typeValue.INTEGER == type)
            return context.getSharedPreferences(context.getString(R.string.shared_key), 0).getInt(context.getString(stringKey), 0);

        else
            return null;

    }

    public static int checkNullInt(int value){
        try{
            return value;
        }catch (NumberFormatException nex){
            return 0;
        }
    }


}


