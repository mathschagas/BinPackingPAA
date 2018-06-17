package br.uece.paa.binpacking;

public class Objeto {
	
	private Integer id;
	private Integer peso;
	
	public Objeto() {
		setId(0);
		setPeso(0);
	}
	
	public Objeto (Integer id, Integer peso) {
		this.setId(id);
		this.setPeso(peso);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public Objeto getCopia() {
		Objeto copia = new Objeto(this.getId(), this.getPeso());
		return copia;
	}
	
	public String toString() {
		String str = "";
		str += getId() + " (" + getPeso() + ")";
		return str;
	}

}
