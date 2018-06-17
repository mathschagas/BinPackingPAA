package br.uece.paa.binpacking;

import java.util.ArrayList;

public class SolucaoBPTS {
	
	private String nomeInstancia;
	private ArrayList<Bin> listaBins;
	private Integer capacidadeBins;
	
	public SolucaoBPTS(String nomeInstancia, Integer capacidade) {
		this.nomeInstancia = nomeInstancia;
		this.capacidadeBins = capacidade;
		this.listaBins = new ArrayList<Bin>();
	}

	public void adicionarObjetoPorFirstFit(Objeto objeto) {
		Boolean adicionado = false;
		for (int i = 0; i < listaBins.size(); i++) {
			adicionado = listaBins.get(i).adicionarObjeto(objeto);
			if (adicionado) break;
		}
		if(!adicionado) {
			adicionarObjetoEmNovoBin(objeto);
		}
	}
	
	public void adicionarObjetoEmNovoBin(Objeto objeto) {
		Bin novoBin = new Bin(capacidadeBins);
		novoBin.adicionarObjeto(objeto);
		listaBins.add(novoBin);
	}
	
	@Override
	public String toString() {
		String str = "";
		str += "Solução da instância: " + nomeInstancia + "\n";
		str += "----------------------------------------------\n";
		for (int i = 0; i < listaBins.size(); i++) {
			Bin binAtual = listaBins.get(i);
			str += "BIN ("+ "id = " + binAtual.getId() + ") nº " + (i) + ": ";
			str += binAtual.stringObjetosInseridos();
			str += "\n";
		}
		return str;
	}

//	public void fazerPerturbacao() {
//
//	}

	public Bin getBin(Integer indiceBin) {
		return listaBins.get(indiceBin);
	}

	public SolucaoBPTS getCopiaDaSolucao() {
		SolucaoBPTS copia = new SolucaoBPTS(nomeInstancia, capacidadeBins);
		for (Bin bin : listaBins) {
			copia.adicionarBin(bin.getCopia());
		}
		return copia;
	}

	public void adicionarBin(Bin bin) {
		listaBins.add(bin);
	}

	public int getQtdBins() {
		return listaBins.size();
	}

	public void removeBin(Bin binVazio) {
		listaBins.remove(binVazio);
	}
	
	public void setBinNaPosicao(Bin bin, Integer posicao) {
		listaBins.set(posicao, bin);
	}

}
