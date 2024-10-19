package uy.edu.ucu.aed.AppRedSocial;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import uy.edu.ucu.aed.IArista;
import uy.edu.ucu.aed.IVertice;

public interface IGrafoRedSocial {
    void agregarUsuario(TUsuario usuario);

    void eliminarUsuario(TUsuario usuario);

    void agregarAmistad(TUsuario usuario1, TUsuario usuario2);

    void eliminarAmistad(TUsuario usuario1, TUsuario usuario2);

    Collection<IVertice<TUsuario>> getUsuarios();

    //amigos de amigos con la cantidad de amigos en común
    Map<TUsuario, Integer> amigosDeAmigos(Comparable usuarioEti);

    //posibles amigos pertenecientes a grupos con el mismo interés
    Collection<TUsuario> amigosDeComunidad(TUsuario usuario);
}
