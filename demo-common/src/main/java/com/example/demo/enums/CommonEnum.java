package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 通用枚举类
 * Created by YuanJW on 2022/12/6.
 */
public interface CommonEnum {
    /**
     * 文件存储位置枚举类
     */
    @Getter
    @AllArgsConstructor
    public enum FileLocationEnum {
        LOCAL(1, "本地"),
        MINIO(2, "MINIO");

        private final Integer type;
        private final String location;

        public static FileLocationEnum toType(int type) {
            return Stream.of(values())
                    .filter(fileLocationEnum -> fileLocationEnum.type == type)
                    .findAny().orElse(null);
        }
    }

    /**
     * 数据源枚举类
     */
    public enum DataSourceEnum {
        MASTER,
        SLAVE;
    }
}
