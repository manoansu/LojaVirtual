package pt.iscte.daam.appvirtual.entity;

/**
 * Created by amane n 06/05/2017.
 */

import java.math.BigDecimal;

public class Produto {

	private int id;

	private String titulo;

	private String descricao;

	private BigDecimal valor;

	private String urlImg;

	private String sku;

	public Produto(int id, String titulo, String descricao, BigDecimal valor, String urlImg, String sku) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.valor = valor;
		this.urlImg = urlImg;
		this.sku = sku;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
