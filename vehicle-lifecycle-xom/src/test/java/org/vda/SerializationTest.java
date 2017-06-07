package org.vda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SerializationTest {

    @Test
    public void test1(){
        VehicleTransaction vt = new VehicleTransaction();
        vt.transactionId = "TestVehicleId";
        vt.vehicle = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(vt);
            VehicleTransaction vt2 = mapper.readValue(json, VehicleTransaction.class);

            Assert.assertTrue(vt.transactionId.equals(vt2.transactionId));

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
