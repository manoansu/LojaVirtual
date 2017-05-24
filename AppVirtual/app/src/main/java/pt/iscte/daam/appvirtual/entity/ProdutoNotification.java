package pt.iscte.daam.appvirtual.entity;

import java.io.Serializable;

/**
 * Created by Diogo on 15/05/2016.
 */
public class ProdutoNotification implements Serializable {

    private int id;

    private String titulo;

    private String msg;

    private String data;

    public ProdutoNotification(int id, String titulo, String msg, String data) {
        this.id = id;
        this.titulo = titulo;
        this.msg = msg;
        this.data = data;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProdutoNotification{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
