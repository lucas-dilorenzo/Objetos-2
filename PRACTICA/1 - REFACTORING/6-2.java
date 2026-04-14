/*
Ejercicio 6.2 - Juego

-------------------------------------------------------------------------
Iteración 1
-------------------------------------------------------------------------

Code smells identificados:
1. Encapsulamiento roto: los campos de Jugador son públicos, cualquier 
   clase puede modificarlos directamente sin ningún control
2. Feature Envy: los métodos incrementar() y decrementar() de Juego 
   manipulan directamente el estado interno de Jugador

Refactoring aplicado: Encapsulate Field + Move Method

Paso 1: cambiar la visibilidad de los campos de Jugador a private
Paso 2: mover la lógica de modificación de puntuación a Jugador
*/

public class Jugador {
    private String nombre;
    private String apellido;
    private int puntuacion = 0;

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public void incrementarPuntuacion() {
        this.puntuacion += 100;
    }

    public void decrementarPuntuacion() {
        this.puntuacion -= 50;
    }
}

/*
Paso 3: actualizar Juego para que delegue la responsabilidad a Jugador.
Juego decide cuándo modificar la puntuación, pero Jugador sabe cómo.
*/

public class Juego {
    public void incrementar(Jugador j) {
        j.incrementarPuntuacion();
    }

    public void decrementar(Jugador j) {
        j.decrementarPuntuacion();
    }
}

/*
Resultado final: Jugador es dueño de su propio estado. 
Juego solo coordina cuándo deben ocurrir los cambios, 
pero no manipula directamente los datos de Jugador.
Este es un principio clave de OOP: los objetos deben ser 
responsables de su propio estado.
*/