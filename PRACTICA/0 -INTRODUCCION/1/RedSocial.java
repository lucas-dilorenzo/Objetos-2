/*
 * Ejercicio 1 — Red Social (tipo Twitter)
 *
 * JERARQUÍA:
 *   Publicacion (abstract)
 *   ├── Tweet    → texto propio (1..280), lista de sus Retweets
 *   └── Retweet  → referencia al Tweet original
 *
 * DECISIÓN: Retweet NO extiende Tweet.
 * Un Retweet no ES un Tweet — es una Publicación que referencia a uno.
 * Extender Tweet violaría la relación "es un" y heredaría métodos
 * que no corresponden (ej: agregarRetweet).
 * Con Publicacion abstracta, Usuario puede tener List<Publicacion>
 * y operar polimórficamente sin saber si es Tweet o Retweet.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// ---------------------------------------------------------------------------
// Publicacion — superclase abstracta común a Tweet y Retweet
// ---------------------------------------------------------------------------

/*
 * Define el contrato mínimo que toda publicación debe cumplir:
 * - getTexto(): devolver el texto visible
 * - eliminarLasReferenciasDeRetweets(): limpieza al borrar en cascada
 *
 * Tweet lo implementa limpiando su lista de retweets.
 * Retweet lo implementa con cuerpo vacío (no tiene retweets propios).
 * Esto permite iterar List<Publicacion> sin instanceof.
 */
abstract class Publicacion {
    public abstract String getTexto();
    public abstract void eliminarLasReferenciasDeRetweets();
}

// ---------------------------------------------------------------------------
// Tweet
// ---------------------------------------------------------------------------

/*
 * INVARIANTE DE CLASE: todo Tweet que exista tiene texto válido (1..280 chars).
 * Si el texto es inválido el objeto no se crea — la excepción en el constructor
 * garantiza esto. Es preferible a devolver un String de error porque el objeto
 * inválido nunca llega a existir.
 *
 * Tweet mantiene una lista de sus propios Retweets para poder limpiarlos
 * cuando sea eliminado (borrado en cascada).
 */
class Tweet extends Publicacion {
    private static final int MIN_LARGO = 1;
    private static final int MAX_LARGO = 280;

    private String texto;
    private List<Retweet> retweetsDeMisTweets;

    public Tweet(String texto) {
        if (texto.length() < MIN_LARGO || texto.length() > MAX_LARGO)
            throw new IllegalArgumentException("El texto debe tener entre 1 y 280 caracteres");
        this.texto = texto;
        this.retweetsDeMisTweets = new ArrayList<>();
    }

    /*
     * Llamado por el constructor de Retweet — el Tweet registra
     * al nuevo Retweet para poder limpiarlo si el Tweet es eliminado.
     */
    public void agregarRetweet(Retweet retweet) {
        this.retweetsDeMisTweets.add(retweet);
    }

    @Override
    public String getTexto() {
        return this.texto;
    }

    /*
     * Al eliminar un Tweet, sus Retweets quedarían huérfanos.
     * Se les avisa llamando eliminar() — cada uno nulifica su referencia.
     * Luego se vacía la lista propia.
     */
    @Override
    public void eliminarLasReferenciasDeRetweets() {
        for (Retweet retweet : retweetsDeMisTweets)
            retweet.eliminar();
        this.retweetsDeMisTweets.clear();
    }

    public int cantidadDeRetweets() {
        return this.retweetsDeMisTweets.size();
    }
}

// ---------------------------------------------------------------------------
// Retweet
// ---------------------------------------------------------------------------

/*
 * Retweet guarda una referencia al Tweet original.
 * Al construirse, se registra en el Tweet (tweet.agregarRetweet(this)).
 *
 * RETWEET HUÉRFANO: si el Tweet original es eliminado, se llama eliminar()
 * que pone this.tweet = null. Cualquier intento posterior de leer el texto
 * lanza IllegalArgumentException — falla explícitamente en lugar de
 * devolver datos incorrectos o null silencioso.
 *
 * eliminarLasReferenciasDeRetweets() tiene cuerpo vacío porque un Retweet
 * no tiene retweets propios — el método existe solo por el contrato polimórfico.
 */
class Retweet extends Publicacion {
    private Tweet tweet;

    public Retweet(Tweet tweet) {
        this.tweet = tweet;
        tweet.agregarRetweet(this);
    }

    public void eliminar() {
        this.tweet = null;
    }

    @Override
    public String getTexto() {
        if (this.tweet == null)
            throw new IllegalArgumentException("El Tweet original ya no existe");
        return this.tweet.getTexto();
    }

    @Override
    public void eliminarLasReferenciasDeRetweets() {
        // un Retweet no tiene retweets propios — nada que limpiar
    }

    public boolean tieneTweetOriginal() {
        return this.tweet != null;
    }
}

// ---------------------------------------------------------------------------
// Usuario
// ---------------------------------------------------------------------------

/*
 * Usuario mantiene su lista de publicaciones (tweets y retweets mezclados).
 * Es responsable de limpiar las suyas cuando es eliminado.
 *
 * eliminarPublicaciones() opera polimórficamente:
 * - Para cada Tweet: limpia sus retweets huérfanos
 * - Para cada Retweet: no hace nada (cuerpo vacío en Retweet)
 * Luego vacía la lista completa.
 */
class Usuario {
    private String userName;
    private List<Publicacion> publicaciones;

    public Usuario(String userName) {
        this.userName = userName;
        this.publicaciones = new ArrayList<>();
    }

    public void agregarPublicacion(Publicacion publicacion) {
        this.publicaciones.add(publicacion);
    }

    public String getUserName() {
        return this.userName;
    }

    public void eliminarPublicaciones() {
        for (Publicacion p : publicaciones)
            p.eliminarLasReferenciasDeRetweets();
        this.publicaciones.clear();
    }

    public int cantidadPublicaciones() {
        return this.publicaciones.size();
    }
}

// ---------------------------------------------------------------------------
// RedSocial
// ---------------------------------------------------------------------------

/*
 * DECISIÓN: Map<String, Usuario> en lugar de List<Usuario>.
 * El Map usa el userName como clave única — garantiza no duplicados
 * y permite buscar en O(1) sin recorrer toda la lista.
 *
 * RedSocial coordina el borrado en cascada en dos pasos:
 *   1. usuario.eliminarPublicaciones() — limpia retweets huérfanos y vacía publicaciones
 *   2. usuarios.remove(userName) — elimina al usuario de la red
 *
 * El orden importa: si se elimina al usuario primero, se pierde la referencia
 * a sus publicaciones y no se pueden limpiar los retweets huérfanos.
 */
class RedSocial {
    private Map<String, Usuario> usuarios;

    public RedSocial() {
        this.usuarios = new HashMap<>();
    }

    public void agregarUsuario(String userName) {
        if (this.existeUsuario(userName))
            throw new IllegalArgumentException("El usuario " + userName + " ya existe");
        this.usuarios.put(userName, new Usuario(userName));
    }

    public void eliminarUsuario(String userName) {
        Usuario usuario = this.buscarUsuario(userName);
        usuario.eliminarPublicaciones(); // paso 1: limpieza en cascada
        this.usuarios.remove(userName);  // paso 2: eliminar de la red
    }

    public boolean existeUsuario(String userName) {
        return this.usuarios.containsKey(userName);
    }

    /*
     * Devuelve el usuario para que el llamador pueda agregarle publicaciones.
     * Lanza excepción si no existe — no devuelve null.
     */
    public Usuario getUsuario(String userName) {
        return this.buscarUsuario(userName);
    }

    private Usuario buscarUsuario(String userName) {
        if (!this.existeUsuario(userName))
            throw new IllegalArgumentException("El usuario " + userName + " no existe");
        return this.usuarios.get(userName);
    }
}
