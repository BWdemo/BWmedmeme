import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class csv_mysql_match 
{ 	
	public static String DrugName="",key = "";
	private static HashMap<String,String> map;
	
    public static void main(String[] args) throws IOException 
    {
    	String[] str = new String[100];
		String inputfilename = "";
		String top50drugfilename = "";
    	String outputfilename = ""; 
    	
    	BufferedReader br1 = null;
    	map = new HashMap<String, String>();
    	String line1= ""; 
		  	
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
        	if (str[i].compareTo("-i1")==0)
        	{
        		top50drugfilename=str[i+1];        		
        	}
        	if (str[i].compareTo("-o")==0)
        	{	
        		outputfilename=str[i+1];
        	}         	
        }    	
        
        String csvFile=inputfilename;
        String line;
       // String cvsSplitBy = "|";
        String city="";
        String headerLine; 
        
        br1 = new BufferedReader(new FileReader(top50drugfilename));
        
        while ((line1 = br1.readLine()) != null) 
        {
        	String[] thera = line1.split("\\|",-1);        	
        	map.put(thera[0].toLowerCase().trim(),thera[1]);        	
        }  
                
        try
        {		
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/citydrugs?useSSL=false", "root", "restdb"); 

            PreparedStatement stat = conn.prepareStatement("select  Prescriber, DrugName, sum(Counts2015) as Counts2015, sum(Counts2016) as Counts2016, sum(TotPaid2015) as TotPaid2015, sum(TotPaid2016) as TotPaid2016 from (select  Prescriber, DrugName, (case YEARS when 2015 then Counts else 0 end) as Counts2015, (case YEARS when 2016 then Counts else 0 end) as Counts2016, (case YEARS when 2015 then TotalPaid else 0 end) as TotPaid2015, (case YEARS when 2016 then TotalPaid else 0 end) as TotPaid2016 from (select Prescriber, DrugName, YEARS, count(Rf) as Counts, sum(TotPaid) as TotalPaid from city_finance,city_timedim where DateFilled=CALENDAR_DT  and Prescriber = ? group by Prescriber, DrugName, YEARS) a group by  Prescriber, DrugName, (case YEARS when 2015 then Counts else 0 end), (case YEARS when 2016 then Counts else 0 end), (case YEARS when 2015 then TotalPaid else 0 end), (case YEARS when 2016 then TotalPaid else 0 end)) a group by  Prescriber, DrugName");
	
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputfilename)); 
        	
            headerLine = br.readLine();
            while ((line = br.readLine()) != null) 
            {   
            	//String[] citydrugs = line.split(cvsSplitBy);
            	int i=line.indexOf('|');
            	city=line.substring(0,i);                
                //city=citydrugs[0];              
                doGet(city,conn,bw,stat); 
                //System.out.println("Drug:"+DrugName);
                //break;                  
            }  
            
            br.close();
            br1.close();
            bw.close();
            conn.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
                
    public static void doGet(String city, Connection conn, BufferedWriter bw, PreparedStatement stat) throws FileNotFoundException
    { 
    	String Prescriber = "";       
        String Counts2015 = "";
        String Counts2016 = "";
        String TotPaid2015 = "";
        String TotPaid2016 = ""; 
        
        try
        {  
        	stat.setString(1, city);           
            ResultSet rs = stat.executeQuery();
            
            while (rs.next())
            {
                Prescriber = rs.getString(1);
                DrugName = rs.getString(2);
                Counts2015 = rs.getString(3);
                Counts2016 = rs.getString(4);
                TotPaid2015 = rs.getString(5);
                TotPaid2016 = rs.getString(6);  
                for (Map.Entry entry : map.entrySet()) 
                {
                	key = entry.getKey().toString();        		   
        		    if(key.equals(DrugName.toLowerCase()))
                    {
                    	System.out.println("Match");
                    	bw.write(Prescriber+"|"+DrugName+"|"+Counts2015+"|"+Counts2016+"|"+TotPaid2015+"|"+TotPaid2016+"\n");
                    } 
        		    else
        		    	System.out.println("Not Match");        		   
        		}
                                      
            } 
            rs.close();      
        }
        catch (Exception e2){
        	e2.printStackTrace();
        }finally{}         
    }
}
       