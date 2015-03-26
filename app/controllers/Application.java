package controllers;

import models.Usuario;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;

import views.html.*;

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
        return ok(dashboard.render(u, App.getListaDeTaskDeUsuario(u)));
    }

}
