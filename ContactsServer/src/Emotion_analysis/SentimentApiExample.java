package Emotion_analysis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.transform.Templates;

import org.apache.bcel.generic.RETURN;
import org.apache.commons.lang3.math.Fraction;
import org.json.JSONArray;
import org.json.JSONException;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SentimentApiExample
{
    public static final String SENTIMENT_URL =
        "http://api.bosonnlp.com/sentiment/analysis";

    public static void main(String[] args) throws JSONException, UnirestException,
                                                  java.io.IOException
    {
        //String body = new JSONArray(new String[]{"他是个傻逼", "美好的世界"}).toString();
        String body = new JSONArray(new String[]{"否定词"}).toString();
        HttpResponse<JsonNode> jsonResponse = Unirest.post(SENTIMENT_URL)
                .header("Accept", "application/json")
                .header("X-Token", "QYdRdKZN.4094.0LmppHcluq1S")
                .body(body)
                .asJson();

        System.out.println(jsonResponse.getBody());

        // Unirest starts a background event loop and your Java
        // application won't be able to exit until you manually
        // shutdown all the threads
        Unirest.shutdown();
    }
}
