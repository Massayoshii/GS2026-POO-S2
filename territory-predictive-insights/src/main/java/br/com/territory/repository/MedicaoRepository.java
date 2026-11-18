package br.com.territory.repository;

import br.com.territory.dominio.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {
    List<Medicao> findByTipoSensorOrderByDataHoraDesc(String tipoSensor);

    List<Medicao> findAllByOrderByDataHoraDesc();
}
