package com.projeto.meninas.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.Regiao;
import com.projeto.meninas.Repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Carrega todos os ~5.570 municípios brasileiros a partir da API pública do IBGE
 * na primeira vez que a aplicação sobe (quando a tabela cidades está vazia).
 *
 * Endpoint utilizado:
 *   https://servicodados.ibge.gov.br/api/v1/localidades/municipios
 *
 * O JSON retornado já inclui UF e região, dispensando qualquer arquivo local.
 * Em caso de falha de rede, a aplicação sobe normalmente e loga um aviso.
 * Um admin pode acionar o seed manualmente via POST /api/cidades/seed.
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class CidadeDataInitializer implements CommandLineRunner {

    private static final String IBGE_URL =
            "https://servicodados.ibge.gov.br/api/v1/localidades/municipios";

    private final CidadeRepository cidadeRepository;

    @Override
    public void run(String... args) {
        if (cidadeRepository.count() > 0) {
            log.info("Tabela cidades já populada ({} registros). Seed ignorado.",
                    cidadeRepository.count());
            return;
        }
        carregarCidades();
    }

    public long carregarCidades() {
        log.info("Iniciando seed de cidades via API IBGE...");
        try {
            RestTemplate restTemplate = new RestTemplate();
            IbgeMunicipio[] municipios = restTemplate.getForObject(IBGE_URL, IbgeMunicipio[].class);

            if (municipios == null || municipios.length == 0) {
                log.warn("API IBGE não retornou municípios. Seed cancelado.");
                return 0;
            }

            List<Cidade> cidades = Arrays.stream(municipios)
                    .map(this::mapear)
                    .filter(Objects::nonNull)
                    .toList();

            cidadeRepository.saveAll(cidades);
            log.info("Seed concluído: {} cidades carregadas.", cidades.size());
            return cidades.size();

        } catch (Exception e) {
            log.warn("Falha ao carregar cidades da API IBGE: {}. " +
                     "A aplicação continua sem dados de cidades. " +
                     "Use POST /api/cidades/seed para tentar novamente.", e.getMessage());
            return 0;
        }
    }

    private Cidade mapear(IbgeMunicipio m) {
        try {
            String ufSigla = m.microrregiao().mesorregiao().uf().sigla();
            String regiaoSigla = m.microrregiao().mesorregiao().uf().regiao().sigla();
            Regiao regiao = Regiao.valueOf(regiaoSigla);

            return Cidade.builder()
                    .id((long) m.id())
                    .nome(m.nome())
                    .uf(ufSigla)
                    .regiao(regiao)
                    .build();
        } catch (Exception e) {
            log.debug("Município ignorado (erro de mapeamento): {} - {}", m.id(), m.nome());
            return null;
        }
    }

    // ── DTOs internos para desserializar o JSON da API IBGE ──────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IbgeMunicipio(
            int id,
            String nome,
            IbgeMicrorregiao microrregiao
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IbgeMicrorregiao(
            IbgeMesorregiao mesorregiao
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IbgeMesorregiao(
            @JsonProperty("UF") IbgeUf uf
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IbgeUf(
            String sigla,
            IbgeRegiao regiao
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IbgeRegiao(
            String sigla
    ) {}
}
