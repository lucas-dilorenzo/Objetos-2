/*
Ejercicio 7 - Etiquetas

-------------------------------------------------------------------------
Pregunta 1: ¿Hay código duplicado?
-------------------------------------------------------------------------

Sí, en los métodos generar() de ambas subclases:
- System.out.println("Producto: " + nombreProducto) — líneas 18 y 31
- System.out.println("-----------------------") — líneas 20 y 35

-------------------------------------------------------------------------
Pregunta 2: ¿Es posible aplicar Pull Up Method sobre generar()?
-------------------------------------------------------------------------

No es posible directamente. Una precondición de Pull Up Method es que
el cuerpo del método sea idéntico en ambas subclases. En este caso
generar() tiene diferencias:
- El encabezado es distinto en cada subclase
- Las líneas del precio son distintas (1 en Simple, 2 en Detalle)

-------------------------------------------------------------------------
Pregunta 3: Refactorings previos necesarios
-------------------------------------------------------------------------

Se necesitan varios refactorings intermedios antes de poder aplicar
Pull Up Method / Form Template Method:

-------------------------------------------------------------------------
Paso 1: Replace Magic Number with Symbolic Constant
-------------------------------------------------------------------------

Code smell: Magic Number — el valor 0.79 en EtiquetaDetalle no tiene
nombre descriptivo. Se lo reemplaza por una constante.
Además, los campos nombreProducto y precio son protected, lo que rompe
el encapsulamiento. Se los hace private y se agregan getters.
*/

abstract class Etiqueta {
    private String nombreProducto;
    private double precio;

    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }

    protected String getNombreProducto() {
        return this.nombreProducto;
    }

    protected double getPrecio() {
        return this.precio;
    }
}

class EtiquetaDetalle extends Etiqueta {
    private final static double IMPUESTO = 0.79; // era 0.79 literal

    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }

    public void generar() {
        System.out.println("--- ETIQUETA DETALLE ---");
        System.out.println("Producto: " + getNombreProducto());
        System.out.println("Precio sin imp.: $" + (getPrecio() * IMPUESTO));
        System.out.println("Precio final: $" + getPrecio());
        System.out.println("-----------------------");
    }
}

/*
-------------------------------------------------------------------------
Paso 2: Extract Method (Duplicate Code)
-------------------------------------------------------------------------

Se descompone generar() en métodos más pequeños en ambas subclases,
separando las partes comunes de las que varían.
Partes comunes: imprimirNombreDeProducto(), imprimirEtiquetaDeFin()
Partes variables: imprimirEtiquetaDeInicio(), imprimirPrecio()
En EtiquetaDetalle también: imprimirPrecioSinImpuesto()
*/

class EtiquetaSimple extends Etiqueta {
    public EtiquetaSimple(String nombre, double precio) {
        super(nombre, precio);
    }

    private void imprimirEtiquetaDeInicio() {
        System.out.println("--- ETIQUETA BÁSICA ---");
    }

    private void imprimirNombreDeProducto() {
        System.out.println("Producto: " + getNombreProducto());
    }

    private void imprimirPrecio() {
        System.out.println("Precio: $" + getPrecio());
    }

    private void imprimirEtiquetaDeFin() {
        System.out.println("-----------------------");
    }

    public void generar() {
        this.imprimirEtiquetaDeInicio();
        this.imprimirNombreDeProducto();
        this.imprimirPrecio();
        this.imprimirEtiquetaDeFin();
    }
}

class EtiquetaDetalle extends Etiqueta {
    private final static double IMPUESTO = 0.79;

    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }

    private void imprimirEtiquetaDeInicio() {
        System.out.println("--- ETIQUETA DETALLE ---");
    }

    private void imprimirNombreDeProducto() {
        System.out.println("Producto: " + getNombreProducto());
    }

    private void imprimirPrecioSinImpuesto() {
        System.out.println("Precio sin imp.: $" + (getPrecio() * IMPUESTO));
    }

    private void imprimirPrecio() {
        System.out.println("Precio final: $" + getPrecio());
    }

    private void imprimirEtiquetaDeFin() {
        System.out.println("-----------------------");
    }

    public void generar() {
        this.imprimirEtiquetaDeInicio();
        this.imprimirNombreDeProducto();
        this.imprimirPrecioSinImpuesto();
        this.imprimirPrecio();
        this.imprimirEtiquetaDeFin();
    }
}

/*
-------------------------------------------------------------------------
Paso 3: Pull Up Method (Duplicate Code)
-------------------------------------------------------------------------

Se suben a Etiqueta los métodos idénticos en ambas subclases:
imprimirNombreDeProducto() e imprimirEtiquetaDeFin().
*/

abstract class Etiqueta {
    private String nombreProducto;
    private double precio;

    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }

    protected String getNombreProducto() {
        return this.nombreProducto;
    }

    protected double getPrecio() {
        return this.precio;
    }

    protected void imprimirNombreDeProducto() {
        System.out.println("Producto: " + getNombreProducto());
    }

    protected void imprimirEtiquetaDeFin() {
        System.out.println("-----------------------");
    }
}

/*
-------------------------------------------------------------------------
Paso 4: Form Template Method (Duplicate Code)
-------------------------------------------------------------------------

Se sube generar() a la superclase como Template Method (final).
Las partes obligatorias que varían se declaran abstractas.
La parte opcional (imprimirPrecioSinImpuesto) se define como
hook method con cuerpo vacío — solo EtiquetaDetalle lo sobreescribe.
*/

abstract class Etiqueta {
    private String nombreProducto;
    private double precio;

    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }

    protected String getNombreProducto() {
        return this.nombreProducto;
    }

    protected double getPrecio() {
        return this.precio;
    }

    protected void imprimirNombreDeProducto() {
        System.out.println("Producto: " + getNombreProducto());
    }

    protected void imprimirPrecioSinImpuesto() {
        // hook method — vacío por defecto, EtiquetaDetalle lo sobreescribe
    }

    protected void imprimirEtiquetaDeFin() {
        System.out.println("-----------------------");
    }

    public final void generar() {
        this.imprimirEtiquetaDeInicio();
        this.imprimirNombreDeProducto();
        this.imprimirPrecioSinImpuesto();
        this.imprimirPrecio();
        this.imprimirEtiquetaDeFin();
    }

    protected abstract void imprimirEtiquetaDeInicio();
    protected abstract void imprimirPrecio();
}

class EtiquetaSimple extends Etiqueta {
    public EtiquetaSimple(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    protected void imprimirEtiquetaDeInicio() {
        System.out.println("--- ETIQUETA BÁSICA ---");
    }

    @Override
    protected void imprimirPrecio() {
        System.out.println("Precio: $" + getPrecio());
    }
}

class EtiquetaDetalle extends Etiqueta {
    private final static double IMPUESTO = 0.79;

    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    protected void imprimirEtiquetaDeInicio() {
        System.out.println("--- ETIQUETA DETALLE ---");
    }

    @Override
    protected void imprimirPrecioSinImpuesto() {
        System.out.println("Precio sin imp.: $" + (getPrecio() * IMPUESTO));
    }

    @Override
    protected void imprimirPrecio() {
        System.out.println("Precio final: $" + getPrecio());
    }
}

/*
Resultado final: generar() vive en un único lugar (Etiqueta) y es final.
Los pasos obligatorios que varían son abstractos (imprimirEtiquetaDeInicio,
imprimirPrecio). El paso opcional (imprimirPrecioSinImpuesto) es un hook
con cuerpo vacío — EtiquetaDetalle lo sobreescribe, EtiquetaSimple no.

Secuencia de refactorings aplicados:
1. Replace Magic Number with Symbolic Constant + Encapsulate Field
2. Extract Method (separar partes de generar())
3. Pull Up Method (subir las partes idénticas)
4. Form Template Method (subir generar() con hooks y métodos abstractos)
*/