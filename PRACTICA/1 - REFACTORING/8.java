/*
Ejercicio 8 - Documentos y estadísticas

-------------------------------------------------------------------------
Pregunta 1: Code smells y refactorings
-------------------------------------------------------------------------

Se detectan tres smells en el código inicial:

1. Public Field: `words` no tiene modificador de acceso → es package-private,
   lo que rompe el encapsulamiento.

2. Temporary Variable: `count` en characterCount() y `avgLength` en
   calculateAvg() son variables temporales que se asignan una vez y
   se retornan de inmediato — innecesarias.

3. Duplicate Code: la expresión .stream().mapToLong(w -> w.length()).sum()
   se repite íntegramente en ambos métodos.

-------------------------------------------------------------------------
Pregunta 2: Aplicar los refactorings
-------------------------------------------------------------------------

Paso 1: Encapsulate Field (Public Field)
Se hace `words` private y se generan getter (con copia defensiva) y setter.
*/

public class Document {
    private List<String> words;

    public List<String> getWords() {
        return new ArrayList<>(this.words);
    }

    public void setWords(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public long characterCount() {
        long count = this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum();
        return count;
    }

    public long calculateAvg() {
        long avgLength = this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum() / this.words.size();
        return avgLength;
    }
}

/*
Paso 2: Inline Temp (Temporary Variable)
Se eliminan las variables temporales `count` y `avgLength` retornando
la expresión directamente.
*/

public class Document {
    private List<String> words;

    public List<String> getWords() {
        return new ArrayList<>(this.words);
    }

    public void setWords(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public long characterCount() {
        return this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum();
    }

    public long calculateAvg() {
        return this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum() / this.words.size();
    }
}

/*
Paso 3: Extract Method (Duplicate Code)
Se elimina la expresión duplicada reutilizando characterCount()
dentro de calculateAvg().
*/

public class Document {
    private List<String> words;

    public List<String> getWords() {
        return new ArrayList<>(this.words);
    }

    public void setWords(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public long characterCount() {
        return this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum();
    }

    public long calculateAvg() {
        return this.characterCount() / this.words.size();
    }
}

/*
-------------------------------------------------------------------------
Pregunta 3: ¿Hay un error en el código original?
-------------------------------------------------------------------------

a) Sí. calculateAvg() divide long / int, lo que provoca división entera:
   Java trunca los decimales.

b) Ocurre siempre que el promedio no sea un número entero exacto.

c) Sí, el error sigue presente luego de los refactorings: solo se
   reorganizó el código, no se corrigió la lógica.

d) Solo se corregiría cambiando el tipo de retorno a double y
   haciendo un cast explícito: (double) this.characterCount() / this.words.size()

e) No es un refactoring. Cambiar el tipo de retorno de long a double
   modifica el comportamiento observable del método.
   Es el sombrero amarillo de Kent Beck: detectamos el bug durante
   el refactoring, pero corregirlo es una tarea separada.
*/