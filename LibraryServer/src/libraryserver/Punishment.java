package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Punishment{
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void penaltyUserPunishment(User user, Connection connection) {
        try{
            String mysql_query = "UPDATE `users` SET penaltyValue="+user.penaltyValue+" WHERE id_user="+user.id_user;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println("error in editUserPunishment...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void banUser(User user, Connection connection) {
        try {    
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String banStart = format1.format(user.banStart);
            String banEnd = format1.format(user.banEnd);

            String mysql_query = "UPDATE `users` SET ban='YES', banStart='"+banStart+"', banEnd='"+banEnd+"' WHERE id_user="+user.id_user;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("error in banUser...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void unbanUser(User user, Connection connection) {
        try {
            String mysql_query = "UPDATE `users` SET ban=null, banStart='NULL', banEnd='NULL' WHERE id_user="+user.id_user;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("error in unbanUser...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String isUserLate(int id, Connection connection) {
        boolean b = false;
        try {
            String mysql_query = "SELECT id_userDatabase FROM users WHERE (bookLate > 0 OR docLate > 0)";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (id == rs.getInt("id_userDatabase")) {
                    b = true;
                    break;
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in isUserLate...");
        }
        
        if (b) {
            return "late";
        }
        return "";
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int nbrLateUser(Connection connection){
        int nbrLateUser = 0;
        try {
            String mysql_query = "SELECT * FROM `users` WHERE ban IS NULL AND (bookLate > 0 OR docLate > 0) AND penaltyValue = 0";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                nbrLateUser++;
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in nbrLateUser...");
        }
        return nbrLateUser;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchUsersCataloguePuniResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfUsers = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                
                while(rs.next() && i<k) {
                    numberOfUsers++;
                    result[i] = numberOfUsers+":::"+rs.getLong("id_user")+":::"+rs.getString("firstName")+":::"+rs.getString("lastName")+":::"+isUserLate(rs.getInt("id_userDatabase"), connection)+":::"+rs.getString("ban")+":::"+rs.getString("banStart")+":::"+rs.getString("banEnd")+":::"+rs.getInt("penaltyValue");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchUsersCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void endBan(Connection connection) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date endBan, today;
      
        try {
            today = format1.parse(format1.format(new Date()));
            
            String mysql_query = "SELECT * FROM users";
            Statement st = connection.createStatement();
            Statement st2 = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (rs.getString("banEnd").equals("NULL")) {
                    continue;
                } else {
                    endBan = format1.parse(rs.getString("banEnd"));
                    if(today.compareTo(endBan) >= 0) {                    
                        st2.executeUpdate("UPDATE `users` SET ban=null, banStart='NULL', banEnd='NULL' WHERE id_userDatabase="+rs.getInt("id_userDatabase"));         
                    }
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println("error in endBan...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchUserCataloguePuniR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM users";
            } else {
                if (searchIndex == 1) {
                    return "SELECT * FROM users WHERE firstName LIKE '%"+string+"%'";
                } else if (searchIndex == 2){
                    return "SELECT * FROM users WHERE lastName LIKE '%"+string+"%'";
                } else if (searchIndex == 0) {
                    return "SELECT * FROM users WHERE id_user LIKE '%"+string+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM users WHERE banStart LIKE '%"+string+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM users WHERE banEnd LIKE '%"+string+"%'";
                } else if (searchIndex == 5) {
                    return "SELECT * FROM users WHERE penaltyValue LIKE '%"+string+"%'";
                }
            }
            return "SELECT * FROM users";
        }
}
