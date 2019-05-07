package org.landy.common.web.controller;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author: Landy
 * @date: 2019/5/7 22:54
 * @description:
 */
public class ExportControllerTest extends AbstractControllerTest {

    /**
     * MockMvcRequestBuilders: 用于构建 MockHttpServletRequest。
     * MockMvcResultMatchers: 用于构建 ResultMatcher ，描述匹配执行请求期望返回结果。
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/exportXls");
        this.mockMvc.perform(builder)
                .andExpect(ok);
    }

}
