/*
Ejercicio 1.1 - Protocolo de Cliente
Code smell identificado: nombres abreviados e ilegibles (Rename Method)
Los métodos lmtCrdt(), mtFcE() y mtCbE() presentan el smell de nombres 
poco descriptivos. Los nombres están abreviados de tal manera que no 
comunican su propósito, obligando al lector a recurrir a los comentarios
para entender qué hace cada método. En código de calidad, el nombre debería
ser suficientemente descriptivo como para que el comentario sea innecesario.

Refactoring a aplicar: Rename Method
Se renombran los métodos para que expresen claramente su intención:

*/

public double limiteDeCredito() {...}

protected double montoFacturadoEntre(LocalDate fechaInicio, LocalDate fechaFin) {...}

private double montoCobradoEntre(LocalDate fechaInicio, LocalDate fechaFin) {...}

/*
Adicionalmente, se aplica Rename Parameter sobre los parámetros f1 y f2, 
reemplazándolos por fechaInicio y fechaFin, mejorando también la legibilidad 
de la firma del método.
Con estos cambios los comentarios se vuelven redundantes y pueden eliminarse,
 ya que el código se explica por sí mismo.
*/