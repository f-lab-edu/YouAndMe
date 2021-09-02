package com.yam.app;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

final class YouAndMeApplicationTests {

    @Test
    @DisplayName("SpringApplication.run() 실행을 Mocking합니다.")
    void mainShouldStartMyApplication() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

            mocked.when(
                    () -> SpringApplication.run(YouAndMeApplication.class, "foo"))
                .thenReturn(null);

            YouAndMeApplication.main(new String[]{"foo"});

            mocked.verify(
                () -> SpringApplication.run(YouAndMeApplication.class, "foo"));
        }
    }
}
