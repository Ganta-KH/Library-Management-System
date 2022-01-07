package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class User {
    
    long id_user;
    String firstName;
    String lastName;
    String userType;
    String ban;
    Date banStart;
    Date banEnd;
    int penaltyValue;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public User() {
        this.id_user = 0;
        this.firstName = "";
        this.lastName = "";
        this.userType = "";
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public User(long id_user, String firstName, String lastName, String userType) {
        this.id_user = id_user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public User(long id_user, int penaltyValue) {
        this.id_user = id_user;
        this.penaltyValue = penaltyValue;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public User(long id_user, Date banStart, Date banEnd) {
        this.id_user = id_user;
        this.banStart = banStart;
        this.banEnd = banEnd;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isIdUserExist(long idUser, Connection connection) {
        String mysql_query = "SELECT * FROM users";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if(idUser == rs.getLong("id_user")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error in isUserExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isLastNameExist(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT * FROM users";

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
            String mysql_query = "SELECT * FROM users";

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
    
    public boolean isUserExist(User user, Connection connection) {
        try{
            String mysql_query = "SELECT * FROM book";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
              
            if (isIdUserExist(user.id_user, connection) || (isFirstNameExist(user.firstName, connection) && isLastNameExist(user.lastName, connection))) {
                return true;
            }
            return false;
            
        } catch(SQLException e) {
            System.out.println("error in isUserExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
   
    public boolean isUserLoaned(int idUser, Connection connection) {
        try {
            String mysql_query = "SELECT id_userDatabase FROM loanedbook UNION SELECT id_userDatabase FROM loaneddoc";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (idUser == rs.getInt("id_userDatabase")) {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is isUserLoaned...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void addUserToDataBase(User user, Connection connection) {
        try
        {            
            String mysql_query = "INSERT INTO `users`(`id_user`, `firstName`, `lastName`, `userType`, `copyCanLoan`, `bookLoaned`, `bookLate`, `docLoaned`, `docLate`, `banStart`, `banEnd`, `penaltyValue`) VALUES ('"+user.id_user+"', '"+user.firstName+"', '"+user.lastName+"', '"+user.userType+"', '"+getNumberOfCopyCanLoan(user.userType)+"', "+0+", "+0+", "+0+", "+0+", 'NULL', 'NULL', "+0+");";
            
            Statement st = connection.createStatement();
            System.out.println(mysql_query);
            
            st.executeUpdate(mysql_query);

            st.close();               
        } catch (SQLException e)
        {
            System.err.println(e);
            System.out.println("error in addUserToDataBase...");
        } 
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void editUserR(User user, long idUser, int databaseIdUser, Connection connection) {
        try{
            String mysql_query = "UPDATE `users` SET `id_user`="+user.id_user+", `firstName`='"+user.firstName+"',`lastName`='"+user.lastName+"',`userType`='"+user.userType+"', `copyCanLoan`= "+canLoanR(user.userType, databaseIdUser, connection)+", `bookLoaned`= "+getNumberOfBookLoaned(databaseIdUser, connection)+", `docLoaned`= "+getNumberOfDocLoaned(databaseIdUser, connection)+" WHERE id_user="+idUser+"";
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println("error in editUser...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void deleteUser(long idUser, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `users` WHERE id_user = "+idUser;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close(); 
        } catch (SQLException e) {
            System.out.println("error in deleteUser...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalUsers(Connection connection) {
        String mysql_query = "SELECT * FROM users";
        int tUsers = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tUsers++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalUsers...");
        }
        return tUsers;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getUserDatabaseIdR(long userId, Connection connection) {
        int id = 0;
        try {
            String mysql_query = "SELECT id_userDatabase FROM users WHERE id_user = '"+userId+"'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            rs.next();
            id = rs.getInt("id_userDatabase");
            st.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println("error in getUserDatabaseIdR...");
        }
        return id;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getBookDatabaseIdR(int bkReturnIdCata, Connection connection) {
        int id = 0;
        try {
            String mysql_query = "SELECT id_bookDatabase FROM book WHERE id_book = '"+bkReturnIdCata+"'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                id = rs.getInt("id_bookDatabase");
            }
            st.close(); 
        } catch (Exception e) {
            System.out.println("error in getBookDatabaseId...");
        }
        return id;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getDocDatabaseIdR(int bkReturnIdCata, Connection connection) {
        int id = 0;
        try {
            String mysql_query = "SELECT id_documentDatabase FROM document WHERE id_document = '"+bkReturnIdCata+"'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                id = rs.getInt("id_documentDatabase");
            }
            st.close(); 
        } catch (Exception e) {
            System.out.println("error in getDocDatabaseId...");
        }
        return id;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public int getNumberOfCopyCanLoan(String userType) {
        if (userType.equals("occasional user")) {
            return 1;
        } else if (userType.equals("subscriber")) {
            return 4;
        }
        return 8;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoanR(String userType, int databaseIdUser, Connection connection) {
        if (getNumberOfCopyCanLoan(userType) -  getNumberOfBookLoaned(databaseIdUser, connection) - getNumberOfDocLoaned(databaseIdUser, connection) < 0) {
            return 0;
        }
        return getNumberOfCopyCanLoan(userType) -  getNumberOfBookLoaned(databaseIdUser, connection) - getNumberOfDocLoaned(databaseIdUser, connection);
        
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoan(long idUser, Connection connection) {
        int copyCanLoan = 0;
        try {
            String mysql_query = "SELECT copyCanLoan FROM users WHERE id_user = '"+idUser+"'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                copyCanLoan = rs.getInt("copyCanLoan");
            }
            st.close(); 
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return copyCanLoan;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
       
    public int getNumberOfBookLoaned(long id, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM loanedbook GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while(rs.next()) {
                if (rs.getInt("id_userDatabase") == id) {
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfCopyLoaned...");
        }
        return 0;
    } 
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfBookLate(long id, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM latebook GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while(rs.next()) {
                if (rs.getInt("id_userDatabase") == id) {
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfBookLate...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfDocLate(long id, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM latedoc GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while(rs.next()) {
                if (rs.getInt("id_userDatabase") == id) {
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfDocLate...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
       
    public int getNumberOfDocLoaned(int id, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM loaneddoc GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while(rs.next()) {
                if (rs.getInt("id_userDatabase") == id) {
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfDocLoaned...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int nbrActiveUser(Connection connection) {
        int k = 0;
        try {
            String mysql_query = "SELECT id_userDatabase FROM loanedbook UNION SELECT id_userDatabase FROM loaneddoc";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while(rs.next()) {
                k++;
            }
            st.close();
        }  catch (Exception e) {
            System.out.println("error in nbrActiveUser...");
        }
        return k;
    }

    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getUserPenaltyValueR(long bkreturnUserID, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM users WHERE id_user = "+bkreturnUserID;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if(rs.next()) {
                return rs.getInt("penaltyValue");
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getUserPenaltyValue...");
        }
        return 0;
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchUsersCatalogueResult(String mysql_query, Connection connection) {
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
                    result[i] = numberOfUsers+":::"+rs.getLong("id_user")+":::"+rs.getString("firstName")+":::"+rs.getString("lastName")+":::"+rs.getString("userType")+":::"+rs.getString("ban")+":::"+rs.getInt("copyCanLoan")+":::"+getNumberOfBookLoaned(rs.getInt("id_userDatabase"), connection)+":::"+rs.getInt("bookLate")+":::"+getNumberOfDocLoaned(rs.getInt("id_userDatabase"), connection)+":::"+rs.getInt("docLate");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchUsersCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchUserCatalogueR(String string, int searchIndex) {
        
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
                    return "SELECT * FROM users WHERE userType LIKE '%"+string+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM users WHERE copyCanLoan LIKE '%"+string+"%'";
                } else if (searchIndex == 5) {
                    return "SELECT * FROM users WHERE bookLoaned LIKE '%"+string+"%'";
                } else if (searchIndex == 6) {
                    return "SELECT * FROM users WHERE docLoaned LIKE '%"+string+"%'";
                } else if (searchIndex == 7) {
                    return "SELECT * FROM users WHERE bookLate LIKE '%"+string+"%'";
                } else if (searchIndex == 8) {
                    return "SELECT * FROM users WHERE docLate LIKE '%"+string+"%'";
                }
            }
            return "SELECT * FROM users";
        }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchLoanUserCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM `users` WHERE `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
            } else {
                if (searchIndex == 1) {
                    return "SELECT * FROM users WHERE firstName LIKE '%"+string+"%' AND `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
                } else if (searchIndex == 2){
                    return "SELECT * FROM users WHERE lastName LIKE '%"+string+"%' AND `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
                } else if (searchIndex == 0) {
                    return "SELECT * FROM users WHERE id_user LIKE '%"+string+"%' AND `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM users WHERE userType LIKE '%"+string+"%' AND `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
                }
            }
            return "SELECT * FROM `users` WHERE `copyCanLoan` > 0 AND bookLate = 0 AND docLate = 0 AND banStart = 'NULL' AND penaltyValue = 0";
        }
}