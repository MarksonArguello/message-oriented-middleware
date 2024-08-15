package br.com.marksonarguello.connect;

import br.com.marksonarguello.util.BodyConverter;
import br.com.marksonarguello.util.ParameterStringBuilder;
import jakarta.ws.rs.HttpMethod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    public <T> T request(URI uri, Class<T> classOfT) throws IOException {
        return this.request(uri, null, classOfT);
    }

    public <T> T request(URI uri, Map<String, String> parameters, Class<T> classOfT) throws IOException {
        URL url = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(HttpMethod.GET);


        BufferedReader response = this.connect(con, parameters);

        return BodyConverter.fromJson(response, classOfT);
    }

    private BufferedReader connect(HttpURLConnection con, Map<String, String> parameters) throws IOException {
        if (parameters != null && !parameters.isEmpty()) {
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());

            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();
        }

        return new BufferedReader(
                new InputStreamReader(con.getInputStream()));
    }


}
