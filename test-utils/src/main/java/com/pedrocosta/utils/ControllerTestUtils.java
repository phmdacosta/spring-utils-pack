package com.pedrocosta.utils;

import org.mockito.Matchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public interface ControllerTestUtils {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());

    public static MockHttpServletRequestBuilder restPost(String route, String content) throws Exception {
        return MockMvcRequestBuilders.post(route)
                .contentType(APPLICATION_JSON_UTF8)
                .content(content)
                .characterEncoding(StandardCharsets.UTF_8.name());
    }

    public static ResultActions expectResponseEntity(ResultActions resultAction, ResultMatcher[] expectMatchers) throws Exception {
        resultAction = resultAction.andDo(MockMvcResultHandlers.print());
        for (ResultMatcher resultMatcher : expectMatchers) {
            resultAction = resultAction.andExpect(resultMatcher);
        }
        return resultAction;
    }

    public static ResultActions expectResponseEntitySuccess(ResultActions resultAction, ResultMatcher ... expectMatchers) throws Exception {
        List<ResultMatcher> matchers = new ArrayList<>(Arrays.asList(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.timestamp", is(notNullValue())),
                MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()),
                MockMvcResultMatchers.jsonPath("$.success").value(true)));
        matchers.addAll(Arrays.asList(expectMatchers));
        return expectResponseEntity(resultAction, matchers.toArray(new ResultMatcher[0]));
    }

    public static ResultActions expectResponseEntityError(HttpStatus httpStatus, ResultActions resultAction) throws Exception {
        ResultMatcher[] matchers = {
                MockMvcResultMatchers.status().is(httpStatus.value()),
                MockMvcResultMatchers.jsonPath("$.timestamp", is(notNullValue())),
                MockMvcResultMatchers.jsonPath("$.status").value(httpStatus.value()),
                MockMvcResultMatchers.jsonPath("$.success").value(false),
                MockMvcResultMatchers.jsonPath("$.message", is(notNullValue()))
        };
        return expectResponseEntity(resultAction, matchers);
    }
}
