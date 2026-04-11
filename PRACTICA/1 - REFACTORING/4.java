/*
Ejercicio 4 - Alcance en Redes Sociales

El ejercicio pide aplicar una serie de Rename Method y Rename Parameter.
Para cada uno se listan los archivos y líneas afectadas.

-------------------------------------------------------------------------
1. Rename Method: procesar → impacto (Publicacion.java)
-------------------------------------------------------------------------
Se renombra la declaración del método y su única llamada, 
ambas dentro de Publicacion.java:
*/

// Publicacion.java - línea 11: declaración
private int impacto() {
    return likes * 3;
}

// Publicacion.java - línea 15: llamada
public int calcular() {
    return impacto() * 10;
}

/*
-------------------------------------------------------------------------
2. Rename Method: calcular → alcance (Publicacion.java línea 14)
-------------------------------------------------------------------------
Se renombra la declaración en Publicacion.java y su llamada en Perfil.java.
Este rename tiene impacto cruzado entre archivos.
*/

// Publicacion.java - línea 14: declaración
public int alcance() {
    return impacto() * 10;
}

// Perfil.java - línea 13: llamada
private int alcanceDePublicaciones() {
    return publicaciones.stream().mapToInt(p -> p.alcance()).sum();
}

/*
-------------------------------------------------------------------------
3. Rename Method: calcular → alcance (Perfil.java línea 15)
-------------------------------------------------------------------------
Se renombra la declaración en Perfil.java.
En el código mostrado no se observan llamadas a este método desde 
otros archivos, por lo tanto solo se modifica la línea 15.
En un sistema real habría que rastrear todos los lugares donde 
se invoca calcular() de Perfil y renombrarlos también.
*/

// Perfil.java - línea 15: declaración
public int alcance() {
    return alcanceDePublicaciones() * bonus();
}

/*
-------------------------------------------------------------------------
4. Rename Parameter: p → publicacion en agregarPublicacion (Perfil.java)
-------------------------------------------------------------------------
El Rename Parameter es el más local de todos: solo afecta dentro del método.
Se modifica el nombre del parámetro en la firma y su referencia 
dentro del cuerpo del método.
*/

// Perfil.java - línea 10
public void agregarPublicacion(Publicacion publicacion) {
    publicaciones.add(publicacion);
}