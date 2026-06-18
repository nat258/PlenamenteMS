package com.example.ms_agendamiento.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms_agendamiento.DTO.BoletaDTO;
import com.example.ms_agendamiento.model.Boleta;
import com.example.ms_agendamiento.model.ReservaHora;
import com.example.ms_agendamiento.repository.BoletaRepository;
import com.example.ms_agendamiento.repository.ReservaHoraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private ReservaHoraRepository reservaHoraRepository;

    public List<BoletaDTO>obtenerBoletas(){
        return boletaRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public BoletaDTO guardarBoleta(BoletaDTO boletaDTO){
        ReservaHora reserva = reservaHoraRepository.findById(boletaDTO.getReservaHoraId())
            .orElseThrow(() -> new RuntimeException("Error! La reserva con ID: " + boletaDTO.getReservaHoraId() + " no fue encontrada."));

        if (reserva.getBoleta() != null) {
            throw new RuntimeException("Error! La reserva " + reserva.getId() + " ya tiene una boleta asociada.");
        }

        Boleta boleta = new Boleta();
        boleta.setTipoPago(boletaDTO.getTipoPago());
        boleta.setMonto(boletaDTO.getMonto());
        boleta.setReservaHora(reserva);

        reserva.setEstado("PAGADO");
        reserva.setBoleta(boleta);
        
        Boleta boletaGuardada = boletaRepository.save(boleta);
        return convertirADTO(boletaGuardada);
    }

    public BoletaDTO buscarPorId(Integer id){
        Boleta boleta = boletaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Error! La boleta con ID " + id + " no fue encontrada."));
        return convertirADTO(boleta);
    }

    public String eliminar(Integer id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error! La boleta con ID " + id + " no fue encontrada."));

        ReservaHora reserva = boleta.getReservaHora();
        
        if (reserva != null) {
            reserva.setBoleta(null);
            reserva.setEstado("PENDIENTE");
            reservaHoraRepository.save(reserva);
        }
        
        boletaRepository.delete(boleta);
        return "La boleta " + id + " ha sido eliminada exitosamente.";
    }

    //Busqueda por monto exacto.
    public List<BoletaDTO>buscarMontoExacto(Integer monto){
        List<Boleta> boletas = boletaRepository.findByMonto(monto);
        if (boletas.isEmpty()){
            throw new RuntimeException("No se encontraron boletas con el monto " + monto);
        }
        return boletas.stream()
                .map(this::convertirADTO)
                .toList();
    }
    
    //Convertir a DTO
    private BoletaDTO convertirADTO(Boleta boleta) {
    BoletaDTO dto = new BoletaDTO();
    dto.setId(boleta.getId());
    dto.setTipoPago(boleta.getTipoPago());
    dto.setMonto(boleta.getMonto());
    
    if (boleta.getReservaHora() != null) {
        dto.setReservaHoraId(boleta.getReservaHora().getId());
    }
    return dto;
    }

}
