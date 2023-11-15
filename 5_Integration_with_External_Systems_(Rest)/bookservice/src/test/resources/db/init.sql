truncate table books cascade;
truncate table categories cascade;
SELECT setval('books_id_seq', 1, false);
SELECT setval('categories_id_seq', 1, false);
insert into categories(name)
values ('category');
insert into books (title, author_name, category_id)
values ('title', 'author', 1);