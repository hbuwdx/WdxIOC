package hbu.spring.di;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlClasspathConfiguration implements BeanFactory {
	private String xmlfile="beans.xml";
	
	private Map<String,Object> beans=new HashMap<String, Object>();
	
	public XmlClasspathConfiguration(String xmlfile) {
		super();
		this.xmlfile = xmlfile;
		SAXReader reader=new SAXReader();
		try {
			Document document=reader.read(new File(xmlfile));
			Element root=document.getRootElement();
			
			List<Element> beanNodes=root.selectNodes("//bean");
			for(Element bean:beanNodes){
				String name=bean.attributeValue("id");
				String clazz=bean.attributeValue("class");
				Object object=Class.forName(clazz).newInstance();
				beans.put(name, object);
			}
			List<Element> propertyNodes=root.selectNodes("//bean/property");
			for(Element node:propertyNodes){
				String parentName=node.getParent().attributeValue("id");
				String propertyName=node.attributeValue("name");
				String refBean=node.attributeValue("ref");
				if(!beans.containsKey(parentName)){
					return;
				}
				Object parentO=beans.get(parentName);
				if(beans.containsKey(refBean)){
					Object object=beans.get(refBean);
					String methodString="set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
					Method method=parentO.getClass().getDeclaredMethod(methodString, object.getClass().getInterfaces()[0]);
					method.invoke(parentO,object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Object getBean(String name) {
		if(!beans.containsKey(name)){
			return null;
		}
		return beans.get(name);
	}

}
