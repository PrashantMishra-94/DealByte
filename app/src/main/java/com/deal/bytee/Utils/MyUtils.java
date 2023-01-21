package com.deal.bytee.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.deal.bytee.model.CartAddVoucherModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.deal.bytee.Network.MyVolley;
import com.deal.bytee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import kotlin.collections.CollectionsKt;

public class MyUtils {
    private static final String TAG = "MyUtils";
    static AlertDialog dialog;
    public static ACProgressFlower dialog1;

    public static ArrayList<CartAddVoucherModel> cartAddVoucherModelArrayList = new ArrayList<>();



    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static final List<String> getSampleEvents() {
        return CollectionsKt.listOf("2021","2020","2019","2018");
    }




    public static String getAddress(double lat, double lng, Activity activity) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setHideShowPassword(final EditText edtPassword) {
        edtPassword.setTag("show");
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (edtPassword.getTag().equals("show")) {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_hide, 0);
                            edtPassword.setTransformationMethod(null);
                            edtPassword.setTag("hide");
                        } else {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_show, 0);
                            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                            edtPassword.setTag("show");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static String simpleVolleyRequestError(String TAG, VolleyError error) {
        String error_type = "";
        error.printStackTrace();
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            try {
                NetworkResponse response = error.networkResponse;
                String body = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                Log.d(TAG, "simpleVolleyRequestErrorER: " + body);
                if (statusCode == 400 || statusCode == 401) {
                    JSONObject obj = new JSONObject(body);
                    if (obj.has("errors_keys")) {
                        Object intervention;
                        intervention = obj.get("errors_keys");
                        if (intervention instanceof JSONArray) {
                            // It's an array
                            JSONArray errors = obj.getJSONArray("errors_keys");
                            String key = errors.get(0).toString();
                            JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                            showTheToastMessage(messageArray.get(0).toString());
                            error_type = key;
                        } else {
                            // It's an object
                            String key = obj.getString("errors_keys");
                            Log.d(TAG, "simpleVolleyRequestErrorkey: " + key);
                            JSONObject messageObject = obj.getJSONObject("errors");
                            showTheToastMessage(messageObject.getString("message"));
                            error_type = key;
                        }
                    }

                } else {
                    String errorString = MyVolley.handleVolleyError(error);
                    showTheToastMessage(errorString);
                }

            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
                showTheToastMessage(e + "");
            }
        } else {
            MyVolley.handleVolleyError(error);
        }
        return error_type;
    }


    public static void onErrorResponse(VolleyError error) {

        // As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
                JSONObject obj = new JSONObject(res);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                e1.printStackTrace();
            } catch (JSONException e2) {
                // returned data is not JSONObject?
                e2.printStackTrace();
            }
        }
    }

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }

    public static void showVolleyError(VolleyError error, String TAG, Context context) {
        error.printStackTrace();
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            try {
                String body = new String(error.networkResponse.data, "UTF-8");
                Log.d(TAG, "simpleVolleyRequestError: " + body);
                if (statusCode == 400 || statusCode == 401) {
                    JSONObject obj = new JSONObject(body);
                    if (obj.has("errors_keys")) {
                        Object intervention;
                        intervention = obj.get("errors_keys");
                        if (intervention instanceof JSONArray) {
                            // It's an array
                            JSONArray errors = obj.getJSONArray("errors_keys");
                            String key = errors.get(0).toString();
                            JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                            Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // It's an object
                            String key = obj.getString("errors_keys");
                            JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                            Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    String errorString = MyVolley.handleVolleyError(error);
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
                }

            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
            }
        } else {
            MyVolley.handleVolleyError(error);
        }
    }

    public static void showVolleyErrorNoDataFound(VolleyError error, String TAG, Context context, TextView textView) {
        error.printStackTrace();
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            try {
                String body = new String(error.networkResponse.data, "UTF-8");
                Log.d(TAG, "simpleVolleyRequestError: " + body);
                if (statusCode == 400 || statusCode == 401) {
                    JSONObject obj = new JSONObject(body);
                    if (obj.has("errors_keys")) {
                        Object intervention;
                        intervention = obj.get("errors_keys");
                        if (intervention instanceof JSONArray) {
                            // It's an array
                            JSONArray errors = obj.getJSONArray("errors_keys");
                            String key = errors.get(0).toString();
                            JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                            Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // It's an object
                            String key = obj.getString("errors_keys");
                            JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                            Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    String errorString = MyVolley.handleVolleyError(error);
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
                }

            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
            }
        } else {
            MyVolley.handleVolleyError(error);
        }
    }

    public static void positionShifterOTP(final EditText et1, final EditText et2) {

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et1.getText().toString().length() == 1) {     //size as per your requirement
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public static boolean isValidMobile(String mobile) {
        String MobilePattern = "[0-9]{10}";
        return mobile.matches(MobilePattern);
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                //  handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {


                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }

//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String fetchFileName(Intent data) {
        Uri uri = data.getData();
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();
        String displayName = null;

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = App.getAppContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    // Log.i(TAG, "onActivityResult_new_file " + displayName);
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
            // Log.i(TAG, "onActivityResult_new_file " + displayName);
        }
        return displayName;
    }

    public static void showTheToastMessage(String message) {
        if (message != null) {
            Toast.makeText(App.getAppContext(), message, Toast.LENGTH_LONG).show();
        }
    }


    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }


    public static String colorDecToHex(int p_red, int p_green, int p_blue) {
        String red = Integer.toHexString(p_red);
        String green = Integer.toHexString(p_green);
        String blue = Integer.toHexString(p_blue);

        if (red.length() == 1) {
            red = "0" + red;
        }
        if (green.length() == 1) {
            green = "0" + green;
        }
        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        String colorHex = "#" + red + green + blue;
        return colorHex;
    }

    public static Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String generateRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("[-+.^:,|@_]", "");
    }

    public static String generateRandomeNumber(){
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        return  formatted;
    }

    public static long getTimeInMilliSeconds(String timeToConvert) {
        String[] data = timeToConvert.split(":");
        int hours = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);
        int seconds = Integer.parseInt(data[2]);
        int time = seconds + 60 * minutes + 3600 * hours;
        return TimeUnit.MILLISECONDS.convert(time, TimeUnit.SECONDS);
    }


    /*public static void showProgressDialog(Context ctx, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View v = LayoutInflater.from(ctx).inflate(R.layout.progresslay, null, false);
        builder.setView(v);
        builder.setCancelable(cancelable);
        dialog = builder.create();
        dialog.show();
    }*/

    /*public static void dismisProgressDialog() {
        dialog.dismiss();
    }*/

    public static void showProgressDialog(Context context, boolean cancelable) {
        try {
            if (dialog1 == null) {
                dialog1 = new ACProgressFlower.Builder(context)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .fadeColor(Color.DKGRAY)
                        .build();
                dialog1.setCancelable(cancelable);
                dialog1.setCanceledOnTouchOutside(cancelable);
            }
            if (!dialog1.isShowing())
                //if (!((Activity) context).isFinishing())
                dialog1.show();

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismisProgressDialog() {
        try {
            if (dialog1 != null) {
                if (dialog1.isShowing()) {
                    dialog1.dismiss();
                    dialog1 = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> getFullAddressAPI(double latitude, double longitude, Context context) {
        List<String> addressList = new ArrayList<>();
        Address locationAddress = getAddress(latitude, longitude, context);

        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();
            String SubLocality = locationAddress.getSubLocality();
            String house_number = locationAddress.getFeatureName();

            addressList.add(country);
            addressList.add(state);
            addressList.add(city);
        }
        return addressList;
    }

    // get adddress
    public static Address getAddress(double latitude, double longitude, Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long convertDateTimeToTimeStamp(String onlyDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        Date date = null;
        try {
            date = sdf.parse(onlyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime() / 1000;
        return millis;
    }

    public static boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    public static boolean isFirstItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItemPosition == 0)
                return true;
        }
        return false;
    }

    public static String convertDateTime(String timestamp) {
        long unixSeconds = Long.parseLong(timestamp);
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("+5:30"));
        return sdf.format(date);
    }


    public static String convertDate(String timestamp) {
        long unixSeconds = Long.parseLong(timestamp);
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("+5:30"));
        return sdf.format(date);
    }

    public static String convertTime(String timestamp) {
        long unixSeconds = Long.parseLong(timestamp);
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("+5:30"));
        return sdf.format(date);
    }

    public static String converDateTimeWhourdot(String timestampStr) {
        long unixSeconds = Long.parseLong(timestampStr);
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy | K:mm a");
        try {
            final Date dateObj = sdf.parse(timestampStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String formattedDate = sdf1.format(date);
        return formattedDate;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void downloadFile(String file, Context context, String file_type) {
        String DownloadUrl = file;
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setDescription("Tripster");
        request1.setTitle("Tripster File");
        request1.setVisibleInDownloadsUi(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalFilesDir(context, "/File", System.currentTimeMillis() + "." + file_type);

        DownloadManager manager1 = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void showVolleyErrorUpload(String error, String TAG, Context context) {
        try {
            JSONObject obj = new JSONObject(error);
            if (obj.has("errors_keys")) {
                Object intervention;
                intervention = obj.get("errors_keys");
                if (intervention instanceof JSONArray) {
                    // It's an array
                    JSONArray errors = obj.getJSONArray("errors_keys");
                    String key = errors.get(0).toString();
                    JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                    Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                } else {
                    // It's an object
                    String key = obj.getString("errors_keys");
                    JSONArray messageArray = obj.getJSONObject("errors").getJSONArray(key);
                    Toast.makeText(context, messageArray.get(0).toString(), Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFileToByte(String filePath) {
        String encodeString = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bt = baos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showAlert(String msg, final Context ctx) {
        try {
            new AlertDialog.Builder(ctx)
                    .setMessage(msg)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean regex_matcher(String string) {
        final String pan_pattern = "(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))";

        Pattern pattern = Pattern.compile(pan_pattern);
        Matcher m = pattern.matcher(string);
        return m.find() && (m.group(0) != null);
    }

    public static String getPath(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static byte[] getFileData(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        byte[] tmpBuff = new byte[size];

        try (FileInputStream inputStream = new FileInputStream(file)) {
            int read = inputStream.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = inputStream.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }


}