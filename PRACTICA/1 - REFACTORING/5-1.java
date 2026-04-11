/*
Ejercicio 5 - Productos
Tarea 1: Encapsulate Field

Code smell identificado: el campo `cost` está declarado como public en 
HotelStay y CarRental, lo que rompe el encapsulamiento permitiendo que 
cualquier clase externa modifique el valor directamente sin ningún control.

Refactoring aplicado: Encapsulate Field
Se hace private el campo y se generan getter y setter públicos.
*/

public class HotelStay extends Product {
    private double cost; // era public, ahora private

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    // ... resto de la clase
}

public class CarRental extends Product {
    private double cost; // era public, ahora private

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    // ... resto de la clase
}

/*
a) ¿Es correcto modificar los tests para que el código refactorizado funcione?

No debería ser necesario si los tests están bien escritos, es decir si 
acceden al campo a través de métodos y no directamente. Un test bien 
implementado debe ser agnóstico de la implementación interna de la clase.
La única razón válida para modificar un test es que el test estaba mal escrito.

b) ¿Qué situación representa un test que falla?

Si un test falla luego de aplicar Encapsulate Field, significa que el test 
estaba acoplado a la implementación interna, accediendo directamente 
al campo público:
*/

// Test mal escrito — accede directamente al campo público
hotelStay.cost = 100.0; // esto rompe con Encapsulate Field

// Test bien escrito — usa el setter
hotelStay.setCost(100.0); // esto sigue funcionando

/*
En ese caso sí es válido modificar el test para que use el getter/setter,
porque el problema estaba en el test, no en el refactoring.
*/
