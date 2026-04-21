/*
Ejercicio 9 - Pedidos

-------------------------------------------------------------------------
Código inicial
-------------------------------------------------------------------------
*/

public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private String formaPago;

    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago)
            && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
            throw new Error("Forma de pago incorrecta");
        }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = 0;
        for (Producto producto : this.productos) {       // líneas 16-19
            costoProductos += producto.getPrecio();
        }
        double extraFormaPago = 0;
        if ("efectivo".equals(this.formaPago)) {         // líneas 21-27
            extraFormaPago = 0;
        } else if ("6 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.2;
        } else if ("12 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.5;
        }
        int añosDesdeFechaAlta = Period.between(        // líneas 28-33
            this.cliente.getFechaAlta(), LocalDate.now()).getYears();
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }
}

public class Cliente {
    private LocalDate fechaAlta;

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }
}

public class Producto {
    private double precio;

    public double getPrecio() {
        return this.precio;
    }
}

/*
-------------------------------------------------------------------------
Paso 1: Replace Loop with Pipeline (líneas 16-19)
-------------------------------------------------------------------------

Code smell: el for recorre productos para sumar precios.
Se reemplaza con una pipeline de streams.
*/

public double getCostoTotal() {
    double costoProductos = productos.stream()
        .mapToDouble(p -> p.getPrecio())
        .sum();
    double extraFormaPago = 0;
    if ("efectivo".equals(this.formaPago)) {
        extraFormaPago = 0;
    } else if ("6 cuotas".equals(this.formaPago)) {
        extraFormaPago = costoProductos * 0.2;
    } else if ("12 cuotas".equals(this.formaPago)) {
        extraFormaPago = costoProductos * 0.5;
    }
    int añosDesdeFechaAlta = Period.between(
        this.cliente.getFechaAlta(), LocalDate.now()).getYears();
    if (añosDesdeFechaAlta > 5) {
        return (costoProductos + extraFormaPago) * 0.9;
    }
    return costoProductos + extraFormaPago;
}

/*
-------------------------------------------------------------------------
Paso 2: Replace Conditional with Polymorphism (líneas 21-27)
-------------------------------------------------------------------------

Code smell: Switch Statements — el if discrimina por tipo de forma de pago.
Se usa una interfaz Pago (no clase abstracta, ya que no hay estado ni
comportamiento concreto compartido). Cada forma de pago implementa
calcularExtra(double costoProductos) devolviendo el monto adicional.
El campo formaPago cambia de String a Pago.
*/

public interface Pago {
    double calcularExtra(double costoProductos);
}

public class Efectivo implements Pago {
    public double calcularExtra(double costoProductos) {
        return 0;
    }
}

public class SeisCuotas implements Pago {
    public double calcularExtra(double costoProductos) {
        return costoProductos * 0.2;
    }
}

public class DoceCuotas implements Pago {
    public double calcularExtra(double costoProductos) {
        return costoProductos * 0.5;
    }
}

// Pedido — el if desaparece, se delega a formaPago
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private Pago formaPago; // era String, ahora es Pago

    public double getCostoProductos() {
        return productos.stream()
            .mapToDouble(p -> p.getPrecio())
            .sum();
    }

    public double getCostoTotal() {
        double costoProductos = getCostoProductos();
        double extraFormaPago = this.formaPago.calcularExtra(costoProductos);
        int añosDesdeFechaAlta = Period.between(
            this.cliente.getFechaAlta(), LocalDate.now()).getYears();
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }
}

/*
-------------------------------------------------------------------------
Paso 3: Extract Method y Move Method (línea 28)
-------------------------------------------------------------------------

Code smell: Feature Envy — Pedido accede a cliente.getFechaAlta()
para calcular algo que le corresponde a Cliente.

Se aplica Extract Method sobre la línea 28 y luego Move Method
para mover el cálculo a Cliente.
*/

public class Cliente {
    private static final int AÑOS_ANTIGÜEDAD = 5;
    private LocalDate fechaAlta;

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    // método movido desde Pedido — Extract Method + Move Method
    public int añosDesdeFechaAlta() {
        return Period.between(this.fechaAlta, LocalDate.now()).getYears();
    }

    // encapsula la regla de negocio — más cohesivo que exponer el int
    public boolean esClienteAntiguo() {
        return this.añosDesdeFechaAlta() > AÑOS_ANTIGÜEDAD;
    }
}

// Pedido — usa el nuevo método de Cliente
public double getCostoTotal() {
    double costoProductos = getCostoProductos();
    double extraFormaPago = this.formaPago.calcularExtra(costoProductos);
    if (this.cliente.esClienteAntiguo()) {
        return (costoProductos + extraFormaPago) * 0.9;
    }
    return costoProductos + extraFormaPago;
}

/*
-------------------------------------------------------------------------
Paso 4: Extract Method y Replace Temp with Query (líneas 28-33)
-------------------------------------------------------------------------

Code smell: variable temporal añosDesdeFechaAlta que se usa solo
para el condicional. Se aplica Replace Temp with Query para eliminarla
y luego Extract Method para extraer el bloque en un método propio.
*/

// Replace Temp with Query — se elimina la variable temporal
// y se usa la llamada directamente en el if
// Luego Extract Method del bloque completo

private double costoFinal(double costoProductos, double extraFormaPago) {
    if (this.cliente.esClienteAntiguo()) {
        return (costoProductos + extraFormaPago) * 0.9;
    }
    return costoProductos + extraFormaPago;
}

// getCostoTotal() queda limpio y legible
public double getCostoTotal() {
    double costoProductos = getCostoProductos();
    double extraFormaPago = this.formaPago.calcularExtra(costoProductos);
    return costoFinal(costoProductos, extraFormaPago);
}

/*
Resultado final: getCostoTotal() delega cada responsabilidad
a quien corresponde:
- El stream calcula el total de productos
- FormaPago calcula el extra según el tipo de pago
- Cliente sabe sus propios años de antigüedad
- costoFinal() aplica el descuento si corresponde

Para el diagrama de clases final:
- Pedido → tiene 1 Cliente, muchos Producto, 1 FormaPago
- FormaPago (abstracta) → SeisCuotas, DoceCuotas, Efectivo
- Cliente → añosDesdeFechaAlta()
- Producto → getPrecio()
*/