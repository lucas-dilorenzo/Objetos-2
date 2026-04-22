// =============================================================================
// EJERCICIO 1 — Friday the 13th en Java
// Patrón de diseño: ADAPTER
// =============================================================================
//
// CONTEXTO INICIAL
// ----------------
// La clase Biblioteca exporta su lista de socios en formato JSON.
// Para eso delega en un objeto Exporter.
// La implementación concreta que viene dada es VoorheesExporter,
// que construye el JSON a mano con Strings.
//
// OBJETIVO (parte 1b)
// -------------------
// Usar la librería JSON.simple para exportar los socios,
// SIN modificar ni una línea de la clase Biblioteca.
//
// PROBLEMA
// --------
// JSON.simple tiene su propia API (JSONObject, JSONArray, toJSONString()).
// Biblioteca espera un objeto que cumpla la interfaz Exporter,
// con el método: exportar(List<Socio>).
// Las interfaces son incompatibles → necesitamos un Adapter.
//
// IDENTIFICACIÓN DE ROLES
// -----------------------
//   Target  → interfaz Exporter        (lo que Biblioteca espera)
//   Client  → clase Biblioteca         (quien usa el Target)
//   Adaptee → librería json-simple     (no se puede modificar; JSONObject y JSONArray
//                                       son sus clases concretas que usamos adentro)
//   Adapter → clase JSONSimpleAdapter  (la creamos nosotros)
//
// Tip para identificar el Adaptee:
//   Preguntate "¿qué tengo pero no puedo usar directamente porque tiene interfaz
//   incompatible?" → eso es el Adaptee. No importa si es una clase o una librería.
//
// =============================================================================


import java.util.ArrayList;
import java.util.List;


// =============================================================================
// CÓDIGO BASE — tal cual viene dado en el ejercicio
// =============================================================================

// --- Socio: modelo de datos ---
class Socio {
    private String nombre;
    private String email;
    private String legajo;

    public Socio(String nombre, String email, String legajo) {
        this.nombre = nombre;
        this.email  = email;
        this.legajo = legajo;
    }

    public String getNombre() { return nombre; }
    public String getEmail()  { return email;  }
    public String getLegajo() { return legajo; }
}


// --- Target: interfaz que Biblioteca conoce ---
interface Exporter {
    String exportar(List<Socio> socios);
}


// --- Implementación original: construye JSON a mano con Strings ---
class VoorheesExporter implements Exporter {

    private String exportar(Socio socio) {
        String sep = System.lineSeparator();
        return "\t{" + sep
            + "\t\t\"nombre\": \"" + socio.getNombre() + "\"," + sep
            + "\t\t\"email\": \""  + socio.getEmail()  + "\"," + sep
            + "\t\t\"legajo\": \"" + socio.getLegajo() + "\""  + sep
            + "\t}";
    }

    @Override
    public String exportar(List<Socio> socios) {
        if (socios.isEmpty()) return "[]";
        String sep = System.lineSeparator();
        StringBuilder buffer = new StringBuilder("[" + sep);
        socios.forEach(s -> buffer.append(this.exportar(s)).append(",").append(sep));
        buffer.setLength(buffer.length() - (sep.length() + 1));
        buffer.append(sep).append("]");
        return buffer.toString();
    }
}


// --- Client: no conoce VoorheesExporter, solo habla con Exporter ---
class Biblioteca {
    private List<Socio> socios   = new ArrayList<>();
    private Exporter    exporter = new VoorheesExporter();  // default

    public void agregarSocio(Socio socio)      { socios.add(socio); }
    public String exportarSocios()             { return exporter.exportar(socios); }
    public void setExporter(Exporter exporter) { this.exporter = exporter; }
}


// =============================================================================
// SOLUCIÓN — patrón Adapter
// =============================================================================
//
// Creamos JSONSimpleAdapter.
// Implementa Exporter   → Biblioteca lo acepta como si fuera VoorheesExporter.
// Usa JSONArray/Object  → delega el trabajo real a json-simple (el Adaptee).
//
// En el proyecto Maven real se agrega al pom.xml:
//   <dependency>
//     <groupId>com.googlecode.json-simple</groupId>
//     <artifactId>json-simple</artifactId>
//     <version>1.1.1</version>
//   </dependency>
//
// IMPORTANTE sobre JSONObject y JSONArray:
// ----------------------------------------
// En el proyecto real estas clases vienen de la dependencia json-simple:
//   import org.json.simple.JSONArray;
//   import org.json.simple.JSONObject;
// No las escribís vos — las importás.
//
// En el examen, el enunciado te describe los métodos disponibles:
//   JSONObject → put(Object key, Object value)
//   JSONArray  → add(Object)
//   Ambas      → toJSONString()
// Con esa info armás el código del Adapter. No hace falta saberse la API de memoria.
//
// En este archivo las simulamos solo para que compile standalone sin Maven.
// =============================================================================

// Simulación de las clases de json-simple (NO las escribís vos en el proyecto real)
class JSONObject {
    private StringBuilder sb = new StringBuilder("{");
    private boolean first = true;

    public void put(String key, String value) {
        if (!first) sb.append(",");
        sb.append("\"").append(key).append("\":\"").append(value).append("\"");
        first = false;
    }

    public String toJSONString() { return sb.toString() + "}"; }
}

class JSONArray {
    private List<JSONObject> items = new ArrayList<>();

    public void add(JSONObject obj) { items.add(obj); }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toJSONString());
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }
}


// --- El Adapter: conecta Exporter (Target) con json-simple (Adaptee) ---
class JSONSimpleAdapter implements Exporter {           // cumple el Target

    @Override
    public String exportar(List<Socio> socios) {
        JSONArray array = new JSONArray();              // usa el Adaptee

        for (Socio socio : socios) {
            JSONObject obj = new JSONObject();
            obj.put("nombre", socio.getNombre());
            obj.put("email",  socio.getEmail());
            obj.put("legajo", socio.getLegajo());
            array.add(obj);
        }

        return array.toJSONString();                   // traduce la llamada
    }
}


// =============================================================================
// TESTS — verificación del comportamiento
// =============================================================================
//
// Test 1: VoorheesExporter sigue funcionando (no rompimos nada)
// Test 2: JSONSimpleAdapter produce JSON con los datos correctos
// Test 3: Biblioteca vacía devuelve "[]"
// Test 4: Biblioteca NO cambia → setExporter() alcanza para switchear
//
// En el proyecto real se usa JUnit 5. Acá los escribimos como asserts
// para poder correr el archivo standalone con: javac 1.java && java Ejercicio1
// =============================================================================

public class Ejercicio1 {

    public static void main(String[] args) {

        // Setup común
        Socio arya  = new Socio("Arya Stark",      "needle@stark.com",        "5234-5");
        Socio tyron = new Socio("Tyron Lannister", "tyron@thelannisters.com", "2345-2");

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.agregarSocio(arya);
        biblioteca.agregarSocio(tyron);

        // --- Test 1: exportador original ---
        String resultadoVoorhees = biblioteca.exportarSocios();
        assert resultadoVoorhees.contains("Arya Stark")       : "Falta nombre";
        assert resultadoVoorhees.contains("needle@stark.com") : "Falta email";
        System.out.println("Test 1 OK — VoorheesExporter:");
        System.out.println(resultadoVoorhees);
        System.out.println();

        // --- Test 2: adapter con json-simple, Biblioteca NO se modifica ---
        biblioteca.setExporter(new JSONSimpleAdapter());       // solo esta línea cambia
        String resultadoAdapter = biblioteca.exportarSocios();
        assert resultadoAdapter.contains("Arya Stark")       : "Falta nombre";
        assert resultadoAdapter.contains("needle@stark.com") : "Falta email";
        System.out.println("Test 2 OK — JSONSimpleAdapter:");
        System.out.println(resultadoAdapter);
        System.out.println();

        // --- Test 3: biblioteca vacía ---
        Biblioteca vacia = new Biblioteca();
        vacia.setExporter(new JSONSimpleAdapter());
        assert vacia.exportarSocios().equals("[]") : "Debería ser []";
        System.out.println("Test 3 OK — lista vacía devuelve: " + vacia.exportarSocios());
    }
}


// =============================================================================
// RESULTADO FINAL — diagrama de clases (para repasar en papel)
// =============================================================================
//
//   Biblioteca ──────────────> <<interface>> Exporter
//     - socios: List<Socio>        + exportar(List<Socio>): String
//     - exporter: Exporter                  △              △
//     + exportarSocios(): String            │              │
//     + setExporter(Exporter)               │              │
//                                  VoorheesExporter   JSONSimpleAdapter
//                                                       - (usa internamente)
//                                                      JSONArray / JSONObject
//                                                        (Adaptee — json-simple)
//
// Punto clave: Biblioteca usa setExporter() para cambiar de implementación
// sin saber nada del Adaptee. Eso es el patrón Adapter en acción.
// =============================================================================
