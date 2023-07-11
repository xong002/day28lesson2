package practice.day28lesson2;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import practice.day28lesson2.repository.PokemonRepository;

@SpringBootApplication
public class Day28lesson2Application implements CommandLineRunner{

	@Autowired
	private PokemonRepository repo;
	public static void main(String[] args) {
		SpringApplication.run(Day28lesson2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Document> result = repo.getPokemonByType("grass");
		result.stream().forEach(System.out::println);
	}

}
