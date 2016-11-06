package fr.masciulli.drinks.net;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23)
public class CustomCacheControlInterceptorTest {

    private CustomCacheControlInterceptor interceptor;

    @Mock
    private ConnectivityChecker connectivityChecker;

    @Before
    public void setup() {
        initMocks(this);
        interceptor = new CustomCacheControlInterceptor(connectivityChecker);
    }

    @Test
    public void testThatInterceptorSetsCorrectHeadersWhenConnected() throws IOException {
        when(connectivityChecker.isConnectedOrConnecting()).thenReturn(true);

        Interceptor.Chain chain = new FakeChain() {
            @Override
            public Response proceed(Request request) throws IOException {
                assertThat(request.header("Cache-Control")).isEqualTo("public");
                return null;
            }
        };

        interceptor.intercept(chain);
    }

    @Test
    public void testThatInterceptorSetsCorrectHeadersWhenNotConnected() throws IOException {
        when(connectivityChecker.isConnectedOrConnecting()).thenReturn(false);

        Interceptor.Chain chain = new FakeChain() {
            @Override
            public Response proceed(Request request) throws IOException {
                assertThat(request.header("Cache-Control")).startsWith("public, only-if-cached, max-stale=");
                return null;
            }
        };

        interceptor.intercept(chain);
    }

    abstract class FakeChain implements Interceptor.Chain {
        @Override
        public Request request() {
            return new Request.Builder()
                    .url("http://test.com/test")
                    .get()
                    .build();
        }

        @Override
        public Connection connection() {
            return null;
        }
    }
}
