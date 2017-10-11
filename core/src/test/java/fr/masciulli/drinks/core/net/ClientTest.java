package fr.masciulli.drinks.core.net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import fr.masciulli.drinks.core.drinks.Drink;
import fr.masciulli.drinks.core.liquors.Liquor;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {
    private static final String FILE_DRINKS = "mock-drinks.json";
    private static final String FILE_LIQUORS = "mock-liquors.json";
    private static final int SIZE_DRINKS = 29;
    private static final int SIZE_LIQUORS = 12;

    private MockWebServer server;
    private Client client;

    @Before
    public void setup() throws IOException {
        server = new MockWebServer();
        server.start();

        client = new Client(server.url("/").toString());
    }

    @Test
    public void testThatDrinksAreRetrievedCorrectly() throws IOException {
        enqueueServerResponse(FILE_DRINKS);

        TestObserver<List<Drink>> observer = client.getDrinks().test();

        observer.assertNoErrors();

        List<Drink> drinks = observer.values().get(0);
        assertThat(drinks.size()).isEqualTo(SIZE_DRINKS);
    }

    @Test
    public void testThatLiquorsAreRetrievedCorrectly() throws IOException {
        enqueueServerResponse(FILE_LIQUORS);

        TestObserver<List<Liquor>> observer = client.getLiquors().test();

        observer.assertNoErrors();

        List<Liquor> liquors = observer.values().get(0);
        assertThat(liquors.size()).isEqualTo(SIZE_LIQUORS);
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
