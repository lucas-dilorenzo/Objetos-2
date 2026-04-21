# Objetos 2 — UNLP

Material de estudio para la materia **Orientación a Objetos 2** de la Facultad de Informática, UNLP.

**Lenguaje:** Java

---

## Contenido

### Unidades

| # | Tema | Estado |
|---|------|--------|
| 1 | Refactoring | Completo |
| 2 | Patrones de diseño | En curso |
| 3 | Frameworks | Pendiente |

### Estructura del repositorio

```
TEORIA/
├── 1 - REFACTORING/
│   ├── Intro a refactoring.pdf
│   ├── 2-Slides-Catalogo.pdf
│   ├── 3-Refactoring-Tools.pdf
│   └── Refactoring-Ejemplo.pdf
└── 2 - PATRONES/
    └── 1 - IntroPatrones-Adapter-Template.pdf

PRACTICA/
├── 0 - INTRODUCCION/        ← Ejercicios previos (Piedra Papel Tijera, Red Social)
│   └── REPASO-INTRODUCCION.md ← Hoja de repaso rápido
└── 1 - REFACTORING/         ← Cuadernillo semestral 2026
    ├── 1-1.java … 9.java    ← Soluciones ejercicios 1 al 9
    └── REPASO-REFACTORING.md ← Hoja de repaso rápido

EXPLICACION PRACTICA/
└── 0- EXPLICACION PRACTICA REPASO/

GUIAS PARA LA MATERIA/
├── Catálogo de code smells de Objetos 2.pdf
├── Diagrama de clases UML - Resumen.pdf
├── Trabajando con proyectos Maven v2.pdf
└── cheatsheet streams.pdf
```

---

## Temas vistos

### Refactoring
- Bad smells y catálogo de refactorings (Fowler)
- Extract Method, Move Method, Replace Conditional with Polymorphism, Pull Up Method/Field, Replace Temp with Query, Inline Temp, Extract Superclass, Encapsulate Field, Replace Loop with Pipeline, Replace Magic Number with Symbolic Constant, Form Template Method (con hook methods)
- Ejemplo completo: Club de Tenis
- Ejercicios 1 al 9 del cuadernillo 2026 — resueltos y comparados con resolución de referencia

### Patrones de diseño
- Introducción al concepto de patrones (Alexander → GoF)
- **Adapter:** adaptar interfaces incompatibles
- **Template Method** *(en progreso)*