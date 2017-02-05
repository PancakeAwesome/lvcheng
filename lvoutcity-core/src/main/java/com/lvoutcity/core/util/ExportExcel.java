package com.lvoutcity.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * 利用开源组件POI3.0.2动态导出EXCEL文档
 * 
 * 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * 
 * @version v1.0
 * 
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 * 
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 * 
 *            byte[]表jpg格式的图片数据
 * 
 */

public class ExportExcel {

	// public void exportExcel(Collection<T> dataset, OutputStream out) {
	//
	// exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
	//
	// }
	//
	// public void exportExcel(String[] headers, Collection<T> dataset,
	//
	// OutputStream out) {
	//
	// exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	//
	// }
	//
	// public void exportExcel(String[] headers, Collection<T> dataset,
	//
	// OutputStream out, String pattern) {
	//
	// exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);
	//
	// }

	/**
	 * 
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * 
	 * 
	 * @param title
	 * 
	 *            表格标题名
	 * 
	 * @param headers
	 * 
	 *            表格属性列名数组
	 * 
	 * @param dataset
	 * 
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 * 
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * 
	 * @param out
	 * 
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * 
	 * @param pattern
	 * 
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @throws InvalidFormatException
	 * 
	 */


	public void exportExcel(String fs, String title, String[] headers, String[] key, List<Record> dataset, long total,
			long start) {
		// OutputStream out;
		FileOutputStream out = null;
		try {
			// POIFSFileSystem ps=new POIFSFileSystem(fs);
			// //使用POI提供的方法得到excel的信息
			FileInputStream ft = new FileInputStream(fs); // 获取d://test.xls
			POIFSFileSystem ps = new POIFSFileSystem(ft);

			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(ps);
			HSSFSheet sheet = workbook.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
			HSSFRow row = null; // 获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值

			out = new FileOutputStream(fs); // 向d://test.xls中写数据
			
			HSSFCellStyle style = workbook.createCellStyle();
			setStyle(style);
			// 生成一个字体
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 12);
			setFont(font);
			// 把字体应用到当前的样式
			style.setFont(font);

			// 生成并设置另一个样式
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(IndexedColors.YELLOW.index);
			setStyle(style2);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			HSSFFont font2 = workbook.createFont();
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			// 把字体应用到当前的样式
			style2.setFont(font2);
			// 获取当前行数
			int row_number = sheet.getLastRowNum();
			// int row_number = (int) total;
			if (row_number == 0) {
				// 产生表格标题行
				HSSFRow headerRow = sheet.createRow(0);
				for (int k = 0; k < headers.length; k++) {
					HSSFCell cell = headerRow.createCell(k);
					cell.setCellValue(headers[k]);
				}
			}

			// XSSFRow row = null;
			for (int m = 0; m < dataset.size(); m++) {
				row = sheet.createRow(sheet.getLastRowNum() + 1);
				for (int n = 0; n < key.length; n++) {
					HSSFCell cell = row.createCell(n);
					cell.setCellStyle(style2);
					String textValue = "";
					if (LvoutcityUtils.isNotNull(dataset.get(m).get(key[n]))) {
						textValue = dataset.get(m).get(key[n]).toString();
					}
					cell.setCellValue(textValue);
				}
				row_number++;

			}
			out.flush();
			workbook.write(out);
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public HSSFSheet createSheet(HSSFWorkbook workbook) {
		HSSFSheet sheet = null;
		// 判断当前位置是否满足新建sheet
		// 生成一个表格
		sheet = workbook.createSheet("sheet1");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 30);
		return sheet;
	}

	public HSSFWorkbook createWork(String fileName) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		workbook.createSheet();
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.close();
		return workbook;
	}

	/**
	 * 设置样式
	 * 
	 * @param style
	 */
	public void setStyle(HSSFCellStyle style) {
		// 设置这些样式
		// style.setFillForegroundColor(XSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	}

	/**
	 * 设置字体
	 * 
	 * @param font
	 */
	public void setFont(HSSFFont font) {
		// font.setColor(XSSFColor..VIOLET.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	}

	public static void main(String[] args) {
		//
		// // 测试学生
//		//
//		ExportExcel ex = new ExportExcel();
//		//
//		String[] headers = { "学号", "姓名", "年龄", "性别", "出生日期" };
//		//
//		List<Student> dataset = new ArrayList<Student>();
//		//
//		dataset.add(new Student(10000001, "张三", 20, "男", new Date()));
//		//
//		dataset.add(new Student(20000002, "李四", 24, "女", new Date()));
//
//		dataset.add(new Student(30000003, "王五", 22, "男", new Date()));
//		//
//		String[] header = { "旅城号", "用户名", "操作详情", "状态", "操作时间", "错误原因" };
//		OutputStream out;
//		try {
//			// File file = new File("E://cc//a.xlsx");
//			// String parentPath = file.getParent();
//			// if (file.exists()) {
//			// file.delete();
//			// }
//			// File dirFile = new File(parentPath);
//			// if (!(dirFile.exists()) || !(dirFile.isDirectory())) {
//			// dirFile.mkdirs();
//			// try {
//			// Thread.sleep(500);
//			// } catch (InterruptedException e) {
//			// e.printStackTrace();
//			// }
//			// }
//			out = new FileOutputStream("E://a.xlsx");
//			// ex.exportExcel(header, dataset, out);
//			out.close();
//			System.out.println("excel导出成功！");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}