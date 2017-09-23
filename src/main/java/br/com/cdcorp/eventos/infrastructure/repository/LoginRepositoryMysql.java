package br.com.cdcorp.eventos.infrastructure.repository;

import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.repository.LoginRepository;
import br.com.cdcorp.eventos.infrastructure.exception.InfrastructureException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ceb on 12/07/17.
 */
@Repository
public class LoginRepositoryMysql implements LoginRepository {

    private static final Logger LOG = LoggerFactory.getLogger(LoginRepositoryMysql.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginRepositoryMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Login findByEmail(String email) {
        val sql = "SELECT id, login, senha, ativo, cadastro_confirmado FROM login where login = ?";
        List<Login> logins = jdbcTemplate.query(sql, new Object[] {email}, new LoginRowMapper());

        if(logins.isEmpty()) {
            return null;
        } else {
            return logins.get(0);
        }
    }

    public Login salvar(Login login) {
        val sql = "INSERT INTO login(login, senha, ativo, cadastro_confirmado) VALUES(?, ?, ?, ?)";

        jdbcTemplate.update(sql, stmt -> {
            stmt.setString(1, login.getLogin());
            stmt.setString(2, login.getSenha());
            stmt.setBoolean(3, login.getAtivo());
            stmt.setBoolean(4, login.getCadastroConfirmado());
        });

        return this.findByEmail(login.getLogin());
    }

    public Login get(Long id) {
        val sql = "SELECT id, login, senha, ativo, cadastro_confirmado FROM login where id = ?";
        List<Login> logins = jdbcTemplate.query(sql, new Object[] {id}, new LoginRowMapper());

        if(logins.isEmpty()) {
            return null;
        } else {
            return logins.get(0);
        }
    }

    public static class LoginRowMapper implements RowMapper<Login> {

        @Override
        public Login mapRow(ResultSet rs, int rowNum) {
            Login login = null;
            try {
                login = new Login(rs.getString("login"), rs.getString("senha"));
                login.setId(rs.getLong("id"));
                login.setAtivo(rs.getBoolean("ativo"));
                login.setCadastroConfirmado(rs.getBoolean("cadastro_confirmado"));
            } catch (SQLException ex) {
                LOG.info("Erro ao recuperar os dados do banco de dados", ex);
                throw new InfrastructureException("Erro ao recuperar os dados do banco de dados", ex);
            }

            return login;
        }
    }
}
