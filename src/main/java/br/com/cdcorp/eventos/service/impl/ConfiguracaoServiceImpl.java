package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.service.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by ceb on 12/07/17.
 */
@Service
public class ConfiguracaoServiceImpl implements ConfiguracaoService {

    private Environment env;

    @Autowired
    public ConfiguracaoServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public String get(String propriedade) {
        return env.getProperty(propriedade);
    }
}
