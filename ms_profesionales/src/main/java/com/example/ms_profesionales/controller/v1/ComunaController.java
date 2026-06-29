package com.example.ms_profesionales.controller.v1;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.service.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("ComunaControllerV1")
@RequestMapping("/api/v1/comunas")
@Tag(name = "Comuna Controller", description = "Endpoints para gestionar comunas")
public class ComunaController {

    private final ComunaService comunaService;

    ComunaController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    // Obtener todas las comunas
    @GetMapping
    @Operation(summary = "Listar todas las comunas", description = "Obtiene una lista de todas las comunas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida exitosamente")
    public ResponseEntity<List<ComunaDTO>> obtenerTodas() {
        List<ComunaDTO> lista = comunaService.obtenerTodas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    //Busqueda por Id 
    @GetMapping("/{id}")
    @Operation(summary = "Buscar comuna por ID", description = "Obtiene una comuna específica utilizando su ID único.")
    @ApiResponse(responseCode = "200", description = "Comuna encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            ComunaDTO comunaDTO = comunaService.buscarPorId(id);
            return new ResponseEntity<>(comunaDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Busqueda por Nombre.
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar comuna por nombre", description = "Obtiene una o más comunas específicas utilizando su nombre.")
    @ApiResponse(responseCode = "200", description = "Comunas encontradas exitosamente")
    @ApiResponse(responseCode = "404", description = "Comunas no encontradas")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        try {
            // Como tu service devuelve una List<ComunaDTO>, aquí enviamos la lista completa
            List<ComunaDTO> listaComunas = comunaService.buscarPorNombre(nombre);
            return new ResponseEntity<>(listaComunas, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Si el service lanza la excepción cuando la lista está vacía
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Busqueda por palabras que contenga el nombre 
    @GetMapping("/nombrePar/{nombre}")
    @Operation(summary = "Buscar comuna por nombre parcial", description = "Obtiene una o más comunas específicas utilizando una parte de su nombre.")
    @ApiResponse(responseCode = "200", description = "Comunas encontradas exitosamente")
    @ApiResponse(responseCode = "404", description = "Comunas no encontradas")
    public ResponseEntity<?> buscarPorNombreParcial(@PathVariable String nombre) {
        try {
            List<ComunaDTO> listaResultados = comunaService.buscarPorNombreParcial(nombre);
            return new ResponseEntity<>(listaResultados, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Registrar nueva comuna 
    @PostMapping
    @Operation(summary = "Registrar nueva comuna", description = "Crea una nueva comuna utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponse(responseCode = "201", description = "Comuna registrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de la comuna inválidos")
    public ResponseEntity<?> registrarComuna(@RequestBody ComunaDTO comunaDTO) {
        try {
            ComunaDTO nuevaComuna = comunaService.registrarComuna(comunaDTO);
            return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Eliminar comuna 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar comuna", description = "Elimina una comuna específica utilizando su ID único.")
    @ApiResponse(responseCode = "200", description = "Comuna eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    public ResponseEntity<String> eliminarComuna(@PathVariable Integer id) {
        String mensaje = comunaService.eliminarComuna(id);
        if (mensaje.contains("éxito")) {
            return ResponseEntity.ok(mensaje);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
    }




}
