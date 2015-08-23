package blood_cloud.com.osahub.app.bloodnew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.osahub.app.blood_cloud.R;

import blood_cloud.com.osahub.app.bloodnew.API.BloodAPI;
import blood_cloud.com.osahub.app.bloodnew.Model.Bloodmodel;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class Login extends ActionBarActivity {
    final String LOG_TAG=Login.class.getSimpleName();
    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);


        String emailId = prefs.getString(EMAIL_ID, "");
        applicationContext = getApplicationContext();


        if (!TextUtils.isEmpty(emailId)) {
            Intent i = new Intent(applicationContext, GCM.class);
            i.putExtra("emailId", emailId);
            startActivity(i);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void Login(View view) {
        EditText email = (EditText) findViewById(R.id.editText);
        EditText passward = (EditText) findViewById(R.id.editText2);
        final String eMail = email.getText().toString();
        final String pass = passward.getText().toString();
        LoginController loginController = new LoginController(eMail, pass);
        loginController.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


     class LoginController extends AsyncTask<Void,Void,Void> {


        private final String LOG_TAG = LoginController.class.getSimpleName();
        private String email,pass;
         RestAdapter restAdapter;

        LoginController(String email, String pass){
            this.email=email;
            this.pass=pass;
        }

         @Override
         protected void onPreExecute() {
             super.onPreExecute();

             restAdapter = new RestAdapter.Builder()
                     .setEndpoint(ApplicationConstants.APP_SERVER_URL).build();
         }


        @Override
        protected Void doInBackground(Void... voids) {


            //create an adapter for retrofit with base url
            Log.v(LOG_TAG, "email " + email + pass);

            BloodAPI bloodAPI  = restAdapter.create(BloodAPI.class);
            Callback callback = new Callback() {
                @Override
                public void success(Object o, retrofit.client.Response response) {
                    Bloodmodel bloodmodel1 = (Bloodmodel) o;
                    if (bloodmodel1.getAction().equals("yes")) {
                        Log.v(LOG_TAG, "email " + email + pass);
                        SharedPreferences prefs = getSharedPreferences("UserDetails",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(EMAIL_ID, email);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GCM.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "no response", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            };
            bloodAPI.getBack(email,pass,callback);
            return null;
        }

    }

}
