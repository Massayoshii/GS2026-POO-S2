package br.com.territory.repository;

import br.com.territory.dominio.Alerta;
import br.com.territory.dominio.NivelAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findAllByOrderByDataHoraDesc();

    List<Alerta> findByTipoSensorOrderByDataHoraDesc(String tipoSensor);

    List<Alerta> findByNivelOrderByDataHoraDesc(NivelAlerta nivel);
}
