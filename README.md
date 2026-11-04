# **Proyecto: Motor de Deliberación Simplificado**

**Materia:** Estructuras de Datos  
**Lenguaje de Desarrollo:** Java  
**Año:** 2026 \- Segundo cuatrimestre

# **1\. Introducción** 

El análisis de debates en plataformas digitales requiere modelos computacionales capaces de representar cómo las opiniones se ramifican: se apoyan o se refutan. En este proyecto, se desarrollará el backend de un Motor de Deliberación Simplificado, un sistema diseñado para almacenar, indexar y consultar opiniones.

A diferencia de un foro lineal, este sistema modela la discusión sobre un tema dado mediante un árbol general, donde cada opinión se clasifica por su postura respecto al tema general. El objetivo principal es aplicar los conceptos de Tipos de Datos Abstractos (TDA) para garantizar que la inserción y búsqueda de opiniones se resuelva en tiempo constante o lineal según corresponda, sin importar el volumen de los datos.

# **2\. Materiales y Tareas**

## **Componentes Proporcionados**

* **Usuario** (`POJO`): Clase simple para mantener información del usuario (`id` y `nombre`).  
* **Opinion** (`POJO`): Clase simple para representar una opinión registrando un identificador único, el texto de la opinión, su postura (`a favor`, `en contra`, o `root` cuando es el nodo raíz), y el `id` del usuario que lo postea. Solo se trabaja con opiniones "a favor" y "en contra".  
* **Operaciones** (`POJO`): Representa las operaciones realizadas mediante un entero (`1: CREAR_DEBATE`, `2: POSTEAR`).  
* **Esqueleto de MotorDeliberacion**: Clase a completar.

## **Implementación Requerida (`MotorDeliberacion`)**

* **Crear debate:** Crea el debate sobre un tema general e inicializa las estructuras.  
* **Postear (`idUsr`, `idNuevo`, `idPadre`, `postura`, `texto`):** Agrega un posteo al árbol como hijo de `idPadre` y actualiza las estructuras correspondientes en O(1).  
* **Polarización de un nodo (`id`):** Calcula la polarización de forma recursiva recorriendo el subárbol desde el nodo dado y retorna cantidad de nodos a favor y en contra.  
* **Eliminar un posteo (`id_de_posteo`):** Elimina el nodo y todo su subárbol de respuestas (eliminación en cascada) en O(m), siendo m la cantidad de nodos en el subárbol a eliminar.  
* **Consultar por usuario:** Permite consultar rápidamente todas las intervenciones de un usuario específico indicando su índice en O(1), retornando las opiniones en un iterable.  
* **Mostrar el árbol:** Imprime el árbol por consola.  
* **Contabilizar la polarización de todos los nodos del árbol:** Recorrido en postorden del árbol en O(n), retornando un iterable `<idnodo, afavor:x, encontra:y>`.  
* **Deshacer última operación:** Permite deshacer la última operación realizada si es de tipo `Crear debate` o `Postear`.  
* **Mostrar las operaciones realizadas:** Muestra el historial de operaciones realizadas por consola.

# **3\. Estructura de Carpetas**

| Paquete | Clases / Contenido |
| :---- | :---- |
| `ar.edu.cs.ed` | `Usuario`, `Opinion`, `Operaciones`, `MotorDeliberacion` (clase a completar) |
| `ar.edu.uns.cs.ed.tdas.*` | TDAs brindados por la cátedra |
| `test.ar.edu.uns.cs.ed` | Casos de prueba de la clase `MotorDeliberacion` |
| `test.java.ar.edu.uns.cs.ed.tdas.*` | Casos de prueba de las clases que implementan los TDAs |

# **4\. Tareas a Realizar**

## **Inicialización**

* **T1:** Acceder a la tarea de GitHub Classroom y aceptarla. *(Completado)*  
* **T2:** Clonar el repositorio automáticamente creado para comenzar a trabajar localmente.  
* **T3:** Familiarizarse con la estructura del proyecto.  
* **T4:** Analizar el problema, diseñar soluciones y seleccionar los TDAs que utilizarán.

## **Implementación**

* Trabajo y desarrollo dentro de la clase `MotorDeliberacion`.

# **5\. Criterios de Evaluación y Entrega**

1. **Encapsulamiento:** Correcta separación entre la interfaz de los TDAs y su implementación subyacente.  
2. **Eficiencia Algorítmica:** Selección de estructuras adecuadas para resolver los métodos en el tiempo de orden indicado.  
3. **Gestión de Memoria:** Correcta instanciación y enlace de nodos sin dejar referencias colgantes.

# **6\. GitHub Classroom**

## **Pasos para acceder y trabajar**

1. **Acceso al proyecto:** Mediante el enlace de invitación.  
2. **Inicio de sesión:** Iniciar sesión con la cuenta personal de GitHub.  
3. **Vinculación de cuenta:** Autoseleccionarse en el listado de alumnos (mapeo CuentaGitHub–Alumno).  
4. **Conformación de comisiones:** Aceptar la tarea y formar comisión (máximo 2 integrantes por grupo). Nombrar la comisión utilizando el/los apellido/s.  
5. **Creación del repositorio:** Generación automática de repositorio privado en la organización `ed-dcic-uns`.  
6. **Trabajo colaborativo:** Clonar el repositorio localmente y utilizar el Pull Request abierto para coordinar.  
7. **Metodología de trabajo:** Libre (uso de ramas y PRs o push directo a `main`).  
8. **Ejecución de tests automáticos:** Ejecución automática en cada cambio a `main`.  
9. **Criterio de aprobación:** Todos los tests unitarios automáticos deben ejecutarse correctamente en el entorno remoto antes de la fecha límite.

