package practice.day28lesson2.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import practice.day28lesson2.model.Pokemon;
import practice.day28lesson2.repository.PokemonRepository;

@Controller
@RequestMapping("/")
public class PokemonController {
    
    @Autowired
    private PokemonRepository repo;

    @GetMapping("/")
    public String getAllTypes(Model model){
        List<Document> result = repo.getAllTypes();
        List<String> stringList = new ArrayList<>();
        result.stream().forEach(d -> {
            stringList.add(d.getString("_id"));
        });
        model.addAttribute("typeList", stringList);
        return "alltypes";
    }

    @GetMapping("/{type}")
    public String getPokemonByType(@PathVariable String type, Model model){
        List<Document> result = repo.getPokemonByType(type);
        List<Pokemon> pokemonList = new ArrayList<>();
        result.stream().forEach(d ->{
            Pokemon pokemon = new Pokemon(Integer.valueOf(d.getString("id")),d.getString("name"), d.getString("img"));
            pokemonList.add(pokemon);
        });
        
        List<Pokemon> sortedList = pokemonList.stream().sorted(Comparator.comparing(Pokemon::getId).thenComparing(Pokemon::getId)).collect(Collectors.toList());
        model.addAttribute("type", type);
        model.addAttribute("pokemonList", sortedList);
        return "pokemonlist";
    }

}
