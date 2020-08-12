package constants;
import java.sql.*; 
public class Tets {

	public static void main(String[] args) {
System.out.println("sdkjv");
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://139.59.7.72:3306/ivendgateway?autoReconnect=true","root","");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from operator_psp");  
			while(rs.next())  
			System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			con.close();  
			}catch(Exception e){ System.out.println(e);}  
			}  
	}

