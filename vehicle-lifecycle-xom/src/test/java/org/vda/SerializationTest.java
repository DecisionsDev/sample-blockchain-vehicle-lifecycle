/*
 *
 *   Copyright IBM Corp. 2017
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.vda;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.acme.vehicle.lifecycle.decision.IsSuspiciousTransferDecisionService;
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
            System.out.println("LOG: Deserializing: " + json);
            IsSuspiciousTransferDecisionService tw = mapper.readValue(json, IsSuspiciousTransferDecisionService.class);
            String json2 = mapper.writeValueAsString(tw);
            System.out.println("LOG: Serializing: " + json2);
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
        	InputStream is = getClass().getResourceAsStream("payload.json");
            return fromStream(is).replaceAll("\\r\\n|\\r|\\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
