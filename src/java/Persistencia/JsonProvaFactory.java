package Persistencia;

import Persistencia.DaoProva;
import static com.sun.corba.se.impl.util.Utility.printStackTrace;
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
     * Lista as provas da base de dados de acordo com o tipo de prova
     * @param tipoProva id do tipo de prova
     * @return Json contendo a lista de provas da base de dados
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

    /**
     * Retorna um Json contendo todos os dados de uma prova, incluindo as questões\n
     * que o compoem
     * @param idProva
     * @return JSONObject conendo a prova
     */
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
            printStackTrace();
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static JSONObject getImagem(String imageId){
        JSONObject json = new JSONObject();
        try {
            String img = DaoProva.buscarImagemQuestao(imageId);
            json.put("img", img);
        } catch (Exception ex) {
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            try {
                json.put("ERRO", "Não foi possivel recuperar a imagem");
            } catch (JSONException ex1) {
                Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return json;
    }

    public static boolean validarLogin(String email, String senha) {
        try {
            return DaoProva.validarLogin(email, senha);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static String cadastrarNovoUsuario(String nome, String emailC, String senhaC) {
        try{
            return DaoProva.cadastrarNovoUsuario(nome, emailC, senhaC);
        }catch (Exception ex) {
            if(ex instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ){
                return "Usuário já existe";
            }else
                ex.printStackTrace();
            return "Houve um erro ao tentar efeturar o cadastro";
        }
    }

    public static String salvarDadosProva(String acertos, String erros, String email) {
       try{
           return DaoProva.salvarDadosProva(acertos, erros, email);
       }catch (Exception ex) {
           ex.printStackTrace();
            return "Houve um erro ao tentar efeturar o cadastro";
        }
    }

    public static JSONObject getUserData(String id) {
        JSONObject json = new JSONObject();
        try {
            String[] dados = DaoProva.buscarDadosUsuario(id);
            json.put("acertos", dados[0]);
            json.put("erros", dados[1]);
            return json;
        } catch (Exception ex) {
            Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex);
            if(ex.getMessage().equals("Dados não encontrados")){
                try {
                    json.put("error", "Dados não encontrados");
                    return json;
                } catch (JSONException ex1) {
                    Logger.getLogger(JsonProvaFactory.class.getName()).log(Level.SEVERE, null, ex1);
                }
                    
            }
            return json;
        }
        
    }
    
}
