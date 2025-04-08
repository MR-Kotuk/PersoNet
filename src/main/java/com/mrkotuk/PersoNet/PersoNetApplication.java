package com.mrkotuk.PersoNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PersoNetApplication {
	public static void main(String[] args) {
		dotenv();
		SpringApplication.run(PersoNetApplication.class, args);
	}

	private static void dotenv() {
		Dotenv dotenv = Dotenv.configure()
				.filename(".env")
				.load();

		System.setProperty("MYSQL_ROOT_PASSWORD", dotenv.get("MYSQL_ROOT_PASSWORD"));
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));

		System.setProperty("GOOGLE_DRIVE_CREDITALS_PATH", dotenv.get("GOOGLE_DRIVE_CREDITALS_PATH"));
		System.setProperty("GOOGLE_DRIVE_FOLDER_ID", dotenv.get("GOOGLE_DRIVE_FOLDER_ID"));

		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));

		System.setProperty("GITHUB_CLIENT_ID", dotenv.get("GITHUB_CLIENT_ID"));
		System.setProperty("GITHUB_CLIENT_SECRET", dotenv.get("GITHUB_CLIENT_SECRET"));
	}
}
