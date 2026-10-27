package com.sports.lottery.service.impl;

import com.sports.lottery.mapper.RecommendRecordMapper;
import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendRecordServiceImplTest {

    @Mock
    private RecommendRecordMapper recommendRecordMapper;

    private RecommendRecordServiceImpl recommendRecordService;

    @BeforeEach
    void setUp() {
        recommendRecordService = new RecommendRecordServiceImpl();
        ReflectionTestUtils.setField(recommendRecordService, "baseMapper", recommendRecordMapper);
    }

    @Test
    void listRecommendersShouldReturnDistinctRecommendersFromMapper() {
        List<String> recommenders = Arrays.asList("alice", "bob");
        when(recommendRecordMapper.selectDistinctRecommenders()).thenReturn(recommenders);

        List<String> result = recommendRecordService.listRecommenders();

        assertSame(recommenders, result);
        verify(recommendRecordMapper).selectDistinctRecommenders();
    }

    @Test
    void selectDistinctRecommendersShouldUseExpectedSqlFilter() throws NoSuchMethodException {
        Method method = RecommendRecordMapper.class.getMethod("selectDistinctRecommenders");
        Select select = method.getAnnotation(Select.class);

        assertEquals(
                "SELECT DISTINCT recommender FROM recommend_record WHERE deleted = 0 AND recommender != ''",
                String.join(" ", select.value()));
    }
}
