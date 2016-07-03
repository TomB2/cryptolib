package org.cryptomator.cryptolib;

import java.security.SecureRandom;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReseedingSecureRandomTest {

	private SecureRandom seeder, csprng;

	@Before
	public void setup() {
		seeder = Mockito.mock(SecureRandom.class);
		csprng = Mockito.mock(SecureRandom.class);
		Mockito.when(seeder.generateSeed(Mockito.anyInt())).then(new Answer<byte[]>() {
			@Override
			public byte[] answer(InvocationOnMock invocation) throws Throwable {
				int num = invocation.getArgumentAt(0, Integer.class);
				return new byte[num];
			}
		});

	}

	@Test
	public void testReseedAfterLimitReached() {
		SecureRandom rand = new ReseedingSecureRandom(seeder, csprng, 10, 1);
		rand.nextBytes(new byte[4]);
		rand.nextBytes(new byte[4]);
		Mockito.verify(seeder, Mockito.times(1)).generateSeed(1);
		rand.nextBytes(new byte[4]);
		Mockito.verify(seeder, Mockito.times(2)).generateSeed(1);
	}

}