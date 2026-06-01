package com.example.aihelpdesk.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.Collections;

/**
 * @Author wzh
 * @Date 2026/6/2 02:18
 */

public class CodeGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/aihelpdesk?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai",
                        "root",
                        ""
                )
                .globalConfig(builder -> builder
                        .author("wuzh")
                        .disableOpenDir()
                        .outputDir(projectPath + "/src/main/java")
                )
                .packageConfig(builder -> builder
                        .parent("com.example.aihelpdesk")
                        .entity("model.entity")
                        .mapper("mapper")
                        .pathInfo(Collections.singletonMap(
                                OutputFile.xml,
                                projectPath + "/src/main/resources/mapper"
                        ))
                )
                .strategyConfig(builder -> builder
                        .addInclude(
                                "sys_user",
                                "sys_role",
                                "sys_user_role",
                                "ticket",
                                "ticket_operation_log"
                        )
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .logicDeleteColumnName("deleted")
                        .mapperBuilder()
                        .enableMapperAnnotation()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
