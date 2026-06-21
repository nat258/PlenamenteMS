package com.example.ms_profesionales.service;

import java.lang.annotation.Inherited;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_profesionales.repository.PsicologoRepository;
import com.example.ms_profesionales.service.PsicologoService;

@ExtendWith(MockitoExtension.class)

class ProfesionalesApplicationTests {

    @Mock
    private PsicologoRepository psicologoRepository;   

    @InjectMocks
    private PsicologoService psicologoService;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testBuscar
    
    }
}
