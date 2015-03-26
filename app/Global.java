import models.GenericDAO;
import models.Usuario;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by caio on 24/03/15.
 */

public class Global extends GlobalSettings {

    private static GenericDAO dao = new GenericDAO();

    @Override
    public void onStart(Application app) {
        Logger.info("inicializada...");

        JPA.withTransaction(new play.libs.F.Callback0() {

            public void invoke() throws Throwable {

                List<Usuario> lis = dao.findAllByClassName(Usuario.class.getName());
                if (lis.size() == 0) {
                    Usuario u = new Usuario("Administrador", "administrador@administrador.com", "oficinag3",
                            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRm8kQFHgBNcYI8Es9_sYht0wvRnCzUqSdkIBb4PnqBVNj51vJF",2);
                    dao.persist(u);
                    dao.flush();
                    Logger.info("Inserindo dados no BD.");
                }
            }
        });
    }

    public void onStop(Application app) {
        Logger.info("desligada...");
    }

}