package br.com.territory;

import br.com.territory.servico.MenuConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TerritoryPredictiveInsightsApplication {
	public static void main(String[] args) {
		SpringApplication.run(
				TerritoryPredictiveInsightsApplication.class,
				args
		);
	}

	@Bean
	public CommandLineRunner iniciarMenu(MenuConsole menuConsole) {
		return args -> menuConsole.iniciar();
	}
}