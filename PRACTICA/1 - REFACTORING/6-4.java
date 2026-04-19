/*
Ejercicio 6.4 - Carrito de compras

-------------------------------------------------------------------------
Iteración 1
-------------------------------------------------------------------------

Code smells identificados:
1. Message Chain: item.getProducto().getPrecio() — Carrito atraviesa
   dos clases para llegar al precio del producto
2. Feature Envy: Carrito.total() usa datos de ItemCarrito y Producto
   para calcular algo que le corresponde a ItemCarrito

Refactorings aplicados: Hide Delegate + Move Method

Paso 1: eliminar la Message Chain agregando un método en ItemCarrito
que exponga el precio directamente, sin exponer el Producto
*/

public class ItemCarrito {
    private Producto producto;
    private int cantidad;

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    // nuevo método — oculta el acceso a Producto
    public double precio() {
        return this.producto.getPrecio();
    }
}

/*
Paso 2: mover la responsabilidad del cálculo del subtotal a ItemCarrito.
Carrito no debería saber cómo se calcula el subtotal de un item.
*/

public class ItemCarrito {
    private Producto producto;
    private int cantidad;

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public double precio() {
        return this.producto.getPrecio();
    }

    // nuevo método — ItemCarrito es responsable de su propio subtotal
    public double subtotal() {
        return this.precio() * this.cantidad;
    }
}

/*
Paso 3: actualizar Carrito para que delegue el cálculo a ItemCarrito.
Carrito solo suma los subtotales, sin conocer la estructura interna
de ItemCarrito ni de Producto.
*/

public class Carrito {
    private List<ItemCarrito> items;

    public double total() {
        return this.items.stream()
            .mapToDouble(item -> item.subtotal())
            .sum();
    }
}

/*
Resultado final: cada clase es responsable de su propio cálculo.
- Producto sabe su precio
- ItemCarrito sabe su subtotal
- Carrito solo suma los subtotales

Carrito ya no necesita conocer la estructura interna de ItemCarrito
ni de Producto. La Message Chain fue eliminada.
*/