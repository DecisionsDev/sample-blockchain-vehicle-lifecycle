package org.vda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.vehicle.lifecycle.TransactionWrapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SerializationTest {

    @Test
    public void test1(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = getJsonPayload();

            // System.out.println(json);
            TransactionWrapper tw = mapper.readValue(json, TransactionWrapper.class);


            String json2 = mapper.writeValueAsString(tw);

            //System.out.println("LOG: " + tw.transaction.buyer.firstName);
            System.out.println("LOG: " + json);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private static String fromStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    private String getJsonPayload() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("payload.json");
            return fromStream(is).replaceAll("\\r\\n|\\r|\\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
