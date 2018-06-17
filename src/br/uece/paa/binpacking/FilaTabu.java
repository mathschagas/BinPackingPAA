package br.uece.paa.binpacking;

import java.util.LinkedList;
import java.util.Queue;

public class FilaTabu {
	
	private Queue<SolucaoBPTS> fila;
	private Integer T;
	
	public FilaTabu(Integer t) {
		this.fila = new LinkedList<SolucaoBPTS>();
		this.T = t;
	}

	public boolean contem(SolucaoBPTS vizinhoAtual) {
		for (SolucaoBPTS solucaoAtual : fila) {
			if (solucaoAtual.toString().equals(vizinhoAtual.toString())) {
				return true;
			}
		}
		return false;
	}

	public void adicionar(SolucaoBPTS solucaoAtual) {
		this.fila.add(solucaoAtual);
		if (fila.size() > T) {
			this.fila.poll();
		}
		
	}
	
	

}
