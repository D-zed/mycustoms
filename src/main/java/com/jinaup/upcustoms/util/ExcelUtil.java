package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import com.jinaup.enums.OrderCellEnum;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
	
//		private final static Logger LOG = Logger.getLogger(ImportExcelUtil.class.getName());

	 	private final static String excel2003L =".xls";    //2003- 版本的excel  
	    private final static String excel2007U =".xlsx";   //2007+ 版本的excel  
	      
	    /** 
	     * 描述：获取IO流中的数据，组装成List<List<Object>>对象  通用方法
	     * @param in,fileName 
	     * @return 
	     * @throws IOException  
	     */  
		public  List<List<Object>> getBankListByExcel(InputStream in,String fileName) throws Exception{  
	        List<List<Object>> list = null;  
	          
	        //创建Excel工作薄  
	        Workbook work = this.getWorkbook(in,fileName);  
	        if(null == work){  
	            throw new Exception("创建Excel工作薄为空！");  
	        }  
	        Sheet sheet = null;  
	        Row row = null;  
	        Cell cell = null;  
	          
	        list = new ArrayList<List<Object>>();  
	        //遍历Excel中所有的sheet  
	        for (int i = 0; i < work.getNumberOfSheets(); i++) {  
	            sheet = work.getSheetAt(i);  
	            if(sheet==null){continue;}  
	              
	            //遍历当前sheet中的所有行  
	            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {  
	                row = sheet.getRow(j);  
	                if(row==null||row.getFirstCellNum()==j){continue;}  
	                  
	                //遍历所有的列  
	                List<Object> li = new ArrayList<Object>();  
	                for (int y = row.getFirstCellNum(); y <= row.getLastCellNum(); y++) {
	                    cell = row.getCell(y);
	                    li.add(this.getCellValue(cell));  
	                }  
	                list.add(li);  
	            }  
	        } 
	        return list;  
	    }  
	    
	    public  List<JSONObject> getByExcel(InputStream in,String fileName) throws Exception{  
	    	List<JSONObject> list = null;  
	    	
	    	//创建Excel工作薄  
	    	Workbook work = this.getWorkbook(in,fileName);  
	    	if(null == work){  
	    		throw new Exception("创建Excel工作薄为空！");  
	    	}  
	    	Sheet sheet = null;  
	    	Row row = null;  
	    	Cell cell = null;  
	    	
	    	list = new ArrayList<JSONObject>();  
	    	//遍历Excel中所有的sheet  
	    	for (int i = 0; i < work.getNumberOfSheets(); i++) {  
	    		sheet = work.getSheetAt(i);  
	    		if(sheet==null){continue;}  
	    		int firstRowNum = sheet.getFirstRowNum();
	    		Row mapRow = sheet.getRow(firstRowNum);
	    		if(mapRow==null){continue;}
	    		/**
	    		 * map 存放 poiCellEnum index，对应xls列参数 
	    		 */
	    		Map<Integer, Integer> map = new HashMap<Integer,Integer>();
	    		for(int m = mapRow.getFirstCellNum();m < mapRow.getLastCellNum();m++){
	    			for(OrderCellEnum orderCellEnum : OrderCellEnum.values()){
	    				if(orderCellEnum.getName().equalsIgnoreCase(mapRow.getCell(m).getStringCellValue())){
	    					map.put(m,orderCellEnum.getIndex());
	    				}
	    			}
	    		}
	    		//遍历当前sheet中的所有行  
	    		JSONObject jsonObject = null;
	    		for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {  
	    			row = sheet.getRow(j);
	    			if(row==null||row.getFirstCellNum()==j){continue;}  
	    			//遍历所有的列  
	    			jsonObject = new JSONObject();
	    			for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {  
	    				cell = row.getCell(y);
	    				if(StringUtils.isEmpty(this.getCellValue(cell)) || map.get(y) == null){
	    					continue;
	    				}
	    				if(map.get(y) == null)
	    					continue;
	    				jsonObject.put(OrderCellEnum.getName(map.get(y)),this.getCellValue(cell));
	    			}  
	    			list.add(jsonObject);  
	    		}  
	    	} 
	    	return list;  
	    }  
	      
	    /** 
	     * 描述：根据文件后缀，自适应上传文件的版本  
	     * @param inStr,fileName 
	     * @return 
	     * @throws Exception 
	     */  
	    public  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{  
	        Workbook wb = null;  
	        String fileType = fileName.substring(fileName.lastIndexOf("."));  
	        if(excel2003L.equals(fileType)){  
	            wb = new HSSFWorkbook(inStr);  //2003-  
	        }else if(excel2007U.equals(fileType)){  
	            wb = new XSSFWorkbook(inStr);  //2007+  
	        }else{  
	            throw new Exception("解析的文件格式有误！");  
	        }  
	        return wb;  
	    }  
	  
	    /** 
	     * 描述：对表格中数值进行格式化 
	     * @param cell 
	     * @return 
	     */  
	    public  Object getCellValue(Cell cell){
	    	
	    	if(cell == null){
	    		return null;
	    	}
	        Object value = null;  
	        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化  
	        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字  
	          
	        switch (cell.getCellType()) {  
	        case Cell.CELL_TYPE_STRING:  
	            value = cell.getRichStringCellValue().getString();  
	            break;  
	        case Cell.CELL_TYPE_NUMERIC:  
	            if("General".equals(cell.getCellStyle().getDataFormatString())){  
	                value = df.format(cell.getNumericCellValue());  
	            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){  
	                value = sdf.format(cell.getDateCellValue());  
	            }else{  
	                value = df2.format(cell.getNumericCellValue());  
	            }  
	            break;  
	        case Cell.CELL_TYPE_BOOLEAN:  
	            value = cell.getBooleanCellValue();  
	            break;  
	        case Cell.CELL_TYPE_BLANK:  
	            value = null;  
	            break;  
	        default: 
	        	value = null;
	            break;  
	        }  
	        return value;  
	    }  
	    
	    
	    
	    /**
	     * 导出Excel
	     * @param sheetName sheet名称
	     * @param title 标题
	     * @param values 内容
	     * @param wb HSSFWorkbook对象
	     * @return
	     */
	    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

	        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
	        if(wb == null){
	            wb = new HSSFWorkbook();
	        }

	        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet sheet = wb.createSheet(sheetName);

	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
	        HSSFRow row = sheet.createRow(0);

	        // 第四步，创建单元格，并设置值表头 设置表头居中
	        HSSFCellStyle style = wb.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

	        //声明列对象
	        HSSFCell cell = null;

	        //创建标题
	        for(int i=0;i<title.length;i++){
	            cell = row.createCell(i);
	            cell.setCellValue(title[i]);
	            cell.setCellStyle(style);
	        }

	        //创建内容
	        for(int i=0;i<values.length;i++){
	            row = sheet.createRow(i + 1);
	            for(int j=0;j<values[i].length;j++){
	            	String value = values[i][j];
	                Boolean isNum = false;//data是否为数值型
	                Boolean isInteger=false;//data是否为整数
	                Boolean isPercent=false;//data是否为百分数
	                if (value != null || "".equals(value)) {
	                    //判断data是否为数值型
	                    isNum = value.matches("^(-?\\d+)(\\.\\d+)?$");
	                    //判断data是否为整数（小数部分是否为0）
	                    isInteger=value.matches("^[-\\+]?[\\d]*$");
	                    //判断data是否为百分数（是否包含“%”）
	                    isPercent= value.contains("%");
	                }
	                //如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
	                if (isNum && !isPercent) {
	                    HSSFDataFormat df = wb.createDataFormat(); // 此处设置数据格式
	                    if (value.length()<=10) {
//	                    	style.setDataFormat(df.getBuiltinFormat("#,#0"));//数据格式只显示整数
	                    	style.setDataFormat(df.getBuiltinFormat("#,##0.00"));//保留两位小数点
		                    // 设置单元格格式
		                    row.createCell(j).setCellStyle(style);
		                    // 设置单元格内容为double类型
		                    row.createCell(j).setCellValue(Double.parseDouble(value));
	                    }else {
		                	row.createCell(j).setCellStyle(style);
		                    // 设置单元格内容为字符型
		                    row.createCell(j).setCellValue(value);
	                    }
	                } else {
	                	row.createCell(j).setCellStyle(style);
	                    // 设置单元格内容为字符型
	                    row.createCell(j).setCellValue(value);
	                }
//	 
//	            	
//	            	
//	                //将内容按顺序赋给对应的列对象
//	                row.createCell(j).setCellValue(value);
	            }
	        }
	        return wb;
	    }
	      
	    
	   public static void main(String[] args) {
			try {
				InputStream is = new FileInputStream("D:/category_attr/1.xlsx");
				ExcelUtil excelUtil = new ExcelUtil();
				try {
					List<List<Object>>  list = excelUtil.getBankListByExcel(is, "a.xlsx");
					for(int i=0;i<list.size();i++) {
						System.out.print(list.get(i).get(0)+",");
						if(list.get(i).get(1) == null) {
							System.out.println(list.get(i).get(1)+",");
						}else {
							System.out.println(list.get(i).get(1)+"111,");
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
}
