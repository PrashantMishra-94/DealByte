package com.deal.bytee.fragment;

import static android.app.Activity.RESULT_OK;

import static com.deal.bytee.fragment.ProfileFragment.tvEmail;
import static com.deal.bytee.fragment.ProfileFragment.tvUserName;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.Endpoints;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.Utils.MyUtils;
import com.deal.bytee.activities.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


public class EditProfileFragment extends BaseFragment {


    private static final String TAG = "EDITPROFILEFRAGMENT";
    View view;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayoutCompat llTop;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private RelativeLayout rlProfileSelect;
    private RoundedImageView ivProfileImage;
    private AppCompatEditText edtName;
    private AppCompatEditText edtEmail;
    private AppCompatEditText edtMobile;
    public Uri profileselectedUri;
    public static String profilefilePath = "";
    public String bytesForImage = "";
    private File profilefile;


    public EditProfileFragment() {
        // Required empty public constructor
        super(R.layout.fragment_edit_profile);
    }

    private void initView(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        llTop = (LinearLayoutCompat) view.findViewById(R.id.llTop);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        rlProfileSelect = (RelativeLayout) view.findViewById(R.id.rlProfileSelect);
        ivProfileImage = (RoundedImageView) view.findViewById(R.id.ivProfileImage);
        edtName = (AppCompatEditText) view.findViewById(R.id.edtName);
        edtEmail = (AppCompatEditText) view.findViewById(R.id.edtEmail);
        edtMobile = (AppCompatEditText) view.findViewById(R.id.edtMobile);
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        if (islogin.equals(MySharedPreferences.YES)) {
            edtName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
            edtEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
            edtMobile.setText(App.sharedPreferences.getKey(MySharedPreferences.mobile));
            bytesForImage = App.sharedPreferences.getKey(MySharedPreferences.profile_pic).replaceAll("\\n","");


           // Bitmap myBitmap = BitmapFactory.decodeFile(bytesForImage);
            try ( InputStream is = new URL( bytesForImage ).openStream() ) {
                Bitmap bitmap = BitmapFactory.decodeStream( is );
                ivProfileImage.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("Get profile pic: "+e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Get profile pic: "+e.toString());
            }


            /*if(isUrlValid(Endpoints.BASE_URL_IMAGE+bytesForImage)) {
                Log.d(TAG, "initView: bytee>>>"+bytesForImage);
                Bitmap profilePic;
                try {
                    InputStream in = new URL(bytesForImage).openConnection().getInputStream();
                    profilePic = BitmapFactory.decodeStream(in);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    profilePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    saveBitmap(stream.toByteArray() , "img");
                } catch(Exception e) {
                    System.out.println("Get profile pic: "+e.toString());
                }
            }else {
                System.out.println("Enter valid URL");
            }
*/


        }

    }

    public  boolean isUrlValid(String url) {
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public void saveBitmap(byte[] bitmap, String key) {

        String path = App.getAppContext().getFilesDir()+"/.image"+"/";

        File fileDir = new File(path);

        if(!fileDir.isDirectory())
            fileDir.mkdirs();
        try {
            File bitmapDir = new File(fileDir+"/"+key);
            bitmapDir.createNewFile();
            FileOutputStream stream = new FileOutputStream(bitmapDir);

            stream.write(bitmap);
            stream.close();
            Glide.with(getActivity()).load(bitmapDir).placeholder(R.drawable.ic_launcher_background)
                    .into(ivProfileImage);
        } catch (IOException e) {
            System.out.println("Problem creating file "+e.toString()+ " Directory: "+fileDir);
        }
    }

    public static final class Companion {
        private Companion() {
        }

        public static final EditProfileFragment instance() {
            return new EditProfileFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initView(view);
        bindClick();
        return view;
    }

    private void bindClick() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });

        rlProfileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            CropImage.startPickImageActivity(getActivity(), EditProfileFragment.this);

                            //CropImage.startPickImageActivity(getActivity());
                           /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_PICTURE);*/
                        }
                    }
                });
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();
            }
        });

    }

    private void ValidationCheck() {

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.requestFocus();
            edtName.setError("Please enter name");
        }else if (!MyUtils.isValidEmail(email)) {
            edtEmail.requestFocus();
            edtEmail.setError("Please enter valid email");
        }else if (!MyUtils.isValidMobile(mobile)) {
            edtMobile.requestFocus();
            edtMobile.setError("Please enter valid mobile");
        }/* else if (TextUtils.isEmpty(bytesForImage)) {
            MyUtils.showTheToastMessage("Please select image!");
        } */else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(getActivity());
                MyUtils.showProgressDialog(getActivity(), false);
                apicallupdate(name, bytesForImage,email,mobile);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }
    }

    private void apicallupdate(String name, String bytesForImage, String email, String mobile) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("name", name);
            js.put("address", App.sharedPreferences.getKey(MySharedPreferences.address));
            js.put("password", App.sharedPreferences.getKey(MySharedPreferences.pass));
            js.put("email", email);
            js.put("mobile", mobile);
            js.put("profile_pic", bytesForImage);
            js.put("aadhar_number", App.sharedPreferences.getKey(MySharedPreferences.aadhar_number));
            js.put("aadhar_front", App.sharedPreferences.getKey(MySharedPreferences.aadhar_front));
            js.put("aadhar_back", App.sharedPreferences.getKey(MySharedPreferences.aadhar_back));
            js.put("pan_number", App.sharedPreferences.getKey(MySharedPreferences.pan_number));
            js.put("pan_front", App.sharedPreferences.getKey(MySharedPreferences.pan_front));
            js.put("lat", App.sharedPreferences.getKey(MySharedPreferences.lat));
            js.put("long", App.sharedPreferences.getKey(MySharedPreferences.longi));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.UPDATE_PROFILE_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("updatepro>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.id, obj.getString("userid"));
                                App.sharedPreferences.setKey(MySharedPreferences.name, obj.getString("name"));
                                App.sharedPreferences.setKey(MySharedPreferences.email, obj.getString("email"));
                                App.sharedPreferences.setKey(MySharedPreferences.mobile, obj.getString("mobile"));
                                App.sharedPreferences.setKey(MySharedPreferences.pass, obj.getString("password"));
                                App.sharedPreferences.setKey(MySharedPreferences.address, obj.getString("address"));
                                edtName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
                                edtEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
                                edtMobile.setText(App.sharedPreferences.getKey(MySharedPreferences.mobile));
                                tvUserName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
                                tvEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
                            } else {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.dismisProgressDialog();
                String error_type = MyUtils.simpleVolleyRequestError("TAG", error);
                MyUtils.showTheToastMessage(error_type);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.mRequestQue.add(request);
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(9, 16)
                .setFixAspectRatio(true)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                profileselectedUri = imageUri;
                profilefilePath = profileselectedUri.getPath().toString();
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ivProfileImage.setImageURI(result.getUri());
                profileselectedUri = result.getUri();
                profilefilePath = profileselectedUri.getPath();
                profilefile = new File(profilefilePath);
                System.out.println("file path==" + profilefilePath);
                bytesForImage = MyUtils.getFileToByte(profilefilePath);
                Log.d("profile>>>>bytee", bytesForImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }


    }

}