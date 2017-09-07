package com.ibm.rules.blockchain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

public class Main {


    private static String getBase64Credentials(String userName, String password) {
        String authstring = userName + ":" + password;
        try {
            return DatatypeConverter.printBase64Binary(authstring.getBytes("ISO8859_1"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return DatatypeConverter.printBase64Binary(authstring.getBytes());
    }

    private Main() throws IOException {
        String username = "resAdmin";
        String password = "resAdmin";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://jrules1.francelab.fr.ibm.com:14090/res/apiauth/v1/ruleapps");

        String base64Credentials = getBase64Credentials(username, password);
        String authHeader = "Basic " + base64Credentials;
        httpPost.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("X-File-Name", "miniloan-rapp.jar");
        File file = new File(this.getClass().getResource("/miniloan-rapp.jar").getFile());

        if (file.exists()) {
            FileBody bin = new FileBody(file);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("body", bin)
                    .build();
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response;
            try {
                response = client.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("Status Code" + response.getStatusLine().getStatusCode() + "");
                System.out.println("Response content " + result);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //      assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
                if (client != null)
                    client.close();
            }
        } else {
            System.out.println("Error file cannot be found");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String thing = "Text to write to the file";
        String dir = Main.class.getResource("/").getFile();
        //String dir = WriteResource.class.getResource("/dir").getFile();
        OutputStream os = new FileOutputStream(dir + "/file.txt");
        final PrintStream printStream = new PrintStream(os);
        printStream.println(thing);
        printStream.close();
    }

    public static void amain(String[] args) throws IOException {
        new Main();
    }
}