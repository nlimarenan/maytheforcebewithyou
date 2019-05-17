package com.game.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.game.dynamodbconfig.DynamoDBConfig;
import com.game.entity.Planet;
import com.game.entity.PlanetApiReceiver;
import com.game.repository.PlanetRepository;
import com.game.request.SavePlanetRequest;
import com.game.response.SavePlanetResponse;
import com.game.response.ShowPlanetResponse;
import com.google.gson.Gson;

@Service
public class PlanetService {	
	
	private final String URL = "https://swapi.co/api/planets/";
	private final String USER_AGENTS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
	
	@Autowired
	PlanetRepository repository;
	
	@Autowired
	HttpServletResponse httpResponse;
	
	public ResponseEntity<SavePlanetResponse> savePlanet(SavePlanetRequest request) {
		
		ResponseEntity<String> planets = allThePlanets();
		String jsonCandidate = planets.getBody();
		JSONObject obj = new JSONObject(jsonCandidate);
		JSONArray array = obj.getJSONArray("results");
		Gson gson = new Gson();
		PlanetApiReceiver [] receiver = gson.fromJson(array.toString(), PlanetApiReceiver[].class);
		
		List<PlanetApiReceiver> receiverList = new ArrayList<>(Arrays.asList(receiver));
		
		Integer qtdAparicoes = receiverList
								.stream()
								.filter(p -> p.getName().equalsIgnoreCase(request.getNome()))
								.findAny()
								.get()
								.getFilms()
								.size();
		
		
		Planet saved = this.repository.save(SavePlanetRequest.convertRequestToEntity(qtdAparicoes,request));
		SavePlanetResponse response = new SavePlanetResponse();
		response.setId(saved.getId());
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	public ResponseEntity<List<ShowPlanetResponse>> showLocalPlanets() {
		
		List<Planet> planetList = new ArrayList<>();
		Iterable<Planet> iterable = this.repository.findAll();
		
		iterable.forEach(planetList::add);		
		List<ShowPlanetResponse> comeToTheDarkSideConversion = planetList.stream().map(Planet::convertEntityToResponse).collect(Collectors.toList());
		return new ResponseEntity<>(comeToTheDarkSideConversion,HttpStatus.OK);
	}

	public ResponseEntity<String> showAllPlanetsFromApi() {		
		
		ResponseEntity<String> result = allThePlanets();		
		
		return result;
	}

	public ResponseEntity<List<ShowPlanetResponse>> showPlanetByName(String planetName) {
		
		DynamoDBMapper mapper = pickMapper();
		
		Map<String, AttributeValue> scanMap = new HashMap<String, AttributeValue>();
		scanMap.put(":value",new AttributeValue().withS(planetName));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
													.withFilterExpression("nome = :value")
													.withExpressionAttributeValues(scanMap);
		
		List<Planet> result = mapper.scan(Planet.class, scanExpression);
		
		List<ShowPlanetResponse> response = result.stream().map(Planet::convertEntityToResponse).collect(Collectors.toList());
		
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
		this.repository.deleteById(id);
	}

	public void destroyPlanetByName(String planetName) {
		
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
		
		if(response == null) {
			httpResponse.setStatus(204);
			
			return;
		}
		this.repository.delete(new Planet());
		
	}
	
	private Table pickTable() {
		DynamoDBConfig dynamoConfig = new DynamoDBConfig();
		DynamoDB db = new DynamoDB(dynamoConfig.amazonDynamoDB());
		Table table = db.getTable("Planetas");
		
		return table;
	}
	
	private DynamoDBMapper pickMapper() {
		DynamoDBMapper mapper = new DynamoDBMapper(new DynamoDBConfig().amazonDynamoDB());
		
		return mapper;
	}
	
	private ResponseEntity<String> allThePlanets(){
		final String uri = URL;
		RestTemplate requestTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", USER_AGENTS);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return requestTemplate.exchange(uri,HttpMethod.GET,entity, String.class);
	}
	
}
