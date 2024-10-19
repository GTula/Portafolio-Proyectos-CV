package uy.edu.ucu.aed.AppRedSocial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TClasificador {
   
    public Map<TUsuario, Integer> ordenarPorHeapSort(Map<TUsuario, Integer> datosParaClasificar) {
        // Conversión del Map a una List
        List<Map.Entry<TUsuario, Integer>> lista = new ArrayList<>(datosParaClasificar.entrySet());
    
        // Se arma el heap inicial desde (lista.size() - 1) / 2 hasta 0
        for (int i = (lista.size() - 1) / 2; i >= 0; i--) {
            armaHeap(lista, i, lista.size() - 1);
        }
        // Tiempo de ejecución: O(n)
    
        // Se ordenan los elementos en el heap
        for (int i = lista.size() - 1; i > 0; i--) {
            Collections.swap(lista, 0, i); // Tiempo de ejecución: O(1)
            armaHeap(lista, 0, i - 1);
        }
        // Tiempo de ejecución: O(n log n)
    
        // Se crea el mapa ordenado
        Map<TUsuario, Integer> datosClasificados = new LinkedHashMap<>();
        for (Map.Entry<TUsuario, Integer> par : lista) {
            datosClasificados.put(par.getKey(), par.getValue());
        }
        // Tiempo de ejecución: O(n)
    
        return datosClasificados;
    }
    
    private void armaHeap(List<Map.Entry<TUsuario, Integer>> lista, int primero, int ultimo) {
        if (primero < ultimo) {
            int r = primero;
            while (r <= ultimo / 2) { // Tiempo de ejecución: O(log n)
                if (ultimo == 2 * r) { // r tiene un solo hijo
                    if (lista.get(r).getValue() > lista.get(2 * r).getValue()) {
                        Collections.swap(lista, r, 2 * r); // Tiempo de ejecución: O(1)
                        r = 2 * r;
                    } else {
                        r = ultimo;
                    }
                } else { // r tiene dos hijos
                    int posicionIntercambio = 0;
                    if (lista.get(2 * r).getValue() > lista.get(2 * r + 1).getValue()) {
                        posicionIntercambio = 2 * r + 1;
                    } else {
                        posicionIntercambio = 2 * r;
                    }
                    if (lista.get(r).getValue() > lista.get(posicionIntercambio).getValue()) {
                        Collections.swap(lista, r, posicionIntercambio); // Tiempo de ejecución: O(1)
                        r = posicionIntercambio;
                    } else {
                        r = ultimo;
                    }
                }
            }
        }
    }
    
}
