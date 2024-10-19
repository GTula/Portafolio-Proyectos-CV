package uy.edu.ucu.aed;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uy.edu.ucu.aed.AppRedSocial.TClasificador;
import uy.edu.ucu.aed.AppRedSocial.TGrafoRedSocial;
import uy.edu.ucu.aed.AppRedSocial.TUsuario;
import uy.edu.ucu.aed.AppRedSocial.UtilGrafosRed;

import java.util.*;
import java.util.stream.Collectors;

public class Parcial2Test_Junit4 {

    TGrafoRedSocial grafo;
    TUsuario usuario1;
    TUsuario usuario2;
    TUsuario usuario3;
    TUsuario usuario4;

    @Before
    public void setUp() {
        usuario1 = new TUsuario("Usuario_1", "futbol");
        usuario2 = new TUsuario("Usuario_2", "poesia");
        usuario3 = new TUsuario("Usuario_3", "futbol");
        usuario4 = new TUsuario("Usuario_4", "futbol");

        List<TUsuario> vertices = Arrays.asList(usuario1, usuario2, usuario3, usuario4);
        List<IArista> aristas = new ArrayList<>();

        grafo = new TGrafoRedSocial(vertices, aristas);
        grafo.agregarAmistad(usuario1, usuario3);
        grafo.agregarAmistad(usuario1, usuario4);
        grafo.agregarAmistad(usuario3, usuario4);
        grafo.agregarAmistad(usuario4, usuario2);

    }

@Test
    public void testCargarGrafoRed() {
        // Cargar el grafo desde los archivos de prueba
        TGrafoRedSocial grafo = UtilGrafosRed.cargarGrafoRed(
            "src\\test\\java\\uy\\edu\\ucu\\aed\\usuarios.txt",
            "src\\test\\java\\uy\\edu\\ucu\\aed\\aristas.txt",
            false
        );

        // Verificar que el grafo no es nulo
        assertNotNull(grafo);

        // Verificar el número de usuarios y aristas
        assertEquals(3, grafo.getVertices().size());
        assertEquals(6, grafo.getLasAristas().size()); //es un grafo no dirigido, por lo que no hay diferencia entre la direccion de las aristas

        // Verificar que todos los usuarios están en el grafo
        TUsuario usuario1 = (TUsuario) grafo.getVertices().get("Usuario_1");
        assertNotNull(usuario1);
        assertEquals("poesia", usuario1.getIntereses());

        TUsuario usuario2 = (TUsuario) grafo.getVertices().get("Usuario_2");
        assertNotNull(usuario2);
        assertEquals("futbol", usuario2.getIntereses());

        TUsuario usuario3 = (TUsuario) grafo.getVertices().get("Usuario_3");
        assertNotNull(usuario3);
        assertEquals("videojuegos", usuario3.getIntereses());

        // Verificar que algunas aristas están en el grafo
        TAristas aristas = grafo.getLasAristas();
        assertNotNull(aristas.buscar(usuario1.getId(), usuario2.getId())); //si son null significa que no existe arista
        assertNotNull(aristas.buscar(usuario3.getId(), usuario2.getId()));
        assertNotNull(aristas.buscar(usuario3.getId(), usuario1.getId()));
    }

    @Test
    public void testAgregarUsuario() {
        TUsuario nuevoUsuario = new TUsuario("Usuario_5", "musica");
        grafo.agregarUsuario(nuevoUsuario);
        assertNotNull(grafo.buscarVertice("Usuario_5")); //si se agregó, el método buscar debería devolver el TUsuario
    }

    @Test
    public void testEliminarUsuario() {
        grafo.eliminarUsuario(usuario4);
        assertNull(grafo.buscarVertice(usuario4.getEtiqueta()));
        assertEquals(false, grafo.existeArista(usuario1.getId(), usuario4.getId())); //debe ser falso ya que el usuario4 se eliminó
    }

    @Test
    public void testAgregarAmistad() {
        TUsuario nuevoUsuario = new TUsuario("Usuario_5", "musica");
        grafo.agregarUsuario(nuevoUsuario);

        //Se verifica que los usuarios existen en el grafo
        assertNotNull(grafo.buscarVertice(usuario1.getEtiqueta()), "Usuario 1 debería existir en el grafo.");
        assertNotNull(grafo.buscarVertice(nuevoUsuario.getEtiqueta()), "Usuario 5 debería existir en el grafo.");

        // Se agrega la amistad
        grafo.agregarAmistad(usuario1, nuevoUsuario);

        // Se verifica que la arista existe en el grafo
        assertEquals(true, grafo.existeArista(usuario1.getId(), nuevoUsuario.getId()));
        assertEquals(true, grafo.existeArista(nuevoUsuario.getId(), usuario1.getId())); //como es no dirigido debe tener una al revés
    }



    @Test
    public void testEliminarAmistad() {
        grafo.eliminarAmistad(usuario1, usuario3);
        assertEquals(false, grafo.existeArista(usuario1.getId(), usuario3.getId())); //debe ser falso ya que se eliminó la conexión
    }

    @Test
    public void testGrafoVacio() {
        TGrafoRedSocial grafoVacio = new TGrafoRedSocial(new ArrayList<>(), new ArrayList<>());
        assertEquals(0, grafoVacio.getVertices().size());
        assertEquals(0, grafoVacio.getLasAristas().size());
    }

    @Test
    public void testComponentesConexos() {
        //Se agrega un nuevo usuario que le gusta el fútbol pero sin amigos
        TUsuario nuevoUsuario = new TUsuario("nuevoUsuario", "futbol");
        grafo.agregarUsuario(nuevoUsuario);
        Collection<TGrafoRedSocial> componentes = grafo.componentesConexos(nuevoUsuario);

        assertEquals(2, componentes.size()); //debe haber dos grupos, ya que el nuevo usuario no tiene amigos(1 grupo solo de él)
    }

    @Test
    public void testComponentesConexosAmigos() {
        //Se agrega un nuevo usuario que le gusta el fútbol y ahora con amigos
        TUsuario nuevoUsuario = new TUsuario("nuevoUsuario", "futbol");
        grafo.agregarUsuario(nuevoUsuario);
        grafo.agregarAmistad(nuevoUsuario, usuario1);
        Collection<TGrafoRedSocial> componentes = grafo.componentesConexos(nuevoUsuario);

        assertEquals(1, componentes.size()); //debe haber solo 1 grupo, ya que todos se encuentran conectados

    }


    @Test
    public void amigosDeComunidadNuevoUsuario() {
        TUsuario usuarioNuevo = new TUsuario("Usuario_21", "futbol"); // se agrega un nuevo usuario
        grafo.insertarVertice(usuarioNuevo);
        grafo.agregarAmistad(usuarioNuevo, usuario3); //se agrega el enlace en otro amigo (usuario 1 no tiene conexion directa de amistad)
        Collection<TUsuario> amigosDeComunidad2 = grafo.amigosDeComunidad(usuario1); 
        assertEquals(1, amigosDeComunidad2.size()); //debe contener solo al usuario21 porque no es amigo y le gusta el fútbol
        assertTrue(amigosDeComunidad2.contains(usuarioNuevo)); 
    }

    @Test
    public void amigosDeComunidadNuevaAmistad() {
        TUsuario usuarioNuevo = new TUsuario("Usuario_21", "futbol"); // se agrega un nuevo usuario
        grafo.insertarVertice(usuarioNuevo);
        grafo.agregarAmistad(usuarioNuevo, usuario1); //se agrega el enlace
        Collection<TUsuario> amigosDeComunidad2 = grafo.amigosDeComunidad(usuario1); 
        assertEquals(0, amigosDeComunidad2.size()); //debe tener un tamaño de 0 ya que es amigo de todos a los que le gusta el fútbol
    }

    @Test
    public void testAmigosDeAmigos() {
        Map<TUsuario, Integer> amigosDeAmigos = grafo.amigosDeAmigos("Usuario_1");
        assertEquals(1, amigosDeAmigos.size()); //debe devolver solo 1 por el usuario1 ya es amigo del 4
        assertTrue(amigosDeAmigos.containsKey(usuario2)); //por lo que solo debe tener el usuario 2
    }

    @Test
    public void testOrdenarPorHeapSort() {
        TClasificador clasificador = new TClasificador();

        //se crea un map cualquiera con valores distintos
        Map<TUsuario, Integer> posiblesAmigos = new HashMap<>();
        posiblesAmigos.put(usuario2, 1); 
        posiblesAmigos.put(usuario3, 2);
        posiblesAmigos.put(usuario4, 4);

        //se invoca al metodo
        Map<TUsuario, Integer> mapaOrdenado = clasificador.ordenarPorHeapSort(posiblesAmigos);
        assertNotNull(mapaOrdenado);

        //se realiza un iterador para poder evaluar cada elemento del map
        Iterator<Map.Entry<TUsuario, Integer>> iterator = mapaOrdenado.entrySet().iterator(); 
        
        //el heap se realizó para que ordene de mayor a menor
        Map.Entry<TUsuario, Integer> entry = iterator.next();
        assertEquals(usuario4, entry.getKey()); 
        assertEquals(4, (int) entry.getValue());
        
        entry = iterator.next();
        assertEquals(usuario3, entry.getKey());
        assertEquals(2, (int) entry.getValue());
        
        entry = iterator.next();
        assertEquals(usuario2, entry.getKey());
        assertEquals(1, (int) entry.getValue());
    }

}
