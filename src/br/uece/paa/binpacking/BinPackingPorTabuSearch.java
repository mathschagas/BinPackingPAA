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
	private int indiceBinOrigem;
	private int indiceObjetoASerTransferido;
	private Objeto objetoPerturbacao;
	private int indiceBinDestino;
	private boolean perturbacaoRemoveuBinDeOrigem;
	private Integer idBinRemovidoNaPertubacao;
	private Boolean coubeNoDestino;
	private Long timerMarcacaoInicio;
	private Long timerMarcacaoTermino;
	private long tempoMaximo;

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
			solucaoInicial.adicionarObjetoPorFirstFit(objetoAtual);
			// solucaoInicial.adicionarObjetoEmNovoBin(objetoAtual);
		}
	}

	private void atribuirValoresParaExecucaoDaTabuSearch() {
		NMAX = 2 * N; // N <= NMAX <= 3N.
		T = 7; // T = 7 a 10 já resulta em soluções satisfatórias.
		NV = N / 2; // Sugestão do livro, de acorod com o estudo de HERTZ, WERRA (1987)
		filaTabu = new FilaTabu(T);
		tempoMaximo = (long) 600000; // Tempo maximo para execução de instâncias de longa duração.
	}

	private void buscaTabu() {
		melhorSolucao = solucaoInicial.getCopiaDaSolucao();
		SolucaoBPTS solucaoAtual = solucaoInicial.getCopiaDaSolucao();
		filaTabu.adicionar(solucaoAtual);
		Integer i = 0; // indice das iterações
		Integer M = 0; // indice da iteração da melhor solução
		while ((i - M) < NMAX) {
			timerMarcacaoInicio = System.currentTimeMillis();
			Boolean primeiroVizinhoNaoTabuRegistrado = false;
			fazerPerturbacao(solucaoAtual);
			SolucaoBPTS melhorVizinho = solucaoAtual.getCopiaDaSolucao();
			desfazerPertubacao(solucaoAtual);

			for (int j = 1; j < NV; j++) {
				fazerPerturbacao(solucaoAtual);
				SolucaoBPTS vizinhoAtual = solucaoAtual;
				if (!primeiroVizinhoNaoTabuRegistrado) {
					if (!filaTabu.contem(vizinhoAtual)) {
						melhorVizinho = vizinhoAtual.getCopiaDaSolucao();
					}
				} else {
					if (!filaTabu.contem(vizinhoAtual) && vizinhoAtual.getQtdBins() < melhorVizinho.getQtdBins()) {
						melhorVizinho = vizinhoAtual.getCopiaDaSolucao();
					}
				}
				desfazerPertubacao(solucaoAtual);
			}
			solucaoAtual = melhorVizinho.getCopiaDaSolucao();
			filaTabu.adicionar(solucaoAtual);
			timerMarcacaoTermino = System.currentTimeMillis();
			if (melhorSolucao.getQtdBins() > melhorVizinho.getQtdBins()) {
				System.out.println("Achou uma solução melhor! i = " + i + ". Num. de Bins: " + melhorVizinho.getQtdBins() +
						". (Tempo = " + (timerMarcacaoTermino-timerMarcacaoInicio) + ")");
				melhorSolucao = melhorVizinho.getCopiaDaSolucao();
				M = i;
			} else {
				System.out.println("i = " + i + ". (Tempo = " + (timerMarcacaoTermino-timerMarcacaoInicio) + ")");
			}
			i++;
			if (tempoDeTermino-tempoDeInicio > tempoMaximo) {
				break;
			}
		}
	}

	public void fazerPerturbacao(SolucaoBPTS solucao) {

		perturbacaoRemoveuBinDeOrigem = false;
		Random gerador = new Random();

		indiceBinOrigem = gerador.nextInt(solucao.getQtdBins());
		while (solucao.getBin(indiceBinOrigem).getQtdObjetos() == 0) {
			indiceBinOrigem = gerador.nextInt(solucao.getQtdBins());
		}

		indiceObjetoASerTransferido = gerador.nextInt(solucao.getBin(indiceBinOrigem).getQtdObjetos());
		objetoPerturbacao = solucao.getBin(indiceBinOrigem).removerObjeto(indiceObjetoASerTransferido);
		if (solucao.getBin(indiceBinOrigem).getQtdObjetos() == 0) {
			Bin binVazio = solucao.getBin(indiceBinOrigem);
			idBinRemovidoNaPertubacao = binVazio.getId();
			solucao.removeBin(binVazio);
			perturbacaoRemoveuBinDeOrigem = true;
		}

		indiceBinDestino = gerador.nextInt(solucao.getQtdBins());
		coubeNoDestino = solucao.getBin(indiceBinDestino).adicionarObjeto(objetoPerturbacao);
		if (!coubeNoDestino) {
			solucao.adicionarObjetoEmNovoBin(objetoPerturbacao);
		}
	}

	public void desfazerPertubacao(SolucaoBPTS solucao) {
		if (coubeNoDestino) {
			solucao.getBin(indiceBinDestino).removerObjeto(solucao.getBin(indiceBinDestino).getQtdObjetos() - 1);
			if (solucao.getBin(indiceBinDestino).getQtdObjetos() == 0) {
				Bin binVazio = solucao.getBin(indiceBinDestino);
				solucao.removeBin(binVazio);
			}
		} else {
			solucao.getBin(solucao.getQtdBins() - 1)
					.removerObjeto(solucao.getBin(solucao.getQtdBins() - 1).getQtdObjetos() - 1);
			if (solucao.getBin(solucao.getQtdBins() - 1).getQtdObjetos() == 0) {
				Bin binVazio = solucao.getBin(solucao.getQtdBins() - 1);
				solucao.removeBin(binVazio);
			}
		}

		if (perturbacaoRemoveuBinDeOrigem) {
			Bin binDeOrigem = new Bin(idBinRemovidoNaPertubacao, C);
			int j = solucao.getQtdBins() + 1;
			while (j >= indiceBinOrigem) {
				if (j == indiceBinOrigem) {
					solucao.setBinNaPosicao(binDeOrigem, j);
				} else if (j == solucao.getQtdBins() + 1) {
					solucao.adicionarBin(binDeOrigem);
				} else {
					solucao.setBinNaPosicao(solucao.getBin(j - 1), (j));
				}

				j--;
			}
		}

		int i = solucao.getBin(indiceBinOrigem).getQtdObjetos() + 1;
		while (i >= indiceObjetoASerTransferido) {
			if (i == indiceObjetoASerTransferido) {
				solucao.getBin(indiceBinOrigem).setObjetoNaPosicao(objetoPerturbacao, i);
			} else if (i == solucao.getBin(indiceBinOrigem).getQtdObjetos() + 1) {
				solucao.getBin(indiceBinOrigem).adicionarObjeto(objetoPerturbacao);
			} else {
				solucao.getBin(indiceBinOrigem).setObjetoNaPosicao(solucao.getBin(indiceBinOrigem).getObjeto(i - 1),
						(i));
			}
			i--;
		}
	}

	private void imprimeSaida() {
		System.out.println(melhorSolucao.toString());
		System.out.println("Tempo de Execução: " + tempoDeExecucao + "ms.");

	}

}
