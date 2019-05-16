package com.game.response;

public class ShowPlanetResponse {
	private String nome;
	private String clima;
	private String terreno;
	private String qtdAparicoes;
	
	public ShowPlanetResponse(String nome, String clima, String terreno, String qtdAparicoes) {
		this.nome = nome;
		this.clima = clima;
		this.terreno = terreno;
		this.qtdAparicoes = qtdAparicoes;

	}
	
	public ShowPlanetResponse() {
		super();
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClima() {
		return clima;
	}
	public void setClima(String clima) {
		this.clima = clima;
	}
	public String getTerreno() {
		return terreno;
	}
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	public String getQtdAparicoes() {
		return qtdAparicoes;
	}
	public void setQtdAparicoes(String qtdAparicoes) {
		this.qtdAparicoes = qtdAparicoes;
	}
}

