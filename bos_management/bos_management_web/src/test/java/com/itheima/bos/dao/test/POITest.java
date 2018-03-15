package com.itheima.bos.dao.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**  
 * ClassName:POITest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午5:25:59 <br/>       
 */
public class POITest {
    
    public static void main(String[] args) throws Exception {
      //读取文件  
        HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream("E:\\daxi.xls"));
      
        //读取工作簿
        HSSFSheet sheet=workbook.getSheetAt(0);
        
                
        
        for (Row row : sheet) {
            if (row.getRowNum()==0) {
                continue;
            }
            for (Cell cell : row) {
                //读取表格中的数据
                String value = cell.getStringCellValue();
                System.out.print(value+"\t");
            }
            
            System.out.println();
        }
        
        //关流
        workbook.close();
    }

}
  
