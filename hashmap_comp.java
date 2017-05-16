import java.io.*;
import java.util.*;

public class hashmap_comp 
{
	public static String key="",key1 = "";
	private static HashMap<String,String> map,map1;	
	
    public static void main(String[] args)throws Exception 
    {
    	String[] str = new String[100];
		String sourceinput = "";
		String datainput = "";
		String sourcekey = "";
		String datakey = "";
    	String matchedoutput = "";     	
    	
        map = new HashMap<String, String>();
        
        for(int i=0;i<args.length;i++)
	    {
			str[i]=args[i];			
		}   	
            
        for(int i=0;i<args.length;i++)
        {
        	if (str[i].compareTo("-si")==0)
        	{
        		sourceinput=str[i+1];        		
        	} 
        	if (str[i].compareTo("-di")==0)
        	{
        		datainput=str[i+1];        		
        	}
        	if (str[i].compareTo("-sk")==0)
        	{
        		sourcekey=str[i+1];        		
        	} 
        	if (str[i].compareTo("-dk")==0)
        	{
        		datakey=str[i+1];        		
        	}
        	if (str[i].compareTo("-o")==0)
        	{	
        		matchedoutput=str[i+1];
        	}         	
        }  
                
        BufferedReader in = new BufferedReader(new FileReader(sourceinput));        
        String line, line1 = "";      
        int input = Integer.parseInt(sourcekey);
        int input1 = Integer.parseInt(datakey);
        BufferedWriter bw = new BufferedWriter(new FileWriter(matchedoutput)); 
        
        while ((line = in.readLine()) != null) 
        {
        	String parts[] = line.split("\\|",-1);
            map.put(parts[input], parts[2]);               
        }
        
        map1 = new HashMap<String, String>();
        BufferedReader in1 = new BufferedReader(new FileReader(datainput));  
        
        while ((line1 = in1.readLine()) != null) 
        {
            String parts[] = line1.split("\\|",-1);            
            map1.put(parts[input1], parts[1]);
        }
        
        for (Map.Entry<String, String> e : map.entrySet()) 
        {    	    
        	key = e.getKey().toString();         
        	for (Map.Entry<String, String> e1 : map1.entrySet()) 
        	{
        		key1 = e1.getKey().toString();     	    
        		if(key.contains(key1))
        		{
        			//System.out.println("Matched data key:"+key1);
        			bw.write(key1+"\n");
        		}
        	}
        }
        in.close();
        in1.close();   
        bw.close();
    }
}