package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {
    long id_admin;
    String userNameA;
    String firstName;
    String lastName;
    String adminType;
    String password;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public Admin() {
        this.id_admin = 0;
        this.userNameA = "";
        this.firstName = "";
        this.lastName = "";
        this.adminType = "";
        this.password = "";
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public Admin(long id_admin, String usernameA, String firstName, String lastName, String adminType, String password) {
        this.id_admin = id_admin;
        this.userNameA = usernameA;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adminType = adminType;
        this.password = password;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public Admin(long id_admin, String usernameA, String firstName, String lastName, String adminType) {
        this.id_admin = id_admin;
        this.userNameA = usernameA;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adminType = adminType;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isUsernameExist(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT * FROM admin";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (t.equalsIgnoreCase(rs.getString("username")))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in isUsernameExist...");
        }
        return false;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------//
    
    public boolean isIDExist(long id_admin, Connection connection) {
        try
        {
            String mysql_query = "SELECT id_admin FROM admin";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (id_admin == rs.getLong("id_admin")) {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in is isIDExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isLastNameExist(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT * FROM admin";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (t.equalsIgnoreCase(rs.getString("lastName")))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in isLastNameExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isFirstNameExist(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT * FROM admin";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (t.equalsIgnoreCase(rs.getString("firstName")))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in isFirstNameExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isAdminExist(Admin admin, Connection connection) {
        try{
            String mysql_query = "SELECT * FROM book";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
              
            if (isIDExist(admin.id_admin, connection) || isUsernameExist(admin.userNameA, connection) || (isFirstNameExist(admin.firstName, connection) && isLastNameExist(admin.lastName, connection))) {
                return true;
            }
            return false;
            
        } catch(SQLException e) {
            System.out.println("error in isAdminExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void addAdminToDataBase(Admin admin, Connection connection) {
        try
        {            
            String mysql_query = "INSERT INTO `admin`(`id_admin`, `username`, `firstName`, `lastName`, `pass`, `adminType`) VALUES ("+admin.id_admin+", '"+admin.userNameA+"','"+admin.firstName+"','"+admin.lastName+"', '"+admin.password+"', '"+admin.adminType+"')";
            
            Statement st = connection.createStatement(); // create the java statement
            System.out.println(mysql_query);
            
            st.executeUpdate(mysql_query);

            st.close();               
        } catch (SQLException e)
        {
            System.out.println("error in addAdminToDataBase...");
        } 
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void editAdmin(Admin admin, long idAdmin, Connection connection) {
        try{
            String mysql_query = "UPDATE `admin` SET `id_admin`="+admin.id_admin+", username = '"+admin.userNameA+"', `firstName`='"+admin.firstName+"',`lastName`='"+admin.lastName+"',`adminType`='"+admin.adminType+"' WHERE id_admin = "+idAdmin;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("error in editAdmin...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void deleteAdmin(long idAdmin, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `admin` WHERE id_admin = "+idAdmin;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close(); 
        } catch (SQLException e) {
            System.out.println("error in deleteAdmin...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getAdminType(int idAdmin, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM admin WHERE id_adminDatabase = "+idAdmin;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                return rs.getString("adminType");
            }
            st.close(); 
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getAdminType...");
        }
        return null;
    } 
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalAdmins(Connection connection) {
        String mysql_query = "SELECT * FROM admin";
        int tAdmins = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tAdmins++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalAdmins...");
        }
        return tAdmins;
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchAdminsCatalogueResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfAdmins = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                
                while(rs.next() && i<k) {
                    numberOfAdmins++;
                    result[i] = numberOfAdmins+":::"+rs.getLong("id_admin")+":::"+rs.getString("username")+":::"+rs.getString("firstName")+":::"+rs.getString("lastName")+":::"+rs.getString("adminType");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchAdminsCataloguePuniResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchAdminCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM admin";
            } else {
                if (searchIndex == 0) {
                    return "SELECT * FROM admin WHERE username LIKE '%"+string+"%'";
                } else if (searchIndex == 1){
                    return "SELECT * FROM admin WHERE id_admin LIKE '%"+string+"%'";
                } else if (searchIndex == 2) {
                    return "SELECT * FROM admin WHERE firstName LIKE '%"+string+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM admin WHERE lastName LIKE '%"+string+"%'";
                }
            }
            return "SELECT * FROM users";
        }
    
}

