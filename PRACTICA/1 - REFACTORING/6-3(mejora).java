/*
Ejercicio 6.3 - Publicaciones
Mejora adicional: Replace Loop with Pipeline

Todos los loops del ejercicio son candidatos a ser reemplazados
por una pipeline de streams, ya que cada uno filtra, ordena o limita
una colección.

Aplicando Replace Loop with Pipeline a cada método extraído:
*/

// Filtro — reemplaza el for con if
private List<Post> postDeUsuariosAexcepcionDe(Usuario user) {
    return this.posts.stream()
        .filter(post -> post.esDeOtroUsuario(user))
        .collect(Collectors.toList());
}

// Ordenamiento — reemplaza el doble for de bubble sort
private List<Post> ordenarPostPorFecha(List<Post> posts) {
    return posts.stream()
        .sorted(Comparator.comparing(Post::getFecha).reversed())
        .collect(Collectors.toList());
}

// Tomar N — reemplaza el while con iterator
private List<Post> obtenerUltimos(int cantidad, List<Post> posts) {
    return posts.stream()
        .limit(cantidad)
        .collect(Collectors.toList());
}

/*
Asi quedaria el método ultimosPosts() con los métodos intermedios:
*/

public List<Post> ultimosPosts(Usuario user, int cantidad) {
    List<Post> postsOtrosUsuarios = postDeUsuariosAexcepcionDe(user);
    List<Post> postsOrdenados = ordenarPostPorFecha(postsOtrosUsuarios);
    return obtenerUltimos(cantidad, postsOrdenados);
}

/*
Otra solucion:

Como las tres operaciones son encadenables sobre la misma colección,
se pueden combinar en una única pipeline, eliminando los métodos
intermedios y dejando ultimosPosts() en una sola expresión:
*/

public List<Post> ultimosPosts(Usuario user, int cantidad) {
    return this.posts.stream()
        .filter(post -> post.esDeOtroUsuario(user))
        .sorted(Comparator.comparing(Post::getFecha).reversed())
        .limit(cantidad)
        .collect(Collectors.toList());
}

/*
Ventajas:
- Código más declarativo: describís QUÉ querés, no CÓMO iterarlo
- Menos variables temporales
- Más legible y conciso

Nota: esta mejora es opcional si el enunciado no la pide explícitamente.
Extract Method aplicado en la iteración 1 ya es una solución válida.
*/