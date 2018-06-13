package br.uece.paa.binpacking;

import java.util.ArrayList;

public class Bin {
	
	private Integer id;
	private Integer capacidade;
	private ArrayList<Objeto> listaDeObjetos;
	
	public Bin(Integer id, Integer C) {
		setId(id);
		setCapacidade(C);
		listaDeObjetos = new ArrayList<Objeto>();
	}

	public Integer getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}
	
	public Boolean isEmpty() {
		return listaDeObjetos.isEmpty();
	}
	
	public Integer espacoLivre() {
		Integer espacoLivre = this.getCapacidade();
		for (Objeto obj : listaDeObjetos) {
			espacoLivre -= obj.getPeso();
		}
		return espacoLivre;
	}
	
	public Boolean adicionarObjeto(Objeto obj) {
		if (obj.getPeso() > this.espacoLivre()) {
			System.out.println("Não é possível adicionar o objeto");
			return false;
		}
		
		this.listaDeObjetos.add(obj);
		return true;
	}
	
	public Boolean removerObjeto(Objeto obj) {
		return this.listaDeObjetos.remove(obj);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
