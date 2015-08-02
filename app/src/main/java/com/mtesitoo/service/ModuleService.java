package com.mtesitoo.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mtesitoo.model.Product;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jackwu on 2015-07-18.
 */
public class ModuleService {

    // TODO: Breakdown this module and integrate with the existing bridge component

    public static ArrayList<Product> getlatest() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://104.155.84.193/api/v1/module/latest");
        ArrayList<Product> products = new ArrayList<>();

        HttpResponse response;
        request.addHeader("Authorization", "Bearer b8yeOzMNPM4dhMQD7lTeQZCh2CKha8uMqnTJ12w4");

        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(result.toString(), JsonElement.class)
                    .getAsJsonObject().get("products").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                String price = jsonObject.get("price").toString().replace("\"", "");
                String name = jsonObject.get("name").toString().replace("\"", "");
                String description = jsonObject.get("description").toString()
                        .replace("\"", "")
                        .replace("\\r", "")
                        .replace("\\t", "")
                        .replace("\\n", "");

                Product product = new Product(name, description, "Location", "Category", "SI Unit", price, 100);
                products.add(product);
            }

        } catch (ClientProtocolException protocolException) {
        } catch (IOException ioException) {
        }

        return products;
    }
}
