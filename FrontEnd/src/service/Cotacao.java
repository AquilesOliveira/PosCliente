/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author itakenami
 */
import swing.model.DefaultModel;
import swing.annotation.GridHeader;
import api.wadl.annotation.XMLCast;
import client.crud.Service;
import client.exception.ValidationException;
import client.request.ApacheRequest;
import client.request.SimpleRequest;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import swing.ui.ModelField;

/**
 *
 * @author itakenami
 */
@XMLCast(thisClassFrom = "models.Cotacao")
public class Cotacao implements DefaultModel<Cotacao> {

    @GridHeader(name = "ID", size = 10)
    public Long id;
    @GridHeader(name = "CPF", size = 100)
    public String CPF;
    @GridHeader(name = "Solicitante", size = 100)
    public String solicitante;
    @GridHeader(name = "Data Solicitação", size = 50)
    public Date data_solicitacao;
    public Set<Produto> produtos;
    public static Service<Cotacao> service = new Service<Cotacao>(new ApacheRequest("http://localhost:8080/restee/api/cotacoes"), Cotacao.class, new TypeToken<List<Cotacao>>() {
    }.getType());

    @Override
    public String toString() {
        return solicitante;
    }

    @Override
    public List<Cotacao> findStart() {
        //return service.findAll();
        return new LinkedList<Cotacao>();
    }

    @Override
    public Cotacao findById(Long id) {
        return service.findById(id);
    }

    @Override
    public Cotacao save(Long id, HashMap<String, Object> map) throws ValidationException {


        System.out.println(map.get("CPF").toString() + " " + map.get("Solicitante").toString() + " " + map.get("Data Solicitacao").toString());

        HashMap<String, String> vo = new HashMap<String, String>();

        vo.put("cotacao.CPF", map.get("CPF").toString());
        vo.put("cotacao.solicitante", map.get("Solicitante").toString());
        vo.put("cotacao.data_solicitacao", map.get("Data Solicitacao").toString());

        String[] sel = (String[]) map.get("Produtos");

        System.out.println(sel.length);

        for (int x = 0; x < sel.length; x++) {
            vo.put("cotacao.produtos[" + sel[x] + "].id", sel[x]);
            System.out.println("cotacao.produtos[" + sel[x] + "].id" + sel[x]);
        }

        return service.save(id, vo);
    }

    @Override
    public ModelField getGridFields() {
        ModelField gf = new ModelField();
        gf.addField(id);
        gf.addField(CPF);
        gf.addField(solicitante);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        gf.addField(sdf.format(data_solicitacao));
        return gf;
    }

    @Override
    public ModelField getViewFields() {
        ModelField ff = new ModelField();
        ff.addField("ID", id);
        ff.addField("CPF", CPF);
        ff.addField("Solicitante", solicitante);
        ff.addField("Data Solicitacao", data_solicitacao, ModelField.DATE);

        ff.addField("Produtos", produtos, ModelField.LISTBOX);
        return ff;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<? extends DefaultModel> getObj(String campo) {
        if (campo.equals("Produtos")) {
            return Produto.service.findAll();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return service.delete(id);
    }

    @Override
    public List<Cotacao> filterGrid(String filter) {

        List<Cotacao> lista = service.findAll();

        if ("".equals(filter) || filter.equals("*")) {
            return lista;
        }

        List<Cotacao> filtro = new ArrayList<Cotacao>();

        for (Cotacao cotacao : lista) {
            if (cotacao.solicitante.toUpperCase().startsWith(filter.toUpperCase())) {
                filtro.add(cotacao);
            }
        }
        return filtro;

    }
}
