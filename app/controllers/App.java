package controllers;

import models.GenericDAO;
import models.Task;
import models.Usuario;
import play.db.jpa.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caio on 26/03/15.
 */
public class App {

    private static GenericDAO dao = new GenericDAO();
    @Transactional
    public static Task getTask(Long id){
        List<Task> l = dao.findByAttributeName(Task.class.getName(),"id",String.valueOf(id));
        if(l.size()>0){
            return l.get(0);
        }
        return null;
    }

    @Transactional
    public static float getPorcentagemBDFree(){
        List<Usuario> lu = getListaDeUsuarios();
        List<Task> lt = getListaDeTasks();
        float t = (float)lt.size();
        float u = (float)lu.size();
        return (100-(((t+u)*100)/10000));
    }

    @Transactional
    public static List<Task> getListaDeTasks(){
        return dao.findAllByClassName(Task.class.getName());
    }
    @Transactional
    public static boolean existeTask(Task task){
        List<Task> l = getListaDeTasks();
        for(Task t: l){
            if(t.equals(t)){
                return true;
            }
        }
        return false;
    }
    @Transactional
    public static Usuario getUsuarioPorEmail(String email){
        List<Usuario> l = dao.findByAttributeName(Usuario.class.getName(),"email",email);
        if(l.size()>0){
            return l.get(0);
        }
        return null;
    }

    @Transactional
    public static List<Task> getListaDeTaskDeUsuario(Usuario usuario){
        List<Task> l = dao.findByAttributeName(Task.class.getName(),"idAutor",String.valueOf(usuario.getId()));
        return l;
    }
    @Transactional
    public static List<Task> getListaDeTaskDeUsuario(Long id){
        List<Task> l = dao.findByAttributeName(Task.class.getName(),"idAutor",String.valueOf(id));
        return l;
    }
    @Transactional
    private static void addTask(Task t){
        dao.persist(t);
        dao.flush();
    }
    @Transactional
    public static boolean addTaskAUmUsuario(String titulo, String data, String hora, Usuario autor, int status){
        Task t = new Task(titulo,data,hora,autor,status);
        if(!existeTask(t)){
            addTask(t);
            return true;
        }
        return false;
    }
    @Transactional
    private static void removeTask(Task t){
        dao.remove(t);
        dao.flush();
    }
    @Transactional
    public static boolean removeTaskDeUmUsuario(Long id){
        Task t = getTask(id);
        if(t!=null){
            removeTask(t);
            return true;
        }
        return false;
    }
    @Transactional
    public static Usuario getUsuario(Long id){
        List<Usuario> l = dao.findByAttributeName(Usuario.class.getName(),"id",String.valueOf(id));
        if(l.size()>0){
            return l.get(0);
        }
        return null;
    }
    @Transactional
    public static List<Usuario> getListaDeUsuarios(){
        return dao.findAllByClassName(Usuario.class.getName());
    }
    @Transactional
    public static boolean existeUsuario(Usuario usuario){
        List<Usuario> l = getListaDeUsuarios();
        for(Usuario u: l){
            if(u.equals(usuario)){
                return true;
            }
        }
        return false;
    }
    @Transactional
    private static void addUsuario(Usuario u){
        dao.persist(u);
        dao.flush();
    }
    @Transactional
    public static boolean addUsuarioNoBD(String nome, String email, String senha, String foto, int plan){
        Usuario u = new Usuario(nome,email, senha, foto, plan);
        if(!existeUsuario(u)){
            addUsuario(u);
            return true;
        }
        return false;
    }
    @Transactional
    private static void removeUsuario(Usuario u){
        dao.remove(u);
        dao.flush();
    }
    @Transactional
    public static boolean removeUsuarioDoBD(Long id){
        Usuario u =getUsuario(id);
        if(u!=null){
            removeUsuario(u);
            return true;
        }
        return false;
    }
    @Transactional
    public static List<Task> getListaTaskFeitasGeral(){
        List<Task> l = dao.findByAttributeName(Task.class.getName(),"status",String.valueOf(1));
        return l;
    }
    @Transactional
    public static List<Task> getListaTaskFeitasDoUsuario(Usuario u){
        List<Task> lg = getListaTaskFeitasGeral();
        List<Task> l = new ArrayList<>();
        for(Task t: lg){
            if(t.getIdAutor().equals(u.getId())){
                l.add(t);
            }
        }
        return l;
    }
    @Transactional
    public static List<Task> getListaTaskFeitasDoUsuario(Long id){
        Usuario u = getUsuario(id);
        List<Task> lg = getListaTaskFeitasGeral();
        List<Task> l = new ArrayList<>();
        for(Task t: lg){
            if(t.getIdAutor().equals(u.getId())){
                l.add(t);
            }
        }
        return l;
    }
    @Transactional
    public static int getTotalTaskDoUsuario(Usuario u){
        List<Task> l = getListaDeTaskDeUsuario(u);
        return l.size();
    }
    @Transactional
    public static int getTotalTaskDoUsuario(Long id){
        List<Task> l = getListaDeTaskDeUsuario(id);
        return l.size();
    }
    @Transactional
    public static int getTotaldeTasksFeitasDoUsuario(Usuario u){
        List<Task> l = getListaTaskFeitasDoUsuario(u);
        return l.size();
    }
    @Transactional
    public static int getTotaldeTasksFeitasDoUsuario(Long id){
        List<Task> l = getListaTaskFeitasDoUsuario(id);
        return l.size();
    }
    @Transactional
    public static int getTotalDeTasksPendentesDoUsuario(Usuario u){
        return (getTotalTaskDoUsuario(u)-getTotaldeTasksFeitasDoUsuario(u));
    }
    @Transactional
    public static int getTotalDeTasksPendentesDoUsuario(Long id){
        return (getTotalTaskDoUsuario(id)-getTotaldeTasksFeitasDoUsuario(id));
    }





}
