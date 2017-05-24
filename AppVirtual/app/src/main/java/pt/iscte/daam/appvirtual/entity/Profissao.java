package pt.iscte.daam.appvirtual.entity;

/**
 * Created by amane on 06/05/2017.
 */
public class Profissao {

	private int codProfissao;

	private String descricao;
	
	private String subDescricao;
	
	private String urlImg;

	public int getCodProfissao() {
		return codProfissao;
	}

	public void setCodProfissao(int codProfissao) {
		this.codProfissao = codProfissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSubDescricao() {
		return subDescricao;
	}

	public void setSubDescricao(String subDescricao) {
		this.subDescricao = subDescricao;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

}
