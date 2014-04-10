/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author azizsault
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import swing.annotation.GridHeader;
import swing.model.DefaultModel;
import swing.ui.ModelField;
import api.wadl.annotation.XMLCast;
import client.crud.Service;
import client.exception.ValidationException;
import client.request.ApacheRequest;
import client.response.JSONResponse;

import com.google.gson.reflect.TypeToken;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author azizsault
 */
@XMLCast(thisClassFrom = "models.Produto")
public class Produto implements DefaultModel<Produto> {

    @GridHeader(name = "ID", size = 10)
    public Long id;
    @GridHeader(name = "Nome", size = 50)
    public String nome;
    @GridHeader(name = "descricao", size = 200)
    public String descricao;
    @GridHeader(name = "valor", size = 12)
    public double valor;
    @GridHeader(name = "Qtd", size = 12)
    public int quantidade;
    public static Service<Produto> service = new Service<Produto>(new ApacheRequest("http://localhost:9000/api/produtos"), Produto.class,
            new TypeToken<List<Produto>>() {
            }.getType());

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public List<Produto> findStart() {
        return service.findAll();
    }

    @Override
    public List<Produto> filterGrid(String filter) {

        if ("".equals(filter) || filter.equals("*")) {
            return service.findAll();
        }

        ApacheRequest req = new ApacheRequest("http://localhost:9000/api/produtos");
        JSONResponse<Produto> json = new JSONResponse<Produto>(req.get("/findName/" + filter));
        List<Produto> lista = null;
        try {
            lista = json.getCollectionContent(new TypeToken<List<Produto>>() {
            }.getType());
        } catch (Exception ex) {
            Logger.getLogger(Produto.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Produto> filtro = new ArrayList<Produto>();
        for (Produto user : lista) {
            if (user.nome.toUpperCase().startsWith(filter.toUpperCase())) {
                filtro.add(user);
            }
        }
        return filtro;

    }

    @Override
    public Produto findById(final Long id) {
        return service.findById(id);
    }

    @Override
    public Produto save(final Long id, final HashMap<String, Object> map) throws ValidationException {
        final HashMap<String, String> vo = new HashMap<String, String>();
        vo.put("produto.nome", map.get("Nome").toString());
        vo.put("produto.descricao", map.get("Descricao").toString());
        vo.put("produto.quantidade", map.get("Quantidade").toString());
        vo.put("produto.valor", map.get("Valor").toString());
        return service.save(id, vo);
    }

    @Override
    public boolean delete(final Long id) {
        return service.delete(id);
    }

    @Override
    public ModelField getGridFields() {
        final ModelField gf = new ModelField();
        gf.addField(this.id);
        gf.addField(this.nome);
        gf.addField(this.descricao);
        gf.addField(this.quantidade);
        gf.addField(this.valor);
        return gf;
    }

    @Override
    public ModelField getViewFields() {
        final ModelField ff = new ModelField();
        ff.addField("Nome", this.nome);
        ff.addField("Descricao", this.descricao);
        ff.addField("Quantidade", this.quantidade);
        ff.addField("Valor", this.valor);
        return ff;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public List<? extends DefaultModel> getObj(final String campo) {
        return null;
    }
}
