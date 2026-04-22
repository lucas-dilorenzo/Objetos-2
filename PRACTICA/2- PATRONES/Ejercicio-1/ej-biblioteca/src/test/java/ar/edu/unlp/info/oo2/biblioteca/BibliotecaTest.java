package ar.edu.unlp.info.oo2.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    private Biblioteca biblioteca;
    private Socio arya;
    private Socio tyron;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        arya  = new Socio("Arya Stark",    "needle@stark.com",        "5234-5");
        tyron = new Socio("Tyron Lannister","tyron@thelannisters.com", "2345-2");
        biblioteca.agregarSocio(arya);
        biblioteca.agregarSocio(tyron);
    }

    @Test
    void exportarConVoorheesProduceJSON() {
        String resultado = biblioteca.exportarSocios();
        assertTrue(resultado.contains("Arya Stark"));
        assertTrue(resultado.contains("needle@stark.com"));
        assertTrue(resultado.contains("5234-5"));
    }

    @Test
    void exportarConJSONSimpleProduceJSON() {
        biblioteca.setExporter(new JSONSimpleAdapter());
        String resultado = biblioteca.exportarSocios();
        assertTrue(resultado.contains("Arya Stark"));
        assertTrue(resultado.contains("needle@stark.com"));
        assertTrue(resultado.contains("5234-5"));
    }

    @Test
    void bibliotecaSinSociosDevuelveListaVacia() {
        Biblioteca vacia = new Biblioteca();
        vacia.setExporter(new JSONSimpleAdapter());
        assertEquals("[]", vacia.exportarSocios());
    }
}
