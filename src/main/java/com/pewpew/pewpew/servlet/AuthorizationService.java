package com.pewpew.pewpew.servlet;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseManager.errorResponse("Error reading input stream", response);
            return;
        }
        Gson gson = new Gson();
        try {
            User authUser = gson.fromJson(jsonBuffer.toString(), User.class);

            if (!Validate.userAuth(authUser)) {
                ResponseManager.errorResponse("Some fiels is missing", response);
                return;
            }
            User user = MongoManager.getUser(authUser.getEmail(), authUser.getPassword());
            if (user == null) {
                ResponseManager.errorResponse("Wrong email or password", response);
                return;
            }

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("token", user.getToken());
            String stringResponse = gson.toJson(jsonResponse);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(stringResponse);
        } catch (JsonSyntaxException error) {
            System.err.println(error);
            ResponseManager.errorResponse("Cannot serilized Json", response);
        }
    }
}
