/*
Ejercicio 1.3 - Cálculos
Code smells identificados:

1. Long Method: el método imprimirValores() mezcla tres responsabilidades 
distintas: calcular el promedio de edades, calcular el total de salarios,
y formatear e imprimir el resultado.

2. Duplicate Code: el recorrido sobre la colección personal se repite para
 calcular cada valor.

 Refactorings aplicados:
Extract Method para separar las responsabilidades:
*/

private double obtenerPromedioEdades() {
    int totalEdades = 0;
    for (Empleado empleado : personal) {
        totalEdades = totalEdades + empleado.getEdad();
    }
    return totalEdades / personal.size();
}

private double obtenerTotalSalarios() {
    double totalSalarios = 0;
    for (Empleado empleado : personal) {
        totalSalarios = totalSalarios + empleado.getSalario();
    }
    return totalSalarios;
}

public void imprimirValores() {
    String message = String.format(
        "El promedio de las edades es %s y el total de salarios es %s",
        obtenerPromedioEdades(),
        obtenerTotalSalarios()
    );
    System.out.println(message);
}

/* 

Tradeoff: al separar en dos métodos se introduce un segundo recorrido sobre la colección,
 lo que podría considerarse Duplicate Code. Sin embargo, es un tradeoff 
 aceptable ya que se gana claridad y cohesión. Como indica Fowler, 
 primero se escribe código claro y bien factorizado; la optimización de 
 performance se aplica solo si es necesaria.

Nota: el código original presenta un bug potencial si personal está vacío 
(división por cero en el promedio). Corregirlo implicaría un cambio de 
comportamiento y por lo tanto no es un refactoring, sino una nueva funcionalidad.
*/