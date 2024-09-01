package br.com.marksonarguello.connect;

import br.com.marksonarguello.util.BodyConverter;
import br.com.marksonarguello.util.ParameterStringBuilder;
import jakarta.ws.rs.HttpMethod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    public <T> T request(URI uri, Map<String, String> parameters, Class<T> classOfT) throws IOException {
        return this.request(uri, parameters, classOfT, HttpMethod.GET, null);
    }

    public <T> T request(URI uri, Map<String, String> parameters, Object body) throws IOException {
        return this.request(uri, parameters, null, HttpMethod.POST, body);
    }

    public  <T> T request(URI uri, Map<String, String> parameters, Class<T> classOfT, String method, Object body) throws IOException {
        URL url = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);

        if (body != null && method.equals(HttpMethod.POST)) {
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(BodyConverter.toJson(body));
        }

        BufferedReader response = this.connect(con, parameters);

        if (classOfT == null)
            return null;

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
