package blood_cloud.com.osahub.app.bloodnew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;
import com.osahub.app.blood_cloud.R;

import java.io.IOException;

import blood_cloud.com.osahub.app.bloodnew.API.GcmAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GCM extends Activity {
    ProgressDialog prgDialog;
    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    String emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        applicationContext = getApplicationContext();
        emailET = prefs.getString("eMailId", "");

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        String registrationId = prefs.getString(REG_ID, "");
        if (!TextUtils.isEmpty(registrationId)) {
            Intent i = new Intent(applicationContext, Location.class);

            startActivity(i);
            finish();
        }
        else
        RegisterUser();
    }

    public void RegisterUser() {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        emailET = prefs.getString("eMailId", "");
        String emailID = emailET;

        if (!TextUtils.isEmpty(emailID) && Utility.validate(emailID)) {
            if (checkPlayServices()) {
                registerInBackground(emailID);
            }
        }
        // When Email is invalid
        else {
            Toast.makeText(applicationContext, "Please enter valid email",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void registerInBackground(final String emailID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref(applicationContext, regId);
                    Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    public void signout(View v){
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("UserDetails", 0);
        editor = pref.edit();
        editor.clear();
        //editor.remove(EMAIL_ID);
        editor.commit();
        Intent intent= new Intent(applicationContext,Login.class);
        startActivity(intent);
    }
    private void storeRegIdinSharedPref(Context context, String regId
                                        ) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);

        editor.commit();

        StoreRegIdinServer storeRegIdinServer = new StoreRegIdinServer();
        storeRegIdinServer.execute();
    }


    private class StoreRegIdinServer extends AsyncTask<Void,Void,Void> {

        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ApplicationConstants.APP_SERVER_URL).build();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GcmAPI gcmAPI = restAdapter.create(GcmAPI.class);
            Callback callback=new Callback() {
                @Override
                public void success(Object o, Response response) {


                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }



                }

                @Override
                public void failure(RetrofitError error) {
                    prgDialog.hide();
                    if (prgDialog != null) {
                        prgDialog.dismiss();
                    }
                    Log.v("Error",error.getMessage()+ " "+regId);



                    Toast.makeText(applicationContext,
                            "Reg Id shared successfully with Web App " ,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(applicationContext,
                            Location.class);
                    i.putExtra("regId", regId);
                    startActivity(i);
                    finish();
                }

            };
            gcmAPI.putGCM("true",regId,callback);
            return null;
        }



    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        applicationContext,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(
                    applicationContext,
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}
