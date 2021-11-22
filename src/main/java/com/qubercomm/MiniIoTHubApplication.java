package com.qubercomm;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniIoTHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniIoTHubApplication.class, args);
		try {
	         File file = new File("h2db.mv.db");
	         file.createNewFile();
	         System.out.println("File created: " + file);
	         file.deleteOnExit();
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}

}
