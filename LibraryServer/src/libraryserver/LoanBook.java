package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanBook extends User{
    long id_user;
    int id_book;
    Date loanDate;
    Date returnDate;
    
    public LoanBook() {
        this.id_user = 0;
        this.id_book = 0;
        this.loanDate = null;
        this.returnDate = null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public LoanBook(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfLoanDays(String userType) {
        if (userType.equalsIgnoreCase("occasional user")) {
            return 15;
        }
        return 30;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoanBR(String userType, int databaseIdUser, Connection connection){
        if (canLoanR(userType, databaseIdUser, connection) - 1 > 0) {
            return canLoanR(userType, databaseIdUser, connection) - 1;
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void loanBookInDatabaseR(int bkLoanIdCata, long bkLoanUserID, String bkLoanDate, LoanBook loanBook, int databaseIdUser, String userType, Connection connection) {
        try{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String RDate = format1.format(loanBook.returnDate);
            
            String mysql_query = "INSERT INTO `loanedbook`(`id_bookDatabase`, `id_userDatabase`, `loadDate`, `returnDate`) VALUES ('"+getBookDatabaseIdR(bkLoanIdCata, connection)+"', '"+getUserDatabaseIdR(bkLoanUserID, connection)+"', '"+bkLoanDate+"', '"+RDate+"')";
            String mysql_query2 = "UPDATE `book` SET `statu`= 'loaned' WHERE id_bookDatabase = "+getBookDatabaseIdR(bkLoanIdCata, connection);
            String mysql_query3 = "UPDATE `users` SET `copyCanLoan`= "+canLoanBR(userType, databaseIdUser, connection)+", `bookLoaned`= "+(getNumberOfBookLoaned(getUserDatabaseIdR(bkLoanUserID, connection), connection)+1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(bkLoanUserID, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.executeUpdate(mysql_query2);
            System.out.println(mysql_query2);
            st.executeUpdate(mysql_query3);
            System.out.println(mysql_query3);
            st.close();    
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in loanBookInDatabase...");
        }
    }     
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchLoanBookCatalogueResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfBooks = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                
                while(rs.next() && i<k) {
                    numberOfBooks++;
                    result[i] = numberOfBooks+":::"+rs.getInt("id_book")+":::"+rs.getString("title")+":::"+rs.getString("autor")+":::"+rs.getString("category")+":::"+rs.getLong("isbn");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchLoanBookCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public String searchLoanBookCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM book WHERE statu = 'on the shelf' ";
            } else {
                if (searchIndex == 0) {
                    return "SELECT * FROM book WHERE title LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 1){
                    return "SELECT * FROM book WHERE autor LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 2) {
                    return "SELECT * FROM book WHERE id_book LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM book WHERE isbn LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM book WHERE category LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                }
            }
            return "SELECT * FROM book WHERE statu = 'on the shelf' ";
        }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean checkLateBook(int idBook, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM latebook WHERE id_bookDatabase = "+idBook;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                if (idBook == rs.getInt("id_bookDatabase")) {
                    st.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error in checkLateBook...");
            System.err.println(e.toString());
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfLateBook(int idUser, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM latebook GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (rs.getInt("id_userDatabase") == idUser) {  
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfLateBook...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalLoandedBooks(Connection connection) {
        String mysql_query = "SELECT * FROM loanedbook";
        int tLBooks = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tLBooks++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalLoandedBooks...");
        }
        return tLBooks;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalLateBooks(Connection connection) {
        String mysql_query = "SELECT * FROM latebook";
        int tLBooks = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tLBooks++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalLateBooks...");
        }
        return tLBooks;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLateBook(Connection connection) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date returnD, today;
      
        try {
            today = format1.parse(format1.format(new Date()));
            
            String mysql_query = "SELECT * FROM `loanedbook`";
            Statement st = connection.createStatement();
            Statement st2 = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                returnD = format1.parse(rs.getString("returnDate"));
                if(today.compareTo(returnD) > 0) {                    
                    if (!checkLateBook(rs.getInt("id_bookDatabase"), connection)) {
                        st2.executeUpdate("INSERT INTO `latebook`(`id_bookDatabase`, `id_userDatabase`) VALUES ("+rs.getInt("id_bookDatabase")+","+rs.getInt("id_userDatabase")+");");
                        st2.executeUpdate("UPDATE `book` SET `statu` = 'late' WHERE id_bookDatabase = "+rs.getInt("id_bookDatabase"));
                        st2.executeUpdate("UPDATE `users` SET `bookLate`= "+getNumberOfLateBook(rs.getInt("id_userDatabase"), connection)+" WHERE id_userDatabase = "+rs.getInt("id_userDatabase"));
                    }
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println("error in setLateBook...");
        }
    }
}
