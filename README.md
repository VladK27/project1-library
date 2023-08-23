# Library managment system

This application can help librarians manage readers and books. 

# Technology stack ☕:
 - Java
 - Hibernate
 - SQL
 - Thymeleaf
 
### Spring
 - Spring Core
 - Spring Boot
 - Spring Web 
 - Spring Data JPA
 - Spring Security

# Realized features:

 ## Crud operations:
  - View all readers and books
  - View all information about particular reader o book
  - Create new book and register new reader
  - Edit, delete book or readers
  - Set book to reader or return book to library

 ## Pagination, sorting and search:
  If there are too many elements on a page, they are ___paginated___.   
  Items can be ___sorted___ by their properties, such as title, year, and so on.  
  On a separate ___search___ page, an employee can find a book or reader by its properties.

 ## Authentication and authorization
  Employee can has role ___manager___ or ___admin___.  
  ___Manager___ can manage books and readers, register readers and add new books, edit them, set books to readers and so on.  
  ___Admin___ can register new __employees__ and see all of them.  
  This functionality is implemented using __Spring Security🛡️__

  ## Security
   - Implemented protection against Cross-Site Request Forgery (CSRF) using the CSRF token mechanism.

  ## Other
   - Custom error handling has been implemented. For this, a new controller has been created that accepts and processes requests with errors.
