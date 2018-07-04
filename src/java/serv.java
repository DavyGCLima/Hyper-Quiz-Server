import Persistencia.DaoProva;
import Persistencia.JsonProvaFactory;
import org.json.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Davy Lima
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
        System.out.println("Tipo: "+tipoReq);
        
        /*try*/ PrintWriter out = response.getWriter(); //{
        System.out.println("REQUEISIÇÃO: "+tipoReq);
        try{
            switch(tipoReq){
                case "buscarProva":buscarProva(request, response, out);
                case "listar":listar(request, response, out);break;
                case "listarProvas":listarProvas(request, response, out);break;
                case "getImageQuest":getImageQuest(request, response, out);break;
                case "getDadosUsuario":getDadosUsuario(request, response, out);break;
                case "atualizarEstatisticasUsuario":atualizaEstatisticasUsuario(request, response, out);break;
                case "login":login(request, response, out);break;
                case "listarHistorico":getHistorico(request, response, out);break;
            }
        }catch (JSONException ex) {
        Logger.getLogger(serv.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            if(ex.getMessage().equals("Dados não encontrados"))
                try {
                    out.print(new JSONObject().put("error", "Dados não encontrados"));
            } catch (JSONException ex1) {
                Logger.getLogger(serv.class.getName()).log(Level.SEVERE, null, ex1);
            }
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

    private void atualizaEstatisticasUsuario(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, JSONException {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String acertos = json.getString("acertos");
        String erros = json.getString("erros");
        String email = json.getString("email");
        String result = JsonProvaFactory.salvarDadosProva(acertos, erros, email);
        updateHistory(acertos, email);
        out.print(result);
    }

    private void getDadosUsuario(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getReader());
        JSONObject json = new JSONObject(js);
        String email = json.getString("email");
        JSONObject jsonDataUser = JsonProvaFactory.getUserData(email);
        out.print(jsonDataUser);
    }

    private void getImageQuest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String imgId = json.getString("imageId");
        JSONObject imagem = JsonProvaFactory.getImagem(imgId);
        out.print(imagem);
    }

    private void listarProvas(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String tipoProva = json.getString("tipoProva");
        JSONObject jsonListaProvas = JsonProvaFactory.getListaProvas(tipoProva);
        out.print(jsonListaProvas);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        JSONObject jsonTipoProva = JsonProvaFactory.jsonTipoProva();
        out.print(jsonTipoProva);
    }

    private void buscarProva(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, JSONException {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String tipoProva = json.getString("tipoProva");
        JSONObject jsonProva = JsonProvaFactory.getProva(tipoProva);
        out.print(jsonProva);
    }

    private void login(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException, Exception {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String token = request.getHeader("token");
        JSONObject refreshToken = acessControl.AcessTokenControl.refreshToken(token);
        String nome = DaoProva.getNomeUsuariio(json.getString("email"));
        refreshToken.put("name", nome);
        out.print(refreshToken);
    }

    private void updateHistory(String acertos, String email) throws IOException, JSONException {
        JsonProvaFactory.updateHistory(acertos, email);
    }

    private void getHistorico(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, JSONException {
        String js = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject json = new JSONObject(js);
        String email = json.getString("email");
        JSONObject retorno = JsonProvaFactory.getHistorico(email);
        out.print(retorno);
    }
    
}
