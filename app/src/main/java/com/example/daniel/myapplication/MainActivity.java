package com.example.daniel.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.artur.myapplication.R;
import com.example.daniel.myapplication.model.Produkty;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> lista = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.ListaProduktow);
        WebService();

    }

    public void WebService(){
        new WebServiceHandlerProdukty().execute("https://salty-wildwood-25330.herokuapp.com/category/json/allProducts");
    }

    private class WebServiceHandlerProdukty extends AsyncTask<String, Void, Produkty[]> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Pobieranie danych");
            progressDialog.show();

        }

        @Override
        protected Produkty[] doInBackground(String... params) {
            try {

                String url = params[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Produkty[] produkty = restTemplate.getForObject(url, Produkty[].class);

                return produkty;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Produkty[] produkty) {
            super.onPostExecute(produkty);
            for (Produkty p : produkty
                 ) {
                lista.add("Product Name: "+p.getName()+"\n"+"Description: "+p.getDescription()+" \n" +"Actual price: "+ p.getBidAmout()+ " $\n");

            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, lista);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }





}
