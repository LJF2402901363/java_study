package com.moyisuiying.booksystem.component;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 * Classname:CustomIdGenerator
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 21:11
 * @Version: 1.0
 **/
@Component
public class CustomIdGenerator  implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        //添加自动生成ID策略
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        //添加生成ID策略
        return null;
    }
}
