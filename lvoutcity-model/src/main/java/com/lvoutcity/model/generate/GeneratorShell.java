package com.lvoutcity.model.generate;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

/** 
* @ClassName: GeneratorShell 
* @Description: Model生成器
* @author Gdh
* @date 2016年4月25日 上午11:42:24 
*  
*/
public class GeneratorShell {
	
	public static DataSource getDataSource() {
		Prop p = PropKit.use("jdbc.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbc.url"), p.get("jdbc.user"), p.get("jdbc.password"),p.get("p.driverClassName"));
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}
	
	public static void main(String[] args) {
		String baseModelPackageName = "com.lvoutcity.model.base";
		String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"+baseModelPackageName.replaceAll("\\.", "/");
		String modelPackageName = "com.lvoutcity.model";
		String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"+modelPackageName.replaceAll("\\.", "/");
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		gernerator.setDialect(new MysqlDialect());
		gernerator.setGenerateDaoInModel(true);
		gernerator.setGenerateDataDictionary(false);
		gernerator.setRemovedTableNamePrefixes("t_");
		gernerator.generate();
	}
}




