# INTRODUCCIÓN — Hoja de repaso

Dos ejercicios de repaso previos a la materia. Conceptos clave: polimorfismo, herencia, Double Dispatch, responsabilidades.

---

## Ejercicio 1 — Red Social (tipo Twitter)

### Clases principales

| Clase | Responsabilidad |
|---|---|
| `RedSocial` | Registrar usuarios, coordinar borrado en cascada |
| `Usuario` | Publicar tweets, seguir/dejar de seguir, eliminar propios tweets |
| `Tweet` | Texto 1..280 chars — valida en constructor |
| `ReTweet extends Tweet` | Es un Tweet — reutiliza validación vía `super(tweetOrigen.getTexto())` |

### Decisiones de diseño

**¿Por qué `Map<String, Usuario>` y no `List<Usuario>`?**
El Map usa el screenName como clave única — garantiza no duplicados y permite buscar sin recorrer toda la lista.

**¿Por qué `ReTweet extends Tweet`?**
ReTweet *es un* Tweet — cumple la relación "es un". Hereda la validación de 280 chars y permite que `Usuario` tenga una sola `List<Tweet>` con tweets y retweets mezclados (polimorfismo).

**¿Por qué `Tweet` lanza `IllegalArgumentException` en el constructor?**
Si el texto es inválido el objeto no debería existir. Con la excepción en el constructor garantizás que todo `Tweet` que exista sea siempre válido — **invariante de clase**.

### Borrado en cascada — el orden importa

```
1. RedSocial limpia retweets huérfanos en otros usuarios
2. Usuario elimina sus propios tweets
3. RedSocial elimina al usuario del mapa
```

Si eliminás al usuario primero, perdés la referencia a sus tweets y no podés limpiar los retweets huérfanos.

**¿Quién es responsable de cada paso?**
`RedSocial` coordina (es la única que conoce a todos los usuarios). `Usuario` limpia sus propios tweets (es el dueño). Cada clase tiene su responsabilidad.

---

## Ejercicio 2 — Piedra Papel Tijera (+ Lagarto y Spock)

### El problema central

`juegaContra(Jugada otra)` recibe una referencia genérica. Sin saber el tipo concreto de `otra` no se puede resolver el resultado sin `instanceof` — que es un **bad smell**.

### Solución: Double Dispatch

Cada subclase se *anuncia a sí misma* mediante el método `contraX()` correspondiente. El polimorfismo resuelve el tipo concreto sin ningún `if/switch`.

```
piedra.juegaContra(papel)
  → papel.contraPiedra(this)   // papel sabe que juega contra Piedra
  → Resultado.PIERDE           // desde el punto de vista de Papel
  → .invertir()                // se invierte: Piedra PIERDE
```

### `Resultado.invertir()` — por qué es necesario

`contraX()` devuelve el resultado **desde el punto de vista de `otra`**, no de `this`. Sin `invertir()` los resultados son al revés.

```java
enum Resultado {
    GANA, PIERDE, EMPATE;

    Resultado invertir() {
        if (this == GANA)   return PIERDE;
        if (this == PIERDE) return GANA;
        return EMPATE;
    }
}
```

**¿Por qué enum y no String?**
El compilador detecta errores de tipeo. No puede existir un resultado inválido como `"Gana"` vs `"gana"`.

### Estructura

```java
abstract class Jugada {
    abstract Resultado juegaContra(Jugada otra);
    abstract Resultado contraPiedra(Piedra p);
    abstract Resultado contraPapel(Papel p);
    abstract Resultado contraTijera(Tijera t);
    abstract Resultado contraLagarto(Lagarto l);
    abstract Resultado contraSpock(Spock s);
}
```

Cada subclase implementa `juegaContra()` anunciándose e invirtiendo, y cada `contraX()` devuelve el resultado desde el punto de vista de la otra jugada.

### Tradeoff — Open/Closed Principle

Agregar Lagarto y Spock requirió modificar `Jugada` (nuevo método abstracto) **y todas las subclases existentes** — viola OCP.

Es un **tradeoff aceptable** porque la alternativa con `instanceof` o `if/switch` es peor: mezcla responsabilidades, no escala y es más difícil de mantener.

---

## Conceptos transversales

| Concepto | Dónde aparece |
|---|---|
| **Polimorfismo** | `List<Tweet>` con tweets y retweets; `juegaContra()` en todas las subclases |
| **Invariante de clase** | Constructor de `Tweet` lanza excepción si texto inválido |
| **Responsabilidad única** | `RedSocial` coordina, `Usuario` limpia los suyos |
| **Double Dispatch** | Piedra Papel Tijera — resolver resultado sin `instanceof` |
| **Enum** | `Resultado` — tipo seguro, con lógica (`invertir()`) |
