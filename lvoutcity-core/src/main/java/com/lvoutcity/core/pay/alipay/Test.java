package com.lvoutcity.core.pay.alipay;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Test {
	public final static void main(String[] args) {
		HashMap<?, ?> a = new HashMap<>();
		XmlMapper mapper = new XmlMapper();
		String a1 = "<xml version=\"1.0\" encoding=\"GBK\" ><alipay><is_success>F</is_success><error>BATCH_NO_FORMAT_ERROR</error></alipay></xml>";
		try {
			a = mapper.readValue(a1, HashMap.class);
			System.out.println(a.toString()); //WOW
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
