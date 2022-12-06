package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.example.myapplication.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{
    //declaration variable
    private ActivityMainBinding binding;
    String index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://1ba0-103-154-144-186.ngrok.io/api/products")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String data) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject innerObj =
                    jsonObject.getJSONObject("data");
            JSONArray cityArray = innerObj.getJSONArray("data");
            for (int i =0; i <cityArray.length(); i++){
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("id").toString();
                if (Sobj.equals(index)){
                    String id = obj.get("id").toString();
                    String name = obj.get("name").toString();
                    String created_at = obj.get("created_at").toString();
                    String updated_at = obj.get("updated_at").toString();
                    String description = obj.get("description").toString();
                    String qty = obj.get("qty").toString();
                    String price = obj.get("price").toString();
                    String image = obj.get("image").toString();
                    String rating = obj.get("rating").toString();
                    binding.resultId.setText(id);
                    binding.resultName.setText(name);
                    binding.resultCreated.setText(created_at);
                    binding.resultUpdated.setText(updated_at);
                    binding.resultDescription.setText(description);
                    binding.resultQty.setText(qty);
                    binding.resultPrice.setText(price);
                    binding.resultImage.setText(image);
                    binding.resultRating.setText(rating);

                    break;
                }
                else{
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }
}
