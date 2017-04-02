package org.bw.ru;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadAndUpload extends HttpServlet
{
    private static final long serialVersionUID = 1L;
   XSSFWorkbook workbook;

    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

                doPost(req, res);
        }
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {

    res.setContentType("text/html");
    PrintWriter pw = res.getWriter();

//PrintWriter pw = new PrintWriter(System.out);
//PrintWriter pw = new PrintWriter(System.out);

 if (!ServletFileUpload.isMultipartContent(req)) {
            // if not, we stop here


            pw.println("Error: Form must has enctype=multipart/form-data.");
            //writer.flush();
            return;
        }

    DiskFileItemFactory factory = new DiskFileItemFactory();
    factory.setSizeThreshold(THRESHOLD_SIZE);
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setFileSizeMax(MAX_FILE_SIZE);
    upload.setSizeMax(MAX_REQUEST_SIZE);

    String uploadPath = getServletContext().getRealPath("")
            + File.separator + UPLOAD_DIRECTORY;
 File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
            uploadDir.mkdir();
 }

 try {
// parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(req);

            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // String storeFile = uploadPath + File.separator + fileName;
                        // saves the file on disk
                       item.write(storeFile);

//FileInputStream file = new FileInputStream(new File(filePath));
FileInputStream file = new FileInputStream(storeFile);

 //Get the xlsx workbook
            workbook = new XSSFWorkbook (file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);


            //Get iterator to all cells of current row
            Iterator<Row> rowIterator = sheet.rowIterator();

         while(rowIterator .hasNext())
         {

             Row row = rowIterator.next();

            //For each row, iterate through each columns
            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
            pw.println("<html><body>");
            pw.println("<table border='1' width=80%");

           pw.println("<td width=80>");
 while(cellIterator.hasNext())
                {

                    Cell cell = cellIterator.next();


                    pw.println("</td>");
                    pw.println("<td width=80>");
                    switch(cell.getCellType())
                    {

                          case Cell.CELL_TYPE_BOOLEAN:

                          //pw.println(cell.getBooleanCellValue());
                          cell.getBooleanCellValue();
                          break;
case Cell.CELL_TYPE_NUMERIC:

                          //pw.println(cell.getNumericCellValue());
                          cell.getNumericCellValue();

                          break;
case Cell.CELL_TYPE_STRING:
                          //pw.println(cell.getStringCellValue());
                          cell.getStringCellValue();
                          break;

                    }
                    //pw.println("<td width=80>");

                    pw.println(cell);

                }

            pw.println("</br>");
            pw.println("</br>");
            pw.println("</td>");
               }
//FileOutputStream out = new FileOutputStream(new File(storeFile));
//  workbook.write(out);

                 file.close();
                 pw.close();
                //out.close();
 }

                    // saves the file on disk
                    //item.write(storeFile);
                }
            }
            req.setAttribute("message", "Upload has been done successfully!");
        } catch (Exception ex) {
            req.setAttribute("message", "There was an error: " + ex.getMessage());
        }
       // getServletContext().getRequestDispatcher("/message.jsp").forward(req, res);
    }
}







