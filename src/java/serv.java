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
            }
        }catch (JSONException ex) {
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
        String js = IOUtils.toString(request.getReader());
        JSONObject json = new JSONObject(js);
        String acertos = json.getString("acertos");
        String erros = json.getString("erros");
        String emailAt = json.getString("email");
        String resultAt = JsonProvaFactory.salvarDadosProva(acertos, erros, emailAt);
        out.print(resultAt);
    }

    private void getDadosUsuario(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getReader());
        System.out.println(">>>>>>> "+js);
        JSONObject json = new JSONObject(js);
        String email = json.getString("email");
        JSONObject jsonDataUser = JsonProvaFactory.getUserData(email);
        System.out.println("DATAUSER "+email+" data:"+jsonDataUser);
        out.print(jsonDataUser);
    }

    private void getImageQuest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getReader());
        JSONObject json = new JSONObject(js);
        String imgId = json.getString("imageId");
        JSONObject imagem = JsonProvaFactory.getImagem(imgId);
        out.print(imagem);
        System.out.println("imagem ===>"+imagem);
    }

    private void listarProvas(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        String js = IOUtils.toString(request.getReader());
        JSONObject json = new JSONObject(js);
        String tipoProva = json.getString("tipoProva");
        JSONObject jsonListaProvas = JsonProvaFactory.getListaProvas(tipoProva);
        out.print(jsonListaProvas);
        System.out.println("listarProvas ===>"+jsonListaProvas);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws JSONException, IOException {
        JSONObject jsonTipoProva = JsonProvaFactory.jsonTipoProva();
        out.print(jsonTipoProva);
        System.out.println("listar ===>"+jsonTipoProva);
    }

    private void buscarProva(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, JSONException {
        String js = IOUtils.toString(request.getReader());
        JSONObject json = new JSONObject(js);
        String idProva = json.getString("idProva");
        JSONObject jsonProva = JsonProvaFactory.getProva(idProva);
        out.print(jsonProva);
        System.out.println("buscarProva ===>"+jsonProva);
    }
    
}
