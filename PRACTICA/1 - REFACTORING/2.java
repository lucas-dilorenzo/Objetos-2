/* 
Ejercicio 2 - Iteradores circulares

Tarea: Rename Variable — `result` → `currentPosition` en `next()`

La variable `result` declarada en la línea 14 dentro de `next()` se renombra 
a `currentPosition` para mejorar la legibilidad y expresar claramente su propósito.

El rename afecta todas las referencias a `result` dentro de `next()` 
(líneas 17 y 18). El código resultante es:
*/

public char next() {
    int currentPosition;
    if (idx >= source.length)
        idx = 0;
    currentPosition = idx++;
    return source[currentPosition];
}

/* 
Inconveniente potencial

Existe otra variable llamada `result` en el constructor de la misma clase:
*/

public CharRing(String srcString) {
    char result;
    ...
}

/* 
Sin embargo, ambas variables son locales a sus respectivos métodos y viven 
en scopes diferentes, por lo que **no hay conflicto real**. 

El inconveniente es que una herramienta de refactoring automático podría, 
si no distingue correctamente el scope, intentar renombrar ambas variables. 
Esto refuerza la importancia de verificar manualmente el resultado luego de 
aplicar un refactoring automático, y de contar con tests que confirmen que 
el comportamiento no cambió.
*/