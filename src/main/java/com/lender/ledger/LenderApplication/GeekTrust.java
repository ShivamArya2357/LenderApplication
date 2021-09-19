package com.lender.ledger.LenderApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeekTrust {

    public static void main(String[] args) {

        String filePath = args[0];
        Factory factory = new Factory();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            List<String> inputs = new ArrayList<>();
            while (line != null) {
                inputs.add(line);
                line = br.readLine();
            }
            factory.callService(inputs);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
