package com.game.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.game.entity.Planet;
import com.game.repository.PlanetRepository;
import com.game.request.SavePlanetRequest;
import com.game.response.SavePlanetResponse;
import com.game.response.ShowPlanetResponse;

@Service
public class PlanetService {	
	
	@Autowired
	PlanetRepository repository;
	
	public SavePlanetResponse savePlanet(SavePlanetRequest request) {
		Planet saved = repository.save(new Planet());
		SavePlanetResponse response = new SavePlanetResponse();
		response.setId(saved.getId());
		return response;
	}

	public List<ShowPlanetResponse> showLocalPlanets() {
		
	
		return null;
	}

	public ResponseEntity<String> showAllPlanets(Integer pageNumber) {
		
		final String uri = "https://swapi.co/api/planets/?page="+pageNumber;
		RestTemplate requestTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = requestTemplate.exchange(uri,HttpMethod.GET,entity, String.class);
		
		
		return result;
	}

	public ShowPlanetResponse showPlanetByName(String planetName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ShowPlanetResponse showPlanetById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ShowPlanetResponse destroyPlanetById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ShowPlanetResponse destroyPlanetByName(String planetName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponseEntity<String> showPlanet(Integer id) {
		final String uri = "https://swapi.co/api/planets/"+id;
		RestTemplate requestTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = requestTemplate.exchange(uri,HttpMethod.GET,entity, String.class);
		
		
		return result;
		
	}

	
	
}
