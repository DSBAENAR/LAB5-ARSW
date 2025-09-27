# Parte I

1. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.

```java
public InMemoryBlueprintPersistence() {
        Blueprint bp=new Blueprint("dsbaenar", "ECI Blueprint", new Point[]{new Point(140, 140),new Point(115, 115)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("dsbaenar", "Highway Blueprint", new Point[]{new Point(10, 10),new Point(15, 15)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("Maria", "House Blueprint", new Point[]{new Point(0, 0),new Point(10, 10)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
    }
```

2. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:
```java
@RestController
@RequestMapping("/api/blueprints")
/**
 * Controller for managing blueprints.
 */
public class BlueprintController {

    private final BlueprintsServices bps;

    /**
     * Constructor for BlueprintController.
     * @param bps the BlueprintsServices instance
     */
    public BlueprintController(BlueprintsServices bps) {
        this.bps = bps;
    }

    /**
     * Get all blueprints.
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity<?> getAllBlueprints() {
        try{
            return ResponseEntity.ok(bps.getAllBlueprints());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
```
3. Verifique el funcionamiento de a aplicación:

Para este caso usé Postman para verificar el funcionamiento

<img width="1419" height="809" alt="image" src="https://github.com/user-attachments/assets/2e2149b0-2901-41a7-bcb0-05f3c9727006" />


4. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en la documentación de Spring, sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).

```java
  @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintByAuthor(@PathVariable String author){
        try {
            return ResponseEntity.ok(bps.getBlueprintsByAuthor(author));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
            
        }
    }
```
<img width="1420" height="792" alt="image" src="https://github.com/user-attachments/assets/3c89ac8e-409e-4517-a38c-57a4ff934851" />
<img width="1423" height="798" alt="image" src="https://github.com/user-attachments/assets/99c61289-aace-478a-9caa-564b2938f4b4" />

5. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404.

```java
@GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByNameAndAuthor(@PathVariable String author, @PathVariable String bpname){
        try {
            return ResponseEntity.ok(bps.getBlueprintByNameAndAuthor(author, bpname));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity. status(404).body(e.getMessage());
            
        }
    }
```
<img width="1021" height="693" alt="image" src="https://github.com/user-attachments/assets/74a2bec2-a48c-443c-acbc-89f2c4efc401" />
<img width="999" height="648" alt="image" src="https://github.com/user-attachments/assets/8e1b48b7-c7d7-4579-8567-81ff6d9b754e" />

6.Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento JSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

```java
public ResponseEntity<?> addBlueprint(@RequestBody Blueprint bp){
        try {
            bps.addNewBlueprint(bp);
            return ResponseEntity.status(201).body("The Blueprint made by " + bp.getAuthor() + " with name " + bp.getName() + " has been created.");
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
```
7. Para probar que el recurso ‘planos’ acepta e interpreta correctamente las peticiones POST, use el comando curl de Unix. Este comando tiene como parámetro el tipo de contenido manejado (en este caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo cual en este caso debe ser un documento jSON equivalente a la clase Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:
<img width="568" height="159" alt="image" src="https://github.com/user-attachments/assets/e2783bc4-a431-43b8-93c2-e71aae2431dd" />
8. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.
```java
@PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint) {
        try {
            bps.updateBlueprint(author, bpname, blueprint);
            return ResponseEntity.ok("Blueprint updated successfully.");
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
```
<img width="998" height="524" alt="image" src="https://github.com/user-attachments/assets/1c3b51aa-42ba-417d-ad36-8e9ddeeaf967" />
<img width="771" height="691" alt="image" src="https://github.com/user-attachments/assets/4f7abf54-cf9e-495c-8df8-ef4db5ecb34c" />

# PARTE II
El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

Qué condiciones de carrera se podrían presentar?
Cuales son las respectivas regiones críticas?
Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt

Respuesta en el archivo ANALISIS_CONCURRENCIA.txt
