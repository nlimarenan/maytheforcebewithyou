package com.game.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.game.dynamodbconfig.DynamoDBConfig;
import com.game.entity.Planet;
import com.game.repository.PlanetRepository;
import com.game.request.SavePlanetRequest;
import com.game.response.SavePlanetResponse;
import com.game.response.ShowPlanetResponse;

@Service
public class PlanetService {	
	
	@Autowired
	PlanetRepository repository;
	
	public ResponseEntity<SavePlanetResponse> savePlanet(SavePlanetRequest request) {
		
		//todo implementar logica de pegar a quantidade de vezes de aparicoes nos filmes
		Planet saved = repository.save(new Planet());
		SavePlanetResponse response = new SavePlanetResponse();
		response.setId(saved.getId());
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	public ResponseEntity<List<ShowPlanetResponse>> showLocalPlanets() {
		
		List<Planet> planetList = new ArrayList<>();
		Iterable<Planet> iterable = repository.findAll();
		
		iterable.forEach(planetList::add);		
		List<ShowPlanetResponse> comeToTheDarkSideConversion = planetList.stream().map(Planet::convertEntityToResponse).collect(Collectors.toList());
		return new ResponseEntity<>(comeToTheDarkSideConversion,HttpStatus.OK);
	}

	public ResponseEntity<String> showAllPlanetsFromApi(Integer pageNumber) {
		
		//TODO por strings em final
		
		final String uri = "https://swapi.co/api/planets/?page="+pageNumber;
		RestTemplate requestTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = requestTemplate.exchange(uri,HttpMethod.GET,entity, String.class);
		
		
		return result;
	}

	public ResponseEntity<ShowPlanetResponse> showPlanetByName(String planetName) {
		
		Table table = pickTable();
		
		QuerySpec spec = new QuerySpec();
		spec.withKeyConditionExpression("nome = :_nome")
			.withValueMap(new ValueMap().withString("_nome", planetName));
		
		ItemCollection<QueryOutcome> planet = table.query(spec);
		Iterator<Item> it = planet.iterator();
		Item item = null;
		
		ShowPlanetResponse response = null;
		
		while (it.hasNext()) {
			
		    item = it.next();
		    
		    response = new ShowPlanetResponse();
		    
		    response.setNome(String.valueOf(item.get("nome")));
		    response.setClima(String.valueOf(item.get("clima")));
		    response.setQtdAparicoes(String.valueOf(item.get("qtdAparicoes")));
		    response.setTerreno(String.valueOf(item.get("terreno")));
		}
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	public ResponseEntity<ShowPlanetResponse> showPlanetById(String id) {
		Optional<Planet>planet = this.repository.findById(id);
		
		if(planet.isPresent()) {
			ShowPlanetResponse response = Planet.convertEntityToResponse(planet.get());
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
		
	}

	public void destroyPlanetById(String id) {
		repository.deleteById(id);
	}

	public void destroyPlanetByName(String planetName) {
		
		Table table = pickTable();
		
		repository.delete(new Planet());
		
	}

	public ResponseEntity<String> showPlanetFromApi(Integer id) {
		
		final String uri = "https://swapi.co/api/planets/"+id;
		RestTemplate requestTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = requestTemplate.exchange(uri,HttpMethod.GET,entity, String.class);
		
		
		return result;
		
	}
	
	private Table pickTable() {
		DynamoDBConfig dynamoConfig = new DynamoDBConfig();
		DynamoDB db = new DynamoDB(dynamoConfig.amazonDynamoDB());
		Table table = db.getTable("PLANET");
		
		return table;
	}
	
}
