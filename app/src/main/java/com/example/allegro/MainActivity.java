package com.example.allegro;

import android.app.VoiceInteractor;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.net.http.HttpResponseCache;

import com.anychart.enums.Sort;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    String token = "";
    StringBuffer sb;
    Boolean ready = false;
    InputStreamReader jsonresoult;
    Category[] categories = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        UpdateToken();
        while (!ready) ;
        CreateMenu("sale/categories");


    }

    public void Search(View view) {
        TextView textView = findViewById(R.id.editText);
        String productName =  textView.getText().toString();

        Items productList = CreateProduktList(productName);

        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("ProductName", productName);

        String[] Values = Count(productList);
        intent.putExtra("Values", Values);

        String[] PriceRanges = SetPriceRanges(Values);
        intent.putExtra("PriceRanges", PriceRanges);

        int[] CountedPriceRanges = CountPriceRanges(productList, PriceRanges);
        intent.putExtra("CountedPriceRanges", CountedPriceRanges);

        startActivity(intent);
    }

    public Category[] CreateMenu(final String parametr) {

        ready = false;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String json1 = GetSomething(parametr);


                    //   Category[] categories = mapper.readValue(json, Category[].class);
                    ObjectMapper mapper=new ObjectMapper();
                    Categories categories = mapper.readValue(json1, Categories.class);
                    JSONObject object = new JSONObject(json1);
                    JSONArray json = object.getJSONArray("categories");
                    Log.e("abc", "abc");
                    for (int index = 0; index < json.length(); index++) {
                        JSONObject jsonObject = json.getJSONObject(index);
                        String name = jsonObject.getString("id");
                        String number = jsonObject.getString("name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ready = true;
            }
        });
        while (!ready) ;
        return categories;


    }

    public Items CreateProduktList(final String parametr) {
        final Items productList= new Items();

        ready=false;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    List<Promoted> productListPromoted = new ArrayList<Promoted>();
                    List<Regular> productListRegular = new ArrayList<Regular>();
                    for(int offset=0;offset<1000;offset+=100)
                    {
                        String prefix = "offers/listing?limit=100&offset="+offset+"&phrase=";
                        String json1 = GetSomething(prefix + parametr);
                        int i=0;
                        while(json1 == "")
                        {
                            json1 = GetSomething(prefix + parametr);
                            if(i==10)break;
                            i++;
                        }
                        ObjectMapper mapper = new ObjectMapper();
                        ProductList productListTemporary = mapper.readValue(json1, ProductList.class);
                        productListPromoted.addAll(productListTemporary.items.promoted);
                        productListRegular.addAll(productListTemporary.items.regular);
                        if(productListTemporary.items.promoted.size()+productListTemporary.items.regular.size()<100) break;


                    }
                    productList.promoted=productListPromoted;
                    productList.regular=productListRegular;

                }
                catch (JsonParseException e)
                {
                    e.printStackTrace();
                }
                catch (JsonMappingException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                ready=true;
            }
        });
        while(!ready);
        return productList;


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


        return super.onOptionsItemSelected(item);
    }

    private void UpdateToken() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                token = "Something wrong";
                try {

                    String authUrl = "https://allegro.pl/auth/oauth/token?grant_type=client_credentials";
                    String clientId = "f3dd50c4a80d48028f3237d1db5d6f50";
                    String clientSecret = "WmCTqs7LK0X892BbXn0bYUjOpVyBssLlkGK4208Wq3krTem8CImZgZFsTnmpM4mk";
                    URL allegroEndpoint = new URL(authUrl);
                    HttpsURLConnection myConnection = (HttpsURLConnection) allegroEndpoint.openConnection();
                    String userCredentials = clientId + ":" + clientSecret;
                    byte[] data = userCredentials.getBytes("UTF-8");

                    String basicAuth = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);

                    myConnection.setRequestProperty("Authorization", basicAuth);
                    myConnection.setRequestMethod("GET");
                    myConnection.setRequestProperty("Content-Type", "application/json");
                    myConnection.setUseCaches(false);
                    myConnection.setDoInput(true);
                    myConnection.setDoOutput(true);


                    Log.e("abc", "abc");
                    if (myConnection.getResponseCode() == 200) {

                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object

                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName();
                            if (key.equals("access_token")) {
                                // Fetch the value as a String
                                token = jsonReader.nextString();

                                // Do something with the value
                                // ...

                                break; // Break out of the loop
                            } else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }
                        Log.e("Key", token);
                    } else {

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ready = true;
            }
        });


    }

    public String GetSomething(String parametr) {
        String Url = "https://api.allegro.pl/";
        String urlToRequest= Url+parametr;
        String str="";
        try {
            URL allegroEndpoint = new URL(urlToRequest);
            HttpsURLConnection myConnection = (HttpsURLConnection) allegroEndpoint.openConnection();
            myConnection.setRequestProperty("Authorization","Bearer "+token);
            myConnection.setRequestMethod("GET");
            myConnection.setRequestProperty("content-type", "application/vnd.allegro.public.v1+json");
            myConnection.setRequestProperty("accept", "application/vnd.allegro.public.v1+json");
            int a= myConnection.getResponseCode();
            if (myConnection.getResponseCode() == 200) {

                InputStream responseBody = myConnection.getInputStream();
                jsonresoult = new InputStreamReader(responseBody, "UTF-8");
                BufferedReader reader = new BufferedReader(jsonresoult);
                StringBuffer sb = new StringBuffer();
                 str=reader.readLine();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String[] Count (Items productList){

        Double[] Prices = {0.0, 0.0, 0.0, 0.0}, Median, MedianRegular = {0.0}, MedianPromoted = {0.0};
        Integer[] Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int MINIMUM_PRICE = 0,      //PRICES
            AVERAGE_PRICE = 1,
            MAXIMUM_PRICE = 2,
            MEDIAN = 3,
            SELLER_COMPANY = 0,     //VALUES
            SELLER_PRIVATE = 1,
            DELIVERY_FREE = 2,
            DELIVERY_PAID = 3,
            COUNTER = 4,
            PROMOTED = 5,
            REGULAR = 6,
            BUY_NOW = 7,
            AUCTION = 8,
            STOCK_AVG = 9,
            STOCK_MAX = 10;

        List<Promoted> promotedList = productList.getPromoted();
        if(promotedList != null){
            Values[PROMOTED] = promotedList.size();
            MedianPromoted = new Double[Values[PROMOTED]];
            for(int i = 0; i < promotedList.size(); i++)
            {
                Promoted promoted = promotedList.get(i);

                SellingMode sellingMode = promoted.getSellingMode();

                Price price = sellingMode.getPrice();
                //Najmniejsza cena
                if(Prices[MINIMUM_PRICE] == 0)
                {
                    Prices[MINIMUM_PRICE] = Double.parseDouble(price.getAmount());
                }
                else{
                    if( Prices[MINIMUM_PRICE] > Double.parseDouble(price.getAmount()) )
                    {
                        Prices[MINIMUM_PRICE] = Double.parseDouble(price.getAmount());
                    }
                }
                //Najwięsza cena
                if(Prices[MAXIMUM_PRICE] < Double.parseDouble(price.getAmount()) )
                {
                    Prices[MAXIMUM_PRICE] = Double.parseDouble(price.getAmount());
                }
                //Średnia cena
                Prices[AVERAGE_PRICE] += Double.parseDouble(price.getAmount());

                MedianPromoted[i] = Double.parseDouble(price.getAmount());

                String format = sellingMode.getFormat();
                if(format.contains("BUY_NOW")){
                    Values[BUY_NOW] += 1;
                }
                if(format.contains("AUCTION")){
                    Values[AUCTION] += 1;
                }

                Stock stock = promoted.getStock();
                //Najwięcej produktów
                if(Values[STOCK_MAX] < stock.getAvailable()){
                   Values[STOCK_MAX] = stock.getAvailable();
                }
                //Średnia cena
                Values[STOCK_AVG] += stock.getAvailable();

                Seller seller = promoted.getSeller();
                //Rodzaje sprzedawców
                if(seller.getCompany())
                {
                    Values[SELLER_COMPANY] += 1;
                }
                else
                {
                    Values[SELLER_PRIVATE] += 1;
                }

                Delivery delivery = promoted.getDelivery();
                //Koszt dostawy
                if(delivery.getAvailableForFree())
                {
                    Values[DELIVERY_FREE] += 1;
                }
                else
                {
                    Values[DELIVERY_PAID] += 1;
                }
            }
        }

        List<Regular> regularList = productList.getRegular();
        if(regularList != null){
            Values[REGULAR] = regularList.size();
            MedianRegular = new Double[Values[REGULAR]];
            for(int i = 0; i < regularList.size(); i++)
            {
                Regular regular = regularList.get(i);

                SellingMode sellingMode = regular.getSellingMode();
                //Najmniejsza cena
                Price price = sellingMode.getPrice();
                if(Prices[MINIMUM_PRICE] == 0)
                {
                    Prices[MINIMUM_PRICE] = Double.parseDouble(price.getAmount());
                }
                else{
                    if( Prices[MINIMUM_PRICE] > Double.parseDouble(price.getAmount()) )
                    {
                        Prices[MINIMUM_PRICE] = Double.parseDouble(price.getAmount());
                    }
                }
                //Najwięsza cena
                if(Prices[MAXIMUM_PRICE] < Double.parseDouble(price.getAmount()) )
                {
                    Prices[MAXIMUM_PRICE] = Double.parseDouble(price.getAmount());
                }
                //Średnie cena
                Prices[AVERAGE_PRICE] += Double.parseDouble(price.getAmount());

                MedianRegular[i] = Double.parseDouble(price.getAmount());

                String format = sellingMode.getFormat();
                if(format.contains("BUY_NOW")){
                    Values[BUY_NOW] += 1;
                }
                if(format.contains("AUCTION")){
                    Values[AUCTION] += 1;
                }

                Stock stock = regular.getStock();
                //Najwięcej produktów
                if(Values[STOCK_MAX] < stock.getAvailable()){
                    Values[STOCK_MAX] = stock.getAvailable();
                }
                //Średnia cena
                Values[STOCK_AVG] += stock.getAvailable();

                //Rodzaje sprzedawców
                Seller seller = regular.getSeller();
                if(seller.getCompany())
                {
                    Values[SELLER_COMPANY]  += 1;
                }
                else
                {
                    Values[SELLER_PRIVATE] += 1;
                }

                //Koszt dostawy
                Delivery delivery = regular.getDelivery();
                if(delivery.getAvailableForFree())
                {
                    Values[DELIVERY_FREE] += 1;
                }
                else
                {
                    Values[DELIVERY_PAID] += 1;
                }
            }
        }

        Values[COUNTER] = Values[PROMOTED] + Values[REGULAR];

        if(Values[COUNTER]!=0){
            //Mediana
            Median = new Double[Values[COUNTER]];
            if(Values[PROMOTED] != null){
                for(int i = 0; i < Values[PROMOTED]; i++){
                    Median[i] = MedianPromoted[i];
                }
            }
            if(Values[REGULAR] != null){
                for (int i = Values[PROMOTED]; i < Values[COUNTER]; i++){
                    Median[i] = MedianRegular[i - Values[PROMOTED]];
                }
            }
            Arrays.sort(Median);


            if(Values[COUNTER] % 2 == 0){
                int licznik = Values[COUNTER] / 2;
                Prices[MEDIAN] = (Median[licznik-1] + Median[licznik]) / 2;
            }
            else{
                int licznik = (Values[COUNTER] - 1) / 2;
                Prices[MEDIAN] = Median[licznik];
            }
            DecimalFormat df = new DecimalFormat("#.##");
            Prices[MEDIAN] = Double.parseDouble(df.format(Prices[MEDIAN]));

            //Średnia
            Prices[AVERAGE_PRICE] = Prices[AVERAGE_PRICE] / Values[COUNTER];
            df = new DecimalFormat("#.##");
            Prices[AVERAGE_PRICE] = Double.parseDouble(df.format(Prices[AVERAGE_PRICE]));

            Values[STOCK_AVG] = Values[STOCK_AVG] / Values[COUNTER];
        }

        //Ceny do stringa
        String StrValues[] = new String[Prices.length + Values.length];
        for(int i = 0; i < Prices.length; i++){
            StrValues[i] = Prices[i].toString();
        }

        //Liczby do stringa
        for(int i = Prices.length; i < Prices.length + Values.length; i++){
            StrValues[i] = Values[i-Prices.length].toString();
        }

        return StrValues;
    }

    public String[] SetPriceRanges (String[] Values){

        String[] PriceRanges = new String[8];
        Double[] DoublePriceRanges = new Double[8];
        Double[] DoubleValues = new Double[3];
        for(int i = 0; i < 3; i++){
            DoubleValues[i] = Double.parseDouble(Values[i]);
        }
        double temp = (DoubleValues[2] - DoubleValues[0]) / (DoublePriceRanges.length - 1);

        for(int i = 0; i < 8; i++){
            DoublePriceRanges[i] = DoubleValues[0] + i * temp;
        }
        DoublePriceRanges[DoublePriceRanges.length - 1] = DoubleValues[2];

        DecimalFormat df = new DecimalFormat("#.##");

        for(int i = 0; i < DoublePriceRanges.length; i++){
            DoublePriceRanges[i] = Double.parseDouble(df.format(DoublePriceRanges[i]));
        }
        for(int i = 0; i < 8; i++){
            PriceRanges[i] = DoublePriceRanges[i].toString();
        }
        return PriceRanges;
    }

    public int[] CountPriceRanges (Items productList, String[] PriceRanges){

        int[] countedPriceRanges = {0, 0, 0, 0, 0, 0, 0};
        Double[] DoublePriceRanges = new Double[8];
        for(int i =0; i < 8; i++){
            DoublePriceRanges[i] = Double.parseDouble(PriceRanges[i]);
        }

        List<Promoted> promotedList = productList.getPromoted();
        if(promotedList != null){
            for(int i = 0; i < promotedList.size(); i++)
            {
                Promoted promoted = promotedList.get(i);

                SellingMode sellingMode = promoted.getSellingMode();
                //Najmniejsza cena
                Price price = sellingMode.getPrice();
                Double offerPrice = Double.parseDouble(price.getAmount());
                if(offerPrice > DoublePriceRanges[6]){
                    countedPriceRanges[6] ++;
                }
                else if(offerPrice > DoublePriceRanges[5]){
                    countedPriceRanges[5] ++;
                }
                else if(offerPrice > DoublePriceRanges[4]){
                    countedPriceRanges[4] ++;
                }
                else if(offerPrice > DoublePriceRanges[3]){
                    countedPriceRanges[3] ++;
                }
                else if(offerPrice > DoublePriceRanges[2]){
                    countedPriceRanges[2] ++;
                }
                else if(offerPrice > DoublePriceRanges[1]){
                    countedPriceRanges[1] ++;
                }
                else{
                    countedPriceRanges[0]++;
                }
            }
        }

        List<Regular> regularList = productList.getRegular();
        if(regularList != null){
            for(int i = 0; i < regularList.size(); i++)
            {
                Regular regular = regularList.get(i);

                SellingMode sellingMode = regular.getSellingMode();
                //Najmniejsza cena
                Price price = sellingMode.getPrice();
                Double offerPrice = Double.parseDouble(price.getAmount());
                if(offerPrice > DoublePriceRanges[6]){
                    countedPriceRanges[6] ++;
                }
                else if(offerPrice > DoublePriceRanges[5]){
                    countedPriceRanges[5] ++;
                }
                else if(offerPrice > DoublePriceRanges[4]){
                    countedPriceRanges[4] ++;
                }
                else if(offerPrice > DoublePriceRanges[3]){
                    countedPriceRanges[3] ++;
                }
                else if(offerPrice > DoublePriceRanges[2]){
                    countedPriceRanges[2] ++;
                }
                else if(offerPrice > DoublePriceRanges[1]){
                    countedPriceRanges[1] ++;
                }
                else{
                    countedPriceRanges[0]++;
                }
            }
        }

        return countedPriceRanges;
    }
}



