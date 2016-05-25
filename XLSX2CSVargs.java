import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XLSX2CSVargs 
{
	static int TotalCnt = 0;
	
	static int TotalCountVal[] =  new int[300];
	static int TotalCountVal1[] =  new int[300];
	static int TotalCountVal2[] =  new int[300];
	static int TotalCountVal3[] =  new int[300];
	
	static HashSet<Integer> posNRxc = new HashSet<Integer>();
	static HashSet<Integer> posTRxc = new HashSet<Integer>();
	static HashSet<Integer> posNRxq = new HashSet<Integer>();
	static HashSet<Integer> posTRxq = new HashSet<Integer>();
	
	static TreeSet myTreeSet = new TreeSet();
	static TreeSet myTreeSet1 = new TreeSet();
	static TreeSet myTreeSet2 = new TreeSet();
	static TreeSet myTreeSet3 = new TreeSet();
	
	static String[] str = new String[100];
	
	static String inputfilename = "";
	static String outputCNRx = "";
	static String outputCTRx = "";
	static String outputQNRx = "";
	static String outputQTRx = "";
	
	static String searchstring = "";
	static String searchstring1 = "";
	static String searchstring2 = "";
	static String searchstring3 = "";
	
	
	private class SheetToCSV implements SheetContentsHandler 
	{		
		private boolean firstCellOfRow = false;
		private int currentRow = -1;
		private int currentCol = -1;
		
		private int NRxCArray[]=new int[300];
		private int countNRxSize=0;
		private int TRxCArray[]=new int[300];
		private int countTRxSize=0;
		private int NRxQArray[]=new int[300];
		private int qtyNRxSize=0;
		private int TRxQArray[]=new int[300];
		private int qtyTRxSize=0;
		private String s; 
		private Boolean accRow=false;
				        
		private void outputMissingRows(int number) 
		{
            for (int i=0; i<number; i++) 
            {
                for (int j=0; j<minColumns; j++) 
                {
                    //output.append(',');
                }
                //output.append('\n');
            }
        }

        public void startRow(int rowNum) 
        {
            // If there were gaps, output the missing rows
            outputMissingRows(rowNum-currentRow-1);
            // Prepare for this row
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;            
        }

        public void endRow(int rowNum) 
        {
            // Ensure the minimum number of columns
            for (int i=currentCol; i<minColumns; i++) 
            {
                //output.append(',');            	
            }
            //output.append('\n');            
        }
       
        public void cell(String cellReference, String formattedValue, XSSFComment comment) 
        {
        	int thisCol = (new CellReference(cellReference)).getCol();  
        	
        	s=cellReference.format(formattedValue);        	        	
        	String searchCNRx = searchstring;
        	String searchCTRx = searchstring1;
        	String searchQNRx = searchstring2;
        	String searchQTRx = searchstring3;
                		
            if (firstCellOfRow) 
            {
                firstCellOfRow = false;
                output.append("\n");
                output1.append("\n");
                output2.append("\n");
                output3.append("\n");
            } 
            else 
            {
                //output.append(',');
            }
                   
            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if(cellReference == null) 
            {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }                 
            
            // Did we miss any cells?
            int missedCols = thisCol - currentCol - 1;
            for (int i=0; i<missedCols; i++) 
            {
                 output.append(',');
                 output1.append(',');
                 output2.append(',');
                 output3.append(',');
            }
            currentCol = thisCol;
            
           // Number or string?
            try 
            {
                Double.parseDouble(formattedValue);
                
                if (thisCol<10)
                {
                	output.append(formattedValue);
                	output.append(',');
                	output1.append(formattedValue);
                	output1.append(',');
                	output2.append(formattedValue);
                	output2.append(',');
                	output3.append(formattedValue);
                	output3.append(',');
                }
                if(posNRxc.contains(thisCol))
            	{
                	output.append(formattedValue);
                	output.append(',');
                	if(accRow==false)
                		TotalCountVal[thisCol] += Integer.parseInt(formattedValue); 
            	}
                if(posTRxc.contains(thisCol))
                {
                		output1.append(formattedValue);
                		output1.append(',');
                		if(accRow==false)
                			TotalCountVal1[thisCol] += Integer.parseInt(formattedValue);                	
                } 
                if(posNRxq.contains(thisCol))
            	{
                	output2.append(formattedValue);
                	output2.append(',');
                	if(accRow==false)
                		TotalCountVal2[thisCol] += Integer.parseInt(formattedValue); 
            	}
                if(posTRxq.contains(thisCol))
                {
                		output3.append(formattedValue);
                		output3.append(',');
                		if(accRow==false)
                		TotalCountVal3[thisCol] += Integer.parseInt(formattedValue);                	
                }             	
            }            
            catch (NumberFormatException e) 
            {
            	if(formattedValue.compareTo("Accumulate Row")==0)
            	{            		
            		accRow=true;
            	} 
            	if (thisCol<10)
                {
                	 output.append(formattedValue);
                	 output.append(',');
                	 output1.append(formattedValue);
                	 output1.append(',');
                	 output2.append(formattedValue);
                	 output2.append(',');
                	 output3.append(formattedValue);
                	 output3.append(',');
                }
            	if(s.indexOf(searchCNRx)>=0)
            	{
            		NRxCArray[countNRxSize]=thisCol;
            		countNRxSize++;
            		posNRxc.add(thisCol); 
            		myTreeSet.addAll(posNRxc);            		 
            	}             	
            	if(posNRxc.contains(thisCol))
            	{
            		 output.append(formattedValue);
            		 output.append(',');     	   
            	} 
            	if(s.indexOf(searchCTRx)>=0)
            	{
            		TRxCArray[countTRxSize]=thisCol;
            		countTRxSize++;
            		posTRxc.add(thisCol); 
            		myTreeSet1.addAll(posTRxc);
            	}     	            	
            	if(posTRxc.contains(thisCol))
            	{
            		 output1.append(formattedValue);
            		 output1.append(',');     	   
            	} 
            	if(s.indexOf(searchQNRx)>=0)
            	{
            		NRxQArray[qtyNRxSize]=thisCol;
            		qtyNRxSize++;
            		posNRxq.add(thisCol); 
            		myTreeSet2.addAll(posNRxq);            		 
            	}     	            	
            	if(posNRxq.contains(thisCol))
            	{
            		 output2.append(formattedValue);
            		 output2.append(',');     	   
            	}   
            	if(s.indexOf(searchQTRx)>=0)
            	{
            		TRxQArray[qtyTRxSize]=thisCol;
            		qtyTRxSize++;
            		posTRxq.add(thisCol); 
            		myTreeSet3.addAll(posTRxq);            		 
            	}     	            	
            	if(posTRxq.contains(thisCol))
            	{
            		 output3.append(formattedValue);
            		 output3.append(',');     	   
            	}   
            }                	    
        }     
        
        public void headerFooter(String text, boolean isHeader, String tagName) 
        {
            // Skip, no headers or footers in CSV
        }
    }

	private final OPCPackage xlsxPackage;

    /** Number of columns to read starting with leftmost */
    private final int minColumns;

    /** Destination for data */
    private final PrintStream output;
    private final PrintStream output1;
    private final PrintStream output2;
    private final PrintStream output3;
 
    public XLSX2CSVargs(OPCPackage pkg, PrintStream output, PrintStream output1,PrintStream output2, PrintStream output3,int minColumns) 
    {
        this.xlsxPackage = pkg;
        this.output = output;
        this.output1=output1;
        this.output2 = output2;
        this.output3=output3;
        this.minColumns = minColumns;
    }
    
    public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler, 
    		InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException 
    {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try 
        {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
            int i = 0;
			output.append("\n");
			output1.append("\n");
			output2.append("\n");
			output3.append("\n");
			for(i=0;i<10;i++)
			{				
				output.append(','); 
				output1.append(','); 
				output2.append(','); 
				output3.append(','); 
            }
            Iterator itr =myTreeSet.iterator();
            while(itr.hasNext())
            {
            	Integer val=(Integer)itr.next();		  	
            	output.append(String.valueOf(TotalCountVal[val.intValue()]));
            	output.append(',');
            } 
            Iterator itr1 =myTreeSet1.iterator();
            while(itr1.hasNext())
            {
            	Integer val1=(Integer)itr1.next();		  	
            	output1.append(String.valueOf(TotalCountVal1[val1.intValue()]));
            	output1.append(',');
            } 
            Iterator itr2 =myTreeSet2.iterator();
            while(itr2.hasNext())
            {
            	Integer val2=(Integer)itr2.next();		  	
            	output2.append(String.valueOf(TotalCountVal2[val2.intValue()]));
            	output2.append(',');
            } 
            Iterator itr3 =myTreeSet3.iterator();
            while(itr3.hasNext())
            {
            	Integer val3=(Integer)itr3.next();		  	
            	output3.append(String.valueOf(TotalCountVal3[val3.intValue()]));
            	output3.append(',');
            }  
        }
        catch(ParserConfigurationException e) 
        {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    public void process()throws IOException, OpenXML4JException, ParserConfigurationException, SAXException 
    {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        //int index = 0;
        while (iter.hasNext()) 
        {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            //this.output.println();
            //this.output.println(sheetName + " [index=" + index + "]:");
            processSheet(styles, strings, new SheetToCSV(), stream);            
            stream.close();
            //++index;
        }
    }
    
    public static void main(String[] args) throws Exception 
    { 
    	for(int i=0;i<args.length;i++)
	    {
			str[i]=args[i];			
		}   	
            
        for(int i=0;i<args.length;i++)
        {
        	if (str[i].compareTo("-i")==0)
        	{
        		inputfilename=str[i+1];        		
        	}
        	if (str[i].compareTo("-o")==0)
        	{	
        		outputCNRx=str[i+1];
        	}
        	if (str[i].compareTo("-o1")==0)
        	{	
        		outputCTRx=str[i+1];
        	}
        	if (str[i].compareTo("-o2")==0)
        	{	
        		outputQNRx=str[i+1];
        	}
        	if (str[i].compareTo("-o3")==0)
        	{	
        		outputQTRx=str[i+1];
        	}
        	if (str[i].compareTo("-s")==0)
        	{	
        		searchstring=str[i+1];
        	}	
        	if (str[i].compareTo("-s1")==0)
        	{	
        		searchstring1=str[i+1];
        	}
        	if (str[i].compareTo("-s2")==0)
        	{	
        		searchstring2=str[i+1];
        	}	
        	if (str[i].compareTo("-s3")==0)
        	{	
        		searchstring3=str[i+1];
        	}       
        } 
        
        File xlsxFile = new File(inputfilename);       
        if (!xlsxFile.exists()) 
        {
            System.err.println("Not found or not a file: " + xlsxFile.getPath());
            return;
        }
                
        int minColumns = -1;
                
        // The package open is instantaneous, as it should be.
        OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
        PrintStream myconsole = new PrintStream(new File(outputCNRx));
        PrintStream myconsole1 = new PrintStream(new File(outputCTRx));
        PrintStream myconsole2 = new PrintStream(new File(outputQNRx));
        PrintStream myconsole3 = new PrintStream(new File(outputQTRx));
        XLSX2CSVargs xlsx2csv = new XLSX2CSVargs(p, myconsole, myconsole1, myconsole2, myconsole3, minColumns);
        xlsx2csv.process();
        p.close();			
    }
}