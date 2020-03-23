package com.uk.location.activity;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkHelper extends AsyncTask<String, String, String> {

    public NetworkHelper() {
    }

    public AsyncTask<String, String, String> NetworkTest(String userName){
        return this.execute("POST", "https://covid-19.dsi.ic.ac.uk/simple_webapp/rest/authentication/login", "{\"username\":\"" + userName + "\",\"password\":\"\"}", "");
    }

    public AsyncTask<String, String, String> CallAPI(String method, String url_suffix, String data, String token) {
        return this.execute(method, "https://covid-19.dsi.ic.ac.uk/simple_webapp/rest/" + url_suffix, data, token);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String urlString = params[1];
        String data = params[2];
        String token = params[3];
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            if (params.length == 4 && token != null && (!token.equals(""))) {
                con.setRequestProperty("Authorization","Bearer " + token);
            }

            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){e.printStackTrace();}

            int responseCode = con.getResponseCode();
            System.out.println("DDDPOST Response Code :  " + responseCode);
            System.out.println("DDDPOST Response Message : " + con.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK || responseCode<300) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                } in .close();
                System.out.println("DDD"+response.toString());
            } else {
                System.out.println("DDD"+response.toString());
                System.out.println("DDDPOST NOT WORKED");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}