package br.uece.paa.binpacking;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BinPackingPorTabuSearch {

	private Integer N;
	private Integer C;
	private ArrayList<Integer> pesos;

	public BinPackingPorTabuSearch() {
		N = 0;
		C = 0;
		pesos = new ArrayList<Integer>();
	}

	public void executar() {
		lerArquivoDeEntradas();

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
	}

}
