package com.game.request;

public class SavePlanetRequest {
	private String nome;
	private String clima;
	private String terreno;
	private Integer qtdAparicoes;
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
	public Integer getQtdAparicoes() {
		return qtdAparicoes;
	}
	public void setQtdAparicoes(Integer qtdAparicoes) {
		this.qtdAparicoes = qtdAparicoes;
	}	
}
