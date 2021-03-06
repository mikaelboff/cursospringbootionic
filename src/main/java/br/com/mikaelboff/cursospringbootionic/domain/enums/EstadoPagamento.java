package br.com.mikaelboff.cursospringbootionic.domain.enums;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"), QUITADO(2, "Quitado"), CANCELADO(3, "Cancelado");

	private int cod;
	public String descricao;

	private EstadoPagamento(int codigo, String descricao) {
		this.cod = codigo;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescrica() {
		return descricao;
	}

	public static EstadoPagamento toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}

		for (EstadoPagamento x : EstadoPagamento.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}

		throw new IllegalArgumentException("Id inválido" + cod);
	}

}
