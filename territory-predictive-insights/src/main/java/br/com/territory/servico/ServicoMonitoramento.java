package br.com.territory.servico;

import br.com.territory.repository.AlertaRepository;
import br.com.territory.repository.MedicaoRepository;
import org.springframework.stereotype.Service;

@Service
public class ServicoMonitoramento {

    private final MedicaoRepository medicaoRepository;
    private final AlertaRepository alertaRepository;

    public ServicoMonitoramento(
            MedicaoRepository medicaoRepository,
            AlertaRepository alertaRepository) {

        this.medicaoRepository = medicaoRepository;
        this.alertaRepository = alertaRepository;
    }
}
