package com.pruebas.pruebas.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.pruebas.dto.EncuestaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EncuestaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ TEST 1: Caracteres españoles (CASO MÁS COMÚN)
    @Test
    void testCaracteresEspanoles() throws Exception {
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Encuesta: Satisfacción del cliente en el restaurante");
        encuesta.setDescripcion("¿Cómo calificarías tu experiencia? ¡Déjanos tu opinión sobre el servicio, comida y atención!");
        encuesta.setEstado("ACTIVA");

        mockMvc.perform(post("/api/encuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Encuesta: Satisfacción del cliente en el restaurante"))
                .andExpect(jsonPath("$.descripcion").value("¿Cómo calificarías tu experiencia? ¡Déjanos tu opinión sobre el servicio, comida y atención!"));
    }

    // ✅ TEST 2: Preguntas con opciones múltiples (caso real)
    @Test
    void testPreguntasConOpciones() throws Exception {
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Preferencias de café: ¿Caliente/Frío? ¿Con/Sin azúcar?");
        encuesta.setDescripcion("Selecciona tus preferencias: ☕ Caliente 🧊 Frío 🍚 Con azúcar 🚫 Sin azúcar");
        encuesta.setEstado("ACTIVA");

        mockMvc.perform(post("/api/encuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Preferencias de café: ¿Caliente/Frío? ¿Con/Sin azúcar?"))
                .andExpect(jsonPath("$.descripcion").value("Selecciona tus preferencias: ☕ Caliente 🧊 Frío 🍚 Con azúcar 🚫 Sin azúcar"));
    }

    // ✅ TEST 3: Encuesta de evaluación (caso real)
    @Test
    void testEncuestaEvaluacion() throws Exception {
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Evaluación de servicio: Del 1⭐ al 5⭐⭐⭐⭐⭐");
        encuesta.setDescripcion("Califica nuestro servicio en: Atención 📞, Calidad 🍽️, Tiempo ⏱️, Precio 💰");
        encuesta.setEstado("ACTIVA");

        mockMvc.perform(post("/api/encuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Evaluación de servicio: Del 1⭐ al 5⭐⭐⭐⭐⭐"))
                .andExpect(jsonPath("$.descripcion").value("Califica nuestro servicio en: Atención 📞, Calidad 🍽️, Tiempo ⏱️, Precio 💰"));
    }

    // ✅ TEST 4: Encuesta con hashtags y menciones (caso moderno)
    @Test
    void testEncuestaConHashtags() throws Exception {
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Encuesta #ExperienciaCliente 2024");
        encuesta.setDescripcion("Participa y gana! @TodosLosClientes #Opinión #Satisfacción 💬");
        encuesta.setEstado("ACTIVA");

        mockMvc.perform(post("/api/encuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Encuesta #ExperienciaCliente 2024"))
                .andExpect(jsonPath("$.descripcion").value("Participa y gana! @TodosLosClientes #Opinión #Satisfacción 💬"));
    }

    // ✅ TEST 5: Límite razonable (texto largo pero dentro de límites)
    @Test
    void testTextoDentroDeLimites() throws Exception {
        // Texto largo pero razonable para una encuesta
        String titulo = "Encuesta completa sobre experiencia del cliente en todos nuestros servicios y productos ofrecidos durante el año 2024";
        String descripcion = "Esta encuesta busca recopilar tu opinión sobre diversos aspectos de nuestro servicio: calidad, atención al cliente, tiempos de entrega, relación precio-calidad y satisfacción general. ¡Tu feedback es muy importante para nosotros!";

        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo(titulo);
        encuesta.setDescripcion(descripcion);
        encuesta.setEstado("ACTIVA");

        mockMvc.perform(post("/api/encuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(titulo))
                .andExpect(jsonPath("$.descripcion").value(descripcion));
    }
}