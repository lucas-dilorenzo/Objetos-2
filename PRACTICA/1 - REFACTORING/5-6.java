/*
Ejercicio 5 - Productos
Tarea 6: Feature Envy en los métodos price()

-------------------------------------------------------------------------
Code smell identificado: Feature Envy
-------------------------------------------------------------------------

Ambos métodos price() acceden principalmente a datos de otras clases:
*/

// HotelStay — usa datos de Hotel
public double price() {
    return this.timePeriod.duration() * this.hotel.nightPrice() * this.hotel.discountRate();
}

// CarRental — usa datos de Company
public double price() {
    return this.company.price() * this.company.promotionRate();
}

/*
-------------------------------------------------------------------------
Refactoring aplicado: Move Method
-------------------------------------------------------------------------

Paso 1: Mover la lógica de cálculo a las clases donde viven los datos.
Se crea un método en Hotel y otro en Company con la lógica correspondiente.
*/

public class Hotel {
    // ... 
    public double precioConDescuento() {
        return this.nightPrice() * this.discountRate();
    }
}

public class Company {
    // ...
    public double precioConPromocion() {
        return this.price() * this.promotionRate();
    }
}

/*
Paso 2: Actualizar los métodos price() en HotelStay y CarRental
para delegar el cálculo a las clases correspondientes.
*/

public class HotelStay extends Product {
    public double price() {
        return this.timePeriod.duration() * this.hotel.precioConDescuento();
    }
}

public class CarRental extends Product {
    public double price() {
        return this.company.precioConPromocion();
    }
}

/*
Nota: en HotelStay todavía se accede a this.timePeriod.duration(), 
lo cual podría considerarse Feature Envy con TimePeriod. Sin embargo, 
dado que timePeriod fue subido a Product en la tarea anterior y es 
un campo propio de la jerarquía, se considera aceptable en este contexto.

Los tests deben seguir pasando ya que el comportamiento observable 
no cambió, solo se redistribuyeron las responsabilidades.
*/