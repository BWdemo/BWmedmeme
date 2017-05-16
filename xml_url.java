import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
 
public class xml_url 
{ 
	static String url = "https://maps.googleapis.com/maps/api/distancematrix/xml?units=imperial&";
	static String dur_val, dur_text, dis_val, dis_text = "";	
	static FileReader in;
	static BufferedReader br;
	static FileWriter fw;
	static BufferedWriter bw=null;
	static String line, url_new, url_replace;
	
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
    {      	
    	String[] var = new String[100];
    	String[] str = new String[100];	
    	
    	String inputfilename = "";    		
        String outputfilename = "";
        
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
        		outputfilename=str[i+1];
        	}         	
        } 
         	
    	try
    	{  	
    		in = new FileReader(inputfilename);
    		br = new BufferedReader(in);
    		line = br.readLine();
    		System.out.println("origin_address"+"|"+"destination_address"+"|"+"duration_value"+"|"+"duration_text"+"|"+"distance_value"+"|"+"distance_text");
    		fw = new FileWriter(outputfilename);
			bw = new BufferedWriter(fw);
			bw.append("origin_address"+"|"+"destination_address"+"|"+"duration_value"+"|"+"duration_text"+"|"+"distance_value"+"|"+"distance_text");
    		while (line!=null) 
    		{			
    			var=line.split("\\|");    			
    			String var1=var[0]+" "+var[1]+" "+var[2]+"|";
    			String var2=var[3]+" "+var[4]+" "+var[5]+"|";
    			String var3=var1+var2;     			    			
    			url_new=url+"origins="+var[0]+var[1]+var[2]+"&destinations="+var[3]+var[4]+var[5];
    			url_replace=url_new.replaceAll(" ", "%20");
    			//System.out.println("URL:"+url_replace);
    			line = br.readLine();
    			
    			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    			DocumentBuilder b = f.newDocumentBuilder();
    			Document doc = b.parse(url_replace);				
    			doc.getDocumentElement().normalize();
    			//System.out.println ("Root element: " + doc.getDocumentElement().getNodeName());			
    				
    			NodeList items = doc.getElementsByTagName("duration");
    			for (int i = 0; i < items.getLength(); i++)
    			{
    				Node n = items.item(i);
    				if (n.getNodeType() != Node.ELEMENT_NODE)
    					continue;
    				Element e = (Element) n;     
    				dur_val = e.getElementsByTagName("value").item(0).getTextContent();
    				dur_text = e.getElementsByTagName("text").item(0).getTextContent();				                                              
    			}
    				
    			NodeList items1 = doc.getElementsByTagName("distance");
    			for (int i = 0; i < items1.getLength(); i++)
    			{
    				Node n = items1.item(i);
    				if (n.getNodeType() != Node.ELEMENT_NODE)
    					continue;
    				Element e = (Element) n;   		
    				dis_val = e.getElementsByTagName("value").item(0).getTextContent();
    				dis_text = e.getElementsByTagName("text").item(0).getTextContent(); 				
    			}
    			System.out.println(var3+"|"+dur_val+"|"+dur_text+"|"+dis_val+"|"+dis_text);
    			bw.append('\n'+var3+dur_val+"|"+dur_text+"|"+dis_val+"|"+dis_text);
    		}  		
    	} catch (IOException ioe){
    		ioe.printStackTrace();
    	}
    	finally{
    		try{
    			if(bw!=null)
    				bw.close();
    		}catch(Exception ex){
    			System.out.println("Error in closing the BufferedWriter"+ex);
    		}in.close();
    	}
    }
}