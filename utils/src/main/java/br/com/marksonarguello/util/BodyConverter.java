package br.com.marksonarguello.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;

public final class BodyConverter {

    public static void print() {
        System.out.println("funcionou");
    }
    private BodyConverter() {
    }


    public static <T> T fromJson(BufferedReader bodyReader, Class<T> classOfT) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String body = bodyReader.lines().reduce("", String::concat);

        return gson.fromJson(body, classOfT);
    }

    public static String toJson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }
}
