package uy.edu.ucu.aed.AppRedSocial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uy.edu.ucu.aed.IArista;
import uy.edu.ucu.aed.IVertice;
import uy.edu.ucu.aed.TArista;
import uy.edu.ucu.aed.TGrafoNoDirigido;
import uy.edu.ucu.aed.TVertice;
import uy.edu.ucu.aed.TAdyacencia;

public class TGrafoRedSocial extends TGrafoNoDirigido<TUsuario> implements IGrafoRedSocial {
    
    @SuppressWarnings("unchecked")
    public TGrafoRedSocial(Collection<TUsuario> vertices, Collection<IArista> aristas) {
        super((Collection<IVertice<TUsuario>>) (Collection<?>) vertices, aristas);
    }


    @Override
    public void agregarUsuario(TUsuario usuario) {
        this.insertarVertice(usuario);
    }

    @Override
    public void eliminarUsuario(TUsuario usuario) {
        this.eliminarVertice(usuario.getId());
    }

    @Override
    public void agregarAmistad(TUsuario usuario1, TUsuario usuario2) {
        this.insertarArista(new TArista(usuario1.getId(), usuario2.getId(), 0));
    }

    @Override
    public void eliminarAmistad(TUsuario usuario1, TUsuario usuario2) {
        this.eliminarArista(usuario1.getId(), usuario2.getId());
    }

    @Override
    public Collection<IVertice<TUsuario>> getUsuarios() {
        return vertices.values();
    }


    public Collection<TGrafoRedSocial> componentesConexos(TUsuario usuario) {
        Collection<TGrafoRedSocial> componentes = new ArrayList<>(); //array con los componentes conexos
        for (IVertice<TUsuario> persona : getVertices().values()) {//se setea a todos los ususarios como no visitados
            persona.setVisitado(false);
        }

        if (getVertices().containsKey(usuario.getId())) { //si pertenece al grafo y no es nulo se realiza el método
            usuario.componentesConexos(componentes, getVertices().values()); //Se llama al método a nivel de vértice
            return componentes;
        }
        return null;
    }

    //Método para recomendar amigos que pertenecen a un grupo de amigos con el mismo interés
    public Collection<TUsuario> amigosDeComunidad(TUsuario usuario) {
        Collection<TGrafoRedSocial> componentes = componentesConexos(usuario); // lista con los componentes
        Collection<TUsuario> amigos = new ArrayList<>();
        for(TGrafoRedSocial grafo : componentes){ //O(n)
            for(IVertice<TUsuario> amigo : grafo.getVertices().values()){ //O(n) se pasan todos los posible amigos a un array
                amigos.add((TUsuario)amigo); //O(1)
            }
        }
        amigos.remove(usuario); //O(1) se elimina el propio usuario del array
        return amigos; //O(1) se devuelven los amigos con el mismo interés
    }//O(n^2)


    //método para recomendar amigos de amigos con el número de amigos en común
    public Map<TUsuario, Integer> amigosDeAmigos(Comparable usuarioEti) {
        TUsuario usuario = (TUsuario) this.buscarVertice(usuarioEti); //O(1) se busca al usuario
        for (IVertice<TUsuario> persona : getVertices().values()) { //O(n) se setea a todos los ususarios como no visitados
            persona.setVisitado(false); //O(1)
        }
        if (usuario != null && getVertices().containsKey(usuario.getId())) { //O(1) si pertenece al grafo y no es nulo se realiza el método
            return usuario.amigosDeAmigos();    //O(n^2) Se llama al método a nivel de vértice
        }
        return null;
    }//O(n^2)

}
