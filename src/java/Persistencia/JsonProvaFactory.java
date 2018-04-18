package Persistencia;

import Persistencia.DaoProva;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Davy Lima
 */
public class JsonProvaFactory {
    
    /**
     * Retorna um JSONObject contendo a lista de tipos de provas
     * @return Retorna Retorna um JSONObject contendo a lista de tipos de provas, em caso de exceção retornará nullo
     */
    public static JSONObject jsonTipoProva(){
        JSONObject json = new JSONObject();
        try {
            List<String> listaTiposProva = DaoProva.buscarTipoProva(); 
            if(listaTiposProva != null)
                for(int i = 0; i < listaTiposProva.size(); i++){
                    json.append("tipo", listaTiposProva.get(i));
                }
        } catch (SQLException ex) {
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            try {
                json.put("ERRO", "Erro ao recuperar tipos de provas");
            } catch (JSONException ex1) {
                Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch ( JSONException ex){
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(json.length() == 0)
                try {
                    json.put("ERRO", "Erro ao recuperar tipos de provas");
            } catch (JSONException ex) {
                Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            return json;
        }
    }

    public static JSONObject getListaProvas(String tipoProva) {
        JSONObject json = new JSONObject();
        try{
            List<List> provas = DaoProva.listarProvas(tipoProva);
            if(provas != null)
                for(int i = 0; i < provas.size(); i++){
                    json.append("prova", provas.get(i));
                }
        } catch (SQLException ex) {
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            try {
                json.put("ERRO", "Erro ao recuperar lista de provas");
            } catch (JSONException ex1) {
                Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch ( JSONException ex){
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(json.length() == 0)
                try {
                    json.put("ERRO", "Erro ao recuperar lista de provas");
            } catch (JSONException ex) {
                Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            return json;
        }
    }
    
}
