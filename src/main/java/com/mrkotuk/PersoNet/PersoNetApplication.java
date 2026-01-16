package com.mrkotuk.PersoNet;

import com.mrkotuk.PersoNet.config.PathConfig;
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
				.filename(PathConfig.DOT_ENV)
				.ignoreIfMissing()
				.load();

		setPropertyIfExists(dotenv, "MYSQL_ROOT_PASSWORD");
		setPropertyIfExists(dotenv, "SPRING_DATASOURCE_URL");
		setPropertyIfExists(dotenv, "SPRING_DATASOURCE_PASSWORD");
		setPropertyIfExists(dotenv, "SPRING_MAIL_PASSWORD");
		setPropertyIfExists(dotenv, "GOOGLE_DRIVE_CREDENTIALS_PATH");
		setPropertyIfExists(dotenv, "GOOGLE_DRIVE_FOLDER_ID");
		setPropertyIfExists(dotenv, "GOOGLE_CLIENT_ID");
		setPropertyIfExists(dotenv, "GOOGLE_CLIENT_SECRET");
		setPropertyIfExists(dotenv, "GITHUB_CLIENT_ID");
		setPropertyIfExists(dotenv, "GITHUB_CLIENT_SECRET");
	}

	private static void setPropertyIfExists(Dotenv dotenv, String key) {
		String value = dotenv.get(key);
		if (value != null) {
			System.setProperty(key, value);
		}
	}
}
