package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
  Classe de pércistência de provas
 * @author Davy Lima
 */
public class DaoProva {
    
    /**
     * Busca na base de dados os tipos de provas cadastrados
     * @return Uma lista contendo todos os tipos de provas
     * @throws SQLException 
     */
    public static List<String> buscarTipoProva() throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
            if(conexao != null){
            String sql = "SELECT Tipo FROM TipoProva";

            PreparedStatement preparedStatement;
            preparedStatement = conexao.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List tipos = new ArrayList<>();
            while(resultSet.next()){
                tipos.add(resultSet.getString("Tipo"));
            }
            return tipos;
        }
            return null;
    }
    
    /**
     * Busca uma lista de provas de acordo com o tipo de prova
     * @param nomeProva Nome da prova desejada
     * @param tipoProva Tipo da prova desejada
     * @return Lista de provas contendo as possiveis provas
     * @throws SQLException 
     */
    public static List buscarProva(String nomeProva, int tipoProva) throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
        if(conexao != null){
            String sql = "SELECT * FROM Prova WHERE idTipoProva = ?";
            PreparedStatement prepareStatement;
            prepareStatement = conexao.prepareStatement(sql);
            prepareStatement.setInt(1, tipoProva);
            //executa a busca
            ResultSet resultSet = prepareStatement.executeQuery();
            //monta o jsconObject da prova
            List provas = new ArrayList<>();
            while (resultSet.next()) {
                Prova p = new Prova();
                p.setId(Integer.parseInt(resultSet.getString("idProva")));
                p.setName(resultSet.getString("Nome"));
                p.setNumQuest(Integer.parseInt(resultSet.getString("QtdQuestoes")));
                provas.add(p);
            }
            return provas;
        }
        return null;
    }
    
    /**
     * Busca na base de dados as questões que compoem uma prova
     * @param provaId identificador da base de dados para a referente prova
     * @return Lista contendo todas as questões da prova
     * @throws SQLException
     * @throws Exception 
     */
    public static List buscarQuestoesProva(int provaId) throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
        
        String sql = "SELECT * FROM Questoes WHERE idProva = ?";
        
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setInt(1, provaId);
        ResultSet resultSet = ps.executeQuery();
        List Quest = new ArrayList<>();
        while(resultSet.next()){
            Prova.Questao q = new Prova.Questao();
            q.setBody(resultSet.getString("body"));
            q.setOptionA(resultSet.getString("optionA"));
            q.setOptionB(resultSet.getString("optionB"));
            q.setOptionC(resultSet.getString("optionC"));
            q.setOptionD(resultSet.getString("optionD"));
            q.setOptionE(resultSet.getString("optionE"));
            q.setAnswer(resultSet.getString("answer"));
            q.setImage(resultSet.getBytes("image"));
        }
        return Quest;
    }
}
