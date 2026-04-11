/*
Ejercicio 3 - Iteradores circulares bis

Code smell identificado: Duplicate Code
- El campo `idx` se repite en ambas clases con el mismo nombre y tipo (int)
- La lógica del reset `if (idx >= source.length) idx = 0` es idéntica en ambos `next()`
- El campo `source` NO se puede subir porque tiene distinto tipo en cada clase

Refactoring a aplicar: Extract Superclass

Paso 1: Crear la superclase Ring y aplicar Pull Up Field sobre `idx`
*/

public abstract class Ring {
    protected int idx;

    public Ring() {
        idx = 0;
    }
}

/*
Paso 2: Aplicar Extract Method sobre la lógica del reset en cualquiera 
de las dos clases, para poder luego subirla
*/

public class CharRing {
    private char[] source;
    private int idx;

    public char next() {
        resetIfNeeded(source.length); // extraído
        return source[idx++];
    }

    private void resetIfNeeded(int length) {
        if (idx >= length)
            idx = 0;
    }
}

/*
Paso 3: Aplicar Pull Up Method sobre resetIfNeeded() subiéndolo a Ring,
ya que la lógica es idéntica en ambas clases y puede acceder a `idx` 
desde la superclase
*/

public abstract class Ring {
    protected int idx;

    public Ring() {
        idx = 0;
    }

    protected void resetIfNeeded(int length) {
        if (idx >= length)
            idx = 0;
    }
}

/*
Paso 4: Declarar next() como abstracto en Ring.
No se puede subir completo porque retorna tipos distintos (char vs int)
y accede a source que es distinto en cada subclase.
La superclase define que toda Ring debe tener un next(), 
pero cada subclase implementa la suya.
*/

public abstract class Ring {
    protected int idx;

    public Ring() {
        idx = 0;
    }

    protected void resetIfNeeded(int length) {
        if (idx >= length)
            idx = 0;
    }

    public abstract Object next();
}

/*
Paso 5: Hacer que CharRing e IntRing extiendan Ring
y eliminar el campo idx y la lógica duplicada de cada una
*/

public class CharRing extends Ring {
    private char[] source;

    public CharRing(String src) {
        source = src.toCharArray();
    }

    public char next() {
        resetIfNeeded(source.length);
        return source[idx++];
    }
}

public class IntRing extends Ring {
    private int[] source;

    public IntRing(int[] src) {
        source = src;
    }

    public int next() {
        resetIfNeeded(source.length);
        return source[idx++];
    }
}

/*
Resultado: el Duplicate Code fue eliminado. 
La superclase Ring centraliza el estado compartido (idx) 
y el comportamiento común (resetIfNeeded), mientras que 
cada subclase mantiene su propio source y su implementación de next().

Los tests definidos previamente deben seguir pasando ya que 
el comportamiento observable no cambió.
*/