/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Persistencia.JsonProvaFactory;
import java.io.File;
import java.io.FileReader;
import org.json.*;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;

/**
 *
 * @author reida
 */
@WebServlet(urlPatterns = {"/serv"})
public class serv extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("conecção");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String tipoReq = request.getHeader("tipo");
                //getParameter("tipo");
        
        try (PrintWriter out = response.getWriter()) {
            
            switch(tipoReq){
                case "tipoProva":
                    JSONObject jsonTipoProva = JsonProvaFactory.jsonTipoProva();
                    out.print(jsonTipoProva);
                    break;
            }
            
            
            
            
            
            
            
            
            
            
            
            
            /* TODO output your page here. You may use following sample code. */
            //File f = new File("E:\\Google Drive\\ifes\\Desenvolvimento Web\\Projeto Web\\webServiceIC\\src\\java\\prova.json").getAbsoluteFile();
            JSONObject json = new JSONObject();
            json.put("name", "Prova1");
            json.put("numQuests", "4");
            
            JSONArray questoes = new JSONArray();
            
            JSONObject questao = new JSONObject();
            questao.put("body", "A afirmação a ponderação é hoje a mais desmoralizada das virtudes deve ser entendida, no contexto, como ");
            questao.put("optionA", "uma constatação já consensual, a partir da tendência dominante de se afirmar que, se uma coisa é isso, é também aquilo.");
            questao.put("optionB", "a valorização do discernimento público que permite distinguir, metaforicamente falando, um abacaxi de um pepino.");
            questao.put("optionC", "a constatação de que está ocorrendo uma negação de análises mais equilibradas, por conta da violência dos radicalismos.");
            questao.put("optionD", "uma forma de repúdio às redes sociais, quando estas expõem sem subterfúgios nossos comportamentos opressivos ancestrais.");
            questao.put("optionE", "uma crítica violenta, dirigida àqueles que entendem o equilíbrio de julgamento como subproduto da perplexidade.");
            questao.put("answer", "a");
            questao.put("image", "");
            questoes.put(questao);
            
            JSONObject questao2 = new JSONObject();
            questao2.put("body", "Ao se referir, metaforicamente, às duas ações do fogo selvagem (3° parágrafo), o autor do texto coloca em evidência");
            questao2.put("optionA", "o aparente desacordo de ações contraditórias que, de fato, se complementam num momento de ponderação.");
            questao2.put("optionB", "a natureza violenta de ações e reações que se regem pelos mesmos paradigmas de brutalidade.");
            questao2.put("optionC", "a contraposição entre ideais que são defendidos com argumentos igualmente ponderáveis.");
            questao2.put("optionD", "a violência de opiniões contrárias, num percurso ao fim do qual elas acabarão por produzir o mesmo efeito positivo.");
            questao2.put("optionE", "o avanço e o retrocesso simultâneos que as ações ponderadas acabam por impor ao ritmo da história contemporânea.");
            questao2.put("answer", "c");
            questao2.put("image", "");
            questoes.put(questao2);
            
            JSONObject questao3 = new JSONObject();
            questao3.put("body", "Sentimos que toda satisfação de nossos desejos advinda do mundo assemelha-se à esmola que mantém hoje o mendigo vivo, porém prolonga amanhã a sua fome. A resignação, ao contrário, assemelha-se à fortuna herdada: livra o herdeiro para sempre de todas as preocupações. SCHOPENHAUER, A. Aforismo para a sabedoria da vida. São Paulo: Martins Fontes, 2005. O trecho destaca uma ideia remanescente de uma tradição filosófica ocidental, segundo a qual a felicidade se mostra indissociavelmente ligada à");
            questao3.put("optionA", "consagração de relacionamentos afetivos.");
            questao3.put("optionB", "administração da independência interior.");
            questao3.put("optionC", "fugacidade do conhecimento empírico.");
            questao3.put("optionD", "liberdade de expressão religiosa.");
            questao3.put("optionE", "busca de prazeres efêmeros. ");
            questao3.put("answer", "e");
            File f = new File("E:\\Google Drive\\ifes\\Desenvolvimento Web\\Projeto Web\\webServiceIC\\src\\java\\img/thumbs_36.jpg");
            byte[] byteArray = FileUtils.readFileToByteArray(f);
            String img = Base64.getEncoder().encodeToString(byteArray);
            questao3.put("image", img);
            questoes.put(questao3);
                    
            json.put("quests", questoes);
            
            JSONObject questao4 = new JSONObject();
            questao4.put("body", " Batizado por Tancredo Neves de “Nova República”, o período que marca o reencontro do Brasil com os governos civis e a democracia ainda não completou seu quinto ano e já viveu dias de grande comoção. Começou com a tragédia de Tancredo, seguiu pela euforia do Plano Cruzado, conheceu as depressões da inflação e das ameaças da hiperinflação e desembocou na movimentação que antecede as primeiras eleições diretas para presidente em 29 anos.\n" +
"\n" +
"O álbum dos presidentes: a história vista pelo JB. Jornal do Brasil, 15 nov. 1989.\n" +
"\n" +
"O período descrito apresenta continuidades e rupturas em relação à conjuntura histórica anterior. Uma dessas continuidades consistiu na" +
                    " Os moradores de Andalsnes, na Noruega, poderiam se dar ao luxo de morar perto do trabalho nos dias úteis e de se refugiar na calmaria do bosque aos fins de semana. E sem sair da mesma casa. Bastaria achar uma vaga para estacionar o imóvel antes de curtir o novo endereço.\n" +
"\n" +
"Disponível em: http://casavogue.globo.com. Acesso em: 3 out. 2015 (adaptado).\n" +
"\n" +
"Uma vez implementada, essa proposta afetaria a dinâmica do espaço urbano por reduzir a intensidade do seguinte processo: ");
            questao4.put("optionA", "representação do legislativo com a fórmula do bipartidarismo. ");
            questao4.put("optionB", "detenção de lideranças populares por crimes de subversão.  ");
            questao4.put("optionC", "presença de políticos com trajetórias no regime autoritário. ");
            questao4.put("optionD", "prorrogação das restrições advindas dos atos institucionais. ");
            questao4.put("optionE", "estabilidade da economia com o congelamento anual de preços.");
            questao4.put("answer", "b");            
            File f2 = new File("E:\\Google Drive\\ifes\\Desenvolvimento Web\\Projeto Web\\webServiceIC\\src\\java\\img/thumbs_36.jpg");
            byte[] byteArray2 = FileUtils.readFileToByteArray(f);
            String img2 = Base64.getEncoder().encodeToString(byteArray2);
            questao4.put("image", img2);
            questoes.put(questao4);
            json.put("quests", questoes);
            
            String sJson = json.toString();
            //String s = FileUtils.readFileToString(sJson, "UTF-8");
            out.print(sJson);
            
        } catch (JSONException ex) {
            Logger.getLogger(serv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
