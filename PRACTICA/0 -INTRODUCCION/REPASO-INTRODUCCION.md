# INTRODUCCIÓN — Hoja de repaso

Dos ejercicios de repaso previos a la materia. Conceptos clave: polimorfismo, herencia, Double Dispatch, responsabilidades.

---

## Ejercicio 1 — Red Social (tipo Twitter)

### Jerarquía de clases

```
Publicacion (abstract)
├── Tweet    → texto propio (1..280), lista de Retweet
└── Retweet  → referencia al Tweet original
```

`Retweet` NO extiende `Tweet` — un Retweet no *es* un Tweet, es una Publicación que apunta a uno. Extender `Tweet` sería incorrecto conceptualmente y heredaría métodos que no corresponden.

### Clases principales

| Clase | Responsabilidad |
|---|---|
| `Twitter` / `RedSocial` | Registrar usuarios, coordinar borrado en cascada |
| `Usuario` | Mantener lista de `Publicacion`, eliminar las propias |
| `Publicacion` (abstract) | `getTexto()` + `eliminarLasReferenciasDeRetweets()` |
| `Tweet` | Valida texto 1..280 en constructor, mantiene lista de sus `Retweet` |
| `Retweet` | Referencia al `Tweet` original; `eliminar()` la pone en null |

### Decisiones de diseño

**¿Por qué `Map<String, Usuario>` y no `List<Usuario>`?**
El Map usa el screenName como clave única — garantiza no duplicados y permite buscar sin recorrer toda la lista. La referencia usa `List` con stream filter — ambas son válidas, `Map` es más eficiente.

**¿Para qué sirve `eliminarLasReferenciasDeRetweets()` en `Publicacion`?**
Permite iterar `List<Publicacion>` sin saber si cada elemento es `Tweet` o `Retweet`:
- `Tweet`: llama `retweet.eliminar()` en cada retweet propio → luego limpia la lista
- `Retweet`: cuerpo vacío — no tiene retweets propios

**¿Por qué `Tweet` lanza `IllegalArgumentException` en el constructor?**
Si el texto es inválido el objeto no debería existir — **invariante de clase**.

**¿Qué pasa con un Retweet cuando se borra el Tweet original?**
`Retweet.eliminar()` pone `this.tweet = null`. Si alguien llama `getTexto()` sobre ese retweet huérfano, lanza excepción — falla explícitamente en lugar de devolver datos incorrectos.

### Borrado en cascada — el orden importa

```
1. Por cada publicación del usuario: limpiar referencias de retweets huérfanos
2. Vaciar la lista de publicaciones del usuario
3. Remover al usuario de la colección
```

Si eliminás al usuario primero, perdés la referencia a sus publicaciones.

**Responsabilidades:**
- `Twitter` coordina (conoce a todos los usuarios)
- `Usuario` limpia sus propias publicaciones
- `Tweet` limpia sus propios retweets
- `Retweet` se desvincula del Tweet original

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

class Jugador {
    private Jugada jugada;
    public Jugador(Jugada jugada) { this.jugada = jugada; }
    public Jugada getJugada() { return jugada; }
    public Resultado juegaContra(Jugador otro) {
        return this.jugada.juegaContra(otro.jugada);
    }
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
