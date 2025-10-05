package com.example.farmacy.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class SequenceSyncUtil {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void syncSequence(String tableName, String idColumn) {
        try {
            // Obtener el valor máximo actual del ID
            Long maxId = (Long) entityManager
                    .createNativeQuery("SELECT COALESCE(MAX(" + idColumn + "), 0) FROM " + tableName)
                    .getSingleResult();

            // Actualizar la secuencia (PostgreSQL)
            String seqQuery = "SELECT setval(pg_get_serial_sequence('" + tableName + "', '" + idColumn + "'), " + maxId + ")";
            entityManager.createNativeQuery(seqQuery).getSingleResult();

            System.out.println("✅ Secuencia sincronizada para " + tableName + " (nuevo valor: " + maxId + ")");
        } catch (Exception e) {
            System.err.println("⚠️ Error al sincronizar secuencia para " + tableName + ": " + e.getMessage());
        }
    }
}
