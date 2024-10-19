package uy.edu.ucu.aed.AppRedSocial;

import java.util.*;

import uy.edu.ucu.aed.IVertice;
import uy.edu.ucu.aed.UtilGrafos;

public class Programa {
    public static void main(String[] args) {
        System.out.println("Inicio del programa");
        //Se carga el nuevo grafo
        TGrafoRedSocial grafo = UtilGrafosRed.cargarGrafoRed(
            "src\\main\\java\\uy\\edu\\ucu\\aed\\AppRedSocial\\usuarios.txt",
            "src\\main\\java\\uy\\edu\\ucu\\aed\\AppRedSocial\\aristas.txt",
            false
        );

        //se crea un nuevo usuario y se agrega al grafo
        TUsuario usuarioNuevo = new TUsuario("Usuario_21", "futbol");
        grafo.insertarVertice(usuarioNuevo);

        System.out.println("---------");

        //se muestran los posibles amigos con el mismo interés
        Collection<TUsuario> amigosInteres = grafo.amigosDeComunidad(usuarioNuevo);
        System.out.println("Posibles amigos que comparten tu mismo interés:");
        if (amigosInteres != null) {
            for (TUsuario amigo : amigosInteres) {
                System.out.println(amigo.getEtiqueta());
            }
        } else {
            System.out.println("No se encontraron amigos con tu mismo interés");
        }

        System.out.println("--------");
        //se busca un par de ususarios para agregar la amistad con el nuevo
        TUsuario us1 = (TUsuario) grafo.buscarVertice("Usuario_1");
        TUsuario us8 = (TUsuario) grafo.buscarVertice("Usuario_8");
        TUsuario us15 = (TUsuario) grafo.buscarVertice("Usuario_15");
        grafo.agregarAmistad(usuarioNuevo, us8);
        grafo.agregarAmistad(usuarioNuevo, us1);
        grafo.agregarAmistad(usuarioNuevo, us15);


        //se muestran los posibles amigos de amigos con la cantidad de amigos en común
        Map<TUsuario, Integer> posiblesAmigos = grafo.amigosDeAmigos("Usuario_21");

        System.out.println("Posibles amigos que conozcas:");
        for (TUsuario posibleAmigo : posiblesAmigos.keySet()) {
            System.out.println(posibleAmigo.getEtiqueta() + " " + posiblesAmigos.get(posibleAmigo));
        }

        System.out.println("--------");
        TClasificador clasificador = new TClasificador();
        // se llama al método de ordenación heapsort para que ordene el map de amigos con respecto a los amigos en común
        Map<TUsuario, Integer> mapaOrdenado = clasificador.ordenarPorHeapSort(posiblesAmigos);

        //Se imprime el mapa ordenado
        System.out.println("Posibles amigos mostrados en orden:");
        for (Map.Entry<TUsuario, Integer> entrada : mapaOrdenado.entrySet()) {
            System.out.println(entrada.getKey().getEtiqueta() + " => " + entrada.getValue());
        }
    }
}
