package com.example.ms_agendamiento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_agendamiento.DTO.PacienteExternoDTO;
import com.example.ms_agendamiento.DTO.PsicologoExternoDTO;

import reactor.core.publisher.Mono;

@Service
public class agendamientoValidaciones {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public PacienteExternoDTO obtenerPaciente(Integer id){
        PacienteExternoDTO pacienteRecuperado = new PacienteExternoDTO();
        try {
            PacienteExternoDTO resultado = webClientBuilder.build()
                .get()
                .uri("http://pacientes/api/v1/pacientes/buscar-paciente/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PacienteExternoDTO.class)
                .block();

            if (resultado != null) {
                return resultado;
            }
            pacienteRecuperado.setId(0);
            pacienteRecuperado.setRut(null);;
            return pacienteRecuperado;

        } catch (Exception e) {
            pacienteRecuperado.setId(0);
            pacienteRecuperado.setReservaHoraId(id);
            return pacienteRecuperado;
        }
    }

    public PsicologoExternoDTO obtenerPsicologo(Integer id){
        PsicologoExternoDTO psicologoRecuperado = new PsicologoExternoDTO();
        try {
            PsicologoExternoDTO resultado = webClientBuilder.build()
                .get()
                .uri("http://psicologos/api/v1/psicologos/buscar-psicologo/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PsicologoExternoDTO.class)
                .block();

            if (resultado != null) {
                return resultado;
            }
            psicologoRecuperado.setId(0);
            psicologoRecuperado.setRut(null);;
            return psicologoRecuperado;

        } catch (Exception e) {
            psicologoRecuperado.setId(0);
            psicologoRecuperado.setReservaHoraIdP(id);
            return psicologoRecuperado;
        }
    }

}
