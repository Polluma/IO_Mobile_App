package io.mobiledriver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = this.getSharedPreferences("com.sharedPreferences", Context.MODE_PRIVATE);
        String key = "Token";
        String l = prefs.getString(key, "0");
        Log.i("Preference: ", l);
        if(l == "0")
        {
            Log.i("Logging: ", "Loging required");
            //TODO
            /*
                Add async task to login
                Save token onPostExecute
             */
        }
        else
        {
            Log.i("Logging: ", "Logging not required, remebered values");
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void register(View view)
    {
        //Button registerButton = (Button)findViewById(R.id.registerButton);
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }

    public void onLogin(View view) throws IOException {
        new loginTask().execute("http://thebilet.usetitan.com/web.ashx/login");
    }

    public void forgotPassword(View view)
    {
        TextView forgotLoginView = (TextView)findViewById(R.id.main_forgotLogin);
        forgotLoginView.setVisibility(View.VISIBLE);
        TextView forgotEmailView = (TextView)findViewById(R.id.main_forgotEmail);
        forgotEmailView.setVisibility(View.VISIBLE);
        EditText forgotLoginEntry = (EditText)findViewById(R.id.main_forgotLoginEntry);
        forgotLoginEntry.setVisibility(View.VISIBLE);
        EditText forgotEmailEntry = (EditText)findViewById(R.id.main_forgotEmailEntry);
        forgotEmailEntry.setVisibility(View.VISIBLE);
        Button sendEmailButton = (Button)findViewById(R.id.main_sendMailButton);
        sendEmailButton.setVisibility(View.VISIBLE);
    }

    public void sendEmail(View view)
    {
        EditText forgotLoginEntry = (EditText)findViewById(R.id.main_forgotLoginEntry);
        EditText forgotEmailEntry = (EditText)findViewById(R.id.main_forgotEmailEntry);
        String login = forgotLoginEntry.getText().toString();
        String email = forgotEmailEntry.getText().toString();
        Log.i("Credentials: ", login + " " + email);
    }

    private class loginTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            EditText eLogin = (EditText)findViewById(R.id.login);
            EditText ePassword = (EditText)findViewById(R.id.password);
            Log.i("Testowanie logowania", eLogin.getText().toString() + " " +ePassword.getText().toString());

            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            Log.i("Device ID: ", tm.getDeviceId());


            String url = params[0];

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("username", eLogin.getText().toString()));
            urlParameters.add(new BasicNameValuePair("password", ePassword.getText().toString()));


            try
            {
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                HttpResponse response = client.execute(post);
                //Log.i("Response Value:", EntityUtils.toString(response.getEntity()));
                return EntityUtils.toString(response.getEntity());


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return "Error";
        }

        protected void onPostExecute(String result)
        {
            Log.i("Post: ", result);
            if(result.startsWith("ERROR"))
            {
                Log.i("Logowanie:", "ERROR");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Login error");
                builder.setMessage("Bad credentials");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    };
                });
                builder.show();
            }
            else
            {
                Log.i("Logowanie:", "OK");
                startActivity(new Intent(MainActivity.this, MainMenu.class));
                finish();
            }

        }

    }

}
