package com.lender.ledger.LenderApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class LenderApplication implements CommandLineRunner {

	public static final String LENDER_SERVICE_FACTORY = "lenderServiceFactory";
	@Autowired
	private ApplicationContext appContext;

	public static void main(String[] args) {
		SpringApplication.run(LenderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		LenderServiceFactory factory = appContext.getBean(LENDER_SERVICE_FACTORY, LenderServiceFactory.class);
		String filePath = args[0];
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

//	@PostConstruct
//	private void init() {
//
//		LenderServiceFactory factory = new LenderServiceFactory();
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(filePath));
//			String line = br.readLine();
//			List<String> inputs = new ArrayList<>();
//			while (line != null) {
//				inputs.add(line);
//				line = br.readLine();
//			}
//			factory.callService(inputs);
//			br.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
