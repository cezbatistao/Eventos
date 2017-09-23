package br.com.cdcorp.eventos.test.support;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.tools4j.spockito.Spockito;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(value = Spockito.class)
public abstract class UnitTest {
}
