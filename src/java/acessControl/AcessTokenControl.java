package acessControl;

import Persistencia.Conexao;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

/**
 * Generates a unique access token for a user who wants to use the service
 * @author Davy Lima
 */
public class AcessTokenControl {
    private static RandomString randomString = new RandomString();

    public static String isValidToken(String token) throws Exception {
        Connection con = Conexao.getConexao();
        CallableStatement call = con.prepareCall("call isValidToken(?)");
        call.setString(1, token);
        ResultSet result = call.executeQuery();
        result.first();
        String r = result.getString("result");
        System.out.println(" isValidToken Result: "+r);
        return r;
    }

    public static JSONObject refreshToken(String token) throws Exception {
        String newToken = randomString.nextString();
        Connection con = Conexao.getConexao();
        String sql = "update autenticacao set token = ?, dataExpira = now(), dataRefresh = now()"
                + "where token = ?";
        PreparedStatement sp = con.prepareStatement(sql);
        sp.setString(1, newToken);
        sp.setString(2, token);
        int executeUpdate = sp.executeUpdate();
        if(executeUpdate == 1){
            JSONObject json = new JSONObject();
            json.put("newToken", newToken);
            return json;
        }
        else 
            throw new ExecutionException("SQL update failed", new Exception("Canot execute update"));
    }

    public static JSONObject newToken(String email, String senha) throws Exception {
        String token = randomString.nextString();
        Connection con = Conexao.getConexao();
        
        String sqlSelect = "select idUsuario from usuario where email = ?";
        PreparedStatement sp = con.prepareStatement(sqlSelect);
        sp.setString(1, email);
        ResultSet rs = sp.executeQuery();
        rs.first();
        String id = rs.getString("idUsuario");
        
        String sqlInsert = "insert into autenticacao set idUsuario = ?, "
                + "token = ?, dataExpira = now()";
        sp = con.prepareStatement(sqlInsert);
        sp.setString(1, id);
        sp.setString(2, token);
        int result = sp.executeUpdate();
        
        System.out.println("NEWTOKEN: "+token + "usuario: "+id);
        
        if(result == 1){
           JSONObject json = new JSONObject();
           json.put("token", token);
           return json;
        }else
            throw new ExecutionException("SQL insert failed", new Exception("Canot execute insert"));
        
    }
   
}


