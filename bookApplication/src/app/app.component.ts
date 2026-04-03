import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'BookApplicationj';
  books: any[] = [];
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


}
