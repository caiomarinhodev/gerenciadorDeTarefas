package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.Json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoginFacebook {

    private static final String client_secret = "4c639a4ea811d58a1fe7dcb92f65cdb2";
    private static final String client_id = "806230146130669";
    private static final String redirect_uri = "https://atarefado.herokuapp.com/loginfbresponse";

    public Usuario obterUsuarioFacebook(String code)
            throws IOException {

        String retorno = readURL(new URL(this.getAuthURL(code)));

        String accessToken = null;
        @SuppressWarnings("unused")
        Integer expires = null;
        String[] pairs = retorno.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length != 2) {
                throw new RuntimeException("Resposta auth inesperada.");
            } else {
                if (kv[0].equals("access_token")) {
                    accessToken = kv[1];
                }
                if (kv[0].equals("expires")) {
                    expires = Integer.valueOf(kv[1]);
                }
            }
        }

        JsonNode resp = Json.parse(readURL(new URL(
                "https://graph.facebook.com/me?access_token=" + accessToken)));

//        JSONObject resp = new JSONObject(readURL(new URL(
//                "https://graph.facebook.com/me?access_token=" + accessToken)));

        String email = resp.findPath("email").textValue();

        String nome = resp.findPath("name").textValue();

        String fo = resp.findPath("cover_photo").textValue();
        String foto = "https://cdn3.iconfinder.com/data/icons/users/100/user_male_1-512.png";
        System.out.println("PICTURE DO FACEBOOK:"+fo);
        //Logger.info(foto);
        //Logger.info(resp.asText());
        Usuario usuarioFacebook = new Usuario(nome,email,"12345",foto,0);
        System.out.println(usuarioFacebook.getFoto());
        return usuarioFacebook;

    }

    private String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }

    public String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id="
                + client_id + "&display=page&redirect_uri=" + redirect_uri
                + "&scope=email,publish_actions";

//        return "http://www.facebook.com/dialog/oauth?" + "client_id="
//                + client_id + "&redirect_uri="
//                +redirect_uri
//                + "&scope=email";
    }

    public String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + client_id + "&redirect_uri=" + redirect_uri
                + "&client_secret=" + client_secret + "&code=" + authCode;
    }

}
