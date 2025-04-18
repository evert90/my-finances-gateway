package br.dev.projects.myfinances_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@SpringBootApplication
public class MyfinancesGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyfinancesGatewayApplication.class, args);
	}

}
