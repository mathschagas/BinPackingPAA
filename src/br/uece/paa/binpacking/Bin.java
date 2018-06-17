package br.uece.paa.binpacking;

import java.util.ArrayList;

public class Bin {
	
	static private Integer _id = 0;
	private Integer id;
	private Integer capacidade;
	private ArrayList<Objeto> listaDeObjetos;
	
	public Bin(Integer C) {
		setId(_id++);
		setCapacidade(C);
		listaDeObjetos = new ArrayList<Objeto>();
	}
	
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
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
			return false;
		}
		this.listaDeObjetos.add(obj);
		return true;
	}
	
	public Objeto getObjeto(Integer indice) {
		return this.listaDeObjetos.get(indice);
	}
	
	
	public Integer getQtdObjetos() {
		return this.listaDeObjetos.size();
	}
	
	public Bin getCopia() {
		Bin copia = new Bin(getId(), getCapacidade());
		for (Objeto obj : listaDeObjetos) {
			copia.adicionarObjeto(obj.getCopia());
		}
		return copia;
	}

	public String stringObjetosInseridos() {
		String str = "";
		for (int i = 0; i < listaDeObjetos.size(); i++) {
			str += listaDeObjetos.get(i).getId() + " (" + listaDeObjetos.get(i).getPeso() + ")";
			if (i == (listaDeObjetos.size()-1)) {
				str += ". | ";
				str += "ESPACO LIVRE (PERDA): " + this.espacoLivre() + ".";
			} else {
				str += ", ";
			}
			
		}
		return str;
	}

	public Objeto removerObjeto(Integer indiceObjetoDaPerturbacao) {
		Objeto objetoASerRemovido = listaDeObjetos.get(indiceObjetoDaPerturbacao);
		listaDeObjetos.remove(objetoASerRemovido);
		return objetoASerRemovido;
	}
	
	public void setObjetoNaPosicao(Objeto objeto, Integer posicao) {
		if (posicao == listaDeObjetos.size()) {
			listaDeObjetos.add(objeto);
		}
		listaDeObjetos.set(posicao, objeto);
	}

}
