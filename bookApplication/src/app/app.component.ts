import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

interface Book {
  id?: number;     // optional (important)
  title: string;
  author: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'BookApplicationj';

  private baseUrl = 'http://localhost:8080/books';

  
  books: any[] = [];

  selectedBook: Book = {
    title: '',
    author: ''
  };




  constructor(private http: HttpClient) {
    this.hitApi();
  }
  hitApi() {
    this.http.get('http://localhost:8080/books')
      .subscribe({
        next: (response: any) => {
          this.books = response;
          console.log("Books loaded:", this.books);
        },
        error: (err) => {
          console.error("Error fetching books:", err);
        }
      });
  }


  ngOnInit(): void {
    this.getAllBooks();
  }

  // -------------------------
  // GET /books
  // -------------------------
  getAllBooks() {
    this.http.get<any[]>(this.baseUrl)
      .subscribe({
        next: (data) => this.books = data,
        error: () => console.error('No books found')
      });
  }

  // -------------------------
  // GET /books/{id}
  // -------------------------
  getBookById(id: number) {
    this.http.get<any>(`${this.baseUrl}/${id}`)
      .subscribe({
        next: (data) => this.selectedBook = data,
        error: () => console.error('Book not found')
      });
  }

  // -------------------------
  // POST /books
  // -------------------------
  createBook() {
    
 const bookPayload = {
  title: this.selectedBook.title,
  author: this.selectedBook.author
};


    this.http.post<any>(this.baseUrl, bookPayload)
      .subscribe({
        next: (data) => {
          console.log('Book created', data);
          this.getAllBooks();
          this.resetForm();
        }
      });
  }

  // -------------------------
  // PUT /books/{id}
  // -------------------------
  updateBook(id: number) {
    const updatedBook = {
      title: 'Updated Title',
      author: 'Updated Author'
    };

    this.http.put<any>(`${this.baseUrl}/${id}`, updatedBook)
      .subscribe({
        next: () => {
          console.log('Book updated');
          this.getAllBooks();
        }
      });
  }

  // -------------------------
  // PATCH /books/{id}
  // -------------------------
  patchBook(id: number) {
    const partialUpdate = {
      title: 'Patched Title'
    };

    this.http.patch<any>(`${this.baseUrl}/${id}`, partialUpdate)
      .subscribe({
        next: () => {
          console.log('Book patched');
          this.getAllBooks();
        }
      });
  }

  // -------------------------
  // DELETE /books/{id}
  // -------------------------
  deleteBook(id: number) {
    this.http.delete(`${this.baseUrl}/${id}`)
      .subscribe({
        next: () => {
          console.log('Book deleted');
          this.getAllBooks();
        }
      });
  }
  
  // -------------------------
  // RESET FORM (IMPORTANT)
  // -------------------------
  resetForm() {
    this.selectedBook = {
      title: '',
      author: ''
    };
  }




}
