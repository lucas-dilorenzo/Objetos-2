/*
Ejercicio 6.6 - Películas

-------------------------------------------------------------------------
Iteración 1
-------------------------------------------------------------------------

Code smell identificado: Switch Statements
El método calcularCostoPelicula() tiene un if/else por cada tipo de 
suscripción. Si se agrega una nueva suscripción hay que modificar
este método, violando el Open/Closed Principle.

Refactoring aplicado: Replace Conditional with Polymorphism

Paso 1: crear la jerarquía de suscripciones.
El campo tipoSubscripcion (String) se reemplaza por una clase abstracta
Suscripcion con una subclase por cada tipo.
Cada subclase implementa su propio cálculo de costo.
*/

public abstract class Suscripcion {
    public abstract double calcularCosto(Pelicula pelicula);
}

public class Basico extends Suscripcion {
    public double calcularCosto(Pelicula pelicula) {
        return pelicula.getCosto() + pelicula.calcularCargoExtraPorEstreno();
    }
}

public class Familia extends Suscripcion {
    public double calcularCosto(Pelicula pelicula) {
        return (pelicula.getCosto() + pelicula.calcularCargoExtraPorEstreno()) * 0.90;
    }
}

public class Plus extends Suscripcion {
    public double calcularCosto(Pelicula pelicula) {
        return pelicula.getCosto();
    }
}

public class Premium extends Suscripcion {
    public double calcularCosto(Pelicula pelicula) {
        return pelicula.getCosto() * 0.75;
    }
}

/*
Paso 2: actualizar Usuario para que use la jerarquía.
El campo tipoSubscripcion (String) se reemplaza por una referencia
a Suscripcion. El if/else desaparece completamente: Usuario delega
el cálculo a su suscripción.
*/

public class Usuario {
    private Suscripcion suscripcion;

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public double calcularCostoPelicula(Pelicula pelicula) {
        return this.suscripcion.calcularCosto(pelicula);
    }
}

/*
Resultado final: el if/else fue eliminado completamente.
Cada tipo de suscripción encapsula su propia lógica de cálculo.
Para agregar una nueva suscripción basta con crear una nueva subclase
sin tocar el código existente — Open/Closed Principle respetado.
*/