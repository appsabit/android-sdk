package com.sensorberg.sdk.scanner;

import com.sensorberg.sdk.SensorbergApplicationTest;
import com.sensorberg.sdk.internal.AndroidPlatform;
import com.sensorberg.sdk.internal.Platform;
import com.sensorberg.sdk.resolver.ResolverListener;
import com.sensorberg.sdk.settings.Settings;
import com.sensorberg.sdk.testUtils.NoClock;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import util.TestConstants;

import static com.sensorberg.sdk.scanner.RecordedRequestAssert.assertThat;
import static org.mockito.Mockito.spy;

public class TheBeaconActionHistoryPublisherIntegrationShould extends SensorbergApplicationTest{


    private  ScanEvent SCAN_EVENT;
    private BeaconActionHistoryPublisher tested;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Platform platform = spy(new AndroidPlatform(getContext()));
        Settings settings = new Settings(platform);
        tested = new BeaconActionHistoryPublisher(platform, platform.getTransport(), ResolverListener.NONE, settings, NoClock.CLOCK);

        startWebserver();
        server.enqueue(new MockResponse().setBody("{}"));
        SCAN_EVENT = new ScanEvent.Builder()
                .withEventMask(ScanEventType.ENTRY.getMask())
                .withBeaconId(TestConstants.ANY_BEACON_ID)
                .withEventTime(100)
                .build();
    }

    public void test_should_send_history_to_the_server() throws Exception {
        tested.onScanEventDetected(SCAN_EVENT);
        tested.publishHistory();

        RecordedRequest request = server.takeRequest();

        assertThat(request).matchesRawResourceRequest(com.sensorberg.sdk.test.R.raw.request_reporting_001, getContext());
    }
}
