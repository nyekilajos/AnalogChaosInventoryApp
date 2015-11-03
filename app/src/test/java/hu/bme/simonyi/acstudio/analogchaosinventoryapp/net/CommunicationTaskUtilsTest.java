package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;

/**
 * Created by Lajos_Nyeki on 10/15/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CommunicationTaskUtilsTest {

    @Test
    public void testIsAuthenticationFaiedForNullThrowable() {
        Assert.assertFalse(CommunicationTaskUtils.isAuthenticationFailed(null));
    }

    @Test
    public void testIsAuthenticationFaiedForNotHttpException() {
        Assert.assertFalse(CommunicationTaskUtils.isAuthenticationFailed(new Exception()));
    }

    @Test
    public void testIsAuthenticationFaiedForAcceptedStatus() {
        Assert.assertFalse(CommunicationTaskUtils.isAuthenticationFailed(new HttpStatusCodeException(HttpStatus.ACCEPTED) {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.ACCEPTED;
            }
        }));
    }

    @Test
    public void testIsAuthenticationFaiedForUnauthorized() {
        Assert.assertTrue(CommunicationTaskUtils.isAuthenticationFailed(new HttpStatusCodeException(HttpStatus.UNAUTHORIZED) {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.UNAUTHORIZED;
            }
        }));
    }

}
