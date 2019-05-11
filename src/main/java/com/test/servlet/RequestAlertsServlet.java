package com.test.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 * Servlet implementation class FirstServlet
 */

/**
 * @author preetham
 */
public class RequestAlertsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestAlertsServlet() {
        System.out.println("Request Alerts Servlet Constructor called!");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("RequestAlertsServlet \"DoGet\" method called");

        DocumentBuilderFactory factory;
        DocumentBuilder builder;

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error with XML");
            return;
        }

        // TODO: ENTER USER PASS
        String userPass = "#######REPLACE_THIS########";
        URL url = new URL("https://safezone.criticalarc.net/api/safezones/ukpubsecdemo.criticalarc.com/alerts");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(60000);
        con.setUseCaches(false);

        String encoded = Base64.encode(userPass.getBytes());
        String authorization = "Basic " + encoded;
        con.setRequestProperty("Authorization", authorization);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        in.close();

        String responseValue;

        Map<String, Map<String, Object>> jsonMap = new HashMap<>();

        try {
            JSONObject alertsObject = XML.toJSONObject(responseBuffer.toString());

            JSONArray alertsArray = alertsObject.getJSONObject("alerts").getJSONArray("alert");

            for (Object alertObj : alertsArray) {
                Map<String, Object> alertMap = new HashMap<>();

                JSONObject alert = (JSONObject) alertObj;

                JSONObject geoloc = (JSONObject) alert.get("geoloc");

                String lat = "";
                String lon = "";
                String alt = "2";

                if (geoloc.has("lon")) {
                    lon = geoloc.get("lon").toString();
                }

                if (geoloc.has("lat")) {
                    lat = geoloc.get("lat").toString();
                }
                if (geoloc.has("alt")) {
                    alt = geoloc.get("alt").toString();
                }

                alertMap.put("lat", lat);
                alertMap.put("lon", lon);
                alertMap.put("alt", alt);

                String state = "unknown";
                if (alert.has("state")) {
                    state = alert.get("state").toString();
                }
                alertMap.put("state", state);

                if (alert.has("raiser")) {
                    if (alert.optJSONObject("raiser") != null) {
                        JSONObject raiser = alert.getJSONObject("raiser");
                        alertMap.put("raiser", raiser.toMap());
                    } else if (alert.optJSONArray("raiser") != null) {
                        JSONObject raiser = alert.getJSONArray("raiser").getJSONObject(1);
                        alertMap.put("raiser", raiser.toMap());
                    }
                }

                jsonMap.put(alert.get("id").toString(), alertMap);
            }

            ObjectMapper objectMapper = new ObjectMapper();

            responseValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);

            // responseValue = xmlJSONObj.toString();

        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error with parsing XML: " + ex.getLocalizedMessage());
            ex.printStackTrace();
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(responseValue);
        return;
    }
}
