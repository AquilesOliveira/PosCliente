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
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import swing.ui.ModelField;

/**
 *
 * @author itakenami
 */
@XMLCast(thisClassFrom = "models.Usuario")
public class Usuario implements DefaultModel<Usuario> {

    @GridHeader(name = "ID", size = 10)
    public Long id;
    @GridHeader(name = "Nome", size = 200)
    public String nome;
    @GridHeader(name = "Email", size = 100)
    public String email;
    
    public static Service<Usuario> service = new Service<Usuario>(new ApacheRequest("http://localhost:9000/api/usuarios"), Usuario.class, new TypeToken<List<Usuario>>() {}.getType());
    

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public List<Usuario> findStart() {
        //return service.findAll();
        return new LinkedList<Usuario>();
    }
    
    @Override
    public List<Usuario> filterGrid(String filter) {
        
        List<Usuario> lista = service.findAll();
        
        if("".equals(filter) || filter.equals("*")){
            return lista;
        }
        
        List<Usuario> filtro = new ArrayList<Usuario>();
        for (Usuario user : lista) {
            if(user.nome.toUpperCase().startsWith(filter.toUpperCase())){
                filtro.add(user);
            }
        }
        return filtro;
        
    }

    @Override
    public Usuario findById(Long id) {
        return service.findById(id);
    }

    @Override
    public Usuario save(Long id, HashMap<String, Object> map) throws ValidationException {
        HashMap<String, String> vo = new HashMap<String, String>();
        vo.put("usuario.nome", map.get("Nome").toString());
        vo.put("usuario.email", map.get("Email").toString());
        return service.save(id, vo);
    }

    @Override
    public boolean delete(Long id) {
        return service.delete(id);
    }

    @Override
    public ModelField getGridFields() {
        ModelField gf = new ModelField();
        gf.addField(id);
        gf.addField(nome);
        gf.addField(email);
        return gf;
    }

    @Override
    public ModelField getViewFields() {
        ModelField ff = new ModelField();
        ff.addField("Nome", nome);
        ff.addField("Email", email);
        return ff;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<? extends DefaultModel> getObj(String campo) {
        return null;
    }
}
