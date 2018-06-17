package br.uece.paa.binpacking;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BinPackingPorTabuSearch {

	private Integer N; // Numero de itens da instancia
	private Integer C; // Capacidade do bin
	private Integer NMAX; // Maximo de iterações
	private Integer NV; // Tamanho da vizinhança
	private Integer T; // Tamanho da Fila Tabu
	private ArrayList<Integer> pesos;
	private String nomeInstancia;
	private SolucaoBPTS solucaoInicial;
	private SolucaoBPTS melhorSolucao;
	private FilaTabu filaTabu;
	private Long tempoDeExecucao;
	private Long tempoDeInicio;
	private Long tempoDeTermino;


	public BinPackingPorTabuSearch() {
		N = 0;
		C = 0;
		pesos = new ArrayList<Integer>();
	}

	public void executar() {
		lerArquivoDeEntradas();
		tempoDeInicio = System.currentTimeMillis();
		gerarSolucaoInicial();
		atribuirValoresParaExecucaoDaTabuSearch();
		buscaTabu();
		tempoDeTermino = System.currentTimeMillis();
		tempoDeExecucao = tempoDeTermino - tempoDeInicio;
		imprimeSaida();
	}

	private void lerArquivoDeEntradas() {

		List<String> linhasArquivo = new ArrayList<String>();
		String nomeArquivo = "";

		// LEITURA DO ARQUIVO
		Scanner reader = new Scanner(System.in);
		System.out.print("DIGITE O NOME DO ARQUIVO: ");
		nomeArquivo = reader.nextLine();
		if (nomeArquivo.endsWith(".txt")) {
			nomeArquivo.substring(0, nomeArquivo.length() - 4);
		}
		try {
			linhasArquivo = Files.readAllLines(Paths.get(".\\src\\instancias\\" + nomeArquivo.toLowerCase() + ".txt"),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();

		// REMOVE ESPAÇOS EXTRAS, CASO EXISTAM
		int indice = 0;
		for (int j = 0; j < linhasArquivo.size(); j++) {
			linhasArquivo.set(indice, linhasArquivo.get(indice++).trim().replaceAll(" +", " "));
		}

		// PERCORRE LINHAS DO ARQUIVO DA INSTANCIA ATRIBUINDO VALORES PARA AS VARIAVEIS
		// DE ENTRADA
		for (int i = 0; i < linhasArquivo.size(); i++) {
			String[] valoresLinhaAtual = linhasArquivo.get(i).split(" ");
			if (i == 0) {
				N = Integer.parseInt(valoresLinhaAtual[0]);
				C = Integer.parseInt(valoresLinhaAtual[1]);
				for (int j = 2; j < valoresLinhaAtual.length; j++) {
					pesos.add(Integer.parseInt(valoresLinhaAtual[j]));
				}
			} else {
				for (int j = 0; j < valoresLinhaAtual.length; j++) {
					pesos.add(Integer.parseInt(valoresLinhaAtual[j]));
				}
			}
		}

		nomeInstancia = nomeArquivo;
	}

	private void gerarSolucaoInicial() {
		solucaoInicial = new SolucaoBPTS(nomeInstancia, C);
		for (int i = 0; i < pesos.size(); i++) {
			Objeto objetoAtual = new Objeto(i + 1, pesos.get(i));
//			solucaoInicial.adicionarObjetoPorFirstFit(objetoAtual);
			solucaoInicial.adicionarObjetoEmNovoBin(objetoAtual);
		}
	}

	private void atribuirValoresParaExecucaoDaTabuSearch() {
		NMAX = 2 * N; // N <= NMAX <= 3N.
		T = 7; // T = 7 a 10 já resulta em soluções satisfatórias.
		NV = N / 2; // Sugestão do livro, de acorod com o estudo de HERTZ, WERRA (1987)
		filaTabu = new FilaTabu(T);
	}

	private void buscaTabu() {
		melhorSolucao = solucaoInicial.getCopiaDaSolucao();
		SolucaoBPTS solucaoAtual = solucaoInicial.getCopiaDaSolucao();
		filaTabu.adicionar(solucaoAtual);
		Integer i = 0; // indice das iterações
		Integer M = 0; // indice da iteração da melhor solução
		while ((i - M) < NMAX) {
			SolucaoBPTS primeiroVizinho = solucaoAtual.getCopiaDaSolucao();
			fazerPerturbacao(primeiroVizinho);
			SolucaoBPTS melhorVizinho = primeiroVizinho;
			for (int j = 1; j < NV; j++) {
				SolucaoBPTS vizinhoAtual = solucaoAtual.getCopiaDaSolucao();
				fazerPerturbacao(vizinhoAtual);
				if (!filaTabu.contem(vizinhoAtual) && vizinhoAtual.getQtdBins() < melhorVizinho.getQtdBins()) {
					melhorVizinho = vizinhoAtual;
				}
			}
			solucaoAtual = melhorVizinho;
			filaTabu.adicionar(solucaoAtual);
			if (melhorSolucao.getQtdBins() > melhorVizinho.getQtdBins()) {
				System.out.println("Achou uma solução melhor! i = " + i + ". Num. de Bins: " + melhorVizinho.getQtdBins());
				melhorSolucao = melhorVizinho;
				M = i;
			} else {
				System.out.println("i = " + i);
			}
			i++;
		}
	}

	public void fazerPerturbacao(SolucaoBPTS vizinhoAtual) {
		Random gerador = new Random();

		Integer indiceBinOrigem = gerador.nextInt(vizinhoAtual.getQtdBins());
		while (vizinhoAtual.getBin(indiceBinOrigem).getQtdObjetos() == 0) {
			indiceBinOrigem = gerador.nextInt(vizinhoAtual.getQtdBins());
		}

		Integer indiceObjetoASerTransferido = gerador.nextInt(vizinhoAtual.getBin(indiceBinOrigem).getQtdObjetos());
		Objeto objetoPerturbacao = vizinhoAtual.getBin(indiceBinOrigem).removerObjeto(indiceObjetoASerTransferido);
		if (vizinhoAtual.getBin(indiceBinOrigem).getQtdObjetos() == 0) {
			Bin binVazio = vizinhoAtual.getBin(indiceBinOrigem);
			vizinhoAtual.removeBin(binVazio);
		}

		Integer indiceBinDestino = gerador.nextInt(vizinhoAtual.getQtdBins());
		Boolean coubeNoDestino = vizinhoAtual.getBin(indiceBinDestino).adicionarObjeto(objetoPerturbacao);
		if (!coubeNoDestino) {
			vizinhoAtual.adicionarObjetoEmNovoBin(objetoPerturbacao);
		}

	}

	private void imprimeSaida() {
		System.out.println(melhorSolucao.toString());
		System.out.println("Tempo de Execução: " + tempoDeExecucao + "ms.");

	}

}
