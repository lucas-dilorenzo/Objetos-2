/*
Ejercicio 5 - Productos
Tareas 3, 4 y 5: Pull Up Method — startDate() y endDate()

-------------------------------------------------------------------------
Tarea 3: ¿Es posible aplicar Pull Up Method directamente?
-------------------------------------------------------------------------

No es posible aplicar Pull Up Method directamente sobre startDate() y 
endDate() porque ambos métodos acceden a `timePeriod`, que está declarado 
como campo privado en cada subclase:
*/

// HotelStay
private TimePeriod timePeriod;

// CarRental
private TimePeriod timePeriod;

/*
Una precondición de Pull Up Method es que todos los elementos que usa 
el método deben ser accesibles desde la superclase. Al estar `timePeriod` 
en las subclases, Product no puede acceder a él.

-------------------------------------------------------------------------
Tarea 4: Refactorings previos necesarios
-------------------------------------------------------------------------

1. Pull Up Field: subir `timePeriod` a Product
2. Change Access Modifier: cambiar su visibilidad de private a protected,
   para que las subclases puedan seguir accediendo a él

-------------------------------------------------------------------------
Tarea 5: Aplicar los refactorings
-------------------------------------------------------------------------

Paso 1: Pull Up Field — timePeriod sube a Product con visibilidad protected
*/

public class Product {
    protected TimePeriod timePeriod; // subido desde las subclases
}

/*
Paso 2: Eliminar timePeriod de HotelStay y CarRental
*/

public class HotelStay extends Product {
    // private TimePeriod timePeriod; — eliminado
}

public class CarRental extends Product {
    // private TimePeriod timePeriod; — eliminado
}

/*
Paso 3: Pull Up Method — startDate() y endDate() suben a Product
*/

public class Product {
    protected TimePeriod timePeriod;

    public LocalDate startDate() {
        return this.timePeriod.start();
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }
}

/*
Los tests deben seguir pasando ya que el comportamiento observable 
no cambió, solo se reorganizó dónde vive el código.
*/