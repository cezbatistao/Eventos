package br.com.cdcorp.eventos.test.support;

import br.com.cdcorp.eventos.infrastructure.config.EventosApplication;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { EventosApplication.class })
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @After
    public void cleanup() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(this.getClass().getClassLoader().getResource("db/cleanup_tables.sql").getFile()))) {
            stream.forEach(jdbcTemplate::execute);
        }
    }
}
