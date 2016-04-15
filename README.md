# RestConnection
To use this plugin you have to add this plugin to yours dependecy 
and add dependecy to com.fasterxml.jackson.core.jackson-annotations.

Next step it's creating Object with Jackson Annotation like that:

public class Book {
	private long id;
	private String title;
	private String authors;

	@JsonCreator
	public Book(@JsonProperty("id") long id, @JsonProperty("title") String title,
			@JsonProperty("authors") String authors) {
		this.id = id;
		this.title = title;
		this.authors = authors;
	}

	public long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthors() {
		return authors;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", authors=" + authors + "]";
	}
}

Now you can create parametrized JSONRestConnection like:
RestConnection<Book> restConnect = new JSONRestConnection<Book>(Book.class);

You can post or get: object, list of object or your custom JSON String. 
