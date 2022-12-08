package fr.enseeiht.sulm;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RequestInterface {
    /*
    body : latlng
    lien : ip + /api/location
    */
    static LatLng pos;

    public static class networkThread implements Runnable {
        public void run() {
            //ping();
            send();
        }


    }

    public static void sendRequest(LatLng latLng) {
        pos = latLng;
        Thread t = new Thread(new networkThread());
        t.start();




    }

    public static void send()
    {

        try {
        HashMap values = new HashMap<String, String>() {{
            put("latitude", ""+pos.latitude);
            put ("longitude", ""+pos.longitude);
        }};


        URL url = new URL("http://172.20.10.3:3000/api/location");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        String urlParameters  = "param1=a&param2=b&param3=c";
        urlParameters = "latitude=" + pos.latitude + "&longitude=" + pos.longitude;
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        con.setDoOutput( true );
        con.setInstanceFollowRedirects( false );
        con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty( "charset", "utf-8");
        con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        con.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write( postData );
        } catch(Exception e) {}

            if (con.getResponseCode() != 0) {
                Log.d("ping",con.getResponseCode()+"");
            }


    } catch (Exception e) {
        e.printStackTrace();}
    }

    public static void ping()
    {
        try {
            //URL url = new URL("http://" + "www.google.com");
            URL url = new URL("http://172.20.10.3");


            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application:");//+Z.APP_VERSION);
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 30); // Timeout is in seconds
            urlc.connect();

            if (urlc.getResponseCode() == 200) {
                Log.d("ping","getResponseCode == 200");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
