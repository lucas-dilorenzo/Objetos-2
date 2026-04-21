# REFACTORING — Hoja de repaso

### Qué es
Transformación **interna** del código que **preserva el comportamiento observable**.
- Sombrero marrón (refactoring) ≠ sombrero amarillo (agregar función) — **nunca los dos a la vez**
- Siempre con **tests en verde** antes, durante y después

---

### BAD SMELLS — los que más salen

| Smell | Señal de alarma |
|---|---|
| **Duplicate Code** | Mismo código en 2+ lugares |
| **Long Method** | Método > ~20 líneas |
| **Feature Envy** | Método que usa más datos de *otra* clase que de la propia |
| **Switch Statements** | `if/else` o `switch` que discrimina por tipo |
| **Data Class** | Solo getters/setters, sin lógica |
| **Magic Number** | Literal numérico sin nombre (`0.79`, `5`) |
| **Public Field** | Campo público o package-private |
| **Temporary Variable** | Variable asignada una sola vez e inmediatamente retornada |
| **Message Chains** | `a.getB().getC().getD()` |

---

### CATÁLOGO — mecánica clave

#### Extract Method
*Precondición:* unidad sintáctica completa; máx. 1 variable temporal modificada (= valor de retorno)
```
crear método → copiar código → revisar variables locales → compilar → reemplazar → testear
```

#### Move Method *(Feature Envy)*
*Precondición:* (1) destino no tiene el método; (2) no hay otra def en jerarquía origen; (3) no modifica v.i. de origen; (4) puede acceder a todo lo que necesita desde destino
```
declarar en destino → copiar y ajustar → compilar destino → reemplazar por delegación → testear
```

#### Replace Conditional with Polymorphism *(Switch Statements)*
```
crear jerarquía → por cada subclase: override + copiar branch + compilar → borrar branch → abstracto en superclase
```

#### Pull Up Field
*Precondición:* mismo nombre, mismo tipo, no existe en superclase, se usa igual
```
crear en superclase (protected si era private) → borrar de subclases → compilar y testear
```

#### Pull Up Method
*Precondición:* **cuerpo idéntico**, signatura idéntica, elementos accesibles desde superclase
```
crear en superclase → borrar de subclases de a una → compilar y testear
```

#### Extract Superclass
Cuando dos clases tienen implementaciones similares. Combina Pull Up Field + Pull Up Method.

#### Form Template Method *(Duplicate Code con estructura común pero pasos variables)*
Pull Up Method no aplica directamente porque los métodos no son idénticos.
```
1. Extract Method → separar partes variables de comunes en cada subclase
2. Pull Up Method → subir partes idénticas
3. Subir el método principal → declararlo final
4. Pasos obligatorios variables → abstract
5. Pasos opcionales → hook method (cuerpo vacío en superclase, @Override donde aplica)
```

#### Encapsulate Field *(Public Field)*
```
hacer private → crear getter/setter → actualizar todos los accesos
```

#### Replace Temp with Query *(Variable temporal reutilizable/compleja)*
```
extraer expresión en método → reemplazar TODAS las referencias → eliminar declaración
```

#### Inline Temp *(Variable temporal trivial)*
```
reemplazar cada referencia por la expresión asignada → eliminar declaración
```
> **Inline Temp** elimina inline. **Replace Temp with Query** extrae un método nuevo — útil cuando la expresión es compleja o se reutiliza.

#### Replace Magic Number with Symbolic Constant
```
declarar private static final con nombre descriptivo → reemplazar todas las ocurrencias
```

#### Replace Loop with Pipeline
```
for → stream().filter().map().reduce() / sum() / collect()
```

---

### Smells → Refactorings (tabla rápida)

| Smell | Refactoring |
|---|---|
| Duplicate Code | Extract Method → Pull Up Method → **Form Template Method** |
| Long Method | Extract Method, Replace Temp with Query |
| Feature Envy | Move Method |
| Switch Statements | Replace Conditional with Polymorphism |
| Magic Number | Replace Magic Number with Symbolic Constant |
| Public/Package Field | Encapsulate Field |
| Variable temporal | Inline Temp / Replace Temp with Query |
| Message Chains | Hide Delegate |

---

### Precondiciones críticas (las preguntan mucho)

- **Pull Up Method** falla si los cuerpos no son idénticos → necesitás Form Template Method primero
- **Move Method** falla si el método modifica variables de instancia de la clase origen
- **Pull Up Field** falla si el campo no se usa igual en todas las subclases
- **Extract Method** falla si el bloque modifica más de 1 variable usada después

---

### Interfaz vs Clase abstracta en polimorfismo
- Usá **`interface`** cuando no hay estado ni comportamiento concreto compartido (ej: `Pago`)
- Usá **clase abstracta** cuando hay campos o métodos concretos que se comparten (ej: `Etiqueta` con `getNombreProducto()`)

---

### Qué NO es refactoring
- Corregir un bug → cambia comportamiento observable
- Agregar funcionalidad
- Optimizar performance
