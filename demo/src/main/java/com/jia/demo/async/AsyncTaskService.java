package com.jia.demo.async;

import com.alibaba.fastjson.JSONObject;

import com.jia.demo.user.service.DictAspect;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * 用于多线程异步实现
 * @author csm
 * @version 1.0
 * @date 2022/2/18 10:05
 */
@Component
@Slf4j
public class AsyncTaskService {

    @Autowired
    @Lazy
    private DictAspect dictAspect;


    /**
     * 数据字典转换-多线程
     * @param list
     * @param countDownLatch
     * @return
     */
    @Async
    public Future<List<JSONObject>> getDictAspectListByAsync(List<?> list, CountDownLatch countDownLatch){
        List<JSONObject> items = new ArrayList<>();
        try {
            log.info("数据字典转换_线程{}执行异步任务",Thread.currentThread().getName());

            for (Object record : list) {
                JSONObject item = dictAspect.parseDetail(record);
                items.add(item);
            }
        }finally {
            countDownLatch.countDown();
        }
        return new AsyncResult<>(items);
    }


}
