package fr.masciulli.drinks.net;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@Config(sdk = 23)
@RunWith(RobolectricTestRunner.class)
public class ClientTest {
    private static final String FILE_DRINKS = "mock-drinks.json";
    private static final int SIZE_DRINKS = 29;

    private MockWebServer server;
    private Client client;

    @Before
    public void setup() throws IOException {
        server = new MockWebServer();
        server.start();

        client = new Client(RuntimeEnvironment.application, server.url("/").toString());
    }

    @Test
    public void testThatDrinksAreRetrievedCorrectly() throws IOException {
        enqueueServerResponse(FILE_DRINKS);

        TestSubscriber<List<Drink>> subscriber = new TestSubscriber<>();
        client.getDrinks().subscribe(subscriber);

        subscriber.assertNoErrors();

        List<Drink> drinks = subscriber.getOnNextEvents().get(0);
        assertThat(drinks.size()).isEqualTo(SIZE_DRINKS);
    }

    private void enqueueServerResponse(String bodyFile) throws IOException {
        MockResponse serverResponse = new MockResponse();
        Buffer buffer = new Buffer();
        buffer.readFrom(getClass().getResourceAsStream(bodyFile));
        serverResponse.setBody(buffer);
        server.enqueue(serverResponse);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
