package br.com.cdcorp.eventos.infrastructure.encode;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by ceb on 12/07/17.
 */
@Service
public class CriptografiaSenha {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public CriptografiaSenha(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String gerarSenha() {
        return RandomStringUtils.random(6, 0, 20, true, true, "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
    }

    public String criptografar(String senha) {
        return passwordEncoder.encode(senha);
    }

    public String gerarSenhaCriptografada() {
        return criptografar(gerarSenha());
    }
}
