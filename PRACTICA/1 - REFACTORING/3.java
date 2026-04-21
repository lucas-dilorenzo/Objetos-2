/*
Ejercicio 3 - Iteradores circulares bis

Code smell identificado: Duplicate Code
- El campo `idx` se repite en ambas clases con el mismo nombre y tipo (int)
- La lógica del reset `if (idx >= source.length) idx = 0` es idéntica en ambos `next()`
- El campo `source` NO se puede subir porque tiene distinto tipo en cada clase

Refactoring a aplicar: Extract Superclass

Paso 1: Crear la superclase Ring y aplicar Pull Up Field sobre `idx`
Se introduce también la constante PRIMER_INDICE para evitar el magic number 0.
*/

public abstract class Ring {
    protected int idx;
    protected static final int PRIMER_INDICE = 0;

    public Ring() {
        idx = PRIMER_INDICE;
    }
}

/*
Paso 2: Aplicar Extract Method sobre la lógica del reset+avance en cualquiera
de las dos clases, para poder luego subirla.
Se extrae como indexNext(int length): combina el reset condicional y el retorno
del índice actual (con post-incremento) en un solo método.
*/

public class CharRing {
    private char[] source;
    private int idx;

    public char next() {
        return source[indexNext(source.length)];
    }

    private int indexNext(int length) {
        if (idx >= length)
            idx = PRIMER_INDICE;
        return idx++;
    }
}

/*
Paso 3: Aplicar Pull Up Method sobre indexNext() subiéndolo a Ring,
ya que la lógica es idéntica en ambas clases y puede acceder a `idx`
desde la superclase.
No se declara next() como abstracto en Ring porque los tipos de retorno
difieren (char vs int) — no es posible una firma común sin boxing.
Cada subclase tiene su propio next() con el tipo correcto.
*/

public abstract class Ring {
    protected int idx;
    protected static final int PRIMER_INDICE = 0;

    public Ring() {
        idx = PRIMER_INDICE;
    }

    protected int indexNext(int length) {
        if (idx >= length)
            idx = PRIMER_INDICE;
        return idx++;
    }
}

/*
Paso 4: Hacer que CharRing e IntRing extiendan Ring
y eliminar el campo idx y la lógica duplicada de cada una.
*/

public class CharRing extends Ring {
    private char[] source;

    public CharRing(String src) {
        super();
        source = src.toCharArray();
    }

    public char next() {
        return source[indexNext(source.length)];
    }
}

public class IntRing extends Ring {
    private int[] source;

    public IntRing(int[] src) {
        super();
        source = src;
    }

    public int next() {
        return source[indexNext(source.length)];
    }
}

/*
Resultado: el Duplicate Code fue eliminado.
La superclase Ring centraliza el estado compartido (idx, PRIMER_INDICE)
y el comportamiento común (indexNext), mientras que
cada subclase mantiene su propio source y su implementación de next()
con el tipo de retorno correcto.

Los tests definidos previamente deben seguir pasando ya que
el comportamiento observable no cambió.
*/