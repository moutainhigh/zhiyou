package com.gc;

import io.gd.generator.Config;
import io.gd.generator.Generator;
import io.gd.generator.handler.*;

public class Run {

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.setGenLogFile("/Users/freeman/IdeaProjects/gc/gc-service-impl/src/main/gc.log");
		config.setUrl("jdbc:mysql://192.168.10.240/gc");
		config.setEntityPackage("com.gc.entity");
//		config.setMybatisMapperPackage("com.gc.mapper");
//
//
//		config.setMybatisMapperPath("D:\\Work\\Workspace2016\\gc\\gc-service-impl\\src\\main\\java\\com\\gc\\mapper");
//		config.setMybatisXmlPath("D:\\Work\\Workspace2016\\gc\\gc-service-impl\\src\\main\\resources\\com\\gc\\mapping");
//		config.setUsername("root");
//		config.setPassword("123456");
//
//		config.setUseLombok(true);
//
//		config.setQueryModelPackage("com.gc.model.query");
//		config.setQueryModelPath("D:\\Work\\Workspace2016\\gc\\gc-service\\src\\main\\java\\com\\gc\\model\\query");


/*
		config.setQueryModelPackage("io.gd.generator.test.model.query");
		config.setQueryModelPath("D:\\Work\\Workspace2016\\gd-generator\\src\\test\\java\\io\\gd\\generator\\test\\model\\query\\");
*/
		Handler handler = new QueryModelHandler();

/*		Generator.generate(config,
				new VoHandler("com.gc.vo", "D:\\Work\\Workspace2016\\gc\\gc-component\\src\\main\\java\\com\\gc\\vo", true),
				new QueryModelHandler(),
				new MybatisMapperHandler(),
				new MybatisXmlHandler(),
				new MysqlHandler()


		);*/

		Generator.generate(config,
				new VoHandler("com.gc.vo", "/Users/freeman/IdeaProjects/gc/gc-service-impl/src/main/java/com/gc/vo", true)/*,
				new QueryModelHandler()
*/

		);

	}

}
