import java.sql.*;
import java.text.*;
import java.util.*;
//	import java.util.Date;

public class date1 
{
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mgmt_time";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "restdb";
	static int rs,rs1;	
	static int i;
    
	
	public static void main(String[] args) throws Exception
	{
		Connection con =getDBConnection();		
		getDate(con);	
		getMonth(con);
		getWeek(con);
	}
	
	private static Connection getDBConnection() 
	{
		Connection dbConnection = null;
        try 
        {
        	Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);            
            return dbConnection;
        } catch (SQLException e) {
        System.out.println(e.getMessage());
        } catch (Exception ex) {
        System.out.println(ex.getMessage());
        }
        return dbConnection;
	}
	
	public static String getDate(Connection con)
	{		
		try
        {         		     
			java.util.Date sysDate = new java.util.Date();
		    java.sql.Date sqlDate = new java.sql.Date(sysDate.getTime());
		    String q1="update datedim set dcode = ? WHERE dcode is not null;";
		    PreparedStatement ps1 = con.prepareStatement(q1);
		    ps1.setInt(1, 1000);
		    rs1=ps1.executeUpdate();
		    
        	String q= "update datedim set dcode = ? where datealone = ?";        	
            PreparedStatement ps = con.prepareStatement(q);
            
            for(i=0;i>=-9;i--)
			{    
            	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
        		Calendar cal = Calendar.getInstance();
        		cal.add(Calendar.DATE,i);
				sysDate = cal.getTime();					
			    sqlDate = new java.sql.Date(sysDate.getTime());	
			    ps.setInt(1,i*(-1) );
			    ps.setDate(2,sqlDate);			  
	            rs=ps.executeUpdate();
			}       	
            ps.close();        	
        }catch(Exception e){
        	System.out.println(e);
        }    
		return null;
	}
	
	public static String getMonth(Connection con)
	{		
		try
        {         		     
			java.util.Date sysDate = new java.util.Date();
			String q1="update datedim set mcode = ? where mcode is not null;";
		    PreparedStatement ps1 = con.prepareStatement(q1);
		    ps1.setInt(1, 1001);
		    rs1=ps1.executeUpdate();
		    String q="update datedim set mcode=? where month(datealone)=?;";
            PreparedStatement ps = con.prepareStatement(q);           
           
        	for(i=0;i>=-9;i--)
			{
        		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
        		Calendar cal = Calendar.getInstance();        		
        		cal.add(Calendar.MONTH,i); 
        		int month = cal.get(Calendar.MONTH)+1;        					   
	            ps.setInt(1,i*(-1) );
	            ps.setInt(2,month);
	            rs=ps.executeUpdate();	                 
			}   ps.close();  
        }catch(Exception e){
        	System.out.println(e);
        }    
		return null;
	}
	
	public static String getWeek (Connection con)
	{		
		try
        {         		     
			java.util.Date sysDate = new java.util.Date();
			String q1="update datedim set wcode = ? where wcode is not null;";
		    PreparedStatement ps1 = con.prepareStatement(q1);
		    ps1.setInt(1, 1002);
		    rs1=ps1.executeUpdate();
		    String q="update datedim set wcode=? where week(datealone)=?;";
            PreparedStatement ps = con.prepareStatement(q);          
           
        	for(i=0;i>=-10;i--)
			{
        		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
        		Calendar cal = Calendar.getInstance();        		
        		cal.add(Calendar.WEEK_OF_YEAR,i); 
        		int month = cal.get(Calendar.WEEK_OF_YEAR)-1;        						   
	            ps.setInt(1,i*(-1) );
	            ps.setInt(2,month);
	            rs=ps.executeUpdate();	                 
			}   ps.close(); 
        }catch(Exception e){
        	System.out.println(e);
        }    
		return null;
	}
	
}
