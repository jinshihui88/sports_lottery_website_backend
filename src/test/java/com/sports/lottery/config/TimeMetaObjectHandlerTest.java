package com.sports.lottery.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sports.lottery.entity.BettingRecord;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TimeMetaObjectHandlerTest {

    @BeforeAll
    static void initTableInfo() {
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        TableInfoHelper.initTableInfo(builderAssistant, BettingRecord.class);
    }

    @Test
    void insertFillShouldSetCreateTimeAndUpdateTime() {
        TimeMetaObjectHandler handler = new TimeMetaObjectHandler();
        BettingRecord record = new BettingRecord();
        MetaObject metaObject = SystemMetaObject.forObject(record);

        handler.insertFill(metaObject);

        assertNotNull(record.getCreateTime());
        assertNotNull(record.getUpdateTime());
    }

    @Test
    void updateFillShouldSetUpdateTimeOnly() {
        TimeMetaObjectHandler handler = new TimeMetaObjectHandler();
        BettingRecord record = new BettingRecord();
        MetaObject metaObject = SystemMetaObject.forObject(record);

        handler.updateFill(metaObject);

        assertNull(record.getCreateTime());
        assertNotNull(record.getUpdateTime());
    }
}
