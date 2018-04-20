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

    /**
     * 
     * @param tipoProva
     * @return Json contendo a 
     */
    public static JSONObject getListaProvas(String tipoProva) {
        JSONObject json = new JSONObject();
        try{
            List<List> provas = DaoProva.listarProvas(tipoProva);
            if(provas != null)
                //le uma linaha e armazena no json
                for(List linha : provas){
                    JSONObject linhaJson = new JSONObject();
                    linhaJson.put("idProva", linha.get(0));
                    linhaJson.put("nome", linha.get(1));
                    linhaJson.put("qtdQuestoes", linha.get(2));
                    linhaJson.put("idTipoProva", linha.get(3));
                    json.append("prova", linhaJson);
                }
//                for(int i = 0; i < provas.size(); i++){
//                    json.append("prova", provas.get(i));
//                }
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

    
    public static JSONObject getProva(String idProva){
        JSONObject json = new JSONObject();
        try {
            List<Prova.Questao> questoes = DaoProva.buscarQuestoesProva(Integer.valueOf(idProva));
            List dados = DaoProva.buscarDadosProva(Integer.valueOf(idProva));
            if(questoes != null && questoes.size() > 0 && dados != null && dados.size() > 0){
                json.put("name", dados.get(1));
                json.put("numQuests", dados.get(2));
                for(Prova.Questao item : questoes){
                    JSONObject questao = new JSONObject(item);
                    json.append("quests", questao);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    
    // {"tipo":["ENAD","ENEM"]}
    //{"prova":[{"idProva":"1","idTipoProva":"1","nome":"Prova teste","qtdQuestoes":"3"},{"idProva":"2","idTipoProva":"1","nome":"P`rova Teste 2","qtdQuestoes":"1"}]}
}
