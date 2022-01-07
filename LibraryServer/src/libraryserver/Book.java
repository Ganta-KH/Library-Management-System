package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    int id_book;
    String title;
    String autor;
    long isbn;
    String category;
    String status;
    String bookState;
    Date purchaseDate;
    long orderNumber;  
    int bookValue;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public Book() {
        this.title = "";
        this.autor = "";
        this.isbn = 0;
        this.category = "";
        this.status = "";
        this.bookState = "";
        this.purchaseDate = null;
        this.orderNumber = 0;
        this.bookValue = 0;
    }

    //--------------------------------------------------------------------------------------------------------------------//
    
    public Book(int id_book, String title, String autor, long isbn, String category, String status, String bookState, Date purchaseDate, long orderNumber, int bookValue) {
        this.id_book = id_book;
        this.title = title;
        this.autor = autor;
        this.isbn = isbn;
        this.category = category;
        this.status = status;
        this.bookState = bookState;
        this.purchaseDate = purchaseDate;
        this.orderNumber = orderNumber;
        this.bookValue = bookValue;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public Book(int id_book, String title, String autor, long isbn, String category) {
        this.id_book = id_book;
        this.title = title;
        this.autor = autor;
        this.isbn = isbn;
        this.category = category;
    }

    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean titleIsThere(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT title FROM book";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (t.equalsIgnoreCase(rs.getString("title")))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in titleIsThere...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isbnIsThere(long isb, Connection connection) {
        try
        {
            String mysql_query = "SELECT isbn FROM book";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (isb == rs.getLong("isbn"))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is isbnIsThere...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isBookLoaned(int idBook, Connection connection) {
        try {
            String mysql_query = "SELECT id_bookDatabase FROM loanedbook";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (idBook == rs.getInt("id_bookDatabase")) {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is isBookLoaned...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String bookOldStatu(int idBook ,Connection connection) {
        String oldStatu = null;
        try {
            String mysql_query = "SELECT * FROM book WHERE id_bookDatabase = "+idBook;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (idBook == rs.getInt("id_bookDatabase")) {
                    oldStatu = rs.getString("statu");
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is isBookStatuLanORLate...");
        }
        return oldStatu;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isBookStatuLanORLate(String oldStatu, String newStatu,Connection connection) {
        if ((oldStatu.equals("loaned") && newStatu.equals("loaned")) || (oldStatu.equals("late") && newStatu.equals("late"))) {
            return true;
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean orderNumberIsThere(long order, Connection connection) {
        try
        {
            String mysql_query = "SELECT orderNumber FROM book";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (order == rs.getLong("orderNumber"))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in orderNumberIsThere...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isIdBookExist(int idBook, Connection connection) {
        String mysql_query = "SELECT * FROM book";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if(idBook == rs.getInt("id_book")) {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("error in isIdBookExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isThereBook(Book book, Connection connection) {
        try{
            String mysql_query = "SELECT * FROM book";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
              

            if (titleIsThere(book.title, connection) || isbnIsThere(book.isbn, connection) || orderNumberIsThere(book.orderNumber, connection)) {
                st.close();
                return true;
            }
            st.close();
            return false;
            
        } catch(SQLException e) {
            System.out.println("error in isThereBook...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void addBookToDataBase(Book book, Connection connection) {
        try
        {  
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String inPurchaseDate = format1.format(book.purchaseDate);
            
            String mysql_query = "INSERT INTO `book`(`id_book`, `title`, `autor`, `isbn`, `category`, `statu`, `purchaseDate`, `orderNumber`, `bookState`, `bookValue`) VALUES ("+book.id_book+", '"+book.title+"','"+book.autor+"',"+book.isbn+",'"+book.category+"','"+book.status+"','"+inPurchaseDate+"',"+book.orderNumber+",'"+book.bookState+"',"+book.bookValue+")";
            
            Statement st = connection.createStatement(); // create the java statement
            System.out.println(mysql_query);
            
            st.executeUpdate(mysql_query);

            st.close();               
        } catch (SQLException e)
        {
            System.out.println("error in addBookToDataBase...");
        } 
    }

    //--------------------------------------------------------------------------------------------------------------------//

    public void editBook(Book book, int idBook, Connection connection) {
        try{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String inPurchaseDate = format1.format(book.purchaseDate);
            
            String mysql_query = "UPDATE `book` SET `id_book`="+book.id_book+", `title`='"+book.title+"',`autor`='"+book.autor+"',`isbn`="+book.isbn+",`category`='"+book.category+"',`statu`='"+book.status+"',`purchaseDate`='"+inPurchaseDate+"',`orderNumber`="+book.orderNumber+" ,`bookState`= '"+book.bookState+"', `bookValue`="+book.bookValue+" WHERE id_book="+idBook;
            Statement st = connection.createStatement();
            System.out.println(mysql_query);
            st.executeUpdate(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("error in editBook...");
        }
    }

    //--------------------------------------------------------------------------------------------------------------------//
    
    public void deleteBook(int idBook, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `book` WHERE id_book = "+idBook;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close(); 
        } catch (SQLException e) {
            System.out.println("error in deleteBook");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getTitle(long isb, Connection connection) {
        String mysql_query = "SELECT * FROM book WHERE isbn = "+isb;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            rs.next();
            return rs.getString("title");
        } catch (Exception e) {
           System.out.println("error in getTitle...");
        }
        return null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalBooks(Connection connection) {
        String mysql_query = "SELECT * FROM book";
        int tBooks = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tBooks++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalBooks...");
        }
        return tBooks;
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String addAllToComboBoxResult(Connection connection) {
        String mysql_query = "SELECT COUNT(id_bookDatabase), category FROM book GROUP BY category ORDER BY COUNT(id_bookDatabase) DESC;";
        String result = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);

            while (rs.next()) {
                result += ":::"+rs.getString("category");
            } 
            st.close();
        } catch (SQLException ex) {
            System.out.println("can't add element in combobox...");
        }
        return result;
    }
          
    //--------------------------------------------------------------------------------------------------------------------//
        
    public String searchBookCatalogueR(String string, int searchIndex, String searchStatuType) {
        
            if (string.equals("")) {
                return "SELECT * FROM book WHERE statu LIKE '%"+searchStatuType+"%'";
            } else {
                if (searchIndex == 0) {
                    return "SELECT * FROM book WHERE title LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 1){
                    return "SELECT * FROM book WHERE autor LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 2) {
                    return "SELECT * FROM book WHERE isbn LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM book WHERE category LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM book WHERE id_book LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 5) {
                    return "SELECT * FROM book WHERE orderNumber LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 6) {
                    return "SELECT * FROM book WHERE purchaseDate LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 7) {
                    return "SELECT * FROM book WHERE bookState LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                }
            }
            return "SELECT * FROM book WHERE statu LIKE '%"+searchStatuType+"%'";
        }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchBookCatalogueResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfBook = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                
                while(rs.next() && i<k) {
                    numberOfBook++;
                    result[i] = numberOfBook+":::"+rs.getInt("id_book")+":::"+rs.getString("title")+":::"+rs.getString("autor")+":::"+rs.getLong("isbn")+":::"+rs.getString("category")+":::"+rs.getString("statu")+":::"+rs.getString("purchaseDate")+":::"+rs.getLong("orderNumber")+":::"+rs.getString("bookState")+":::"+rs.getInt("bookValue");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchBookCatalogueResult...");
            }
        return result;
    }
}

