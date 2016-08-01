package Source;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;

import jxl.CellView;
import jxl.Hyperlink;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Excel_file {

  private WritableCellFormat timesBoldUnderline;
  private WritableCellFormat times;
  private String inputFile;
  private static List<String> data=new ArrayList<String>();
  
  
  
public void setOutputFile(String inputFile) {
  this.inputFile = inputFile;
  }




  public void write() throws IOException, WriteException {
    File file = new File(inputFile);
    WorkbookSettings wbSettings = new WorkbookSettings();

    wbSettings.setLocale(new Locale("en", "EN"));

    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
    workbook.createSheet("Report", 0);
    WritableSheet excelSheet = workbook.getSheet(0);
    createLabel(excelSheet);
    writeContent(excelSheet);

    workbook.write();
    workbook.close();
  }

  private void add_data(String s)
  {
          data.add(s);
  }
  
  
  private void createLabel(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    // create create a bold font with unterlines
    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
        UnderlineStyle.SINGLE);
    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);
    cv.setAutosize(true);

    // Write a few headers
    addCaption(sheet, 0, 0, "Website");
    addCaption(sheet, 1, 0, "Hubspot Code");
    

  }

  
  
  
  private void readContent(Sheet sheet)
  {
      int rows=sheet.getRows();
      for(int i=1;i<rows;i++)
      {
          Cell cell=sheet.getCell(0, i);
          data.add(cell.getContents());
          cell=sheet.getCell(1, i);
          data.add(cell.getContents());
      }
      
      int size=data.size();
      List<String> tmp=new ArrayList<>();
      tmp=data;
      data=new ArrayList<>();
      for(int i=0;i<size;i++)
      {
          if(!"".equals(tmp.get(i))) 
          {
              data.add(tmp.get(i));
          }
      }
      
      
  }
  
  
  
  private void writeContent(WritableSheet sheet) throws WriteException,
      RowsExceededException {
    // Write a few number
      int size=data.size();
    for (int i = 1; i <size; i += 2) {
      // First column
      addHyperlink(sheet, 0, i, data.get(i-1));
      // Second column
      addLabel(sheet, 1, i,data.get(i));
    }
    
  }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
    Label label;
    label = new Label(column, row, s, timesBoldUnderline);
    sheet.addCell(label);
  }


  private void addLabel(WritableSheet sheet, int column, int row, String s)throws WriteException, RowsExceededException {
    Label label;
    label = new Label(column, row, s, times);
    sheet.addCell(label);
  }

  private void addHyperlink(WritableSheet sheet, int column, int row, String s)throws WriteException, RowsExceededException {
    WritableHyperlink hl=null;
      try {
          hl = new WritableHyperlink(column, row,new URL(s));
      } catch (MalformedURLException ex) {
          Logger.getLogger(Excel_file.class.getName()).log(Level.SEVERE, null, ex);
      }
    sheet.addHyperlink(hl);
  }
  private void read_and_write_file() throws IOException
  {
      
      File File=new File(inputFile);
      Workbook w=null;
      try {
           w=Workbook.getWorkbook(File);
      } catch (BiffException ex) {
          Logger.getLogger(Excel_file.class.getName()).log(Level.SEVERE, null, ex);
      }
      Sheet s=w.getSheet(0);
      readContent(s);
      try {
          write();
      } catch (WriteException ex) {
          Logger.getLogger(Excel_file.class.getName()).log(Level.SEVERE, null, ex);
      }
      
  }
  
  
  public void create_excel(List<String> data) throws WriteException, IOException {
    Excel_file test = new Excel_file();
    test.setOutputFile("Hubspot_search_results.xls");
    Excel_file.data=data;
      try 
      {
          test.read_and_write_file();
      } catch (Exception e) 
      {
          test.write();
      }
  }
} 