/*
Ejercicio 5 - Productos
Tarea 2: Rename Field — cost → quote en HotelStay

Se renombra el campo `cost` por `quote` en HotelStay.
Los lugares afectados son:
*/

public class HotelStay extends Product {
    public double quote; // línea 2: declaración renombrada

    public HotelStay(double quote, TimePeriod timePeriod, Hotel hotel) { // línea 6: parámetro renombrado
        this.quote = quote; // línea 7: asignación actualizada
        this.timePeriod = timePeriod;
        this.hotel = hotel;
    }

    public double priceFactor() {
        return this.quote / this.price(); // línea 21: referencia actualizada
    }
    // ... resto de la clase
}

/*
¿Es necesario modificar algún test?

No tenemos visibilidad de los tests, pero dado que `cost` es un campo público,
cualquier clase externa — incluyendo los tests — podría estar accediendo 
directamente a él. Si algún test asigna o lee hotelStay.cost directamente,
al renombrarlo a quote ese test rompería y habría que actualizarlo.

A diferencia del Rename Method donde el compilador detecta todos los 
lugares afectados, un campo público puede ser accedido desde cualquier 
lugar, lo que hace este rename más riesgoso y requiere una búsqueda 
exhaustiva de todas sus referencias.
*/