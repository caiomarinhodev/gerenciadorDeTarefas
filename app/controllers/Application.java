package controllers;

import models.LoginFacebook;
import models.Task;
import models.Usuario;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;

import views.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {
    @Transactional
    public static Result index() {
        String email = session().get("email");
        Usuario defaul = new Usuario("Administrador", "administrador@administrador.com", "oficinag3",
                "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRm8kQFHgBNcYI8Es9_sYht0wvRnCzUqSdkIBb4PnqBVNj51vJF", 2);
        Usuario u = null;
        if (email != null || email != "") {
            u = App.getUsuarioPorEmail(email);
        }

        if (u != null) {
            if (u != null) {
                if (u.getEmail().equals(defaul.getEmail()) && u.getSenha().equals(defaul.getSenha())) {
                    session().clear();
                    session().put("email", email);
                    return renderDashAdmin();
                } else {
                    session().clear();
                    session().put("email", email);
                    return renderDashboardUsuario();
                }
            }
        }
        return ok(login.render(""));
    }

    @Transactional
    public static Result auth() {
        DynamicForm r = Form.form().bindFromRequest();
        String email = r.get("email");
        String senha = r.get("senha");
        Logger.info(senha);
        Usuario u = App.getUsuarioPorEmail(email);
        if (u != null) {
            if ((u.getEmail().equals("administrador@administrador.com"))) {
                if (u.getSenha().equals("oficinag3")) {
                    session().clear();
                    session().put("email", email);
                    return renderDashAdmin();
                }
            } else {
                session().clear();
                session().put("email", email);
                return renderDashboardUsuario();
            }
        }

        return index();

    }

    @Transactional
    public static Result logout() {
        session().clear();
        return index();
    }

    @Transactional
    public static Result renderDashAdmin() {
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        return ok(admindash.render(u));
    }

    public static Result renderRegister() {
        return ok(register.render(""));
    }

    @Transactional
    public static Result addUsuario() {
        DynamicForm r = Form.form().bindFromRequest();
        String nome, email, senha, foto;
        int plan = 0;
        nome = r.get("nome");
        email = r.get("email");
        senha = r.get("senha");
        foto = "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/256/Actions-im-invisible-user-icon.png";
        if (App.addUsuarioNoBD(nome, email, senha, foto, plan)) {
            return index();
        }
        return renderRegister();
    }

    @Transactional
    public static Result renderDashboardUsuario() {
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        return renderListTarefasUsuario(1);
    }

    @Transactional
    public static Result renderAddTarefa(){
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        return ok(add.render(u));
    }

    @Transactional
    public static Result removeTarefa(Long id){
        App.removeTaskDeUmUsuario(id);
        return renderListTarefasUsuario(1);
    }

    @Transactional
    public static Result renderTasksAdmin(int ind){
        Logger.info("INDICE:"+ ind);
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        List<Task> l = App.getListaDeTasks();
        int resto = (l.size())%30;
        if (l.size() <= 30) {
            return ok(tasks.render(u, l));
        } else if(resto==0 && ind!=App.getIndicesTableTasksGeral()){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(tasks.render(u, ln));
        }else if(resto!=0 && ind!=App.getIndicesTableTasksGeral()){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(tasks.render(u,ln));
        }else{
            int ini = 30 * (ind - 1);
            int k = ini + resto;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(tasks.render(u,ln));
        }
    }

    @Transactional
    public static Result renderUsersAdmin(int ind){
        Logger.info("INDICE:"+ ind);
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        List<Usuario> l = App.getListaDeUsuarios();
        int resto = (l.size())%30;
        if (l.size() <= 30) {
            return ok(users.render(u, l));
        } else if(resto==0 && ind!=App.getIndicesTableUsersGeral()){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Usuario> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(users.render(u, ln));
        }else if(resto!=0 && ind!=App.getIndicesTableUsersGeral() ){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Usuario> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(users.render(u,ln));
        }else{
            int ini = 30 * (ind - 1);
            int k = ini + resto;
            List<Usuario> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(users.render(u,ln));
        }
    }

    @Transactional
    public static Result salvar(){
        DynamicForm r = Form.form().bindFromRequest();
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        for(Task t: App.getListaDeTaskDeUsuario(u)){
            if(r.get(String.valueOf(t.getId()))!=null){
                //Logger.info("VAlue: " + r.get(String.valueOf(t.getId())));
                App.setStatusTask(t);
            }
        }
        return renderListTarefasUsuario(1);
    }

    @Transactional
    public static Result removeTask(Long id){
        App.removeTaskDeUmUsuario(id);
        return renderHistoricoUsuario(1);
    }

    @Transactional
    public static Result removeTaskAdmin(Long id){
        App.removeTaskDeUmUsuario(id);
        return renderTasksAdmin(1);
    }

    @Transactional
    public static Result removeUsuarioAdmin(Long id){
        App.removeUsuarioDoBD(id);
        return renderUsersAdmin(1);
    }

    @Transactional
    public static Result renderHistoricoUsuario(int ind){
        Logger.info("INDICE:"+ ind);
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        List<Task> l = App.getListaDeTaskDeUsuario(u);
        int resto = (l.size())%30;
        if (l.size() <= 30) {
            return ok(historicoUsuario.render(u,l));
        } else if(resto==0 && ind!=App.getIndicesTable(u)){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(historicoUsuario.render(u,ln));
        }else if(resto!=0 && ind!=App.getIndicesTable(u) ){
            int ini = 30 * (ind - 1);
            int k = ini + 30;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(historicoUsuario.render(u,ln));
        }else{
            int ini = 30 * (ind - 1);
            int k = ini + resto;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(historicoUsuario.render(u,ln));
        }
    }

    @Transactional
    public static Result renderListTarefasUsuario(int ind){
        Logger.info("INDICE:"+ ind);
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        List<Task> l = App.getListaTaskPendentesDoUsuario(u);
        int resto = (l.size())%5;
        if (l.size() <= 5) {
            return ok(dashboard.render(u, l));
        } else if(resto==0 && ind!=App.getIndicesListTarefas(u)){
            int ini = 5 * (ind - 1);
            int k = ini + 5;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(dashboard.render(u,ln));
        }else if(resto!=0 && ind!=App.getIndicesListTarefas(u)){
            int ini = 5 * (ind - 1);
            int k = ini + 5;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(dashboard.render(u,ln));
        }else{
            int ini = 5 * (ind - 1);
            int k = ini + resto;
            List<Task> ln = new ArrayList<>();
            for (int i = ini; i < k; i++){
                ln.add(l.get(i));
            }
            return ok(dashboard.render(u,ln));
        }
    }

    @Transactional
    public static Result addItemTask(){
        DynamicForm r = Form.form().bindFromRequest();
        String titulo, data, hora;
        titulo = r.get("titulo");
        data = r.get("data");
        hora = r.get("hora");
        int status = 0;
        Usuario u = App.getUsuarioPorEmail(session().get("email"));
        if(App.addTaskAUmUsuario(titulo,data,hora,u,status)){
            return renderListTarefasUsuario(1);
        }
        return renderAddTarefa();
    }

    //@RequestMapping("/loginfb")
    public static Result logarComFacebook() {
        LoginFacebook loginFacebook = new LoginFacebook();
        return redirect(loginFacebook.getLoginRedirectURL());
    }

    /**
     * Executado quando o Servidor de Autorização fizer o redirect.
     *
     * @param code
     * @return
     * @throws IOException
     */
    //@RequestMapping("/loginfbresponse")
    @Transactional
    public static Result logarComFace(String code) throws IOException {
        LoginFacebook loginFacebook = new LoginFacebook();

        Logger.info("CODE:" + code);
        Usuario ufb = loginFacebook.obterUsuarioFacebook(code);
        Usuario us = App.getUsuarioPorEmail(ufb.getEmail());
        if(us==null){
            App.addUsuarioNoBD(ufb.getEmail(),"12345", ufb.getNome(),ufb.getFoto(),0);
            session().clear();
            session().put("email",ufb.getEmail());
            return renderDashboardUsuario();
        }
        if (us!=null) {
            session().clear();
            session().put("email",ufb.getEmail());
            return renderDashboardUsuario();
        } else {
            return index();
        }
    }

}
