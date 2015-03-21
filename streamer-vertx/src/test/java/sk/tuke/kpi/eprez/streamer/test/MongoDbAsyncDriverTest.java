package sk.tuke.kpi.eprez.streamer.test;

import java.io.IOException;

import sk.tuke.kpi.eprez.streamer.SharedData;

public class MongoDbAsyncDriverTest {

	public static void main(final String[] args) throws IOException {
		SharedData.presentation().findBySessionToken("55072721ddcc0b48b6ddd6fe", (throwable, result) -> {
		});

		System.in.read();
	}
}
