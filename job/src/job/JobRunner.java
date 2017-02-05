package job;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.lvoutcity.model._MappingKit;

public class JobRunner{
	private static final Logger log = Logger.getLogger(JobRunner.class);

	private static String getDesc(Method inv){
		String value = null;
		if (inv.getAnnotation(JobDescription.class) != null
				&& !inv.getAnnotation(JobDescription.class).value().trim().isEmpty()) {
			value = inv.getAnnotation(JobDescription.class).value();

		}
		return value;
	}
	
	private static Integer getOrder(Method inv){
		Integer order = null;
		if (inv.getAnnotation(JobOrder.class) != null) {
			order = inv.getAnnotation(JobOrder.class).value();
		}
		return order;
	}
	
	private static Method[] sort(Method[] mtds){
		Method[] re = new Method[mtds.length];
		for(Method m : mtds){
			Integer o = getOrder(m);
			if(o!=null){
				re[o]= m;
			}
		}
		return re;
	}
	
	public static void init(){
		Prop p = PropKit.use("resources/config.props");
//		Prop p = PropKit.use("jdbc.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbc.url"), p.get("jdbc.user"), p.get("jdbc.password"),p.get("p.driverClassName"));
		druidPlugin.start();
		
		
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setShowSql(true);
		_MappingKit.mapping(activeRecordPlugin);
		activeRecordPlugin.start();
	}
	
	public static void main(String[] args) {
		String strValue = args[0].trim();
//		String strValue = "daily";
		if(strValue==null){
			System.out.println("wrong parameter");
			return;
		}
		init();
		Method[] methods = null;
		if(strValue.equals("daily")){
			methods = DailyJob.class.getMethods();
		}else if (strValue.equals("monthly")){
			methods = MonthlyJob.class.getMethods();
		}
		methods = sort(methods);
		for (Method inv : methods) {
			if(inv==null)
				continue;
			String value = getDesc(inv);
			try {
				if (value != null){
					log.info("【-------" + value + "开始执行-------】");
					inv.invoke(null, null);
				}
			} catch (Exception e) {
				log.error("执行异常：",e);
			} finally {
				if (value != null)
					log.info("【-------" + value + "执行结束-------】");
			}
		}

	}
}
