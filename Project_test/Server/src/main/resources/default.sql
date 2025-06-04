UPDATE users
SET is_logged_in = FALSE;
---psql -h localhost -p 5432 -U postgres -d java -f "default.sql"
