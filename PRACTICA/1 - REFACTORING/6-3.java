/*
Ejercicio 6.3 - Publicaciones

-------------------------------------------------------------------------
Iteración 1
-------------------------------------------------------------------------

Code smell identificado: Long Method
El método ultimosPosts() hace tres cosas distintas:
1. Filtrar posts que no pertenecen al usuario
2. Ordenar los posts por fecha
3. Tomar los primeros N posts

Refactoring aplicado: Extract Method (3 veces)
Se extrae cada responsabilidad en su propio método.
*/

// Paso 1: extraer el filtro
private List<Post> postDeUsuariosAexcepcionDe(Usuario user) {
    List<Post> postsOtrosUsuarios = new ArrayList<Post>();
    for (Post post : this.posts) {
        if (!post.getUsuario().equals(user)) {
            postsOtrosUsuarios.add(post);
        }
    }
    return postsOtrosUsuarios;
}

// Paso 2: extraer el ordenamiento
// La lista se pasa por referencia, no hace falta retornarla
private void ordenarPostPorFecha(List<Post> postsOtrosUsuarios) {
    for (int i = 0; i < postsOtrosUsuarios.size(); i++) {
        int masNuevo = i;
        for (int j = i + 1; j < postsOtrosUsuarios.size(); j++) {
            if (postsOtrosUsuarios.get(j).getFecha().isAfter(
                postsOtrosUsuarios.get(masNuevo).getFecha())) {
                masNuevo = j;
            }
        }
        Post unPost = postsOtrosUsuarios.set(i, postsOtrosUsuarios.get(masNuevo));
        postsOtrosUsuarios.set(masNuevo, unPost);
    }
}

// Paso 3: extraer la toma de los primeros N
private List<Post> obtenerUltimos(int cantidad, List<Post> postsOtrosUsuarios) {
    List<Post> ultimosPosts = new ArrayList<Post>();
    int index = 0;
    Iterator<Post> postIterator = postsOtrosUsuarios.iterator();
    while (postIterator.hasNext() && index < cantidad) {
        ultimosPosts.add(postIterator.next());
        index++; // importante: sin esto el while es infinito
    }
    return ultimosPosts;
}

// Método principal: limpio y legible, cada paso en una línea
public List<Post> ultimosPosts(Usuario user, int cantidad) {
    List<Post> postsOtrosUsuarios = postDeUsuariosAexcepcionDe(user);
    ordenarPostPorFecha(postsOtrosUsuarios);
    return obtenerUltimos(cantidad, postsOtrosUsuarios);
}

/*
-------------------------------------------------------------------------
Iteración 2
-------------------------------------------------------------------------

Code smell identificado: Feature Envy
postDeUsuariosAexcepcionDe() accede a post.getUsuario() para decidir
si un post pertenece a un usuario. Esa lógica le corresponde a Post,
no a PostApp.

Refactoring aplicado: Move Method
Se mueve la lógica de comparación a Post como esDeOtroUsuario().
*/

// Post
public boolean esDeOtroUsuario(Usuario user) {
    return !this.usuario.equals(user);
}

// PostApp — el filtro ahora delega en Post
private List<Post> postDeUsuariosAexcepcionDe(Usuario user) {
    List<Post> postsOtrosUsuarios = new ArrayList<Post>();
    for (Post post : this.posts) {
        if (post.esDeOtroUsuario(user)) {
            postsOtrosUsuarios.add(post);
        }
    }
    return postsOtrosUsuarios;
}

/*
Resultado final: cada clase es responsable de su propio comportamiento.
Post sabe si pertenece a un usuario; PostApp coordina el flujo general.
*/