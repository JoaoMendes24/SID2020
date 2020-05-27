package inserirNoSQL;

public enum EstadoSistema {
	
	ESTAVEL(0,"Temperatura normal"),SUBIDA(1,"Subida de temperatura pouco grave"),
	PERTO_LIMITE(2,"Temperatura próxima do limite"),ACIMA_LIMITE(3,"Temperatura acima do limite");
	
	private int nivel_alerta;
	private String titulo_alerta;
	
	EstadoSistema(int nivel_alerta, String titulo_alerta){
		this.nivel_alerta=nivel_alerta;
		this.titulo_alerta=titulo_alerta;
		
	}
	
	public int getNivel() {
		return nivel_alerta;
	}
	
	public String getTitulo() {
		return titulo_alerta;
	}

}
