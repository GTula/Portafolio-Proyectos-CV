package uy.edu.ucu.aed.AppRedSocial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import uy.edu.ucu.aed.IAdyacencia;
import uy.edu.ucu.aed.IVertice;
import uy.edu.ucu.aed.TVertice;

import uy.edu.ucu.aed.TAdyacencia;
import uy.edu.ucu.aed.TArista;
import uy.edu.ucu.aed.TGrafoNoDirigido;

public class TUsuario extends TVertice implements IVertice{

    private String interes;

    public TUsuario(String id, String interes) {
        super(id);
        this.interes = interes;
    }

    public String getIntereses() {
        return interes;
    }
    
    public Comparable getId(){
        return this.getEtiqueta();
    }

    public Collection<TUsuario> getAmigos(){
        Collection<TUsuario> amigos = new ArrayList<>();
        for(Object adyacente : this.adyacentes){
            TUsuario amigo = (TUsuario)((TAdyacencia)adyacente).destino;
            amigos.add(amigo);
        }
        return amigos;
    }

    public void componentesConexos(Collection<TGrafoRedSocial> componentes, Collection<IVertice<TUsuario>> usuarios) {
        for (IVertice<TUsuario> usuario : usuarios) { //O(n) se itera entre todos los usuarios
            TUsuario usuario1 = (TUsuario) usuario; //O(1) casteo para TUsuario
            if (!usuario1.getVisitado() && getIntereses().equals(usuario1.interes)) { //O(1)si no se visitó y tiene el mismo interés
                //se crea un nuevo grafo (ya que es un nuevo componente conexo)
                TGrafoRedSocial subgrafo = new TGrafoRedSocial(new ArrayList<>(), new ArrayList<>()); //O(1)
                componentes.add(subgrafo); //O(1)se coloca en la lista de grafos
                usuario1.componenteConexoAux(interes, subgrafo, getAmigos());  //se llama al metodo recursivo
            }
        }
    }

    private void componenteConexoAux(String interes, TGrafoRedSocial subgrafo, Collection<TUsuario> amigos) { 
        this.setVisitado(true); //O(1) setea al usuario como visitado
        if(!amigos.contains(this)){  //O(1) si pertenece al grupo de amigos no se agrega al grafo
            subgrafo.insertarVertice(this);// O(1)
        }
        for (Object adyacente : this.getAdyacentes()) { // O(n) se itera entre los amigos del usuario
            TUsuario destino = (TUsuario) ((TAdyacencia) adyacente).getDestino(); //O(1)
            if (destino.getIntereses().equals(interes) && !destino.getVisitado()) { //O(1) si tiene el mismo interes y no es visitado se procede
                subgrafo.insertarArista(new TArista(this.getId(), destino.getId(), 0)); //O(1) se inserte la arista entre el usuario y el amigo
                //arista de costo 0 debido a que es no dirigida
                destino.componenteConexoAux(interes, subgrafo, amigos); //se llama recursivamente
            }
        } //O(n)
    }

    public Map<TUsuario, Integer> amigosDeAmigos() {
        Map<TUsuario, Integer> amigosDeAmigos = new HashMap<>(); //O(1) Mapa para almacenar amigos de amigos y sus conteos
        Set<TUsuario> amigosVisitados = new HashSet<>(getAmigos()); //O(1) Conjunto de amigos visitados
    
        for (TUsuario amigo : amigosVisitados) { //O(n) Búsqueda en amplitud para encontrar amigos de amigos
            for (TUsuario amigoDeAmigo : amigo.getAmigos()) {//O(n)
                if (!amigoDeAmigo.equals(this) && !amigosVisitados.contains(amigoDeAmigo)) { //O(1) Si no es igual al usuario y no es amigo directo
                    amigosDeAmigos.put(amigoDeAmigo, amigosDeAmigos.getOrDefault(amigoDeAmigo, 0) + 1); //O(1) Agregar o actualizar el conteo
                }
            }
        }
        return amigosDeAmigos;
    }
    //O(n^2)
}