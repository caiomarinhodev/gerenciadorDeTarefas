package models;

import javax.persistence.*;

/**
 * Created by Caio on 25/03/2015.
 */
@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String titulo;
    @Column
    private String data;
    @Column
    private String hora;
    @Column
    private Long idAutor;
    @ManyToOne
    private Usuario autor;
    @Column
    private int status;

    public Task(String titulo, String data, String hora, Usuario autor, int status) {
        this.titulo = titulo;
        this.data = data;
        this.hora = hora;
        this.autor = autor;
        this.status = status;
        this.idAutor = autor.getId();
    }

    public Task(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (status != task.status) return false;
        if (data != null ? !data.equals(task.data) : task.data != null) return false;
        if (hora != null ? !hora.equals(task.hora) : task.hora != null) return false;
        if (!idAutor.equals(task.idAutor)) return false;
        if (titulo != null ? !titulo.equals(task.titulo) : task.titulo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = titulo != null ? titulo.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (hora != null ? hora.hashCode() : 0);
        result = 31 * result + idAutor.hashCode();
        result = 31 * result + status;
        return result;
    }
}
