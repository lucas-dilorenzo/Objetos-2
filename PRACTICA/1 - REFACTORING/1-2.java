/*
Ejercicio 1.2 - Participación en proyectos
Code smell identificado: Feature Envy
El método participaEnProyecto(Proyecto p) en Persona accede a 
los datos internos de Proyecto (su lista de participantes) para
responder una pregunta que conceptualmente le pertenece a Proyecto.
Esto es Feature Envy: el método envidia los atributos de otra clase.
Refactoring aplicado: Move Method
Se movió el método a Proyecto, ajustando la firma acorde:
*/

// Antes — en Persona
public boolean participaEnProyecto(Proyecto p) {
    return p.getParticipantes().contains(this);
}

// Después — en Proyecto
public boolean participa(Persona p) {
    return participantes.contains(p);
}

/*
El cambio es apropiado porque ahora la responsabilidad está 
correctamente asignada: es Proyecto quien conoce sus participantes
 y quien debe responder si una persona participa en él.
*/