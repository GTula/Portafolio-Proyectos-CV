package uy.edu.ucu.aed.AppRedSocial;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uy.edu.ucu.aed.IArista;
import uy.edu.ucu.aed.ManejadorArchivosGenerico;
import uy.edu.ucu.aed.TArista;
import uy.edu.ucu.aed.UtilGrafos;

public class UtilGrafosRed {
        public static TGrafoRedSocial cargarGrafoRed(String nomArchVert, String nomArchAdy, boolean ignoreHeader) {
        String[] vertices = ManejadorArchivosGenerico.leerArchivo(nomArchVert, ignoreHeader);
        String[] aristas = ManejadorArchivosGenerico.leerArchivo(nomArchAdy, ignoreHeader);

        List<TUsuario> usuariosList = new ArrayList<>(vertices.length); 
        List<IArista> aristasList = new ArrayList<>(aristas.length);

        for (String verticeString : vertices) {
            if ((verticeString != null) && !verticeString.trim().isEmpty()) { // si la linea no es nula
                try {
                    String[] datos = verticeString.split(",");
                    String id = datos[0]; //el primer dato es el id
                    String interes = datos[1]; //segundo dato el interés
                    TUsuario usuario = new TUsuario(id, interes); 
                    usuariosList.add(usuario); //se agrega el nuevo usario a la lista
                } catch (Exception ex) {
                    Logger.getLogger(UtilGrafos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        for (String aristaString : aristas) {
            if ((aristaString != null) && !aristaString.trim().isEmpty()) { //si la arista no es nula
                String[] datos = aristaString.split(",");
                aristasList.add(new TArista(datos[0], datos[1], 0)); //el primer dato es el origen y el segundo el destino
                //(da igual en realidad ya que es no dirigida) costo 0 porque es no dirigida
            }
        }

        try {
            return new TGrafoRedSocial(usuariosList, aristasList); //al final se retorna el nuevo grafo
        } catch (Exception ex) {
            Logger.getLogger(UtilGrafos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
