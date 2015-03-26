package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caio on 25/03/2015.
 */
@Entity
@Table(name = "USUARIO")
public class Usuario {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column
    private String email;
    @Column
    private String senha;
    @Column
    private String foto;
    @OneToMany
    private List<Task> tasks;
    @Column
    private int plan;

    public Usuario(){

    }

    public Usuario(String nome, String email, String senha, String foto, int plan) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.foto = foto;
        this.tasks = new ArrayList<>();
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (plan != usuario.plan) return false;
        if (email != null ? !email.equals(usuario.email) : usuario.email != null) return false;
        if (foto != null ? !foto.equals(usuario.foto) : usuario.foto != null) return false;
        if (!nome.equals(usuario.nome)) return false;
        if (senha != null ? !senha.equals(usuario.senha) : usuario.senha != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + plan;
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }
}
