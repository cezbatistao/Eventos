package br.com.cdcorp.eventos.infrastructure.repository;

import br.com.cdcorp.eventos.domain.model.Endereco;
import br.com.cdcorp.eventos.domain.model.PessoaFisica;
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica;
import br.com.cdcorp.eventos.domain.repository.LoginRepository;
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository;
import br.com.cdcorp.eventos.infrastructure.exception.EventosException;
import br.com.cdcorp.eventos.infrastructure.exception.InfrastructureException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ceb on 12/07/17.
 */
@Repository
class PessoaFisicaRepositoryMysql implements PessoaFisicaRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaFisicaRepositoryMysql.class);

    private JdbcTemplate jdbcTemplate;
    private LoginRepository loginRepository;

    @Autowired
    public PessoaFisicaRepositoryMysql(JdbcTemplate jdbcTemplate, LoginRepository loginRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.loginRepository = loginRepository;
    }


    public PessoaFisica salvar(PessoaFisica pessoaFisica) {
        val sql = "INSERT INTO pessoa_fisica(nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, " +
                "endereco_numero, endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, " +
                "login_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, stmt -> {
            Date dataNascimento = Date.valueOf(pessoaFisica.getDataNascimento());

            stmt.setString(1, pessoaFisica.getNome());
            stmt.setString(2, pessoaFisica.getEmail());
            stmt.setString(3, pessoaFisica.getCelular());
            stmt.setString(4, pessoaFisica.getTelefone());
            stmt.setDate(5, dataNascimento);
            stmt.setString(6, pessoaFisica.getCpf());
            stmt.setString(7, pessoaFisica.getRg());
            stmt.setString(8, pessoaFisica.getTipoPessoaFisica().name());
            stmt.setString(9, pessoaFisica.getEndereco().getLogradouro());
            stmt.setString(10, pessoaFisica.getEndereco().getNumero());
            stmt.setString(11, pessoaFisica.getEndereco().getComplemento());
            stmt.setString(12, pessoaFisica.getEndereco().getCep());
            stmt.setString(13, pessoaFisica.getEndereco().getBairro());
            stmt.setString(14, pessoaFisica.getEndereco().getEstado());
            stmt.setString(15, pessoaFisica.getEndereco().getCidade());
            stmt.setLong(16, pessoaFisica.getLogin().getId());
        });

        return this.findByCpf(pessoaFisica.getCpf());
    }

    public PessoaFisica findByCpf(String cpf) {
        // TODO pode-se usar a classe ResultSetExtractor para retornar
        val sql = "SELECT id, nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, endereco_numero, " +
                "endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, login_id FROM pessoa_fisica where cpf = ?";
        List<PessoaFisica> pessoasFisicas = jdbcTemplate.query(sql, new Object[] {cpf}, new PessoaFisicaRowMapper(this.loginRepository));

        if(pessoasFisicas.isEmpty()) {
            return null;
        } else {
            return pessoasFisicas.get(0);
        }
    }

    public PessoaFisica get(Long id) {
        val sql = "SELECT id, nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, endereco_numero, " +
                "endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, login_id FROM pessoa_fisica where id = ?";
        List<PessoaFisica> pessoasFisicas = jdbcTemplate.query(sql, new Object[] {id}, new PessoaFisicaRowMapper(this.loginRepository));

        if(pessoasFisicas.isEmpty()) {
            return null;
        } else {
            return pessoasFisicas.get(0);
        }
    }

    public PessoaFisica atualizar(PessoaFisica pessoaFisica) {
        val sql = "UPDATE pessoa_fisica set nome = ?, email = ?, celular = ?, telefone = ?, rg = ?, tipo_pessoa = ?, endereco_logradouro = ?, " +
                "endereco_numero = ?, endereco_complemento = ?, endereco_cep = ?, endereco_bairro = ?, endereco_estado = ?, " +
                "endereco_cidade = ? WHERE id = ?";
        jdbcTemplate.update(sql, stmt -> {
            stmt.setString(1, pessoaFisica.getNome());
            stmt.setString(2, pessoaFisica.getEmail());
            stmt.setString(3, pessoaFisica.getCelular());
            stmt.setString(4, pessoaFisica.getTelefone());
            stmt.setString(5, pessoaFisica.getRg());
            stmt.setString(6, pessoaFisica.getTipoPessoaFisica().name());
            stmt.setString(7, pessoaFisica.getEndereco().getLogradouro());
            stmt.setString(8, pessoaFisica.getEndereco().getNumero());
            stmt.setString(9, pessoaFisica.getEndereco().getComplemento());
            stmt.setString(10, pessoaFisica.getEndereco().getCep());
            stmt.setString(11, pessoaFisica.getEndereco().getBairro());
            stmt.setString(12, pessoaFisica.getEndereco().getEstado());
            stmt.setString(13, pessoaFisica.getEndereco().getCidade());
            stmt.setLong(14, pessoaFisica.getId());
        });

        return pessoaFisica;
    }

    public static class PessoaFisicaRowMapper implements RowMapper<PessoaFisica> {

        private LoginRepository loginRepository;

        public PessoaFisicaRowMapper(LoginRepository loginRepository) {
            this.loginRepository = loginRepository;
        }

        @Override
        public PessoaFisica mapRow(ResultSet rs, int rowNum) {
            PessoaFisica pessoaFisica = null;
            try {
                val dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                val tipoPessoaFisica = TipoPessoaFisica.valueOf(rs.getString("tipo_pessoa"));
                val endereco = new Endereco(rs.getString("endereco_logradouro"), rs.getString("endereco_numero"), rs.getString("endereco_complemento"),
                        rs.getString("endereco_cep"), rs.getString("endereco_bairro"), rs.getString("endereco_estado"), rs.getString("endereco_cidade"));

                pessoaFisica = new PessoaFisica(rs.getString("nome"), rs.getString("email"), rs.getString("celular"), dataNascimento, rs.getString("cpf"),
                        endereco, tipoPessoaFisica);
                pessoaFisica.setId(rs.getLong("id"));
                pessoaFisica.setTelefone(rs.getString("telefone"));
                pessoaFisica.setRg(rs.getString("rg"));

                val login = this.loginRepository.get(rs.getLong("login_id"));
                pessoaFisica.setLogin(login);
            } catch (SQLException ex) {
                LOG.info("Erro ao recuperar os dados do banco de dados", ex);
                throw new InfrastructureException("Erro ao recuperar os dados do banco de dados", ex);
            }

            return pessoaFisica;
        }
    }
}
