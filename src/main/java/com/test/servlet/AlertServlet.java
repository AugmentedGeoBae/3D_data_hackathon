package com.test.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class FirstServlet
 */

/**
 * @author preetham
 */
public class AlertServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlertServlet() {
        System.out.println("Alert Servlet Constructor called!");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("AlertServlet \"DoGet\" method called");

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "not allowed");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("AlertServlet \"DoPost\" method called");

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading json");
            return;
        }

        try {
            JSONObject jsonObject = HTTP.toJSONObject(jb.toString());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonObject.toString());
            response.getWriter().flush();
            response.getWriter().close();
            return;

        } catch (JSONException e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        }

    }

}
