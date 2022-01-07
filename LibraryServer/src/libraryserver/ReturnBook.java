package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnBook extends User{
    
    Date returnDate;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public ReturnBook() {
        this.returnDate = null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public ReturnBook(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void returnBookInDatabaseR(int bkreturnIdCata, long bkreturnUserID, String returnBookState, String bkreturnUserType, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `loanedbook` WHERE id_bookDatabase = "+getBookDatabaseIdR(bkreturnIdCata, connection)+" AND id_userDatabase = "+getUserDatabaseIdR(bkreturnUserID, connection);
            String mysql_query2 = "DELETE FROM `latebook` WHERE id_bookDatabase = "+getBookDatabaseIdR(bkreturnIdCata, connection);
            String mysql_query3 = "UPDATE `book` SET `statu`= 'on the shelf' , bookState = '"+returnBookState+"' WHERE id_bookDatabase = "+getBookDatabaseIdR(bkreturnIdCata, connection);
            String mysql_query4 = "UPDATE `users` SET `copyCanLoan`= "+canLoanBR(bkreturnUserID, bkreturnUserType, connection)+", `bookLoaned`= "+(getNumberOfBookLoaned(getUserDatabaseIdR(bkreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(bkreturnUserID, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            if (isBookInLateR(bkreturnIdCata, connection)) {
                st.executeUpdate("UPDATE `users` SET `bookLate`= "+(getNumberOfBookLate(getUserDatabaseIdR(bkreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(bkreturnUserID, connection));
                System.out.println("UPDATE `users` SET `bookLate`= "+(getNumberOfBookLate(getUserDatabaseIdR(bkreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(bkreturnUserID, connection));
            }
            st.executeUpdate(mysql_query2);
            System.out.println(mysql_query2);
            st.executeUpdate(mysql_query3);
            System.out.println(mysql_query3);
            st.executeUpdate(mysql_query4);
            System.out.println(mysql_query4);
            st.close();    
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in returnBookInDatabase...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isBookInLateR(int bkreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM latebook WHERE id_bookDatabase = "+getBookDatabaseIdR(bkreturnIdCata, connection);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                if (getBookDatabaseIdR(bkreturnIdCata, connection) == rs.getInt("id_bookDatabase")) {
                    st.close();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in isBookInLate...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getBookStateR(int bkreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM book WHERE id_book = "+bkreturnIdCata;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if(rs.next()) {
                return rs.getString("bookState");
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getBookState...");
        }
        return null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getBookValueR(int bkreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM book WHERE id_book = "+bkreturnIdCata;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if(rs.next()) {
                return rs.getInt("bookValue");
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getBookValue...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int penaltyR(int oldIndex, int newIndex, int bkreturnIdCata, Connection connection) {
        int p = 0;
        if (newIndex - oldIndex == 2) {
            p = (int) (getBookValueR(bkreturnIdCata, connection) * 0.5);
        } else if (newIndex - oldIndex == 3){
            p = (int) (getBookValueR(bkreturnIdCata, connection) * 0.6);
        } else if (newIndex - oldIndex == 4) {
            p = (int) (getBookValueR(bkreturnIdCata, connection) * 0.7);
        }
        return p;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setpenaltyR(long bkreturnUserID, int penalty, Connection connection) {
        try {
            int p = getUserPenaltyValueR(bkreturnUserID, connection) + penalty;
            String mysql_query = "UPDATE `users` SET `penaltyValue`= '"+p+"' WHERE id_user = "+bkreturnUserID;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in setpenalty...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void editBookReturnDateR(ReturnBook returnBook, int bkreturnIdCata, Connection connection) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String RDate = format1.format(returnBook.returnDate);
            
            String mysql_query = "UPDATE `loanedbook` SET `returnDate`= '"+RDate+"' WHERE id_bookDatabase = "+getBookDatabaseIdR(bkreturnIdCata, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in editBookReturnDate...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoanBR(long bkreturnUserID,String bkreturnUserType, Connection connection){
        if (canLoan(bkreturnUserID, connection) + 1 >= getNumberOfCopyCanLoan(bkreturnUserType)) {
            return getNumberOfCopyCanLoan(bkreturnUserType);
        }
        return canLoan(bkreturnUserID, connection) +1;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchReturnBookUserCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM `users` WHERE bookLoaned > 0";
            } else {
                if (searchIndex == 1) {
                    return "SELECT * FROM users WHERE firstName LIKE '%"+string+"%' AND bookLoaned > 0 ";
                } else if (searchIndex == 2){
                    return "SELECT * FROM users WHERE lastName LIKE '%"+string+"%' AND bookLoaned > 0";
                } else if (searchIndex == 0) {
                    return "SELECT * FROM users WHERE id_user LIKE '%"+string+"%' AND bookLoaned > 0";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM users WHERE userType LIKE '%"+string+"%' AND bookLoaned > 0";
                }
            }
            return "SELECT * FROM `users` WHERE bookLoaned > 0";
        }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchReturnBookCatalogueResult(String mysql_query, Connection connection) {
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
                    result[i] = numberOfBooks+":::"+rs.getInt("id_book")+":::"+rs.getString("title")+":::"+rs.getString("autor")+":::"+rs.getString("category")+":::"+rs.getString("statu")+":::"+rs.getString("bookState")+":::"+rs.getLong("isbn")+":::"+rs.getString("loadDate")+":::"+rs.getString("returnDate");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchReturnBookCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public String searchReturnBookCatalogueR(String string,long userID ,int searchIndex) {
            if (string.equals("")) {
                return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID;
            } else {
                if (searchIndex == 0) {
                    return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND title LIKE '%"+string+"%'";
                } else if (searchIndex == 1){
                    return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND autor LIKE '%"+string+"%'";
                } else if (searchIndex == 2) {
                    return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND id_book LIKE '%"+string+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND isbn LIKE '%"+string+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND category LIKE '%"+string+"%'";
                }
            }
            return "SELECT book.id_book, book.title, book.autor, book.category, book.statu, book.bookState, book.isbn, loanedbook.loadDate, loanedbook.returnDate FROM loanedbook JOIN book ON loanedbook.id_bookDatabase = book.id_bookDatabase JOIN users ON loanedbook.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID;
    }
}
